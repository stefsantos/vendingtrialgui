public class SpecialVendingMachine extends VendingMachine {
    public SpecialVendingMachine(int numRows) {
        super(numRows); // Call the constructor of the parent class (VendingMachineInterface)
    }

    // Add any additional methods or override existing methods to provide new functionality

    @Override
    public void start() {
        // You can override the start() method to provide specialized behavior
        // For example, display a welcome message or perform some unique actions
        System.out.println("Welcome to the Special Vending Machine!");
        super.start(); // Call the start() method of the parent class to display the interface
    }

    // Add more methods and functionality specific to the special vending machine
}
