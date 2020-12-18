package gameClient;

import Server.Game_Server_Ex2;
import api.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable{
    private static directed_weighted_graph g;
    private static dw_graph_algorithms _g;
    private static List<My_Pokemon> list_of_pokemons;
    private static List<CL_Agent> list_of_agents;
    private HashMap<Integer, Boolean> vis;
    private static My_Arena arena;
    private static game_service game;
    private static Frame _win;

    public static void main(String[] args) {
        Thread client = new Thread(new Ex2());
        client.start();
    }
    @Override
    public void run() {
        int level_number = 0, number_of_agents = 0;
        game_service game = Game_Server_Ex2.getServer(level_number);
//        System.out.println(game.toString());
        g = new DWGraph_DS();
        _g = new DWGraph_Algo();
        g = My_Arena.load_graph(game.getGraph());
        _g.init(g);
        arena = new My_Arena();
        number_of_agents = My_Arena.load_num_of_agents(game.toString());
        list_of_pokemons = My_Arena.load_pokemon(game.getPokemons());
        list_of_pokemons.sort(My_Pokemon::compareTo);
//        System.out.println(game.getGraph());

        int j = 0;
        for (node_data n : g.getV()) {
            for (edge_data e : g.getE(n.getKey())) {
                if (j == list_of_pokemons.size()) break;
                geo_location pos = list_of_pokemons.get(j).getLocation();
                int type = list_of_pokemons.get(j).getType();
                if (My_Arena.isOnEdge(pos, e, type, g)) {
                    if (j < number_of_agents)
                        game.addAgent(e.getSrc());
                    j++;
                }
            }
        }
        My_Arena.load_agents(game.getAgents(), g);
        init(game);

        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());

        int ind = 0;
        long dt = 100;
        System.out.println("old " + list_of_pokemons.toString());
        while (true /*game.isRunning()*/) {
            game.move();
            list_of_pokemons = new ArrayList<>();
            list_of_pokemons = My_Arena.load_pokemon(game.getPokemons());
            list_of_agents = new ArrayList<>();
            list_of_agents = My_Arena.load_agents(game.getAgents(), g);
            System.out.println("update " + list_of_pokemons.toString());
            j = 0;
            CL_Agent ag;
            for (int i = 0; i < number_of_agents; i++) {
                ag = list_of_agents.get(i);
                int src = ag.getSrcNode();
                int dest = ag.getNextNode();
                int id = ag.getID();
                double v = ag.getValue();
                if (dest == -1) {
                    dest = nextNode(g, src, dest);
                    game.chooseNextEdge(id, dest);
                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
                }
                //j++;
                try {
                    if (ind % 1 == 0) {
                        _win.repaint();
                    }
                    Thread.sleep(dt);
                    ind++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            String res = game.toString();
//            System.out.println(res);
//            System.exit(0);
        }
    }


    private static int nextNode(directed_weighted_graph g, int src, int dest) {
        _g.init(g);
        if (list_of_pokemons.isEmpty()) {
            list_of_pokemons = My_Arena.json2Pokemons(game.getPokemons());
            arena.setPokemons(list_of_pokemons);
        }
        My_Pokemon mew = list_of_pokemons.remove(0);
        List<node_data> ls = new LinkedList<>();
        edge_data e = My_Arena.updateEdge(mew,g);

        if (mew.getType() < 0 && src > dest) {
            ls = _g.shortestPath(src,e.getDest());
        }
        if (mew.getType() > 0 && src < dest) {
            ls = _g.shortestPath(dest, e.getSrc());
        }


        if(ls.size()==1) return -1;
        return ls.remove(1).getKey();
        }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        arena = new My_Arena();
        arena.setGraph(gg);
        arena.setPokemons(My_Arena.json2Pokemons(fs));
        _win = new Frame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(arena);

        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<My_Pokemon> cl_fs = My_Arena.json2Pokemons(game.getPokemons());
            for (My_Pokemon cl_f : cl_fs) {
                My_Arena.updateEdge(cl_f, gg);
            }
            for(int a = 0;a<rs;a++) {
                int ind = a%cl_fs.size();
                My_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }

//    private static void moveAgents(game_service game, directed_weighted_graph gg) {
//        String lg = game.move();
//       // List<CL_Agent> log = My_Arena.getAgents(lg, gg, log);
//        arena.setAgents(log);
//        String fs = game.getPokemons();
//        List<My_Pokemon> ffs = My_Arena.json2Pokemons(fs);
//        arena.setPokemons(ffs);
//        for (CL_Agent ag : log) {
//            int id = ag.getID();
//            int dest = ag.getNextNode();
//            int src = ag.getSrcNode();
//            double v = ag.getValue();
//            if (dest == -1) {
//                dest = nextNode(gg, src, dest);
//                game.chooseNextEdge(ag.getID(), dest);
//                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);

                /*
                for (node_data n : g.getV()) {
                for (edge_data e : g.getE(n.getKey())) {
                    geo_location pos = list_of_pokemons.get(j).getLocation();
                    int type = list_of_pokemons.get(j).getType();
                    if (My_Arena.isOnEdge(pos, e, type, g)) {
                        for (int i = 0; i < list_of_agents.size(); i++) {
                            CL_Agent ag = log.get(i); //list_of_agents.get(i);
                            int id = ag.getID();
                            int dest = ag.getNextNode();
                            int src = ag.getSrcNode();
                            double v = ag.getValue();
                            if (dest == -1) {
                                dest = nextNode(g, src, e.getDest());
                                if (dest == -1) {
                                    // load_pokemon(game.getPokemons());
                                    src = ag.getSrcNode();
                                    // v = ag.getValue();
                                    dest = nextNode(g, src, e.getDest());
                                }
                                game.chooseNextEdge(ag.getID(), dest);
                                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest );
                 */
//            }
//        }
//    }



    private static int load_num_of_agents(String s){
        int t=0;
        try {
            JSONObject o = new JSONObject(s);
            t= o.getJSONObject("GameServer").getInt("agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
    }


    private static directed_weighted_graph load_graph (String s) {
        g = new DWGraph_DS();
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

    private static void load_agents (String s) {
        list_of_agents = new ArrayList<>();
        try {
            JSONObject agent = new JSONObject(s);
            JSONArray agents = agent.getJSONArray("Agents");
            for (int i = 0; i < agents.length(); i++) {
                JSONObject a = agents.getJSONObject(i).getJSONObject("Agent");
                int id = a.getInt("id");
                int src= a.getInt("src");
                double x, y, z;
                CL_Agent aa= new CL_Agent(g,src,id);
                list_of_agents.add(aa);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void load_pokemon (String s) {
        list_of_pokemons = new ArrayList<>();
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
    }

}

