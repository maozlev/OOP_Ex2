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
    private My_Arena arena;
    private gameClient.util.Range2Range _w2f;


    Frame(String a) {
        super(a);
    }

    /**
     * Updates arena data
     * @param arena - update
     */
    public void update(My_Arena arena) {
        this.arena = arena;
        updateFrame();
    }

    /**
     * Update frame size
     */
    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = arena.getGraph();
        _w2f = My_Arena.w2f(g,frame);
    }

    /**
     * Paint all parameters in the fame
     * @param g - responsible of the graphics
     */
    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        setSize(w,h);
        updateFrame();
        Image image;
        Graphics graphics;
        image = createImage(w, h);
        graphics = image.getGraphics();
        graphics.clearRect(0, 0, w, h);
        drawInfo(graphics);
        drawGraph(graphics);
        drawPokemons(graphics);
        drawAgants(graphics);
        g.drawImage(image, 0, 0, this);
    }

    /**
     * Draws all infor in the game, such as icon and clock
     * @param g - graphics
     */
    private void drawInfo(Graphics g) {
        ImageIcon icon = new ImageIcon("./src/resources/pikachu-icon.png");
        setIconImage(icon.getImage());
        Graphics2D g2d = (Graphics2D) g;
        Image im = new ImageIcon("./src/resources/pikapika.png").getImage();
        g2d.drawImage(im,0,0,this.getWidth(),this.getHeight(),null);
        drawClock(g);
        drawLevel(g);
    }

    /**
     * Draws a clock that counts down the time left in the game
     * @param g - graphics
     */
    private void drawClock(Graphics g){
        g.setColor(Color.black);
        g.setFont(new Font("David", Font.BOLD, 40) );
        g.drawString("Time: "+(arena.getTime()/1000),470,80);
    }

    /**
     * Draw the level of the game from scenario_num
     * @param g - graphics
     */
    private void drawLevel(Graphics g){
        g.setColor(Color.black);
        g.setFont(new Font("David", Font.BOLD, 40) );
        g.drawString("Level: "+(Ex2.scenario_num),250,80);
    }

    /**
     * Draws the graph - Nodes and edges
     * @param g - Graphics
     */
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

    /**
     * Draw the pokemons on the graph
     * @param g - Graphics
     */
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (f.getType() > 0) {
                        try {
                            BufferedImage img = ImageIO.read(new File("./src/resources/Pikachu.png"));
                            g.drawImage(img, (int) fp.x() - r, (int) fp.y() - r, 30, 30, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw the agents on the graph
     * @param g - Graphics
     */
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

    /**
     * Draws the node on the graph
     * @param n - the node we want to draw
     * @param r - it's size
     * @param g - Graphics
     */
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }

    /**
     * Draws the edges on the graph
     * @param e - the edge we want to draw
     * @param g - Graphics
     */
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
