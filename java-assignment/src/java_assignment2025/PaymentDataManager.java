/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Isaac
 */
public class PaymentDataManager {
    private final String FILE_PATH = "src/java_assignment2025/payment.txt";

    public List<Payment> loadPayments() {
        List<Payment> payments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Payment payment = new Payment(
                        parts[0], // paymentId
                        parts[1], // poId
                        parts[2], // itemIds
                        parts[3], // unitPrices
                        parts[4], // quantities
                        parts[5],  // amount
                        parts[6]    //paymentdate
                    );
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payments;
    }
    
    public DefaultTableModel getPaymentTableModel() {
        String[] columns = {"Payment ID", "PO ID", "Item IDs", "Unit Prices", "Quantities", "Amount","Payment Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Payment p : loadPayments()) {
            model.addRow(new Object[] {
                p.getPaymentId(),
                p.getPoId(),
                p.getItemIds().replace("|", " | "),
                p.getUnitPrices().replace("|", " | "),
                p.getQuantities().replace("|", " | "),
                p.getAmount(),
                p.getPaymentDate()
            });

        }

        return model;
    }
    
}
