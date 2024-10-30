import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class purchase_bill extends JDialog{
    private JPanel purchasePanel;
    private JScrollPane table_1;
    private JTable table1;
    private JButton btnsave;
    private JComboBox cmbp_name;
    private JTextField tfp_id;
    private JTextField tfpt_id;
    private JButton btnadd;
    private JButton btnremove;
    private JComboBox cmbpt_name;
    private JTextField tfpurchase_id;
    private JTextField tfdealer_id;
    private JComboBox cmbdealer_name;
    private JTextField tf_total;
    private JTextField tfsub_total;
    private JTextField tf_price;
    private JTextField tf_date;
    private  JSpinner tf_qty;

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

    private void autopurchaseid() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NurseryManagement", "postgres", "pallu1619");
            String sql = "Select purchase_id from purchase_info ORDER BY purchase_id DESC LIMIT 1";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int purchase_id = rs.getInt(1);
                int n = purchase_id + 1;
                tfpurchase_id.setText(Integer.toString(n));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

        public void filldealer_name(){

        try {

            String sql="select * from dealer_info";
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                cmbdealer_name.addItem(rs.getString("dealer_name"));
            }

        }catch (Exception e){
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

    public void showdate(){
        Date d=new Date();
        SimpleDateFormat sf=new SimpleDateFormat("dd/MM/yyyy");
        tf_date.setText(sf.format(d));
    }
    void table_load() {
        try {
            pst = conn.prepareStatement("select * from purchase_bill_info");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public purchase_bill() {
        connect();
        autopurchaseid();
        filldealer_name();
        fillp_name();
        fillpt_name();
        showdate();
        table_load();
        cmbpt_name.setSelectedIndex(-1);

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) table1.getModel();
        ((DefaultTableModel) table1.getModel()).setRowCount(0);

        cmbdealer_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String item = (String) cmbdealer_name.getSelectedItem();
                String sql = "Select * from dealer_info where dealer_name=?";

                try {
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, item);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next() == true) {
                        tfdealer_id.setText(rs.getString("dealer_id"));
                    }
                } catch (Exception e1) {
                    System.out.println(e1);
                }

            }
        });
        cmbp_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String item = (String) cmbp_name.getSelectedItem();
                String sql = "Select * from plant_info where p_name=?";

                try {
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, item);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next() == true) {
                        tfp_id.setText(rs.getString("p_id"));
                        fillptname();
                        tfpt_id.setText("");
                    }
                } catch (Exception e1) {
                    System.out.println(e1);
                }

            }
        });
        cmbpt_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String item = (String) cmbpt_name.getSelectedItem();
                String sql = "Select * from plant_type_info where pt_name=?";

                try {
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, item);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next() == true) {
                        tfpt_id.setText(rs.getString("pt_id"));
                    }
                } catch (Exception e1) {
                    System.out.println(e1);
                }
            }
        });

        tf_qty.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int qty=Integer.parseInt(tf_qty.getValue().toString());
                int price=Integer.parseInt(tf_price.getText());

                int total=qty*price;
                tf_total.setText(String.valueOf(total));

            }
        });


        btnadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showdate();

                DefaultTableModel model=new DefaultTableModel();
                model=(DefaultTableModel)table1.getModel();

                model.addRow(new Object[]{
                        tfpurchase_id.getText(),
                        tfp_id.getText(),
                        tfpt_id.getText(),
                        tf_price.getText(),
                        tf_qty.getValue().toString(),
                        tf_total.getText(),

                });


                try {
                    String sql="update stock_info set qty=qty+" + tf_qty.getValue().toString() + " where p_id=" + tfp_id.getText() + " and pt_id=" + tfpt_id.getText();
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.executeUpdate();

                }catch (Exception e1){
                    System.out.println(e1);
                }
                int sum=0;
                for(int i=0;i<table1.getRowCount();i++){
                    sum=sum+Integer.parseInt(table1.getValueAt(i,5).toString());
                }
                tfsub_total.setText(Integer.toString(sum));


                tfp_id.setText("");
                tfpt_id.setText("");
                cmbpt_name.setSelectedIndex(-1);
                tf_price.setText("");
                tf_total.setText("");




            }
        });
        btnremove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showdate();
                DefaultTableModel model=new DefaultTableModel();
                model=(DefaultTableModel)table1.getModel();

                model.removeRow(table1.getSelectedRow());

                int sum=0;
                for (int i=0;i<table1.getRowCount();i++){
                    sum=sum+Integer.parseInt(table1.getValueAt(i,7).toString());
                }
                tfsub_total.setText(Integer.toString(sum));

            }
        });
        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showdate();
                int dealer_id;
                String date;


               dealer_id=Integer.parseInt(tfdealer_id.getText());
                date=tf_date.getText();


                if(tfdealer_id.getText().length()<=0)
                {
                    JOptionPane.showMessageDialog(null,"Please , Enter Choose Name");
                }
                else
                {
                    try {
                        String sql = "Insert into purchase_info(dealer_id,date) Values(?,?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);


                        preparedStatement.setInt(1, dealer_id);
                        preparedStatement.setString(2, date);
                        preparedStatement.executeUpdate();


                        DefaultTableModel model=new DefaultTableModel();
                        model=(DefaultTableModel)table1.getModel();


                        String sql1 = "Insert into purchase_bill_info(purchase_id,p_id,pt_id,price,qty,total) Values(?,?,?,?,?,?)";
                        PreparedStatement pst = conn.prepareStatement(sql1);

                        int purchase_id,p_id,pt_id,price, qty, total;
                        for(int i=0;i<model.getRowCount();i++){

                            purchase_id=Integer.parseInt((String) model.getValueAt(i,0));
                            p_id=Integer.parseInt((String) model.getValueAt(i,1));
                            pt_id=Integer.parseInt((String) model.getValueAt(i,2));
                            price=Integer.parseInt((String) model.getValueAt(i,3));
                            qty=Integer.parseInt((String) model.getValueAt(i,4));
                            total=Integer.parseInt((String) model.getValueAt(i,5));

                            pst.setInt(1, purchase_id);
                            pst.setInt(2, p_id);
                            pst.setInt(3, pt_id);
                            pst.setInt(4, price);
                            pst.setInt(5, qty);
                            pst.setInt(6, total);
                            pst.executeUpdate();

                        }
                        JOptionPane.showMessageDialog(null, "Record Addedd!!!");
                        tfdealer_id.setText("");
                        cmbdealer_name.setSelectedIndex(-1);
                        tfsub_total.setText("");
                        autopurchaseid();
                        ((DefaultTableModel) table1.getModel()).setRowCount(0);


                    }catch (Exception e1){
                        System.out.println(e1);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("purchase_bil");
        frame.setContentPane(new purchase_bill().purchasePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
    }
