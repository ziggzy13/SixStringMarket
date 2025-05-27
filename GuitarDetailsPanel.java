package com.sixstringmarket.ui;

import com.sixstringmarket.dao.GuitarReviewDAO;
import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.GuitarReview;
import com.sixstringmarket.model.User;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.service.GuitarService;
import com.sixstringmarket.service.OrderService;
import com.sixstringmarket.service.SavedGuitarService;
import com.sixstringmarket.service.UserService;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ImageHandler;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;

/**
 * Панел за показване на детайли за китара
 */
public class GuitarDetailsPanel extends JPanel {

	private MainFrame parentFrame;
	private GuitarService guitarService;
	private UserService userService;
	private OrderService orderService;
	private SavedGuitarService savedGuitarService;
	private Guitar guitar;
	private User seller;
	private User currentUser;

	/**
	 * Конструктор
	 * 
	 * @param parentFrame Родителският прозорец
	 * @param guitarId    ID на китарата
	 */
	public GuitarDetailsPanel(MainFrame parentFrame, int guitarId) {
		this.parentFrame = parentFrame;
		this.guitarService = new GuitarService();
		this.userService = new UserService();
		this.orderService = new OrderService();
		this.savedGuitarService = new SavedGuitarService();
		this.currentUser = AuthenticationService.getInstance().getCurrentUser();

		// Зареждане на данните за китарата
		this.guitar = guitarService.getGuitarById(guitarId);
		if (this.guitar == null) {
			showErrorMessage("Китарата не е намерена");
			return;
		}

		// Зареждане на данните за продавача
		this.seller = userService.getUserById(guitar.getSellerId());

		// Настройка на панела
		setLayout(new BorderLayout(10, 10));
		setBackground(Constants.PANEL_COLOR); // Бял фон
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Инициализация на компонентите
		initComponents();
	}

	/**
	 * Инициализира компонентите на панела
	 */
	private void initComponents() {
		// Горен панел с навигация
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		JButton backButton = new JButton("« Назад");
		backButton.addActionListener(e -> parentFrame.showGuitarListPanel());
		backButton.setForeground(Constants.TEXT_COLOR);
		topPanel.add(backButton, BorderLayout.WEST);

		add(topPanel, BorderLayout.NORTH);

		// Основен панел с информация
		JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
		mainPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		// Ляв панел със снимка
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		// Зареждане на снимката
		BufferedImage image = null;
		if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
			image = ImageHandler.loadImage(guitar.getImagePath());
		}

