import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class App extends JFrame implements ActionListener {
    //JFrame loginWindow = new JFrame();
    JButton loginButton = new JButton("Login");
    JTextField loginText = new JTextField();
    JLabel libraryName = new JLabel();
    MenuWindow menuWindow ;
    String numeBibliotecarlogat;
    Connection con;
    public App() throws SQLException {
        menuWindow = new MenuWindow(this);
        ////////////////////////////////////////////
        loginButton.setBounds(270,420,300,40);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        loginButton.setBackground(new Color(0x975E64));
        loginButton.setFont(new Font("Arial",Font.PLAIN,28));

        loginText.setBounds(220,320,400,40);
        loginText.setFont(new Font("Arial",Font.PLAIN,30));

        libraryName.setText("Biblioteca Județeană \"Gh. Asachi\"");
        libraryName.setFont(new Font("Arial",Font.BOLD,36));
        libraryName.setBounds(120,120,600,60);

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(840,630);
        this.setResizable(false);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));

        this.menuWindow.setVisible(false);
        this.add(loginButton);
        this.add(loginText);
        this.add(libraryName);
        this.setVisible(true);
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl","bd102","bd102");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==loginButton){
            //daca numele din login se potriveste cu ce e in baza de date
            numeBibliotecarlogat = loginText.getText();
            Boolean ok = false;
            try {
                Statement stmt = con.createStatement();
                ResultSet numeB = stmt.executeQuery("SELECT nume from bibliotecar");
                while(numeB.next())
                {
                    if(numeBibliotecarlogat.equals(numeB.getString(1)))
                        ok=true;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if(ok == true){
                this.setVisible(false);
                menuWindow.setVisible(true);
            }
        }
    }

}
