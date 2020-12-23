import api.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Java Unit Testing for Exercise 2 in Object Oriented Programming Course
 */
class Test_Graph {

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
     * TEST -> MC:
     * 1) |V| = 10 -> MC = 10
     * 2) |V| = 10, |E| = 9, -> MC = 19
     * 3) |V| = 10, |E| = 9, -> MC = 19
     * 4) |V| = 10, |E| = 9, -> MC = 28
     * 5) |V| = 6, |E| = 5 -> MC = 35
     */
    @Test
    public void TEST1() {
        /* Check addNode MC */
        node_data p;
        for(int i=0; i<10; i++) {
            p = new NodeData(i);
            g.addNode(p);
        }
        assertEquals(g.getMC(), 10);

        /* Check Connect MC */
        for(int i=0; i<9; i++)
            g.connect(i, i+1, i+1);
        assertEquals(g.getMC(), 19);

        /* Check Connect MC Again - same Weights!!! */
        for(int i=0; i<9; i++)
            g.connect(i, i+1, i+1);
        assertEquals(g.getMC(), 19);

        /* Check Connect MC Again - different Weights!!! */
        for(int i=0; i<9; i++)
            g.connect(i, i+1, i+2);
        assertEquals(g.getMC(), 28);

        /* Check RemoveNode MC */
        for(int i=6; i<10; i++)
            g.removeNode(i);
        assertEquals(g.getMC(), 35);
    }

    /**
     * TEST -> NodeSize
     * 1) |V| = 6
     * 2) |V| = 6 -> Remove Nodes which doesn't exist.
     * 3) |V| = 3 -> Remove 3 Nodes (3 Remain)
     */
    @Test
    public void TEST2() {
        directed_weighted_graph g = graph_creator(6, 0, 1);
        assertEquals(6, g.nodeSize());

        for(int i=7; i<10; i++)
            g.removeNode(i);
        assertEquals(6, g.nodeSize());

        for(int i=3; i<7; i++)
            g.removeNode(i);
        assertEquals(3, g.nodeSize());
    }

    /**
     *  TEST -> EdgeSize
     *  1) |V| = 10, |E| = 9
     *  2) |V| = 9, |E| = 8 (Remove node 2 -> 1 edges removed)
     *  3) |V| = 9, |E| = 4 (Remove 4 edges)
     */
    @Test
    public void TEST3() {
        directed_weighted_graph g = graph_creator(10, 0, 1);
        for(int i=0; i<9; i++)
            g.connect(i, i+1, i+1);
        assertEquals(9, g.edgeSize());

        g.removeNode(2);// Remove just 1 edge 2-->3
        assertEquals(8, g.edgeSize());

        for(int i=5; i<9; i++)
            g.removeEdge(i, i+1);
        assertEquals(4, g.edgeSize());

    }


    /**
     *  TEST -> getV
     *  1) |V| = 1000, |E| = 0 -> Check all the nodes (Not Null)
     */
    @Test
    public void TEST4() {
        directed_weighted_graph g = graph_creator(1000, 0, 1);
        Collection<node_data> v = g.getV();
        for (node_data n : v) {
            assertNotNull(n);
        }
    }

    /**
     * TEST -> Connect
     * 1) No nodes -> try to connect 1-2 || *** FAIL ***
     * 2) Add node 1 -> |V| = 1 -> try to connect 1-2 ->  return null
     * 3) |V| = 1 -> try to connect 1-1 -> return null
     * 4) |V| = 2 -> connect 1-2 -> weight = 1
     * 5) |V| = 2, |E| = 1 (edge 1-2) -> connect 1-2 again with weight = 4 -> Update the weight to 4
     */
    @Test
    public void TEST5() {
        directed_weighted_graph g = new DWGraph_DS();
        g.connect(1, 2, 1);

        try {
            assertNull(g.getEdge(1, 2)); // Null Pointer Exception
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        node_data p = new NodeData(1);
        g.addNode(p);
        g.connect(1, 2, 1);

        try {
            assertNull(g.getEdge(1, 2)); // Null Pointer Exception
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        g.connect(1, 1, 1);
        try {
            assertNull(g.getEdge(1, 1)); // Null Pointer Exception
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        node_data q = new NodeData(2);
        g.addNode(q);

        g.connect(1, 2, 1);
        double r= g.getEdge(1, 2).getWeight();
        assertEquals(r,1);

        g.connect(1, 2, 4);
        r= g.getEdge(1, 2).getWeight();
        assertEquals(r, 4); // Updated the weight in connect
    }

    /**
     * check if all nodes in the graph are strongly connected.
     */
    @Test
    void TEST6(){
        assertTrue(_g.isConnected()); // Empty graph should return true
        node_data p;
        for (int i = 0; i <10 ; i++) {
            p= new NodeData(i);
            g.addNode(p);
        }
        for (int i = 1; i <g.nodeSize() ; i++) {
            g.connect(i-1,i,2);
        }
        assertFalse(_g.isConnected()); // This graph is not connected
        for (int i = g.nodeSize()-1; i >0 ; i--) {
            g.connect(i,i-1,1);
        }
        assertTrue(_g.isConnected()); // This graph is connected
    }

    /**
     * Check copy, save and load:
     * 1. Copy a graph and check equals with the original.
     * 2. Save the graph and check equals.
     * 3. Load and check equals.
     */
    @Test
    public void TEST7() {
        g = graph_creator(7, 9, 9);
        _g.init(g);
        // Check the copy function by create new graph and let him to be copy
        directed_weighted_graph g1;
        g1 = _g.copy();
        assertEquals(g, g1); // Check if the copy equals to original
        dw_graph_algorithms _g2 = new DWGraph_Algo();
        directed_weighted_graph g2 = new DWGraph_DS();
        _g2.init(g2);
        assertTrue(_g.save("g")); // Check if the graph saved
        assertTrue(_g2.load("g.json")); // Check if the graph can be loaded
        g2=_g2.copy();
        assertEquals(g, g2); // Check if the load function works
    }

    /**
     * TEST -> ShortestPath & ShortestPathDist
     * Create a connected graph (line) -> Edge 1-2 : Weight 2 | Edge 2-3 : Weight 3 ...
     * 1) Check that all the paths => not null!
     * 2) The shortest path is the edge connected between two nearby nodes
     * 3) Check the path from each node to the starting node -> The distance is the sum of the weights (edges)
     * 4) Check that the distance from node 0-6 = 21
     * 5) Check that the distance from node 0-8 = 36
     * 6) Check node 0-8 path (0->1->2->3->4->5->6->7->8)
     * 7) Connect node 0-11 and node 10-11 -> Distance from node 0-8 is now equals to distance node 0-6 = 21
     * 8) Check node 0-8 NEW path (0->11->10->9->8)
     *
     * If there's no path -> Null Pointer Exception
     */
    @Test
    public void TEST8() {
        directed_weighted_graph g1 = graph_creator(11, 0, 1);
        dw_graph_algorithms ag1 = new DWGraph_Algo();
        ag1.init(g1);
        for(int i=0; i<10; i++)
            g1.connect(i, i+1, i+1);

        for(int i=0; i<g1.nodeSize()-1; i++) {
            assertNotNull(ag1.shortestPath(i, i+1));
            assertEquals(ag1.shortestPathDist(i, i+1), i+1);
            assertEquals(ag1.shortestPathDist(0, i), _count+=i);
        }

        assertEquals(ag1.shortestPathDist(0, 6), 21);
        assertEquals(ag1.shortestPathDist(0, 8), 36);

        List<Integer> myList = new ArrayList<>();
        for(int i=0; i<9; i++)
            myList.add(i);
        assertTrue(checkPath(ag1.shortestPath(0, 8), myList));

        g1.connect(0, 11, 1);
        g1.connect(10, 11, 1);

        assertEquals(ag1.shortestPathDist(0, 8), ag1.shortestPathDist(0, 6), 21); // Distance: 21

        List<Integer> myList2 = new ArrayList<>();
        myList2.add(0);
        for(int i=11; i>7; i--)
            myList2.add(i);
        assertTrue(checkPath(ag1.shortestPath(0, 8), myList2));
    }

    /**
     * private function to check ShortestPath
     * @param list - the returned list from ShortestPath
     * @param index - list of integers (Correct path)
     * @return boolean -> True/False
     */
    private static boolean checkPath(List<node_data> list, List<Integer> index) {
        if(list.size() == index.size())
            for(int i=0; i<list.size(); i++)
                if(list.get(i).getKey() != index.get(i))
                    return false;
        return true;
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