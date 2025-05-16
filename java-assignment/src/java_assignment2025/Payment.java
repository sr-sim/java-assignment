package java_assignment2025;

public class Payment {
    private String paymentId;
    private String poId;
    private String itemIds;
    private String unitPrices;
    private String quantities;
    private String amount;

    // Constructor
    public Payment(String paymentId, String poId, String itemIds, String unitPrices, String quantities, String amount) {
        this.paymentId = paymentId;
        this.poId = poId;
        this.itemIds = itemIds;
        this.unitPrices = unitPrices;
        this.quantities = quantities;
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPoId() {
        return poId;
    }

    public String getItemIds() {
        return itemIds;
    }

    public String getUnitPrices() {
        return unitPrices;
    }

    public String getQuantities() {
        return quantities;
    }

    public String getAmount() {
        return amount;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public void setItemIds(String itemIds) {
        this.itemIds = itemIds;
    }

    public void setUnitPrices(String unitPrices) {
        this.unitPrices = unitPrices;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
