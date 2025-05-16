/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.List;

/**
 *
 * @author ASUS
 */
public class AuthManager {
    public static User authenticate(String username, String password) {
        for (User user : new UserDataManager().getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.isActive()) {
                return switch (user.getRole()) {
                    case ADMIN -> new Administrator(user);
                    case SALES_MANAGER -> new SalesManager(user);
                    case FINANCE_MANAGER -> new FinanceManager(user);
                    case INVENTORY_MANAGER -> new InventoryManager(user);
                    case PURCHASE_MANAGER -> new PurchaseManager(user);
                };
            }
        }
        return null;
    }
}
