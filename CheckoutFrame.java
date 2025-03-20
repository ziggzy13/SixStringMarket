package com.sixstringmarket.ui;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Order;
import com.sixstringmarket.model.Payment;
import com.sixstringmarket.service.OrderService;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.CreditCardPayment;
import com.sixstringmarket.util.CashOnDeliveryPayment;
import com.sixstringmarket.util.BankTransferPayment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class CheckoutFrame extends JDialog {
    
    private MainFrame parentFrame;
    private Guitar guitar;
    private OrderService orderService;
    private JPanel contentPanel;
    private JTabbedPane tabbedPane;
    private JPanel summaryPanel;
    private JPanel paymentPanel;
    private JPanel confirmationPanel;
    
    private CreditCardPayment creditCardPayment;
    private CashOnDeliveryPayment cashOnDeliveryPayment;
    private BankTransferPayment bankTransferPayment;
    
    /**
     * Constructor
     * @param parentFrame Parent frame
     * @param guitar Guitar to purchase
     */
    public CheckoutFrame(MainFrame parentFrame, Guitar guitar) {
        super(parentFrame, "Checkout", true);
        this.parentFrame = parentFrame;
        this.guitar = guitar;
        this.orderService = new OrderService();
        
        setSize(800, 600);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        
        initComponents();
        layoutComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Close button
        JButton closeButton = new JButton("×");
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        titlePanel.add(closeButton, BorderLayout.EAST);
        
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Summary Panel
        summaryPanel = createSummaryPanel();
        
        // Payment Panel
        paymentPanel = createPaymentPanel();
        
        // Confirmation Panel
        confirmationPanel = createConfirmationPanel();
        
        // Add tabs
        tabbedPane.addTab("1. Order Summary", summaryPanel);
        tabbedPane.addTab("2. Payment Method", paymentPanel);
        tabbedPane.addTab("3. Confirmation", confirmationPanel);
        
        // Disable tabs initially
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);
        
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setContentPane(contentPanel);
    }
    
    /**
     * Layout components
     */
    private void layoutComponents() {
        // Already handled in initComponents
    }
    
    /**
     * Create summary panel
     */
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Guitar details
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        detailsPanel.setBackground(Color.WHITE);
        
        detailsPanel.add(new JLabel("Item:"));
        detailsPanel.add(new JLabel(guitar.getTitle()));
        
        detailsPanel.add(new JLabel("Brand:"));
        detailsPanel.add(new JLabel(guitar.getBrand() + " " + guitar.getModel()));
        
        detailsPanel.add(new JLabel("Type:"));
        detailsPanel.add(new JLabel(getGuitarTypeText(guitar.getType())));
        
        detailsPanel.add(new JLabel("Condition:"));
        detailsPanel.add(new JLabel(getConditionText(guitar.getCondition())));
        
        detailsPanel.add(new JLabel("Price:"));
        JLabel priceLabel = new JLabel(guitar.getPrice() + " лв.");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(Constants.SUCCESS_COLOR);
        detailsPanel.add(priceLabel);
        
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        // Continue button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton continueButton = new JButton("Continue to Payment");
        continueButton.setBackground(Constants.PRIMARY_COLOR);
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        continueButton.addActionListener(e -> {
            tabbedPane.setEnabledAt(1, true);
            tabbedPane.setSelectedIndex(1);
        });
        
        buttonPanel.add(continueButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create payment panel
     */
    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Initialize payment methods
        creditCardPayment = new CreditCardPayment();
        cashOnDeliveryPayment = new CashOnDeliveryPayment();
        bankTransferPayment = new BankTransferPayment();
        
        // Use the existing PaymentPanel component
        com.sixstringmarket.ui.components.PaymentPanel paymentMethodPanel = 
            new com.sixstringmarket.ui.components.PaymentPanel(guitar.getPrice());
        
        panel.add(paymentMethodPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
        });
        
        JButton continueButton = new JButton("Continue to Confirmation");
        continueButton.setBackground(Constants.PRIMARY_COLOR);
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        continueButton.addActionListener(e -> {
            if (paymentMethodPanel.validatePaymentData()) {
                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all payment details correctly.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(continueButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create confirmation panel
     */
    private JPanel createConfirmationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Confirmation message
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Color.WHITE);
        
        JLabel confirmTitleLabel = new JLabel("Confirm Your Order");
        confirmTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        confirmTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel confirmMessageLabel = new JLabel(
            "<html><body style='text-align: center; width: 400px;'>You are about to purchase:<br><br>" +
            "<b>" + guitar.getTitle() + "</b><br>" +
            "Price: <b>" + guitar.getPrice() + " лв.</b><br><br>" +
            "Please confirm to complete your order.</body></html>");
        confirmMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        messagePanel.add(Box.createVerticalGlue());
        messagePanel.add(confirmTitleLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(confirmMessageLabel);
        messagePanel.add(Box.createVerticalGlue());
        
        panel.add(messagePanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
        });
        
        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setBackground(Constants.SUCCESS_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.addActionListener(e -> {
            processOrder();
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Process the order
     */
    private void processOrder() {
        try {
            int userId = AuthenticationService.getInstance().getCurrentUser().getUserId();
            
            // Create the order
            boolean success = orderService.createOrder(guitar.getGuitarId(), userId);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    "Your order has been successfully created!",
                    "Order Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                dispose();
                parentFrame.showOrderHistoryPanel();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "There was an error processing your order. Please try again.",
                    "Order Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Convert guitar type to text
     */
    private String getGuitarTypeText(Guitar.GuitarType type) {
        switch (type) {
            case ACOUSTIC: return "Acoustic";
            case ELECTRIC: return "Electric";
            case CLASSICAL: return "Classical";
            case BASS: return "Bass";
            case OTHER: return "Other";
            default: return "Unknown";
        }
    }
    
    /**
     * Convert condition to text
     */
    private String getConditionText(Guitar.Condition condition) {
        switch (condition) {
            case NEW: return "New";
            case USED: return "Used";
            case VINTAGE: return "Vintage";
            default: return "Unknown";
        }
    }
}