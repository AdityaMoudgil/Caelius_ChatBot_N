import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

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
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT question, answer FROM qa")) {

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

        String answer = getAnswerFromDatabase(message);
        if (!answer.isEmpty()) {
            bot(answer);
        } else if (isMathematicalExpression(message)) {
            try {
                double result = evaluateMathExpression(message);
                bot("The result is: " + result);
            } catch (Exception exception) {
                bot("Invalid mathematical expression.");
            }
        } else {
            searchOnGoogle(message);
        }
    }

    public String getAnswerFromDatabase(String question){
        String url = "jdbc:mysql://localhost:3306/chatbot_db";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT answer FROM qa WHERE question = '" + question + "'")) {

            if (resultSet.next()) {
                return resultSet.getString("answer");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void searchOnGoogle(String query) {
        try {
            URL url = new URL("https://google.com/search?q=" + query.replace(" ", "+"));
            java.awt.Desktop.getDesktop().browse(url.toURI());
            bot("Here are some results for you.");
        } catch (Exception e) {
            bot("Please connect to the internet for better results.");
        }
    }

    public static void bot(String message) {
        area.append("Bot : " + message + "\n");
    }

    public static boolean isMathematicalExpression(String message) {
        // Check if the message contains only numbers, mathematical operators (+, -, *, /), and spaces
        return message.matches("[0-9+\\-*/\\s]+");
    }

    public static double evaluateMathExpression(String expression) {
        String[] tokens = expression.split("\\s");
        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (token.matches("[+-/*]")) {
                operators.push(token);
            } else {
                double number = Double.parseDouble(token);
                numbers.push(number);
            }

            while (numbers.size() >= 2 && operators.size() >= 1) {
                double operand2 = numbers.pop();
                double operand1 = numbers.pop();
                String operator = operators.pop();
                double result = performOperation(operand1, operator, operand2);
                numbers.push(result);
            }
        }

        return numbers.pop();
    }

    public static double performOperation(double operand1, String operator, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public static void main(String[] args) {
        Main cb = new Main("Chat Bot");
        cb.setSize(800, 605);
        cb.setLocation(50, 50);
    }
}
