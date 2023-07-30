import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VendingMachine extends JFrame {
    private int numRows;
    private Item[] items;
    private boolean buyingMode = false;
    private JButton buyButton;

    private MoneySlot moneySlot; // Money slot for storing the inserted money
    private JLabel balanceLabel; // Label to display the amount of money in the machine

    public void start() {
        setVisible(true);
    }

    public VendingMachine(int numRows) {
        this.numRows = numRows;
        this.items = new Item[numRows];
        this.moneySlot = new MoneySlot(); // Initialize the money slot

        presetItems();
        setupGUI();
    }

    private void presetItems() {
        // Define the preset items
        Item[] presetItems = new Item[10];
        presetItems[0] = new Item("Roasted Milk Tea", 10, 150, 100.00);
        presetItems[1] = new Item("Tiramisu Milk Tea", 10, 200, 100.00);
        presetItems[2] = new Item("Lychee Milk Tea", 10, 180, 100.00);
        presetItems[3] = new Item("Strawberry Milk Tea", 10, 170, 100.00);
        presetItems[4] = new Item("Honeydew Milk Tea", 10, 160, 100.00);
        presetItems[5] = new Item("Oolong Milk Tea", 10, 110, 100.00);
        presetItems[6] = new Item("Hazelnut Milk Tea", 10, 190, 100.00);
        presetItems[7] = new Item("Coffee Milk Tea", 10, 160, 100.00);
        presetItems[8] = new Item("Choco-berry Milk Tea", 10, 210, 100.00);
        presetItems[9] = new Item("Honeydew Milk Tea", 10, 150, 100.00);

        // Populate the buttons with the preset items
        for (int i = 0; i < Math.min(numRows, presetItems.length); i++) {
            items[i] = presetItems[i];
        }
    }

    private void setupGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Vending Machine");
        setLayout(new GridLayout(numRows + 3, 2)); // +3 for balance label and money insertion buttons

        for (int i = 0; i < numRows; i++) {
            JButton setItemButton = new JButton("Set Item");
            setItemButton.addActionListener(new SetItemActionListener(i));
            add(setItemButton);

            JButton restockButton = new JButton("Restock");
            restockButton.addActionListener(new RestockActionListener(i, setItemButton));
            add(restockButton);
        }

        buyButton = new JButton("Buy");
        buyButton.addActionListener(new BuyActionListener());
        add(buyButton);

        // Create the money insertion buttons
        JButton insertPesoButton = new JButton("Insert P1");
        insertPesoButton.addActionListener(new InsertMoneyActionListener(1.0));
        JButton insertFivePesosButton = new JButton("Insert P5");
        insertFivePesosButton.addActionListener(new InsertMoneyActionListener(5.0));
        JButton insertTenPesosButton = new JButton("Insert P10");
        insertTenPesosButton.addActionListener(new InsertMoneyActionListener(10.0));
        JButton insertTwentyPesosButton = new JButton("Insert P20");
        insertTwentyPesosButton.addActionListener(new InsertMoneyActionListener(20.0));
        JButton insertFiftyPesosButton = new JButton("Insert P50");
        insertFiftyPesosButton.addActionListener(new InsertMoneyActionListener(50.0));
        JButton insertHundredPesosButton = new JButton("Insert P100");
        insertHundredPesosButton.addActionListener(new InsertMoneyActionListener(100.0));
        JButton insertTwoHundredPesosButton = new JButton("Insert P200");
        insertTwoHundredPesosButton.addActionListener(new InsertMoneyActionListener(200.0));
        JButton insertFiveHundredPesosButton = new JButton("Insert P500");
        insertFiveHundredPesosButton.addActionListener(new InsertMoneyActionListener(500.0));
        JButton insertThousandPesosButton = new JButton("Insert P1000");
        insertThousandPesosButton.addActionListener(new InsertMoneyActionListener(1000.0));

        // Add the money insertion buttons to the GUI
        JPanel moneyPanel = new JPanel();
        moneyPanel.add(insertPesoButton);
        moneyPanel.add(insertFivePesosButton);
        moneyPanel.add(insertTenPesosButton);
        moneyPanel.add(insertTwentyPesosButton);
        moneyPanel.add(insertFiftyPesosButton);
        moneyPanel.add(insertHundredPesosButton);
        moneyPanel.add(insertTwoHundredPesosButton);
        moneyPanel.add(insertFiveHundredPesosButton);
        moneyPanel.add(insertThousandPesosButton);
        add(moneyPanel);

        JButton produceChangeButton = new JButton("Produce Change");
        produceChangeButton.addActionListener(new ProduceChangeActionListener());
        add(produceChangeButton);

        // Add the balance label to the GUI
        balanceLabel = new JLabel("Balance: P0.00");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        // Update the button labels with the preset items
        for (int i = 0; i < numRows; i++) {
            updateButtonLabel((JButton) getContentPane().getComponent(i * 2), items[i]);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateButtonLabel(JButton button, Item item) {
        if (item != null) {
            String deci2 = String.format("%.2f", item.getPrice());
            button.setText(item.getName() + "\n Stock: " + item.getStock() + "/" + item.getMaxStock() + "\n Calories: " + item.getCalories() + "\n Price: P" + deci2);
        }
    }

    private void updateButtons() {
        Component[] components = getContentPane().getComponents();
        for (int i = 0; i < numRows; i++) {
            JButton setItemButton = (JButton) components[i * 2];
            JButton restockButton = (JButton) components[i * 2 + 1];

            if (buyingMode) {
                setItemButton.setEnabled(false);
                restockButton.setText("Buy");
                restockButton.removeActionListener(restockButton.getActionListeners()[0]);
                restockButton.addActionListener(new BuyItemActionListener(i));
            } else {
                setItemButton.setEnabled(true);
                restockButton.setText("Restock");
                restockButton.removeActionListener(restockButton.getActionListeners()[0]);
                restockButton.addActionListener(new RestockActionListener(i, setItemButton));
            }
        }

        // Change the label of the Buy button based on the current mode
        if (buyingMode) {
            buyButton.setText("Maintenance");
        } else {
            buyButton.setText("Buy");
        }
    }

    private void updateBalanceLabel() {
        String balanceText = String.format("Balance: P%.2f", moneySlot.getBalance());
        balanceLabel.setText(balanceText);
    }

    private class SetItemActionListener implements ActionListener {
        private int row;

        public SetItemActionListener(int row) {
            this.row = row;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("Enter item name:");
            int maxStock = Integer.parseInt(JOptionPane.showInputDialog("Enter maximum stock:"));

            while (maxStock < 10) {
                JOptionPane.showMessageDialog(null, "Maximum stock must be at least 10.", "Error", JOptionPane.ERROR_MESSAGE);
                maxStock = Integer.parseInt(JOptionPane.showInputDialog("Enter maximum stock:"));
            }

            final int finalmaxStock = maxStock;

            int calories = Integer.parseInt(JOptionPane.showInputDialog("Enter calories:"));
            double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));

            Item item = new Item(name, finalmaxStock, calories, price);
            item.setStock(0); // Initialize stock to 0 after creating a new Item
            items[row] = item;

            updateButtonLabel((JButton) e.getSource(), item);
        }

        private void updateButtonLabel(JButton button, Item item) {
            if (item != null) {
                String deci2 = String.format("%.2f", item.getPrice());
                button.setText(item.getName() + "\n Stock: " + item.getStock() + "/" + item.getMaxStock() + "\n Calories: " + item.getCalories() + "\n Price: P" + deci2);
            }
        }
    }

    private class ProduceChangeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            produceChange();
        }
    }
    
    private void produceChange() {
        double balance = moneySlot.getBalance();
    
        // Define the denominations in descending order (larger denominations first)
        double[] denominations = { 1000.0, 500.0, 200.0, 100.0, 50.0, 20.0, 10.0, 5.0, 1.0 };
    
        StringBuilder changeInfo = new StringBuilder("Dispensing change:\n");
    
        for (double denomination : denominations) {
            int numBills = (int) (balance / denomination);
            if (numBills > 0) {
                balance -= numBills * denomination;
                changeInfo.append("P").append((int) denomination).append(" bill(s): ").append(numBills).append("\n");
            }
        }
    
        if (balance > 0.0) {
            // If there's still a remaining balance that couldn't be dispensed in denominations
            changeInfo.append("Remaining balance: P").append(String.format("%.2f", balance)).append("\n");
        }
    
        JOptionPane.showMessageDialog(null, changeInfo.toString(), "Change Dispensed", JOptionPane.INFORMATION_MESSAGE);
    
        // Reset the balance to zero after dispensing change
        moneySlot.setBalance(0.0);
        updateBalanceLabel();
    }
    

    private class RestockActionListener implements ActionListener {
        private int row;
        private JButton setItemButton;

        public RestockActionListener(int row, JButton setItemButton) {
            this.row = row;
            this.setItemButton = setItemButton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Item item = items[row];
            if (item != null) {
                String restockInput = JOptionPane.showInputDialog("Enter the quantity to restock for " + item.getName() + ":");
                try {
                    int restockAmount = Integer.parseInt(restockInput);
                    int maxStock = item.getMaxStock();

                    if (restockAmount < 0) {
                        JOptionPane.showMessageDialog(null, "Restock quantity must be a non-negative number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (item.getStock() + restockAmount > maxStock) {
                        JOptionPane.showMessageDialog(null, "Cannot restock more than the maximum stock.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    item.setStock(item.getStock() + restockAmount);
                    updateButtonLabel(setItemButton, item); // Update the "Set Item" button label
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void updateButtonLabel(JButton button, Item item) {
            if (item != null) {
                button.setText(item.getName() + "\nStock: " + item.getStock() + "/" + item.getMaxStock());
            }
        }
    }

    private class BuyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buyingMode = !buyingMode;
            updateButtons();
        }
    }

    private class BuyItemActionListener implements ActionListener {
        private int row;
    
        public BuyItemActionListener(int row) {
            this.row = row;
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            Item item = items[row];
            if (item != null) {
                if (item.getStock() > 0) {
                    double itemPrice = item.getPrice();
                    if (moneySlot.getBalance() >= itemPrice) {
                        item.setStock(item.getStock() - 1);
                        updateButtonLabel(row);
                        moneySlot.insertMoney(-itemPrice); // Deduct the item price for the purchase
                        updateBalanceLabel(); // Update the balance label
                        JOptionPane.showMessageDialog(null, "You bought one " + item.getName() + ". Enjoy!", "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient balance. Please insert enough money.", "Insufficient Balance", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Item out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    
        private void updateButtonLabel(int row) {
            JButton button = (JButton) getContentPane().getComponent(row * 2);
            Item item = items[row];
            if (item != null) {
                button.setText(item.getName() + "\nStock: " + item.getStock() + "/" + item.getMaxStock());
            }
        }
    }
    

    private class InsertMoneyActionListener implements ActionListener {
        private double amount;

        public InsertMoneyActionListener(double amount) {
            this.amount = amount;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            moneySlot.insertMoney(amount);
            updateBalanceLabel(); // Update the balance label
            JOptionPane.showMessageDialog(null, "P" + amount + " inserted successfully.", "Money Inserted", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
