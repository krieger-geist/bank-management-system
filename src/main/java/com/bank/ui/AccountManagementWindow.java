package com.bank.ui;

import com.bank.dao.AccountDAO;
import com.bank.dao.CustomerDAO;
import com.bank.model.Account;
import com.bank.model.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class AccountManagementWindow extends JFrame {
    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    
    private JTable accountTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<String> statusFilter;
    private JButton filterButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton closeButton;
    
    private JTextField accountNumberField;
    private JComboBox<Customer> customerComboBox;
    private JComboBox<String> accountTypeComboBox;
    private JTextField balanceField;
    private JComboBox<String> statusComboBox;
    
    private Account selectedAccount;
    
    public AccountManagementWindow() {
        accountDAO = new AccountDAO();
        customerDAO = new CustomerDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindow();
        loadAccounts();
    }
    
    private void initializeComponents() {
        statusFilter = new JComboBox<>(new String[]{"ALL", "ACTIVE", "INACTIVE", "CLOSED"});
        filterButton = new JButton("Filter");
        addButton = new JButton("Add Account");
        editButton = new JButton("Edit Account");
        deleteButton = new JButton("Delete Account");
        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");
        
        setupButtonStyles();
        
        String[] columns = {"Account Number", "Customer ID", "Customer Name", "Type", "Balance", "Status", "Created"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accountTable = new JTable(tableModel);
        accountTable.getTableHeader().setReorderingAllowed(false);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountTable.setRowHeight(25);
        
        accountNumberField = new JTextField(15);
        accountNumberField.setEditable(false);
        customerComboBox = new JComboBox<>();
        accountTypeComboBox = new JComboBox<>(new String[]{"SAVINGS", "CURRENT", "FIXED"});
        balanceField = new JTextField(15);
        statusComboBox = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE", "CLOSED"});
        
        loadCustomers();
    }
    
    private void setupButtonStyles() {
        Font buttonFont = new Font("Arial", Font.BOLD, 11);
        filterButton.setFont(buttonFont);
        addButton.setFont(buttonFont);
        editButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        refreshButton.setFont(buttonFont);
        closeButton.setFont(buttonFont);
        
        addButton.setBackground(new Color(0, 128, 0));
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(new Color(30, 144, 255));
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(255, 140, 0));
        refreshButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(105, 105, 105));
        closeButton.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(filterButton);
        
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel formPanel = createFormPanel();
        centerPanel.add(formPanel, BorderLayout.SOUTH);
        
        return centerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Account Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(accountNumberField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer:"), gbc);
        gbc.gridx = 3;
        formPanel.add(customerComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(accountTypeComboBox, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        formPanel.add(new JLabel("Balance:"), gbc);
        gbc.gridx = 3;
        formPanel.add(balanceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusComboBox, gbc);
        
        return formPanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(closeButton);
        
        return bottomPanel;
    }
    
    private void setupEventHandlers() {
        accountTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectAccount();
                }
            }
        });
        
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterAccounts();
            }
        });
        
        statusFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterAccounts();
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAccount();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAccounts();
            }
        });
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void loadCustomers() {
        customerComboBox.removeAllItems();
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            customerComboBox.addItem(customer);
        }
    }
    
    private void loadAccounts() {
        tableModel.setRowCount(0);
        List<Account> accounts = accountDAO.getAllAccounts();
        
        for (Account account : accounts) {
            Customer customer = customerDAO.getCustomerById(account.getCustomerId());
            String customerName = customer != null ? customer.getFullName() : "Unknown";
            
            Object[] row = {
                account.getAccountNumber(),
                account.getCustomerId(),
                customerName,
                account.getAccountType(),
                String.format("%,.2f", account.getBalance()),
                account.getStatus(),
                account.getCreatedAt() != null ? account.getCreatedAt().toString().substring(0, 19) : "N/A"
            };
            tableModel.addRow(row);
        }
        
        clearForm();
    }
    
    private void filterAccounts() {
        String statusFilter = (String) this.statusFilter.getSelectedItem();
        
        tableModel.setRowCount(0);
        List<Account> accounts;
        
        if ("ALL".equals(statusFilter)) {
            accounts = accountDAO.getAllAccounts();
        } else {
            accounts = accountDAO.getActiveAccounts();
            if (!"ACTIVE".equals(statusFilter)) {
                accounts.removeIf(account -> !account.getStatus().equals(statusFilter));
            }
        }
        
        for (Account account : accounts) {
            Customer customer = customerDAO.getCustomerById(account.getCustomerId());
            String customerName = customer != null ? customer.getFullName() : "Unknown";
            
            Object[] row = {
                account.getAccountNumber(),
                account.getCustomerId(),
                customerName,
                account.getAccountType(),
                String.format("%,.2f", account.getBalance()),
                account.getStatus(),
                account.getCreatedAt() != null ? account.getCreatedAt().toString().substring(0, 19) : "N/A"
            };
            tableModel.addRow(row);
        }
        
        clearForm();
    }
    
    private void selectAccount() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
        selectedAccount = accountDAO.getAccountByNumber(accountNumber);
        
        if (selectedAccount != null) {
            accountNumberField.setText(selectedAccount.getAccountNumber());
            
            for (int i = 0; i < customerComboBox.getItemCount(); i++) {
                Customer customer = customerComboBox.getItemAt(i);
                if (customer.getCustomerId() == selectedAccount.getCustomerId()) {
                    customerComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            accountTypeComboBox.setSelectedItem(selectedAccount.getAccountType());
            balanceField.setText(selectedAccount.getBalance().toString());
            statusComboBox.setSelectedItem(selectedAccount.getStatus());
        }
    }
    
    private void addAccount() {
        if (!validateForm()) return;
        
        Account account = new Account();
        account.setAccountNumber(accountDAO.generateAccountNumber());
        account.setCustomerId(((Customer) customerComboBox.getSelectedItem()).getCustomerId());
        account.setAccountType((String) accountTypeComboBox.getSelectedItem());
        
        try {
            account.setBalance(new BigDecimal(balanceField.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid balance amount", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        account.setStatus((String) statusComboBox.getSelectedItem());
        
        if (accountDAO.addAccount(account)) {
            JOptionPane.showMessageDialog(this, 
                "Account added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadAccounts();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add account", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editAccount() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an account to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateForm()) return;
        
        selectedAccount.setCustomerId(((Customer) customerComboBox.getSelectedItem()).getCustomerId());
        selectedAccount.setAccountType((String) accountTypeComboBox.getSelectedItem());
        
        try {
            selectedAccount.setBalance(new BigDecimal(balanceField.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid balance amount", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        selectedAccount.setStatus((String) statusComboBox.getSelectedItem());
        
        if (accountDAO.updateAccount(selectedAccount)) {
            JOptionPane.showMessageDialog(this, 
                "Account updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadAccounts();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update account", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteAccount() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an account to delete", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete account: " + selectedAccount.getAccountNumber() + "?\n" +
            "This will also delete all associated transactions!", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (accountDAO.deleteAccount(selectedAccount.getAccountNumber())) {
                JOptionPane.showMessageDialog(this, 
                    "Account deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadAccounts();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete account", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateForm() {
        if (customerComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Customer is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (balanceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Balance is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            BigDecimal balance = new BigDecimal(balanceField.getText().trim());
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Balance cannot be negative", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid balance amount", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        accountNumberField.setText("");
        customerComboBox.setSelectedIndex(0);
        accountTypeComboBox.setSelectedIndex(0);
        balanceField.setText("");
        statusComboBox.setSelectedIndex(0);
        selectedAccount = null;
    }
    
    private void setupWindow() {
        setTitle("Account Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 500));
    }
}
