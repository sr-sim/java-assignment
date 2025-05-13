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
    private final String pofilepath = "C:\\Users\\Isaac\\OneDrive - Asia Pacific University\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\PurchaseOrder.txt";
    
    public PurchaseOrderManager() {
        this.polist = new ArrayList<>();
        this.textfile = new TextFile();
        loadAllpofromtxtfile();
    }
    
    public void loadAllpofromtxtfile() {
        polist.clear();
         
        List<String> lines = textfile.readFile(pofilepath);
        for (String line : lines) {
            String[] parts = line.split(",", 11);
            if (parts.length == 11) {
                List<String> itemids = Arrays.asList(parts[3].trim().split("\\|"));
                List<String> quantities = Arrays.asList(parts[4].trim().split("\\|"));
                List<String> supplierids = Arrays.asList(parts[6].trim().split("\\|"));
                polist.add(new PurchaseOrder(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        itemids, // new ArrayList<>(itemids)
                        quantities,
                        Double.parseDouble(parts[5].trim()),  
                        supplierids,
                        parts[7].trim(),
                        parts[8].trim(),
                        parts[9].trim(),
                        parts[10].trim())
                       
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
    String supplierFilePath = "C:\\Users\\Isaac\\OneDrive - Asia Pacific University\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\supplier.txt";
    List<String> lines = TextFile.readFile(supplierFilePath);
    
    for (String line : lines) {
        String[] parts = line.split(",");
        if (parts.length >= 2 && parts[0].trim().equalsIgnoreCase(supplierId.trim())) {
            return parts[1].trim(); // Return Supplier Name
        }
    }
    return "Unknown Supplier";
    }

    public void deletepo(String orderId){
        PurchaseOrder po = findpoid(orderId);
        if (po != null) {
        String lineToDelete = po.toString(); // This must exactly match the line in the file
        System.out.println("Trying to delete line:\n[" + lineToDelete + "]");

        List<String> allLines = textfile.readFile(pofilepath);
        for (String line : allLines) {
            System.out.println("Line in file:\n[" + line + "]");
            if (line.equals(lineToDelete)) {
                System.out.println(" Match found.");
            }
        }
        polist.remove(po);
        textfile.deleteLine(pofilepath, lineToDelete);
        System.out.println("Delete successful");
            } else {
                System.out.println("Purchase order not found");
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

    

}
