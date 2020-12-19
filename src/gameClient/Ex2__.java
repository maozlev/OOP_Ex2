package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2__ implements Runnable {
    private static Frame _win;
    private static My_Arena _ar;
    private static List<node_data> path;

    public static void main(String[] a) {
        Thread client = new Thread(new Ex2__());
        client.start();
    }

    @Override
    public void run() {
        int scenario_num = 0;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        System.out.println(g);
        String pks = game.getPokemons();
        System.out.println(pks);
        directed_weighted_graph gg = My_Arena.load_graph(g);
        init(game);

        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=100;

        while(game.isRunning()) {
            moveAgants(game, gg);
            try {
                if(ind%1==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }
    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        My_Arena.setAgents(log);
        String fs =  game.getPokemons();
        List<My_Pokemon> ffs = My_Arena.json2Pokemons(fs);
        My_Arena.setPokemons(ffs);
        for (CL_Agent ag : log) {
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(game, gg, src, ffs);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }
    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(game_service game, directed_weighted_graph g, int src, List<My_Pokemon> pokemons) {
        if (pokemons.isEmpty()) {
            pokemons = My_Arena.json2Pokemons(game.getPokemons());
            My_Arena.setPokemons(pokemons);
        }
        dw_graph_algorithms _g = new DWGraph_Algo();
        _g.init(g);
        My_Pokemon mew;
        My_Pokemon mewtwo = null;
        double dist= Double.MAX_VALUE;
        for (My_Pokemon pokemon : pokemons) {
            mew = pokemon;
            edge_data e = My_Arena.updateEdge(mew, g);
            double dist1 = _g.shortestPathDist(src, e.getSrc());
            if (dist1 == 0 && dist1 < dist) {
                dist = dist1;
                mewtwo = mew;
            }
            if (dist1 > 0 && dist1 < dist) {
                dist = dist1;
                mewtwo = mew;
            }
        }
        edge_data e = My_Arena.updateEdge(mewtwo,g);
        System.out.println(e.toString());

        if (path.size() <= 1) {
        path = _g.shortestPath(src,e.getSrc());
        path.add(g.getNode(e.getDest()));
        System.out.println(path.toString());
        }

        return path.remove(1).getKey();
    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = My_Arena.load_graph(g);
        _ar = new My_Arena();
        path = new LinkedList<>();
        My_Arena.setGraph(gg);
        My_Arena.setPokemons(My_Arena.json2Pokemons(fs));
        _win = new Frame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
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
}
