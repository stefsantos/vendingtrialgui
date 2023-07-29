import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class VendingMachineGUI extends JFrame {
    private int numRows;
    private VendingMachineItem[] items;
    private boolean buyingMode = false;
    private JButton buyButton;

    public VendingMachineGUI(int numRows) {
        this.numRows = numRows;
        this.items = new VendingMachineItem[numRows];

        presetItems();
        setupGUI();
    }

    private void presetItems() {
        // Define the preset items
        VendingMachineItem[] presetItems = new VendingMachineItem[10];
        presetItems[0] = new VendingMachineItem("Roasted Milk Tea", 10, 150);
        presetItems[1] = new VendingMachineItem("Tiramisu Milk Tea", 10, 200);
        presetItems[2] = new VendingMachineItem("Lychee Milk Tea", 10, 180);
        presetItems[3] = new VendingMachineItem("Strawberry Milk Tea", 10, 170);
        presetItems[4] = new VendingMachineItem("Honeydew Milk Tea", 10, 160);
        presetItems[5] = new VendingMachineItem("Oolong Milk Tea", 10, 110);
        presetItems[6] = new VendingMachineItem("Hazelnut Milk Tea", 10, 190);
        presetItems[7] = new VendingMachineItem("Coffee Milk Tea", 10, 160);
        presetItems[8] = new VendingMachineItem("Choco-berry Milk Tea", 10, 210);
        presetItems[9] = new VendingMachineItem("Honeydew Milk Tea", 10, 150);

        // Populate the buttons with the preset items
        for (int i = 0; i < Math.min(numRows, presetItems.length); i++) {
            items[i] = presetItems[i];
        }
    }

    private void setupGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Vending Machine");
        setLayout(new GridLayout(numRows + 1, 2));

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

        // Update the button labels with the preset items
        for (int i = 0; i < numRows; i++) {
            updateButtonLabel((JButton) getContentPane().getComponent(i * 2), items[i]);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateButtonLabel(JButton button, VendingMachineItem item) {
        if (item != null) {
            button.setText(item.getName() + "\n Stock: " + item.getStock() + "/" + item.getMaxStock() + "\n Calories: " + item.getCalories());
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

            VendingMachineItem item = new VendingMachineItem(name, finalmaxStock, calories);
            item.setStock(0); // Initialize stock to 0 after creating a new VendingMachineItem
            items[row] = item;

            updateButtonLabel((JButton) e.getSource(), item);
        }

        private void updateButtonLabel(JButton button, VendingMachineItem item) {
            if (item != null) {
                button.setText(item.getName() + "\n Stock: " + item.getStock() + "/" + item.getMaxStock() + "\n Calories: " + item.getCalories());
            }
        }
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
            VendingMachineItem item = items[row];
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

        private void updateButtonLabel(JButton button, VendingMachineItem item) {
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
            VendingMachineItem item = items[row];
            if (item != null) {
                if (item.getStock() > 0) {
                    item.setStock(item.getStock() - 1);
                    updateButtonLabel(row);
                    JOptionPane.showMessageDialog(null, "You bought one " + item.getName() + ". Enjoy!", "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Item out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        private void updateButtonLabel(int row) {
            JButton button = (JButton) getContentPane().getComponent(row * 2);
            VendingMachineItem item = items[row];
            if (item != null) {
                button.setText(item.getName() + "\nStock: " + item.getStock() + "/" + item.getMaxStock());
            }
        }
    }
}
