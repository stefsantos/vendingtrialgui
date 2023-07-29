import javax.swing.*;

public class VendingMachineApp {
    public static void main(String[] args) {
        int numRows = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of items:"));

        while (numRows < 8) {
            JOptionPane.showMessageDialog(null, "Number of items must be at least 8.", "Error", JOptionPane.ERROR_MESSAGE);
            numRows = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of items:"));
        }

        final int finalNumRows = numRows; // Declare numRows as final

        SwingUtilities.invokeLater(() -> new VendingMachineGUI(finalNumRows));
    }
}
