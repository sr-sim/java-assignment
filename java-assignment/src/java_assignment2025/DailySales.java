/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
//Item Id, Total Sales (for each item), Date of sales
public class DailySales {
    private String itemid;
    private double totalsales;
    private String dateofsales;
    
    public DailySales(String itemid, double totalsales, String dateofsales){
        this.itemid = itemid;
        this.totalsales = totalsales;
        this.dateofsales = dateofsales;
    }
    
    public DailySales(){
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public double getTotalsales() {
        return totalsales;
    }

    public void setTotalsales(double totalsales) {
        this.totalsales = totalsales;
    }

    public String getDateofsales() {
        return dateofsales;
    }

    public void setDateofsales(String dateofsales) {
        this.dateofsales = dateofsales;
    }
    @Override
    public String toString(){
        return  itemid +"," +  String.format("%.2f", totalsales) + "," + dateofsales;
    } 
}
