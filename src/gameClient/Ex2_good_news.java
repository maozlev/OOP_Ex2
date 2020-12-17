package gameClient;
import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Ex2_good_news implements Runnable {
    private static directed_weighted_graph g;
    private static dw_graph_algorithms _g;
    private My_Arena arena;
    private My_Pokemon mew;
    private game_service game;
    private List<node_data> path;
    private List<My_Pokemon> pokemons;
    private List<CL_Agent> agents;


    public static void main(String[] args) {
        Thread client = new Thread(new Ex2_good_news());
        client.start();
    }

    @Override
    public void run() {
        int level = 0;
        game_service game = Game_Server_Ex2.getServer(level);
        arena = new My_Arena();
        g = new DWGraph_DS();
        _g = new DWGraph_Algo();
        g = My_Arena.load_graph(game.getGraph(), g);
        _g.init(g);
        arena.setGraph(g);
        int num_agents = My_Arena.load_num_of_agents(game.toString());
        System.out.println(game.toString());
        pokemons = My_Arena.json2Pokemons(game.getPokemons());
        agents = new ArrayList<>();
        pokemons.sort(My_Pokemon::compareTo);
        arena.setPokemons(My_Arena.json2Pokemons(game.getPokemons()));
        My_Arena.first_agents(num_agents, pokemons, g, game, agents);

        game.startGame();
        while (true /*game.isRunning()*/) {
            String move = game.move();
            agents = My_Arena.getAgents(move, g);
            agents= My_Arena.load_agents(game.getAgents(),g);
            arena.setAgents(agents);
            arena.setPokemons(pokemons);
            pokemons = My_Arena.json2Pokemons(game.getPokemons());
            pokemons = My_Arena.load_pokemon(game.getPokemons());
            System.out.println(pokemons.toString());

            CL_Agent ag;
            for (int i = 0; i < num_agents; i++) {
                ag=agents.get(i);
                int dest = ag.getNextNode();
                int src = ag.getSrcNode();
                int id = ag.getID();
                double v = ag.getValue();
                if (dest == -1) {
                    try {
                        dest = nextNode(g, src, dest, pokemons);
                        game.chooseNextEdge(id, dest);

                        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private int nextNode(directed_weighted_graph g, int src, int dest, List<My_Pokemon> pokemons) {
        if (pokemons.isEmpty()) {
            pokemons = My_Arena.json2Pokemons(game.getPokemons());
            pokemons = My_Arena.load_pokemon(game.getPokemons());
            arena.setPokemons(pokemons);
        }
        My_Pokemon mew = pokemons.remove(0);
        path = new LinkedList<>();
        edge_data e = My_Arena.updateEdge(mew,g);
        System.out.println(mew.get_edge().toString());
        if (mew.getType() < 0 && src > dest) {
            path = _g.shortestPath(src,e.getDest());
        }
        if (mew.getType() > 0 && src < dest) {
            path = _g.shortestPath(dest, e.getSrc());
        }
        if (path.size() == 1) return -1;

        return path.remove(1).getKey();
    }

}
