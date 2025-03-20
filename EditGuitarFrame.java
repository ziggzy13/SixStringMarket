package com.sixstringmarket.ui;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.model.Guitar.Condition;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.service.GuitarService;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;

/**
 * Прозорец за редактиране на китара
 */
public class EditGuitarFrame extends JDialog {
    
    private MainFrame parentFrame;
    private GuitarService guitarService;
    private Guitar guitar;
    
    private JTextField titleField;
    private JComboBox<String> brandCombo;
    private JTextField modelField;
    private JComboBox<String> typeCombo;
    private JComboBox<String> conditionCombo;
    private JTextField yearField;
    private JTextField priceField;
    private JTextArea descriptionArea;
    private JTextField imagePathField;
    private JButton browseButton;
    private JButton saveButton;
    private JButton cancelButton;
    
    private File selectedImageFile;
    
    /**
     * Конструктор
     * @param parentFrame Родителският прозорец
     * @param guitarId ID на китарата за редактиране
     */
    public EditGuitarFrame(MainFrame parentFrame, int guitarId) {
        super(parentFrame, "Редактиране на китара", true);
        this.parentFrame = parentFrame;
        this.guitarService = new GuitarService();
        
        // Зареждане на данните за китарата
        this.guitar = guitarService.getGuitarById(guitarId);
        if (this.guitar == null) {
            JOptionPane.showMessageDialog(parentFrame,
                "Китарата не е намерена",
                "Грешка",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Проверка дали текущият потребител има права да редактира тази китара
        if (!AuthenticationService.getInstance().hasPermission("edit", guitar.getSellerId())) {
            JOptionPane.showMessageDialog(parentFrame,
                "Нямате права да редактирате тази китара",
                "Грешка",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        setSize(600, 600);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        
        initComponents();
        loadGuitarData();
        layoutComponents();
        addEventListeners();
    }
    
    /**
     * Инициализира компонентите
     */
    private void initComponents() {
        titleField = new JTextField(20);
        
        // Падащо меню за марка
        brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.setEditable(true);
        
        modelField = new JTextField(20);
        
        // Падащо меню за тип
        String[] types = {"Акустична", "Електрическа", "Класическа", "Бас", "Друга"};
        typeCombo = new JComboBox<>(types);
        
        // Падащо меню за състояние
        String[] conditions = {"Нова", "Употребявана", "Винтидж"};
        conditionCombo = new JComboBox<>(conditions);
        
        yearField = new JTextField(10);
        priceField = new JTextField(10);
        
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        imagePathField = new JTextField(20);
        imagePathField.setEditable(false);
        
        browseButton = new JButton("Промени снимка");
        
        saveButton = new JButton("Запази промените");
        saveButton.setBackground(Constants.PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Отказ");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
    }
    
    /**
     * Зарежда данните за китарата в полетата
     */
    private void loadGuitarData() {
        titleField.setText(guitar.getTitle());
        
        // Избиране на марката от падащото меню или добавяне ако не съществува
        boolean brandFound = false;
        for (int i = 0; i < brandCombo.getItemCount(); i++) {
            if (brandCombo.getItemAt(i).equals(guitar.getBrand())) {
                brandCombo.setSelectedIndex(i);
                brandFound = true;
                break;
            }
        }
        if (!brandFound) {
            brandCombo.addItem(guitar.getBrand());
            brandCombo.setSelectedItem(guitar.getBrand());
        }
        
        modelField.setText(guitar.getModel() != null ? guitar.getModel() : "");
        
        // Избиране на типа
        switch (guitar.getType()) {
            case ACOUSTIC: typeCombo.setSelectedIndex(0); break;
            case ELECTRIC: typeCombo.setSelectedIndex(1); break;
            case CLASSICAL: typeCombo.setSelectedIndex(2); break;
            case BASS: typeCombo.setSelectedIndex(3); break;
            case OTHER: typeCombo.setSelectedIndex(4); break;
        }
        
        // Избиране на състоянието
        switch (guitar.getCondition()) {
            case NEW: conditionCombo.setSelectedIndex(0); break;
            case USED: conditionCombo.setSelectedIndex(1); break;
            case VINTAGE: conditionCombo.setSelectedIndex(2); break;
        }
        
        yearField.setText(guitar.getManufacturingYear() != null ? guitar.getManufacturingYear().toString() : "");
        priceField.setText(guitar.getPrice().toString());
        descriptionArea.setText(guitar.getDescription() != null ? guitar.getDescription() : "");
        
        // Показване на пътя до снимката ако има такава
        if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
            imagePathField.setText("Има качена снимка");
        } else {
            imagePathField.setText("Няма снимка");
        }
    }
    
    /**
     * Подрежда компонентите
     */
    private void layoutComponents() {
        // Главен панел с форма
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Заглавие
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Заглавие:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(titleField, gbc);
        
        // Марка
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Марка:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(brandCombo, gbc);
        
        // Модел
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Модел:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(modelField, gbc);
        
        // Тип
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Тип:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(typeCombo, gbc);
        
        // Състояние
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Състояние:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(conditionCombo, gbc);
        
        // Година на производство
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Година:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(yearField, gbc);
        
        // Цена
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Цена (лв.):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(priceField, gbc);
        
        // Описание
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Описание:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        formPanel.add(descriptionScroll, gbc);
        
        // Снимка
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Снимка:"), gbc);
        
        JPanel imagePanel = new JPanel(new BorderLayout(5, 0));
        imagePanel.setBackground(Constants.BACKGROUND_COLOR);
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(imagePanel, gbc);
        
        // Бутони
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Constants.BACKGROUND_COLOR);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Главен панел
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Constants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    /**
     * Добавя слушатели за събития
     */
    private void addEventListeners() {
        // Бутон за избор на снимка
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImageFile();
            }
        });
        
        // Бутон за запазване
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGuitar();
            }
        });
        
        // Бутон за отказ
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Избор на файл за снимка
     */
    private void selectImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Избор на нова снимка");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Изображения (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            imagePathField.setText(selectedImageFile.getAbsolutePath());
        }
    }
    
    /**
     * Запазване на промените по китарата
     */
    private void saveGuitar() {
        // Валидация на данните
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Моля, въведете заглавие.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String brand = brandCombo.getSelectedItem().toString().trim();
        if (brand.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Моля, въведете марка.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String priceText = priceField.getText().trim();
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Моля, въведете цена.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        BigDecimal price;
        try {
            price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Цената трябва да бъде положително число.",
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Невалидна цена. Моля, въведете число.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Валидация на годината
        Integer year = null;
        String yearText = yearField.getText().trim();
        if (!yearText.isEmpty()) {
            if (!ValidationUtils.isValidYear(yearText)) {
                JOptionPane.showMessageDialog(this,
                    "Невалидна година. Моля, въведете година между 1900 и текущата година.",
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            year = Integer.parseInt(yearText);
        }
        
        // Обновяване на обекта китара
        guitar.setTitle(title);
        guitar.setBrand(brand);
        guitar.setModel(modelField.getText().trim());
        
        // Определяне на типа
        switch (typeCombo.getSelectedIndex()) {
            case 0: guitar.setType(GuitarType.ACOUSTIC); break;
            case 1: guitar.setType(GuitarType.ELECTRIC); break;
            case 2: guitar.setType(GuitarType.CLASSICAL); break;
            case 3: guitar.setType(GuitarType.BASS); break;
            case 4: guitar.setType(GuitarType.OTHER); break;
        }
        
        // Определяне на състоянието
        switch (conditionCombo.getSelectedIndex()) {
            case 0: guitar.setCondition(Condition.NEW); break;
            case 1: guitar.setCondition(Condition.USED); break;
            case 2: guitar.setCondition(Condition.VINTAGE); break;
        }
        
        guitar.setManufacturingYear(year);
        guitar.setPrice(price);
        guitar.setDescription(descriptionArea.getText().trim());
        
        // Обновяване на китарата
        try {
            boolean success = guitarService.updateGuitar(guitar, selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Китарата е обновена успешно.",
                    "Успешно обновяване",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Затваряне на прозореца и опресняване на детайлите на китарата
                dispose();
                parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
            } else {
                JOptionPane.showMessageDialog(this,
                    "Грешка при обновяване на китарата.",
                    "Грешка",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}