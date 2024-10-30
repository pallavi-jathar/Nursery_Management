import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class plant_info extends JDialog {
    private JPanel plantPanel;
    private JTextField tfp_id;
    private JTextField tfp_name;
    private JTable table1;
    private JButton btnsave;
    private JButton btnupdate;
    private JButton btnsearch;
    private JButton btndelete;
    private JScrollPane table_1;


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
            String sql = "Select p_id from plant_info ORDER BY p_id DESC LIMIT 1";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int p_id=rs.getInt(1);
                int n=p_id+1;
                tfp_id.setText(Integer.toString(n));
            }
        }catch (Exception e){
            System.out.println(e);
        }

  }

    void table_load() {
        try {
            pst = conn.prepareStatement("select * from plant_info");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public plant_info() {
        connect();
        autoid();
        table_load();
        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String p_name;

                p_name=tfp_name.getText();


                try {
                    if(tfp_name.getText().length()<=0)
                    {
                        JOptionPane.showMessageDialog(null,"Please , Enter the Plant Name");
                    }
                    else
                    {
                        String sql = "Insert into plant_info(p_name) Values(?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);


                        preparedStatement.setString(1, p_name);
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Addedd!!!");

                        autoid();
                        table_load();
                        tfp_name.setText("");
                        tfp_name.requestFocus();

                    }

                } catch (Exception e1) {
                    System.out.println(e1);
                }


            }

        });

        btnsearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int p_id=Integer.parseInt(JOptionPane.showInputDialog("Enter Plant ID for Search"));
                try{
                    String sql = "Select p_id,p_name from plant_info where p_id = ?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);

                    preparedStatement.setInt(1, p_id);
                    ResultSet rs=preparedStatement.executeQuery();

                    if(rs.next()==true){

                        tfp_id.setText(rs.getString("p_id"));
                        tfp_name.setText(rs.getString("p_name"));
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Invalid Plant ID");

                        autoid();
                        tfp_name.setText("");

                    }

                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });
        btnupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int p_id;
                String p_name;

                p_name=tfp_name.getText();
                p_id=Integer.parseInt(tfp_id.getText());

                try{

                    if(tfp_name.getText().length()<=0)
                    {
                        JOptionPane.showMessageDialog(null,"Please , Enter the Plant Name");
                    }
                    else {
                        pst=conn.prepareStatement("Update plant_info set p_name=? where p_id=?");
                        pst.setString(1, p_name);
                        pst.setInt(2,p_id);

                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Updateddd!!!");

                        table_load();
                        autoid();

                        tfp_name.setText("");
                        tfp_name.requestFocus();
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }


            }
        });
        btndelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int p_id=Integer.parseInt(JOptionPane.showInputDialog("Enter Plant ID for Delete"));

                try {
                    String sql = "Select p_id,p_name from plant_info where p_id = ?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);

                    preparedStatement.setInt(1, p_id);
                    ResultSet rs = preparedStatement.executeQuery();

                    if (rs.next() == true) {

                        tfp_id.setText(rs.getString("p_id"));
                        tfp_name.setText(rs.getString("p_name"));
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }

                try{
                    String sql="delete from plant_info where p_id=?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, p_id);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null,"Record Deletedd!!!!");
                    table_load();
                    autoid();
                    tfp_name.setText("");
                    tfp_name.requestFocus();


                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("plant_info");
        frame.setContentPane(new plant_info().plantPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}