package java_assignment2025;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    // ✅ Returns all paid POs
    public List<PurchaseOrder> getPaidOrders() {
        return poManager.getpolist().stream()
                .filter(po -> "paid".equalsIgnoreCase(po.getPaymentStatus()))
                .collect(Collectors.toList());
    }

    // ✅ Filter by Month (e.g., "May")
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

    // ✅ Filter by Week number (e.g., "1", "2", ...)
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

    // ✅ Calculate total amount for a list of POs
    public double calculateTotalAmount(List<PurchaseOrder> poList) {
        return poList.stream()
                .mapToDouble(PurchaseOrder::getAmount)
                .sum();
    }
    
    public List<DailySalesSummary> getDailySalesSummary(String targetDate, InventoryDataManager inventoryManager) {
        List<DailySales> dailyList = salesDataManager.getdailysaleslist();
        List<IndividualSales> individualList = salesDataManager.getindividualsaleslist();


        List<DailySalesSummary> summaries = new ArrayList<>();

        for (DailySales daily : dailyList) {
            if (!daily.getDateofsales().equals(targetDate)) continue;

            String itemId = daily.getItemid();
            String total = daily.getTotalsales();

            int qty = 0;
            for (IndividualSales indiv : individualList) {
                if (indiv.getItemid().equals(itemId) && indiv.getDateofsales().equals(targetDate)) {
                    qty += Integer.parseInt(indiv.getQuantity());
                }
            }

            Item item = inventoryManager.finditemid(itemId);
            String itemName = (item != null) ? item.getItemname() : "Unknown Item";

            summaries.add(new DailySalesSummary(itemId, itemName, qty, Double.parseDouble(total), targetDate));
    }

    return summaries;
}
    public List<DailySalesSummary> getMonthlySalesSummary(String monthName, InventoryDataManager inventoryManager) {
    List<DailySalesSummary> allSummaries = new ArrayList<>();
    List<DailySales> dailyList = salesDataManager.getdailysaleslist();
    List<IndividualSales> individualList = salesDataManager.getindividualsaleslist();

    for (DailySales daily : dailyList) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(daily.getDateofsales());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String entryMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

            if (!entryMonth.equalsIgnoreCase(monthName)) continue;

            String itemId = daily.getItemid();
            double total = Double.parseDouble(daily.getTotalsales());
            int qty = 0;
            for (IndividualSales indiv : individualList) {
                if (indiv.getItemid().equals(itemId) && indiv.getDateofsales().equals(daily.getDateofsales())) {
                    qty += Integer.parseInt(indiv.getQuantity());
                }
            }

            Item item = inventoryManager.finditemid(itemId);
            String itemName = (item != null) ? item.getItemname() : "Unknown Item";

            allSummaries.add(new DailySalesSummary(itemId, itemName, qty, total, daily.getDateofsales()));
        } catch (Exception e) {
            System.out.println("Error parsing date: " + daily.getDateofsales());
        }
    }

    return allSummaries;
}
//    public static void exportJTableToJasper(JTable table) {
//    try {
//        System.setProperty("net.sf.jasperreports.export.pdf.force.linebreak.policy", "false");
//        System.setProperty("net.sf.jasperreports.default.pdf.font.name", "Helvetica");
//        System.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1252");
//        System.setProperty("net.sf.jasperreports.default.pdf.embedded", "false");
//
//        // Step 1: Extract JTable data into a list of maps
//        List<Map<String, ?>> data = new ArrayList<>();
//        for (int row = 0; row < table.getRowCount(); row++) {
//            Map<String, Object> record = new HashMap<>();
//            record.put("poid", table.getValueAt(row, 0));
//            record.put("itemIds", table.getValueAt(row, 1));
//            record.put("itemNames", table.getValueAt(row, 2));
//            record.put("quantities", table.getValueAt(row, 3));
//            record.put("amount", table.getValueAt(row, 4));
//            record.put("orderDate", table.getValueAt(row, 5));
//            data.add(record);
//        }
//
//        // Step 2: Create data source
//        JRMapCollectionDataSource tableSource = new JRMapCollectionDataSource(data);
//        Map<String, Object> params = new HashMap<>();
//        params.put("REPORT_DATA_SOURCE", tableSource);
//
//        // Step 3: Load compiled Jasper file
//        JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(
//            "C://Users//Isaac//OneDrive - Asia Pacific University//Documents//NetBeansProjects//java-assignment//java-assignment//src//java_assignment2025//Jasper//Finance.jasper"
//        );
//
//        // Step 4: Fill report
//        JasperPrint filled = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
//
//        // Step 5: Export to PDF
//        String path = "Finance_Report_" + System.currentTimeMillis() + ".pdf";
//
//        JRPdfExporter exporter = new JRPdfExporter();
//        exporter.setExporterInput(new SimpleExporterInput(filled));
//        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));
//
//        SimplePdfExporterConfiguration config = new SimplePdfExporterConfiguration();
//        
//        exporter.setConfiguration(config);
//
//        exporter.exportReport();
//
//        JOptionPane.showMessageDialog(null, "Report exported to " + path);
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
//    }
//}



}
