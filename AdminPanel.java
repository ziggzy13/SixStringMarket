package com.sixstringmarket.ui;

import com.sixstringmarket.model.User;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.service.UserService;
import com.sixstringmarket.util.AdminManager;
import com.sixstringmarket.util.Constants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminPanel extends JPanel {
    
    private MainFrame parentFrame;
    private UserService userService;
    
    private JTable usersTable;
    private DefaultTableModel usersModel;
    
    // Fields for admin creation
    private JTextField adminUsernameField;
    private JPasswordField adminPasswordField;
    private JTextField adminEmailField;
    
    /**
     * Constructor
     * @param parentFrame Parent frame
     */
    public AdminPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.userService = new UserService();
        
        setLayout(new BorderLayout(0, 20));
        setBackground(Constants.BACKGROUND_COLOR);
        
        initComponents();
        
        // Load users
        loadUsers();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        // Check if user is admin
        if (!AuthenticationService.getInstance().isAdmin()) {
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.setBackground(Constants.BACKGROUND_COLOR);
            
            JLabel notAdminLabel = new JLabel("You don't have permission to access the admin panel", JLabel.CENTER);
            notAdminLabel.setFont(Constants.DEFAULT_FONT);
            messagePanel.add(notAdminLabel, BorderLayout.CENTER);
            
            add(messagePanel, BorderLayout.CENTER);
            return;
        }
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Admin Panel", JLabel.LEFT);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel (with admin creation and user list)
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        // Admin creation panel
        JPanel adminCreationPanel = createAdminCreationPanel();
        contentPanel.add(adminCreationPanel, BorderLayout.NORTH);
        
        // Users table
        String[] columns = {"ID", "Username", "Email", "Phone", "Registration Date", "Role", "Actions"};
        usersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        usersTable = new JTable(usersModel);
        usersTable.setRowHeight(30);
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(30);   // ID
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // Username
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(200);  // Email
        usersTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Phone
        usersTable.getColumnModel().getColumn(4).setPreferredWidth(150);  // Registration Date
        usersTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Role
        usersTable.getColumnModel().getColumn(6).setPreferredWidth(100);  // Actions
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Add table actions
        addTableActions();
    }
    
    /**
     * Create admin creation panel
     */
    private JPanel createAdminCreationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                "Create New Admin Account",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(70, 80, 160)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        adminUsernameField = new JTextField(20);
        adminUsernameField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(adminUsernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        adminPasswordField = new JPasswordField(20);
        adminPasswordField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(adminPasswordField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        adminEmailField = new JTextField(20);
        adminEmailField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(adminEmailField, gbc);
        
        // Create button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JButton createButton = new JButton("Create Admin");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createButton.setBackground(new Color(103, 58, 183));
        createButton.setForeground(Color.BLACK);
        createButton.addActionListener(e -> createAdminAccount());
        formPanel.add(createButton, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create admin account
     */
    private void createAdminAccount() {
        String username = adminUsernameField.getText().trim();
        String password = new String(adminPasswordField.getPassword());
        String email = adminEmailField.getText().trim();
        
        // Validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Create admin account
        AdminManager adminManager = new AdminManager();
        boolean success = adminManager.createAdminAccount(username, password, email);
        
        if (success) {
            JOptionPane.showMessageDialog(
                this,
                "Admin account created successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Clear fields
            adminUsernameField.setText("");
            adminPasswordField.setText("");
            adminEmailField.setText("");
            
            // Reload users
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Error creating admin account. The username may already exist.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Add table actions
     */
    private void addTableActions() {
        usersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = usersTable.rowAtPoint(evt.getPoint());
                int col = usersTable.columnAtPoint(evt.getPoint());
                
                // Check if clicked in actions column
                if (col == 6 && row >= 0) {
                    int userId = Integer.parseInt(usersTable.getValueAt(row, 0).toString());
                    String role = usersTable.getValueAt(row, 5).toString();
                    
                    // Show options menu
                    JPopupMenu menu = new JPopupMenu();
                    
                    // Role change option
                    if (role.equals("User")) {
                        JMenuItem makeAdminItem = new JMenuItem("Make Admin");
                        makeAdminItem.addActionListener(e -> changeUserRole(userId, User.UserRole.ADMIN));
                        menu.add(makeAdminItem);
                    } else {
                        JMenuItem makeUserItem = new JMenuItem("Make Regular User");
                        makeUserItem.addActionListener(e -> changeUserRole(userId, User.UserRole.USER));
                        menu.add(makeUserItem);
                    }
                    
                    // Delete option
                    JMenuItem deleteItem = new JMenuItem("Delete User");
                    deleteItem.addActionListener(e -> deleteUser(userId));
                    menu.add(deleteItem);
                    
                    menu.show(usersTable, evt.getX(), evt.getY());
                }
            }
        });
    }
    
    /**
     * Load users from database
     */
    private void loadUsers() {
        // Clear model
        usersModel.setRowCount(0);
        
        // Load users
        List<User> users = userService.getAllUsers();
        
        for (User user : users) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone() != null ? user.getPhone() : "",
                dateFormat.format(user.getRegistrationDate()),
                user.getRole() == User.UserRole.ADMIN ? "Admin" : "User",
                "Actions"
            };
            
            usersModel.addRow(row);
        }
    }
    
    /**
     * Change user role
     * @param userId User ID
     * @param newRole New role
     */
    private void changeUserRole(int userId, User.UserRole newRole) {
        try {
            boolean success = userService.changeUserRole(userId, newRole);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    "User role changed successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Reload users
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Error changing user role",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Delete user
     * @param userId User ID
     */
    private void deleteUser(int userId) {
        int response = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this user?\n" +
            "This will delete all guitars and orders associated with this user!",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            try {
                boolean success = userService.deleteUser(userId);
                
                if (success) {
                    JOptionPane.showMessageDialog(
                        this,
                        "User deleted successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Reload users
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error deleting user",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}