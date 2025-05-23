/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author hew
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.awt.Desktop;

public class InventoryStockReport {
    private File pdfFolder = new File("./reports/");

    // Data structure for report rows
    public static class StockReportRow {
        private String itemId;
        private String itemName;
        private String itemDescription;
        private String supplierId;
        private String supplierName;
        private int inStockQty;
        private double unitPrice;
        private double retailPrice;
        private String lastModifiedDate;
        private int restockQuantity;
        public StockReportRow(String itemId, String itemName, String itemDescription, String supplierId,
                              String supplierName, int inStockQty, double unitPrice, double retailPrice,
                              String lastModifiedDate, int restockQuantity) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.itemDescription = itemDescription;
            this.supplierId = supplierId;
            this.supplierName = supplierName;
            this.inStockQty = inStockQty;
            this.unitPrice = unitPrice;
            this.retailPrice = retailPrice;
            this.lastModifiedDate = lastModifiedDate;
            this.restockQuantity = restockQuantity;
        }

        // Getters for JasperReports
        public String getItemId() { return itemId; }
        public String getItemName() { return itemName; }
        public String getItemDescription() { return itemDescription; }
        public String getSupplierId() { return supplierId; }
        public String getSupplierName() { return supplierName; }
        public int getInStockQty() { return inStockQty; }
        public double getUnitPrice() { return unitPrice; }
        public double getRetailPrice() { return retailPrice; }
        public String getLastModifiedDate() { return lastModifiedDate; }
        public int getRestockQuantity() { return restockQuantity; }
    }

    public InventoryStockReport() {
        // Ensure reports folder exists
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }
    }
    
    public String generateStockReport(JTable table, String selectedItemId, String month, javax.swing.JFrame parent) {
        try {
            String reportType = selectedItemId != null ? "Selected Item" : "All Items";
            List<StockReportRow> reportData = new ArrayList<>();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (selectedItemId == null || model.getValueAt(i, 0).equals(selectedItemId)) {
                    reportData.add(new StockReportRow(
                        (String) model.getValueAt(i, 0),
                        (String) model.getValueAt(i, 1),
                        (String) model.getValueAt(i, 2),
                        (String) model.getValueAt(i, 3),
                        (String) model.getValueAt(i, 4),
                        ((Number) model.getValueAt(i, 5)).intValue(),
                        ((Number) model.getValueAt(i, 6)).doubleValue(),
                        ((Number) model.getValueAt(i, 7)).doubleValue(),
                        (String) model.getValueAt(i, 8),
                        ((Number) model.getValueAt(i, 9)).intValue()
                    ));
                }
            }

            String jrxmlFile = "/InventoryStockReport.jrxml";
            InputStream jrxmlStream = getClass().getResourceAsStream(jrxmlFile);
            System.out.println("Classpath: " + getClass().getResource("/"));
            System.out.println("JRXML URL: " + getClass().getResource(jrxmlFile));
            if (jrxmlStream == null) {
                throw new Exception("JRXML resource not found: " + jrxmlFile);
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Stock Report - " + (selectedItemId != null ? selectedItemId : "All Items"));
            parameters.put("Month", month);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                JasperCompileManager.compileReport(jrxmlStream),
                parameters,
                dataSource
            );

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = sdf.format(new Date());
            String pdfFileName = "Stock_Report_" + timestamp + ".pdf";
            File pdfFile = new File(pdfFolder, pdfFileName);

            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdfFile));

            openPDF(pdfFile);

            return pdfFileName;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error generating PDF: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
    private void openPDF(File pdfFile) {
        try {
            if (Desktop.isDesktopSupported() && pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                throw new Exception("Cannot open PDF file: " + pdfFile.getName());
            }
        } catch (Exception e) {
            // Error is shown in the calling method
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
