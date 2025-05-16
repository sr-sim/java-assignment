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
    private final String inventoryfilepath = "src/java_assignment2025/inventory.txt";
    
    
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
        itemlist.clear();
        List<String> lines = textfile.readFile(inventoryfilepath);
        for(String line : lines){
            String[] parts = line.split(",", 11);
            if(parts.length == 11){
                boolean isdeleted = Boolean.parseBoolean(parts[10].trim());
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
                        isdeleted
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
    PurchaseOrderManager poManager = new PurchaseOrderManager();

    for (PurchaseRequisition pr : prManager.getprlist()) {
        List<String> itemIds = pr.getItemids();

        if (itemIds.contains(itemId)&&!pr.isDeleted()) {
            PurchaseRequisition.ApproveStatus prStatus = pr.getApprovestatus();

            if (prStatus == PurchaseRequisition.ApproveStatus.pending) {
                return "cannot_delete_pending_pr";
            } else if (prStatus == PurchaseRequisition.ApproveStatus.reject) {
                return "cannot_delete_rejected_pr";
            } else if (prStatus == PurchaseRequisition.ApproveStatus.approved) {
                for (PurchaseOrder po : poManager.getpolist()) {
                    if (po.getRequestId().equals(pr.getPrid()) && po.getItemIds().contains(itemId)) {
                        String poStatus = po.getOrderStatus().trim();
                        boolean isPaid = po.getPaymentStatus().trim().equalsIgnoreCase("paid");

                        if (poStatus.equalsIgnoreCase("approved") && isPaid) {
                        } else if (poStatus.equalsIgnoreCase("reject")) {
                            continue; 
                        } else {
                            return "cannot_delete_approved_pr_po_not_paid"; 
                        }
                    }
                }
            }
        }
    }

    return "can_delete"; // No blocking PRs or POs found
}

public List<Item> getItemsBySupplier(String supplierId) {
    List<Item> supplierItems = new ArrayList<>();
    System.out.println("Looking for items for supplier: " + supplierId);
    for(Item item : this.itemlist) {
        if(item.getSupplierid().equalsIgnoreCase(supplierId) && !item.isDeleted()) {
            System.out.println("Found item: " + item.getItemid() + " Supplier: " + item.getSupplierid());
            supplierItems.add(item);
        }
    }
    return supplierItems;
}


}
