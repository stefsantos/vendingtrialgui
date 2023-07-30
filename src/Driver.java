import javax.swing.*;

public class Driver {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);

            mainMenu.promptNumRows(); // Prompt the user for the number of items
            
        });
    }
}
