package java_assignment2025;

import java.awt.Desktop;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;


import java.util.*;
import java.util.stream.Collectors;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class FinanceReport {
    private final PurchaseOrderManager poManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SalesDataManager salesDataManager = new SalesDataManager();

    public FinanceReport(PurchaseOrderManager poManager) {
        this.poManager = poManager;
    }

    public List<PurchaseOrder> getPaidOrders() {
        return poManager.getpolist().stream()
                .filter(po -> "paid".equalsIgnoreCase(po.getPaymentStatus()))
                .collect(Collectors.toList());
    }

    public List<PurchaseOrder> filterByMonth(String monthName) {
        return getPaidOrders().stream()
                .filter(po -> {
                    try {
                        Date date = dateFormat.parse(po.getOrderDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                        return month.equalsIgnoreCase(monthName);
                    } catch (ParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

//    public List<PurchaseOrder> filterByWeek(String weekNumberStr) {
//        int targetWeek = Integer.parseInt(weekNumberStr);
//        return getPaidOrders().stream()
//                .filter(po -> {
//                    try {
//                        Date date = dateFormat.parse(po.getOrderDate());
//                        Calendar cal = Calendar.getInstance();
//                        cal.setTime(date);
//                        int week = cal.get(Calendar.WEEK_OF_MONTH);
//                        return week == targetWeek;
//                    } catch (ParseException e) {
//                        return false;
//                    }
//                })
//                .collect(Collectors.toList());
//    }
    public List<PurchaseOrder> filterByWeek(String weekNumberStr) {
        int targetWeek = Integer.parseInt(weekNumberStr);
        return getPaidOrders().stream()
                .filter(po -> {
                    try {
                        Date date = dateFormat.parse(po.getOrderDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int week = cal.get(Calendar.WEEK_OF_MONTH);
                        return week == targetWeek;
                    } catch (ParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public double calculateTotalAmount(List<PurchaseOrder> poList) {
        return poList.stream()
                .mapToDouble(PurchaseOrder::getAmount)
                .sum();
    }

//    public List<DailySalesSummary> getDailySalesSummary(String targetDate, InventoryDataManager inventoryManager) {
//        List<DailySales> dailyList = salesDataManager.getdailysaleslist();
//        List<IndividualSales> individualList = salesDataManager.getindividualsaleslist();
//
//
//        List<DailySalesSummary> summaries = new ArrayList<>();
//
//        for (DailySales daily : dailyList) {
//            if (!daily.getDateofsales().equals(targetDate)) continue;
//
//            String itemId = daily.getItemid();
//            String total = daily.getTotalsales();
//
//            int qty = 0;
//            for (IndividualSales indiv : individualList) {
//                if (indiv.getItemid().equals(itemId) && indiv.getDateofsales().equals(targetDate)) {
//                    qty += Integer.parseInt(indiv.getQuantity());
//                }
//            }
//
//            Item item = inventoryManager.finditemid(itemId);
//            String itemName = (item != null) ? item.getItemname() : "Unknown Item";
//
//            summaries.add(new DailySalesSummary(itemId, itemName, qty, Double.parseDouble(total), targetDate));
//    }
//
//    return summaries;
//}
//    public List<DailySalesSummary> getMonthlySalesSummary(String monthName, InventoryDataManager inventoryManager) {
//    List<DailySalesSummary> allSummaries = new ArrayList<>();
//    List<DailySales> dailyList = salesDataManager.getdailysaleslist();
//    List<IndividualSales> individualList = salesDataManager.getindividualsaleslist();
//
//    for (DailySales daily : dailyList) {
//        try {
//            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(daily.getDateofsales());
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            String entryMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
//
//            if (!entryMonth.equalsIgnoreCase(monthName)) continue;
//
//            String itemId = daily.getItemid();
//            double total = Double.parseDouble(daily.getTotalsales());
//            int qty = 0;
//            for (IndividualSales indiv : individualList) {
//                if (indiv.getItemid().equals(itemId) && indiv.getDateofsales().equals(daily.getDateofsales())) {
//                    qty += Integer.parseInt(indiv.getQuantity());
//                }
//            }
//
//            Item item = inventoryManager.finditemid(itemId);
//            String itemName = (item != null) ? item.getItemname() : "Unknown Item";
//
//            allSummaries.add(new DailySalesSummary(itemId, itemName, qty, total, daily.getDateofsales()));
//        } catch (Exception e) {
//            System.out.println("Error parsing date: " + daily.getDateofsales());
//        }
//    }
//
//    return allSummaries;
//}
    public static void exportJTableToJasper(JTable table) {
    try {
//        System.setProperty("net.sf.jasperreports.export.pdf.force.linebreak.policy", "false");
//        System.setProperty("net.sf.jasperreports.default.pdf.font.name", "Helvetica");
//        System.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1252");
//        System.setProperty("net.sf.jasperreports.default.pdf.embedded", "false");

        List<Map<String, ?>> data = new ArrayList<>();
        
        double totalAmount = 0.0;
        
        for (int row = 0; row < table.getRowCount(); row++) {
            Object statusObj = table.getValueAt(row, 13); 
            if (statusObj == null || !"paid".equalsIgnoreCase(statusObj.toString().trim())) {
                continue; //skip nonpaid rows
            }
            //injecting
            Map<String, Object> record = new HashMap<>();
            record.put("poid", table.getValueAt(row, 0));
            record.put("itemIds", table.getValueAt(row, 4));
            record.put("itemNames", table.getValueAt(row, 5));
            record.put("quantities", table.getValueAt(row, 7).toString().trim());
            record.put("amount", table.getValueAt(row, 8));
            record.put("orderDate", table.getValueAt(row, 10));
            
            Object amtObj = table.getValueAt(row, 8);
            if (amtObj instanceof Number) {
                totalAmount += ((Number) amtObj).doubleValue();
            } else {
                try {
                    totalAmount += Double.parseDouble(amtObj.toString());
                } catch (NumberFormatException e) {
                    
                }
            }

            data.add(record);
        }

        //create the data source
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(data);
        Map<String, Object> params = new HashMap<>();
        params.put("DATA_SOURCE", dataSource);
        
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        params.put("DateCreated", today); 
        
        params.put("GrandTotal", String.format("RM %.2f", totalAmount));

        //load the compiled jasper file
        JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(
            "src/java_assignment2025/Jasper/stupidting.jasper"
        );

        //fill the report
        JasperPrint filled = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

        //export to pdf
        String path = "Payment_Report_" + System.currentTimeMillis() + ".pdf";

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(filled));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));

        SimplePdfExporterConfiguration config = new SimplePdfExporterConfiguration();
        
        exporter.setConfiguration(config);

        exporter.exportReport();
        
        File pdfFile = new File(path);
        if (pdfFile.exists()) {
            Desktop.getDesktop().open(pdfFile);
        }

        JOptionPane.showMessageDialog(null, "Report exported to " + path);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
    }
}    
    public static void exportDailySumToJasper(JTable table) {
    try {
        List<Map<String, ?>> data = new ArrayList<>();
        double totalAmount = 0.0;

        for (int row = 0; row < table.getRowCount(); row++) {
            Map<String, Object> record = new HashMap<>();
            record.put("itemIds", table.getValueAt(row, 0));      // Item ID
            record.put("itemNames", table.getValueAt(row, 1));    // Item Name
            record.put("quantities", table.getValueAt(row, 2));  // Quantity
            Object amtObj = table.getValueAt(row, 3);             // Amount
            record.put("amount", amtObj);
            record.put("orderDate", table.getValueAt(row, 4));    // Date

            if (amtObj instanceof Number) {
                totalAmount += ((Number) amtObj).doubleValue();
            } else {
                try {
                    totalAmount += Double.parseDouble(amtObj.toString().replace("RM", "").trim());
                } catch (NumberFormatException e) {
                  
                }
            }

            data.add(record);
        }

        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(data);

        Map<String, Object> params = new HashMap<>();
        params.put("DATA_SOURCE", dataSource);
        params.put("DateCreated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        params.put("GrandTotal", String.format("RM %.2f", totalAmount));

        JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(
            "src/java_assignment2025/Jasper/DailySumReport.jasper"
        );

        JasperPrint filled = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
        String path = "Daily_Sales_Report_" + System.currentTimeMillis() + ".pdf";

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(filled));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));
        exporter.setConfiguration(new SimplePdfExporterConfiguration());
        exporter.exportReport();
        
        File pdfFile = new File(path);
        if (pdfFile.exists()) {
            Desktop.getDesktop().open(pdfFile);
        }
        
        JOptionPane.showMessageDialog(null, "Report exported to " + path);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
    }
}
    
    public static void PaymentExportToJasper(JTable table) {
    try {
        List<Map<String, ?>> data = new ArrayList<>();
        double totalAmount = 0.0;

        for (int row = 0; row < table.getRowCount(); row++) {
            Map<String, Object> record = new HashMap<>();
            record.put("payid", table.getValueAt(row, 0));
            record.put("itemNames", table.getValueAt(row, 1));
            record.put("itemIds", table.getValueAt(row, 2));
            record.put("quantities", table.getValueAt(row, 3).toString().trim());
            record.put("unitprice", table.getValueAt(row, 4).toString().trim());
            
            Object amtObj = table.getValueAt(row, 5);
            double amountValue = 0.0;
            
            if (amtObj instanceof Number) {
                amountValue = ((Number) amtObj).doubleValue();
            } else {
                try {
                    amountValue = Double.parseDouble(amtObj.toString().trim().replace("RM", "").trim());
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            record.put("amount",amountValue);
            totalAmount += amountValue;
            record.put("orderDate", table.getValueAt(row, 6));
            
            data.add(record);
        }

        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(data);

        Map<String, Object> params = new HashMap<>();
        params.put("DATA_SOURCE", dataSource);
        params.put("DateCreated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        params.put("GrandTotal", String.format("RM %.2f", totalAmount));

        JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(
            "src/java_assignment2025/Jasper/PaymentReport.jasper"
        );

        JasperPrint filled = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

        String path = "Payment_Report_" + System.currentTimeMillis() + ".pdf";

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(filled));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));
        exporter.setConfiguration(new SimplePdfExporterConfiguration());

        exporter.exportReport();
        
        File pdfFile = new File(path);
        if (pdfFile.exists()) {
            Desktop.getDesktop().open(pdfFile);
        }

        JOptionPane.showMessageDialog(null, "Payment report exported to " + path);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
    }
}
    
    public static List<Object[]> filterByDate(List<IndividualSales> individualList, Date selectedDate, InventoryDataManager inventoryDataManager) {
        Map<String, Object[]> grouped = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selected = sdf.format(selectedDate);

        for (IndividualSales ind : individualList) {
            if (ind.getDateofsales().equals(selected)) {
                String itemId = ind.getItemid();
                String key = itemId + "_" + selected;

                Item item = inventoryDataManager.finditemid(itemId);
                String itemName = (item != null) ? item.getItemname() : "Unknown Item";

                int quantity = ind.getQuantity();
                double amount = ind.getAmount();

                if (!grouped.containsKey(key)) {
                    grouped.put(key, new Object[]{itemId, itemName, quantity, amount, selected});
                } else {
                    Object[] row = grouped.get(key);
                    row[2] = (int) row[2] + quantity;
                    row[3] = (double) row[3] + amount;
                }
            }
        }

        return new ArrayList<>(grouped.values());
    }


    public static List<Object[]> filterByMonth(List<IndividualSales> individualList, String monthName, InventoryDataManager inventoryDataManager) {
        Map<String, Object[]> grouped = new LinkedHashMap<>();

        for (IndividualSales ind : individualList) {
            try {
                LocalDate date = LocalDate.parse(ind.getDateofsales());
                String saleMonth = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                if (saleMonth.equalsIgnoreCase(monthName)) {
                    String itemId = ind.getItemid();
                    String dateStr = ind.getDateofsales();
                    String key = itemId + "_" + dateStr;

                    Item item = inventoryDataManager.finditemid(itemId);
                    String itemName = (item != null) ? item.getItemname() : "Unknown Item";

                    int quantity = ind.getQuantity();
                    double amount = ind.getAmount();

                    if (!grouped.containsKey(key)) {
                        grouped.put(key, new Object[]{itemId, itemName, quantity, amount, dateStr});
                    } else {
                        Object[] row = grouped.get(key);
                        row[2] = (int) row[2] + quantity;
                        row[3] = (double) row[3] + amount;
                    }
                }
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>(grouped.values());
    }

}


    
