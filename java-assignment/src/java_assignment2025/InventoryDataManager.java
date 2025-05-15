/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class InventoryDataManager {
    private final List<Item>itemlist;
    private final List<Item>itemlistbysupplier;
    private final TextFile textfile;
    private final String inventoryfilepath = "C:\\Users\\User\\OneDrive\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\inventory.txt";
    
    
    public InventoryDataManager() {
        this.itemlist = new ArrayList<>();
        this.itemlistbysupplier = new ArrayList<>();
        this.textfile = new TextFile();
        loadAllinventoryfromtxtfile();
    }
    public List<Item> getinventorylist(){
        return itemlist;
    }
    public void loadAllinventoryfromtxtfile(){  
        List<String> lines = textfile.readFile(inventoryfilepath);
        for(String line : lines){
            String[] parts = line.split(",", 11);
            if(parts.length == 11){
                    itemlist.add(new Item( // one by one add to list
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        parts[6].trim(),
                        parts[7].trim(),
                        parts[8].trim(),
                        parts[9].trim(),
                        Boolean.parseBoolean(parts[10].trim())
                ));
            }
        }
        
    }
    public String getinventoryfilepath(){
        return inventoryfilepath;
    }
    public String generateItemId(){
        int max = 0;
        for (Item item : itemlist){
            String itemid = item.getItemid();
            int num = Integer.parseInt(itemid.replace("I", ""));
            if (num > max){
                max = num;
            }
        }
        return String.format("I%03d", max+1);
    }
    
    public void addItem(Item item){
        if(!duplicateditem(item)){
            itemlist.add(item); //add a list here
            System.out.println("Appending item: " + item.toString());
            textfile.appendTo(inventoryfilepath, item.toString());
            System.out.println("add item successfuly ya");
        }else{
            System.out.println("item exist already ya");
        }
    }
    public void markitemasDeleted(String itemid){
        Item item = finditemid(itemid);
            if (item != null){
                item.setDeleted(true);
                textfile.rewriteFile(inventoryfilepath, itemlist);
                System.out.println("delete successful");
                return;
            }else{
                System.out.println("item not found");
            } 
    }
    public void updateItem(String itemid, String itemname, String itemdesc, String supplierid, String unitprice, String retailprice,String instockquantity,String reorderlevel,String reorderstatus,String lastmodifieddate,boolean deleted) {
        Item existingitem = finditemid(itemid);
        if (existingitem != null) {
            existingitem.setItemid(itemid);
            existingitem.setItemname(itemname);
            existingitem.setItemdesc(itemdesc);
            existingitem.setSupplierid(supplierid);
            existingitem.setUnitprice(unitprice);
            existingitem.setInstockquantity(instockquantity);
            existingitem.setRetailprice(retailprice);
            existingitem.setReorderlevel(reorderlevel);
            existingitem.setReorderstatus(reorderstatus);
            existingitem.setLastmodifieddate(lastmodifieddate);
            existingitem.setDeleted(deleted);
            textfile.rewriteFile(inventoryfilepath, itemlist);
            System.out.println("Supplier updated successfully.");
        } else {
            System.out.println("Supplier not found.");
        }
    }
//        private String itemid;
//    private String itemname;
//    private String itemdesc;
//    private String supplierid;
//    private String unitprice;
//    private String instockquantity;
//    private String retailprice;
//    private String reorderlevel;
//    private String reorderstatus;
//    private String lastmodifieddate;
    
    private boolean duplicateditem(Item item){
        for (Item existitem : itemlist){
            if (existitem.getItemid().equals(item.getItemid())){
                return true;
            }
        }
        return false;
    }
    public Item finditemid(String itemid){
        for (Item item : itemlist){
            if(item.getItemid().equals(itemid)){
                return item;
            }
        }
        return null;
    }
    
public String getItemDeletionStatus(String itemId) {
    PurchaseRequisitionManager prManager = new PurchaseRequisitionManager();
    PurchaseRequisition pr = null;

    // Step 1: Look for PR that contains the exact itemId
    for (PurchaseRequisition p : prManager.getprlist()) {
        List<String> itemIds = p.getItemids(); // List<String>
        for (String id : itemIds) {
            if (id.trim().equalsIgnoreCase(itemId)) {
                pr = p;
                break;
            }
        }
        if (pr != null) break; // Exit loop if PR found
    }

    // Step 2: If no PR contains the item, allow delete
    if (pr == null) {
        return "can_delete";
    }

    // Step 3: Get PR status
    PurchaseRequisition.ApproveStatus prStatus = pr.getApprovestatus();

    // Step 4: Look for PO containing the item
    PurchaseOrderManager poManager = new PurchaseOrderManager();
    PurchaseOrder po = null;
    for (PurchaseOrder order : poManager.getpolist()) {
        List<String> itemIds = order.getItemIds(); // List<String>
        for (String id : itemIds) {
            if (id.trim().equalsIgnoreCase(itemId)) {
                po = order;
                break;
            }
        }
        if (po != null) break; // Exit loop if PO found
    }

    // Step 5: If no PO found, handle based on PR status
    if (po == null) {
        if (prStatus == PurchaseRequisition.ApproveStatus.pending) {
            return "cannot_delete_pending_pr";
        } else if (prStatus == PurchaseRequisition.ApproveStatus.reject) {
            return "cannot_delete_rejected_pr";
        } else {
            return "can_delete"; // Approved PR but no PO
        }
    }

    // Step 6: PO found, check PO status
    String poStatus = po.getVerifyStatus().trim();  // Ensure no extra spaces
    boolean isPaid = po.getPaymentStatus().trim().equalsIgnoreCase("paid");

    // Debugging PO status explicitly
    System.out.println("DEBUG: poStatus = [" + poStatus + "]");  // Show exactly what poStatus is
    System.out.println("DEBUG: isPaid = " + isPaid);

    // PR Approved
    if (prStatus == PurchaseRequisition.ApproveStatus.approved) {
        // Check if PO is rejected
        if (poStatus.equalsIgnoreCase("reject")) {
            System.out.println("DEBUG: PO is rejected, allowing deletion.");
            return "can_delete"; // PR approved but PO rejected
        }
        // Check if PO is approved and paid
        if (poStatus.equalsIgnoreCase("approved") && isPaid) {
            System.out.println("DEBUG: PO is approved and paid, allowing deletion.");
            return "can_delete"; // PR approved and PO approved & paid
        }
        System.out.println("DEBUG: PO is not approved or paid, not allowed to delete.");
        return "cannot_delete_approved_pr_po_not_paid"; // PR approved but PO not paid or not approved
    }

    // PR Pending or Rejected
    if (prStatus == PurchaseRequisition.ApproveStatus.pending) {
        return "cannot_delete_pending_pr";
    } else if (prStatus == PurchaseRequisition.ApproveStatus.reject) {
        return "cannot_delete_rejected_pr";
    }

    return "cannot_delete_unknown"; // If something goes wrong
}

public List<Item> getItemsBySupplier(String supplierId) {
    List<Item> supplierItems = new ArrayList<>();
    for(Item item : this.itemlistbysupplier) {  // assume itemList holds all items
        if(item.getSupplierid().equalsIgnoreCase(supplierId) && !item.isDeleted()) {
            supplierItems.add(item);
        }
    }
    return supplierItems;
}

}
