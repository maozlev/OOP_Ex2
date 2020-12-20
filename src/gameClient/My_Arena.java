package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class My_Arena {
    public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
    private static directed_weighted_graph _gg;
    private static List<CL_Agent> _agents;
    private static List<My_Pokemon> _pokemons;
    private List<String> _info;
    private static long time;
    private static Point3D MIN = new Point3D(0, 100,0);
    private static Point3D MAX = new Point3D(0, 100,0);

    public My_Arena() {;
        _info = new ArrayList<>();
    }
    private My_Arena(directed_weighted_graph g, List<CL_Agent> r, List<My_Pokemon> p) {
        _gg = g;
        setAgents(r);
        setPokemons(p);
    }

    public static void setPokemons(List<My_Pokemon> f) {
        _pokemons = f;
    }
    public static void setAgents(List<CL_Agent> f) {
        _agents = f;
    }
    public static void setGraph(directed_weighted_graph g) {_gg =g;}
    public static void setTime(long t) { time = t;}

    public long getTime() { return time;}
    public List<CL_Agent> getAgents() {return _agents;}
    public List<My_Pokemon> getPokemons() {return _pokemons;}

    public directed_weighted_graph getGraph() {return _gg; }

    public List<String> get_info() {
        return _info;
    }

//

    public static ArrayList<My_Pokemon> json2Pokemons(String fs) {
        ArrayList<My_Pokemon> ans = new ArrayList<>();
        try {
            JSONObject ttt = new JSONObject(fs);
            JSONArray ags = ttt.getJSONArray("Pokemons");
            for(int i=0;i<ags.length();i++) {
                JSONObject pp = ags.getJSONObject(i);
                JSONObject pk = pp.getJSONObject("Pokemon");
                int t = pk.getInt("type");
                double v = pk.getDouble("value");
                double x, y, z;
                String[] arr = (pk.getString("pos")).split(",");
                x = Double.parseDouble(arr[0]);
                y = Double.parseDouble(arr[1]);
                z = Double.parseDouble(arr[2]);
                geo_location m = new GeoLocation(x, y, z);
                My_Pokemon f = new My_Pokemon(m, t, v, null);
                ans.add(f);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
        return ans;
    }


    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {
        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if(dist>d1-EPS2) {ans = true;}
        return ans;
    }

    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
        geo_location src = g.getNode(s).getLocation();
        geo_location dest = g.getNode(d).getLocation();
        return isOnEdge(p,src,dest);
    }

    public static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if(type<0 && dest>src) {return false;}
        if(type>0 && src>dest) {return false;}
        return isOnEdge(p,src, dest, g);
    }

    public static edge_data updateEdge(My_Pokemon fr, directed_weighted_graph g) {
        edge_data ans = null;
        for (node_data v : g.getV()) {
            for (edge_data e : g.getE(v.getKey())) {
                boolean f = isOnEdge(fr.getLocation(), e, fr.getType(), g);
                if (f) {
                    fr.set_edge(e);
                    ans = e;
                }
            }
        }
        return ans;
    }

    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0=0,x1=0,y0=0,y1=0;
        boolean first = true;
        while(itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if(first) {
                x0=p.x(); x1=x0;
                y0=p.y(); y1=y0;
                first = false;
            }
            else {
                if(p.x()<x0) {x0=p.x();}
                if(p.x()>x1) {x1=p.x();}
                if(p.y()<y0) {y0=p.y();}
                if(p.y()>y1) {y1=p.y();}
            }
        }
        Range xr = new Range(x0,x1);
        Range yr = new Range(y0,y1);
        return new Range2D(xr,yr);
    }
    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        return new Range2Range(world, frame);
    }

    public static directed_weighted_graph load_graph(String s) {
        directed_weighted_graph g = new DWGraph_DS();
        try {
            JSONObject graph = new JSONObject(s);
            JSONArray edges=graph.getJSONArray("Edges");
            JSONArray nodes=graph.getJSONArray("Nodes");
            for (int i = 0; i < nodes.length() ; i++) {
                JSONObject n= nodes.getJSONObject(i);
                int key= n.getInt("id");
                node_data p= new NodeData(key);
                double x,y,z;
                String[] arr= (n.getString("pos")).split(",");
                x=Double.parseDouble(arr[0]);
                y=Double.parseDouble(arr[1]);
                z=Double.parseDouble(arr[2]);
                geo_location m= new GeoLocation(x,y,z);
                p.setLocation(m);
                g.addNode(p);
            }
            for (int i = 0; i <edges.length(); i++) {
                JSONObject e= edges.getJSONObject(i);
                int src = e.getInt("src");
                double w = e.getDouble("w");
                int dest = e.getInt("dest");
                g.connect(src,dest,w);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return g;
    }

    public static int load_num_of_agents(String s){
        int t=0;
        try {
            JSONObject o = new JSONObject(s);
            t= o.getJSONObject("GameServer").getInt("agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static void first_agents (int num_agents, List<My_Pokemon> pokemons, directed_weighted_graph g, game_service game, List<CL_Agent> agents) {
        for (int i = 0; i < num_agents; i++) {
            My_Arena.updateEdge(pokemons.get(i), g);
            CL_Agent tmp_agent;
            int src = pokemons.get(i).get_edge().getSrc();
            int dest = pokemons.get(i).get_edge().getDest();

            if(pokemons.get(i).getType() < 0 && src > dest) {
                game.addAgent(src);
                tmp_agent = new CL_Agent(g, src);
                agents.add(tmp_agent);
            }
            if(pokemons.get(i).getType() > 0 && src < dest) {
                game.addAgent(dest);
                tmp_agent = new CL_Agent(g, dest);
                agents.add(tmp_agent);
            }
        }
    }

    public static List<My_Pokemon> load_pokemon (String s) {
        List<My_Pokemon> list_of_pokemons = new LinkedList<>();
        try {
            JSONObject pokemon = new JSONObject(s);
            JSONArray pokemons = pokemon.getJSONArray("Pokemons");
            for (int i = 0; i < pokemons.length(); i++) {
                JSONObject p = pokemons.getJSONObject(i).getJSONObject("Pokemon");
                double value = p.getDouble("value");
                int type= p.getInt("type");
                double x, y, z;
                String[] arr = (p.getString("pos")).split(",");
                x = Double.parseDouble(arr[0]);
                y = Double.parseDouble(arr[1]);
                z = Double.parseDouble(arr[2]);
                geo_location m = new GeoLocation(x, y, z);
                My_Pokemon pp= new My_Pokemon(m,type,value);
                list_of_pokemons.add(pp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  list_of_pokemons;
    }

    public static List<CL_Agent> load_agents (String s,directed_weighted_graph g) {
        ArrayList<CL_Agent> list_of_agents = new ArrayList<>();
        try {
            JSONObject agent = new JSONObject(s);
            JSONArray agents = agent.getJSONArray("Agents");
            for (int i = 0; i < agents.length(); i++) {
                JSONObject a = agents.getJSONObject(i).getJSONObject("Agent");
                int id = a.getInt("id");
                int src= a.getInt("src");
                double x, y, z;
                CL_Agent aa= new CL_Agent(g, src,id);
                list_of_agents.add(aa);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_of_agents;
    }
}
