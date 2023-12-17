import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class employees {
    private JPanel mainPanel;
    private JLabel regTitle;
    private JTextField idInput;
    private JTextField firstNameInput;
    private JTextField lastNameInput;
    private JTextField contactNumberInput;
    private JTextField salaryInput;
    private JButton addBtn;
    private JTable table1;
    private JButton searchBtn;
    private JTextField searchInput;
    private JButton removeBtn;
    private JButton updateBtn;
    private JLabel employeeId;
    private JLabel salary;
    private JLabel contactNumber;
    private JLabel lastName;
    private JLabel firstName;

    Connection con;
    PreparedStatement pst;

    public static void main(String[] args) {
        JFrame frame = new JFrame("employees");
        frame.setContentPane(new employees().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
//databai check
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/employee_db", "root", "root");
            System.out.println("Connected na bai");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Di pa connected bai");
        }
    }
    // update sa ui method
    public void updateUI(String employeeId, String firstName, String lastName,String contactNumber,String salary ){
  // for adding table model (ty swing library)
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.addRow(new Object[]{employeeId, firstName, lastName, contactNumber, salary});
        eraser();

    }
   //pambura ng values after mag lagay
    public void eraser(){
        idInput.setText("");
        firstNameInput.setText("");
        lastNameInput.setText("");
        contactNumberInput.setText("");
        salaryInput.setText("");
    }
    public employees() {
        connect();
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

          //pang get ng data from input fields
                String employeeId = idInput.getText();
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String contactNumber = contactNumberInput.getText();
                String salary = salaryInput.getText();



          //SQL query dito para sa parameters sq db
                try{
                    String sql = "INSERT INTO employees (employee_id, first_name,last_name,contact_number,salary) VALUES(?,?,?,?,?)";
                    pst = con.prepareStatement(sql);

                    pst.setString(1, employeeId);
                    pst.setString(2,firstName);
                    pst.setString(3,lastName);
                    pst.setString(4,contactNumber);
                    pst.setString(5,salary);

                    int rowsChanged = pst.executeUpdate();

                    //logic para sa row changes sa UI
                    if(rowsChanged > 0){

                    }
                }
            }
        });
    }

    private void createUIComponents() {

    }
}
