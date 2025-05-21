/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;
import java.util.ArrayList;
import java.util.List;
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
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
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
                        parts[1].trim(),
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
                String amount = individualsales.getAmount();
                String qtyStr = individualsales.getQuantity();
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
    public void updateindividualsales(String salesid, String itemid, String quantity, String retailprice, String amount, String dateofsales) {
        IndividualSales existingsales= findsalesid(salesid);
        if (existingsales != null) {
            String oldItemId = existingsales.getItemid();
            String oldDate = existingsales.getDateofsales();
            String oldAmount = existingsales.getAmount();
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
        String amount = sales.getAmount();

        boolean updated=false;

        for(DailySales dailysales : dailysaleslist){
            if (dailysales.getItemid().equals(itemid) && dailysales.getDateofsales().equals(date)) {
                double currentTotalSales = Double.parseDouble(dailysales.getTotalsales());
                double newTotalSales = currentTotalSales + Double.parseDouble(amount);
                String formattedCurrentTotal = String.format("%.2f", currentTotalSales);
                String formattedNewTotal = String.format("%.2f", newTotalSales);
                
                dailysales.setTotalsales(formattedNewTotal);
                String oldLine = itemid + "," + formattedCurrentTotal + "," + date;
                String newLine = itemid + "," + formattedNewTotal + "," + date;
                textfile.replaceLine(dailysalesfilepath, oldLine, newLine); 

                updated = true;
                break;
            }
        }
        if(!updated){
            String formattedAmount = String.format("%.2f", Double.parseDouble(amount));
            String newentry = itemid + "," + formattedAmount + "," + date;
            dailysaleslist.add(new DailySales(itemid, formattedAmount, date)); 
            textfile.appendTo(dailysalesfilepath,newentry);
        }
    }

    private void updatedailysummaryAfterDelete(IndividualSales sales) {
        String itemid = sales.getItemid();
        String date = sales.getDateofsales();
        String amount = sales.getAmount();

        for (int i = 0; i < dailysaleslist.size(); i++) {
            DailySales dailysales = dailysaleslist.get(i);
            if (dailysales.getItemid().equals(itemid) && dailysales.getDateofsales().equals(date)) {
                double currentTotalSales = Double.parseDouble(dailysales.getTotalsales());
                double newTotalSales = currentTotalSales - Double.parseDouble(amount);
                String formattedCurrentTotal = String.format("%.2f", currentTotalSales);
                String formattedNewTotal = String.format("%.2f", newTotalSales);
                
                String oldLine = itemid + "," + formattedCurrentTotal + "," + date;

                if (newTotalSales <= 0.00) {
                    dailysaleslist.remove(i);
                    textfile.deleteLine(dailysalesfilepath, oldLine);
                    System.out.println("Daily summary entry removed completely");
                } else {
                    dailysales.setTotalsales(formattedNewTotal);
                    String newLine = itemid + "," + formattedNewTotal + "," + date;
                    textfile.replaceLine(dailysalesfilepath, oldLine, newLine);
                    System.out.println("Daily summary updated after delete");
                }
                break;
            }
        }
    }
    private void updatedailysummaryAfterUpdate(String oldItemid, String oldDate, String oldAmount,String newItemid, String newDate, String newAmount) {
        double oldAmt = Double.parseDouble(oldAmount);
        double newAmt = Double.parseDouble(newAmount);
        boolean oldUpdated = false;
        boolean newUpdated = false;

        for (DailySales dailysales : dailysaleslist) {
            if (dailysales.getItemid().equals(oldItemid) && dailysales.getDateofsales().equals(oldDate)) {
                double currentTotal = Double.parseDouble(dailysales.getTotalsales());
                double newTotal = currentTotal - oldAmt;
                
                String formattedCurrentTotal = String.format("%.2f", currentTotal);
                String formattedNewTotal = String.format("%.2f", newTotal);

                String oldLine = oldItemid + "," + formattedCurrentTotal + "," + oldDate;
                if (newTotal <= 0.00) {
                    dailysaleslist.remove(dailysales);
                    textfile.deleteLine(dailysalesfilepath, oldLine);
                } else {
                    dailysales.setTotalsales(formattedNewTotal);
                    String newLine = oldItemid + "," + formattedNewTotal + "," + oldDate;
                    textfile.replaceLine(dailysalesfilepath, oldLine, newLine);
                }

                oldUpdated = true;
                break;
            }
        }
        for (DailySales dailysales : dailysaleslist) {
            if (dailysales.getItemid().equals(newItemid) && dailysales.getDateofsales().equals(newDate)) {
                double currentTotal = Double.parseDouble(dailysales.getTotalsales());
                double newTotal = currentTotal + newAmt;
                
                String formattedCurrentTotal = String.format("%.2f", currentTotal);
                String formattedNewTotal = String.format("%.2f", newTotal);

                String oldLine = newItemid + "," + formattedCurrentTotal + "," + newDate;
                String newLine = newItemid + "," + formattedNewTotal + "," + newDate;
                dailysales.setTotalsales(formattedNewTotal);
                textfile.replaceLine(dailysalesfilepath, oldLine, newLine);

                newUpdated = true;
                break;
            }
        }

        if (!newUpdated) {
            String formattedNewAmount = String.format("%.2f", Double.parseDouble(newAmount));
            DailySales newEntry = new DailySales(newItemid, formattedNewAmount, newDate);
            dailysaleslist.add(newEntry);
            textfile.appendTo(dailysalesfilepath, newItemid + "," + formattedNewAmount + "," + newDate);
        }

        System.out.println("Daily summary updated after individual sales edit.");
    }
}
