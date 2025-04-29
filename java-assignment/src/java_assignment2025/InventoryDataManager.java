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
    private final String inventoryfilepath = "C:\\Users\\User\\OneDrive\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\inventory.txt";
    
    
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
            String[] parts = line.split(",", 10);
            if(parts.length == 10){
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
                        parts[9].trim()
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
    public void deleteItem(String itemid){
        Item item = finditemid(itemid);
            if (item != null){
                itemlist.remove(item);
                textfile.deleteLine(inventoryfilepath, item.toString());
                System.out.println("delete successful");
                return;
            }else{
                System.out.println("item not found");
            } 
    }
    public void updateItem(Item olditem, Item newitem){
        if(itemlist.contains(olditem)){
            itemlist.remove(olditem);
            itemlist.add(newitem);
            textfile.replaceLine(inventoryfilepath, olditem.toString(), newitem.toString());
            System.out.println("Updating item: " + olditem.getItemid() + " to " + newitem.getItemid());

            System.out.println("successful edit");
        }else{
            System.out.println("failed to edit");
        }
    }
    
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

}
