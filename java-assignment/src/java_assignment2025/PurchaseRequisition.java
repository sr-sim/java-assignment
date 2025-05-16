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
public class PurchaseRequisition {
    private String prid;
    private List<String> itemids;
    private String userid;
    private List<String> quantities;
    private List<String> unitPrices;
    private String total;
    private String requestdate;
    private String expecteddeliverydate;
    private ApproveStatus approvestatus;
    private String note;
    private boolean deleted;
    
    public enum ApproveStatus {
    pending, approved, reject;

    public static ApproveStatus fromString(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return pending;
            case "approved":
                return approved;
            case "reject":
                return reject;
            default:
                throw new IllegalArgumentException("Invalid approve status: " + status);
        }
    }
}

    public PurchaseRequisition(String prid, List<String> itemids, String userid,List<String> quantities,List<String> unitPrices,String total,String requestdate, String expecteddeliverydate, ApproveStatus approvestatus, String note,boolean deleted){
        this.prid = prid;
        this.itemids = itemids;
        this.userid = userid;
        this.quantities = quantities;
        this.unitPrices=unitPrices;
        this.total = total;
        this.requestdate = requestdate;
        this.expecteddeliverydate = expecteddeliverydate;
        this.approvestatus = approvestatus;
        this.note = note;
        this.deleted = deleted;
    }
    public PurchaseRequisition() {
        this.itemids = new ArrayList<>();
    }
    public void addItemid(String itemid){
        this.getItemids().add(itemid);
    }

    public String getPrid() {
        return prid;
    }


    public void setPrid(String prid) {
        this.prid = prid;
    }

    
    public List<String> getItemids() {
        return itemids;
    }


    public void setItemids(List<String> itemids) {
        this.itemids = itemids;
    }


    public String getUserid() {
        return userid;
    }


    public void setUserid(String userid) {
        this.userid = userid;
    }


    public List<String> getQuantities() {
        return quantities;
    }


    public void setQuantitiesList(List<String> quantities) {
        this.quantities = quantities;
    }

    
    public String getTotal() {
        return total;
    }

    
    public void setTotal(String total) {
        this.total = total;
    }


    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }

    public String getExpecteddeliverydate() {
        return expecteddeliverydate;
    }

    public void setExpecteddeliverydate(String expecteddeliverydate) {
        this.expecteddeliverydate = expecteddeliverydate;
    }

    public ApproveStatus getApprovestatus() {
        return approvestatus;
    }

    public void setApprovestatus(ApproveStatus approvestatus) {
        this.approvestatus = approvestatus;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    @Override
    public String toString(){
        return prid + "," + String.join("|", itemids) + "," + userid + "," + String.join("|", quantities) +","+ String.join("|",unitPrices)+"," + total+ "," + requestdate + "," + expecteddeliverydate + "," + approvestatus + "," + note + ","+deleted;
    } 

    
    public List<String> getUnitPrices() {
        return unitPrices;
    }

   
    public void setUnitPrices(List<String> unitPrices) {
        this.unitPrices = unitPrices;
    }


    public boolean isDeleted() {
        return deleted;
    }


    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
