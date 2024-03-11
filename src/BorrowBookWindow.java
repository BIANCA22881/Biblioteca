import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BorrowBookWindow extends JFrame implements ActionListener {
    MenuWindow menu;
    CarteIndisponibila carteIndisponibila = new CarteIndisponibila(this);
    CarteEsteImprumutata carteEsteImprumutata = new CarteEsteImprumutata(this);
    CarteaEsteReturnata carteaEsteReturnata = new CarteaEsteReturnata(this);
    JTextField idText = new JTextField();
    JLabel idLabel = new JLabel();
    JTextField carteText = new JTextField();
    JLabel carteLabel = new JLabel();
    JButton menuButton = new JButton("Back");
    JButton borrowButton = new JButton("Împrumută");
    JButton returnButton = new JButton("Returnează");
    public BorrowBookWindow(MenuWindow menu){
        this.menu = menu;
        this.setComponents();
    }
    public void setComponents() {
        borrowButton.setBounds(100,450,200,40);
        borrowButton.setFocusable(false);
        borrowButton.addActionListener(this);
        borrowButton.setBackground(new Color(0x975E64));
        borrowButton.setFont(new Font("Arial",Font.PLAIN,28));

        menuButton.setBounds(350,500,140,40);
        menuButton.setFocusable(false);
        menuButton.addActionListener(this);
        menuButton.setBackground(new Color(0x975E64));
        menuButton.setFont(new Font("Arial",Font.PLAIN,28));

        returnButton.setBounds(530,450,200,40);
        returnButton.setFocusable(false);
        returnButton.addActionListener(this);
        returnButton.setBackground(new Color(0x975E64));
        returnButton.setFont(new Font("Arial",Font.PLAIN,28));

        idLabel.setText("Id Card: ");
        idLabel.setFont(new Font("Arial",Font.PLAIN,24));
        idLabel.setBounds(200,100,300,40);

        idText.setBounds(300,100,300,40);
        idText.setFont(new Font("Arial",Font.PLAIN,30));

        carteLabel.setText("Carte: ");
        carteLabel.setFont(new Font("Arial",Font.PLAIN,24));
        carteLabel.setBounds(200,250,300,40);

        carteText.setBounds(300,250,300,40);
        carteText.setFont(new Font("Arial",Font.PLAIN,30));

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(840, 630);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));

        this.add(idLabel);
        this.add(idText);

        this.add(carteLabel);
        this.add(carteText);

        this.add(borrowButton);
        this.add(returnButton);
        this.add(menuButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==menuButton){
            this.setVisible(false);
            this.menu.setVisible(true);
        }else if(e.getSource()==borrowButton){
            int idUtilizatorCard = Integer.parseInt(idText.getText());
            String titlu = carteText.getText();
            String numeBib = this.menu.app.numeBibliotecarlogat;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl", "bd102", "bd102");
                Statement stmt = con.createStatement();
                int credit=0;
                ResultSet crd = stmt.executeQuery("select credit from utilizator_card where id_utilizator_card ="+idUtilizatorCard);
                while (crd.next()){
                    credit=crd.getInt(1);
                }
                crd.close();
                //verific daca id ul cardului are credit
                if(credit == 0){
                    idText.setText("");
                    carteText.setText("");
                    this.setVisible(false);
                    this.carteIndisponibila.setVisible(true);
                }else{
                    //verific daca cartea este in biblioteca
                    ResultSet carteDisp = stmt.executeQuery("select c.titlu\n" +
                            "from carte c\n" +
                            "where c.id_carte not in (select id_carte from carte_imprumutata where data_restituire is null) ");
                    boolean ok=false;
                    while (carteDisp.next()){
                        if(titlu.equals(carteDisp.getString(1)))
                            ok=true;
                    }
                    carteDisp.close();
                    if(ok=false) {//nu este in biblioteca
                        idText.setText("");
                        carteText.setText("");
                        this.setVisible(false);
                        this.carteIndisponibila.setVisible(true);
                    }else {//cartea este disponibila si va fi imprumutata
                        ResultSet idB = stmt.executeQuery("select id_bibliotecar from bibliotecar where nume = '"+numeBib+"'");
                        //iau id ul bibliotecarei
                        int idBibliotecar=0;
                        while(idB.next()) {
                            idBibliotecar = idB.getInt(1);
                        }
                        idB.close();
                        //iau id ul carti
                        ResultSet idcarte = stmt.executeQuery("select id_carte from carte where titlu = '"+titlu+"'");
                        int idCarte=0;
                        while(idcarte.next()) {
                            idCarte = idcarte.getInt(1);
                        }
                        idcarte.close();
                        Statement stmt1 = con.createStatement();
                        //stmt1.execute("START TRANSACTION");
                        //am id ul utilizatorului
                        //pun cartea in carte_imprumutata
                        String insert = "insert into carte_imprumutata values(sysdate,sysdate + 7,null,"+idUtilizatorCard+","+idBibliotecar+","+idCarte+")";
                        //System.out.println(insert);
                        int cnt = stmt.executeUpdate(insert);
                        //scad creditului de la utilizator card
                        cnt = stmt.executeUpdate("update utilizator_card\n" +
                                "set credit = credit - 1\n" +
                                "where id_utilizator_card = "+idUtilizatorCard);
                        //commit
                        ResultSet com = stmt.executeQuery("COMMIT ");
                        com.close();

                        idText.setText("");
                        carteText.setText("");
                        this.setVisible(false);
                        this.carteEsteImprumutata.setVisible(true);
                        con.close();
                    }
                }
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }else if (e.getSource()==returnButton){
            int idUtilizatorCard = Integer.parseInt(idText.getText());
            String titlu = carteText.getText();
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl", "bd102", "bd102");
                Statement stmt = con.createStatement();
                //iau id carte
                ResultSet idcarte = stmt.executeQuery("select id_carte from carte where titlu = '"+titlu+"'");
                int idCarte=0;
                while(idcarte.next()) {
                    idCarte = idcarte.getInt(1);
                }
                idcarte.close();
                Statement stmt1 = con.createStatement();
                //stmt1.execute("START TRANSACTION");

                //update la carte imprumutat
                int cnt = stmt.executeUpdate("update carte_imprumutata\n" +
                        "set data_restituire = sysdate\n" +
                        "where id_utilizator_card ="+idUtilizatorCard+"  and id_carte ="+idCarte);
                //update la utilizator card
                cnt = stmt.executeUpdate("update utilizator_card\n" +
                        "set credit = credit + 1\n" +
                        "where id_utilizator_card = "+idUtilizatorCard);
                //commit
                ResultSet com = stmt.executeQuery("COMMIT ");
                com.close();
                this.setVisible(false);
                this.carteaEsteReturnata.setVisible(true);
                con.close();
            }catch (Exception e1) {
                System.out.println(e1);
            }

        }
    }
}
