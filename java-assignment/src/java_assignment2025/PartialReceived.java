/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;
import java.util.List;
/**
 *
 * @author hew
 */
public class PartialReceived {
    private String orderId;
    private List<String> itemIds;
    private List<String> itemNames;
    private List<Integer> remainingQuantities;

    public PartialReceived(String orderId, List<String> itemIds, List<String> itemNames, List<Integer> remainingQuantities) {
        this.orderId = orderId;
        this.itemIds = itemIds;
        this.itemNames = itemNames;
        this.remainingQuantities = remainingQuantities;
    }

    public String getOrderId() { return orderId; }
    public List<String> getItemIds() { return itemIds; }
    public List<String> getItemNames() { return itemNames; }
    public List<Integer> getRemainingQuantities() { return remainingQuantities; }

    @Override
    public String toString() {
        return orderId + "," + 
               String.join("|", itemIds) + "," + 
               String.join("|", itemNames) + "," + 
               String.join("|", remainingQuantities.stream().map(String::valueOf).toList());
    }

    public static PartialReceived fromString(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length == 4) {
            String orderId = parts[0].trim();
            List<String> itemIds = List.of(parts[1].split("\\|"));
            List<String> itemNames = List.of(parts[2].split("\\|"));
            List<Integer> remainingQuantities = List.of(parts[3].split("\\|"))
                    .stream().map(Integer::parseInt).toList();
            return new PartialReceived(orderId, itemIds, itemNames, remainingQuantities);
        }
        return null;
    }
}
