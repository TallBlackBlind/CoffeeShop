package CoffeeShop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.net.URL;


public class CoffeeShopGUI extends JFrame {
    private CoffeeShopFacade coffeeShopFacade;
    private DefaultListModel<String> orderListModel;

    private JTextField customerNameField;
    private JComboBox<String> coffeeTypeComboBox;
    private JTextField milkTypeField;
    private JTextField sugarTypeField;
    private JCheckBox addSyrupCheckBox;
    private JComboBox<String> syrupTypeComboBox;
    private JCheckBox addCinnamonCheckBox;
    private JLabel totalPriceLabel;

    private JButton createOrderButton;
    private JButton checkOrderButton;
    private JLabel orderDetailsLabel;
    private JLabel coffeeShopImageLabel;
    public CoffeeShopGUI() {
        coffeeShopFacade = new CoffeeShopFacade();
        orderListModel = new DefaultListModel<>();

        setTitle("Coffee Shop Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        customerNameField = new JTextField();
        coffeeTypeComboBox = new JComboBox<>(new String[]{"Espresso", "Cappuccino"});
        milkTypeField = new JTextField();
        sugarTypeField = new JTextField();
        addSyrupCheckBox = new JCheckBox("Add Syrup");
        syrupTypeComboBox = new JComboBox<>(new String[]{"Vanilla", "Caramel"});
        syrupTypeComboBox.setEnabled(false);
        addCinnamonCheckBox = new JCheckBox("Add Cinnamon");
        totalPriceLabel = new JLabel("Total Price: $0.0");

        createOrderButton = new JButton("Create Order");

        inputPanel.add(new JLabel("Customer Name:"));
        inputPanel.add(customerNameField);
        inputPanel.add(new JLabel("Coffee Type:"));
        inputPanel.add(coffeeTypeComboBox);
        inputPanel.add(new JLabel("Milk Type:"));
        inputPanel.add(milkTypeField);
        inputPanel.add(new JLabel("Sugar Type:"));
        inputPanel.add(sugarTypeField);
        inputPanel.add(addSyrupCheckBox);
        inputPanel.add(syrupTypeComboBox);
        inputPanel.add(addCinnamonCheckBox);
        inputPanel.add(totalPriceLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(createOrderButton, BorderLayout.CENTER);

        checkOrderButton = new JButton("Check Order State");
        orderDetailsLabel = new JLabel("<html><body style='width: 200px;'>Order Details: </body></html>");
        coffeeShopImageLabel = new JLabel();

        createOrderButton.setBackground(Color.GREEN);

        add(checkOrderButton, BorderLayout.EAST);


        addSyrupCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean syrupCheckBoxSelected = addSyrupCheckBox.isSelected();
                syrupTypeComboBox.setEnabled(syrupCheckBoxSelected);
            }
        });





        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerName = customerNameField.getText();
                String coffeeType = (String) coffeeTypeComboBox.getSelectedItem();
                String milkType = milkTypeField.getText();
                String sugarType = sugarTypeField.getText();
                boolean addSyrup = addSyrupCheckBox.isSelected();
                String syrupType = (String) syrupTypeComboBox.getSelectedItem();
                boolean addCinnamon = addCinnamonCheckBox.isSelected();

                // Вызов фасада для создания заказа
                coffeeShopFacade.createOrderWithSyrup(customerName, coffeeType, milkType, sugarType, addSyrup, addCinnamon, syrupType);

                // Получить созданный заказ
                List<CoffeeOrder> orders = coffeeShopFacade.getOrders();
                CoffeeOrder order = orders.get(orders.size() - 1); // Получить последний заказ

                // Рассчитать общую стоимость заказа
                double totalPrice = order.getCoffee().getCost();
                totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));

                // Формирование текста заказа
                StringBuilder orderDetails = new StringBuilder();
                orderDetails.append("Order placed by ").append(customerName).append(" for ").append(coffeeType);

                if (addSyrup) {
                    orderDetails.append(" with Syrup (").append(syrupType).append(")");
                }

                if (addCinnamon) {
                    orderDetails.append(" with Cinnamon");
                }

                orderDetails.append(". Total Price: $").append(totalPrice);

                // Отображение сообщения о заказе
                JOptionPane.showMessageDialog(null, orderDetails.toString());

                // Process the order to change its state
                order.processOrder();

                // Обновление списка заказов и очистка полей ввода
                updateOrderList();
                clearInputFields();
            }

        });


        JList<String> orderList = new JList<>(orderListModel);
        JScrollPane orderScrollPane = new JScrollPane(orderList);

        add(orderScrollPane, BorderLayout.SOUTH);

        checkOrderButton = new JButton("Check Order State");
        add(checkOrderButton, BorderLayout.EAST);
        checkOrderButton.setBackground(Color.YELLOW);


        checkOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = orderList.getSelectedIndex();
                if (selectedIndex != -1) {
                    CoffeeOrder selectedOrder = coffeeShopFacade.getOrders().get(selectedIndex);

                    // Display order details including Order State
                    String orderDetails = "<html><body style='width: 200px;'>" + "Order State: " + selectedOrder.getState().getClass().getSimpleName() + "</body></html>";

                    JOptionPane.showMessageDialog(null, orderDetails);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an order from the list.");
                }
            }
        });
        add(coffeeShopImageLabel, BorderLayout.WEST);
        setImageFromURL("https://neurosciencenews.com/files/2023/06/coffee-brain-caffeine-neuroscincces.jpg", 200, 150);

    }

    private void setImageFromURL(String imageURL, int width, int height) {
        try {
            URL url = new URL(imageURL);
            ImageIcon originalIcon = new ImageIcon(url);

            // Resize the image
            Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            coffeeShopImageLabel.setIcon(resizedIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateOrderList() {
        List<CoffeeOrder> orders = coffeeShopFacade.getOrders();
        orderListModel.clear();
        for (CoffeeOrder order : orders) {
            orderListModel.addElement(order.getOrderDetails());
        }
    }





    private void clearInputFields() {
        customerNameField.setText("");
        milkTypeField.setText("");
        sugarTypeField.setText("");
        addSyrupCheckBox.setSelected(false);
        syrupTypeComboBox.setEnabled(false);
        addCinnamonCheckBox.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CoffeeShopGUI coffeeShopGUI = new CoffeeShopGUI();
                coffeeShopGUI.setVisible(true);
            }
        });
    }
}
