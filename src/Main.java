import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main extends JFrame implements ActionListener {
    static JTextArea area = new JTextArea();
    JTextField field = new JTextField();
    JScrollPane sp;
    JButton send;
    LocalTime time = LocalTime.now();
    LocalDate date = LocalDate.now();
    Random random = new Random();
    List<String> questions = new ArrayList<>();

    public Main(String title) {
        super(title);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.white);
        field = new JTextField();
        send = new JButton(">");
        send.setFont(new Font("Poppins", Font.BOLD, 25));
        send.setBackground(Color.white);
        send.setBounds(735, 520, 50, 35);
        add(send);
        // For Text area
        area.setEditable(false);
        area.setBackground(Color.white);
        add(area);
        area.setFont(new Font("Poppins", Font.PLAIN, 15));
        // scrollbar
        sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setBounds(10, 20, 775, 470);
        add(sp);

        // For TextField
        field.setSize(725, 35);
        field.setLocation(10, 520);
        field.setForeground(Color.black);
        field.setFont(new Font("Poppins", Font.BOLD, 25));
        add(field);

        send.addActionListener(this);
        getRootPane().setDefaultButton(send);

        loadQuestionsFromDatabase();
    }

    public void loadQuestionsFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/chatbot_db";
        String username = "your_username";
        String password = "your_password";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT question FROM questions_table")) {

            while (resultSet.next()) {
                String question = resultSet.getString("question");
                questions.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String message = field.getText().toLowerCase();

        area.append("You : " + field.getText() + "\n");
        field.setText("");
        Socket sock = new Socket();

        if (questions.contains(message)) {
            bot("Yes, that's a valid question!");
        } else {
            try {
                try {
                    URL url = new URL("https://google.co.in");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    bot("Here are some results for you...");
                    java.awt.Desktop.getDesktop()
                            .browse(java.net.URI.create("http://www.google.com/search?hl=en&q=" + message.replace(" ", "+")
                                    + "&btnG=Google+Search"));

                } catch (Exception ee) {
                    bot("Please connect to the internet for better results.");
                }

            } catch (Exception eee) {
                int num = random.nextInt(3);
                if (num == 0) {
                    bot("Sorry, I can't understand you!");
                } else if (num == 1) {
                    bot("Sorry, I don't understand.");
                } else if (num == 2) {
                    bot("My apologies, I don't understand.");
                }
            }
        }

    }

    public static void bot(String message) {
        area.append("Bot : " + message + "\n");
    }

    public static void main(String[] args) {
        Main cb = new Main("Chat Bot");
        cb.setSize(800, 605);
        cb.setLocation(50, 50);
    }

}
