import javax.swing.*;

public class VendingMachineApp {
    public static void main(String[] args) {
        int numRows = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of rows:"));

        SwingUtilities.invokeLater(() -> new VendingMachineGUI(numRows));
    }
}