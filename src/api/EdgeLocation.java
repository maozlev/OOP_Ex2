package api;

public class EdgeLocation implements edge_location {
    private edge_data edge;

    public EdgeLocation(int src , int dest, double weight){
        edge= new EdgeData(src, dest, weight);
    }

    @Override
    public edge_data getEdge() {
        return edge;
    }

    @Override
    public double getRatio() {
        return edge.getWeight();
    }
}
