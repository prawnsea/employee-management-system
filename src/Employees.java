import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employees {
    private JPanel mainPanel;
    private JLabel regTitle;
    private JTextField idInput;
    private JTextField firstNameInput;
    private JTextField lastNameInput;
    private JTextField contactNumberInput;
    private JTextField salaryInput;
    private JButton addBtn;
    private JButton searchBtn;

    private JButton removeBtn;
    private JButton updateBtn;
    private JTextField searchInput;

    private JTable table1;

    Connection con;
    PreparedStatement pst;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employees");
        frame.setContentPane(new Employees().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Databai connection
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/employee_db", "root", "root");
            System.out.println("Connected to the database");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // updating ui
    public void updateUI(String employeeId, String firstName, String lastName, String contactNumber, String salary) {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.addRow(new Object[]{employeeId, firstName, lastName, contactNumber, salary});
        eraseInput() ;
    }

    // Insert employee into the database
    public void databaseInjection(String employeeId, String firstName, String lastName, String contactNumber, String salary) {
        try {
            String sql = "INSERT INTO employees (employee_id, first_name, last_name, contact_number, salary) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, employeeId);
                pst.setString(2, firstName);
                pst.setString(3, lastName);
                pst.setString(4, contactNumber);
                pst.setString(5, salary);

                int rowUpdate = pst.executeUpdate();

                // Check the number of rows affected
                if (rowUpdate > 0) {
                    JOptionPane.showMessageDialog(mainPanel, "Employee inserted successfully into the database", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Failed to insert employee into the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "SQL/db error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clear input fields method
    public void eraseInput() {
        idInput.setText("");
        firstNameInput.setText("");
        lastNameInput.setText("");
        contactNumberInput.setText("");
        salaryInput.setText("");
    }

    public Employees() {
        connect();
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get data from input fields
                String employeeId = idInput.getText();
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String contactNumber = contactNumberInput.getText();
                String salary = salaryInput.getText();

                // Update UI
                updateUI(employeeId, firstName, lastName, contactNumber, salary);

                // Update database
                databaseInjection(employeeId, firstName, lastName, contactNumber, salary);
            }
        });
    }
}
