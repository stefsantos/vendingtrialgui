class VendingMachineItem {
    private String name;
    private int stock;
    private int maxStock;
    private int calories;

    public VendingMachineItem(String name, int maxStock, int calories) {
        this.name = name;
        this.maxStock = maxStock;
        this.stock = maxStock;
        this.calories = calories;
    }

    // Getters and setters...

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public int getCalories() {
        return calories;
    }
}