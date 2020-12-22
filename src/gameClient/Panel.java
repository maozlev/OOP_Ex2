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
    }

    /**
     * Init the panel
     */
    public void init() {
        w = getWidth();
        h = getHeight();
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        this.revalidate();
        panel.setLayout(null);
        back = new JLabel();
        setID();
        setScenarioNumber();
        setButtons();
        setPanel();
        add(panel);
    }

    public void paint(Graphics g) {
        w = getWidth();
        h = getHeight();
        setSize(w, h);
        Image image = this.createImage(w, h);
        Graphics gr = image.getGraphics();
        paintComponents(gr);
        g.drawImage(image, 0, 0, this);
        back.setBounds(0, 0, getWidth(), getHeight());
    }

    /**
     * Set ID on panel
     */
    private void setID() {
        idLabel = new JLabel("ID:");
        Font IDtitle = new Font("David", Font.BOLD, 16);
        Font IDtext = new Font("David", Font.PLAIN, 12);
        idLabel.setForeground(Color.white);
        idLabel.setFont(IDtitle);
        idLabel.setBounds(150, 40, 200, 20);
        idText = new JTextField(20);
        idText.setFont(IDtext);
        idText.setBounds(150, 70, 200, 20);
        idText.setBackground(Color.orange);
    }

    /**
     * Set scenario number on panel
     */
    private void setScenarioNumber() {
        Font ScenarioTitle = new Font("David", Font.BOLD, 16);
        Font Scenariotext = new Font("David", Font.PLAIN, 12);
        scenarioNum_Label = new JLabel("Scenario:");
        scenarioNum_Label.setForeground(Color.white);
        scenarioNum_Label.setFont(ScenarioTitle);
        scenarioNum_Label.setBounds(150, 120, 200, 20);
        scenario_number = new JTextField(20);
        scenario_number.setFont(Scenariotext);
        scenario_number.setBounds(150, 150, 200, 28);
        scenario_number.setBackground(Color.orange);
    }

    /**
     * Set the login button (the button that play the game in case the details are ok)
     */
    private void setButtons() {
        LoginButton = new JButton("Let's start");
        LoginButton.setVisible(true);
        Font Loginfont = new Font("David", Font.BOLD, 15);
        LoginButton.setBackground(Color.yellow);
        LoginButton.setFont(Loginfont);
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

    /**
     * If details are true - Start the game
     * If ID & scenarioNumber are not valid - throw an exception and stop the game
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource())==LoginButton) {
            try {
                id = Integer.parseInt(idText.getText());
                scenarioNumber = Integer.parseInt(scenario_number.getText());
                run();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input, please enter again");
                System.out.println("Error, please enter again");
            }
        }
    }

    /**
     * Run panel
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