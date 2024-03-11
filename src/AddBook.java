import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddBook extends JFrame implements ActionListener {
    BooksInventory booksInventory;
    BookAdded bookAdded =new BookAdded(this);
    String titlu;
    String autor;
    JLabel descriereLabel = new JLabel();
    JTextField descriereText = new JTextField();
    JLabel anLabel = new JLabel();
    JTextField anText = new JTextField();
    JButton addButton = new JButton("Adauga");
    public AddBook(BooksInventory booksInventory){
        this.booksInventory=booksInventory;
        setComponents();
    }
    public void setComponents() {
        descriereLabel.setText("Descriere: ");
        descriereLabel.setFont(new Font("Arial",Font.PLAIN,24));
        descriereLabel.setBounds(100,50,300,40);

        descriereText.setBounds(100,100,640,40);
        descriereText.setFont(new Font("Arial",Font.PLAIN,30));

        anLabel.setText("An publicare: ");
        anLabel.setFont(new Font("Arial",Font.PLAIN,24));
        anLabel.setBounds(100,200,300,40);

        anText.setBounds(100,250,640,40);
        anText.setFont(new Font("Arial",Font.PLAIN,30));

        addButton.setBounds(320,450,200,40);
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        addButton.setBackground(new Color(0x975E64));
        addButton.setFont(new Font("Arial",Font.PLAIN,28));

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(840,630);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));

        this.add(descriereText);
        this.add(descriereLabel);
        this.add(anText);
        this.add(anLabel);
        this.add(addButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==addButton){
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl", "bd102", "bd102");
                Statement stmt = con.createStatement();
                int an = Integer.parseInt(anText.getText());
                String descriere = descriereText.getText();
                //verific daca autorul este in baza de date si il adaug daca nu(pastrez id ul pentru a adauga in carte_autori)
                ResultSet idA = stmt.executeQuery("select id_autor from autor where nume = '"+this.autor+"'");
                int idAutor = 0;
                while(idA.next()){
                    idAutor=idA.getInt(1);
                }
                idA.close();
                Statement stmt1 = con.createStatement();
                //stmt1.execute("START TRANSACTION");
                int cnt;
                if(idAutor == 0){
                    cnt =stmt.executeUpdate("INSERT INTO Autor VALUES (NULL,'"+this.autor+"')");

                    idA = stmt.executeQuery("select id_autor from autor where nume = '"+this.autor+"'");
                    while(idA.next()){
                        idAutor=idA.getInt(1);
                    }
                    idA.close();
                }
                //adaug in carte
                if(descriere.equals("")){
                    cnt =stmt.executeUpdate("INSERT INTO Carte VALUES (NULL,'"+this.titlu+"',null,"+an+","+idAutor+")");

                }else{
                    cnt =stmt.executeUpdate("INSERT INTO Carte VALUES (NULL,'"+this.titlu+"','"+descriere+"',"+an+","+idAutor+")");
                }
                //adaug in carte autori
                int idCarte = 0;
                ResultSet idC = stmt.executeQuery("select id_carte \n" +
                        "from carte \n" +
                        "where titlu = '"+this.titlu+"' and id_autor =(select id_autor from autor where nume = '"+this.autor+"')");
                while(idC.next()){
                    idCarte = idC.getInt(1);
                }
                idC.close();
                cnt = stmt.executeUpdate("INSERT INTO Carti_Autor VALUES ("+idAutor+","+idCarte+")");
                cnt =stmt.executeUpdate("COMMIT ");

            } catch (Exception e1) {
                System.out.println(e1);
            }
            this.descriereText.setText("");
            this.anText.setText("");
            this.setVisible(false);
            this.bookAdded.setVisible(true);
        }
    }
}
