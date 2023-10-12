package code1.model.receipt;

public class OrderContentBuilder {
    private String productId;
    private String productName;
    private double unitPrice;
    private double quantity;
    private double totalCost;

    public OrderContentBuilder setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public OrderContentBuilder setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public OrderContentBuilder setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public OrderContentBuilder setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderContentBuilder setTotalCost(double totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public OrderContent createOrderContent() {
        return new OrderContent(productId, productName, unitPrice, quantity, totalCost);
    }
}