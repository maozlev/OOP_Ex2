import java.util.*;

import Server.Game_Server_Ex2;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import gameClient.*;
import api.*;

public class Test_Game {
    private static Random _rnd = null;
    public static int _count = 0;
    public static directed_weighted_graph g;
    public static dw_graph_algorithms _g;


    @BeforeEach
    public void declareGraph() {
        g = new DWGraph_DS();
        _g= new DWGraph_Algo();
        _g.init(g);
    }

    /**
     * Check load_graph: load a graph and check if equals with the original
     */
    @Test
    public void TEST_Load_Graph() {
        g = graph_creator(7, 9, 9);
        _g.init(g);
        // Check the load_graph function by create new graph and let him to be loaded
        directed_weighted_graph g1;
        g1 = My_Arena.load_graph(_g.toString());
        assertEquals(g, g1); // Check if the copy equals to original
    }

    /**
     * In this test, we check another function - IsOnEdge - Which checks if the pokemons are on the edge
     * If they are, it means they are loaded correctly in the game and it updates the edge
     */
    @Test
    public void TEST_UpdateEdge() {
        game_service game = Game_Server_Ex2.getServer(0);
        String g1 = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = My_Arena.load_graph(g1);
        My_Arena.setGraph(gg);
        My_Arena.setPokemons(My_Arena.json2Pokemons(fs));
        ArrayList<My_Pokemon> cl_fs = My_Arena.json2Pokemons(game.getPokemons());

        assertNotNull(My_Arena.updateEdge(cl_fs.get(0),gg));
    }

    /**
     * Test Checks if the next node for the agent equals to the setting
     */
    @Test
    public void Test_GetNextNode() {
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.connect(0,1,1);
        CL_Agent agent = new CL_Agent(g,0);
        agent.setNextNode(1);
        assertEquals(agent.getNextNode(), 1);
    }

    public static directed_weighted_graph graph_creator(int v_size, int e_size, int seed) {
        directed_weighted_graph g = new DWGraph_DS();
        _rnd = new Random(seed);
        node_data p;
        for(int i=0;i<v_size;i++) {
            p = new NodeData(i);
            g.addNode(p);
        }

        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        return (int)v;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        return d*dx+min;
    }

    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an  Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(directed_weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodes = new node_data[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
}
