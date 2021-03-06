package api;

import java.util.Objects;

public class EdgeData implements edge_data {
    private int src,dest,tag;
    private double w;
    private String info;

    public EdgeData(int src,int dest, double w) {
        this.src=src;
        this.dest=dest;
        this.w=w;
    }

    @Override
    public int getSrc() { return src; }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return w;
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
        return "Edge:{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + w +'}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeData)) return false;
        EdgeData edgeData = (EdgeData) o;
        return src == edgeData.src &&
                dest == edgeData.dest &&
                Double.compare(edgeData.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, w);
    }
}
