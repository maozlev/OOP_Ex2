package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.List;

/**
  This class represents the main functionality of the game-server, allowing a User the following:
  1. Run a game with ID of the player and Scenario number;
  2. MoveAgents();
  3. NextNode();
  4. chooseNextEdge(int agentID, int destNodeID); // main algorithm
  5. CatchEm() - Wake up near to a pokemon

  @author Asif Rot && Maoz Lev
 */
public class Ex2 implements Runnable {
    private static Frame _win;
    private static List<node_data> path;
    private static HashMap<My_Pokemon, Boolean> pokadoor;
    public static Thread client;
    public static int id;
    public static long dt;
    public static int scenario_num;
    public static double grade = 0;

    public static void main(String[] a) {

        client = new Thread(new Ex2());

        if (a.length == 2) {
            Ex2 login = new Ex2();
            int scenario_num1 = Integer.MIN_VALUE, id1 = Integer.MIN_VALUE;
            boolean res = false;
            try {
                scenario_num1 = Integer.parseInt(a[1]);
                id1 = Integer.parseInt(a[0]);
                res = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid id, please try again");
            }
            if (res) {
                login.setScenario_num(scenario_num1);
                login.setID(id1);
                client.start();
            }
        } else {
            Panel login = new Panel();
            login.setVisible(true);

        }
    }

    /**
     * Function to run the game
     */
    @Override
    public void run() {
        game_service game = null;
        try {
            game = Game_Server_Ex2.getServer(scenario_num);
            game.login(id);
        } catch (Exception e) {
            System.out.println("Invalid id, please try again");
        }

        if (game == null) {
            System.out.println("Invalid type");
            return;
        }
        String g = game.getGraph();
        directed_weighted_graph gg = My_Arena.load_graph(g);

        init(game);

        game.startGame();

        _win.setTitle("Level " + scenario_num + ": Gotta catch 'Em all");
        dt = 300;
        long end = game.timeToEnd();

        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                My_Arena.setTime(game.timeToEnd());
                if (dt == 300) {
                    if ((double) (4 * (end - game.timeToEnd())) >= end) dt = 110;
                    if ((double) (2 * (end - game.timeToEnd())) >= end) dt = 80;
                    if (((1.3) * (end - game.timeToEnd())) >= end) dt = 60;
                }
                _win.repaint();
                Thread.sleep(dt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge to catch pokemons,
     * each agent go to the nearest pokemon who is next to him using the shortest path
     * algorithm on the graph.
     *
     * @param game - game service
     * @param gg   - the graph we run on
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> agents = My_Arena.getAgents(lg, gg);
        My_Arena.setAgents(agents);
        String fs = game.getPokemons();
        List<My_Pokemon> ffs = My_Arena.json2Pokemons(fs);
        My_Arena.setPokemons(ffs);
        grade = 0;
        dt = 300;
        pokadoor = new HashMap<>();
        for (My_Pokemon p : ffs)
            pokadoor.put(p, false);
        for (CL_Agent ag : agents) {
            int id = ag.getID();
            int dest;
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (!ag.isMoving()) {
                dest = nextNode(game, gg, src, ffs,ag);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
            grade+= ag.getValue();
        }

    }

    /**
     * Implementation of getting the next node, using the shortest path function from graph algo.
     *
     * @param game     - game service
     * @param g        - graph we run on
     * @param src      - source node
     * @param pokemons - List of pokemons
     * @return the next node the agent should go to
     */
    private static int nextNode(game_service game, directed_weighted_graph g, int src, List<My_Pokemon> pokemons,CL_Agent ag) {
        if (pokemons.isEmpty()) {
            pokemons = My_Arena.json2Pokemons(game.getPokemons());
            My_Arena.setPokemons(pokemons);
        }
        dw_graph_algorithms _g = new DWGraph_Algo();
        _g.init(g);
        My_Pokemon mew;
        My_Pokemon mewtwo = null;
        double dist = Double.MAX_VALUE;
        for (My_Pokemon pokemon : pokemons) {
            mew = pokemon;
            edge_data e = My_Arena.updateEdge(mew, g);
            double dist1 = _g.shortestPathDist(src, e.getSrc());
            if (pokadoor.get(mew) == null) {
                pokadoor = new HashMap<>();
                pokadoor.put(mew, false);
            }
            if (dist1 == 0 && dist1 < dist && !pokadoor.get(mew)) {
                if (mew == null) {
                    pokemons = My_Arena.json2Pokemons(game.getPokemons());
                    My_Arena.setPokemons(pokemons);
                    return nextNode(game, g, src, pokemons,ag);
                }
                dist = dist1;
                mewtwo = mew;
                pokadoor.replace(mewtwo, true);
            }
            if (dist1 > 0 && dist1 < dist && !pokadoor.get(mew)) {
                if (mew == null) {
                    pokemons = My_Arena.json2Pokemons(game.getPokemons());
                    My_Arena.setPokemons(pokemons);
                    return nextNode(game, g, src, pokemons,ag);
                }
                dist = dist1;
                mewtwo = mew;
                pokadoor.replace(mewtwo, true);
            }
        }
        if (mewtwo == null) {
            pokemons = My_Arena.json2Pokemons(game.getPokemons());
            My_Arena.setPokemons(pokemons);
            return nextNode(game, g, src, pokemons,ag);
        }

        edge_data e = My_Arena.updateEdge(mewtwo, g);
        ag.setPokemonTarget(mewtwo);
        path = _g.shortestPath(src, e.getSrc());
        path.add(g.getNode(e.getDest()));
        if(ag.get_curr_edge() == mewtwo.get_edge()) {
            if (mewtwo.getLocation().distance(ag.getLocation()) < CL_Agent.EPS)
            dt = (long) (mewtwo.get_edge().getWeight() * CL_Agent.EPS);
        }
        return path.remove(1).getKey();
    }

    /**
     * Initialize the game for the first time:
     * Add pokemons to the graph.
     * Add the agents close as much as we can near to the pokemon.
     * Create the frame
     *
     * @param game - the game service
     */
    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = My_Arena.load_graph(g);
        My_Arena _ar = new My_Arena();
        path = new LinkedList<>();
        My_Arena.setGraph(gg);
        My_Arena.setPokemons(My_Arena.json2Pokemons(fs));
        _win = new Frame("Level " + scenario_num + ": Gotta catch 'Em all");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();

        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            ArrayList<My_Pokemon> cl_fs = My_Arena.json2Pokemons(game.getPokemons());
            pokadoor = new HashMap<>();
            for (My_Pokemon cl_f : cl_fs) {
                pokadoor.put(cl_f, false);
                My_Arena.updateEdge(cl_f, gg);
            }
            for (int a = 0; a < rs; a++) {
                int ind = a % cl_fs.size();
                My_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) nn = c.get_edge().getSrc();
                game.addAgent(nn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting ID of the player
     * @param id - Player's ID
     */
    public void setID(int id) {
        Ex2.id = id;
    }


    /**
     * Setting scenario of the game
     * @param scenario_num  is scenario number of the game [0, 23]
     *                     although the player can type any scenario number he wants
     *                      and then the server deal with it
     */
    public void setScenario_num(int scenario_num) {
        Ex2.scenario_num = scenario_num;
    }
}

