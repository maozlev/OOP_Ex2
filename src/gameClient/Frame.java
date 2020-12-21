package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Frame extends JFrame {
    private int _ind;
    private My_Arena arena;
    private gameClient.util.Range2Range _w2f;
    //private static Panel panel = new Panel();


    Frame(String a) {
        super(a);
        int _ind = 0;
    }

    public void update(My_Arena arena) {
        this.arena = arena;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = arena.getGraph();
        _w2f = My_Arena.w2f(g,frame);
    }
    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        Image image;
        Graphics graphics;
        image = createImage(w, h);
        graphics = image.getGraphics();
        graphics.clearRect(0, 0, w, h);
        drawGraph(graphics);
        drawPokemons(graphics);
        drawAgants(graphics);
        drawClock(graphics);
        drawInfo(graphics);
        //panel.update(g);
        g.drawImage(image, 0, 0, this);

    }
    private void drawInfo(Graphics g) {
        java.util.List<String> str = arena.get_info();
        String dt = "asif";
        ImageIcon icon = new ImageIcon("./src/resources/pikachu-icon.png");
        setIconImage(icon.getImage());
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }
    }

    private void drawClock(Graphics g){

        g.setColor(Color.black);
        g.setFont(new Font("David", Font.BOLD, 40) );
        g.drawString(""+(arena.getTime()/1000),470,80);

    }
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = arena.getGraph();
        for (node_data n : gg.getV()) {
            g.setColor(new Color(255,128,0));
            drawNode(n, 10, g);
            for (edge_data e : gg.getE(n.getKey())) {
                g.setColor(Color.black);
                drawEdge(e, g);
            }
        }
    }
    private void drawPokemons(Graphics g) {
        java.util.List<My_Pokemon> fs = arena.getPokemons();
        if(fs!=null) {
            for (My_Pokemon f : fs) {
                geo_location c = f.getLocation();
                int r = 20;
                if (c != null) {
                    geo_location fp = this._w2f.world2frame(c);
                    if (f.getType() < 0) {
                        try {
                            BufferedImage img = ImageIO.read(new File("./src/resources/Mewtwo.png"));
                            g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r, 30, 30, null);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else if (f.getType() > 0) {
                        try {
                            BufferedImage img = ImageIO.read(new File("./src/resources/Pikachu.png"));
                            g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r, 30, 30, null);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = arena.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r=8;
            i++;
            if(c!=null) {
                geo_location fp = this._w2f.world2frame(c);
                try {
                    BufferedImage img = ImageIO.read(new File("./src/resources/ash.png"));
                    g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r, 30, 30, null);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = arena.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        Graphics2D g1= (Graphics2D)g;
        g1.setStroke(new BasicStroke(4));
        g1.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }
}
