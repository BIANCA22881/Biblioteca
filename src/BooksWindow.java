import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BooksWindow extends JFrame implements ActionListener {
    MenuWindow menu;
    JButton menuButton = new JButton("Menu");
    JLabel []carti=new JLabel[15];
    JLabel []autori= new JLabel[15];
    public BooksWindow(MenuWindow menu){
        this.menu = menu;
        this.setComponents();
    }
    public void setComponents() {
        int i =0;
        int firstXT = 20;
        int firstXA = 440;
        int firstY = 50;

        menuButton.setBounds(350,500,140,40);
        menuButton.setFocusable(false);
        menuButton.addActionListener(this);
        menuButton.setBackground(new Color(0x975E64));
        menuButton.setFont(new Font("Arial",Font.PLAIN,28));

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(840, 630);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));
        this.add(menuButton);

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            //create the connection
            Connection con1 = DriverManager.getConnection(
                    "jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl","bd102","bd102");

            Statement stmt = con1.createStatement();
            ResultSet cartiB = stmt.executeQuery("select c.titlu,a.nume \n" +
                    "from carte c,autor a \n" +
                    "where c.id_autor = a.id_autor and c.id_carte not in (select id_carte from carte_imprumutata where data_restituire is null)");
            while(cartiB.next())
            {
                carti[i]=new JLabel(cartiB.getString("titlu"));
                carti[i].setFont(new Font("Arial",Font.PLAIN,24));
                carti[i].setBounds(firstXT,firstY,400,30);
                carti[i].setVisible(true);

                autori[i]=new JLabel(cartiB.getString("nume"));
                autori[i].setFont(new Font("Arial",Font.PLAIN,24));
                autori[i].setBounds(firstXA,firstY,400,30);
                autori[i].setVisible(true);
                this.add(carti[i]);
                this.add(autori[i]);

                i++;
                firstY+=40;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==menuButton){
            this.setVisible(false);
            this.menu.setVisible(true);
        }
    }
}
