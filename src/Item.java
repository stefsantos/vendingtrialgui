class Item {
    private String name;
    private int stock;
    private int maxStock;
    private int calories;
    private double price;

    public Item(String name, int maxStock, int calories, double price) {
        this.name = name;
        this.maxStock = maxStock;
        this.stock = maxStock;
        this.calories = calories;
        this.price = price;
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

    public double getPrice() {
        return price;
    }
}
