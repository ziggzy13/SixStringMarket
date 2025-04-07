package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.BankTransferPayment;
import com.sixstringmarket.util.CashOnDeliveryPayment;
import com.sixstringmarket.util.CreditCardPayment;
import com.sixstringmarket.util.PaymentMethod;
import com.sixstringmarket.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * UI компонент за избор и въвеждане на информация за метода на плащане
 */
public class PaymentPanel extends JPanel {
	private JComboBox<PaymentMethod> paymentMethodComboBox;
	private JPanel cardDetailsPanel;
	private JPanel cashOnDeliveryPanel;
	private JPanel bankTransferPanel;

	private JFormattedTextField cardNumberField;
	private JTextField cardHolderField;
	private JFormattedTextField expiryDateField;
	private JPasswordField cvvField;

	private JTextField addressField;
	private JTextField recipientNameField;
	private JTextField recipientPhoneField;

	private JTextField customerNameField;
	private JTextArea bankInstructionsArea;

	private CreditCardPayment creditCardPayment;
	private CashOnDeliveryPayment cashOnDeliveryPayment;
	private BankTransferPayment bankTransferPayment;

	private BigDecimal orderTotal;

	/**
	 * Конструктор със сума на поръчката
	 * 
	 * @param orderTotal Общата сума на поръчката
	 */
	public PaymentPanel(BigDecimal orderTotal) {
		this.orderTotal = orderTotal;

		// Инициализиране на методите на плащане
		creditCardPayment = new CreditCardPayment();
		cashOnDeliveryPayment = new CashOnDeliveryPayment();
		bankTransferPayment = new BankTransferPayment();

		initializeUI();
	}

	/**
	 * Инициализиране на потребителския интерфейс
	 */
	private void initializeUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		// Панел за заглавие
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titlePanel.setBackground(Color.WHITE);

		JLabel titleLabel = new JLabel("Начин на плащане");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		titleLabel.setForeground(new Color(70, 80, 160));

		titlePanel.add(titleLabel);
		add(titlePanel, BorderLayout.NORTH);

		// Създаване на основния панел
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Панел за избор на метод на плащане
		JPanel paymentMethodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		paymentMethodPanel.setBackground(Color.WHITE);
		paymentMethodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel methodLabel = new JLabel("Изберете метод на плащане:");
		methodLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		methodLabel.setForeground(Constants.TEXT_COLOR); // Черен текст

