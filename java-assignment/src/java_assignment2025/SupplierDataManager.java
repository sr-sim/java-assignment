package java_assignment2025;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java_assignment2025.Item;
import java_assignment2025.Supplier;
import java_assignment2025.TextFile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
public class SupplierDataManager {
    private final List<Supplier>supplierlist;
    private final TextFile textfile;
    private final String supplierfilepath = "src/java_assignment2025/supplier.txt";
    private final String inventoryfilepath = "src/java_assignment2025/inventory.txt";
    
    public SupplierDataManager() {
        this.supplierlist = new ArrayList<>();
        this.textfile = new TextFile();
        loadAllsupplierfromtxtfile();
    }
    
        public void loadAllsupplierfromtxtfile(){
        List<String> lines = textfile.readFile(supplierfilepath);
        for(String line : lines){
            String[] parts = line.split(",", 8);
            if(parts.length == 8){
                supplierlist.add(new Supplier( // one by one add to list
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        Boolean.parseBoolean(parts[6].trim()),
                        Boolean.parseBoolean(parts[7].trim())
                ));
            }
        }
    }

    public String getsupplierfilepath(){
        return supplierfilepath;
    }
    public List<Supplier> getsupplierlist(){
        return supplierlist;
    }
    public String generateSupplierId(){
        int max = 0;
        for (Supplier supplier : supplierlist){
            String supplierid = supplier.getSupplierid();
            int num = Integer.parseInt(supplierid.replace("SUP", ""));
            if (num > max){
                max = num;
            }
        }
        return String.format("SUP%03d", max+1);
    }
    
    public void addSupplier(Supplier supplier){
        if(!duplicatedsupplier(supplier)){
            supplierlist.add(supplier); //add a list here
            System.out.println("Appending supplier: " + supplier.toString());
            textfile.appendTo(supplierfilepath, supplier.toString());
            System.out.println("add supplier successfuly ya");
        }else{
            System.out.println("supplier exist already ya");
        }
    }
    public void marksupplierasDeleted(String supplierid,InventoryDataManager inventorydatamanager) {
        InventoryDataManager inventoryManager = new InventoryDataManager();
        List<Item> items = inventoryManager.getinventorylist();

        List<String> blockingItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.isDeleted() && item.getSupplierid().equals(supplierid)) {
                String status = inventoryManager.getItemDeletionStatus(item.getItemid());
                if (!"can_delete".equals(status)) {
                    blockingItems.add(item.getItemid() + " - " + status.replace('_', ' '));
                }
            }
        }

        if (!blockingItems.isEmpty()) {
            for (String bi : blockingItems) {
                System.out.println("Item ID: " + bi);
            }
            return; 
        }
            Supplier supplier = findsupplierid(supplierid);
            if (supplier != null) {
                supplier.setDeleted(true);
                textfile.rewriteFile(supplierfilepath, supplierlist);
                System.out.println("Supplier deleted successfully.");
                boolean anyChanges = false;
                for (Item item : items) {
                    if (!item.isDeleted() && item.getSupplierid().equals(supplierid)) {
                        item.setDeleted(true);
                        anyChanges = true;
                    }
                }

                if (anyChanges) {
                    textfile.rewriteFile(inventoryfilepath, items); 
                    inventorydatamanager.loadAllinventoryfromtxtfile();
                    System.out.println("Related items also marked as deleted.");
                }
            } else {
                System.out.println("Supplier not found.");
            }
        }

    public void marknewsupplierinitemasRead(String supplierid, boolean status){
        Supplier supplier = findsupplierid(supplierid);
            if (supplier != null){
                supplier.setReadDescrptionStatus(status);
                textfile.rewriteFile(supplierfilepath, supplierlist);
                System.out.println("Updated read status for " + supplierid + " to " + status);
                return;
            }else{
                System.out.println("supplier not found");
            } 
    }
    public void updateSupplier(String supplierId, String supplierName, String address, String contact, String email, String itemDesc, boolean readDescriptionStatus, boolean deleted) {
        Supplier existingsupplier = findsupplierid(supplierId);
        if (existingsupplier != null) {
            existingsupplier.setSupplierid(supplierId);
            existingsupplier.setSuppliername(supplierName);
            existingsupplier.setContact(contact);
            existingsupplier.setEmail(email);
            existingsupplier.setAddress(address);
            existingsupplier.setItemdescription(itemDesc);
            existingsupplier.setReadDescrptionStatus(readDescriptionStatus);
            existingsupplier.setDeleted(deleted);
            textfile.rewriteFile(supplierfilepath, supplierlist);
            System.out.println("Supplier updated successfully.");
        } else {
            System.out.println("Supplier not found.");
        }
    }
//    supplierid, suppliername , contact, email, address, itemdesc, readDescrptionStatus
    private boolean duplicatedsupplier(Supplier supplier){
        for (Supplier existsupplier : supplierlist){
            if (existsupplier.getSupplierid().equals(supplier.getSupplierid())){
                return true;
            }
        }
        return false;
    }
    public Supplier findsupplierid(String supplierId) {
        for (Supplier supplier : supplierlist) {
            if (supplier.getSupplierid().equals(supplierId)) {
                return supplier;
            }
        }
        return null;
    }
}

