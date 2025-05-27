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
import com.sixstringmarket.dao.PaymentDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

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
    
    // Fields for credit card payment
    private JTextField cardNumberField;
    private JTextField cardholderNameField;
    private JTextField expiryDateField;
    private JTextField cvvField;
    
    // Fields for cash on delivery payment
    private JTextField deliveryAddressField;
    private JTextField recipientNameField;
    private JTextField recipientPhoneField;
    
    // Fields for bank transfer payment
    private JTextField customerNameField;
    
    // Selected payment method
    private String selectedPaymentMethod;
    
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
        this.selectedPaymentMethod = "CreditCard"; // Default payment method
        
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
        contentPanel.setBackground(Constants.PANEL_COLOR);
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Close button
        JButton closeButton = new JButton("×");
        closeButton.setForeground(Color.BLACK);
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
        tabbedPane.setForeground(Constants.TEXT_COLOR);
        
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
        
        // Added setForeground to all JLabels for black text
        JLabel itemLabel = new JLabel("Item:");
        itemLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(itemLabel);
        
        JLabel itemValueLabel = new JLabel(guitar.getTitle());
        itemValueLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(itemValueLabel);
        
        JLabel brandLabel = new JLabel("Brand:");
        brandLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(brandLabel);
        
        JLabel brandValueLabel = new JLabel(guitar.getBrand() + " " + guitar.getModel());
        brandValueLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(brandValueLabel);
        
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(typeLabel);
        
        JLabel typeValueLabel = new JLabel(getGuitarTypeText(guitar.getType()));
        typeValueLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(typeValueLabel);
        
        JLabel conditionLabel = new JLabel("Condition:");
        conditionLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(conditionLabel);
        
        JLabel conditionValueLabel = new JLabel(getConditionText(guitar.getCondition()));
        conditionValueLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(conditionValueLabel);
        
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Constants.TEXT_COLOR);
        detailsPanel.add(priceLabel);
        
        JLabel priceValueLabel = new JLabel(guitar.getPrice() + " лв.");
        priceValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceValueLabel.setForeground(Constants.SUCCESS_COLOR);
        detailsPanel.add(priceValueLabel);
        
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        // Continue button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton continueButton = new JButton("Continue to Payment");
        continueButton.setBackground(Constants.PRIMARY_COLOR);
        continueButton.setForeground(Color.BLACK);
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
        
        // Create payment method selection panel with radio buttons only
        JPanel paymentMethodSelectionPanel = new JPanel(new BorderLayout());
        paymentMethodSelectionPanel.setBackground(Color.WHITE);
        
        // Radio buttons for payment method selection
        JRadioButton creditCardRadio = new JRadioButton("Credit Card");
        creditCardRadio.setSelected(true);
        creditCardRadio.setBackground(Color.WHITE);
        creditCardRadio.setForeground(Constants.TEXT_COLOR);
        
        JRadioButton cashOnDeliveryRadio = new JRadioButton("Cash On Delivery");
        cashOnDeliveryRadio.setBackground(Color.WHITE);
        cashOnDeliveryRadio.setForeground(Constants.TEXT_COLOR);
        
        JRadioButton bankTransferRadio = new JRadioButton("Bank Transfer");
        bankTransferRadio.setBackground(Color.WHITE);
        bankTransferRadio.setForeground(Constants.TEXT_COLOR);
        
        // Group radio buttons
        ButtonGroup paymentMethodGroup = new ButtonGroup();
        paymentMethodGroup.add(creditCardRadio);
        paymentMethodGroup.add(cashOnDeliveryRadio);
        paymentMethodGroup.add(bankTransferRadio);
        
        // Panel for radio buttons
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.add(creditCardRadio);
        radioPanel.add(cashOnDeliveryRadio);
        radioPanel.add(bankTransferRadio);
        
        paymentMethodSelectionPanel.add(radioPanel, BorderLayout.NORTH);
        
        // Card panel - for different payment method details
        final JPanel[] cardPanel = {new JPanel(new CardLayout())};
        cardPanel[0].setBackground(Color.WHITE);
        
        // Credit card panel
        JPanel creditCardPanel = createCreditCardPanel();
        
        // Cash on delivery panel
        JPanel cashOnDeliveryPanel = createCashOnDeliveryPanel();
        
        // Bank transfer panel
        JPanel bankTransferPanel = createBankTransferPanel();
        
        // Add panels to card layout
        cardPanel[0].add(creditCardPanel, "CreditCard");
        cardPanel[0].add(cashOnDeliveryPanel, "CashOnDelivery");
        cardPanel[0].add(bankTransferPanel, "BankTransfer");
        
        // Set up action listeners
        creditCardRadio.addActionListener(e -> {
            ((CardLayout)cardPanel[0].getLayout()).show(cardPanel[0], "CreditCard");
            selectedPaymentMethod = "CreditCard";
        });
        
        cashOnDeliveryRadio.addActionListener(e -> {
            ((CardLayout)cardPanel[0].getLayout()).show(cardPanel[0], "CashOnDelivery");
            selectedPaymentMethod = "CashOnDelivery";
        });
        
        bankTransferRadio.addActionListener(e -> {
            ((CardLayout)cardPanel[0].getLayout()).show(cardPanel[0], "BankTransfer");
            selectedPaymentMethod = "BankTransfer";
        });
        
        paymentMethodSelectionPanel.add(cardPanel[0], BorderLayout.CENTER);
        
        panel.add(paymentMethodSelectionPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setForeground(Color.BLACK);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
        });
        
        JButton continueButton = new JButton("Continue to Confirmation");
        continueButton.setBackground(Constants.PRIMARY_COLOR);
        continueButton.setForeground(Color.BLACK);
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        continueButton.addActionListener(e -> {
            boolean isValid = false;
            
            // Validate based on selected payment method
            switch (selectedPaymentMethod) {
                case "CreditCard":
                    isValid = validateCreditCardPayment();
                    break;
                case "CashOnDelivery":
                    isValid = validateCashOnDeliveryPayment();
                    break;
                case "BankTransfer":
                    isValid = validateBankTransferPayment();
                    break;
            }
            
            if (isValid) {
                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setSelectedIndex(2);
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(continueButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create credit card payment panel
     */
    private JPanel createCreditCardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create form panel directly (without the "Данни за кредитна карта" header)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        // Card number field
        JLabel cardNumberLabel = new JLabel("Номер на картата: *");
        cardNumberLabel.setForeground(Constants.TEXT_COLOR);
        formPanel.add(cardNumberLabel);
        
        cardNumberField = new JTextField();
        formPanel.add(cardNumberField);
        
        // Cardholder name field
        JLabel cardholderLabel = new JLabel("Име на картодържателя: *");
        cardholderLabel.setForeground(Constants.TEXT_COLOR);
        formPanel.add(cardholderLabel);
        
        cardholderNameField = new JTextField();
        formPanel.add(cardholderNameField);
        
        // Expiry date field
        JLabel expiryLabel = new JLabel("Срок на валидност (ММ/ГГ): *");
        expiryLabel.setForeground(Constants.TEXT_COLOR);
        formPanel.add(expiryLabel);
        
        expiryDateField = new JTextField();
        formPanel.add(expiryDateField);
        
        // CVV field
        JLabel cvvLabel = new JLabel("CVV: *");
        cvvLabel.setForeground(Constants.TEXT_COLOR);
        formPanel.add(cvvLabel);
        
        cvvField = new JTextField();
        formPanel.add(cvvField);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Security notice
        JPanel securityPanel = new JPanel(new BorderLayout());
        securityPanel.setBackground(Color.WHITE);
        securityPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel securityLabel = new JLabel(
            "<html><body style='font-style: italic;'>Вашите данни са защитени с SSL шифроване.<br>" +
            "Ние не съхраняваме данните от вашата кредитна карта.</body></html>"
        );
        securityLabel.setForeground(new Color(100, 100, 100));
        securityPanel.add(securityLabel, BorderLayout.CENTER);
        
        panel.add(securityPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create cash on delivery payment panel
     */
    private JPanel createCashOnDeliveryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        // Address field
        JLabel addressLabel = new JLabel("Delivery Address:");
        addressLabel.setForeground(Constants.TEXT_COLOR);
        formPanel.add(addressLabel);
        
        deliveryAddressField = new JTextField(20);
        formPanel.add(deliveryAddressField);
        
        // Recipient name field
        JLabel nameLabel = new JLabel("Recipient Name:");
        nameLabel.setForeground(Constants.TEXT_COLOR);
        formPanel.add(nameLabel);
        
        recipientNameField = new JTextField(20);
        formPanel.add(recipientNameField);
        
        // Phone field
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setForeground(Constants.TEXT_COLOR);
        formPanel.add(phoneLabel);
        
        recipientPhoneField = new JTextField(20);
        formPanel.add(recipientPhoneField);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Fee information
        JPanel feePanel = new JPanel(new BorderLayout());
        feePanel.setBackground(Color.WHITE);
        feePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        BigDecimal codFee = cashOnDeliveryPayment.getProcessingFee();
        BigDecimal totalWithFee = guitar.getPrice().add(codFee);
        
        JLabel feeInfoLabel = new JLabel(
            "<html><body>" +
            "Cash on delivery fee: <b>" + codFee + " лв.</b><br><br>" +
            "Total amount (including fee): <b>" + totalWithFee + " лв.</b>" +
            "</body></html>"
        );
        feeInfoLabel.setForeground(Constants.TEXT_COLOR);
        
        feePanel.add(feeInfoLabel, BorderLayout.CENTER);
        
        panel.add(feePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create bank transfer payment panel
     */
    private JPanel createBankTransferPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Customer name field
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel("Your Name (as it will appear on the transfer):");
        nameLabel.setForeground(Constants.TEXT_COLOR);
        
        customerNameField = new JTextField(20);
        
        namePanel.add(nameLabel, BorderLayout.NORTH);
        namePanel.add(customerNameField, BorderLayout.CENTER);
        
        panel.add(namePanel, BorderLayout.NORTH);
        
        // Bank information
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        String bankInfo = bankTransferPayment.generatePaymentInstructions(guitar.getPrice());
        
        JTextArea infoArea = new JTextArea(bankInfo);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoArea.setBackground(new Color(245, 245, 250));
        infoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        infoPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
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
        confirmTitleLabel.setForeground(Constants.TEXT_COLOR);
        confirmTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel confirmMessageLabel = new JLabel(
            "<html><body style='text-align: center; width: 400px; color: #212121;'>You are about to purchase:<br><br>" +
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
        backButton.setForeground(Color.BLACK);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
        });
        
        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setBackground(Constants.SUCCESS_COLOR);
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.addActionListener(e -> {
            // Process based on selected payment method
            switch (selectedPaymentMethod) {
                case "CreditCard":
                    processCreditCardPayment();
                    break;
                case "CashOnDelivery":
                    processCashOnDeliveryPayment();
                    break;
                case "BankTransfer":
                    processBankTransferPayment();
                    break;
                default:
                    processOrder(); // Fallback to default processing
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Process the order (default method)
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
     * Validate credit card payment details
     */
    private boolean validateCreditCardPayment() {
        // Get values from fields
        String cardNumber = cardNumberField.getText().trim();
        String cardHolder = cardholderNameField.getText().trim();
        String expiryDate = expiryDateField.getText().trim();
        String cvv = cvvField.getText().trim();
        
        // Create a credit card payment object
        CreditCardPayment payment = new CreditCardPayment(cardNumber, cardHolder, expiryDate, cvv);
        
        // Validate
        if (!payment.isValidCardNumber()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid credit card number.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            cardNumberField.requestFocus();
            return false;
        }
        
        if (!payment.isValidCardHolderName()) {
            JOptionPane.showMessageDialog(this,
                "Please enter the cardholder's name.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            cardholderNameField.requestFocus();
            return false;
        }
        
        if (!payment.isValidExpiryDate()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid expiry date (MM/YY).",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            expiryDateField.requestFocus();
            return false;
        }
        
        if (!payment.isValidCVV()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid CVV code (3 or 4 digits).",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            cvvField.requestFocus();
            return false;
        }
        
        // Store the payment object for later use
        this.creditCardPayment = payment;
        
        return true;
    }
    
    /**
     * Validate cash on delivery payment details
     */
    private boolean validateCashOnDeliveryPayment() {
        // Get values from fields
        String address = deliveryAddressField.getText().trim();
        String name = recipientNameField.getText().trim();
        String phone = recipientPhoneField.getText().trim();
        
        // Set values in payment object
        cashOnDeliveryPayment.setDeliveryAddress(address);
        cashOnDeliveryPayment.setRecipientName(name);
        cashOnDeliveryPayment.setRecipientPhone(phone);
        
        // Validate
        if (!cashOnDeliveryPayment.isValidDeliveryAddress()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid delivery address (at least 10 characters).",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            deliveryAddressField.requestFocus();
            return false;
        }
        
        if (!cashOnDeliveryPayment.isValidRecipientName()) {
            JOptionPane.showMessageDialog(this,
                "Please enter the recipient's name (at least 3 characters).",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            recipientNameField.requestFocus();
            return false;
        }
        
        if (!cashOnDeliveryPayment.isValidRecipientPhone()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid phone number (format: 0xxxxxxxxx).",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            recipientPhoneField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate bank transfer payment details
     */
    private boolean validateBankTransferPayment() {
        // Get customer name
        String customerName = customerNameField.getText().trim();
        
        // Set in payment object
        bankTransferPayment.setCustomerName(customerName);
        
        // Validate
        if (!bankTransferPayment.isValidCustomerName()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your name for the bank transfer reference (at least 3 characters).",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            customerNameField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Process credit card payment
     */
    private void processCreditCardPayment() {
        // Use default order processing for credit card
        processOrder();
    }
    
    /**
     * Process cash on delivery payment
     */
    private void processCashOnDeliveryPayment() {
        try {
            // Get the current user and guitar data
            int userId = AuthenticationService.getInstance().getCurrentUser().getUserId();
            
            // Get delivery details
            String address = deliveryAddressField.getText().trim();
            String name = recipientNameField.getText().trim();
            String phone = recipientPhoneField.getText().trim();
            
            // Create a cash on delivery payment
            CashOnDeliveryPayment codPayment = new CashOnDeliveryPayment(address, name, phone);
            
            // Calculate total with COD fee
            BigDecimal totalWithFee = codPayment.calculateTotal(guitar.getPrice());
            
            // Create the order
            boolean orderSuccess = orderService.createOrder(guitar.getGuitarId(), userId);
            
            if (orderSuccess) {
                // Since we don't have a getLastOrderId method, we'll use the most recent order
                // Get all orders by buyer and take the most recent one
                List<Order> buyerOrders = orderService.getOrdersByBuyer(userId);
                if (!buyerOrders.isEmpty()) {
                    // The most recent order should be the first in the list
                    Order order = buyerOrders.get(0);
                    
                    // Create payment record with COD details
                    Payment payment = new Payment();
                    payment.setOrderId(order.getOrderId());
                    payment.setPaymentMethod(Payment.PaymentMethod.CASH_ON_DELIVERY);
                    payment.setAmount(totalWithFee); // Include the COD fee
                    payment.setStatus(Payment.PaymentStatus.PENDING);
                    
                    // Save delivery information in reference field
                    String deliveryInfo = "Address: " + address + 
                                        "; Recipient: " + name + 
                                        "; Phone: " + phone;
                    
                    payment.setReference(deliveryInfo);
                    
                    // Save payment information
                    PaymentDAO paymentDAO = new PaymentDAO();
                    boolean paymentSuccess = paymentDAO.createPayment(payment);
                    
                    if (paymentSuccess) {
                        JOptionPane.showMessageDialog(this,
                            "Your order has been created successfully!\n\n" +
                            "Total amount (including COD fee): " + totalWithFee + " лв.\n" +
                            "You will pay this amount to the courier upon delivery.",
                            "Order Confirmation",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        dispose();
                        parentFrame.showOrderHistoryPanel();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "There was an error processing your payment information. Please try again.",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Could not retrieve order information. Please check your order history.",
                        "Order Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "There was an error creating your order. Please try again.",
                    "Order Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Process bank transfer payment
     */
    private void processBankTransferPayment() {
        try {
            // Get the current user and guitar data
            int userId = AuthenticationService.getInstance().getCurrentUser().getUserId();
            
            // Get customer name
            String customerName = customerNameField.getText().trim();
            
            // Create a bank transfer payment
            BankTransferPayment bankPayment = new BankTransferPayment(customerName);
            
            // Generate reference number for tracking this payment
            String reference = "SSM-" + System.currentTimeMillis() + "-" + userId;
            
            // Create the order
            boolean orderSuccess = orderService.createOrder(guitar.getGuitarId(), userId);
            
            if (orderSuccess) {
                // Since we don't have a getLastOrderId method, we'll use the most recent order
                // Get all orders by buyer and take the most recent one
                List<Order> buyerOrders = orderService.getOrdersByBuyer(userId);
                if (!buyerOrders.isEmpty()) {
                    // The most recent order should be the first in the list
                    Order order = buyerOrders.get(0);
                    
                    // Create payment record
                    Payment payment = new Payment();
                    payment.setOrderId(order.getOrderId());
                    payment.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
                    payment.setAmount(guitar.getPrice());
                    payment.setStatus(Payment.PaymentStatus.PENDING);
                    payment.setReference(reference);
                    
                    // Save payment information
                    PaymentDAO paymentDAO = new PaymentDAO();
                    boolean paymentSuccess = paymentDAO.createPayment(payment);
                    
                    if (paymentSuccess) {
                        // Show bank transfer instructions
                        String instructions = bankPayment.generatePaymentInstructions(guitar.getPrice());
                        
                        // Create a custom dialog to show the instructions
                        JDialog instructionsDialog = new JDialog(this, "Bank Transfer Instructions", true);
                        instructionsDialog.setSize(500, 400);
                        instructionsDialog.setLocationRelativeTo(this);
                        
                        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
                        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                        dialogPanel.setBackground(Color.WHITE);
                        
                        JLabel titleLabel = new JLabel("Please complete your bank transfer with these details:");
                        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                        dialogPanel.add(titleLabel, BorderLayout.NORTH);
                        
                        JTextArea instructionsArea = new JTextArea(instructions);
                        instructionsArea.setEditable(false);
                        instructionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        instructionsArea.setBackground(new Color(245, 245, 250));
                        instructionsArea.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(200, 200, 220)),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));
                        
                        JScrollPane scrollPane = new JScrollPane(instructionsArea);
                        dialogPanel.add(scrollPane, BorderLayout.CENTER);
                        
                        JButton closeButton = new JButton("Close");
                        closeButton.setBackground(Constants.PRIMARY_COLOR);
                        closeButton.setForeground(Color.BLACK);
                        closeButton.addActionListener(evt -> {
                            instructionsDialog.dispose();
                            dispose();
                            parentFrame.showOrderHistoryPanel();
                        });
                        
                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                        buttonPanel.setBackground(Color.WHITE);
                        buttonPanel.add(closeButton);
                        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
                        
                        instructionsDialog.setContentPane(dialogPanel);
                        instructionsDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "There was an error processing your payment information. Please try again.",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Could not retrieve order information. Please check your order history.",
                        "Order Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "There was an error creating your order. Please try again.",
                    "Order Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
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