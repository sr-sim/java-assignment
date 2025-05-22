/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
/**
 *
 * @author User
 */
public class SalesDataManager {
    private final List<IndividualSales>individualsaleslist;
    private final List<DailySales>dailysaleslist;
    private final TextFile textfile;
    private final InventoryDataManager inventorydatamanager = new InventoryDataManager();
    private final String individualsalesfilepath = "src/java_assignment2025/sales.txt";
    private final String dailysalesfilepath = "src/java_assignment2025/dailysales.txt";
    
    public SalesDataManager() {
        this.individualsaleslist = new ArrayList<>();
        this.dailysaleslist = new ArrayList<>();
        this.textfile = new TextFile();
        loadAllindividualsalesfromtxtfile();
        loadAlldailysalesfromtxtfile();
    }
    public List<IndividualSales> getindividualsaleslist(){
        return individualsaleslist;
    }
    public List<DailySales> getdailysaleslist(){
        return dailysaleslist;
    }
    public void loadAllindividualsalesfromtxtfile(){
        List<String> lines = textfile.readFile(individualsalesfilepath);
        for(String line : lines){
            String[] parts = line.split(",", 6);
            if(parts.length == 6){
                individualsaleslist.add(new IndividualSales( // one by one add to list
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        Double.parseDouble(parts[3].trim()),
                        Double.parseDouble(parts[4].trim()),
                        parts[5].trim()
                ));
            }
        }
        
    }
    public void loadAlldailysalesfromtxtfile(){
        List<String> lines = textfile.readFile(dailysalesfilepath);
        for(String line : lines){
            String[] parts = line.split(",", 3);
            if(parts.length == 3){
                dailysaleslist.add(new DailySales( // one by one add to list
                        parts[0].trim(),
                        Double.parseDouble(parts[1].trim()),
                        parts[2].trim()
                ));
            }
        }
        
    }
    public String getindividualsalesfilepath(){
        return individualsalesfilepath;
    }
    public String generatesalesId(){
        int max = 0;
        for (IndividualSales sales : individualsaleslist){
            String salesid = sales.getSalesid();
            int num = Integer.parseInt(salesid.replace("S", ""));
            if (num > max){
                max = num;
            }
        }
        return String.format("S%03d", max+1);
    }
    
    public void addIndividualSales(IndividualSales sales){
        if(!duplicatedindividualsales(sales)){
            individualsaleslist.add(sales); //add a individual sales here
            System.out.println("Appending individual sales: " + sales.toString());
            textfile.appendTo(individualsalesfilepath, sales.toString());
            System.out.println("add individual sales successfuly ya");
            updatedailysummary(sales);
        }else{
            System.out.println("individual sales exist already ya");
        }
    }
    public void deleteindividualsales(String salesid){
        IndividualSales individualsales = findsalesid(salesid);
            if (individualsales != null){
                String itemid = individualsales.getItemid();
                String date = individualsales.getDateofsales();
                double amount = individualsales.getAmount();
                int qtyStr = individualsales.getQuantity();
                individualsaleslist.remove(individualsales);
                textfile.deleteLine(individualsalesfilepath, individualsales.toString());
                System.out.println("delete successful");
                updatedailysummaryAfterDelete(individualsales);
            }else{
                System.out.println("individualsales not found");
            } 
    }

//    public void updateindividualsales(IndividualSales oldindividualsales, IndividualSales newindividualsales){
//        if(individualsaleslist.contains(oldindividualsales)){
//            String oldItemId = oldindividualsales.getItemid();
//            String oldDate = oldindividualsales.getDateofsales();
//            String oldAmount = oldindividualsales.getAmount();
//
//            String newItemId = newindividualsales.getItemid();
//            String newDate = newindividualsales.getDateofsales();
//            String newAmount = newindividualsales.getAmount();
//            individualsaleslist.remove(oldindividualsales);
//            individualsaleslist.add(newindividualsales);
//            textfile.replaceLine(individualsalesfilepath, oldindividualsales.toString(), newindividualsales.toString());
//            System.out.println("Updating IndividualSales: " + oldindividualsales.getSalesid() + " to " + newindividualsales.getSalesid());
//            System.out.println("successful edit");
//            updatedailysummaryAfterUpdate(oldItemId, oldDate, oldAmount, newItemId, newDate, newAmount);
//        }else{
//            System.out.println("failed to edit");
//        }
//    }
    public void updateindividualsales(String salesid, String itemid, int quantity, double retailprice, double amount, String dateofsales) {
        IndividualSales existingsales= findsalesid(salesid);
        if (existingsales != null) {
            String oldItemId = existingsales.getItemid();
            String oldDate = existingsales.getDateofsales();
            double oldAmount = existingsales.getAmount();
            existingsales.setretailprice(retailprice);
            existingsales.setSalesid(salesid);
            existingsales.setItemid(itemid);
            existingsales.setQuantity(quantity);
            existingsales.setAmount(amount);
            existingsales.setDateofsales(dateofsales);
            textfile.rewriteFile(individualsalesfilepath, individualsaleslist);
            System.out.println("individual sales updated successfully.");
            updatedailysummaryAfterUpdate(oldItemId, oldDate, oldAmount, itemid, dateofsales, amount);
        } else {
            System.out.println("individual sales not found.");
        }
    }
//        private String salesid;
//    private String itemid;
//    private String quantity;
//    private String amount;
//    private String dateofsales;
    
