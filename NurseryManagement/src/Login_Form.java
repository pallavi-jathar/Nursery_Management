import javax.swing.*;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login_Form extends JDialog{
    private JTextField tf_username;
    private JPasswordField pf_password;
    private JButton btnlogin;
    private JButton btncancel;
    private JPanel loginPanel;

    public Login_Form(JFrame parent) {
        super(parent);
        setTitle(("Login"));
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String username=tf_username.getText();
                    String password=String.valueOf(pf_password.getPassword());

                    user = getAuthenticatedUser(username, password);

                    if (user!=null){
                        dispose();
                        // JOptionPane.showInputDialog("Welcome "+ user.username);
                        JOptionPane.showMessageDialog(Login_Form.this,
                                "Welcome "+ user.username);
                    }
                    else {
                        JOptionPane.showMessageDialog(Login_Form.this,
                                "UserName or Password Invalid",
                                "Try Again",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {dispose();}
        });
        setVisible(true);


    }

        public User user;
        private User getAuthenticatedUser(String username, String password){
            User user=null;

            try {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NurseryManagement", "postgres", "pallu1619");

                Statement stmt = conn.createStatement();
                String sql = "SELECT * FROM Login WHERE username=? and password=?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    user=new User();
                    user.username=resultSet.getString("UserName");
                    user.password=resultSet.getString("Password");

                }

                stmt.close();
                conn.close();

            }catch (Exception e){
                System.out.println(e);
            }





            return user;
        }

        public static void main(String[] args) {
            Login_Form login=new Login_Form(null);
            User user=login.user;
            if(user!=null){
                System.out.println("WELCOME :" +user.username);
                System.out.println("Successful Authentication of password :" +user.password);
            }
            else {
                System.out.println("Authentication Canceled");
            }

        }
    }


