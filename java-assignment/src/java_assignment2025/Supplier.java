/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
//Supplier Id, Supplier name, Address, Contact, email, item description
public class Supplier {
    private String supplierid;
    private String suppliername;
    private String address;
    private String contact;
    private String email;
    private String itemdescription;
    private boolean readDescrptionStatus;
    
    public Supplier(String supplierid, String suppliername, String address, String contact, String email, String itemdescription, boolean readDescrptionStatus){
        this.supplierid = supplierid;
        this.suppliername = suppliername;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.itemdescription = itemdescription;
        this.readDescrptionStatus =readDescrptionStatus;
    }
    public Supplier() {
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public boolean getReadDescrptionStatus() {
        return readDescrptionStatus;
    }

    public void setReadDescrptionStatus(boolean readDescrptionStatus) {
        this.readDescrptionStatus = readDescrptionStatus;
    }
    @Override
    public String toString(){
        return supplierid + "," + suppliername + "," + address + "," + contact +"," + email+ "," + itemdescription + "," + readDescrptionStatus;
    }

}
//suppliername;
//    private String address;
//    private String contact;
//    private String email;
//    private String itemdescription;