@echo off
echo Fixing LoginWindow compilation issue...
echo.

REM Create a fixed version of LoginWindow.java
(
echo package com.bank.ui;
echo.
echo import com.bank.dao.UserDAO;
echo import com.bank.util.DatabaseConnection;
echo.
echo import javax.swing.*;
echo import java.awt.*;
echo import java.awt.event.ActionEvent;
echo import java.awt.event.ActionListener;
echo import java.awt.event.KeyEvent;
echo import java.awt.event.KeyListener;
echo.
echo public class LoginWindow extends JFrame {
echo     private JTextField usernameField;
echo     private JPasswordField passwordField;
echo     private JButton loginButton;
echo     private JButton exitButton;
echo     private UserDAO userDAO;
echo     
echo     public LoginWindow() {
echo         userDAO = new UserDAO();
echo         initializeComponents();
echo         setupLayout();
echo         setupEventHandlers();
echo         setupWindow();
echo     }
echo     
echo     private void initializeComponents() {
echo         usernameField = new JTextField(15);
echo         passwordField = new JPasswordField(15);
echo         loginButton = new JButton("Login");
echo         exitButton = new JButton("Exit");
echo         
echo         usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
echo         passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
echo         loginButton.setFont(new Font("Arial", Font.BOLD, 14));
echo         exitButton.setFont(new Font("Arial", Font.BOLD, 14));
echo     }
echo     
echo     private void setupLayout() {
echo         setLayout(new BorderLayout());
echo         
echo         JPanel mainPanel = new JPanel(new GridBagLayout());
echo         mainPanel.setBackground(new Color(240, 248, 255));
echo         GridBagConstraints gbc = new GridBagConstraints();
echo         gbc.insets = new Insets(10, 10, 10, 10);
echo         gbc.fill = GridBagConstraints.HORIZONTAL;
echo         
echo         JLabel titleLabel = new JLabel("Bank Management System", JLabel.CENTER);
echo         titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
echo         titleLabel.setForeground(new Color(0, 102, 204));
echo         gbc.gridx = 0;
echo         gbc.gridy = 0;
echo         gbc.gridwidth = 2;
echo         gbc.insets = new Insets(20, 10, 30, 10);
echo         mainPanel.add(titleLabel, gbc);
echo         
echo         gbc.insets = new Insets(10, 10, 10, 10);
echo         gbc.gridwidth = 1;
echo         
echo         JLabel usernameLabel = new JLabel("Username:");
echo         usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
echo         gbc.gridx = 0;
echo         gbc.gridy = 1;
echo         gbc.anchor = GridBagConstraints.EAST;
echo         mainPanel.add(usernameLabel, gbc);
echo         
echo         gbc.gridx = 1;
echo         gbc.gridy = 1;
echo         gbc.anchor = GridBagConstraints.WEST;
echo         mainPanel.add(usernameField, gbc);
echo         
echo         JLabel passwordLabel = new JLabel("Password:");
echo         passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
echo         gbc.gridx = 0;
echo         gbc.gridy = 2;
echo         gbc.anchor = GridBagConstraints.EAST;
echo         mainPanel.add(passwordLabel, gbc);
echo         
echo         gbc.gridx = 1;
echo         gbc.gridy = 2;
echo         gbc.anchor = GridBagConstraints.WEST;
echo         mainPanel.add(passwordField, gbc);
echo         
echo         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
echo         buttonPanel.setBackground(new Color(240, 248, 255));
echo         buttonPanel.add(loginButton);
echo         buttonPanel.add(exitButton);
echo         
echo         gbc.gridx = 0;
echo         gbc.gridy = 3;
echo         gbc.gridwidth = 2;
echo         gbc.anchor = GridBagConstraints.CENTER;
echo         mainPanel.add(buttonPanel, gbc);
echo         
echo         add(mainPanel, BorderLayout.CENTER);
echo         
echo         JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
echo         infoPanel.setBackground(new Color(200, 200, 200));
echo         JLabel infoLabel = new JLabel("Default: admin / admin123");
echo         infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
echo         infoPanel.add(infoLabel);
echo         add(infoPanel, BorderLayout.SOUTH);
echo     }
echo     
echo     private void setupEventHandlers() {
echo         loginButton.addActionListener(new ActionListener() {
echo             @Override
echo             public void actionPerformed(ActionEvent e) {
echo                 performLogin();
echo             }
echo         });
echo         
echo         exitButton.addActionListener(new ActionListener() {
echo             @Override
echo             public void actionPerformed(ActionEvent e) {
echo                 System.exit(0);
echo             }
echo         });
echo         
echo         passwordField.addKeyListener(new KeyListener() {
echo             @Override
echo             public void keyPressed(KeyEvent e) {
echo                 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
echo                     performLogin();
echo                 }
echo             }
echo             
echo             @Override
echo             public void keyReleased(KeyEvent e) {}
echo             
echo             @Override
echo             public void keyTyped(KeyEvent e) {}
echo         });
echo         
echo         usernameField.addKeyListener(new KeyListener() {
echo             @Override
echo             public void keyPressed(KeyEvent e) {
echo                 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
echo                     passwordField.requestFocus();
echo                 }
echo             }
echo             
echo             @Override
echo             public void keyReleased(KeyEvent e) {}
echo             
echo             @Override
echo             public void keyTyped(KeyEvent e) {}
echo         });
echo     }
echo     
echo     private void performLogin() {
echo         String username = usernameField.getText().trim();
echo         String password = new String(passwordField.getPassword()).trim();
echo         
echo         if (username.isEmpty() || password.isEmpty()) {
echo             JOptionPane.showMessageDialog(this, 
echo                 "Please enter both username and password", 
echo                 "Login Error", 
echo                 JOptionPane.ERROR_MESSAGE);
echo             return;
echo         }
echo         
echo         if (userDAO.authenticateUser(username, password)) {
echo             String role = userDAO.getUserRole(username);
echo             JOptionPane.showMessageDialog(this, 
echo                 "Login successful! Welcome " + username, 
echo                 "Success", 
echo                 JOptionPane.INFORMATION_MESSAGE);
echo             
echo             DashboardWindow dashboard = new DashboardWindow(username, role);
echo             dashboard.setVisible(true);
echo             this.dispose();
echo         } else {
echo             JOptionPane.showMessageDialog(this, 
echo                 "Invalid username or password", 
echo                 "Login Failed", 
echo                 JOptionPane.ERROR_MESSAGE);
echo             passwordField.setText("");
echo             passwordField.requestFocus();
echo         }
echo     }
echo     
echo     private void setupWindow() {
echo         setTitle("Bank Management System - Login");
echo         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
echo         setSize(400, 300);
echo         setLocationRelativeTo(null);
echo         setResizable(false);
echo     }
echo     
echo     public static void main(String[] args) {
echo         SwingUtilities.invokeLater(new Runnable() {
echo             @Override
echo             public void run() {
echo                 if (!DatabaseConnection.testConnection()) {
echo                     JOptionPane.showMessageDialog(null, 
echo                         "Failed to connect to database. Please check your MySQL connection.", 
echo                         "Database Error", 
echo                         JOptionPane.ERROR_MESSAGE);
echo                     System.exit(1);
echo                 }
echo                 
echo                 new LoginWindow().setVisible(true);
echo             }
echo         });
echo     }
echo }
) > src\main\java\com\bank\ui\LoginWindow.java

echo LoginWindow.java has been fixed!
echo.
pause
