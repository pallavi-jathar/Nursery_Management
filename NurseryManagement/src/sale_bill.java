import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sale_bill extends JDialog{
    private JTextField tfsale_id;
    private JTextField tfc_name;
    private JTextField tfc_mob;
    private JTextField tfp_id;
    private JComboBox cmbp_name;
    private JTextField tfpt_id;
    private JComboBox cmbpt_name;
    private JTextField tf_total;
    private JTextField tf_price;
    private JScrollPane table_1;
    private JTable table1;
    private JButton btnsave;
    private JButton btnadd;
    private JButton btnremove;
    private JSpinner tf_qty;
    private JTextField tfc_address;
    private JTextField tfsub_total;
    private JPanel salePanel;
    private JTextField tf_pay;
    private JTextField tf_balance;
    private JButton btnprint;
    private JTextArea txt_bill;
    private JTextField tf_date;

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

    private void autosaleid(){
        try{
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NurseryManagement", "postgres", "pallu1619");
            String sql = "Select sale_id from sale_info ORDER BY sale_id DESC LIMIT 1";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int sale_id=rs.getInt(1);
                int n=sale_id+1;
                tfsale_id.setText(Integer.toString(n));
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

    public void price(){
        try {

            String sql = "Select * from price_info where pt_id="+ tfpt_id.getText();
            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs=pst.executeQuery();

            while (rs.next()){
                tf_price.setText(rs.getString("price"));
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void balance(){
            int sub_total = Integer.parseInt(tfsub_total.getText());
            int pay = Integer.parseInt(tf_pay.getText());

            int balance = pay - sub_total;
            tf_balance.setText(String.valueOf(balance));

    }

    public void bill(){
        String  c_name=tfc_name.getText();
        String  c_mob=tfc_mob.getText();
        String c_add=tfc_address.getText();
        String  date=tf_date.getText();
        String sub_total=tfsub_total.getText();
        String  pay=tf_pay.getText();
        String balance=tf_balance.getText();

        DefaultTableModel model=new DefaultTableModel();
        model=(DefaultTableModel)table1.getModel();
        txt_bill.setText(txt_bill.getText()+"Name       : "+ c_name+"\n");
        txt_bill.setText(txt_bill.getText()+"Address    : "+ c_add+"\n");
        txt_bill.setText(txt_bill.getText()+"Mobile No. : "+ c_mob+"\n");
        txt_bill.setText(txt_bill.getText()+"Date       : "+ date+"\n");
        txt_bill.setText(txt_bill.getText()+"\n");

        txt_bill.setText(txt_bill.getText()+ "*********************************************************************************************************************\n");
        txt_bill.setText(txt_bill.getText()+ "                                                                                           BILL                       \n");
        txt_bill.setText(txt_bill.getText()+ "**********************************************************************************************************************\n");

            txt_bill.setText(txt_bill.getText()+ "  Plant Name"+"\t\t"+"     Plant Type Name"+"\t"+"        Price"+"\t"+"        Qty"+"\t"+"           Total"+"\n");

        for(int i=0;i<model.getRowCount();i++){
            String p_name=(String)model.getValueAt(i,2);
            String pt_name=(String)model.getValueAt(i,4);
            String price=(String)model.getValueAt(i,5);
            String qty=(String)model.getValueAt(i,6);
            String total=(String)model.getValueAt(i,7);
            txt_bill.setText(txt_bill.getText()+ "     "+ p_name+"\t\t"+  "             " +pt_name+"\t\t"+     "        "+ price+"\t"+"           "+qty+" \t"+"           "+total+"\n");

        }
        txt_bill.setText(txt_bill.getText()+"\n");
        txt_bill.setText(txt_bill.getText()+"\n");

        txt_bill.setText(txt_bill.getText()+"\t"+ "\t"+"                                                                                                    Sub Total : "+ sub_total+"\n");
        txt_bill.setText(txt_bill.getText()+"\t"+ "\t"+"                                                                                                    Pay       : "+ pay+"\n");
        txt_bill.setText(txt_bill.getText()+"\t"+ "\t"+"                                                                                                    Balance   : "+ balance+"\n");

        txt_bill.setText(txt_bill.getText()+"\n");
        txt_bill.setText(txt_bill.getText()+ "**********************************************************************************************************************\n");
        txt_bill.setText(txt_bill.getText()+ "                                                                                Thank You, Come Again...                                  \n");

    }
    void table_load() {
        try {
            pst = conn.prepareStatement("select * from sale_bill_info");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public sale_bill() {
        connect();
        autosaleid();
        table_load();
        fillp_name();
        fillpt_name();
        showdate();
        cmbpt_name.setSelectedIndex(-1);

        DefaultTableModel model=new DefaultTableModel();
        model=(DefaultTableModel)table1.getModel();

        ((DefaultTableModel) table1.getModel()).setRowCount(0);

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
                        tf_price.setText("");
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
                        price();
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
                        tfsale_id.getText(),
                        tfp_id.getText(),
                        cmbp_name.getSelectedItem(),
                        tfpt_id.getText(),
                        cmbpt_name.getSelectedItem(),
                        tf_price.getText(),
                        tf_qty.getValue().toString(),
                        tf_total.getText(),

                });
                try {
                    String sql="update stock_info set qty=qty-" + tf_qty.getValue().toString() + " where p_id=" + tfp_id.getText() + " and pt_id=" + tfpt_id.getText();
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.executeUpdate();

                }catch (Exception e1){
                    System.out.println(e1);
                }

                int sum=0;
                for(int i=0;i<table1.getRowCount();i++){
                    sum=sum+Integer.parseInt(table1.getValueAt(i,7).toString());
                }
                    tfsub_total.setText(Integer.toString(sum));
                    tfp_id.setText("");
                    tfpt_id.setText("");
                    cmbpt_name.setSelectedIndex(-1);
                    tf_price.setText("");
                    tf_total.setText("");

            }
        });
        btnprint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showdate();
                 balance();
                 bill();
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
                String c_name,c_mobile,c_address,date;


                c_name=tfc_name.getText();
                c_mobile=tfc_mob.getText();
                c_address=tfc_address.getText();
                date=tf_date.getText();


                if(tfc_name.getText().length()<=0)
                {
                    JOptionPane.showMessageDialog(null,"Please , Enter the Customer Name");
                }
                else if (tfc_address.getText().length()<=0)
                {
                    JOptionPane.showMessageDialog(null,"Please , Enter the Customer Address");
                }
                else if (tfc_mob.getText().length()<=0)
                {
                    JOptionPane.showMessageDialog(null,"Please , Enter the Customer Mobile No.");

                }
                else
                {
                    try {
                        String sql = "Insert into sale_info(c_name,c_mobile,c_address,date) Values(?,?,?,?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);


                        preparedStatement.setString(1, c_name);
                        preparedStatement.setString(2, c_mobile);
                        preparedStatement.setString(3, c_address);
                        preparedStatement.setString(4, date);
                        preparedStatement.executeUpdate();


                        DefaultTableModel model=new DefaultTableModel();
                        model=(DefaultTableModel)table1.getModel();


                        String sql1 = "Insert into sale_bill_info(sale_id,p_id,p_name,pt_id,pt_name,price,qty,total) Values(?,?,?,?,?,?,?,?)";
                        PreparedStatement pst = conn.prepareStatement(sql1);

                        int sale_id,p_id,pt_id,price, qty, total;
                        String p_name,pt_name;
                         for(int i=0;i<model.getRowCount();i++){

                            sale_id=Integer.parseInt((String) model.getValueAt(i,0));
                            p_id=Integer.parseInt((String) model.getValueAt(i,1));
                            p_name=(String) model.getValueAt(i,2);
                            pt_id=Integer.parseInt((String) model.getValueAt(i,3));
                            pt_name=(String)model.getValueAt(i,4);
                            price=Integer.parseInt((String) model.getValueAt(i,5));
                            qty=Integer.parseInt((String) model.getValueAt(i,6));
                            total=Integer.parseInt((String) model.getValueAt(i,7));

                            pst.setInt(1, sale_id);
                            pst.setInt(2, p_id);
                            pst.setString(3, p_name);
                            pst.setInt(4, pt_id);
                            pst.setString(5, pt_name);
                            pst.setInt(6, price);
                            pst.setInt(7, qty);
                            pst.setInt(8, total);
                            pst.executeUpdate();

                         }
                        JOptionPane.showMessageDialog(null, "Record Addedd!!!");
                        tfc_name.setText("");
                        tfc_mob.setText("");
                        tfc_address.setText("");
                        tfsub_total.setText("");
                        tf_pay.setText("");
                        tf_balance.setText("");
                        txt_bill.setText("");

                        autosaleid();
                        ((DefaultTableModel) table1.getModel()).setRowCount(0);


                    }catch (Exception e1){
                        System.out.println(e1);
                    }
                }
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("sale_bill");
        frame.setContentPane(new sale_bill().salePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


}
