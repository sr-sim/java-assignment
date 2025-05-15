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
    private final TextFile textfile;
    private final String inventoryfilepath = "C:\\Users\\Isaac\\OneDrive - Asia Pacific University\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\inventory.txt";
    
    
    public InventoryDataManager() {
        this.itemlist = new ArrayList<>();
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
    
    public String findItemNameById(String itemid) {
    Item item = finditemid(itemid);
    return (item != null) ? item.getItemname() : "Unknown";
}

    
    
}
