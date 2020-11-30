package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> mygraph;
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;
    private int MC, numofedges;


    public DWGraph_DS() {
        mygraph = new HashMap<>();
        edges = new HashMap<>();
        MC = 0;
    }

    @Override
    public node_data getNode(int key) {
        return mygraph.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return edges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        if (!mygraph.containsKey(n.getKey())) {
            mygraph.put(n.getKey(), n);
            edges.put(n.getKey(), new HashMap<>());
            MC++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (!mygraph.containsKey(src) || !mygraph.containsKey(dest)) {
            return;
        }
        if (src == dest) return;
        edge_data edge = new EdgeData(src, dest, w);
        if (edges.get(src).containsKey(dest)) {
            edges.get(src).replace(dest, edge);
            return;
        }
        edges.get(src).put(dest, edge);
        numofedges++;
        MC++;
    }

    @Override
    public Collection<node_data> getV() {
        return mygraph.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!mygraph.containsKey(node_id)) return null;
        Set<Integer> keys = edges.get(node_id).keySet(); // Get neighbors keys
        ArrayList<edge_data> edge_list = new ArrayList<>(keys.size()); // ArrayList for collection
        for (Integer k : keys) { // iterate through the keys
            edge_list.add(edges.get(node_id).get(k));
        }
        return edge_list;
    }

    @Override
    public node_data removeNode(int key) {
        if (getNode(key) == null)
            return null;
        for (int n : edges.get(key).keySet()) { // not sure about it, need to check
            edges.get(n).remove(key);
            numofedges--;
            MC++;
        }

        edges.remove(key);
        MC++;
        return mygraph.remove(key);
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if (!edges.containsKey(src) || !edges.containsKey(dest) || !edges.get(src).containsKey(dest))
            return null;

        numofedges--;
        MC++;
        return edges.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return mygraph.size();
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
        return "" + edges;
    }
}
