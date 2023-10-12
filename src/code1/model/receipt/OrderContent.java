package code1.model.receipt;

public class OrderContent {
    private String productId;
    private String productName;
    private double unitPrice;
    private double quantity;
    private double totalCost;

    public OrderContent(String productId, String productName, double unitPrice, double quantity, double totalCost) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }
}
