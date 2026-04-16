package com.bank.ui;

import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class CustomerManagementWindow extends JFrame {
    private CustomerDAO customerDAO;
    
    private JTable customerTable;
    private DefaultTableModel tableModel;
    
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton closeButton;
    
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField dobField;
    
    private Customer selectedCustomer;
    
    public CustomerManagementWindow() {
        customerDAO = new CustomerDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindow();
        loadCustomers();
    }
    
    private void initializeComponents() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        addButton = new JButton("Add Customer");
        editButton = new JButton("Edit Customer");
        deleteButton = new JButton("Delete Customer");
        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");
        
        setupButtonStyles();
        
        String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone", "Address", "Date of Birth"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setRowHeight(25);
        
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        emailField = new JTextField(15);
        phoneField = new JTextField(15);
        addressField = new JTextField(15);
        dobField = new JTextField(15);
        dobField.setToolTipText("Format: YYYY-MM-DD");
    }
    
    private void setupButtonStyles() {
        Font buttonFont = new Font("Arial", Font.BOLD, 11);
        searchButton.setFont(buttonFont);
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
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel formPanel = createFormPanel();
        centerPanel.add(formPanel, BorderLayout.SOUTH);
        
        return centerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3;
        formPanel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(addressField, gbc);
        
        gbc.gridx = 3; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("DOB:"), gbc);
        gbc.gridx = 4;
        formPanel.add(dobField, gbc);
        
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
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectCustomer();
                }
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomers();
            }
        });
        
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomers();
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCustomer();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCustomers();
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
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.getAllCustomers();
        
        for (Customer customer : customers) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getDateOfBirth()
            };
            tableModel.addRow(row);
        }
        
        clearForm();
    }
    
    private void searchCustomers() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadCustomers();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.searchCustomers(searchTerm);
        
        for (Customer customer : customers) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getDateOfBirth()
            };
            tableModel.addRow(row);
        }
        
        clearForm();
    }
    
    private void selectCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        selectedCustomer = customerDAO.getCustomerById(customerId);
        
        if (selectedCustomer != null) {
            firstNameField.setText(selectedCustomer.getFirstName());
            lastNameField.setText(selectedCustomer.getLastName());
            emailField.setText(selectedCustomer.getEmail());
            phoneField.setText(selectedCustomer.getPhone());
            addressField.setText(selectedCustomer.getAddress());
            dobField.setText(selectedCustomer.getDateOfBirth().toString());
        }
    }
    
    private void addCustomer() {
        if (!validateForm()) return;
        
        Customer customer = new Customer();
        customer.setFirstName(firstNameField.getText().trim());
        customer.setLastName(lastNameField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setPhone(phoneField.getText().trim());
        customer.setAddress(addressField.getText().trim());
        
        try {
            customer.setDateOfBirth(LocalDate.parse(dobField.getText().trim()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date format. Use YYYY-MM-DD", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (customerDAO.addCustomer(customer)) {
            JOptionPane.showMessageDialog(this, 
                "Customer added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadCustomers();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add customer", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a customer to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateForm()) return;
        
        selectedCustomer.setFirstName(firstNameField.getText().trim());
        selectedCustomer.setLastName(lastNameField.getText().trim());
        selectedCustomer.setEmail(emailField.getText().trim());
        selectedCustomer.setPhone(phoneField.getText().trim());
        selectedCustomer.setAddress(addressField.getText().trim());
        
        try {
            selectedCustomer.setDateOfBirth(LocalDate.parse(dobField.getText().trim()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date format. Use YYYY-MM-DD", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (customerDAO.updateCustomer(selectedCustomer)) {
            JOptionPane.showMessageDialog(this, 
                "Customer updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadCustomers();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update customer", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a customer to delete", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete customer: " + selectedCustomer.getFullName() + "?\n" +
            "This will also delete all associated accounts and transactions!", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (customerDAO.deleteCustomer(selectedCustomer.getCustomerId())) {
                JOptionPane.showMessageDialog(this, 
                    "Customer deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadCustomers();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete customer", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateForm() {
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (dobField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date of birth is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        dobField.setText("");
        selectedCustomer = null;
    }
    
    private void setupWindow() {
        setTitle("Customer Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));
    }
}
