/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author ASUS
 */
public interface CanManageUser {
    default UserDataManager getUserDataManager() {
        return DataManagerFactory.getDataManager(UserDataManager.class);
    }
}
