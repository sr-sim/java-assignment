/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public class InventoryManager extends User{
    public InventoryManager(String userId, String username, String contact, String password) {
        super(userId, username, password, contact, "Inventory Manager");
    }
    public void manageInventory() {
        System.out.println("Managing Inventory...");
    }
}
