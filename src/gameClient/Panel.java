package gameClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Panel extends JFrame implements ActionListener {
    private JTextField ID_Text;
    private JLabel ID_Label;
    private JLabel GameNumber_Label;

    private JPanel panel;
    private JComboBox GameOpt;
    private JButton LoginButton;
    private int ID;
    private int GameNumber;
    private int h = 350;
    private int w = 550;
    private ImageIcon pokemon;
    private Image image;
    private Graphics gr;
    private JLabel back;

    public Panel() {
        setSize(w, h);
        init();
        panel.setBackground(new Color(0,255,0));
        this.revalidate();
       // panel.revalidate();
    }

    //init the login frame with all the labels and buttons
    public void init() {
        w = getWidth();
        h = getHeight();
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        this.revalidate();
        panel.revalidate();
        panel.setLayout(null);
        back = new JLabel();
        setID();
        setGameNumber();
        setButtons();
        setPanel();
        this.add(panel);
    }

    public void paint(Graphics g) {
        w = getWidth();
        h = getHeight();
        this.setSize(w, h);
        image = this.createImage(w, h);
        gr = image.getGraphics();
        paintComponents(gr);
        g.drawImage(image, 0, 0, this);
        back.setBounds(0, 0, getWidth(), getHeight());
    }

    /**
     * set all the labels and the text place for the ID
     */
    private void setID() {
        ID_Label = new JLabel("ID number");
        Font f1 = new Font("SansSerif", Font.BOLD, 15);
        Font f2 = new Font("SansSerif", Font.PLAIN, 12);
        ID_Label.setFont(f1);
        ID_Label.setBounds(175, 40, 200, 20);
        ID_Text = new JTextField(20);
        ID_Text.setFont(f2);
        ID_Text.setBounds(175, 70, 200, 28);
    }

    /**
     * set all the labels and the text place for the Game number
     */
    private void setGameNumber() {
        Font f1 = new Font("SansSerif", Font.BOLD, 15);
        Font f2 = new Font("SansSerif", Font.PLAIN, 12);
        GameNumber_Label = new JLabel("Game number");
        GameNumber_Label.setFont(f1);
        GameNumber_Label.setBounds(175, 120, 200, 20);
        String[] s={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        GameOpt=new JComboBox(s);
        GameOpt.setBackground(Color.white);
        GameOpt.setFont(f2);
        GameOpt.setBounds(175, 150, 200, 28);
    }

    /**
     * set the login button (the button that play the game in case the details are ok)
     */
    private void setButtons() {
        //login button
        LoginButton = new JButton("login");
        LoginButton.setVisible(true);
        Font f3 = new Font("Dialog", Font.BOLD, 15);
        LoginButton.setBackground(new Color(217, 89, 64));
        LoginButton.setFont(f3);
        LoginButton.setBounds(225, 250, 100, 25);
        LoginButton.addActionListener(this);
    }

    /**
     * add all the labels and the buttons to the panel
     */
    private void setPanel() {
        panel.add(GameNumber_Label);
        panel.add(ID_Label);
        panel.add(ID_Text);
        panel.add(LoginButton);
        panel.add(GameOpt);
        panel.add(back);
        panel.setSize(getWidth(), getHeight());
    }

    /**
     * paint the background of the screen
     */
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        pokemon = new ImageIcon("data//images//ash.jpg");
        Image pokemon1 = pokemon.getImage();
        Image pokemon2 = pokemon1.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        pokemon = new ImageIcon(pokemon2);
        back.setIcon(pokemon);
    }

    /**
     * check if the login button is clicked, if so, check if the
     * id and the game number is valid and start the game.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource())==LoginButton) {
            try {
                ID = Integer.parseInt(ID_Text.getText());
                GameNumber = GameOpt.getSelectedIndex();
                JOptionPane.showMessageDialog(this, "Let's start the game");
                run();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid ID\nPlease enter again");
                System.out.println("ERROR, enter again");
            }
        }
    }

    /**
     * create a new Ex2 (new game) and run it with the given id and game number
     */
    public void run() {
        Ex2 ex2=new Ex2();
        ex2.setID(ID);
        ex2.setGameNumber(GameNumber);
        this.dispose();
        Thread Game = new Thread(ex2);
        Game.start();
    }
}