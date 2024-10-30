import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class dealer_info extends JDialog {
    private JPanel dealerPanel;
    private JTextField tfd_mobile;
    private JTextField tfd_name;
    private JTextField tfd_address;

    private JScrollPane table_1;
    private JTable table1;
    private JButton btnupdate;
    private JButton btndelete;
    private JButton btnsearch;
    private JButton btnsave;
    private JTextField tfd_id;


    Connection conn;
    PreparedStatement pst;
    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NurseryManagement", "postgres", "pallu1619");
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void autoid(){
        try{
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NurseryManagement", "postgres", "pallu1619");
            String sql = "Select dealer_id from dealer_info ORDER BY dealer_id DESC LIMIT 1";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int dealer_id=rs.getInt(1);
                int n=dealer_id+1;
                tfd_id.setText(Integer.toString(n));
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }

    void table_load(){
        try{
            pst  =conn.prepareStatement("select * from dealer_info");
            ResultSet rs= pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));


        }catch (Exception e){
            System.out.println(e);
        }

    }

    public dealer_info(){
        connect();
        autoid();
        table_load();
        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String dealer_name,dealer_add,dealer_mob;

                dealer_name=tfd_name.getText();
                dealer_add=tfd_address.getText();
                dealer_mob=tfd_mobile.getText();


                    try {
                        if(tfd_name.getText().length()<=0)
                        {
                            JOptionPane.showMessageDialog(null,"Please , Enter the Dealer Name");
                        }
                        else if (tfd_address.getText().length()<=0)
                        {
                            JOptionPane.showMessageDialog(null,"Please , Enter the Dealer Address");
                        }
                        else if (tfd_mobile.getText().length()<=0)
                        {
                            JOptionPane.showMessageDialog(null,"Please , Enter the Dealer Mobile No.");

                        }
                        else
                        {
                            String sql = "Insert into dealer_info(dealer_name,dealer_add,dealer_mob) Values(?,?,?)";
                            PreparedStatement preparedStatement = conn.prepareStatement(sql);


                            preparedStatement.setString(1, dealer_name);
                            preparedStatement.setString(2, dealer_add);
                            preparedStatement.setString(3, dealer_mob);
                            preparedStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Record Addedd!!!");

                            autoid();
                            table_load();
                            tfd_name.setText("");
                            tfd_address.setText("");
                            tfd_mobile.setText("");
                            tfd_name.requestFocus();

                        }

                    } catch (Exception e1) {
                        System.out.println(e1);
                    }

            }

        });

        btnsearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int dealer_id=Integer.parseInt(JOptionPane.showInputDialog("Enter Dealer ID for Search"));
                try{
                    String sql = "Select dealer_id,dealer_name,dealer_add,dealer_mob from dealer_info where dealer_id = ?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);

                    preparedStatement.setInt(1, dealer_id);
                    ResultSet rs=preparedStatement.executeQuery();

                    if(rs.next()==true){

                        tfd_id.setText(rs.getString("dealer_id"));
                        tfd_name.setText(rs.getString("dealer_name"));
                        tfd_address.setText(rs.getString("dealer_add"));
                        tfd_mobile.setText(rs.getString("dealer_mob"));
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Invalid Dealer ID");

                        autoid();
                        tfd_name.setText("");
                        tfd_address.setText("");
                        tfd_mobile.setText("");

                    }

                }catch (Exception e1){
                    System.out.println(e1);
                }

            }
        });

        
        btnupdate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        int dealer_id;
                        String dealer_name,dealer_add,dealer_mob;

                        dealer_name=tfd_name.getText();
                         dealer_add=tfd_address.getText();
                         dealer_mob=tfd_mobile.getText();
                        dealer_id=Integer.parseInt(tfd_id.getText());

                        try{

                      if(tfd_name.getText().length()<=0)
                     {
                         JOptionPane.showMessageDialog(null,"Please , Enter the Dealer Name");
                     }
                      else if (tfd_address.getText().length()<=0)
                      {
                        JOptionPane.showMessageDialog(null,"Please , Enter the Dealer Address");
                      }
                      else if (tfd_mobile.getText().length()<=0)
                      {
                        JOptionPane.showMessageDialog(null,"Please , Enter the Dealer Mobile No.");

                      }
                      else {
                            pst=conn.prepareStatement("Update dealer_info set dealer_name=?,dealer_add=?,dealer_mob=? where dealer_id=?");
                            pst.setString(1, dealer_name);
                            pst.setString(2, dealer_add);
                            pst.setString(3, dealer_mob);
                            pst.setInt(4,dealer_id);

                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Record Updateddd!!!");

                            table_load();
                            autoid();

                            tfd_name.setText("");
                            tfd_address.setText("");
                            tfd_mobile.setText("");
                            tfd_name.requestFocus();
                      }
                }catch (Exception e1){
                    System.out.println(e1);
                }

                    }
                });
//                }
//        });
        btndelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int dealer_id=Integer.parseInt(JOptionPane.showInputDialog("Enter Dealer ID for Delete"));

                try {
                    String sql = "Select dealer_id,dealer_name,dealer_add,dealer_mob from dealer_info where dealer_id = ?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);

                    preparedStatement.setInt(1, dealer_id);
                    ResultSet rs = preparedStatement.executeQuery();

                    if (rs.next() == true) {

                        tfd_id.setText(rs.getString("dealer_id"));
                        tfd_name.setText(rs.getString("dealer_name"));
                        tfd_address.setText(rs.getString("dealer_add"));
                        tfd_mobile.setText(rs.getString("dealer_mob"));
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }



                try{
                    String sql="delete from dealer_info where dealer_id=?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, dealer_id);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null,"Record Deletedd!!!!");
                    table_load();
                    autoid();
                    tfd_name.setText("");
                    tfd_address.setText("");
                    tfd_mobile.setText("");
                    tfd_name.requestFocus();


                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });



    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("dealer_info");
        frame.setContentPane(new dealer_info().dealerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
