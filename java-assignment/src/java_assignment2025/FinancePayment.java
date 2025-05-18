/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author Isaac
 */
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FinancePayment {
    
    public void markAsPaid(String poId) throws IOException {
        File poFile = new File("C:\\Users\\Isaac\\OneDrive - Asia Pacific University\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\PurchaseOrder.txt");
        List<String> lines = Files.readAllLines(poFile.toPath());
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[0].equals(poId)) {
                parts[12] = "paid"; // update paymentStatus
                updatedLines.add(String.join(",", parts));
            } else {
                updatedLines.add(line);
}
        }

        Files.write(poFile.toPath(), updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public boolean createPaymentEntry(String poId) throws IOException {
    File poFile = new File("src/java_assignment2025/PurchaseOrder.txt");
    File paymentFile = new File("src/java_assignment2025/payment.txt");

    List<String> poLines = Files.readAllLines(poFile.toPath());
    List<String> paymentLines = paymentFile.exists() ? Files.readAllLines(paymentFile.toPath()) : new ArrayList<>();

    int nextId = paymentLines.size() + 1;
    String paymentId = String.format("P%02d", nextId);

    for (String line : poLines) {
        String[] parts = line.split(",");
        if (parts[0].equals(poId)) {
            // ✅ Check if "verified" before proceeding
            if (!parts[11].equalsIgnoreCase("received")) {
                JOptionPane.showMessageDialog(null,
                        "Payment cannot proceed. Status is not 'received' for PO: " + poId);
                return false;
            }

            String itemIds = parts[4];        // I002|I003|I004
            String unitPrices = parts[5];     // 2.5|3.0|5.0
            String quantities = parts[6];     // 2|3|5

            String[] qtyArr = quantities.split("\\|");
            String[] priceArr = unitPrices.split("\\|");

            if (qtyArr.length != priceArr.length) {
                JOptionPane.showMessageDialog(null,
                        "Mismatch between quantities and unit prices for PO: " + poId);
                return false;
            }

            double totalAmount = 0;

            for (int i = 0; i < qtyArr.length; i++) {
                int qty = Integer.parseInt(qtyArr[i]);
                double price = Double.parseDouble(priceArr[i]);
                totalAmount += qty * price;
            }
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String paymentEntry = paymentId + "," + poId + "," +
                    itemIds + "," +
                    unitPrices + "," +
                    quantities + "," +
                    String.format("%.2f", totalAmount) + "," +
                    currentDate;

            Files.write(paymentFile.toPath(),
                    Collections.singletonList(paymentEntry),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return true;
        }
    }
    return false;
}


    public boolean processPayment(String poId) {
        try {
            File poFile = new File("src/java_assignment2025/PurchaseOrder.txt");
            List<String> poLines = Files.readAllLines(poFile.toPath());

            for (String line : poLines) {
                String[] parts = line.split(",");
                if (parts[0].equals(poId)) {
                    String paymentStatus = parts[12].trim();
                    if (paymentStatus.equalsIgnoreCase("paid")) {
                        JOptionPane.showMessageDialog(null, "PO " + poId + " has already been paid.");
                        return false;
                    }
                }
            }
            boolean success = createPaymentEntry(poId);
            if (success){
                markAsPaid(poId);
                JOptionPane.showMessageDialog(null, "Payment recorded for " + poId);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to process payment");
        }
        return false;
    }
}


