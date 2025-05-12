package java_assignment2025;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Macy Khoo
 */
public class PurchaseOrder {
    
    private String orderId;
    private String requestId;
    private String userId;
    private List<String> itemIds;
    private List<String> quantities;
    private double amount;
    private List<String> supplierIds;
    private String orderDate;
    private String orderStatus;
    private String paymentStatus;
   
    
    public PurchaseOrder(){}
    // Constructor
    public PurchaseOrder(String orderId, String requestId, String userId,List<String> itemIds ,
                         List<String> quantities, double amount,List<String>  supplierIds,
                         String orderDate, String orderStatus, String paymentStatus) {
        this.orderId = orderId;
        this.requestId = requestId;
        this.userId = userId;
        this.itemIds = itemIds;
        this.quantities = quantities;
        this.amount = amount;
        this.supplierIds = supplierIds;
        this.orderDate = getCurrentDate();
        this.orderStatus = "pending";
        this.paymentStatus = "unpaid";
    }
    

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getItemIds() {
    return itemIds;
}

    public List<String> getQuantities() {
        return quantities;
}

    public double getAmount() {
        return amount;
    }

    public List <String> getSupplierIds() {
        return supplierIds;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

   

     public static String getCurrentDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yy-MM-dd");
        return today.format(formatter);
    }
    
    
    public static String getNextOrderId() {
        String filePath = "C:\\JPL9\\java-assignment\\java-assignment\\src\\java_assignment2025\\PurchaseOrder.txt";
        List<String> lines = TextFile.readFile(filePath);
        int maxId = 0;

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith("PO")) {
                    String poId = parts[0];
                    try {
                        int idNum = Integer.parseInt(poId.substring(2)); // Extract numeric part
                        if (idNum > maxId) {
                            maxId = idNum;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid PO ID format: " + poId);
                    }
                }
            }
        }

        return String.format("PO%03d", maxId + 1);
    }

    @Override
    public String toString() {
        return orderId + "," + requestId + "," + userId + "," +
               String.join("|", itemIds) + "," +
               String.join("|", quantities) + "," +
               String.format("%.2f", amount) + "," +
               String.join("|", supplierIds) + "," +
               orderDate + "," + orderStatus + "," + paymentStatus;
}

}
