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
//    private static SupplierDataManager supplierDataManager;
//    private static UserFeatures features; 

    public static boolean initialize(User user) {
        currentUser = user;
        return user != null;
        
        //since we do not use data caching, i commented this out
//        features = UserFeatureFactory.getUserFeatures(user);
//        features.loadData();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

//    public static UserFeatures getFeatures() {
//        return features;
//    }

    public static void logout() {
        currentUser = null;
//        features = null;
    }
}
