import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

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
    private JButton deleteBtn;
    private JButton updateBtn;  // Added update button
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
            System.out.println("Connected na bai");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Failed to connect bai", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // for clearing input fields after lagyan
    public void eraseInput() {
        idInput.setText("");
        firstNameInput.setText("");
        lastNameInput.setText("");
        contactNumberInput.setText("");
        salaryInput.setText("");
    }

    // Updating UI
    public void updateUI(String employeeId, String firstName, String lastName, String contactNumber, String salary) {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        String employeeIdFormat = String.format("%s", employeeId);
        String contactNumberFormat = String.format("%s", contactNumber);
        String salaryFormat = String.format("%s", salary);
        model.addRow(new Object[]{employeeId, firstName, lastName, contactNumber, salary});
        eraseInput();
    }

    // Insert employee sa db
    public void addEmployee(String employeeIdStr, String firstName, String lastName, String contactNumber, String salaryStr) {
        try {
            String sql = "INSERT INTO employee_info (employee_id, first_name, last_name, contact_number, salary) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                int employeeId = Integer.parseInt(employeeIdStr);
                int salary = Integer.parseInt(salaryStr);

                pst.setInt(1, employeeId);
                pst.setString(2, firstName);
                pst.setString(3, lastName);
                pst.setString(4, contactNumber);
                pst.setInt(5, salary);

                int rowUpdate = pst.executeUpdate();

                // row check
                if (rowUpdate > 0) {
                    JOptionPane.showMessageDialog(mainPanel, "oks na", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "mali", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "SQL/db error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // search employee
    public void searchEmployee(String employeeIdStr) {
        try {
            String sql = "SELECT * FROM employee_info WHERE employee_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, employeeIdStr);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // pang hanap ng employee
                        String findEmployee = rs.getString("employee_id");
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String contactNumber = rs.getString("contact_number");
                        String salary = rs.getString("salary");

                        idInput.setText(findEmployee);
                        firstNameInput.setText(firstName);
                        lastNameInput.setText(lastName);
                        contactNumberInput.setText(contactNumber);
                        salaryInput.setText(salary);
                        JOptionPane.showMessageDialog(mainPanel,
                                "norem, update or delete the employee",
                                "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        eraseInput();
                        JOptionPane.showMessageDialog(mainPanel, "alaws", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "SQL/db error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update employee in db
    public void updateEmployee(String employeeIdStr, String firstName, String lastName, String contactNumber, String salaryStr) {
        try {
            String sql = "UPDATE employee_info SET first_name = ?, last_name = ?, contact_number = ?, salary = ? WHERE employee_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, firstName);
                pst.setString(2, lastName);
                pst.setString(3, contactNumber);
                pst.setString(4, salaryStr);
                pst.setString(5, employeeIdStr);

                int rowUpdate = pst.executeUpdate();

                // Check the number of rows updated
                if (rowUpdate > 0) {
                    JOptionPane.showMessageDialog(mainPanel, "Employee updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "No employee found with the given ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "SQL/db error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteEmployee(){
        try {
            String sql = "DELETE FROM employee_info";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                int rowUpdate = pst.executeUpdate();
                // row check
                if (rowUpdate > 0) {
                    JOptionPane.showMessageDialog(mainPanel, "employee deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);
                    eraseInput();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "mali", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "SQL/db error", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    public void intFormatter(JFormattedTextField textField) {
        NumberFormatter intValidator = new NumberFormatter();
        intValidator.setValueClass(Integer.class);
        intValidator.setAllowsInvalid(false);
        intValidator.setCommitsOnValidEdit(true);
        intValidator.setMinimum(0);
        intValidator.setFormat(NumberFormat.getIntegerInstance(Locale.getDefault()));
//        intValidator.setGroupingUsed(false);
        textField.setFormatterFactory(new DefaultFormatterFactory(intValidator));
    }


    public Employees() {
        connect();
        // update sa table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Employee ID");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Contact" + " " + "#");
        tableModel.addColumn("Salary");
        table1.setModel(tableModel);

        // event listeners kasi naka ui
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // pang get ng input field values
                String employeeIdStr = idInput.getText();
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String contactNumberStr = contactNumberInput.getText();
                String salaryStr = salaryInput.getText();

                // restrictions sa makulet
                try {
                    if (employeeIdStr.trim().isEmpty() || contactNumberStr.trim().isEmpty() || salaryStr.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(mainPanel, "Fill all input fields", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String employeeId = idInput.getText();
                    String contactNumber = contactNumberInput.getText();
                    String salary = salaryInput.getText();

                    // ui update and db injection(for exception handling)
                    updateUI(String.valueOf(employeeId), firstName, lastName, String.valueOf(contactNumber), String.valueOf(salary));
                    addEmployee(String.valueOf(employeeId), firstName, lastName, String.valueOf(contactNumber), String.valueOf(salary));
                    eraseInput();
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel, "Error: " + exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Event listener for the Search button
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeIdStr = searchInput.getText();
                searchEmployee(employeeIdStr);
            }
        });

        // Event listener for the Update button
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the updated values
                String employeeIdStr = idInput.getText();
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String contactNumberStr = contactNumberInput.getText();
                String salaryStr = salaryInput.getText();
                updateEmployee(employeeIdStr, firstName, lastName, contactNumberStr, salaryStr);
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });
    }
}
