import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDejaInregistrat extends JFrame implements ActionListener {
    InsertStudentWindow insertWin;
    JButton menuButton = new JButton("Menu");
    JLabel text1 = new JLabel("Student este deja");
    JLabel text2 = new JLabel("în baza de date");
    public StudentDejaInregistrat(InsertStudentWindow insertWin){
        this.insertWin = insertWin;
        this.setComponents();
    }
    public void setComponents() {
        text1.setFont(new Font("Arial",Font.PLAIN,36));
        text1.setBounds(60,50,300,40);

        text2.setFont(new Font("Arial",Font.PLAIN,36));
        text2.setBounds(75,100,300,40);

        menuButton.setBounds(140,200,140,40);
        menuButton.setFocusable(false);
        menuButton.addActionListener(this);
        menuButton.setBackground(new Color(0x975E64));
        menuButton.setFont(new Font("Arial",Font.PLAIN,28));

        this.setTitle("Library App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(420,315);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0xFBF4EF));

        this.add(menuButton);
        this.add(text1);
        this.add(text2);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==menuButton){
            this.setVisible(false);
            this.insertWin.menu.setVisible(true);
        }
    }
}
