package java_assignment2025;


import java.util.ArrayList;
import java.util.List;
import java_assignment2025.Item;
import java_assignment2025.Supplier;
import java_assignment2025.TextFile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
public class SupplierDataManager {
    private final List<Supplier>supplierlist;
    private final TextFile textfile;
    private final String supplierfilepath = "C:\\Users\\Isaac\\OneDrive - Asia Pacific University\\Documents\\NetBeansProjects\\java-assignment\\java-assignment\\src\\java_assignment2025\\supplier.txt";
    
    public SupplierDataManager() {
        this.supplierlist = new ArrayList<>();
        this.textfile = new TextFile();
        loadAllsupplierfromtxtfile();
    }
    
        public void loadAllsupplierfromtxtfile(){
        List<String> lines = textfile.readFile(supplierfilepath);
        for(String line : lines){
            String[] parts = line.split(",", 7);
            if(parts.length == 7){
                supplierlist.add(new Supplier( // one by one add to list
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        Boolean.parseBoolean(parts[6].trim())
                ));
            }
        }
    }

    public String getsupplierfilepath(){
        return supplierfilepath;
    }
    public List<Supplier> getsupplierlist(){
        return supplierlist;
    }
    public String generateSupplierId(){
        int max = 0;
        for (Supplier supplier : supplierlist){
            String supplierid = supplier.getSupplierid();
            int num = Integer.parseInt(supplierid.replace("SUP", ""));
            if (num > max){
                max = num;
            }
        }
        return String.format("SUP%03d", max+1);
    }
    
    public void addSupplier(Supplier supplier){
        if(!duplicatedsupplier(supplier)){
            supplierlist.add(supplier); //add a list here
            System.out.println("Appending supplier: " + supplier.toString());
            textfile.appendTo(supplierfilepath, supplier.toString());
            System.out.println("add supplier successfuly ya");
        }else{
            System.out.println("supplier exist already ya");
        }
    }
    public void deleteSupplier(String supplierid){
        Supplier supplier = findsupplierid(supplierid);
            if (supplier != null){
                supplierlist.remove(supplier);
                textfile.deleteLine(supplierfilepath, supplier.toString());
                System.out.println("delete successful");
                return;
            }else{
                System.out.println("supplier not found");
            } 
    }
    public void updateSupplier(Supplier oldsupplier, Supplier newsupplier){
        if(supplierlist.contains(oldsupplier)){
            supplierlist.remove(oldsupplier);
            supplierlist.add(newsupplier);
            textfile.replaceLine(supplierfilepath, oldsupplier.toString(), newsupplier.toString());
            System.out.println("successful edit");
        }else{
            System.out.println("failed to edit");
        }
    }
    
    private boolean duplicatedsupplier(Supplier supplier){
        for (Supplier existsupplier : supplierlist){
            if (existsupplier.getSupplierid().equals(supplier.getSupplierid())){
                return true;
            }
        }
        return false;
    }
public Supplier findsupplierid(String supplierId) {
    for (Supplier supplier : supplierlist) {
        if (supplier.getSupplierid().equals(supplierId)) {
            return supplier;
        }
    }
    return null;
}

    
    
}

