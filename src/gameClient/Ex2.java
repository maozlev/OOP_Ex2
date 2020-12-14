package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ex2 implements Runnable{
    private static dw_graph_algorithms _g;

    public static void main(String[] args) {
        int level_number = 0;
        game_service game = Game_Server_Ex2.getServer(level_number);
        System.out.println(game.getGraph());
        System.out.println(game.getPokemons());
        _g = load_graph(game.getGraph());


//
//        game.addAgent(0);
//        System.out.println(game.getAgents());
//
//        game.startGame();
//
//        while (game.isRunning()) {
//            System.out.println(game.chooseNextEdge(0, 5));
//            System.out.println(game.move());
//
//        }
    }

    @Override
    public void run() {
//        int scenario_num = 0;
//        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
//        //  int id = 999;
//        //  game.login(id);
//        _g.load(game.getGraph());
//
//        String g = game.getGraph();
//        String pks = game.getPokemons();
//
//        init(game);
//
//        game.startGame();
//        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
//        int ind=0;
//        long dt=100;
//
//        while(game.isRunning()) {
//            moveAgants(game, gg);
//            try {
//                if(ind%1==0) {_win.repaint();}
//                Thread.sleep(dt);
//                ind++;
//            }
//            catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//        String res = game.toString();
//
//        System.out.println(res);
//        System.exit(0);
    }

    private static int nextNode(directed_weighted_graph g, int src) {
        return 0; // need to use shortestpath(src, CL_Pokemon c.get_edge().getDest()) in order to place the agent
    }

    private static dw_graph_algorithms load_graph (String s) {
        try {
            JSONObject graph = new JSONObject(s);
            JSONArray edges=graph.getJSONArray("Edges");
            JSONArray nodes=graph.getJSONArray("Nodes");
            directed_weighted_graph g = new DWGraph_DS();
            _g = new DWGraph_Algo();
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
            _g.init(g);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return _g;
    }
}
