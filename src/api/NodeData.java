package api;

public class NodeData implements node_data, Comparable<NodeData> {
    private final int key;
    private static int counter=0;
    private int tag;
    private geo_location pos;
    private double weight;
    private String info;

    public NodeData(){
        key=counter++;
        tag=0;
        weight=0;
        info=null;
        double x=0,y=0,z=0;
        geo_location p = new GeoLocation(x,y,z);
        pos=p;
    }

    public NodeData(int key){
        this.key=key;
        tag=0;
        double x=0,y=0,z=0;
        geo_location p = new GeoLocation(x,y,z);
        pos=p;
        weight=0;
        info=null;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return pos;
    }

    @Override
    public void setLocation(geo_location p) {
        pos=p;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
    weight=w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
    info=s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
    tag=t;
    }

    @Override
    public String toString() {
        return " key=" + key+".";
    }

    @Override
    public int compareTo(NodeData o) {
        if(this.weight - o.weight > 0) return 1;
        return -1;
    }
}
