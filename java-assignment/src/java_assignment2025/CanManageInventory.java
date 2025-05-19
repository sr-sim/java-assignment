/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author ASUS
 */
public interface CanManageInventory {
    default InventoryDataManager getInventoryDataManager() {
        return DataManagerFactory.getDataManager(InventoryDataManager.class);
    }
}
