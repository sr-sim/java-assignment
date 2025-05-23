/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
/**
 *
 * @author hew
 */
public class PartialReceivedDataManager {
    private final List<PartialReceived> partialReceiveds;
    private final TextFile textfile;
    private final String filepath;
    private final PurchaseOrderManager pomanager;
    private static final String DEFAULT_FILEPATH = "src/java_assignment2025/PartialReceiveds.txt";
    // Constructor with pomanager only (uses default filepath)
    public PartialReceivedDataManager(PurchaseOrderManager pomanager) {
        this(DEFAULT_FILEPATH, pomanager);
    }
                                                                                           
    // Constructor with custom filepath
    public PartialReceivedDataManager(String filepath, PurchaseOrderManager pomanager) {
        this.partialReceiveds = new ArrayList<>();
        this.textfile = new TextFile();
        this.filepath = filepath;
        this.pomanager = pomanager;
        loadAllPartialReceiveds();
    }

    

    public void loadAllPartialReceiveds() {
        partialReceiveds.clear();
        List<String> lines = textfile.readFile(filepath);
        for (String line : lines) {
            PartialReceived pr = PartialReceived.fromString(line);
            if (pr != null) {
                PurchaseOrder po = pomanager.findpoid(pr.getOrderId());
                if (po != null && po.getVerifyStatus().equals("partial received")) {
                    partialReceiveds.add(pr);
                    System.out.println("Loaded PartialReceived for PO: " + pr.getOrderId());
                } else {
                    System.err.println("Invalid PartialReceived entry: No matching PO or incorrect receiveStatus for " + pr.getOrderId());
                }
            } else {
                System.err.println("Failed to parse PartialReceived line: " + line);
            }
        }
    }

    public void addPartialReceived(PartialReceived pr) {
        partialReceiveds.add(pr);
        saveToFile();
    }

    public void removePartialReceived(String orderId) {
      partialReceiveds.removeIf(pr -> pr.getOrderId().equals(orderId));
        saveToFile();
    }

    public List<PartialReceived> getPartialReceiveds() {
        return partialReceiveds;
    }

    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (PartialReceived pr : partialReceiveds) {
            lines.add(pr.toString());
        }
        textfile.rewriteFile(filepath, lines);
    }
}
