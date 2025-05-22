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
    private int quantity;
    private double unitprice;
    private double totalprice;
    
    public PRItem(Item item,int quantity,double unitprice,double totalprice){
        this.item = item;
        this.quantity = quantity;
        this.unitprice = unitprice;
        this.totalprice = totalprice;
    }
    
    public Item getItem() {
        return item;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getTotalprice() {
        return totalprice;
    }


    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }
    
    @Override
    public String toString(){
        return item.getItemid() + ","+quantity +","+ String.format("%.2f", unitprice)+","+ String.format("%.2f", totalprice);
    }


    public double getUnitprice() {
        return unitprice;
    }


    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }
}