		if (image != null) {
			JLabel imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(400, 400, Image.SCALE_SMOOTH)));
			imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			leftPanel.add(imageLabel, BorderLayout.CENTER);
		} else {
			// Заместваща снимка
			JPanel placeholderPanel = new JPanel();
			placeholderPanel.setPreferredSize(new Dimension(400, 400));
			placeholderPanel.setBackground(Color.WHITE);
			placeholderPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			JLabel placeholderLabel = new JLabel("Няма снимка", JLabel.CENTER);
			placeholderLabel.setFont(Constants.DEFAULT_FONT);
			placeholderLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
			placeholderPanel.add(placeholderLabel);
			leftPanel.add(placeholderPanel, BorderLayout.CENTER);
		}

		mainPanel.add(leftPanel, BorderLayout.WEST);

		// Десен панел с детайли
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		// Заглавие
		JLabel titleLabel = new JLabel(guitar.getTitle());
		titleLabel.setFont(Constants.TITLE_FONT);
		titleLabel.setForeground(Constants.PRIMARY_COLOR);
		rightPanel.add(titleLabel);
		rightPanel.add(Box.createVerticalStrut(10));

		// Цена
		JLabel priceLabel = new JLabel("Цена: " + guitar.getPrice() + " лв.");
		priceLabel.setFont(new Font(Constants.BOLD_FONT.getName(), Font.BOLD, 18));
		priceLabel.setForeground(new Color(76, 175, 80)); // Зелен цвят за цената
		rightPanel.add(priceLabel);
		rightPanel.add(Box.createVerticalStrut(20));

		// Основна информация - всички текстове трябва да са черни
		JLabel brandLabel = new JLabel("Марка: " + guitar.getBrand());
		brandLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		rightPanel.add(brandLabel);
		rightPanel.add(Box.createVerticalStrut(5));

		if (guitar.getModel() != null && !guitar.getModel().isEmpty()) {
			JLabel modelLabel = new JLabel("Модел: " + guitar.getModel());
			modelLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
			rightPanel.add(modelLabel);
			rightPanel.add(Box.createVerticalStrut(5));
		}

		JLabel typeLabel = new JLabel("Тип: " + getGuitarTypeText(guitar.getType()));
		typeLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		rightPanel.add(typeLabel);
		rightPanel.add(Box.createVerticalStrut(5));

		JLabel conditionLabel = new JLabel("Състояние: " + getConditionText(guitar.getCondition()));
		conditionLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		rightPanel.add(conditionLabel);
		rightPanel.add(Box.createVerticalStrut(5));

		if (guitar.getManufacturingYear() != null) {
			JLabel yearLabel = new JLabel("Година на производство: " + guitar.getManufacturingYear());
			yearLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
			rightPanel.add(yearLabel);
			rightPanel.add(Box.createVerticalStrut(5));
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		JLabel dateLabel = new JLabel("Публикувана на: " + dateFormat.format(guitar.getDateAdded()));
		dateLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		rightPanel.add(dateLabel);
		rightPanel.add(Box.createVerticalStrut(5));

		JLabel sellerLabel = new JLabel("Продавач: " + seller.getUsername());
		sellerLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		rightPanel.add(sellerLabel);
		rightPanel.add(Box.createVerticalStrut(5));

		JLabel statusLabel = new JLabel("Статус: " + getStatusText(guitar.getStatus()));
		statusLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		rightPanel.add(statusLabel);
		rightPanel.add(Box.createVerticalStrut(20));

		// Описание
		if (guitar.getDescription() != null && !guitar.getDescription().isEmpty()) {
			JLabel descriptionTitle = new JLabel("Описание:");
			descriptionTitle.setFont(Constants.BOLD_FONT);
			descriptionTitle.setForeground(Constants.TEXT_COLOR); // Черен текст
			rightPanel.add(descriptionTitle);
			rightPanel.add(Box.createVerticalStrut(5));

			JTextArea descriptionArea = new JTextArea(guitar.getDescription());
			descriptionArea.setLineWrap(true);
			descriptionArea.setWrapStyleWord(true);
			descriptionArea.setEditable(false);
			descriptionArea.setBackground(Constants.PANEL_COLOR); // Бял фон
			descriptionArea.setForeground(Constants.TEXT_COLOR); // Черен текст
			descriptionArea.setFont(Constants.DEFAULT_FONT);

			JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
			descriptionScroll.setBorder(BorderFactory.createEmptyBorder());
			descriptionScroll.setPreferredSize(new Dimension(400, 100));

			rightPanel.add(descriptionScroll);
			rightPanel.add(Box.createVerticalStrut(20));
		}

		// Бутони за действия
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		// Бутоните се показват само ако потребителят е влязъл в системата
		if (currentUser != null) {
			// Потребителят не може да купува собствените си китари
			boolean isOwner = currentUser.getUserId() == guitar.getSellerId();
			boolean isActive = guitar.getStatus() == Guitar.Status.ACTIVE
					|| guitar.getStatus() == Guitar.Status.RESERVED;

			if (!isOwner && isActive) {
				JButton buyButton = new JButton("Купи");
				buyButton.setBackground(Constants.ACCENT_COLOR);
				buyButton.setForeground(Color.BLACK);
				buyButton.addActionListener(e -> purchaseGuitar());
				buttonsPanel.add(buyButton);

				// Бутон за запазване/премахване от запазени
				boolean isSaved = savedGuitarService.isGuitarSaved(currentUser.getUserId(), guitar.getGuitarId());
				JButton saveButton = new JButton(isSaved ? "Премахни от запазени" : "Запази");
				saveButton.setBackground(isSaved ? Color.GRAY : Constants.SECONDARY_COLOR);
				saveButton.setForeground(Color.BLACK);
				saveButton.addActionListener(e -> {
				    // Вземане на текущото състояние от текста на бутона
				    boolean currentSaved = saveButton.getText().equals("Премахни от запазени");
				    toggleSaveGuitar(saveButton, currentSaved);
				});
				buttonsPanel.add(saveButton);
			}

			// Собственикът може да редактира своите китари
			if (isOwner) {
				JButton editButton = new JButton("Редактирай");
				editButton.setBackground(Constants.PRIMARY_COLOR);
				editButton.setForeground(Color.BLACK);
				editButton.addActionListener(e -> parentFrame.showEditGuitarFrame(guitar.getGuitarId()));
				buttonsPanel.add(editButton);

				JButton removeButton = new JButton("Премахни обявата");
				removeButton.setBackground(Color.RED);
				removeButton.setForeground(Color.BLACK);
				removeButton.addActionListener(e -> removeGuitar());
				buttonsPanel.add(removeButton);
			}
		}

		rightPanel.add(buttonsPanel);

		mainPanel.add(rightPanel, BorderLayout.CENTER);

		add(mainPanel, BorderLayout.CENTER);

		// Добавяне на панел за отзиви
		JPanel reviewsPanel = createReviewsPanel();
		add(reviewsPanel, BorderLayout.SOUTH);
	}

	/**
	 * Create reviews panel
	 */
	private JPanel createReviewsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Constants.PANEL_COLOR); // Бял фон
		panel.setBorder(BorderFactory.createTitledBorder("Customer Reviews"));

		// Get reviews
		GuitarReviewDAO reviewDAO = new GuitarReviewDAO();
		List<GuitarReview> reviews = reviewDAO.getReviewsByGuitarId(guitar.getGuitarId());

		// Average rating
		double avgRating = reviewDAO.getAverageRatingForGuitar(guitar.getGuitarId());
		JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ratingPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		JLabel avgRatingLabel = new JLabel(String.format("Average Rating: %.1f/5.0", avgRating));
		avgRatingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		avgRatingLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		ratingPanel.add(avgRatingLabel);

		// Add rating stars
		for (int i = 1; i <= 5; i++) {
			JLabel starLabel = new JLabel(i <= Math.round(avgRating) ? "★" : "☆");
			starLabel.setFont(new Font("Arial Unicode MS", Font.PLAIN, 18));
			starLabel.setForeground(i <= Math.round(avgRating) ? new Color(255, 215, 0) : Color.GRAY);
			ratingPanel.add(starLabel);
		}

		panel.add(ratingPanel);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));

		// Add review button
		if (currentUser != null
				&& (currentUser.getUserId() != guitar.getSellerId() || AuthenticationService.getInstance().isAdmin())) {
			JButton addReviewButton = new JButton("Add Review");
			addReviewButton.setBackground(Constants.SECONDARY_COLOR);
			addReviewButton.setForeground(Color.BLACK); // Black text
			addReviewButton.addActionListener(e -> showAddReviewDialog());

			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttonPanel.setBackground(Constants.BACKGROUND_COLOR);
			buttonPanel.add(addReviewButton);

			panel.add(buttonPanel);
			panel.add(Box.createRigidArea(new Dimension(0, 10)));
		}

		// Reviews list
		if (reviews.isEmpty()) {
			JLabel noReviewsLabel = new JLabel("No reviews yet");
			noReviewsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
			noReviewsLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
			noReviewsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

			panel.add(noReviewsLabel);
		} else {
			for (GuitarReview review : reviews) {
				// Get username
				UserService userService = new UserService();
				User reviewer = userService.getUserById(review.getUserId());

				JPanel reviewPanel = new JPanel();
				reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
				reviewPanel.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
						BorderFactory.createEmptyBorder(5, 0, 5, 0)));
				reviewPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

				// Reviewer and date
				JPanel headerPanel = new JPanel(new BorderLayout());
				headerPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

				JLabel reviewerLabel = new JLabel(reviewer != null ? reviewer.getUsername() : "Unknown User");
				reviewerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
				reviewerLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
				headerPanel.add(reviewerLabel, BorderLayout.WEST);

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				JLabel dateLabel = new JLabel(dateFormat.format(review.getReviewDate()));
				dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
				dateLabel.setForeground(Constants.TEXT_SECONDARY_COLOR);
				headerPanel.add(dateLabel, BorderLayout.EAST);

				reviewPanel.add(headerPanel);

				// Rating
				JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
				starsPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

				for (int i = 1; i <= 5; i++) {
					JLabel starLabel = new JLabel(i <= review.getRating() ? "★" : "☆");
					starLabel.setFont(new Font("Arial Unicode MS ", Font.PLAIN, 16));
					starLabel.setForeground(i <= review.getRating() ? new Color(255, 215, 0) : Color.GRAY);
					starsPanel.add(starLabel);
				}

				reviewPanel.add(starsPanel);

				// Comment
				if (review.getComment() != null && !review.getComment().isEmpty()) {
					JTextArea commentArea = new JTextArea(review.getComment());
					commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
					commentArea.setLineWrap(true);
					commentArea.setWrapStyleWord(true);
					commentArea.setEditable(false);
					commentArea.setBackground(Constants.PANEL_COLOR); // Бял фон
					commentArea.setForeground(Constants.TEXT_COLOR); // Черен текст
					commentArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
					commentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

					reviewPanel.add(commentArea);
				}

				panel.add(reviewPanel);
				panel.add(Box.createRigidArea(new Dimension(0, 10)));
			}
		}

		return panel;
	}

	/**
	 * Show dialog to add a review
	 */
	private void showAddReviewDialog() {
		JDialog reviewDialog = new JDialog(parentFrame, "Add Review", true);
		reviewDialog.setSize(400, 300);
		reviewDialog.setLocationRelativeTo(parentFrame);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		panel.setBackground(Constants.PANEL_COLOR); // Бял фон

		// Rating
		JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ratingPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		JLabel ratingLabel = new JLabel("Rating:");
		ratingLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		ratingPanel.add(ratingLabel);

		JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
		ratingCombo.setSelectedIndex(4); // Default 5 stars
		ratingPanel.add(ratingCombo);

		panel.add(ratingPanel);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));

		// Comment
		JLabel commentLabel = new JLabel("Comment:");
		commentLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
		commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(commentLabel);

		JTextArea commentArea = new JTextArea(5, 20);
		commentArea.setLineWrap(true);
		commentArea.setWrapStyleWord(true);
		commentArea.setForeground(Constants.TEXT_COLOR); // Черен текст
		JScrollPane scrollPane = new JScrollPane(commentArea);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(scrollPane);
		panel.add(Box.createRigidArea(new Dimension(0, 15)));

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(Constants.PANEL_COLOR); // Бял фон

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> reviewDialog.dispose());
		cancelButton.setBackground(Constants.PRIMARY_COLOR);
		cancelButton.setForeground(Color.BLACK);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setBackground(Constants.PRIMARY_COLOR);
		submitButton.setForeground(Color.BLACK);
		submitButton.addActionListener(e -> {
			// Create review
			GuitarReview review = new GuitarReview(guitar.getGuitarId(), currentUser.getUserId(),
					(Integer) ratingCombo.getSelectedItem(), commentArea.getText());

			// Save review
			GuitarReviewDAO reviewDAO = new GuitarReviewDAO();
			boolean success = reviewDAO.addReview(review);

			if (success) {
				JOptionPane.showMessageDialog(reviewDialog, "Review submitted successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				reviewDialog.dispose();

				// Refresh panel
				removeAll();
				initComponents();
				revalidate();
				repaint();
			} else {
				JOptionPane.showMessageDialog(reviewDialog, "Error submitting review", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		buttonPanel.add(cancelButton);
		buttonPanel.add(submitButton);

		panel.add(buttonPanel);

		reviewDialog.setContentPane(panel);
		reviewDialog.setVisible(true);
	}

	/**
	 * Преобразува типа на китарата в текст
	 * 
	 * @param type Типът на китарата
	 * @return Текстово представяне
	 */
	private String getGuitarTypeText(Guitar.GuitarType type) {
		switch (type) {
		case ACOUSTIC:
			return "Акустична";
		case ELECTRIC:
			return "Електрическа";
		case CLASSICAL:
			return "Класическа";
		case BASS:
			return "Бас";
		case OTHER:
			return "Друга";
		default:
			return "Неизвестен";
		}
	}

	/**
	 * Преобразува състоянието на китарата в текст
	 * 
	 * @param condition Състоянието на китарата
	 * @return Текстово представяне
	 */
	private String getConditionText(Guitar.Condition condition) {
		switch (condition) {
		case NEW:
			return "Нова";
		case USED:
			return "Употребявана";
		case VINTAGE:
			return "Винтидж";
		default:
			return "Неизвестно";
		}
	}

	/**
	 * Преобразува статуса на китарата в текст
	 * 
	 * @param status Статусът на китарата
	 * @return Текстово представяне
	 */
	private String getStatusText(Guitar.Status status) {
		switch (status) {
		case ACTIVE:
			return "Активна";
		case SOLD:
			return "Продадена";
		case RESERVED:
			return "Резервирана";
		case REMOVED:
			return "Премахната";
		default:
			return "Неизвестен";
		}
	}

	/**
	 * Закупуване на китара
	 */
	private void purchaseGuitar() {
		CheckoutFrame checkoutFrame = new CheckoutFrame(parentFrame, guitar);
		checkoutFrame.setVisible(true);
		// Диалогът за потвърждение се появява след затваряне на checkoutFrame
	}

	/**
	 * Превключване на запазване/премахване от запазени
	 * 
	 * @param saveButton Бутонът за запазване
	 * @param isSaved    Флаг дали китарата е запазена
	 */
	private void toggleSaveGuitar(JButton saveButton, boolean isSaved) {
	    boolean success;
	    
	    if (isSaved) {
	        success = savedGuitarService.removeFromSaved(currentUser.getUserId(), guitar.getGuitarId());
	        if (success) {
	            saveButton.setText("Запази");
	            saveButton.setBackground(Constants.SECONDARY_COLOR);
	        }
	    } else {
	        success = savedGuitarService.saveGuitar(currentUser.getUserId(), guitar.getGuitarId());
	        if (success) {
	            saveButton.setText("Премахни от запазени");
	            saveButton.setBackground(Color.GRAY);
	        }
	    }
	    
	    if (!success) {
	        showErrorMessage("Грешка при обработката. Моля, опитайте отново.");
	    }
	}

	/**
	 * Премахване на обява за китара
	 */
	private void removeGuitar() {
		int response = JOptionPane.showConfirmDialog(this, "Сигурни ли сте, че искате да премахнете тази обява?",
				"Потвърждение", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (response == JOptionPane.YES_OPTION) {
			boolean success = guitarService.removeGuitar(guitar.getGuitarId());

			if (success) {
				JOptionPane.showMessageDialog(this, "Обявата е премахната успешно.", "Успешно премахване",
						JOptionPane.INFORMATION_MESSAGE);

				parentFrame.showMyGuitarsPanel();
			} else {
				showErrorMessage("Грешка при премахване на обявата. Моля, опитайте отново.");
			}
		}
	}

	/**
	 * Показва съобщение за грешка
	 * 
	 * @param message Съобщението
	 */
	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Грешка", JOptionPane.ERROR_MESSAGE);
	}
}