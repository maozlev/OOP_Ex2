package api;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph g;
    private HashMap<Integer, Boolean> vis;


    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }

    @Override
    public directed_weighted_graph copy(){
        directed_weighted_graph gn = new DWGraph_DS();

        for (node_data n : this.g.getV()) {
            gn.addNode(n); // Adding the nodes to the copied grpah
        }

        for (node_data n1 : this.g.getV()) {
            for (edge_data n2 : this.g.getE(n1.getKey())) {
                gn.connect(n1.getKey(), n2.getDest(), g.getEdge(n1.getKey(),n2.getDest()).getWeight()); // Connecting the nodes to their neighbors
            }
        }
        return gn;
    }

    private void Dijkstra(node_data src) {
        Queue<node_data> pq = new PriorityQueue<>();
        HashMap<Integer, Boolean> vis = new HashMap<>();
        HashMap<edge_data, Double> weights = new HashMap<>();


        for (node_data n : g.getV()) {
            n.setWeight(Double.MAX_VALUE);
            vis.put(n.getKey(), false);
            weights.put((edge_data) n, Double.MAX_VALUE);
        }

        g.getNode(src.getKey()).setWeight(0);
        pq.add(src);

        while (!pq.isEmpty()) {
            node_data u = pq.poll();
            vis.replace(u.getKey(), true);

            for (edge_data n : g.getE(u.getKey())) {
                if (!vis.get(n.getDest())) {
                    double alt = u.getWeight() + g.getEdge(u.getKey(), n.getDest()).getWeight();
                    if (alt < weights.get(n)) {
                        weights.put(n, alt);
                        pq.add(g.getNode(n.getDest()));
                    }
                }
            }
        }
    }

    private boolean DFS (int src, int dest) {
        vis.put(src,false);
        if (src == dest)
            return true;
        for (edge_data n: g.getE(src)) {
            if (!vis.get(n.getDest()))
                if (DFS(n.getSrc(), n.getDest()))
                    return true;
        }
        vis.put(src, true);
        return false;
    }

    @Override
    public boolean isConnected() {
//        if (g.nodeSize() == 0 || g.nodeSize() == 1)
//            return true;
//        Dijkstra(g.getV().stream().findFirst().orElse(new WGraph_DS.NodeInfo())); // Goes through the graph from the first node and checks that the graph is connected
//
//        for (node_info n: g.getV()) {
//            if (n.getTag() == Double.MAX_VALUE) // If a tag is infinite, it means the Dijkstra algorithm didn't reach it
//                return false;
//        }
//        return true;
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
