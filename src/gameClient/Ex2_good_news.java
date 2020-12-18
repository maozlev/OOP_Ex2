package gameClient;
import Server.Game_Server_Ex2;
import api.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Ex2_good_news implements Runnable {
    private static dw_graph_algorithms _g;
    private My_Pokemon mew;
    private static game_service game;


    public static void main(String[] args) {
        Thread client = new Thread(new Ex2_good_news());
        client.start();
    }

    @Override
    public void run() {
        int level = 0;
        game_service game = Game_Server_Ex2.getServer(level);
        directed_weighted_graph g = new DWGraph_DS();
        g = My_Arena.load_graph(game.getGraph());
        My_Arena.setGraph(g);
        _g = new DWGraph_Algo();
        _g.init(g);
        int num_agents = My_Arena.load_num_of_agents(game.toString());
        List<My_Pokemon> pokemons = My_Arena.json2Pokemons(game.getPokemons());
        pokemons.sort(My_Pokemon::compareTo);
        My_Arena.setPokemons(My_Arena.json2Pokemons(game.getPokemons()));
        List<CL_Agent> agents = new ArrayList<>();
        My_Arena.first_agents(num_agents, pokemons, g, game, agents);
        My_Arena.setAgents(agents);
        System.out.println(game.toString());

        game.startGame();
        long dt = 250;
        long end = game.timeToEnd();

        while (game.isRunning()) {
            My_Arena.moveAgents(game, g, num_agents);
            try {
                My_Arena.setTime(game.timeToEnd());
                if ((double)(4*(end - game.timeToEnd())) >= end) dt = 110;
                if ((double)(2*(end - game.timeToEnd())) >= end) dt = 80;
                if (((1.3)*(end - game.timeToEnd())) >= end) dt = 60;
                Thread.sleep(dt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static int nextNode(directed_weighted_graph g, int src, List<My_Pokemon> pokemons) {
        if (pokemons.isEmpty()) {
            pokemons = My_Arena.json2Pokemons(game.getPokemons());
            My_Arena.setPokemons(pokemons);
        }
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
        List<node_data> path = new LinkedList<>();
        edge_data e = My_Arena.updateEdge(mewtwo,g);
        System.out.println(e.toString());
        path = _g.shortestPath(src,e.getSrc());
        path.add(g.getNode(e.getDest()));

        if (path.size() == 1) return -1;

        return path.remove(1).getKey();
    }

}
