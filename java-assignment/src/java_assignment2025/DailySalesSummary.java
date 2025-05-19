/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author Isaac
 */
public class DailySalesSummary {
    private String itemId;
    private String itemName;
    private int quantity;
    private double totalAmount;
    private String date;

    public DailySalesSummary(String itemId, String itemName, int quantity, double totalAmount, String date) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.date = date;
    }

    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return itemId + " | " + itemName + " | Qty: " + quantity + " | RM " + totalAmount;
    }
}
