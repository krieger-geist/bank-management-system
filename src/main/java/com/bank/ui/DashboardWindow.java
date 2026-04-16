package com.bank.ui;

import com.bank.dao.AccountDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dao.TransactionDAO;
import com.bank.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;

public class DashboardWindow extends JFrame {
    private String currentUser;
    private String userRole;
    
    private JLabel welcomeLabel;
    private JLabel totalCustomersLabel;
    private JLabel totalAccountsLabel;
    private JLabel totalBalanceLabel;
    private JLabel totalTransactionsLabel;
    
    private JButton customerButton;
    private JButton accountButton;
    private JButton transactionButton;
    private JButton logoutButton;
    private JButton refreshButton;
    
    private JTable recentTransactionsTable;
    private DefaultTableModel tableModel;
    
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    public DashboardWindow(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        
        customerDAO = new CustomerDAO();
        accountDAO = new AccountDAO();
        transactionDAO = new TransactionDAO();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindow();
        refreshDashboard();
    }
    
    private void initializeComponents() {
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(0, 102, 204));
        
        totalCustomersLabel = new JLabel("0");
        totalAccountsLabel = new JLabel("0");
        totalBalanceLabel = new JLabel("0.00");
        totalTransactionsLabel = new JLabel("0");
        
        customerButton = new JButton("Customer Management");
        accountButton = new JButton("Account Management");
        transactionButton = new JButton("Transaction Management");
        logoutButton = new JButton("Logout");
        refreshButton = new JButton("Refresh");
        
        setupButtonStyles();
        
        String[] columns = {"Transaction ID", "Account", "Type", "Amount", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recentTransactionsTable = new JTable(tableModel);
        recentTransactionsTable.getTableHeader().setReorderingAllowed(false);
        recentTransactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupButtonStyles() {
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        customerButton.setFont(buttonFont);
        accountButton.setFont(buttonFont);
        transactionButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);
        refreshButton.setFont(buttonFont);
        
        customerButton.setBackground(new Color(70, 130, 180));
        customerButton.setForeground(Color.WHITE);
        accountButton.setBackground(new Color(70, 130, 180));
        accountButton.setForeground(Color.WHITE);
        transactionButton.setBackground(new Color(70, 130, 180));
        transactionButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(60, 179, 113));
        refreshButton.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        welcomeLabel.setText("Welcome, " + currentUser + " (" + userRole + ")");
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel statsPanel = createStatsPanel();
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        
        JPanel recentTransactionsPanel = createRecentTransactionsPanel();
        centerPanel.add(recentTransactionsPanel, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("System Statistics"));
        
        statsPanel.add(createStatCard("Total Customers", totalCustomersLabel, new Color(255, 140, 0)));
        statsPanel.add(createStatCard("Total Accounts", totalAccountsLabel, new Color(0, 128, 0)));
        statsPanel.add(createStatCard("Total Balance", totalBalanceLabel, new Color(30, 144, 255)));
        statsPanel.add(createStatCard("Total Transactions", totalTransactionsLabel, new Color(148, 0, 211)));
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(color);
        
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));
        
        JScrollPane scrollPane = new JScrollPane(recentTransactionsTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footerPanel.setBackground(new Color(240, 248, 255));
        
        footerPanel.add(customerButton);
        footerPanel.add(accountButton);
        footerPanel.add(transactionButton);
        footerPanel.add(logoutButton);
        
        return footerPanel;
    }
    
    private void setupEventHandlers() {
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerManagementWindow customerWindow = new CustomerManagementWindow();
                customerWindow.setVisible(true);
            }
        });
        
        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccountManagementWindow accountWindow = new AccountManagementWindow();
                accountWindow.setVisible(true);
            }
        });
        
        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransactionManagementWindow transactionWindow = new TransactionManagementWindow();
                transactionWindow.setVisible(true);
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshDashboard();
            }
        });
    }
    
    private void refreshDashboard() {
        try {
            int totalCustomers = customerDAO.getAllCustomers().size();
            int totalAccounts = accountDAO.getAllAccounts().size();
            BigDecimal totalBalance = BigDecimal.ZERO;
            
            for (var account : accountDAO.getAllAccounts()) {
                totalBalance = totalBalance.add(account.getBalance());
            }
            
            int totalTransactions = transactionDAO.getAllTransactions().size();
            
            totalCustomersLabel.setText(String.valueOf(totalCustomers));
            totalAccountsLabel.setText(String.valueOf(totalAccounts));
            totalBalanceLabel.setText(String.format("%,.2f", totalBalance));
            totalTransactionsLabel.setText(String.valueOf(totalTransactions));
            
            loadRecentTransactions();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error refreshing dashboard: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadRecentTransactions() {
        tableModel.setRowCount(0);
        
        try {
            var recentTransactions = transactionDAO.getAllTransactions();
            int count = 0;
            for (var transaction : recentTransactions) {
                if (count >= 10) break;
                
                Object[] row = {
                    transaction.getTransactionId(),
                    transaction.getAccountNumber(),
                    transaction.getTransactionType(),
                    String.format("%,.2f", transaction.getAmount()),
                    transaction.getTransactionDate().toString().substring(0, 19)
                };
                tableModel.addRow(row);
                count++;
            }
        } catch (Exception e) {
            System.err.println("Error loading recent transactions: " + e.getMessage());
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnection.closeConnection();
            this.dispose();
            new LoginWindow().setVisible(true);
        }
    }
    
    private void setupWindow() {
        setTitle("Bank Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 500));
    }
}
