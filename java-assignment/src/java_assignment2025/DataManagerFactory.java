/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public class DataManagerFactory {
    //put pair DataManger class & instance of the data manager
    private static final Map<Class<?>, DataManager> dataManagerMap = new HashMap<>();
    
    @SuppressWarnings("unchecked")
    public static <T extends DataManager> T getDataManager(Class<T> dataManagerClass) {
        if (dataManagerMap.containsKey(dataManagerClass)) {    //returns if already loaded
            return (T) dataManagerMap.get(dataManagerClass);
        }
        try {
            T manager = dataManagerClass.getDeclaredConstructor().newInstance();
            dataManagerMap.put(dataManagerClass, manager);
            return manager;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate data manager");
        }
    }
    public static void clearAll() {
        dataManagerMap.clear();
    }
}

