import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class plant_type_info extends JDialog{
    private JPanel planttypePanel;
    private JScrollPane table_1;
    private JTable table1;
    private JButton btnsave;
    private JButton btnupdate;
    private JButton btnsearch;
    private JButton btndelete;
    private JTextField tfpt_id;
    private JTextField tfpt_name;
    private JTextField tfp_id;
    private JComboBox cmbp_name;

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
    public void fillcombobox(){

        try {

            String sql="Select p_name from plant_info";
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                  cmbp_name.addItem(rs.getString("p_name"));
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }


    private void autoid(){
        try{
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NurseryManagement", "postgres", "pallu1619");
            String sql = "Select pt_id from plant_type_info ORDER BY pt_id DESC LIMIT 1";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int pt_id=rs.getInt(1);
                int n=pt_id+1;
                tfpt_id.setText(Integer.toString(n));
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }

    void table_load() {
        try {
            pst = conn.prepareStatement("select * from plant_type_info");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));


        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public plant_type_info() {
        connect();
        fillcombobox();
        autoid();
        table_load();

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

                   }


                }catch (Exception e1){
                    System.out.println(e1);
                }

            }
        });


        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int p_id;
                String pt_name;

                p_id=Integer.parseInt(tfp_id.getText());
                pt_name = tfpt_name.getText();



                try {
                    if (tfpt_name.getText().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Please , Enter the Plant Type Name");
                    }
                    else {
                        String sql = "Insert into plant_type_info(p_id,pt_name) Values(?,?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);


                        preparedStatement.setInt(1, p_id);
                        preparedStatement.setString(2, pt_name);

                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Addedd!!!");

                        autoid();
                        table_load();
                        tfpt_name.setText("");
                        tfp_id.setText("");
                        tfpt_name.requestFocus();

                    }

                } catch (Exception e1) {
                    System.out.println(e1);
                }

            }
        });

        btnsearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pt_id=Integer.parseInt(JOptionPane.showInputDialog("Enter Plant TYpe ID for Search"));
                try{
                    String sql = "Select p_id,pt_id,pt_name from plant_type_info where pt_id = ?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);

                    preparedStatement.setInt(1, pt_id);
                    ResultSet rs=preparedStatement.executeQuery();

                    if(rs.next()==true){

                        tfp_id.setText(rs.getString("p_id"));
                        tfpt_id.setText(rs.getString("pt_id"));
                        tfpt_name.setText(rs.getString("pt_name"));
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Invalid Plant Type ID");

                        autoid();
                        tfpt_name.setText("");
                        tfp_id.setText("");
                        cmbp_name.setSelectedIndex(-1);



                    }

                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });
        btnupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pt_id,p_id;
                String pt_name;

                p_id=Integer.parseInt(tfp_id.getText());
                pt_name=tfpt_name.getText();
                pt_id=Integer.parseInt(tfpt_id.getText());

                try{

                    if(tfpt_name.getText().length()<=0)
                    {
                        JOptionPane.showMessageDialog(null,"Please , Enter the Plant Type Name");
                    }
                    else {
                        pst=conn.prepareStatement("Update plant_type_info set p_id=?,pt_name=? where pt_id=?");
                        pst.setInt(1, p_id);
                        pst.setString(2, pt_name);
                        pst.setInt(3,pt_id);

                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Updateddd!!!");

                        table_load();
                        autoid();

                        tfpt_name.setText("");
                        tfp_id.setText("");
                        cmbp_name.setSelectedIndex(-1);
                        tfpt_name.requestFocus();
                    }
                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });
        btndelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pt_id=Integer.parseInt(JOptionPane.showInputDialog("Enter Plant TYpe ID for Search"));
                try{
                    String sql = "Select p_id,pt_id,pt_name from plant_type_info where pt_id = ?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);

                    preparedStatement.setInt(1, pt_id);
                    ResultSet rs=preparedStatement.executeQuery();

                    if(rs.next()==true){

                        tfp_id.setText(rs.getString("p_id"));
                        tfpt_id.setText(rs.getString("pt_id"));
                        tfpt_name.setText(rs.getString("pt_name"));
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Invalid Plant Type ID");

                        autoid();
                        tfpt_name.setText("");
                    }

                }catch (Exception e1){
                    System.out.println(e1);
                }


                try{
                    String sql="delete from plant_type_info where pt_id=?";

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, pt_id);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null,"Record Deletedd!!!!");
                    table_load();
                    autoid();
                    tfpt_name.setText("");
                    tfp_id.setText("");
                    cmbp_name.setSelectedIndex(-1);
                    tfpt_name.requestFocus();


                }catch (Exception e1){
                    System.out.println(e1);
                }
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("plant_type_info");
        frame.setContentPane(new plant_type_info().planttypePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
