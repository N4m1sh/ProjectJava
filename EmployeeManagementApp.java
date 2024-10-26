import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeManagementApp extends JFrame {
    private JTextField idField, nameField, ageField, salaryField, experienceField;
    private JButton insertButton, deleteButton, searchButton, updateButton, displayButton, exitButton;

    private Connection connection;
    private Statement statement;

    private JTextArea displayTextArea;

    public EmployeeManagementApp() {
        initializeUI();
        connectToDatabase();
    }

    private void initializeUI() {
        setTitle("Employee Management App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new GridLayout(6, 2));


        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(10);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(10);

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField(10);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField(10);

        JLabel experienceLabel = new JLabel("Experience:");
        experienceField = new JTextField(10);

        insertButton = new JButton("Insert");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");
        updateButton = new JButton("Update");
        displayButton = new JButton("Display");
        exitButton = new JButton("Exit");

        JPanel idPanel = new JPanel(new FlowLayout());
        idPanel.add(idLabel);
        idPanel.add(idField);

        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel agePanel = new JPanel(new FlowLayout());
        agePanel.add(ageLabel);
        agePanel.add(ageField);

        JPanel salaryPanel = new JPanel(new FlowLayout());
        salaryPanel.add(salaryLabel);
        salaryPanel.add(salaryField);

        JPanel experiencePanel = new JPanel(new FlowLayout());
        experiencePanel.add(experienceLabel);
        experiencePanel.add(experienceField);

        JPanel buttonPanel1 = new JPanel(new FlowLayout());
        buttonPanel1.add(insertButton);
        buttonPanel1.add(deleteButton);
        buttonPanel1.add(searchButton);

        JPanel buttonPanel2 = new JPanel(new FlowLayout());
        buttonPanel2.add(updateButton);
        buttonPanel2.add(displayButton);
        buttonPanel2.add(exitButton);

        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertEmployee();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchEmployee();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayEmployees();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        displayTextArea = new JTextArea(10, 30);
        displayTextArea.setEditable(false);

        add(idPanel);
        add(namePanel);
        add(agePanel);
        add(salaryPanel);
        add(experiencePanel);
        add(buttonPanel1);
        add(buttonPanel2);
        add(displayTextArea);
        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/DBMS";
            String username = "root";
            String password = "NamishSql#123";
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertEmployee() {
        String id = idField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String salary = salaryField.getText();
        String experience = experienceField.getText();

        String checkQuery = "SELECT * FROM employees WHERE id = '" + id + "'";

        try {
            ResultSet resultSet = statement.executeQuery(checkQuery);
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Employee with ID " + id + " already exists!");
            } else {
                String insertQuery = "INSERT INTO employees (id, name, age, salary, experience) VALUES ('" + id + "', '" + name + "', '" + age + "', '" + salary + "', '" + experience + "')";

                statement.executeUpdate(insertQuery);
                JOptionPane.showMessageDialog(this, "Employee inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee() {
        String id = idField.getText();

        String query = "DELETE FROM employees WHERE id = '" + id + "'";

        try {
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchEmployee() {
        String id = idField.getText();

        String query = "SELECT * FROM employees WHERE id = '" + id + "'";

        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String age = resultSet.getString("age");
                String salary = resultSet.getString("salary");
                String experience = resultSet.getString("experience");



                String employeeInfo = "ID: " + id + "\n" +
                        "Name: " + name + "\n" +
                        "Age: " + age + "\n" +
                        "Salary: " + salary + "\n" +
                        "Experience: " + experience + "\n";

                displayTextArea.setText(employeeInfo);
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateEmployee() {
        String id = idField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String salary = salaryField.getText();
        String experience = experienceField.getText();

        String query = "UPDATE employees SET name = '" + name + "', age = '" + age + "', salary = '" + salary + "', experience = '" + experience + "' WHERE id = '" + id + "'";

        try {
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayEmployees() {
        String query = "SELECT * FROM employees";

        try {
            ResultSet resultSet = statement.executeQuery(query);
            StringBuilder employeesInfo = new StringBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String age = resultSet.getString("age");
                String salary = resultSet.getString("salary");
                String experience = resultSet.getString("experience");

                employeesInfo.append("ID: ").append(id)
                        .append(", Name: ").append(name)
                        .append(", Age: ").append(age)
                        .append(", Salary: ").append(salary)
                        .append(", Experience: ").append(experience)
                        .append("\n");
            }
            displayTextArea.setText(employeesInfo.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EmployeeManagementApp();
            }
        });
    }
}
