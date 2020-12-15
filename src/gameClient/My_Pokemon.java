package gameClient;
import api.*;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;

public class My_Pokemon implements Comparable<My_Pokemon> {
    private edge_data _edge;
    private double _value;
    private int _type;
    private geo_location _pos;
    private double min_dist;
    private int min_ro;

    public My_Pokemon(geo_location p, int t, double v) {
        _type = t;
        _value = v;
        _pos = p;
    }

    public edge_data get_edge() {
        return _edge;
    }

    public void set_edge(edge_data _edge) {
        this._edge = _edge;
    }

    public geo_location getLocation() {
        return _pos;
    }

    public int getType() {return _type;}

    public double getValue() {return _value;}

    public double getMin_dist() {
        return min_dist;
    }

    public void setMin_dist(double mid_dist) {
        this.min_dist = mid_dist;
    }

    public int getMin_ro() {
        return min_ro;
    }

    public void setMin_ro(int min_ro) {
        this.min_ro = min_ro;
    }

    public String toString() {return "F:{v="+_value+", t="+_type+", pos="+_pos+ "}";}

    @Override
    public int compareTo(@NotNull My_Pokemon o) {
        if(this._value - o.getValue() < 0) return 1;
        return -1;
    }
}
