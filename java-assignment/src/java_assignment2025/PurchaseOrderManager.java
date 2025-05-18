/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Macy Khoo
 */
public class PurchaseOrderManager {
    private final List<PurchaseOrder>polist;
    private final TextFile textfile;
    private final String pofilepath = "C:\\JPL9\\java-assignment\\java-assignment\\src\\java_assignment2025\\PurchaseOrder.txt";
    
    public PurchaseOrderManager() {
        this.polist = new ArrayList<>();
        this.textfile = new TextFile();
        this.inventorydatamanager = new InventoryDataManager();
        loadAllpofromtxtfile();
    }
    
    public void loadAllpofromtxtfile() {
        polist.clear();
         
        List<String> lines = textfile.readFile(pofilepath);
        for (String line : lines) {
            String[] parts = line.split(",", 13);
            if (parts.length == 13) {
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
                        parts[12].trim())
                       
                );
            }
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

//    public void deletepo(String orderId) {
//        PurchaseOrder po = findpoid(orderId);
//        if (po != null) {
//            String poString = po.toString().trim();
//            List<String> allLines = textfile.readFile(pofilepath);
//            String lineToDelete = null;
//
//            for (String line : allLines) {
//                if (line.trim().equals(poString)) {
//                    lineToDelete = line; // Use exact line from file
//                    break;
//                }
//            }
//
//            if (lineToDelete != null) {
//                System.out.println("Deleting: [" + lineToDelete + "]");
//                System.out.println("po:" +po);
//                polist.remove(po);
//                
//                textfile.deleteLine(pofilepath, lineToDelete);
//                System.out.println("Delete successful");
//            } else {
//                System.out.println("Line not found in file.");
//            }
//
//        } else {
//            System.out.println("Purchase order not found");
//        }
//}
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
}
