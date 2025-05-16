/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author User
 */
public class PurchaseRequisitionManager {
   private final List<PurchaseRequisition>prlist;
    private final TextFile textfile;
    private final String prfilepath = "C:\\Users\\hew\\OneDrive - Asia Pacific University\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\purchaserequisition.txt";
    
    public PurchaseRequisitionManager() {
        this.prlist = new ArrayList<>();
        this.textfile = new TextFile();
        loadAllprfromtxtfile();
    }
    
    public void loadAllprfromtxtfile() {
        List<String> lines = textfile.readFile(prfilepath);
        for (String line : lines) {
            String[] parts = line.split(",", 10);
            if (parts.length == 10) {
                List<String> itemids = Arrays.asList(parts[1].trim().split("\\|"));
                List<String> quantities = Arrays.asList(parts[3].trim().split("\\|"));
                List<String> unitPrices = Arrays.asList(parts[4].trim().split("\\|"));
                prlist.add(new PurchaseRequisition(
                        parts[0].trim(),
                        itemids, // new ArrayList<>(itemids),
                        parts[2].trim(),
                        unitPrices,
                        quantities,
                        parts[5].trim(),
                        parts[6].trim(),
                        parts[7].trim(),
                        PurchaseRequisition.ApproveStatus.fromString(parts[8].trim()),
                        parts[9].trim()
                ));
            }
        }
    }




    public String getprfilepath(){
        return prfilepath;
    }
    public List<PurchaseRequisition> getprlist(){
        return prlist;
    }
    public String generatePrId(){
        int max = 0;
        for (PurchaseRequisition PurchaseRequisition : prlist){
            String prid = PurchaseRequisition.getPrid();
            int num = Integer.parseInt(prid.replace("PR", ""));
            if (num > max){
                max = num;
            }
        }
        return String.format("PR%03d", max+1);
    }
    
    public void addpr(PurchaseRequisition pr){
        if(!duplicatedpr(pr)){
            prlist.add(pr); //add a list here
            System.out.println("Appending pr: " + pr.toString());
            textfile.appendTo(prfilepath, pr.toString());
            System.out.println("add pr successfuly ya");
        }else{
            System.out.println("pr exist already ya");
        }
    }
    public void deletepr(String prid){
        PurchaseRequisition pr = findprid(prid);
            if (pr != null){
                prlist.remove(pr);
                textfile.deleteLine(prfilepath, pr.toString());
                System.out.println("delete successful");
                return;
            }else{
                System.out.println("pr not found");
            } 
    }
    public void updatepr(String prid, List<String> itemids, String userid, List<String> quantities,List<String> unitprices, String total,String requestdate,String expecteddeliverydate,PurchaseRequisition.ApproveStatus status,String note) {
        PurchaseRequisition existingpr= findprid(prid);
        if (existingpr != null) {
            existingpr.setPrid(prid);
            existingpr.setItemids(itemids);
            existingpr.setUserid(userid);
            existingpr.setQuantitiesList(quantities);
            existingpr.setUnitPrices(unitprices);
            existingpr.setTotal(total);
            existingpr.setRequestdate(requestdate);
            existingpr.setExpecteddeliverydate(expecteddeliverydate);
            existingpr.setApprovestatus(status);
            existingpr.setNote(note);
            textfile.rewriteFile(prfilepath, prlist);
            System.out.println("updated successfully.");
        } else {
            System.out.println("not found.");
        }
    }
    
//        private String prid;
//    private List<String> itemids;
//    private String userid;
//    private List<String> quantities;
//    private String total;
//    private String requestdate;
//    private String expecteddeliverydate;
//    private ApproveStatus approvestatus;
//    private String note;
//    
    private boolean duplicatedpr(PurchaseRequisition pr){
        for (PurchaseRequisition existpr : prlist){
            if (existpr.getPrid().equals(pr.getPrid())){
                return true;
            }
        }
        return false;
    }
    public PurchaseRequisition findprid(String prid) {
        for (PurchaseRequisition pr : prlist) {
            if (pr.getPrid().equals(prid)) {
                return pr;
            }
        }
        return null;
    }

   public double calculatetotalprice(List<PRItem> pritemlist){
        double totalprice = 0.0;
        for (PRItem pritem : pritemlist){
            try{
                totalprice += Double.parseDouble(pritem.getTotalprice());
            }catch(NumberFormatException e){
                System.out.println("invalid price format in PRitem");
            }
        }
        return totalprice;
    }
    
}