		// Combo box за избор на метод на плащане
		paymentMethodComboBox = new JComboBox<>();
		paymentMethodComboBox.addItem(creditCardPayment);
		paymentMethodComboBox.addItem(cashOnDeliveryPayment);
		paymentMethodComboBox.addItem(bankTransferPayment);
		paymentMethodComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		paymentMethodComboBox.setPreferredSize(new Dimension(300, 30));
		paymentMethodComboBox.setForeground(Constants.TEXT_COLOR); // Черен текст
		paymentMethodComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePaymentDetailsPanel();
			}
		});

		paymentMethodPanel.add(methodLabel);
		paymentMethodPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		paymentMethodPanel.add(paymentMethodComboBox);

		mainPanel.add(paymentMethodPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		// Създаване на панелите за различните методи на плащане
		createCreditCardPanel();
		createCashOnDeliveryPanel();
		createBankTransferPanel();

		// Първоначално показваме панела за кредитна карта
		mainPanel.add(cardDetailsPanel);
		cardDetailsPanel.setVisible(true);
		cashOnDeliveryPanel.setVisible(false);
		bankTransferPanel.setVisible(false);

		add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Създава панела за плащане с кредитна карта
	 */
	private void createCreditCardPanel() {
		cardDetailsPanel = new JPanel();
		cardDetailsPanel.setLayout(new BoxLayout(cardDetailsPanel, BoxLayout.Y_AXIS));
		cardDetailsPanel.setBackground(Color.WHITE);
		cardDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
						"Данни за кредитна карта", TitledBorder.LEFT, TitledBorder.TOP,
						new Font("Arial", Font.BOLD, 14), new Color(70, 80, 160)),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		cardDetailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Номер на картата
		JPanel cardNumberPanel = createLabelFieldPanel("Номер на картата:", true);
		try {
			MaskFormatter cardNumberFormatter = new MaskFormatter("#### #### #### ####");
			cardNumberFormatter.setPlaceholderCharacter('_');
			cardNumberField = new JFormattedTextField(cardNumberFormatter);
		} catch (ParseException e) {
			// Ако има грешка, използваме обикновен JTextField
			cardNumberField = new JFormattedTextField();
		}
		cardNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
		cardNumberField.setPreferredSize(new Dimension(250, 30));
		cardNumberField.setForeground(Constants.TEXT_COLOR); // Черен текст
		cardNumberPanel.add(cardNumberField);

		// Име на картодържателя
		JPanel cardHolderPanel = createLabelFieldPanel("Име на картодържателя:", true);
		cardHolderField = new JTextField();
		cardHolderField.setFont(new Font("Arial", Font.PLAIN, 14));
		cardHolderField.setPreferredSize(new Dimension(250, 30));
		cardHolderField.setForeground(Constants.TEXT_COLOR); // Черен текст
		cardHolderPanel.add(cardHolderField);

		// Панел за срок на валидност и CVV
		JPanel expiryAndCvvPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		expiryAndCvvPanel.setBackground(Color.WHITE);
		expiryAndCvvPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Срок на валидност
		JPanel expiryPanel = createLabelFieldPanel("Срок на валидност (ММ/ГГ):", true);
		expiryPanel.setPreferredSize(new Dimension(250, 60));
		try {
			MaskFormatter expiryFormatter = new MaskFormatter("##/##");
			expiryFormatter.setPlaceholderCharacter('_');
			expiryDateField = new JFormattedTextField(expiryFormatter);
		} catch (ParseException e) {
			// Ако има грешка, използваме обикновен JTextField
			expiryDateField = new JFormattedTextField();
		}
		expiryDateField.setFont(new Font("Arial", Font.PLAIN, 14));
		expiryDateField.setPreferredSize(new Dimension(80, 30));
		expiryDateField.setForeground(Constants.TEXT_COLOR); // Черен текст
		expiryPanel.add(expiryDateField);

		// CVV код
		JPanel cvvPanel = createLabelFieldPanel("CVV:", true);
		cvvPanel.setPreferredSize(new Dimension(150, 60));
		cvvField = new JPasswordField();
		cvvField.setFont(new Font("Arial", Font.PLAIN, 14));
		cvvField.setPreferredSize(new Dimension(60, 30));
		cvvField.setForeground(Constants.TEXT_COLOR); // Черен текст
		cvvPanel.add(cvvField);

		expiryAndCvvPanel.add(expiryPanel);
		expiryAndCvvPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		expiryAndCvvPanel.add(cvvPanel);

		// Информация за сигурност
		JPanel securityInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		securityInfoPanel.setBackground(Color.WHITE);
		securityInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel securityLabel = new JLabel("<html><body style='color: #646464;'>Вашите данни са защитени с SSL шифроване.<br>" +
				"Ние не съхраняваме данните от вашата кредитна карта.</body></html>");
		securityLabel.setFont(new Font("Arial", Font.ITALIC, 12));

		securityInfoPanel.add(securityLabel);

		// Добавяне на панелите към главния панел
		cardDetailsPanel.add(cardNumberPanel);
		cardDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		cardDetailsPanel.add(cardHolderPanel);
		cardDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		cardDetailsPanel.add(expiryAndCvvPanel);
		cardDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		cardDetailsPanel.add(securityInfoPanel);
	}

	/**
	 * Създава панела за плащане с наложен платеж
	 */
	private void createCashOnDeliveryPanel() {
		cashOnDeliveryPanel = new JPanel();
		cashOnDeliveryPanel.setLayout(new BoxLayout(cashOnDeliveryPanel, BoxLayout.Y_AXIS));
		cashOnDeliveryPanel.setBackground(Color.WHITE);
		cashOnDeliveryPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
						"Данни за наложен платеж", TitledBorder.LEFT, TitledBorder.TOP,
						new Font("Arial", Font.BOLD, 14), new Color(70, 80, 160)),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		cashOnDeliveryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Адрес за доставка
		JPanel addressPanel = createLabelFieldPanel("Адрес за доставка:", true);
		addressField = new JTextField();
		addressField.setFont(new Font("Arial", Font.PLAIN, 14));
		addressField.setPreferredSize(new Dimension(350, 30));
		addressField.setForeground(Constants.TEXT_COLOR); // Черен текст
		addressPanel.add(addressField);

		// Име на получателя
		JPanel namePanel = createLabelFieldPanel("Име на получателя:", true);
		recipientNameField = new JTextField();
		recipientNameField.setFont(new Font("Arial", Font.PLAIN, 14));
		recipientNameField.setPreferredSize(new Dimension(350, 30));
		recipientNameField.setForeground(Constants.TEXT_COLOR); // Черен текст
		namePanel.add(recipientNameField);

		// Телефон на получателя
		JPanel phonePanel = createLabelFieldPanel("Телефон за връзка:", true);
		recipientPhoneField = new JTextField();
		recipientPhoneField.setFont(new Font("Arial", Font.PLAIN, 14));
		recipientPhoneField.setPreferredSize(new Dimension(350, 30));
		recipientPhoneField.setForeground(Constants.TEXT_COLOR); // Черен текст
		phonePanel.add(recipientPhoneField);

		// Информация за таксата
		JPanel feeInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		feeInfoPanel.setBackground(Color.WHITE);
		feeInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		BigDecimal fee = cashOnDeliveryPayment.getProcessingFee();
		BigDecimal totalWithFee = orderTotal.add(fee);

		JLabel feeLabel = new JLabel("<html><body style='color: #212121;'>Такса за наложен платеж: <b>" + fee + " лв.</b><br>" +
				"Обща сума за плащане: <b>" + totalWithFee + " лв.</b></body></html>");
		feeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		feeInfoPanel.add(feeLabel);

		// Добавяне на панелите към главния панел
		cashOnDeliveryPanel.add(addressPanel);
		cashOnDeliveryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		cashOnDeliveryPanel.add(namePanel);
		cashOnDeliveryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		cashOnDeliveryPanel.add(phonePanel);
		cashOnDeliveryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		cashOnDeliveryPanel.add(feeInfoPanel);
	}

	/**
	 * Създава панела за плащане с банков превод
	 */
	private void createBankTransferPanel() {
		bankTransferPanel = new JPanel();
		bankTransferPanel.setLayout(new BoxLayout(bankTransferPanel, BoxLayout.Y_AXIS));
		bankTransferPanel.setBackground(Color.WHITE);
		bankTransferPanel
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
								"Данни за банков превод", TitledBorder.LEFT, TitledBorder.TOP,
								new Font("Arial", Font.BOLD, 14), new Color(70, 80, 160)),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		bankTransferPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Име на клиента
		JPanel namePanel = createLabelFieldPanel("Вашето име (както ще извършите превода):", true);
		customerNameField = new JTextField();
		customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
		customerNameField.setPreferredSize(new Dimension(350, 30));
		customerNameField.setForeground(Constants.TEXT_COLOR); // Черен текст
		namePanel.add(customerNameField);

		// Инструкции за банков превод
		JPanel instructionsPanel = new JPanel();
		instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
		instructionsPanel.setBackground(Color.WHITE);
		instructionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel instructionsLabel = new JLabel("Инструкции за банков превод:");
		instructionsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		instructionsLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		instructionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		bankInstructionsArea = new JTextArea();
		bankInstructionsArea.setFont(new Font("Arial", Font.PLAIN, 14));
		bankInstructionsArea.setLineWrap(true);
		bankInstructionsArea.setWrapStyleWord(true);
		bankInstructionsArea.setEditable(false);
		bankInstructionsArea.setBackground(new Color(245, 245, 255));
		bankInstructionsArea.setForeground(Constants.TEXT_COLOR); // Черен текст
		bankInstructionsArea
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		bankInstructionsArea.setText(bankTransferPayment.generatePaymentInstructions(orderTotal));

		JScrollPane scrollPane = new JScrollPane(bankInstructionsArea);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		instructionsPanel.add(instructionsLabel);
		instructionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		instructionsPanel.add(scrollPane);

		// Добавяне на панелите към главния панел
		bankTransferPanel.add(namePanel);
		bankTransferPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		bankTransferPanel.add(instructionsPanel);
	}

	/**
	 * Създава панел с етикет и поле за въвеждане
	 * 
	 * @param labelText Текст на етикета
	 * @param required  Дали полето е задължително
	 * @return Панел с етикет и поле за въвеждане
	 */
	private JPanel createLabelFieldPanel(String labelText, boolean required) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.setBackground(Color.WHITE);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel labelPanel = new JPanel(new BorderLayout());
		labelPanel.setBackground(Color.WHITE);
		labelPanel.setPreferredSize(new Dimension(200, 30));

		JLabel label = new JLabel(labelText + (required ? " *" : ""));
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		label.setForeground(Constants.TEXT_COLOR); // Черен текст
		labelPanel.add(label, BorderLayout.WEST);

		panel.add(labelPanel);

		return panel;
	}

	/**
	 * Обновява панела за въвеждане на данни за плащане в зависимост от избрания
	 * метод
	 */
	private void updatePaymentDetailsPanel() {
		PaymentMethod selectedMethod = (PaymentMethod) paymentMethodComboBox.getSelectedItem();

		// Скриване на всички панели
		cardDetailsPanel.setVisible(false);
		cashOnDeliveryPanel.setVisible(false);
		bankTransferPanel.setVisible(false);

		// Показване на избрания панел
		if (selectedMethod instanceof CreditCardPayment) {
			cardDetailsPanel.setVisible(true);
		} else if (selectedMethod instanceof CashOnDeliveryPayment) {
			cashOnDeliveryPanel.setVisible(true);
		} else if (selectedMethod instanceof BankTransferPayment) {
			bankTransferPanel.setVisible(true);
		}

		// Преизчисляване на размера на панела
		revalidate();
		repaint();
	}

	/**
	 * Валидира въведените данни за плащане
	 * 
	 * @return true ако данните са валидни, false в противен случай
	 */
	public boolean validatePaymentData() {
		PaymentMethod selectedMethod = (PaymentMethod) paymentMethodComboBox.getSelectedItem();

		if (selectedMethod instanceof CreditCardPayment) {
			// Валидация на данните за кредитна карта
			String cardNumber = cardNumberField.getText().replaceAll("\\s", "");
			String cardHolder = cardHolderField.getText();
			String expiryDate = expiryDateField.getText();
			String cvv = new String(cvvField.getPassword());

			// Актуализиране на обекта за плащане с кредитна карта
			creditCardPayment.setCardNumber(cardNumber);
			creditCardPayment.setCardHolderName(cardHolder);
			creditCardPayment.setExpiryDate(expiryDate);
			creditCardPayment.setCvv(cvv);

			if (!creditCardPayment.isValidCardNumber()) {
				JOptionPane.showMessageDialog(this, "Невалиден номер на кредитна карта", "Грешка във формата",
						JOptionPane.ERROR_MESSAGE);
				cardNumberField.requestFocusInWindow();
				return false;
			}

			if (!creditCardPayment.isValidCardHolderName()) {
				JOptionPane.showMessageDialog(this, "Моля, въведете име на картодържателя", "Грешка във формата",
						JOptionPane.ERROR_MESSAGE);
				cardHolderField.requestFocusInWindow();
				return false;
			}

			if (!creditCardPayment.isValidExpiryDate()) {
				JOptionPane.showMessageDialog(this, "Невалиден срок на валидност на картата", "Грешка във формата",
						JOptionPane.ERROR_MESSAGE);
				expiryDateField.requestFocusInWindow();
				return false;
			}

			if (!creditCardPayment.isValidCVV()) {
				JOptionPane.showMessageDialog(this, "Невалиден CVV код", "Грешка във формата",
						JOptionPane.ERROR_MESSAGE);
				cvvField.requestFocusInWindow();
				return false;
			}

			return true;
		} else if (selectedMethod instanceof CashOnDeliveryPayment) {
			// Валидация на данните за наложен платеж
			String address = addressField.getText();
			String name = recipientNameField.getText();
			String phone = recipientPhoneField.getText();

			// Актуализиране на обекта за плащане с наложен платеж
			cashOnDeliveryPayment.setDeliveryAddress(address);
			cashOnDeliveryPayment.setRecipientName(name);
			cashOnDeliveryPayment.setRecipientPhone(phone);

			if (!cashOnDeliveryPayment.isValidDeliveryAddress()) {
				JOptionPane.showMessageDialog(this, "Моля, въведете валиден адрес за доставка", "Грешка във формата",
						JOptionPane.ERROR_MESSAGE);
				addressField.requestFocusInWindow();
				return false;
			}

			if (!cashOnDeliveryPayment.isValidRecipientName()) {
				JOptionPane.showMessageDialog(this, "Моля, въведете име на получателя", "Грешка във формата",
						JOptionPane.ERROR_MESSAGE);
				recipientNameField.requestFocusInWindow();
				return false;
			}

			if (!cashOnDeliveryPayment.isValidRecipientPhone()) {
				JOptionPane.showMessageDialog(this, "Моля, въведете валиден телефонен номер (напр. 0895123456)",
						"Грешка във формата", JOptionPane.ERROR_MESSAGE);
				recipientPhoneField.requestFocusInWindow();
				return false;
			}

			return true;
		} else if (selectedMethod instanceof BankTransferPayment) {
			// Валидация на данните за банков превод
			String name = customerNameField.getText();

			// Актуализиране на обекта за плащане с банков превод
			bankTransferPayment.setCustomerName(name);

			if (!bankTransferPayment.isValidCustomerName()) {
				JOptionPane.showMessageDialog(this, "Моля, въведете вашето име за банковия превод",
						"Грешка във формата", JOptionPane.ERROR_MESSAGE);
				customerNameField.requestFocusInWindow();
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * Връща избрания метод на плащане
	 * 
	 * @return Избраният метод на плащане
	 */
	public PaymentMethod getSelectedPaymentMethod() {
		return (PaymentMethod) paymentMethodComboBox.getSelectedItem();
	}

	/**
	 * Връща обекта за плащане с кредитна карта
	 * 
	 * @return Обект за плащане с кредитна карта
	 */
	public CreditCardPayment getCreditCardPayment() {
		return creditCardPayment;
	}

	/**
	 * Връща обекта за плащане с наложен платеж
	 * 
	 * @return Обект за плащане с наложен платеж
	 */
	public CashOnDeliveryPayment getCashOnDeliveryPayment() {
		return cashOnDeliveryPayment;
	}

	/**
	 * Връща обекта за плащане с банков превод
	 * 
	 * @return Обект за плащане с банков превод
	 */
	public BankTransferPayment getBankTransferPayment() {
		return bankTransferPayment;
	}
}