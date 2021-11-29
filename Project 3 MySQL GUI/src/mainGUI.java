import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class mainGUI extends JFrame
{
        // JLabel
        
        private JLabel passwordLabel;
        private JLabel ConnectStatLabel;
        private JComboBox driverDrop;
        private JComboBox urlListDrop;
        public JTextField userUsername;
        private JPasswordField userPassword;
        private JLabel driverLabel;
        private JLabel dbURLLabel;
        private JLabel usernameLabel;
        private JButton connectDBbtn;
        private JButton clearCommandButton;
        private JButton executeCommandButton;
        private JButton clearQueryButton;
        private JTextArea sqlCommandTA;
        private ResultSetTableModel tableModel = null;
        private ResultSetTableModel operationModel = null;
        private JTable table;
        public String uName;
        private int num_queries = 0;
        private int num_updates = 0;
        private Connection dbConnection;
        private Connection connectOperations;
        private Statement operStatement;

        private boolean connectedToDatabase = false;

        public mainGUI() throws ClassNotFoundException, SQLException, IOException 
        {
                this.createInstanceGUIComponents();
                
                
                this.connectDBbtn.addActionListener(new ActionListener() 
                {

                        @Override
                        public void actionPerformed(ActionEvent arg0) 
                        {
                                
                                try
                                {
                                        Class.forName(String.valueOf(driverDrop.getSelectedItem()));
                                } catch (ClassNotFoundException e) 
                                {       

                                        ConnectStatLabel.setText("No Connection");
                                        ConnectStatLabel.setForeground(Color.RED);
                                        e.printStackTrace();
                                        table.setModel(new DefaultTableModel());
                                        tableModel = null;
                                }
                                
                                try 
                                {
                                        if(connectedToDatabase == true)
                                        {
                                                dbConnection.close();
                                                
                                                ConnectStatLabel.setText("No Connection");
                                                ConnectStatLabel.setForeground(Color.RED);
                                                
                                                connectedToDatabase = false;
                                                
                                                table.setModel(new DefaultTableModel());
                                                tableModel = null;
                                        }

                                        uName = userUsername.getText();
                                                
                                        dbConnection = DriverManager.getConnection(String.valueOf(urlListDrop.getSelectedItem()), userUsername.getText(), userPassword.getText());
                                        connectOperations = DriverManager.getConnection("jdbc:mysql://localhost:3306/operationslog", "root", "client");

                                        operStatement = connectOperations.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                        
                                        System.out.println(uName);
                                        
                                        ConnectStatLabel.setText("Connected to " + String.valueOf(urlListDrop.getSelectedItem()));
                                        ConnectStatLabel.setForeground(Color.GREEN);
                                        
                                        connectedToDatabase = true;

                                } catch (SQLException e) 
                                {
                                        
                                        ConnectStatLabel.setText("No Connection");
                                        ConnectStatLabel.setForeground(Color.RED);
                                        
                                        table.setModel(new DefaultTableModel());
                                        tableModel = null;
                                        e.printStackTrace();
                                }
                                
                        }
                        
                });
                
                
        
                this.clearCommandButton.addActionListener(new ActionListener() 
                {

                        @Override
                        public void actionPerformed(ActionEvent arg0) 
                        {
                                sqlCommandTA.setText("");
                        }
                        
                });
                
        
                this.executeCommandButton.addActionListener(new ActionListener() 
                {

                        @Override
                        public void actionPerformed(ActionEvent arg0)
                        {
                                

                                if(connectedToDatabase == true && tableModel == null)
                                {
                                        try 
                                        {
                                                
                                                tableModel = new ResultSetTableModel(sqlCommandTA.getText(), dbConnection);
                                                table.setModel(tableModel);
                                                operStatement.executeUpdate("update operationscount set num_queries= num_queries+1;"); //Remove late if failure to get increment to num queries
                                                System.out.println(num_queries);
                                                //operationModel = new ResultSetTableModel(sqlCommandTA.getText(), connectOperations, uName);

                                        } catch (ClassNotFoundException | SQLException | IOException e)
                                        {
                                                 table.setModel(new DefaultTableModel());
                                                 tableModel = null;
                                                JOptionPane.showMessageDialog( null, 
                                                e.getMessage(), "Database error", 
                                                JOptionPane.ERROR_MESSAGE );
                                                e.printStackTrace();

                                        }
                                }
                                else if(connectedToDatabase == true && tableModel != null)
                                {
                                        
                                        String query = sqlCommandTA.getText(); 

                                        if(query.contains("select") || query.contains("SELECT"))
                                        {
                                                try 
                                                {
                                                        tableModel.setQuery(query);
                                                        num_queries++;
                                                        
                                                        System.out.println(num_queries);
                                                        operStatement.executeUpdate("update operationscount set num_queries= num_queries+1;");
                                                        //operationModel.setOperationQuery("update operationscount set num_queries= num_queries+1;");

                                                } catch (IllegalStateException | SQLException e) 
                                                {
                                                        
                                                         table.setModel(new DefaultTableModel());
                                                         tableModel = null;
                                                        
                                                         JOptionPane.showMessageDialog( null, 
                                                        e.getMessage(), "Database error", 
                                                        JOptionPane.ERROR_MESSAGE );
                                                
                                                        e.printStackTrace();
                                                }
                                        }
                                        
                                        else
                                        {
                                                try 
                                                {
                                                        System.out.println("FLAG"); //Testing if code reaches here
                                                        tableModel.setUpdate(query);
                                                        num_updates++; //Variable to see if code gets num  updates

                                                        System.out.println(num_updates);
                                                        operStatement.executeUpdate("update operationscount set num_updates= num_updates+1;");
                                                        //operationModel.setOperationUpdate("update operationscount set num_updates= num_updates+1;");
                                                        
                                                        table.setModel(new DefaultTableModel());
                                                        tableModel = null;
                                                } catch (IllegalStateException | SQLException e) 
                                                {
                                                        
                                                         table.setModel(new DefaultTableModel());
                                                         tableModel = null;
                                                        
                                                         JOptionPane.showMessageDialog( null, 
                                                        e.getMessage(), "Database error", 
                                                        JOptionPane.ERROR_MESSAGE );
                                                         
                                                        e.printStackTrace();
                                                }
                                        }
                                }
                        
                                
                                
                                
                                
                        }
                        
                });
                
                 
                this.clearQueryButton.addActionListener(new ActionListener() 
                {

                        @Override
                        public void actionPerformed(ActionEvent arg0) 
                        {
   
                                table.setModel(new DefaultTableModel());
                                tableModel = null;
                                
                        }
                        
                });

                
                JPanel labelsAndTextFields = new JPanel(new GridLayout(7, 3));

                labelsAndTextFields.add(this.driverLabel);
                labelsAndTextFields.add(this.driverDrop);
                labelsAndTextFields.add(this.dbURLLabel);
                labelsAndTextFields.add(this.urlListDrop);
                labelsAndTextFields.add(this.usernameLabel);
                labelsAndTextFields.add(this.userUsername);
                labelsAndTextFields.add(this.passwordLabel);
                labelsAndTextFields.add(this.userPassword);


                JPanel buttons = new JPanel(new GridLayout(1, 4));
                buttons.add(this.ConnectStatLabel);
                buttons.add(this.clearCommandButton);
                buttons.add(this.executeCommandButton);
                buttons.add(this.connectDBbtn);
                

                
                JPanel top = new JPanel(new GridLayout(1, 2));
                top.add(labelsAndTextFields);
                top.add(this.sqlCommandTA);
                
                
                JPanel bot = new JPanel();
                bot.setLayout(new BorderLayout(20,0));
                bot.add(new JScrollPane(this.table), BorderLayout.NORTH);
                bot.add(this.clearQueryButton, BorderLayout.SOUTH);
                
                add(top, BorderLayout.NORTH);
                add(buttons, BorderLayout.CENTER);
                add(bot, BorderLayout.SOUTH);
                
                
              setDefaultCloseOperation( DISPOSE_ON_CLOSE );
              
              addWindowListener(new WindowAdapter() 
                 {
                    public void windowClosed( WindowEvent event )
                    {
                        try 
                        {
                                                if(!dbConnection.isClosed())
                                                        dbConnection.close();
                                        } catch (SQLException e) 
                        {
                                                e.printStackTrace();
                                        }
                       System.exit( 0 );
                    }
                 });

        }


        public void createInstanceGUIComponents() throws ClassNotFoundException, SQLException, IOException 
        {
                String[] driverString = { "com.mysql.jdbc.Driver", "" };
                String[] dataBaseURLString = { "jdbc:mysql://localhost:3306/Project3", "" };

                this.driverLabel = new JLabel("JDBC Driver");
                this.dbURLLabel = new JLabel("Database URL");
                this.usernameLabel = new JLabel("Username");
                this.passwordLabel = new JLabel("Password");
                this.ConnectStatLabel = new JLabel("No Connection Now");
                this.ConnectStatLabel.setForeground(Color.RED);

                this.userUsername = new JTextField();
                this.userPassword = new JPasswordField();

                this.driverDrop = new JComboBox(driverString);
                this.driverDrop.setSelectedIndex(0);
                this.urlListDrop = new JComboBox(dataBaseURLString);

                this.connectDBbtn = new JButton("Connect to Database");
                this.clearCommandButton = new JButton("Clear SQL Command");
                this.executeCommandButton = new JButton("Execute SQL Command");
                this.clearQueryButton = new JButton("Clear Result Window");

                this.sqlCommandTA = new JTextArea(3, 75);
                this.sqlCommandTA.setWrapStyleWord(true);
                this.sqlCommandTA.setLineWrap(true);
                
                this.table = new JTable();
        }
}