/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public class FinanceManager extends User{
    public FinanceManager(String userID, String userName, String password, String fullname, String email, String contact, boolean isActive) {
        super(userID, userName, password, fullname, email, contact, Role.FINANCE_MANAGER, isActive);
    }
    
    public FinanceManager(String userID, String userName, String password, String fullname, String email, String contact) {
        super(userID, userName, password, fullname, email, contact, Role.FINANCE_MANAGER, true);
    }
    
    public FinanceManager(User user) {
        super (user.getUserId(), user.getUsername(), user.getPassword(), user.getFullname(), user.getEmail(), user.getContact(), Role.FINANCE_MANAGER, true);
    }
}
