import net.proteanit.sql.DbUtils;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

public class price_info extends JDialog {
    private JPanel pricePanel;
    private JScrollPane table_1;
    private JTable table1;
    private JButton btnsave;
    private JButton btnupdate;
    private JButton btnsearch;
    private JButton btndelete;
    private JComboBox cmbp_name;
    private JTextField tfp_id;
    private JTextField tfpt_id;
    private JTextField tf_price;
    private JComboBox cmbpt_name;

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

    public void fillp_name(){

        try {

            String sql="select * from plant_info";
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                cmbp_name.addItem(rs.getString("p_name"));
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void fillpt_name(){
        try {

            String sql="Select * from plant_type_info";
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs=pst.executeQuery();

            while (rs.next()){
                cmbpt_name.addItem(rs.getString("pt_name"));
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void fillptname(){

        try {

            String sql="Select * from plant_type_info where p_id="+ tfp_id.getText();
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs=pst.executeQuery();
            cmbpt_name.removeAllItems();
            while (rs.next()){
                cmbpt_name.addItem(rs.getString("pt_name"));
            }

        }catch (Exception e){
            System.out.println(e);
        }
      }
      void table_load() {
        try {
            pst = conn.prepareStatement("select * from price_info");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public price_info() {
        connect();
        fillp_name();
        fillpt_name();
        table_load();
        cmbpt_name.setSelectedIndex(-1);




        cmbp_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String item=(String)cmbp_name.getSelectedItem();
                String sql="Select * from plant_info where p_name=?";

                try{
                    PreparedStatement pst=conn.prepareStatement(sql);
                    pst.setString(1,item);

                    ResultSet rs=pst.executeQuery();

                    if (rs.next()==true){
                        tfp_id.setText(rs.getString("p_id"));
                        fillptname();
                        tfpt_id.setText("");
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });

        cmbpt_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String item=(String)cmbpt_name.getSelectedItem();
                String sql="Select * from plant_type_info where pt_name=?";

                try{
                    PreparedStatement pst=conn.prepareStatement(sql);
                    pst.setString(1,item);

                    ResultSet rs=pst.executeQuery();

                    if (rs.next()==true){
                        tfpt_id.setText(rs.getString("pt_id"));
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });

        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int p_id,pt_id,price;

                p_id=Integer.parseInt(tfp_id.getText());
                pt_id =Integer.parseInt(tfpt_id.getText());
                price=Integer.parseInt(tf_price.getText());

                try {
                    if (tfp_id.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please ,Choose Plant Name");
                    }
                    else if (tfpt_id.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please ,Choose Plant Type Name");
                    }
                    else if (tf_price.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please ,Enter the Price");
                    }
                    else {
                        String sql = "Insert into price_info(p_id,pt_id,price) Values(?,?,?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);


                        preparedStatement.setInt(1, p_id);
                        preparedStatement.setInt(2, pt_id);
                        preparedStatement.setInt(3, price);

                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Addedd!!!");

                        table_load();
                        tfp_id.setText("");
                        tfpt_id.setText("");
                        tf_price.setText("");
                        cmbpt_name.setSelectedIndex(-1);

                    }

                } catch (Exception e1) {
                    System.out.println(e1);
                }


            }
        });
        btnsearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    if (tfp_id.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please ,Choose Plant Name");
                    }
                    else if (tfpt_id.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please ,Choose Plant Type Name");
                    }
                    else
                    {
                    String sql = "Select price from price_info where p_id = "+ tfp_id.getText() + " and pt_id= "+ tfpt_id.getText();

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);

                    ResultSet rs=preparedStatement.executeQuery();

                    if(rs.next()==true){

                        tf_price.setText(rs.getString("price"));
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Invalid Plant And Plant Type ID");

                        tfp_id.setText("");
                        tfpt_id.setText("");
                        tf_price.setText("");
                        // cmbpt_name.setSelectedIndex(-1);

                        }
                    }

                }catch (Exception e1){
                    System.out.println(e1);
                }


            }
        });
        btnupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int price;

                price=Integer.parseInt(tf_price.getText());

                try{

                    if(tf_price.getText().length()<=0)
                    {
                        JOptionPane.showMessageDialog(null,"Please , Enter the Price");
                    }
                    else {
                        pst=conn.prepareStatement("Update price_info set price=? where p_id= " +tfp_id.getText()+" and pt_id= "+tfpt_id.getText());

                        pst.setInt(1, price);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Updateddd!!!");

                        table_load();
                        tfp_id.setText("");
                        tfpt_id.setText("");
                        tf_price.setText("");
                        cmbpt_name.setSelectedIndex(-1);
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });
        btndelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    if (tfp_id.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please ,Choose Plant Name");
                    }
                    else if (tfpt_id.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please ,Choose Plant Type Name");
                    }
                    else {
                        String sql = "Select price from price_info where p_id = " + tfp_id.getText() + " and pt_id= " + tfpt_id.getText();

                        PreparedStatement preparedStatement = conn.prepareStatement(sql);

                        ResultSet rs = preparedStatement.executeQuery();

                        if (rs.next() == true) {

                            tf_price.setText(rs.getString("price"));
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Plant And Plant Type ID");

                            tfp_id.setText("");
                            tfpt_id.setText("");
                            tf_price.setText("");
                            cmbpt_name.setSelectedIndex(-1);
                        }
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }

                try{
                    String sql="delete from price_info where p_id= "+tfp_id.getText()+" and pt_id= "+tfpt_id.getText();

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null,"Record Deletedd!!!!");
                    table_load();

                    tfp_id.setText("");
                    tfpt_id.setText("");
                    tf_price.setText("");
                    cmbpt_name.setSelectedIndex(-1);

                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("price_info");
        frame.setContentPane(new price_info().pricePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
