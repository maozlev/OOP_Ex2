package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, HashMap<Integer, edge_data>> Edges;
    private HashMap<Integer, node_data> Nodes;
    private int MC, numofedges;


    public DWGraph_DS() {
        Nodes = new HashMap<>();
        Edges = new HashMap<>();
        MC = 0;
        numofedges=0;
    }

    @Override
    public node_data getNode(int key) {
        return Nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return Edges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        if (!Nodes.containsKey(n.getKey())) {
            Nodes.put(n.getKey(), n);
            Edges.put(n.getKey(), new HashMap<>());
            MC++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (!Nodes.containsKey(src) || !Nodes.containsKey(dest)) {
            return;
        }
        if (src == dest) return;
        edge_data edge = new EdgeData(src, dest, w);
        if (Edges.get(src).containsKey(dest)) {
            Edges.get(src).replace(dest, edge);
            return;
        }
        Edges.get(src).put(dest, edge);
        numofedges++;
        MC++;
    }

    @Override
    public Collection<node_data> getV() {
        return Nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!Nodes.containsKey(node_id)) return null;
        Set<Integer> keys = Edges.get(node_id).keySet(); // Get neighbors keys
        ArrayList<edge_data> edge_list = new ArrayList<>(keys.size()); // ArrayList for collection
        for (Integer k : keys) { // iterate through the keys
            edge_list.add(Edges.get(node_id).get(k));
        }
        return edge_list;
    }

    @Override
    public node_data removeNode(int key) {
        if (getNode(key) == null)
            return null;
        for (int n : Edges.get(key).keySet()) {
            Edges.get(n).remove(key);
            numofedges--;
            MC++;
        }

        Edges.remove(key);
        MC++;
        return Nodes.remove(key);
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if (!Edges.containsKey(src) || !Edges.containsKey(dest) || !Edges.get(src).containsKey(dest))
            return null;

        numofedges--;
        MC++;
        return Edges.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return Nodes.size();
    }

    @Override
    public int edgeSize() {
        return numofedges;
    }

    @Override
    public int getMC() {
        return MC;
    }

    @Override
    public String toString() {
        StringBuilder st= new StringBuilder();
        for (node_data n:getV()) {
            st.append("key: ").append(n.getKey()).append("\n");
            for (edge_data e:getE(n.getKey())) {
                st.append("dest: ").append(e.getDest()).append(" w: ").append(e.getWeight()).append("\n");
            }
        }
        return st.toString();
    }
}
