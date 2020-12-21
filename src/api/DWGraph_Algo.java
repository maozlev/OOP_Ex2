package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph g;
    private HashMap<Integer, Boolean> vis;
    private int[] low;
    private int count;
    private Stack<Integer> stack;
    private List<List<Integer>> Scc;
    private HashMap<Integer, Double> weights;

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g - the graph
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return the graph
     */
    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * @return - a copied graph
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph gn = new DWGraph_DS();

        for (node_data n : this.g.getV()) {
            gn.addNode(n); // Adding the nodes to the copied grpah
        }

        for (node_data n1 : this.g.getV()) {
            for (edge_data n2 : this.g.getE(n1.getKey())) {
                gn.connect(n1.getKey(), n2.getDest(), g.getEdge(n1.getKey(), n2.getDest()).getWeight()); // Connecting the nodes to their neighbors
            }
        }
        return gn;
    }

    /**
     * Tarjan algorithm goes over a directed graph,
     * and produces a partition of the graph's vertices into the graph's strongly connected components
     * The main idea is to use DFS algorithm, with DFS, we will visit every node of the graph
     * exactly once, declining to revisit any node that has already been visited.
     * Thus, the collection of search trees is a spanning forest of the graph.
     * The strongly connected components will be recovered as certain subtrees of this forest.
     */
    private void Tarzan() {
        int V = g.nodeSize();
        low = new int[V];
        vis = new HashMap<>();
        stack = new Stack<>();
        Scc = new ArrayList<>();

        for (node_data n : g.getV()) {
            vis.put(n.getKey(), false);
        }

        for (int i = 0; i < V; i++)
            if (!vis.get(i)) dfs(i);

    }

    private void dfs(int i) {
        low[i] = count++;
        vis.put(i, true);
        stack.push(i);
        int min = low[i];
        for (edge_data e : g.getE(i)) {
            if (!vis.get(e.getDest()))
                dfs(e.getDest());
            if (low[e.getDest()] < min)
                min = low[e.getDest()];
        }
        if (min < low[i]) {
            low[i] = min;
            return;
        }
        List<Integer> component = new ArrayList<>();
        int k;
        do {
            k = stack.pop();
            component.add(k);
            low[k] = g.nodeSize();
        } while (k != i);
        Scc.add(component);
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     */
    @Override
    public boolean isConnected() {
        if (g.nodeSize() == 0 || g.nodeSize() == 1)
            return true;
        Tarzan();
        return Scc.size() <= 1; // if there are more than one component return false
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (shortestPath(src, dest) == null) return 0;
        return weights.get(dest);
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     */
    public List<node_data> shortestPath(int src, int dest) {
        if (g.getNode(src) == null || g.getNode(dest) == null) // Checking if the keys exist
            return null;

        // Dijkstra algorithm
        weights = new HashMap<>();
        HashMap<Integer, node_data> daddy = new HashMap<>();
        for (node_data n : g.getV()) {
            weights.put(n.getKey(), Double.MAX_VALUE);
            daddy.put(n.getKey(), null);
        }
        weights.put(src, 0.0);
        g.getNode(src).setWeight(0.0);
        Queue<node_data> q = new PriorityQueue<>();
        q.add(g.getNode(src));
        while (!q.isEmpty()) {
            int tmp = q.poll().getKey();
            for (edge_data e : g.getE(tmp)) {
                double cur_dis = weights.get(tmp);
                double full_dis = cur_dis + e.getWeight();
                double dest_dis = weights.get(e.getDest());
                if (full_dis < dest_dis) {
                    weights.put(e.getDest(), full_dis);
                    g.getNode(e.getDest()).setWeight(full_dis);
                    daddy.put(e.getDest(), g.getNode(tmp));
                    q.add(g.getNode(e.getDest()));
                }

            }
        }

        Stack<node_data> rev = new Stack<>();
        node_data p = g.getNode(dest);
        rev.add(p);
        for (int i = 1; i < daddy.size(); i++) {
            if (daddy.get(p.getKey()) == null) break;
            rev.add(daddy.get(p.getKey()));
            p = g.getNode(daddy.get(p.getKey()).getKey());
        }
        List<node_data> path = new LinkedList<>();
        while (!rev.isEmpty()) {
            path.add(rev.pop());
        }

        return path;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        JSONObject graph = new JSONObject();
        JSONArray Edges = new JSONArray();
        JSONArray Nodes = new JSONArray();
        System.out.println("json");
        try {
            for (node_data n : g.getV()) {
                String st=n.getLocation().x() + "," + n.getLocation().y() + "," + n.getLocation().z();
                JSONObject node = new JSONObject();
                node.put("pos",st);
                node.put("id", n.getKey());
                Nodes.put(node);
                for (edge_data e : g.getE(n.getKey())) {
                    JSONObject edge = new JSONObject();
                    edge.put("src", e.getSrc());
                    edge.put("w", e.getWeight());
                    edge.put("dest", e.getDest());
                    Edges.put(edge);
                }
            }
            graph.put("Edges", Edges);
            graph.put("Nodes", Nodes);
            PrintWriter pw = new PrintWriter(new File(file+".json"));
            pw.write(graph.toString());
            pw.close();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            String r= new String(Files.readAllBytes(Paths.get(file)));
            JSONObject graph = new JSONObject(r);
            JSONArray edges=graph.getJSONArray("Edges");
            JSONArray nodes=graph.getJSONArray("Nodes");
            directed_weighted_graph G= new DWGraph_DS();
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
                G.addNode(p);
            }
            for (int i = 0; i <edges.length(); i++) {
                JSONObject e= edges.getJSONObject(i);
                int src = e.getInt("src");
                double w = e.getDouble("w");
                int dest = e.getInt("dest");
                G.connect(src,dest,w);
            }
            init(G);
            return true;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
    public String toString() {
        JSONObject graph = new JSONObject();
        JSONArray Edges = new JSONArray();
        JSONArray Nodes = new JSONArray();
        try {
            for (node_data n : g.getV()) {
                String st=n.getLocation().x() + "," + n.getLocation().y() + "," + n.getLocation().z();
                JSONObject node = new JSONObject();
                node.put("pos",st);
                node.put("id", n.getKey());
                Nodes.put(node);
                for (edge_data e : g.getE(n.getKey())) {
                    JSONObject edge = new JSONObject();
                    edge.put("src", e.getSrc());
                    edge.put("w", e.getWeight());
                    edge.put("dest", e.getDest());
                    Edges.put(edge);
                }
            }
            graph.put("Edges", Edges);
            graph.put("Nodes", Nodes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(graph);
    }
}





