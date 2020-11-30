package api;

public class GeoLocation implements geo_location {
    private double x, y, z;

    public GeoLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location g) {
        double dx = x() - g.x();
        double dy = y() - g.y();
        double dz = z() - g.z();
        double t = Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2);
        return Math.sqrt(t);
    }
}
