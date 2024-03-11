import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BooksInventory extends JFrame implements ActionListener {
    MenuWindow menu;
    DeleteBook deleteBook = new DeleteBook(this);
    AddBook addBook=new AddBook(this);
    JLabel titluLabel = new JLabel();;
    JTextField titluText = new JTextField();
    JLabel autorLabel = new JLabel();;
    JTextField autorText = new JTextField();
    JButton deleteButton = new JButton("Sterge");
    JButton addButton = new JButton("Adauga");
    JButton backButton = new JButton("Back");
    public BooksInventory(MenuWindow menu){
        this.menu = menu;
        this.setComponents();
    }

    public void setComponents() {
        deleteButton.setBounds(100,450,200,40);
        deleteButton.setFocusable(false);
        deleteButton.addActionListener(this);
        deleteButton.setBackground(new Color(0x975E64));
        deleteButton.setFont(new Font("Arial",Font.PLAIN,28));

        backButton.setBounds(350,500,140,40);
        backButton.setFocusable(false);
        backButton.addActionListener(this);
        backButton.setBackground(new Color(0x975E64));
        backButton.setFont(new Font("Arial",Font.PLAIN,28));

        addButton.setBounds(530,450,200,40);
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        addButton.setBackground(new Color(0x975E64));
        addButton.setFont(new Font("Arial",Font.PLAIN,28));

        titluLabel.setText("Titlu: ");
        titluLabel.setFont(new Font("Arial",Font.PLAIN,24));
        titluLabel.setBounds(200,100,300,40);

        titluText.setBounds(300,100,300,40);
        titluText.setFont(new Font("Arial",Font.PLAIN,30));

        autorLabel.setText("Autor: ");
        autorLabel.setFont(new Font("Arial",Font.PLAIN,24));
        autorLabel.setBounds(200,250,300,40);

        autorText.setBounds(300,250,300,40);
        autorText.setFont(new Font("Arial",Font.PLAIN,30));

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(840, 630);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));

        this.add(titluLabel);
        this.add(titluText);

        this.add(autorLabel);
        this.add(autorText);

        this.add(deleteButton);
        this.add(addButton);
        this.add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==deleteButton){
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl", "bd102", "bd102");
                Statement stmt = con.createStatement();
                String titlu = titluText.getText();
                String autor = autorText.getText();
                //se sterge din carte_imprumutata
                Statement stmt1 = con.createStatement();
                //stmt1.execute("START TRANSACTION");
                int cnt = stmt.executeUpdate("delete from carte_imprumutata\n" +
                        "where id_carte = (select id_carte from carte where titlu = '"+titlu+"' and id_autor =(select id_autor from autor where nume = '"+autor+"'))");
                //se sterge din carte_autor
                cnt = stmt.executeUpdate("delete from carti_autor\n" +
                        "where carte_id_carte = (select id_carte from carte where titlu = '"+titlu+"' and id_autor =(select id_autor from autor where nume = '"+autor+"'))");
                //se sterge din carte
                cnt = stmt.executeUpdate("delete from carte\n" +
                        "where id_carte = (select id_carte from carte where titlu = '"+titlu+"' and id_autor =(select id_autor from autor where nume = '"+autor+"'))");
                ResultSet com = stmt.executeQuery("COMMIT ");
                com.close();
                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }
            this.titluText.setText("");
            this.autorText.setText("");
            this.setVisible(false);
            this.deleteBook.setVisible(true);
        }else if(e.getSource()==addButton){
            this.addBook.titlu = titluText.getText();
            this.addBook.autor = autorText.getText();
            this.titluText.setText("");
            this.autorText.setText("");
            this.setVisible(false);
            this.addBook.setVisible(true);
        }else if (e.getSource()==backButton){
            this.titluText.setText("");
            this.autorText.setText("");
            this.setVisible(false);
            this.menu.setVisible(true);
        }
    }
}
