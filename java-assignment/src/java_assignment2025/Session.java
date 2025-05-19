/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author ASUS
 */
public class Session {
    private static User currentUser;

    public static boolean initialize(User user) {
        currentUser = user;
        return user != null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
        DataManagerFactory.clearAll();
    }
}
