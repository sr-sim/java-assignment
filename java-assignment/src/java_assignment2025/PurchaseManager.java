/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public class PurchaseManager extends User{
    public PurchaseManager(String userID, String userName, String password, String fullname, String email, String contact, boolean isActive) {
        super(userID, userName, password, fullname, email, contact, Role.PURCHASE_MANAGER, isActive);
    }
    
    public PurchaseManager(String userID, String userName, String password, String fullname, String email, String contact) {
        super(userID, userName, password, fullname, email, contact, Role.PURCHASE_MANAGER, true);
    }
    
    public PurchaseManager(User user) {
        super (user.getUserId(), user.getUsername(), user.getPassword(), user.getFullname(), user.getEmail(), user.getContact(), Role.PURCHASE_MANAGER, true);
    }
}