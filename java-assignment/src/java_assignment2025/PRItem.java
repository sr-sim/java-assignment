/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public class PRItem {
    private Item item;
    private String quantity;
    private String totalprice;
    
    public PRItem(Item item,String quantity, String totalprice){
        this.item = item;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }
    
    public Item getItem() {
        return item;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    public String getQuantity() {
        return quantity;
    }


    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getTotalprice() {
        return totalprice;
    }


    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
    
    @Override
    public String toString(){
        return item.getItemid() + ","+quantity +","+totalprice;
    }
}
