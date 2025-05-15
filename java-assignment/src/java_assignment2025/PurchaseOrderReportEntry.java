/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author Isaac
 */

public class PurchaseOrderReportEntry {
    private String poid;
    private String itemIds;
    private String itemNames;
    private String quantities;
    private String orderDate;
    private String amount;

    public PurchaseOrderReportEntry(String poid, String itemIds, String itemNames,
                                    String quantities, String orderDate, String amount) {
        this.poid = poid;
        this.itemIds = itemIds;
        this.itemNames = itemNames;
        this.quantities = quantities;
        this.orderDate = orderDate;
        this.amount = amount;
    }

    public String getPoid() { return poid; }
    public String getItemIds() { return itemIds; }
    public String getItemNames() { return itemNames; }
    public String getQuantities() { return quantities; }
    public String getOrderDate() { return orderDate; }
    public String getAmount() { return amount; }
}

