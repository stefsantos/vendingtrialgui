class MoneySlot {
    private double balance;

    public MoneySlot() {
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public void insertMoney(double amount) {
        balance += amount;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
