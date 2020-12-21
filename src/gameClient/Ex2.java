package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.sun.jdi.request.ExceptionRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.List;

public class Ex2 implements Runnable {
    private static Frame _win;
    private static My_Arena _ar;
    private static Panel panel;
    private static List<node_data> path;
    private static List<CL_Agent> agents;
    private static HashMap<My_Pokemon, Boolean> pokadoor;
    public static Thread client;
    public static int id;
    public static long dt;
    public static int scenario_num;

    public static void main(String[] a) {

        client = new Thread(new Ex2());

        if(a.length==2) {
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
        }
        else {
            Panel login = new Panel();
            login.setVisible(true);

        }
    }

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
//        String pks = game.getPokemons();

        init(game);

        game.startGame();

        _win.setTitle("Level " + scenario_num + ": Gotta catch 'Em all");
        dt = 250;
        long end = game.timeToEnd();

        while(game.isRunning()) {
            moveAgents(game, gg);
            // dt = isCloseToPokemon()? 20 : 120;
            try {
                My_Arena.setTime(game.timeToEnd());
                if (dt == 250) {
                    if ((double) (4 * (end - game.timeToEnd())) >= end) dt = 110;
                    if ((double) (2 * (end - game.timeToEnd())) >= end) dt = 80;
                    if (((1.3) * (end - game.timeToEnd())) >= end) dt = 60;
                }
                _win.repaint();
                Thread.sleep(dt);
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
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        agents = Arena.getAgents(lg, gg);
        List <CL_Agent> agents1 = new ArrayList<>(agents);
        My_Arena.setAgents(agents);
        String fs =  game.getPokemons();
        List<My_Pokemon> ffs = My_Arena.json2Pokemons(fs);
        My_Arena.setPokemons(ffs);
        pokadoor = new HashMap<>();
        for (My_Pokemon p:ffs)
            pokadoor.put(p,false);

        for (CL_Agent ag : agents) {
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(game, gg, src, ffs);
                for (My_Pokemon p: ffs) {
                    runBarry(p, gg, agents1); // changes dt if pokemon is on edge
                }
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }

    /**
     * walk implementation using the shortest path function from graph algo.
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
                    return nextNode(game, g, src, pokemons);
                }
                dist = dist1;
                mewtwo = mew;
                pokadoor.replace(mewtwo, true);
            }
            if (dist1 > 0 && dist1 < dist && !pokadoor.get(mew)) {
                if (mew == null) {
                    pokemons = My_Arena.json2Pokemons(game.getPokemons());
                    My_Arena.setPokemons(pokemons);
                    return nextNode(game, g, src, pokemons);
                }
                dist = dist1;
                mewtwo = mew;
                pokadoor.replace(mewtwo, true);
            }
        }
        if (mewtwo == null) {
            pokemons = My_Arena.json2Pokemons(game.getPokemons());
            My_Arena.setPokemons(pokemons);
            return nextNode(game,g,src, pokemons);
        }

        edge_data e = My_Arena.updateEdge(mewtwo, g);
        path = _g.shortestPath(src, e.getSrc());
        path.add(g.getNode(e.getDest()));
        return path.remove(1).getKey();
    }

    /**
     * initialize the game for the first time:
     * add pokemons to the graph.
     * add the agents close as much as we can next to pokemon.
     * Create the frame
     *
     * @param game - the game service
     */

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = My_Arena.load_graph(g);
        _ar = new My_Arena();
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
            //cl_fs.sort(My_Pokemon::compareTo);
            for (My_Pokemon cl_f : cl_fs) {
                pokadoor.put(cl_f, false);
                My_Arena.updateEdge(cl_f, gg);
            }
            for(int a = 0;a<rs;a++) {
                int ind = a%cl_fs.size();
                My_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) nn = c.get_edge().getSrc();
                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }

    public void setID(int id) {
        Ex2.id = id;
    }

    public void setScenario_num(int scenario_num) {
        Ex2.scenario_num=scenario_num;
    }

    private static void runBarry (My_Pokemon p, directed_weighted_graph g, List<CL_Agent> agents) {
        for (CL_Agent agent : agents) {
            if (agent.get_curr_edge() == p.get_edge())
                if (p.getLocation().distance(agent.getLocation()) < 0.00001) {
                    dt = (long) (p.get_edge().getWeight() * 0.00001);
                }
        }
    }

    private boolean isCloseToPokemon() {
        for(CL_Agent ag : _ar.getAgents()){
            for(My_Pokemon p : _ar.getPokemons()) {
                if (ag.get_curr_edge() == p.get_edge() &&
                        ag.getLocation().distance(p.getLocation()) < 0.001 * ag.getSpeed())
                    return true;
            }
        }
        return false;
    }
}
