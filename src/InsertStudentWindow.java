import com.sun.jdi.IntegerValue;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsertStudentWindow extends JFrame implements ActionListener {
    MenuWindow menu;
    StudentInregistrat studentInregistrat = new StudentInregistrat(this);
    StudentDejaInregistrat studentDejaInregistrat = new StudentDejaInregistrat(this);
    JTextField nameText = new JTextField();
    JLabel nameLabel = new JLabel();
    JTextField nrMatricolText = new JTextField();
    JLabel nrMatricolLabel = new JLabel();
    JTextField anText = new JTextField();
    JLabel anLabel = new JLabel();
    JTextField grupaText = new JTextField();
    JLabel grupaLabel = new JLabel();
    JButton menuButton = new JButton("Back");
    JButton okButton = new JButton("Ok");
    public InsertStudentWindow(MenuWindow menu){
        this.menu = menu;
        this.setComponents();
    }
    public void setComponents() {
        menuButton.setBounds(100,450,100,40);
        menuButton.setFocusable(false);
        menuButton.addActionListener(this);
        menuButton.setBackground(new Color(0x975E64));
        menuButton.setFont(new Font("Arial",Font.PLAIN,28));

        okButton.setBounds(630,450,100,40);
        okButton.setFocusable(false);
        okButton.addActionListener(this);
        okButton.setBackground(new Color(0x975E64));
        okButton.setFont(new Font("Arial",Font.PLAIN,28));

        nameLabel.setText("Nume: ");
        nameLabel.setFont(new Font("Arial",Font.PLAIN,24));
        nameLabel.setBounds(100,50,200,40);

        nameText.setBounds(100,100,250,40);
        nameText.setFont(new Font("Arial",Font.PLAIN,30));

        nrMatricolLabel.setText("Nr Matricol: ");
        nrMatricolLabel.setFont(new Font("Arial",Font.PLAIN,24));
        nrMatricolLabel.setBounds(100,250,200,40);

        nrMatricolText.setBounds(100,300,250,40);
        nrMatricolText.setFont(new Font("Arial",Font.PLAIN,30));

        anLabel.setText("An: ");
        anLabel.setFont(new Font("Arial",Font.PLAIN,24));
        anLabel.setBounds(480,50,200,40);

        anText.setBounds(480,100,250,40);
        anText.setFont(new Font("Arial",Font.PLAIN,30));

        grupaLabel.setText("Grupa: ");
        grupaLabel.setFont(new Font("Arial",Font.PLAIN,24));
        grupaLabel.setBounds(480,250,250,40);

        grupaText.setBounds(480,300,250,40);
        grupaText.setFont(new Font("Arial",Font.PLAIN,30));

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(840, 630);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));

        this.add(nameLabel);
        this.add(nameText);

        this.add(nrMatricolLabel);
        this.add(nrMatricolText);

        this.add(anLabel);
        this.add(anText);

        this.add(grupaLabel);
        this.add(grupaText);

        this.add(menuButton);
        this.add(okButton);
    }
        @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==menuButton){
                this.setVisible(false);
                this.menu.setVisible(true);
        }else if(e.getSource()==okButton) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");

                Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl", "bd102", "bd102");
                String numei = nameText.getText();
                int ani = Integer.parseInt(anText.getText());
                String grupai = grupaText.getText();
                String nrMatricoli = nrMatricolText.getText();

                Statement stmt = con.createStatement();
                ResultSet nrM = stmt.executeQuery("SELECT nr_matricol from Student");
                boolean isAlready = false;
                while (nrM.next()) {
                    if (nrMatricoli.equals(nrM.getString(1))) {
                        isAlready = true;
                    }
                }
                if (isAlready) {//studentul era deja inregistrat
                    nameText.setText("");
                    anText.setText("");
                    grupaText.setText("");
                    nrMatricolText.setText("");
                    this.setVisible(false);
                    this.studentDejaInregistrat.setVisible(true);
                } else {
                    Statement stmt1 = con.createStatement();
                    //stmt1.execute("START TRANSACTION");

                    stmt.executeUpdate("insert into utilizator_card values (null,3)");
                    ResultSet id = stmt.executeQuery("select id_utilizator_card from utilizator_card");
                    int newId=0;
                    while(id.next()){
                        newId =id.getInt(1);
                    }
                    id.close();
                    String insert = "insert into student values (null,'"+numei+"','"+nrMatricoli+"',"+ani+",'"+grupai+"',"+newId+")";
                    //System.out.println(insert);
                    int cnt = stmt.executeUpdate(insert);
                    //commit
                    ResultSet concert = stmt.executeQuery("COMMIT");
                    nameText.setText("");
                    anText.setText("");
                    grupaText.setText("");
                    nrMatricolText.setText("");
                    this.setVisible(false);
                    this.studentInregistrat.setVisible(true);
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }
    }
}
