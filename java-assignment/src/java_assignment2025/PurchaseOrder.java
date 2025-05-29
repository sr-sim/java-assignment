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
    private String poCreator;
    private String requestId;
    private String userId;
    private List<String> itemIds;
    private List<String> unitPrices;
    private List<String> quantities;
    private double amount;
    private List<String> supplierIds;
    private String orderDate;
    private String orderStatus;
    private String verifyStatus;
    private String paymentStatus;
    private String receiveStatus;
    private String postatuschangeby;
   
    
    public PurchaseOrder(){}
    // Constructor
    public PurchaseOrder(String orderId, String poCreator, String requestId, String userId,List<String> itemIds ,
                         List<String> unitPrices, List<String> quantities, double amount,List<String>  supplierIds,
                         String orderDate, String orderStatus, String verifyStatus, String paymentStatus,String postatuschangeby) {
                       
        this.orderId = orderId;
        this.poCreator = poCreator;
        this.requestId = requestId;
        this.userId = userId;
        this.itemIds = itemIds;
        this.unitPrices=unitPrices;
        this.quantities = quantities;
        this.amount = amount;
        this.supplierIds = supplierIds;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.verifyStatus = verifyStatus;
        this.paymentStatus = paymentStatus;
        this.postatuschangeby = postatuschangeby;
        
    }
    

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public String getPoCreator(){
        return poCreator;
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

    public List<String> getUnitPrices() {
        return unitPrices;
    }

    
    public void setUnitPrices(List<String> unitPrices) {
        this.unitPrices = unitPrices;
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

    public String getVerifyStatus() {
        return verifyStatus;
    }
    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getReceiveStatus() {
        return receiveStatus;
    }
    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    
   

    public static String getCurrentDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yy-MM-dd");
        return today.format(formatter);
    }
    
    
    public static String getNextOrderId() {

        String filePath = "src/java_assignment2025/PurchaseOrder.txt";
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
        return orderId + "," + poCreator + ","+ requestId + "," + userId + "," +
               String.join("|", itemIds) + "," +
               String.join("|",unitPrices)+","+
               String.join("|", quantities) + "," +
               String.format("%.2f", amount) + "," +
               String.join("|", supplierIds) + "," +
               orderDate + "," + orderStatus + "," + verifyStatus+","+ paymentStatus
                + "," +postatuschangeby;
                
}

    public String getPostatuschangeby() {
        return postatuschangeby;
    }

    public void setPostatuschangeby(String postatuschangeby) {
        this.postatuschangeby = postatuschangeby;
    }

   
    
}
