package gameClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Panel extends JFrame implements ActionListener {
    private JLabel idLabel;
    private JTextField idText;
    private JLabel scenarioNum_Label;
    private JTextField scenario_number;
    private JPanel panel;
    private JButton LoginButton;
    private int id;
    private int scenarioNumber;
    private int h = 320;
    private int w = 500;
    private JLabel back;

    public Panel() {
        setSize(w, h);
        init();
        panel.setBackground(Color.darkGray);
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
        setScenarioNumber();
        setButtons();
        setPanel();
        this.add(panel);
    }

    public void paint(Graphics g) {
        w = getWidth();
        h = getHeight();
        this.setSize(w, h);
        Image image = this.createImage(w, h);
        Graphics graph = image.getGraphics();
        paintComponents(graph);
        g.drawImage(image, 0, 0, this);
        back.setBounds(0, 0, getWidth(), getHeight());
    }

    /**
     * set ID
     */
    private void setID() {
        idLabel = new JLabel("ID:");
        Font f1 = new Font("David", Font.BOLD, 16);
        Font f2 = new Font("David", Font.PLAIN, 12);
        idLabel.setForeground(Color.white);
        idLabel.setFont(f1);
        idLabel.setBounds(150, 40, 200, 20);
        idText = new JTextField(20);
        idText.setFont(f2);
        idText.setBounds(150, 70, 200, 20);
        idText.setBackground(Color.orange);
    }

    /**
     * set Game number
     */
    private void setScenarioNumber() {
        Font f1 = new Font("David", Font.BOLD, 16);
        Font f2 = new Font("David", Font.PLAIN, 12);
        scenarioNum_Label = new JLabel("Scenario:");
        scenarioNum_Label.setForeground(Color.white);
        scenarioNum_Label.setFont(f1);
        scenarioNum_Label.setBounds(150, 120, 200, 20);
        scenario_number = new JTextField(20);
        scenario_number.setFont(f2);
        scenario_number.setBounds(150, 150, 200, 28);
        scenario_number.setBackground(Color.orange);
    }

    /**
     * set the login button (the button that play the game in case the details are ok)
     */
    private void setButtons() {
        //login button
        LoginButton = new JButton("Let's start");
        LoginButton.setVisible(true);
        Font f3 = new Font("David", Font.BOLD, 15);
        LoginButton.setBackground(Color.yellow);
        LoginButton.setFont(f3);
        LoginButton.setBounds(200, 210, 100, 25);
        LoginButton.addActionListener(this);
    }

    /**
     * add all the labels and the buttons to the panel
     */
    private void setPanel() {
        panel.add(scenarioNum_Label);
        panel.add(idLabel);
        panel.add(idText);
        panel.add(LoginButton);
        panel.add(scenario_number);
        panel.add(back);
        panel.setSize(getWidth(), getHeight());
    }

//    /**
//     * paint the background of the screen
//     */
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        ImageIcon pokemon = new ImageIcon("./src/resources/pokemon.jpeg");
        Image pokemon1 = pokemon.getImage();
        Image pokemon2 = pokemon1.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        pokemon = new ImageIcon(pokemon2);
        back.setIcon(pokemon);
    }

    /**
     * check if the login button is clicked, and then, check if the
     * id and the scenario is ok and start the game.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource())==LoginButton) {
            try {
                id = Integer.parseInt(idText.getText());
                scenarioNumber = Integer.parseInt(scenario_number.getText());
                // JOptionPane.showMessageDialog(this, "Click to start!");
                run();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input, please enter again");
                System.out.println("Error, please enter again");
            }
        }
    }

    /**
     * create a new Ex2 (new game) and run it with the given id and game number
     */
    public void run() {
        Ex2 login=new Ex2();
        login.setID(id);
        login.setScenario_num(scenarioNumber);
        this.dispose();
        Thread Game = new Thread(login);
        Game.start();
    }
}