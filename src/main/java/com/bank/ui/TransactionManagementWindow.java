package com.bank.ui;

import com.bank.dao.AccountDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionManagementWindow extends JFrame {
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;
    
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<Account> accountComboBox;
    private JButton viewButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton refreshButton;
    private JButton closeButton;
    
    private JTextField amountField;
    private JTextField descriptionField;
    private JLabel balanceLabel;
    
    private Account selectedAccount;
    
    public TransactionManagementWindow() {
        transactionDAO = new TransactionDAO();
        accountDAO = new AccountDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindow();
        loadAccounts();
        loadTransactions();
    }
    
    private void initializeComponents() {
        accountComboBox = new JComboBox<>();
        viewButton = new JButton("View Transactions");
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        transferButton = new JButton("Transfer");
        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");
        
        setupButtonStyles();
        
        String[] columns = {"Transaction ID", "Account", "Type", "Amount", "Description", "Date", "Balance After"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setRowHeight(25);
        
        amountField = new JTextField(15);
        descriptionField = new JTextField(20);
        balanceLabel = new JLabel("Balance: $0.00", JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(0, 128, 0));
    }
    
    private void setupButtonStyles() {
        Font buttonFont = new Font("Arial", Font.BOLD, 11);
        viewButton.setFont(buttonFont);
        depositButton.setFont(buttonFont);
        withdrawButton.setFont(buttonFont);
        transferButton.setFont(buttonFont);
        refreshButton.setFont(buttonFont);
        closeButton.setFont(buttonFont);
        
        viewButton.setBackground(new Color(70, 130, 180));
        viewButton.setForeground(Color.WHITE);
        depositButton.setBackground(new Color(0, 128, 0));
        depositButton.setForeground(Color.WHITE);
        withdrawButton.setBackground(new Color(255, 140, 0));
        withdrawButton.setForeground(Color.WHITE);
        transferButton.setBackground(new Color(30, 144, 255));
        transferButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(128, 0, 128));
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
        
        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        accountPanel.add(new JLabel("Select Account:"));
        accountPanel.add(accountComboBox);
        accountPanel.add(viewButton);
        
        topPanel.add(accountPanel, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel transactionPanel = createTransactionPanel();
        centerPanel.add(transactionPanel, BorderLayout.SOUTH);
        
        return centerPanel;
    }
    
    private JPanel createTransactionPanel() {
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Transaction Operations"));
        
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        balancePanel.add(balanceLabel);
        transactionPanel.add(balancePanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        formPanel.add(amountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);
        
        transactionPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        transactionPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return transactionPanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        bottomPanel.add(closeButton);
        
        return bottomPanel;
    }
    
    private void setupEventHandlers() {
        accountComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAccount();
            }
        });
        
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTransactions();
            }
        });
        
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDeposit();
            }
        });
        
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performWithdrawal();
            }
        });
        
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTransfer();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAccounts();
                loadTransactions();
            }
        });
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void loadAccounts() {
        accountComboBox.removeAllItems();
        List<Account> accounts = accountDAO.getActiveAccounts();
        for (Account account : accounts) {
            accountComboBox.addItem(account);
        }
        
        if (accountComboBox.getItemCount() > 0) {
            accountComboBox.setSelectedIndex(0);
            selectAccount();
        }
    }
    
    private void selectAccount() {
        selectedAccount = (Account) accountComboBox.getSelectedItem();
        if (selectedAccount != null) {
            balanceLabel.setText("Balance: $" + String.format("%,.2f", selectedAccount.getBalance()));
            loadTransactions();
        }
    }
    
    private void loadTransactions() {
        tableModel.setRowCount(0);
        
        if (selectedAccount == null) return;
        
        List<Transaction> transactions = transactionDAO.getTransactionsByAccount(selectedAccount.getAccountNumber());
        
        for (Transaction transaction : transactions) {
            Object[] row = {
                transaction.getTransactionId(),
                transaction.getAccountNumber(),
                transaction.getTransactionType(),
                String.format("%,.2f", transaction.getAmount()),
                transaction.getDescription() != null ? transaction.getDescription() : "N/A",
                transaction.getTransactionDate().toString().substring(0, 19),
                String.format("%,.2f", transaction.getBalanceAfter())
            };
            tableModel.addRow(row);
        }
    }
    
    private void performDeposit() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an account", 
                "No Account", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateAmount()) return;
        
        BigDecimal amount = new BigDecimal(amountField.getText().trim());
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            description = "Deposit";
        }
        
        BigDecimal newBalance = selectedAccount.getBalance().add(amount);
        
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(selectedAccount.getAccountNumber());
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setBalanceAfter(newBalance);
        
        if (transactionDAO.addTransaction(transaction)) {
            if (accountDAO.updateAccountBalance(selectedAccount.getAccountNumber(), newBalance)) {
                JOptionPane.showMessageDialog(this, 
                    "Deposit successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                selectedAccount.setBalance(newBalance);
                balanceLabel.setText("Balance: $" + String.format("%,.2f", selectedAccount.getBalance()));
                loadTransactions();
                clearTransactionForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error updating account balance", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to process deposit", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performWithdrawal() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an account", 
                "No Account", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateAmount()) return;
        
        BigDecimal amount = new BigDecimal(amountField.getText().trim());
        
        if (amount.compareTo(selectedAccount.getBalance()) > 0) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient funds", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            description = "Withdrawal";
        }
        
        BigDecimal newBalance = selectedAccount.getBalance().subtract(amount);
        
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(selectedAccount.getAccountNumber());
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setBalanceAfter(newBalance);
        
        if (transactionDAO.addTransaction(transaction)) {
            if (accountDAO.updateAccountBalance(selectedAccount.getAccountNumber(), newBalance)) {
                JOptionPane.showMessageDialog(this, 
                    "Withdrawal successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                selectedAccount.setBalance(newBalance);
                balanceLabel.setText("Balance: $" + String.format("%,.2f", selectedAccount.getBalance()));
                loadTransactions();
                clearTransactionForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error updating account balance", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to process withdrawal", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performTransfer() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an account", 
                "No Account", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateAmount()) return;
        
        BigDecimal amount = new BigDecimal(amountField.getText().trim());
        
        if (amount.compareTo(selectedAccount.getBalance()) > 0) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient funds", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Account> accounts = accountDAO.getActiveAccounts();
        accounts.removeIf(acc -> acc.getAccountNumber().equals(selectedAccount.getAccountNumber()));
        
        if (accounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No other accounts available for transfer", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Account[] accountArray = accounts.toArray(new Account[0]);
        Account targetAccount = (Account) JOptionPane.showInputDialog(
            this,
            "Select target account:",
            "Transfer",
            JOptionPane.QUESTION_MESSAGE,
            null,
            accountArray,
            accountArray[0]
        );
        
        if (targetAccount == null) return;
        
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            description = "Transfer to " + targetAccount.getAccountNumber();
        }
        
        BigDecimal sourceNewBalance = selectedAccount.getBalance().subtract(amount);
        BigDecimal targetNewBalance = targetAccount.getBalance().add(amount);
        
        Transaction sourceTransaction = new Transaction();
        sourceTransaction.setAccountNumber(selectedAccount.getAccountNumber());
        sourceTransaction.setTransactionType("TRANSFER");
        sourceTransaction.setAmount(amount);
        sourceTransaction.setDescription(description);
        sourceTransaction.setBalanceAfter(sourceNewBalance);
        
        Transaction targetTransaction = new Transaction();
        targetTransaction.setAccountNumber(targetAccount.getAccountNumber());
        targetTransaction.setTransactionType("TRANSFER");
        targetTransaction.setAmount(amount);
        targetTransaction.setDescription("Transfer from " + selectedAccount.getAccountNumber());
        targetTransaction.setBalanceAfter(targetNewBalance);
        
        if (transactionDAO.addTransaction(sourceTransaction) && 
            transactionDAO.addTransaction(targetTransaction)) {
            
            if (accountDAO.updateAccountBalance(selectedAccount.getAccountNumber(), sourceNewBalance) &&
                accountDAO.updateAccountBalance(targetAccount.getAccountNumber(), targetNewBalance)) {
                
                JOptionPane.showMessageDialog(this, 
                    "Transfer successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                selectedAccount.setBalance(sourceNewBalance);
                balanceLabel.setText("Balance: $" + String.format("%,.2f", selectedAccount.getBalance()));
                loadTransactions();
                clearTransactionForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error updating account balances", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to process transfer", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateAmount() {
        if (amountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearTransactionForm() {
        amountField.setText("");
        descriptionField.setText("");
    }
    
    private void setupWindow() {
        setTitle("Transaction Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 500));
    }
}
