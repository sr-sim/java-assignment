/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */

//Sales Id, Item Id, Quantity, Amount, Date of sales
public class IndividualSales {
    private String salesid;
    private String itemid;
    private int quantity;
    private double retailprice;
    private double amount;
    private String dateofsales;
    
    public IndividualSales(String salesid, String itemid, int quantity, double retailprice, double amount, String dateofsales){
        this.salesid = salesid;
        this.itemid = itemid;
        this.quantity = quantity;
        this.retailprice = retailprice;
        this.amount = amount;
        this.dateofsales = dateofsales;
    }
    
    public IndividualSales(){
    }

    public String getSalesid() {
        return salesid;
    }

    public void setSalesid(String salesid) {
        this.salesid = salesid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDateofsales() {
        return dateofsales;
    }

    public void setDateofsales(String dateofsales) {
        this.dateofsales = dateofsales;
    }
    @Override
    public String toString(){
        return salesid + "," + itemid +"," + quantity + "," +  String.format("%.2f", retailprice) + "," +  String.format("%.2f", amount) + "," + dateofsales;
    } 


    public double getretailprice() {
        return retailprice;
    }

    public void setretailprice(double retailprice) {
        this.retailprice = retailprice;
    }

}