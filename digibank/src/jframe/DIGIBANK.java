/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class DIGIBANK extends javax.swing.JFrame {

    Connection Con = null;
    PreparedStatement pst = null;
    ResultSet Rs = null;
    Statement St = null;

    public DIGIBANK() {
        initComponents();
        hidepane();
        Accnumber.setVisible(true);
        ShowDate();
        time();
    }

    private void hidepane() {
        Accnumber.setVisible(false);
        EnterPin.setVisible(false);
        transaction.setVisible(false);
        ViewBalance.setVisible(false);
        Withdraw.setVisible(false);
        Deposit.setVisible(false);
        Transfer.setVisible(false);
        choosebank.setVisible(false);
        transact.setVisible(false);
        receipt.setVisible(false);
        toreceipt.setVisible(false);
    }

    private void clear() {
        WithdrawAmount.setText("");
        Depositamount.setText("");
        transferamount.setText("");
        transferaccnum.setText("");
        Accnumbetb.setText("");
        PIN.setText("");
        printreceipt.setText("***************DIGIBANK***********************");
        transactionnumber = 0;
        boolean sameacc = false;

    }
    public void time(){

       new Timer (0,new ActionListener() {

           @Override

           public void actionPerformed(ActionEvent e) {

              Date d = new Date();

              SimpleDateFormat s = new SimpleDateFormat("hh-mm-ss a");

              String tim = s.format(d);

              Time.setText(tim);

           }

       }).start();

   } 
    private void ShowDate()
{
    Date d = new Date();
    SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
    TodayDateLbl.setText(s.format(d));
}
    int id;
    int balance;
    boolean tocharge = false;
    int transactionnumber;
    boolean pinverified = false;
    boolean sameacc = false;
    String chosenbank;

    private void getId() {
        String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                id = Rs.getInt(1);
                balance = Rs.getInt(4);
            }
        } catch (Exception e) {

        }
    }

    private void todeduct() {
        if (tocharge == true) {
            getId();
            String Query = "Update bankaccounts set Balance=? Where ID=?";

            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                PreparedStatement Ps = Con.prepareStatement(Query);
                int newbalance = balance - 15;
                Ps.setInt(1, newbalance);
                Ps.setInt(2, id);
                if (Ps.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(null, "You have been charged 15 pesos for transacting with a different bank.");
                    NumberFormat myFormat = NumberFormat.getInstance();
                    NumberFormat MyFormat = NumberFormat.getInstance();
                    MyFormat.setGroupingUsed(true);
                    myFormat.setGroupingUsed(true);
                    printreceipt.setText(printreceipt.getText() + "\n" + bankname.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "\n" + accnumber.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(balance) + "\n\n");
                    printreceipt.setText(printreceipt.getText() + bankname.getText() + "-> " + chosenbank + "Bank-to-Bank Transaction: ₱" + myFormat.format(balance) + "- ₱15(Deduction fee)" + "\n\n");
                    printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(newbalance) + "\n\n");

                } else {
                    JOptionPane.showMessageDialog(null, "Error!");

                }
            } catch (Exception e) {

            }
        }
    }

    private void message() {
        switch (transactionnumber) {
            case 0:
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Withdraw Sucessfully");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Deposit Successfully");
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "Transferred Successfully");
                break;
            case 4:
                JOptionPane.showMessageDialog(null, "View Balance successfully");
                break;
            default:
                break;

        }
    }

    private void toTransfer() {
        String Query = "select * from bankaccounts where AccountNumber= '" + transferaccnum.getText() + "'";
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                id = Rs.getInt(1);
                balance = Rs.getInt(4);
            } else {
                JOptionPane.showMessageDialog(null, "Account number does not exist.Please Try Again");
                transferaccnum.setText("");
                transferaccnum.requestFocus();
            }
        } catch (Exception e) {

        }
    }

    class Withdraw extends DIGIBANK {

        private void getId() {
            String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                St = Con.createStatement();
                Rs = St.executeQuery(Query);
                if (Rs.next()) {
                    id = Rs.getInt(1);
                    balance = Rs.getInt(4);
                }
            } catch (Exception e) {

            }
        }

        public void Withdraw() {
            getId();
            String Query = "Update bankaccounts set Balance=? Where ID=?";

            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                PreparedStatement Ps = Con.prepareStatement(Query);
                Integer withdraw = Integer.valueOf(WithdrawAmount.getText());
                int newbalance;
                if (balance < withdraw) {
                    JOptionPane.showMessageDialog(null, "Insufficient Funds");
                    hidepane();
                    Withdraw.setVisible(true);
                    WithdrawAmount.setText("");
                    WithdrawAmount.requestFocus();
                } else {
                    newbalance = balance - withdraw;
                    Ps.setInt(1, newbalance);
                    Ps.setInt(2, id);

                }

                if (Ps.executeUpdate() == 1) {
                    hidepane();
                    this.transactionnumber = 1;
                    newbalance = balance - withdraw;
                    NumberFormat myFormat = NumberFormat.getInstance();
                    NumberFormat MyFormat = NumberFormat.getInstance();
                    MyFormat.setGroupingUsed(true);
                    myFormat.setGroupingUsed(true);
                    printreceipt.setText(printreceipt.getText() + "\n" + bankname.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "\n" + accnumber.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(balance) + "\n\n");
                    printreceipt.setText(printreceipt.getText() + "Transaction: Withdraw ₱" + myFormat.format(balance) + "- ₱" + MyFormat.format(withdraw) + "\n\n");
                    printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(newbalance) + "\n\n");
                    todeduct();

                    WithdrawAmount.setText("");

                } else {
                    JOptionPane.showMessageDialog(null, "Error!");

                }

            } catch (Exception e) {

            }
        }

    }

    class Deposit extends DIGIBANK {

        private void getId() {
            String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                St = Con.createStatement();
                Rs = St.executeQuery(Query);
                if (Rs.next()) {
                    id = Rs.getInt(1);
                    balance = Rs.getInt(4);
                }
            } catch (Exception e) {

            }
        }

        public void Deposit() {

            getId();
            String Query = "Update bankaccounts set Balance=? Where ID=?";

            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                PreparedStatement Ps = Con.prepareStatement(Query);
                Integer deposit = Integer.valueOf(Depositamount.getText());
                int newbalance = balance + deposit;
                Ps.setInt(1, newbalance);
                Ps.setInt(2, id);

                if (Ps.executeUpdate() == 1) {
                    hidepane();

                    Depositamount.setText("");
                    transactionnumber = 2;
                    NumberFormat myFormat = NumberFormat.getInstance();
                    printreceipt.setText(printreceipt.getText() + "\n" + bankname.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "\n" + accnumber.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(balance) + "\n\n");
                    printreceipt.setText(printreceipt.getText() + "Transaction: Deposit ₱" + myFormat.format(balance) + "+" + deposit + "\n\n");
                    printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(newbalance) + "\n\n");
                    todeduct();

                    toreceipt.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Error!");

                }
            } catch (Exception e) {

            }
        }
    }

    class Transfer extends DIGIBANK {

        private void getId() {
            String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                St = Con.createStatement();
                Rs = St.executeQuery(Query);
                if (Rs.next()) {
                    id = Rs.getInt(1);
                    balance = Rs.getInt(4);
                }
            } catch (Exception e) {

            }
        }

        public void Transfer() {
            if (transferamount.getText().equals("") || transferaccnum.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please input the required information!");
            } else if (transferaccnum.getText().equals(Accnumbetb.getText())) {
                JOptionPane.showMessageDialog(null, "This is your account number!");

            } else {

                hidepane();
                getId();
                String Query = "Update bankaccounts set Balance=? Where ID=?";

                try {
                    Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                    PreparedStatement Ps = Con.prepareStatement(Query);
                    Integer transfer = Integer.valueOf(transferamount.getText());
                    Integer transferacc = Integer.valueOf(transferaccnum.getText());

                    if (transfer > balance) {
                        JOptionPane.showMessageDialog(null, "Insufficient funds");
                    } else {
                        int newbalance = balance - transfer;
                        Ps.setInt(1, newbalance);
                        Ps.setInt(2, id);
                        toTransfer();
                        PreparedStatement PS = Con.prepareStatement(Query);
                        int addedbalance = balance + transfer;
                        PS.setInt(1, addedbalance);
                        PS.setInt(2, id);
                        transactionnumber = 3;

                        if (Ps.executeUpdate() == 1) {
                            hidepane();
                            NumberFormat myFormat = NumberFormat.getInstance();
                            myFormat.setGroupingUsed(true);
                            printreceipt.setText(printreceipt.getText() + "\n" + bankname.getText() + "\n");
                            printreceipt.setText(printreceipt.getText() + "\n" + accnumber.getText() + "\n");
                            printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(balance) + "\n\n");
                            printreceipt.setText(printreceipt.getText() + "Transaction: Transfer ₱" + myFormat.format(balance) + "-" + transfer + "\n\n");
                            printreceipt.setText(printreceipt.getText() + "Transferred to: " + transferacc + " ₱" + myFormat.format(balance) + "-" + transfer + "\n\n");
                            printreceipt.setText(printreceipt.getText() + "Balance: ₱" + myFormat.format(newbalance) + "\n\n");
                            if (sameacc) {
                                transferaccnum.setText("");
                                transferaccnum.requestFocus();
                            } else {
                                transferamount.setText("");
                                transferaccnum.setText("");
                                toreceipt.setVisible(true);
                                todeduct();
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Error!");

                        }
                    }
                } catch (Exception e) {

                }

            }
        }
    }

    class viewBalance extends DIGIBANK {

        private void getId() {
            String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                St = Con.createStatement();
                Rs = St.executeQuery(Query);
                if (Rs.next()) {
                    id = Rs.getInt(1);
                    balance = Rs.getInt(4);
                }
            } catch (Exception e) {

            }
        }

        public void viewBalance() {
            transact.setVisible(true);
            getId();

            String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                St = Con.createStatement();
                Rs = St.executeQuery(Query);
                if (Rs.next()) {
                    transactionnumber = 4;
                    NumberFormat myFormat = NumberFormat.getInstance();
                    myFormat.setGroupingUsed(true);

                    Balance.setText(myFormat.format(Rs.getInt(4)));
                    bankname.setText("Bank: " + Rs.getString(3));
                    accnumber.setText("Account Number: " + Rs.getString(2));
                    printreceipt.setText(printreceipt.getText() + "\n" + bankname.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "\n" + accnumber.getText() + "\n");
                    printreceipt.setText(printreceipt.getText() + "Transaction: View Balance ₱" + myFormat.format(balance) + "\n\n");

                    hidepane();
                    ViewBalance.setVisible(true);
                }
            } catch (Exception e) {

            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Time = new javax.swing.JLabel();
        TodayDateLbl = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        transaction = new javax.swing.JLayeredPane();
        transfer = new javax.swing.JButton();
        viewbalance = new javax.swing.JButton();
        withdraw = new javax.swing.JButton();
        EJECTd = new javax.swing.JButton();
        deposit1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        EnterPin = new javax.swing.JLayeredPane();
        jLabel2 = new javax.swing.JLabel();
        PIN = new javax.swing.JPasswordField();
        Accnumber = new javax.swing.JLayeredPane();
        Accnumbetb = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        choosebank = new javax.swing.JLayeredPane();
        BPI = new javax.swing.JButton();
        PNB = new javax.swing.JButton();
        BDO = new javax.swing.JButton();
        LBP = new javax.swing.JButton();
        ViewBalance = new javax.swing.JLayeredPane();
        bankname = new javax.swing.JLabel();
        accnumber = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Balance = new javax.swing.JLabel();
        Back = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        Withdraw = new javax.swing.JLayeredPane();
        banknamew = new javax.swing.JLabel();
        accnumberw = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        CancelW = new javax.swing.JButton();
        CLEAR1 = new javax.swing.JButton();
        WithdrawAmount = new javax.swing.JTextField();
        Deposit = new javax.swing.JLayeredPane();
        banknamed = new javax.swing.JLabel();
        accnumberd = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        CANCELd = new javax.swing.JButton();
        CLEARd = new javax.swing.JButton();
        Depositamount = new javax.swing.JTextField();
        Transfer = new javax.swing.JLayeredPane();
        jLabel3 = new javax.swing.JLabel();
        transferamount = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        transferaccnum = new javax.swing.JTextField();
        CANCELt = new javax.swing.JButton();
        CLEARt = new javax.swing.JButton();
        transact = new javax.swing.JLayeredPane();
        totransact = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        receipt = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        printreceipt = new javax.swing.JTextArea();
        receiptback = new javax.swing.JButton();
        toreceipt = new javax.swing.JLayeredPane();
        No = new javax.swing.JButton();
        Yes = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        Time.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        Time.setForeground(new java.awt.Color(255, 255, 255));
        Time.setText("Time");

        TodayDateLbl.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        TodayDateLbl.setForeground(new java.awt.Color(255, 255, 255));
        TodayDateLbl.setText("Date");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                windowopen(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        transaction.setForeground(new java.awt.Color(255, 255, 255));

        transfer.setBackground(new java.awt.Color(102, 102, 102));
        transfer.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        transfer.setForeground(new java.awt.Color(255, 255, 255));
        transfer.setText("Transfer");
        transfer.setBorderPainted(false);
        transfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferActionPerformed(evt);
            }
        });

        viewbalance.setBackground(new java.awt.Color(102, 102, 102));
        viewbalance.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        viewbalance.setForeground(new java.awt.Color(255, 255, 255));
        viewbalance.setText("View Balance");
        viewbalance.setBorderPainted(false);
        viewbalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewbalanceActionPerformed(evt);
            }
        });

        withdraw.setBackground(new java.awt.Color(102, 102, 102));
        withdraw.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        withdraw.setForeground(new java.awt.Color(255, 255, 255));
        withdraw.setText("Withdraw");
        withdraw.setBorderPainted(false);
        withdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withdrawActionPerformed(evt);
            }
        });

        EJECTd.setBackground(new java.awt.Color(255, 0, 0));
        EJECTd.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        EJECTd.setForeground(new java.awt.Color(255, 255, 255));
        EJECTd.setText("Eject Card");
        EJECTd.setBorderPainted(false);
        EJECTd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EJECTdActionPerformed(evt);
            }
        });

        deposit1.setBackground(new java.awt.Color(102, 102, 102));
        deposit1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        deposit1.setForeground(new java.awt.Color(255, 255, 255));
        deposit1.setText("Deposit");
        deposit1.setBorderPainted(false);
        deposit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deposit1ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Century Gothic", 0, 30)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("Welcome to DIGIBANK");

        transaction.setLayer(transfer, javax.swing.JLayeredPane.DEFAULT_LAYER);
        transaction.setLayer(viewbalance, javax.swing.JLayeredPane.DEFAULT_LAYER);
        transaction.setLayer(withdraw, javax.swing.JLayeredPane.DEFAULT_LAYER);
        transaction.setLayer(EJECTd, javax.swing.JLayeredPane.DEFAULT_LAYER);
        transaction.setLayer(deposit1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        transaction.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout transactionLayout = new javax.swing.GroupLayout(transaction);
        transaction.setLayout(transactionLayout);
        transactionLayout.setHorizontalGroup(
            transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transactionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(transactionLayout.createSequentialGroup()
                        .addGroup(transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(viewbalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(withdraw, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addGroup(transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(deposit1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(EJECTd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(transactionLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8))))
        );
        transactionLayout.setVerticalGroup(
            transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewbalance)
                    .addComponent(transfer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(transactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(withdraw)
                    .addComponent(deposit1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EJECTd)
                .addGap(53, 53, 53))
        );

        EnterPin.setForeground(new java.awt.Color(255, 255, 255));
        EnterPin.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Enter PIN:");

        PIN.setBackground(new java.awt.Color(102, 102, 102));
        PIN.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        PIN.setForeground(new java.awt.Color(255, 255, 255));
        PIN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PINActionPerformed(evt);
            }
        });
        PIN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PINKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PINKeyTyped(evt);
            }
        });

        EnterPin.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        EnterPin.setLayer(PIN, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout EnterPinLayout = new javax.swing.GroupLayout(EnterPin);
        EnterPin.setLayout(EnterPinLayout);
        EnterPinLayout.setHorizontalGroup(
            EnterPinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EnterPinLayout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(EnterPinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EnterPinLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel2))
                    .addComponent(PIN, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        EnterPinLayout.setVerticalGroup(
            EnterPinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EnterPinLayout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(jLabel2)
                .addGap(28, 28, 28)
                .addComponent(PIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        Accnumber.setForeground(new java.awt.Color(255, 255, 255));

        Accnumbetb.setBackground(new java.awt.Color(153, 153, 153));
        Accnumbetb.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        Accnumbetb.setForeground(new java.awt.Color(255, 255, 255));
        Accnumbetb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccnumbetbActionPerformed(evt);
            }
        });
        Accnumbetb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AccnumbetbKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AccnumbetbKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Enter Account Number:");

        Accnumber.setLayer(Accnumbetb, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Accnumber.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout AccnumberLayout = new javax.swing.GroupLayout(Accnumber);
        Accnumber.setLayout(AccnumberLayout);
        AccnumberLayout.setHorizontalGroup(
            AccnumberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AccnumberLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(AccnumberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Accnumbetb, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        AccnumberLayout.setVerticalGroup(
            AccnumberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AccnumberLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jLabel1)
                .addGap(63, 63, 63)
                .addComponent(Accnumbetb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        BPI.setBackground(new java.awt.Color(255, 0, 0));
        BPI.setForeground(new java.awt.Color(255, 255, 255));
        BPI.setText("BPI");
        BPI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPIActionPerformed(evt);
            }
        });

        PNB.setBackground(new java.awt.Color(102, 102, 255));
        PNB.setForeground(new java.awt.Color(255, 0, 0));
        PNB.setText("PNB");
        PNB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PNBActionPerformed(evt);
            }
        });

        BDO.setBackground(new java.awt.Color(0, 204, 0));
        BDO.setForeground(new java.awt.Color(255, 255, 0));
        BDO.setText("BDO");
        BDO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BDOActionPerformed(evt);
            }
        });

        LBP.setBackground(new java.awt.Color(255, 255, 0));
        LBP.setForeground(new java.awt.Color(51, 102, 0));
        LBP.setText("LBP");
        LBP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LBPActionPerformed(evt);
            }
        });

        choosebank.setLayer(BPI, javax.swing.JLayeredPane.DEFAULT_LAYER);
        choosebank.setLayer(PNB, javax.swing.JLayeredPane.DEFAULT_LAYER);
        choosebank.setLayer(BDO, javax.swing.JLayeredPane.DEFAULT_LAYER);
        choosebank.setLayer(LBP, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout choosebankLayout = new javax.swing.GroupLayout(choosebank);
        choosebank.setLayout(choosebankLayout);
        choosebankLayout.setHorizontalGroup(
            choosebankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(choosebankLayout.createSequentialGroup()
                .addGroup(choosebankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BPI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PNB, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
                .addGroup(choosebankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BDO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LBP, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        choosebankLayout.setVerticalGroup(
            choosebankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, choosebankLayout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addGroup(choosebankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(choosebankLayout.createSequentialGroup()
                        .addComponent(BDO)
                        .addGap(52, 52, 52)
                        .addComponent(LBP))
                    .addGroup(choosebankLayout.createSequentialGroup()
                        .addComponent(BPI)
                        .addGap(52, 52, 52)
                        .addComponent(PNB)))
                .addGap(65, 65, 65))
        );

        ViewBalance.setForeground(new java.awt.Color(255, 255, 255));

        bankname.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        bankname.setForeground(new java.awt.Color(255, 255, 255));
        bankname.setText("BankName");

        accnumber.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        accnumber.setForeground(new java.awt.Color(255, 255, 255));
        accnumber.setText("Account Number:");

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("BALANCE");

        Balance.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        Balance.setForeground(new java.awt.Color(255, 255, 255));
        Balance.setText("P2,000,000");

        Back.setBackground(new java.awt.Color(51, 204, 0));
        Back.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        Back.setForeground(new java.awt.Color(255, 255, 255));
        Back.setText("BACK");
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("PHP");

        ViewBalance.setLayer(bankname, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ViewBalance.setLayer(accnumber, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ViewBalance.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ViewBalance.setLayer(Balance, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ViewBalance.setLayer(Back, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ViewBalance.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout ViewBalanceLayout = new javax.swing.GroupLayout(ViewBalance);
        ViewBalance.setLayout(ViewBalanceLayout);
        ViewBalanceLayout.setHorizontalGroup(
            ViewBalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewBalanceLayout.createSequentialGroup()
                .addGroup(ViewBalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ViewBalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ViewBalanceLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(ViewBalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(bankname)
                                .addComponent(accnumber)))
                        .addGroup(ViewBalanceLayout.createSequentialGroup()
                            .addGap(113, 113, 113)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ViewBalanceLayout.createSequentialGroup()
                            .addGap(64, 64, 64)
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Balance)))
                    .addGroup(ViewBalanceLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(Back)))
                .addContainerGap(183, Short.MAX_VALUE))
        );
        ViewBalanceLayout.setVerticalGroup(
            ViewBalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewBalanceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bankname)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accnumber)
                .addGap(59, 59, 59)
                .addComponent(jLabel5)
                .addGap(36, 36, 36)
                .addGroup(ViewBalanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Balance)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(Back)
                .addGap(43, 43, 43))
        );

        Withdraw.setForeground(new java.awt.Color(255, 255, 255));

        banknamew.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        banknamew.setForeground(new java.awt.Color(255, 255, 255));
        banknamew.setText("BankName");

        accnumberw.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        accnumberw.setForeground(new java.awt.Color(255, 255, 255));
        accnumberw.setText("Account Number:");

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("WITHDRAW AMOUNT:");

        CancelW.setBackground(new java.awt.Color(102, 255, 0));
        CancelW.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        CancelW.setForeground(new java.awt.Color(255, 255, 255));
        CancelW.setText("CANCEL");
        CancelW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelWActionPerformed(evt);
            }
        });

        CLEAR1.setBackground(new java.awt.Color(255, 0, 0));
        CLEAR1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        CLEAR1.setForeground(new java.awt.Color(255, 255, 255));
        CLEAR1.setText("CLEAR");
        CLEAR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLEAR1ActionPerformed(evt);
            }
        });

        WithdrawAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WithdrawAmountActionPerformed(evt);
            }
        });
        WithdrawAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WithdrawAmountKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                WithdrawAmountKeyTyped(evt);
            }
        });

        Withdraw.setLayer(banknamew, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Withdraw.setLayer(accnumberw, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Withdraw.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Withdraw.setLayer(CancelW, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Withdraw.setLayer(CLEAR1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Withdraw.setLayer(WithdrawAmount, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout WithdrawLayout = new javax.swing.GroupLayout(Withdraw);
        Withdraw.setLayout(WithdrawLayout);
        WithdrawLayout.setHorizontalGroup(
            WithdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WithdrawLayout.createSequentialGroup()
                .addGroup(WithdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WithdrawLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(WithdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(banknamew)
                            .addComponent(accnumberw)))
                    .addGroup(WithdrawLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel6))
                    .addGroup(WithdrawLayout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(WithdrawAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(WithdrawLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(CancelW)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                .addComponent(CLEAR1)
                .addGap(34, 34, 34))
        );
        WithdrawLayout.setVerticalGroup(
            WithdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WithdrawLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(banknamew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accnumberw)
                .addGap(56, 56, 56)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(WithdrawAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(WithdrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelW)
                    .addComponent(CLEAR1))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        Deposit.setForeground(new java.awt.Color(255, 255, 255));

        banknamed.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        banknamed.setForeground(new java.awt.Color(255, 255, 255));
        banknamed.setText("BankName");

        accnumberd.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        accnumberd.setForeground(new java.awt.Color(255, 255, 255));
        accnumberd.setText("Account Number:");

        jLabel7.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("DEPOSIT AMOUNT:");

        CANCELd.setBackground(new java.awt.Color(102, 255, 0));
        CANCELd.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        CANCELd.setForeground(new java.awt.Color(255, 255, 255));
        CANCELd.setText("CANCEL");
        CANCELd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CANCELdActionPerformed(evt);
            }
        });

        CLEARd.setBackground(new java.awt.Color(255, 0, 0));
        CLEARd.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        CLEARd.setForeground(new java.awt.Color(255, 255, 255));
        CLEARd.setText("CLEAR");
        CLEARd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLEARdActionPerformed(evt);
            }
        });

        Depositamount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DepositamountKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DepositamountKeyTyped(evt);
            }
        });

        Deposit.setLayer(banknamed, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Deposit.setLayer(accnumberd, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Deposit.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Deposit.setLayer(CANCELd, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Deposit.setLayer(CLEARd, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Deposit.setLayer(Depositamount, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout DepositLayout = new javax.swing.GroupLayout(Deposit);
        Deposit.setLayout(DepositLayout);
        DepositLayout.setHorizontalGroup(
            DepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DepositLayout.createSequentialGroup()
                .addGroup(DepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DepositLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(DepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(banknamed)
                            .addComponent(accnumberd)))
                    .addGroup(DepositLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel7))
                    .addGroup(DepositLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(CANCELd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(CLEARd)
                .addGap(43, 43, 43))
            .addGroup(DepositLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(Depositamount, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DepositLayout.setVerticalGroup(
            DepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DepositLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(banknamed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accnumberd)
                .addGap(56, 56, 56)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Depositamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(DepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CANCELd)
                    .addComponent(CLEARd))
                .addGap(34, 34, 34))
        );

        Transfer.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TRANSFER AMOUNT:");

        transferamount.setBackground(new java.awt.Color(153, 153, 153));
        transferamount.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        transferamount.setForeground(new java.awt.Color(255, 255, 255));
        transferamount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                transferamountKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter Account Number:");

        transferaccnum.setBackground(new java.awt.Color(153, 153, 153));
        transferaccnum.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        transferaccnum.setForeground(new java.awt.Color(255, 255, 255));
        transferaccnum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                transferaccnumKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                transferaccnumKeyTyped(evt);
            }
        });

        CANCELt.setBackground(new java.awt.Color(102, 255, 0));
        CANCELt.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        CANCELt.setForeground(new java.awt.Color(255, 255, 255));
        CANCELt.setText("CANCEL");
        CANCELt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CANCELtActionPerformed(evt);
            }
        });

        CLEARt.setBackground(new java.awt.Color(255, 51, 0));
        CLEARt.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        CLEARt.setForeground(new java.awt.Color(255, 255, 255));
        CLEARt.setText("CLEAR");
        CLEARt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLEARtActionPerformed(evt);
            }
        });

        Transfer.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Transfer.setLayer(transferamount, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Transfer.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Transfer.setLayer(transferaccnum, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Transfer.setLayer(CANCELt, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Transfer.setLayer(CLEARt, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout TransferLayout = new javax.swing.GroupLayout(Transfer);
        Transfer.setLayout(TransferLayout);
        TransferLayout.setHorizontalGroup(
            TransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TransferLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(TransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(transferamount)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(transferaccnum)
                    .addGroup(TransferLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(TransferLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(CANCELt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(CLEARt)
                .addGap(76, 76, 76))
        );
        TransferLayout.setVerticalGroup(
            TransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TransferLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(transferamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(transferaccnum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(TransferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CANCELt)
                    .addComponent(CLEARt))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        transact.setForeground(new java.awt.Color(255, 255, 255));

        totransact.setBackground(new java.awt.Color(153, 153, 153));
        totransact.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        totransact.setForeground(new java.awt.Color(255, 255, 255));
        totransact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totransactActionPerformed(evt);
            }
        });
        totransact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                totransactKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                totransactKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Enter Account Number:");

        transact.setLayer(totransact, javax.swing.JLayeredPane.DEFAULT_LAYER);
        transact.setLayer(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout transactLayout = new javax.swing.GroupLayout(transact);
        transact.setLayout(transactLayout);
        transactLayout.setHorizontalGroup(
            transactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transactLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(transactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totransact))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        transactLayout.setVerticalGroup(
            transactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transactLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel11)
                .addGap(63, 63, 63)
                .addComponent(totransact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(172, Short.MAX_VALUE))
        );

        receipt.setForeground(new java.awt.Color(255, 255, 255));

        printreceipt.setColumns(20);
        printreceipt.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        printreceipt.setRows(5);
        printreceipt.setText("***************PEU  BANK***********************");
        printreceipt.setWrapStyleWord(true);
        jScrollPane1.setViewportView(printreceipt);

        receiptback.setBackground(new java.awt.Color(153, 153, 0));
        receiptback.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        receiptback.setForeground(new java.awt.Color(255, 255, 255));
        receiptback.setText("Back");
        receiptback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receiptbackActionPerformed(evt);
            }
        });

        receipt.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        receipt.setLayer(receiptback, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout receiptLayout = new javax.swing.GroupLayout(receipt);
        receipt.setLayout(receiptLayout);
        receiptLayout.setHorizontalGroup(
            receiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
            .addGroup(receiptLayout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addComponent(receiptback)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        receiptLayout.setVerticalGroup(
            receiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(receiptLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(receiptback))
        );

        toreceipt.setForeground(new java.awt.Color(255, 255, 255));

        No.setBackground(new java.awt.Color(255, 51, 51));
        No.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        No.setForeground(new java.awt.Color(255, 255, 255));
        No.setText("NO");
        No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoActionPerformed(evt);
            }
        });

        Yes.setBackground(new java.awt.Color(153, 153, 0));
        Yes.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        Yes.setForeground(new java.awt.Color(255, 255, 255));
        Yes.setText("YES");
        Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YesActionPerformed(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Do you want a receipt for this transaction?");

        toreceipt.setLayer(No, javax.swing.JLayeredPane.DEFAULT_LAYER);
        toreceipt.setLayer(Yes, javax.swing.JLayeredPane.DEFAULT_LAYER);
        toreceipt.setLayer(jLabel12, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout toreceiptLayout = new javax.swing.GroupLayout(toreceipt);
        toreceipt.setLayout(toreceiptLayout);
        toreceiptLayout.setHorizontalGroup(
            toreceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toreceiptLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(Yes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(No)
                .addGap(68, 68, 68))
            .addGroup(toreceiptLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel12)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        toreceiptLayout.setVerticalGroup(
            toreceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, toreceiptLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addGroup(toreceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Yes)
                    .addComponent(No))
                .addGap(81, 81, 81))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Accnumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 55, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(transaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(87, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(ViewBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(64, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Withdraw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(67, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Deposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(62, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Transfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(62, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(choosebank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(60, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(transact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(receipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(65, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(EnterPin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(63, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(toreceipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(65, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Accnumber)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(transaction)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(ViewBalance)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Withdraw)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Deposit)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Transfer)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addComponent(choosebank)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(transact)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(receipt)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(EnterPin)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(toreceipt)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 280, 390, 320));

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/atm.jpg"))); // NOI18N
        jLabel9.setText("jLabel9");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, 960, 920));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void viewbalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewbalanceActionPerformed
        viewBalance viewbalance = new viewBalance();
        todeduct();

        viewbalance.viewBalance();
    }//GEN-LAST:event_viewbalanceActionPerformed

    private void withdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withdrawActionPerformed
        String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                NumberFormat myFormat = NumberFormat.getInstance();
                myFormat.setGroupingUsed(true);
                banknamew.setText("Bank: " + Rs.getString(3));
                accnumberw.setText("Account Number: " + Rs.getString(2));
                hidepane();
                Withdraw.setVisible(true);

            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_withdrawActionPerformed

    private void EJECTdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EJECTdActionPerformed
        hidepane();
        clear();
        Accnumber.setVisible(true);
    }//GEN-LAST:event_EJECTdActionPerformed

    private void PINActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PINActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PINActionPerformed

    private void PINKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PINKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                St = Con.createStatement();
                Rs = St.executeQuery(Query);
                if (Rs.next()) {
                    if (!Rs.getString(5).equals(PIN.getText())) {
                        JOptionPane.showMessageDialog(null, "Invalid PIN! Please try again");
                        PIN.setText("");
                        PIN.requestFocus();
                    } else {
                        PIN.setText("");
                        NumberFormat myFormat = NumberFormat.getInstance();
                        myFormat.setGroupingUsed(true);
                        Balance.setText(myFormat.format(Rs.getInt(4)));
                        bankname.setText("Bank: " + Rs.getString(3));
                        accnumber.setText("Account Number: " + Rs.getString(2));
                        hidepane();
                        transaction.setVisible(true);
                        message();

                    }

                }
            } catch (Exception e) {

            }
        }

    }//GEN-LAST:event_PINKeyPressed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        hidepane();
        toreceipt.setVisible(true);
    }//GEN-LAST:event_BackActionPerformed

    private void deposit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deposit1ActionPerformed
        String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                NumberFormat myFormat = NumberFormat.getInstance();
                myFormat.setGroupingUsed(true);
                banknamed.setText("Bank: " + Rs.getString(3));
                accnumberd.setText("Account Number: " + Rs.getString(2));
                hidepane();
                Deposit.setVisible(true);

            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_deposit1ActionPerformed

    private void AccnumbetbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AccnumbetbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AccnumbetbActionPerformed

    private void AccnumbetbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AccnumbetbKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (Accnumbetb.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please input Account Number");
                clear();
                Accnumbetb.requestFocus();
            } else {
                String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
                try {
                    Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
                    St = Con.createStatement();
                    Rs = St.executeQuery(Query);
                    if (Rs.next()) {
                        hidepane();
                        choosebank.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Account Number! Please Try again.");
                        clear();
                        Accnumbetb.requestFocus();
                    }
                } catch (Exception e) {

                }
            }

        }
    }//GEN-LAST:event_AccnumbetbKeyPressed

    private void transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferActionPerformed
        hidepane();
        Transfer.setVisible(true);
    }//GEN-LAST:event_transferActionPerformed

    private void WithdrawAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WithdrawAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_WithdrawAmountActionPerformed

    private void CancelWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelWActionPerformed
        hidepane();
        WithdrawAmount.setText("");
        transaction.setVisible(true);

    }//GEN-LAST:event_CancelWActionPerformed

    private void CANCELdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CANCELdActionPerformed
        hidepane();
        Depositamount.setText("");
        transaction.setVisible(true);

    }//GEN-LAST:event_CANCELdActionPerformed

    private void CANCELtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CANCELtActionPerformed
        hidepane();
        transferamount.setText("");
        transferaccnum.setText("");
        transaction.setVisible(true);

    }//GEN-LAST:event_CANCELtActionPerformed

    private void CLEAR1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLEAR1ActionPerformed
        WithdrawAmount.setText("");

    }//GEN-LAST:event_CLEAR1ActionPerformed

    private void CLEARdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLEARdActionPerformed
        Depositamount.setText("");
    }//GEN-LAST:event_CLEARdActionPerformed

    private void CLEARtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLEARtActionPerformed
        transferamount.setText("");
        transferaccnum.setText("");
    }//GEN-LAST:event_CLEARtActionPerformed

    private void windowopen(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowopen
         final int maxx = 384;
        final int miny = 0;

        Thread animation = new Thread(new Runnable() {
            @Override
            public void run() {
                int x = 20;
                int y = 30;
                boolean checked = true;
                while (true) {
                    if (checked) {
                        jLabel8.setLocation(x, y);
                        x += 20;
                    } else {
                        x = 20;
                    }
                    if (x > maxx) {
                        x = -350;
                    }
                    if (x < miny) {
                        checked = true;
                    }

                    try {
                        Thread.sleep(200);

                    } catch (Exception e) {

                    }
                }
            }

        });
        animation.start();
    }//GEN-LAST:event_windowopen

    private void AccnumbetbKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AccnumbetbKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_AccnumbetbKeyTyped

    private void PINKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PINKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_PINKeyTyped

    private void WithdrawAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WithdrawAmountKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_WithdrawAmountKeyTyped

    private void DepositamountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DepositamountKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_DepositamountKeyTyped

    private void transferamountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transferamountKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_transferamountKeyTyped

    private void transferaccnumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transferaccnumKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_transferaccnumKeyTyped

    private void BPIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPIActionPerformed
        String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                chosenbank = "BPI";

                if (!Rs.getString(3).equals("BPI")) {
                    tocharge = true;
                    JOptionPane.showMessageDialog(null, "Warning!Transacting with a different bank will have additional charges");
                    hidepane();
                    EnterPin.setVisible(true);

                } else {
                    tocharge = false;

                    hidepane();
                    EnterPin.setVisible(true);
                }
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_BPIActionPerformed

    private void PNBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PNBActionPerformed
        String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                chosenbank = "PNB";

                if (!Rs.getString(3).equals("PNB")) {
                    tocharge = true;

                    JOptionPane.showMessageDialog(null, "Warning!Transacting with a different bank will have additional charges");
                    hidepane();
                    EnterPin.setVisible(true);
                } else {
                    tocharge = false;

                    hidepane();
                    EnterPin.setVisible(true);

                }
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_PNBActionPerformed

    private void BDOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BDOActionPerformed
        String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                chosenbank = "BDO";

                if (!Rs.getString(3).equals("BDO")) {
                    tocharge = true;

                    JOptionPane.showMessageDialog(null, "Warning!Transacting with a different bank will have additional charges");
                    hidepane();
                    EnterPin.setVisible(true);
                } else {
                    tocharge = false;

                    hidepane();
                    EnterPin.setVisible(true);
                }
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_BDOActionPerformed

    private void LBPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LBPActionPerformed
        String Query = "select * from bankaccounts where AccountNumber= '" + Accnumbetb.getText() + "'";

        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery(Query);
            if (Rs.next()) {
                chosenbank = "LBP";

                if (!Rs.getString(3).equals("LBP")) {
                    tocharge = true;

                    JOptionPane.showMessageDialog(null, "Warning!Transacting with a different bank will have additional charges");
                    hidepane();
                    transaction.setVisible(true);
                } else {
                    tocharge = false;
                    hidepane();
                    transaction.setVisible(true);
                }
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_LBPActionPerformed

    private void WithdrawAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WithdrawAmountKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Withdraw withdraw = new Withdraw();
            withdraw.Withdraw();
            transactionnumber = 1;
            toreceipt.setVisible(true);

        }

    }//GEN-LAST:event_WithdrawAmountKeyPressed

    private void DepositamountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DepositamountKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Deposit deposit = new Deposit();
            deposit.Deposit();
            transactionnumber = 2;
            toreceipt.setVisible(true);

        }

    }//GEN-LAST:event_DepositamountKeyPressed

    private void transferaccnumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transferaccnumKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Transfer transfer = new Transfer();
            transfer.Transfer();
            transactionnumber = 3;
            transferaccnum.setText("");
            transferaccnum.requestFocus();
        }
    }//GEN-LAST:event_transferaccnumKeyPressed

    private void totransactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totransactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totransactActionPerformed

    private void totransactKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totransactKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (totransact.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please input Account Number");
                totransact.setText("");

                totransact.requestFocus();

            }
            if (totransact.getText().equals(Accnumbetb.getText())) {

                totransact.setText("");
                hidepane();
                EnterPin.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Account number!  Please try again");
                totransact.requestFocus();
            }

        }
    }//GEN-LAST:event_totransactKeyPressed

    private void totransactKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totransactKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_totransactKeyTyped

    private void receiptbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receiptbackActionPerformed
        hidepane();
        transact.setVisible(true);

    }//GEN-LAST:event_receiptbackActionPerformed

    private void NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoActionPerformed
        hidepane();
        transact.setVisible(true);

    }//GEN-LAST:event_NoActionPerformed

    private void YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YesActionPerformed
        hidepane();
        receipt.setVisible(true);
    }//GEN-LAST:event_YesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DIGIBANK.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DIGIBANK.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DIGIBANK.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DIGIBANK.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DIGIBANK().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane Accnumber;
    private javax.swing.JTextField Accnumbetb;
    private javax.swing.JButton BDO;
    private javax.swing.JButton BPI;
    private javax.swing.JButton Back;
    private javax.swing.JLabel Balance;
    private javax.swing.JButton CANCELd;
    private javax.swing.JButton CANCELt;
    private javax.swing.JButton CLEAR1;
    private javax.swing.JButton CLEARd;
    private javax.swing.JButton CLEARt;
    private javax.swing.JButton CancelW;
    private javax.swing.JLayeredPane Deposit;
    private javax.swing.JTextField Depositamount;
    private javax.swing.JButton EJECTd;
    private javax.swing.JLayeredPane EnterPin;
    private javax.swing.JButton LBP;
    private javax.swing.JButton No;
    private javax.swing.JPasswordField PIN;
    private javax.swing.JButton PNB;
    private javax.swing.JLabel Time;
    private javax.swing.JLabel TodayDateLbl;
    private javax.swing.JLayeredPane Transfer;
    private javax.swing.JLayeredPane ViewBalance;
    private javax.swing.JLayeredPane Withdraw;
    private javax.swing.JTextField WithdrawAmount;
    private javax.swing.JButton Yes;
    private javax.swing.JLabel accnumber;
    private javax.swing.JLabel accnumberd;
    private javax.swing.JLabel accnumberw;
    private javax.swing.JLabel bankname;
    private javax.swing.JLabel banknamed;
    private javax.swing.JLabel banknamew;
    private javax.swing.JLayeredPane choosebank;
    private javax.swing.JButton deposit1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea printreceipt;
    private javax.swing.JLayeredPane receipt;
    private javax.swing.JButton receiptback;
    private javax.swing.JLayeredPane toreceipt;
    private javax.swing.JTextField totransact;
    private javax.swing.JLayeredPane transact;
    private javax.swing.JLayeredPane transaction;
    private javax.swing.JButton transfer;
    private javax.swing.JTextField transferaccnum;
    private javax.swing.JTextField transferamount;
    private javax.swing.JButton viewbalance;
    private javax.swing.JButton withdraw;
    // End of variables declaration//GEN-END:variables
}
