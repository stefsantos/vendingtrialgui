import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    private int[] numRows = { 8 }; // Default value of 8 in an array

    public MainMenu() {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 3)); // Changed to 1 row and 3 columns to accommodate the new button

        JButton regularVendingButton = new JButton("Regular Vending Machine");
        regularVendingButton.addActionListener(new RegularVendingActionListener());
        add(regularVendingButton);

        // Add the "Special Vending Machine" button
        JButton specialVendingButton = new JButton("Special Vending Machine");
        specialVendingButton.addActionListener(new SpecialVendingActionListener());
        add(specialVendingButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(cancelButton);

        pack();
        setLocationRelativeTo(null);
    }

    public int getNumRows() {
        return numRows[0]; // Access the value from the array
    }

    public void addSpecialVendingButton() {
        JButton specialVendingButton = new JButton("Special Vending Machine");
        specialVendingButton.addActionListener(new SpecialVendingActionListener());
        add(specialVendingButton);
        revalidate(); // Refresh the layout to display the new button
    }

    private class RegularVendingActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            VendingMachine vendingMachineInterface = new VendingMachine(numRows[0]); // Pass the value from the array
            vendingMachineInterface.start();
        }
    }

    private class SpecialVendingActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            SpecialVendingMachine specialVendingMachine = new SpecialVendingMachine(numRows[0]); // Pass the value from the array
            specialVendingMachine.start();
        }
    }

    // Method to handle user input for the number of items
    public void promptNumRows() {
        String input = JOptionPane.showInputDialog("Enter the number of items:");
        try {
            int value = Integer.parseInt(input);
            if (value >= 8) {
                numRows[0] = value; // Update the value in the array
            } else {
                JOptionPane.showMessageDialog(null, "Number of items must be at least 8.", "Error", JOptionPane.ERROR_MESSAGE);
                promptNumRows(); // Prompt again if the input is less than 8
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            promptNumRows(); // Prompt again if the input is not a valid number
        }
    }

}