    private boolean duplicatedindividualsales(IndividualSales individualsales){
        for (IndividualSales existsales : individualsaleslist){
            if (existsales.getSalesid().equals(individualsales.getSalesid())){
                return true;
            }
        }
        return false;
    }
    public IndividualSales findsalesid(String salesid){
        for (IndividualSales sales : individualsaleslist){
            if(sales.getSalesid().equals(salesid)){
                return sales;
            }
        }
        return null;
    }
    
    public void updatedailysummary(IndividualSales sales){
        String itemid = sales.getItemid();
        String date = sales.getDateofsales();
        double amount = sales.getAmount();

        boolean updated=false;

        for(DailySales dailysales : dailysaleslist){
            if (dailysales.getItemid().equals(itemid) && dailysales.getDateofsales().equals(date)) {
                double currentTotalSales = dailysales.getTotalsales();
                double newTotalSales = currentTotalSales + amount;
//                String formattedCurrentTotal = String.format("%.2f", currentTotalSales);
//                String formattedNewTotal = String.format("%.2f", newTotalSales);
                
                dailysales.setTotalsales(newTotalSales);
                String oldLine = itemid + "," + currentTotalSales + "," + date;
                String newLine = itemid + "," + newTotalSales + "," + date;
                textfile.replaceLine(dailysalesfilepath, oldLine, newLine); 

                updated = true;
                break;
            }
        }
        if(!updated){
//            String formattedAmount = String.format("%.2f", Double.parseDouble(amount));
            String newentry = itemid + "," + amount + "," + date;
            dailysaleslist.add(new DailySales(itemid, amount, date)); 
            textfile.appendTo(dailysalesfilepath,newentry);
        }
    }

