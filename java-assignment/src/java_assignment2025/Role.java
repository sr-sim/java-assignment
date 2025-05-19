/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author ASUS
 */
public enum Role {
    ADMIN("Administrator", "AM"),
    SALES_MANAGER("Sales Manager", "SM"),
    PURCHASE_MANAGER("Purchase Manager", "PM"),
    INVENTORY_MANAGER("Inventory Manager", "IM"),
    FINANCE_MANAGER("Finance Manager", "FM");
    
    private final String roleFullName;
    private final String roleAbbr;

    private Role(String roleFullName, String roleAbbr) {
        this.roleFullName = roleFullName;   //for display saja
        this.roleAbbr = roleAbbr;   //for write to text file punya
    }

    //to display full role name
    public String getRoleFullName() {
        return roleFullName;
    }

    //to write in text file
    public String getRoleAbbr() {
        return roleAbbr;
    }
    
    //use this method to convert string to enum value
    public static Role convertFromAbbr(String data) {
        return EnumUtils.convertFromLog(Role.class, Role::getRoleAbbr, data, "user role");
    }
    
    public static Role convertFromFullName(String data) {
        return EnumUtils.convertFromLog(Role.class, Role::getRoleFullName, data, "user role");
    }
}
