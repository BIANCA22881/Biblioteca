import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuWindow extends JFrame implements ActionListener {

    App app;
    JButton insertButton = new JButton("Înregistrează student");
    JButton borrowButton = new JButton("Împrumută carte");
    JButton booksButton = new JButton("Cărți disponibile");
    JButton inventoryButton = new JButton("Inventar carti");
    JButton logoutButton = new JButton("Logout");
    InsertStudentWindow insertStudent;
    BorrowBookWindow borrowBook;
    BooksWindow books;
    BooksInventory booksInventory;
    public MenuWindow(App app){
        this.app = app;
        insertStudent = new InsertStudentWindow(this);
        borrowBook = new BorrowBookWindow(this);
        books = new BooksWindow(this);
        booksInventory=new BooksInventory(this);
        setComponents();

    }
    public void setComponents(){

        insertButton.setBounds(220,80,400,40);
        insertButton.setFocusable(false);
        insertButton.addActionListener(this);
        insertButton.setBackground(new Color(0x975E64));
        insertButton.setFont(new Font("Arial",Font.PLAIN,28));

        borrowButton.setBounds(220,180,400,40);
        borrowButton.setFocusable(false);
        borrowButton.addActionListener(this);
        borrowButton.setBackground(new Color(0x975E64));
        borrowButton.setFont(new Font("Arial",Font.PLAIN,28));

        booksButton.setBounds(220,280,400,40);
        booksButton.setFocusable(false);
        booksButton.addActionListener(this);
        booksButton.setBackground(new Color(0x975E64));
        booksButton.setFont(new Font("Arial",Font.PLAIN,28));

        inventoryButton.setBounds(220,380,400,40);
        inventoryButton.setFocusable(false);
        inventoryButton.addActionListener(this);
        inventoryButton.setBackground(new Color(0x975E64));
        inventoryButton.setFont(new Font("Arial",Font.PLAIN,28));

        logoutButton.setBounds(320,480,200,40);
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(this);
        logoutButton.setBackground(new Color(0x975E64));
        logoutButton.setFont(new Font("Arial",Font.PLAIN,28));

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(840,630);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));

        this.add(insertButton);
        this.add(borrowButton);
        this.add(booksButton);
        this.add(inventoryButton);
        this.add(logoutButton);

        this.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==insertButton){
            this.setVisible(false);
            this.insertStudent.setVisible(true);
        }
        else if(e.getSource()==borrowButton){
            this.setVisible(false);
            this.borrowBook.setVisible(true);
        }else if(e.getSource()==booksButton){
            this.setVisible(false);
            this.books.setVisible(true);
        }else if(e.getSource()==logoutButton) {
            this.setVisible(false);
            this.app.setVisible(true);
            this.app.loginText.setText("");
        }else if(e.getSource()==inventoryButton){
            this.setVisible(false);
            this.booksInventory.setVisible(true);
        }
    }
}