    private void updatedailysummaryAfterDelete(IndividualSales sales) {
        String itemid = sales.getItemid();
        String date = sales.getDateofsales();
        double amount = sales.getAmount();

        for (int i = 0; i < dailysaleslist.size(); i++) {
            DailySales dailysales = dailysaleslist.get(i);
            if (dailysales.getItemid().equals(itemid) && dailysales.getDateofsales().equals(date)) {
                double currentTotalSales = dailysales.getTotalsales();
                double newTotalSales = currentTotalSales - amount;
//                String formattedCurrentTotal = String.format("%.2f", currentTotalSales);
//                String formattedNewTotal = String.format("%.2f", newTotalSales);
                
                String oldLine = itemid + "," + currentTotalSales + "," + date;

                if (newTotalSales <= 0.00) {
                    dailysaleslist.remove(i);
                    textfile.deleteLine(dailysalesfilepath, oldLine);
                    System.out.println("Daily summary entry removed completely");
                } else {
                    dailysales.setTotalsales(newTotalSales);
                    String newLine = itemid + "," + newTotalSales + "," + date;
                    textfile.replaceLine(dailysalesfilepath, oldLine, newLine);
                    System.out.println("Daily summary updated after delete");
                }
                break;
            }
        }
    }
    private void updatedailysummaryAfterUpdate(String oldItemid, String oldDate, double oldAmount,String newItemid, String newDate, double newAmount) {
        double oldAmt = oldAmount;
        double newAmt = newAmount;
        boolean oldUpdated = false;
        boolean newUpdated = false;

        for (DailySales dailysales : dailysaleslist) {
            if (dailysales.getItemid().equals(oldItemid) && dailysales.getDateofsales().equals(oldDate)) {
                double currentTotal = dailysales.getTotalsales();
                double newTotal = currentTotal - oldAmt;
//                
//                String formattedCurrentTotal = String.format("%.2f", currentTotal);
//                String formattedNewTotal = String.format("%.2f", newTotal);

                String oldLine = oldItemid + "," + currentTotal + "," + oldDate;
                if (newTotal <= 0.00) {
                    dailysaleslist.remove(dailysales);
                    textfile.deleteLine(dailysalesfilepath, oldLine);
                } else {
                    dailysales.setTotalsales(newTotal);
                    String newLine = oldItemid + "," + newTotal + "," + oldDate;
                    textfile.replaceLine(dailysalesfilepath, oldLine, newLine);
                }

                oldUpdated = true;
                break;
            }
        }
          if (!oldItemid.equals(newItemid) || !oldDate.equals(newDate)) {
            // Handle adding to a different item/date
            boolean found = false;
            for (DailySales dailysales : dailysaleslist) {
                if (dailysales.getItemid().equals(newItemid) && dailysales.getDateofsales().equals(newDate)) {
                    double currentTotal = dailysales.getTotalsales();
                    double newTotal = currentTotal + newAmt;

                    String oldLine = newItemid + "," + currentTotal + "," + newDate;
                    String newLine = newItemid + "," + newTotal + "," + newDate;

                    dailysales.setTotalsales(newTotal);
                    textfile.replaceLine(dailysalesfilepath, oldLine, newLine);
                    found = true;
                    break;
                }
            }
            if (!found) {
                String newEntry = newItemid + "," + String.format("%.2f", newAmt) + "," + newDate;
                dailysaleslist.add(new DailySales(newItemid, newAmt, newDate));
                textfile.appendTo(dailysalesfilepath, newEntry);
            }
        }
        for (DailySales dailysales : dailysaleslist) {
            if (dailysales.getItemid().equals(newItemid) && dailysales.getDateofsales().equals(newDate)) {
                double currentTotal = dailysales.getTotalsales();
                double newTotal = currentTotal + newAmt;
//                
//                String formattedCurrentTotal = String.format("%.2f", currentTotal);
//                String formattedNewTotal = String.format("%.2f", newTotal);

                String oldLine = newItemid + "," + currentTotal + "," + newDate;
                String newLine = newItemid + "," + newTotal + "," + newDate;
                dailysales.setTotalsales(newTotal);
                textfile.replaceLine(dailysalesfilepath, oldLine, newLine);

                newUpdated = true;
                break;
            }
        }

        if (!newUpdated) {
//            String formattedNewAmount = String.format("%.2f", Double.parseDouble(newAmount));
            DailySales newEntry = new DailySales(newItemid, newAmt, newDate);
            dailysaleslist.add(newEntry);
            textfile.appendTo(dailysalesfilepath, newItemid + "," + newAmt + "," + newDate);
        }

        System.out.println("Daily summary updated after individual sales edit.");
    }
}
