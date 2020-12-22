package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, HashMap<Integer, edge_data>> Edges;
    private HashMap<Integer, node_data> Nodes;
    private int MC, numofedges;

    // Constructor
    public DWGraph_DS() {
        Nodes = new HashMap<>();
        Edges = new HashMap<>();
        MC = 0;
        numofedges=0;
    }

    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return Nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        return Edges.get(src).get(dest);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * @param n - new node
     */
    @Override
    public void addNode(node_data n) {
        if (!Nodes.containsKey(n.getKey())) {
            Nodes.put(n.getKey(), n);
            Edges.put(n.getKey(), new HashMap<>());
            MC++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (!Nodes.containsKey(src) || !Nodes.containsKey(dest)) {
            return;
        }
        if (src == dest) return;
        edge_data edge = new EdgeData(src, dest, w);
        if (Edges.get(src).containsKey(dest)) {
            if (Edges.get(src).get(dest).getWeight() != w) MC++;
            Edges.get(src).replace(dest, edge);
            return;
        }
        Edges.get(src).put(dest, edge);
        numofedges++;
        MC++;
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return Nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @return Collection<edge_data>
     */
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

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key - The key to be removed
     */
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

    /**
     * Deletes the edge from the graph,
     * @param src - start node
     * @param dest - end (target) node
     * @return the data of the removed edge (null if none).
     */
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

    public boolean equals(Object g){
        if (this == g) return true;
        if(!(g instanceof DWGraph_DS)) return false; // g need to be DWGraph_DS object
        DWGraph_DS g1 = (DWGraph_DS)g;
        boolean flag=true;
        if(g1.nodeSize()!= this.nodeSize()) return false; // first of all check sizes
        if(g1.edgeSize()!= this.edgeSize()) return false;
        for (node_data n : g1.getV()) { // check if all the nodes are equal
            if (n!=getNode(n.getKey())) flag=false;
            for (edge_data e: g1.getE(n.getKey())) {
                if(e.getWeight()!= getEdge(e.getSrc(),e.getDest()).getWeight()) flag=false; // check if all the weights are equal
            }
        }
        return flag;
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
