package com.sixstringmarket.ui;

import com.sixstringmarket.model.Order;
import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.User;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.service.OrderService;
import com.sixstringmarket.service.GuitarService;
import com.sixstringmarket.service.UserService;
import com.sixstringmarket.util.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Панел за показване на история на поръчките
 */
public class OrderHistoryPanel extends JPanel {
    
    private MainFrame parentFrame;
    private OrderService orderService;
    private GuitarService guitarService;
    private UserService userService;
    private User currentUser;
    
    private JTabbedPane tabbedPane;
    private JTable buyerOrdersTable;
    private JTable sellerOrdersTable;
    private DefaultTableModel buyerOrdersModel;
    private DefaultTableModel sellerOrdersModel;
    
    /**
     * Конструктор
     * @param parentFrame Родителският прозорец
     */
    public OrderHistoryPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.orderService = new OrderService();
        this.guitarService = new GuitarService();
        this.userService = new UserService();
        this.currentUser = AuthenticationService.getInstance().getCurrentUser();
        
        setLayout(new BorderLayout());
        setBackground(Constants.PANEL_COLOR); // Бял фон
        
        initComponents();
        
        // Зареждане на поръчките
        loadOrders();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Заглавие на панела
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.PANEL_COLOR); // Бял фон
        
        JLabel titleLabel = new JLabel("История на поръчките", JLabel.LEFT);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Проверка дали има влязъл потребител
        if (!AuthenticationService.getInstance().isAuthenticated()) {
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.setBackground(Constants.PANEL_COLOR); // Бял фон
            
            JLabel notLoggedInLabel = new JLabel("Моля, влезте в системата, за да видите историята на поръчките", JLabel.CENTER);
            notLoggedInLabel.setFont(Constants.DEFAULT_FONT);
            notLoggedInLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
            messagePanel.add(notLoggedInLabel, BorderLayout.CENTER);
            
            add(messagePanel, BorderLayout.CENTER);
            return;
        }
        
        // Създаване на табове
        tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Таблица за поръчки като купувач
        String[] buyerColumns = {"№", "Китара", "Продавач", "Дата", "Цена", "Статус", "Действия"};
        buyerOrdersModel = new DefaultTableModel(buyerColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        buyerOrdersTable = new JTable(buyerOrdersModel);
        buyerOrdersTable.setRowHeight(30);
        buyerOrdersTable.getColumnModel().getColumn(0).setPreferredWidth(30);  // №
        buyerOrdersTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Китара
        buyerOrdersTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Продавач
        buyerOrdersTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Дата
        buyerOrdersTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Цена
        buyerOrdersTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Статус
        buyerOrdersTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Действия
        buyerOrdersTable.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Установяване на черен текст за заглавния ред на таблицата
        buyerOrdersTable.getTableHeader().setForeground(Constants.TEXT_COLOR);
        
        JScrollPane buyerScrollPane = new JScrollPane(buyerOrdersTable);
        
        // Таблица за поръчки като продавач
        String[] sellerColumns = {"№", "Китара", "Купувач", "Дата", "Цена", "Статус", "Действия"};
        sellerOrdersModel = new DefaultTableModel(sellerColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        sellerOrdersTable = new JTable(sellerOrdersModel);
        sellerOrdersTable.setRowHeight(30);
        sellerOrdersTable.getColumnModel().getColumn(0).setPreferredWidth(30);  // №
        sellerOrdersTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Китара
        sellerOrdersTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Купувач
        sellerOrdersTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Дата
        sellerOrdersTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Цена
        sellerOrdersTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Статус
        sellerOrdersTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Действия
        sellerOrdersTable.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Установяване на черен текст за заглавния ред на таблицата
        sellerOrdersTable.getTableHeader().setForeground(Constants.TEXT_COLOR);
        
        JScrollPane sellerScrollPane = new JScrollPane(sellerOrdersTable);
        
        // Добавяне на табове
        tabbedPane.addTab("Моите покупки", buyerScrollPane);
        tabbedPane.addTab("Моите продажби", sellerScrollPane);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Добавяне на действия към таблиците
        addTableActions();
    }
    
    /**
     * Добавя действия към таблиците
     */
    private void addTableActions() {
        // Добавяне на действие при клик върху бутон в таблицата за поръчки като купувач
        buyerOrdersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = buyerOrdersTable.rowAtPoint(evt.getPoint());
                int col = buyerOrdersTable.columnAtPoint(evt.getPoint());
                
                // Проверка дали е кликнато в колоната с действия
                if (col == 6 && row >= 0) {
                    int orderId = Integer.parseInt(buyerOrdersTable.getValueAt(row, 0).toString());
                    String status = buyerOrdersTable.getValueAt(row, 5).toString();
                    
                    if (status.equals("В процес")) {
                        // Показване на меню с опции
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem cancelItem = new JMenuItem("Откажи поръчката");
                        cancelItem.setForeground(Constants.TEXT_COLOR); // Черен текст
                        cancelItem.addActionListener(e -> cancelOrder(orderId));
                        menu.add(cancelItem);
                        
                        menu.show(buyerOrdersTable, evt.getX(), evt.getY());
                    }
                }
            }
        });
        
        // Добавяне на действие при клик върху бутон в таблицата за поръчки като продавач
        sellerOrdersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = sellerOrdersTable.rowAtPoint(evt.getPoint());
                int col = sellerOrdersTable.columnAtPoint(evt.getPoint());
                
                // Проверка дали е кликнато в колоната с действия
                if (col == 6 && row >= 0) {
                    int orderId = Integer.parseInt(sellerOrdersTable.getValueAt(row, 0).toString());
                    String status = sellerOrdersTable.getValueAt(row, 5).toString();
                    
                    if (status.equals("В процес")) {
                        // Показване на меню с опции
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem confirmItem = new JMenuItem("Потвърди поръчката");
                        confirmItem.setForeground(Constants.TEXT_COLOR); // Черен текст
                        confirmItem.addActionListener(e -> confirmOrder(orderId));
                        menu.add(confirmItem);
                        
                        JMenuItem cancelItem = new JMenuItem("Откажи поръчката");
                        cancelItem.setForeground(Constants.TEXT_COLOR); // Черен текст
                        cancelItem.addActionListener(e -> cancelOrder(orderId));
                        menu.add(cancelItem);
                        
                        menu.show(sellerOrdersTable, evt.getX(), evt.getY());
                    }
                }
            }
        });
    }
    
    /**
     * Зарежда поръчките от базата данни
     */
    private void loadOrders() {
        if (!AuthenticationService.getInstance().isAuthenticated()) {
            return;
        }
        
        // Изчистване на моделите
        buyerOrdersModel.setRowCount(0);
        sellerOrdersModel.setRowCount(0);
        
        // Зареждане на поръчки като купувач
        List<Order> buyerOrders = orderService.getOrdersByBuyer(currentUser.getUserId());
        for (Order order : buyerOrders) {
            Guitar guitar = guitarService.getGuitarById(order.getGuitarId());
            User seller = userService.getUserById(order.getSellerId());
            
            if (guitar != null && seller != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                
                Object[] row = {
                    order.getOrderId(),
                    guitar.getTitle(),
                    seller.getUsername(),
                    dateFormat.format(order.getOrderDate()),
                    order.getPrice() + " лв.",
                    getOrderStatusText(order.getStatus()),
                    getActionButtonText(order.getStatus())
                };
                
                buyerOrdersModel.addRow(row);
            }
        }
        
        // Зареждане на поръчки като продавач
        List<Order> sellerOrders = orderService.getOrdersBySeller(currentUser.getUserId());
        for (Order order : sellerOrders) {
            Guitar guitar = guitarService.getGuitarById(order.getGuitarId());
            User buyer = userService.getUserById(order.getBuyerId());
            
            if (guitar != null && buyer != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                
                Object[] row = {
                    order.getOrderId(),
                    guitar.getTitle(),
                    buyer.getUsername(),
                    dateFormat.format(order.getOrderDate()),
                    order.getPrice() + " лв.",
                    getOrderStatusText(order.getStatus()),
                    getActionButtonText(order.getStatus())
                };
                
                sellerOrdersModel.addRow(row);
            }
        }
    }
    
    /**
     * Преобразува статуса на поръчка в текст
     * @param status Статусът на поръчката
     * @return Текстово представяне
     */
    private String getOrderStatusText(Order.OrderStatus status) {
        switch (status) {
            case COMPLETED: return "Завършена";
            case PROCESSING: return "В процес";
            case CANCELLED: return "Отказана";
            default: return "Неизвестен";
        }
    }
    
    /**
     * Връща текста за бутона за действие според статуса
     * @param status Статусът на поръчката
     * @return Текст за бутона
     */
    private String getActionButtonText(Order.OrderStatus status) {
        switch (status) {
            case PROCESSING: return "Действия";
            case COMPLETED: return "";
            case CANCELLED: return "";
            default: return "";
        }
    }
    
    /**
     * Потвърждаване на поръчка
     * @param orderId ID на поръчката
     */
    private void confirmOrder(int orderId) {
        try {
            boolean success = orderService.confirmOrder(orderId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Поръчката е потвърдена успешно.",
                    "Успешно потвърждаване",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Презареждане на поръчките
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Грешка при потвърждаване на поръчката.",
                    "Грешка",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Грешка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Отказване на поръчка
     * @param orderId ID на поръчката
     */
    private void cancelOrder(int orderId) {
        try {
            boolean success = orderService.cancelOrder(orderId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Поръчката е отказана успешно.",
                    "Успешно отказване",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Презареждане на поръчките
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Грешка при отказване на поръчката.",
                    "Грешка",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Грешка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}