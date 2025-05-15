/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public class Item {
    private String itemid;
    private String itemname;
    private String itemdesc;
    private String supplierid;
    private String unitprice;
    private String instockquantity;
    private String retailprice;
    private String reorderlevel;
    private String reorderstatus;
    private String lastmodifieddate;
    private boolean deleted;
   
    public Item(String itemid, String itemname, String itemdesc, String supplierid, String unitprice, String retailprice, String instockquantity, String reorderlevel, String reorderstatus, String lastmodifieddate,boolean deleted){
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemdesc = itemdesc;
        this.supplierid = supplierid;
        this.unitprice = unitprice;
        this.instockquantity = instockquantity;
        this.retailprice = retailprice;
        this.reorderlevel = reorderlevel;
        this.reorderstatus = reorderstatus;
        this.lastmodifieddate = lastmodifieddate;
        this.deleted = deleted;
    }
    public Item() {
    }

    public String getItemid() {
        return itemid;
    }


    public void setItemid(String itemid) {
        this.itemid = itemid;
    }


    public String getItemname() {
        return itemname;
    }


    public void setItemname(String itemname) {
        this.itemname = itemname;
    }


    public String getItemdesc() {
        return itemdesc;
    }


    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }


    public String getSupplierid() {
        return supplierid;
    }


    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }


    public String getUnitprice() {
        return unitprice;
    }


    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }


    public String getInstockquantity() {
        return instockquantity;
    }


    public void setInstockquantity(String instockquantity) {
        this.instockquantity = instockquantity;
    }


    public String getRetailprice() {
        return retailprice;
    }


    public void setRetailprice(String retailprice) {
        this.retailprice = retailprice;
    }

 
    public String getReorderlevel() {
        return reorderlevel;
    }

 
    public void setReorderlevel(String reorderlevel) {
        this.reorderlevel = reorderlevel;
    }


    public String getReorderstatus() {
        return reorderstatus;
    }

    public void setReorderstatus(String reorderstatus) {
        this.reorderstatus = reorderstatus;
    }
    
    public String getLastmodifieddate() {
        return lastmodifieddate;
    }

    public void setLastmodifieddate(String lastmodifieddate) {
        this.lastmodifieddate = lastmodifieddate;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    @Override
    public String toString(){
        return itemid + "," + itemname + "," + itemdesc + "," + supplierid + "," + unitprice+ "," + retailprice+ "," + instockquantity +","+ reorderlevel+ "," + reorderstatus + "," + lastmodifieddate +","+ isDeleted();
    }

    /**
     * @return the deleted
     */

}
