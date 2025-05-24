/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.util.*;
import java.nio.file.*;

/**
 *
 * @author Macy Khoo
 */
public class PurchaseOrderManager extends DataManager {
    private final List<PurchaseOrder>polist;
    private final TextFile textfile;
//    private final String pofilepath = "C:\\Users\\hew\\OneDrive - Asia Pacific University\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\PurchaseOrder.txt";
    private final String pofilepath= "src/java_assignment2025/PurchaseOrder.txt";
    
    
    public PurchaseOrderManager() {
        this.polist = new ArrayList<>();
        this.textfile = new TextFile();
        loadAllpofromtxtfile();
    }
      
      
    public void loadAllpofromtxtfile() {
        polist.clear();
        
            List<String> lines = textfile.readFile(pofilepath);
           
            for (String line : lines) {
                
                String[] parts = line.split(",", 14);
                if (parts.length == 14) {
                    try {
                            List<String> itemids = Arrays.asList(parts[4].trim().split("\\|"));
                            List<String> unitPrices = Arrays.asList(parts[5].trim().split("\\|"));
                            List<String> quantities = Arrays.asList(parts[6].trim().split("\\|"));
                            List<String> supplierids = Arrays.asList(parts[8].trim().split("\\|"));
                            polist.add(new PurchaseOrder(
                                    parts[0].trim(),
                                    parts[1].trim(),
                                    parts[2].trim(),
                                    parts[3].trim(),
                                    itemids, // new ArrayList<>(itemids)
                                    unitPrices,
                                    quantities,
                                    Double.parseDouble(parts[7].trim()),  
                                    supplierids,
                                    parts[9].trim(),
                                    parts[10].trim(),
                                    parts[11].trim(),
                                    parts[12].trim(),
                                    parts[13].trim()
                                    
                            ));
                            System.out.println("Added PO: " + parts[0].trim());
                        }catch (NumberFormatException e) {
                        System.err.println("Error parsing amount in line: " + line + " - " + e.getMessage());
                    }
                }else{
                    System.err.println("Invalid line format: " + line);
                }
            
                
        
            }
}
    
     public void updatePurchaseOrderInFile(PurchaseOrder po) {
        List<String> lines = textfile.readFile(pofilepath);
        List<String> updatedLines = new ArrayList<>();
        boolean updated = false;//new
        try{
            for (String line : lines) {
                if (line.startsWith(po.getOrderId() + ",")) {
                    updatedLines.add(po.toString());
                    updated = true; // Set flag when updated
                } else {
                    updatedLines.add(line);
                }
            }
            if (!updated) {
                    updatedLines.add(po.toString());
            }
            textfile.rewriteFile(pofilepath, updatedLines);
        } catch (Exception e) {
            System.err.println("Error updating PurchaseOrder.txt: " + e.getMessage()); //this function macy dh
        }
    }
    public String getpofilepath(){
        return pofilepath;
    }
    public List<PurchaseOrder> getpolist(){
        return polist;
    }
    
    public static String findSupplierNameById(String supplierId) {
    String supplierFilePath = "src/java_assignment2025/supplier.txt";
    List<String> lines = TextFile.readFile(supplierFilePath);
    
    for (String line : lines) {
        String[] parts = line.split(",");
        if (parts.length >= 2 && parts[0].trim().equalsIgnoreCase(supplierId.trim())) {
            return parts[1].trim(); // Return Supplier Name
        }
    }
    return "Unknown Supplier";
    }


    public void deletepo(String poid){
        PurchaseOrder po = findpoid(poid);
            if (po != null){
                System.out.println("here"+ po);
                polist.remove(po);
                textfile.deleteLine(pofilepath, po.toString());
                System.out.println("delete successful");
                return;
            }else{
                System.out.println("pr not found");
            } 
    } 

    public PurchaseOrder findpoid(String orderId) {
    for (PurchaseOrder po : polist) {
        if (po.getOrderId().equals(orderId)) {
            return po;
            }
        }
        return null;
        }
    
    public void updatePOStatus(String orderId, String newStatus) {
    PurchaseOrder po = findpoid(orderId);
    if (po != null) {
        String oldLine = po.toString();
        po.setOrderStatus(newStatus);
        String newLine = po.toString();
        textfile.replaceLineByPOId(pofilepath, po.getOrderId(), newLine);
        System.out.println("Status updated to " + newStatus + " for PO: " + orderId);
    } else {
        System.out.println("PO not found for ID: " + orderId);
    }
}

     public static String getCurrentDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }
     
     public void updateReceiveStatus(String orderId, String status) {
        PurchaseOrder po = findpoid(orderId);
        if (po != null) {
            po.setVerifyStatus(status);
           

            updatePurchaseOrderInFile(po);
        } else {
            System.out.println("Failed to update ReceiveStatus: PO " + orderId + " not found");
        }
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
        
        
    
    
    
    
     
     

}
