/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_assignment2025;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.swing.table.TableColumn;
/**
 *
 * @author hew
 */
public class IM_VerifyPo extends javax.swing.JFrame {
    private InventoryManager inventorymanager;
    private SupplierDataManager supplierdatamanager;
    private PurchaseOrderManager pomanager ;
    private InventoryDataManager inventorydatamanager;
    private PartialReceivedDataManager partialReceivedDataManager; // New manager
    private List<PartialReceived> partialReceiveds;
    
    /**
     * Creates new form IM_VerifyPo
     */
    public IM_VerifyPo(InventoryManager inventorymanager,PurchaseOrderManager pomanager,InventoryDataManager inventorydatamanager, SupplierDataManager supplierdatamanager,PartialReceivedDataManager partialReceivedDataManager) {
   
            this.inventorymanager = (InventoryManager)Session.getCurrentUser();
            this.inventorydatamanager = inventorydatamanager;
            this.supplierdatamanager = supplierdatamanager;
            this.pomanager = pomanager;
            this.partialReceivedDataManager = partialReceivedDataManager; 
            // Reload data to ensure consistency
            this.pomanager.loadAllpofromtxtfile();
            this.partialReceivedDataManager.loadAllPartialReceiveds();
            this.partialReceiveds = this.partialReceivedDataManager.getPartialReceiveds();
             initComponents();
             setupTable();
            
    }
    private void setupTable() {
        if (jTable1 == null || jTable2 == null) {
            JOptionPane.showMessageDialog(this, "Table initialization failed. Check GUI setup in NetBeans.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Order Id", "Supplier Id", "Item Id", "Item Name", "Quantity", "Action"}
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Action column is editable
            }
        };
        
        jTable1.setModel(model);
        jTable1.setRowHeight(30); // Adjust for single-line height
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); // Allow last column to expand
        jTable1.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
        
        // Set up ActionButtonEditor for Action column (index 5)
        ActionButtonEditor buttonEditor = new ActionButtonEditor(jTable1, inventorydatamanager, true, pomanager, partialReceivedDataManager, jTable1, jTable2);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(buttonEditor);
        jTable1.getColumnModel().getColumn(5).setCellEditor(buttonEditor);
        

        // Populate the table
        fillTable1FromTxtFile(jTable1, inventorydatamanager);
        autoAdjustColumnWidths(jTable1); // Adjust columns after filling
        jTable1.clearSelection();
        jTable1.repaint(); // Force repaint to apply new sizes
        jTable1.revalidate(); // Added: Ensure layout is updated after population
        
        // Setup jTable2 (RemainReceiveItemTable)
        DefaultTableModel model2 = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Order Id", "Supplier Id", "Item Id", "Item Name", "Remain Quantity", "Action"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
      
        jTable2.setModel(model2);
        jTable2.setRowHeight(30);
        jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        jTable2.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
        ActionButtonEditor remainEditor = new ActionButtonEditor(jTable2, inventorydatamanager, false, pomanager, partialReceivedDataManager, jTable1, jTable2);
        jTable2.getColumnModel().getColumn(5).setCellRenderer(remainEditor);
        jTable2.getColumnModel().getColumn(5).setCellEditor(remainEditor);
        fillTable2FromPartialReceiveds(jTable2, inventorydatamanager);   
        autoAdjustColumnWidths(jTable2);
        jTable2.clearSelection();
        jTable2.revalidate(); // Added: Ensure layout is updated after population
        jTable2.repaint();
    }
    
    private void autoAdjustColumnWidths(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = 50; // Minimum width
            int maxWidth = 300; // Maximum width

            // Calculate width based on header
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                table, tableColumn.getHeaderValue(), false, false, 0, column);
            preferredWidth = Math.max(preferredWidth, headerComp.getPreferredSize().width+10);

            // Calculate width based on cell content (up to 10 rows)
            for (int row = 0; row < Math.min(table.getRowCount(), 10); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                preferredWidth = Math.max(preferredWidth, comp.getPreferredSize().width + 20);
            }

            // Adjust for Action column (ensure buttons fit)
            if (column == 5) {
                preferredWidth = Math.max(preferredWidth, 200); // Ensure enough space for buttons
               
            }

            tableColumn.setPreferredWidth(Math.min(preferredWidth, maxWidth));
        }
    }
    public void fillTable1FromTxtFile(JTable jTable1, InventoryDataManager inventorydatamanager) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        // Verify column count
        if (model.getColumnCount() != 6) {
            System.err.println("Warning: jTable1 model has " + model.getColumnCount() + " columns, resetting to correct model");
            model = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Order Id", "Supplier Id", "Item Id", "Item Name", "Quantity", "Action"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5;
                }
            };
            jTable1.setModel(model);
        }
        model.setRowCount(0);
        
        try {
            List<PurchaseOrder> poList = pomanager.getpolist();
            if (poList == null || poList.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No purchase orders found in " + pomanager.getpofilepath() + " (File does not exist or is empty)", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (PurchaseOrder po : pomanager.getpolist()) {
                if (po.getOrderStatus().equalsIgnoreCase("approved") &&
                    po.getVerifyStatus().equalsIgnoreCase("pending")) {
                    List<String> itemIds = po.getItemIds();
                    List<String> quantities = po.getQuantities();
                    List<String> supplierIds = po.getSupplierIds();
                    List<String> itemNames = new ArrayList<>();
                    for (String itemId : itemIds) {
                        Item item = inventorydatamanager.finditemid(itemId);
                        itemNames.add(item != null ? item.getItemname() : "Unknown Item (" + itemId + ")");
                    }
                    int maxSize = Math.min(itemIds.size(), Math.min(quantities.size(), Math.min(supplierIds.size(), itemNames.size())));
                    for (int i = 0; i < itemIds.size(); i++) {
                        Object actionValue = (i == itemIds.size() - 1) ? "" : null;
                        Object[] row = {
                            i == 0 ? po.getOrderId() : "",
                            i < supplierIds.size() ? supplierIds.get(i) : "",
                            i < itemIds.size() ? itemIds.get(i) : "",
                            i < itemNames.size() ? itemNames.get(i) : "",
                            i < quantities.size() ? quantities.get(i) : "",
                            actionValue
                        };
                        model.addRow(row);
                       
                    }
                }
            }
            model.fireTableDataChanged();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading purchase orders from " + pomanager.getpofilepath() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        jTable1.revalidate();
        jTable1.repaint();
    }
    
    private void fillTable2FromPartialReceiveds(JTable table, InventoryDataManager inventoryDataManager) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);
    List<PartialReceived> partialReceivedList = partialReceivedDataManager.getPartialReceiveds();
    for (PartialReceived pr : partialReceivedList) {
        String orderId = pr.getOrderId();
        PurchaseOrder po = pomanager.findpoid(orderId);
        if (po != null && po.getVerifyStatus().equals("partial received")) {
            List<String> itemIds = pr.getItemIds();
            List<String> itemNames = pr.getItemNames();
            List<Integer> remainingQuantities = pr.getRemainingQuantities();
            String supplierIds = String.join("|", po.getSupplierIds());
            if (itemIds.size() == itemNames.size() && itemIds.size() == remainingQuantities.size()) {
                for (int i = 0; i < itemIds.size(); i++) {
                    String displayOrderId = (i == 0) ? orderId : "";
                    String displaySupplierId = (i == 0) ? supplierIds : "";
                    String actionValue = (i == itemIds.size() - 1) ? orderId : "";

                    model.addRow(new Object[]{
                        displayOrderId,
                        displaySupplierId,
                        itemIds.get(i),
                        itemNames.get(i),
                        remainingQuantities.get(i),
                        actionValue
                    });
                }
            }else{
                System.err.println("Invalid PartialReceived data for PO " + orderId + ": Mismatched list sizes");
            }
        }else{
            System.err.println("Skipping PO " + orderId + ": Not found or not partial received");
        }
    }
    model.fireTableDataChanged();
    table.clearSelection();
    table.revalidate();
    table.repaint();
    }
    private class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                  boolean hasFocus, int row, int column) {
            if (value == null) {
                setText("");
            } else {
                setText(value.toString());
            }
            // Ensure text wraps within column width
            setColumns(table.getColumnModel().getColumn(column).getWidth() / 
            getFontMetrics(getFont()).charWidth('m'));
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            setFont(table.getFont());
            return this;
        }
    }
    // Inner class for Action column buttons TableCellRenderer,
    private class ActionButtonEditor extends AbstractCellEditor implements  TableCellRenderer, TableCellEditor {
        private JPanel rendererPanel; // Separate panel for renderer
        private JPanel editorPanel;   // Separate panel for editor
        private final JButton receivedButton;
        private final JButton partialReceivedButton;
        private final JTable table;
        private final InventoryDataManager inventorydatamanager;
        private final boolean isMainTable;
        private String orderId;
        private Object currentValue;
       private final PurchaseOrderManager pomanager ;
        private final PartialReceivedDataManager partialReceivedDataManager; // Added
        private final JTable jTable1; // Added
        private final JTable jTable2; // Added

            public ActionButtonEditor(JTable table, InventoryDataManager inventorydatamanager, boolean isMainTable,
                    PurchaseOrderManager pomanager, PartialReceivedDataManager partialReceivedDataManager,
                                  JTable jTable1, JTable jTable2) {
            this.table = table;
            this.inventorydatamanager = inventorydatamanager;
            this.isMainTable = isMainTable;
            this.pomanager = pomanager; // This assignment will now work
            this.jTable1 = jTable1;
            this.jTable2 = jTable2;
            this.partialReceivedDataManager = partialReceivedDataManager;
            rendererPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
            editorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
            receivedButton = new JButton("Received");
            partialReceivedButton = isMainTable ? new JButton("Partial") : null;

            receivedButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            receivedButton.setMargin(new Insets(1, 2, 1, 2));
            if (partialReceivedButton != null) {
                partialReceivedButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                partialReceivedButton.setMargin(new Insets(1, 2, 1, 2));
            }

            receivedButton.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row >= 0 && row < table.getRowCount()) {
                        
                        if (isMainTable) {
                            orderId = getOrderIdForRow(row); // Use getOrderIdForRow for jTable1
                        } else {
                            orderId = (String) table.getModel().getValueAt(row, 5); // Use column 5 for jTable2
                        }
                        if (orderId == null || orderId.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Cannot determine Order ID for this row", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (isMainTable) {
                            updateReceiveStatus("order received");
                        } else {
                            
                            completePartialReceived();
                        }
                        fireEditingCanceled();
                        table.clearSelection();
                        table.revalidate();
                        table.repaint();
                    }
            });
            if (isMainTable && partialReceivedButton != null) {
            partialReceivedButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0 && row < table.getRowCount()) {
                    orderId = getOrderIdForRow(row);
                    if (orderId.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Cannot determine Order ID for this row", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    showPartialReceivedPanel();
                    fireEditingCanceled();
                    table.clearSelection();
                    table.revalidate();
                    table.repaint();
                    
                }
            });
        }
    
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            rendererPanel.removeAll();
            rendererPanel.setVisible(false);

            // Ensure row and column are valid
            if (row < 0 || row >= table.getRowCount() || column != 5) {
                return rendererPanel; // Return empty panel for invalid rows or non-Action columns
            }

            Object modelValue = table.getModel().getValueAt(row, column);

            if (isMainTable) { // jTable1
                if (modelValue instanceof String && ((String) modelValue).equals("")) {
                    rendererPanel.add(receivedButton);
                    if (partialReceivedButton != null) {
                        rendererPanel.add(partialReceivedButton);
                    }
                    orderId = getOrderIdForRow(row);
                    PurchaseOrder po = pomanager.findpoid(orderId);
                    boolean isEnabled = po != null && po.getVerifyStatus().equals("pending");

                    receivedButton.setEnabled(isEnabled);
                    if (partialReceivedButton != null) {
                        partialReceivedButton.setEnabled(isEnabled);
                    }
                    rendererPanel.setVisible(true);
                }
            } else { // jTable2
                if (modelValue instanceof String && !((String) modelValue).isEmpty()) {
                    orderId = (String) modelValue;
                    PurchaseOrder po = pomanager.findpoid(orderId);
                    boolean isEnabled = po != null && po.getVerifyStatus().equals("partial received") &&
                                   partialReceivedDataManager.getPartialReceiveds().stream()
                                       .anyMatch(r -> r.getOrderId().equals(orderId));

                    
                    receivedButton.setEnabled(isEnabled);
                    rendererPanel.add(receivedButton);
                    rendererPanel.setVisible(true);
                }
            }

            rendererPanel.revalidate();
            rendererPanel.repaint();
            return rendererPanel;
        }
         
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                editorPanel.removeAll();
                editorPanel.setVisible(false);
                if (row < 0 || row >= table.getRowCount()) {
                            return editorPanel;
                }
                currentValue = value;
               
                if (isMainTable) {
                    if (value instanceof String && ((String) value).equals("")) {
                        editorPanel.add(receivedButton);
                        if (partialReceivedButton != null) {
                            editorPanel.add(partialReceivedButton);
                        }
                        editorPanel.setVisible(true);
                    }
                } else {
                    if (value instanceof String && !((String) value).isEmpty()) {
                        editorPanel.add(receivedButton);
                        editorPanel.setVisible(true);
                    }
                }

                editorPanel.revalidate();
                editorPanel.repaint();
                return editorPanel;
            }

        //        private boolean isLastRowOfPOGroup(int row) {
        //        String currentOrderId = getOrderIdForRow(row);
        //        if (currentOrderId == null || currentOrderId.isEmpty()) {
        //            return false;
        //        }
        //        // Check the next row
        //        if (row + 1 < table.getRowCount()) {
        //            String nextOrderId = getOrderIdForRow(row + 1);
        //            return !currentOrderId.equals(nextOrderId) || nextOrderId.isEmpty();
        //        }
        //        return true; // Last row of the table is always considered the last row of the group
        //    }



            @Override
            public Object getCellEditorValue() {
            // Return the original cell value to prevent overwriting
            return currentValue;
            }
            private String getOrderIdForRow(int row) {
                if (row < 0 || row >= table.getRowCount()) {
                    return "";
                }
                
                for (int i = row; i >= 0; i--) {
                    String orderId = (String) table.getModel().getValueAt(i, 0);
                    if (orderId != null && !orderId.isEmpty()) {
                        return orderId;
                    }
                }
                return "";
            }
        
    


            private void updateReceiveStatus(String status) {
            PurchaseOrder po = pomanager.findpoid(orderId);
            if (po != null) {
                if (!po.getVerifyStatus().equals("pending")) {
                    JOptionPane.showMessageDialog(IM_VerifyPo.this, "Status already set to " + po.getVerifyStatus(), "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                List<String> itemIds = po.getItemIds();
                List<String> quantities = po.getQuantities();
                for (int i = 0; i < itemIds.size(); i++) {
                    Item item = inventorydatamanager.finditemid(itemIds.get(i));
                    if (item != null) {
                        int newQuantity = item.getInstockquantity() + Integer.parseInt(quantities.get(i));
                        inventorydatamanager.updateItem(
                            item.getItemid(), item.getItemname(), item.getItemdesc(), item.getSupplierid(),
                            item.getUnitprice(), item.getRetailprice(), newQuantity,
                            item.getReorderlevel(), LocalDate.now().toString(),
                            item.isDeleted()
                        );
                    }
                }
                pomanager.updateReceiveStatus(orderId, status);
                JOptionPane.showMessageDialog(IM_VerifyPo.this, "Receive status updated to " + status, "Success", JOptionPane.INFORMATION_MESSAGE);
                fillTable1FromTxtFile(jTable1, inventorydatamanager);
                fillTable2FromPartialReceiveds(jTable2, inventorydatamanager);
            } else {
                JOptionPane.showMessageDialog(IM_VerifyPo.this, "Purchase Order not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void showPartialReceivedPanel() {
            PurchaseOrder po = pomanager.findpoid(orderId);
            if (po == null) {
                JOptionPane.showMessageDialog(IM_VerifyPo.this, "Purchase Order not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!po.getVerifyStatus().equals("pending")) {
                JOptionPane.showMessageDialog(IM_VerifyPo.this, "Status already set to " + po.getVerifyStatus(), "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridLayout(0, 2));
            List<String> itemIds = po.getItemIds();
            List<String> quantities = po.getQuantities();
            List<String> itemNames = new ArrayList<>();
            List<JTextField> quantityFields = new ArrayList<>();
            for (String itemId : itemIds) {
                Item item = inventorydatamanager.finditemid(itemId);
                String itemName = item != null ? item.getItemname() : "Unknown Item";
                itemNames.add(itemName);
                panel.add(new JLabel(itemName + " Quantity:"));
                JTextField field = new JTextField(5);
                quantityFields.add(field);
                panel.add(field);
            }

            int result = JOptionPane.showConfirmDialog(IM_VerifyPo.this, panel, "Enter Received Quantities", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                List<Integer> receivedQuantities = new ArrayList<>();
                List<Integer> remainingQuantities = new ArrayList<>();
                boolean allReceived = true;
                for (int i = 0; i < itemIds.size(); i++) {
                    try {
                        int received = Integer.parseInt(quantityFields.get(i).getText());
                        int ordered = Integer.parseInt(quantities.get(i));
                        if (received < 0 || received > ordered) {
                            JOptionPane.showMessageDialog(IM_VerifyPo.this, "Invalid quantity for " + itemNames.get(i), "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        receivedQuantities.add(received);
                        int remaining = ordered - received;
                        remainingQuantities.add(remaining);
                        if (remaining > 0) allReceived = false;

                        Item item = inventorydatamanager.finditemid(itemIds.get(i));
                        if (item != null) {
                            int newQuantity = item.getInstockquantity() + received;
                            inventorydatamanager.updateItem(
                                item.getItemid(), item.getItemname(), item.getItemdesc(), item.getSupplierid(),
                                item.getUnitprice(), item.getRetailprice(), newQuantity,
                                item.getReorderlevel(), LocalDate.now().toString(),
                                item.isDeleted()
                            );
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(IM_VerifyPo.this, "Invalid input for " + itemNames.get(i), "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                po.setVerifyStatus(allReceived ? "order received" : "partial received");
                pomanager.updatePurchaseOrderInFile(po);

                if (!allReceived) {
                    PartialReceived pr = new PartialReceived(orderId, itemIds, itemNames, remainingQuantities);
                    partialReceivedDataManager.addPartialReceived(pr);
                } else {
                    partialReceivedDataManager.removePartialReceived(orderId);
                }

                JOptionPane.showMessageDialog(IM_VerifyPo.this, "Partial receipt processed", "Success", JOptionPane.INFORMATION_MESSAGE);
                fillTable1FromTxtFile(jTable1, inventorydatamanager);
                fillTable2FromPartialReceiveds(jTable2, inventorydatamanager);
            }
        }
        // Rest of the methods remain as provided, except for completePartialReceived
        private void completePartialReceived() {
            if (orderId == null || orderId.isEmpty()) {
            JOptionPane.showMessageDialog(IM_VerifyPo.this, "Invalid Order ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
            }
            PurchaseOrder po = pomanager.findpoid(orderId);
            if (po == null) {
                JOptionPane.showMessageDialog(IM_VerifyPo.this, "Purchase Order not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PartialReceived receive = partialReceivedDataManager.getPartialReceiveds().stream()
                    .filter(r -> r.getOrderId().equals(orderId))
                    .findFirst()
                    .orElse(null);
            if (receive == null) {
                JOptionPane.showMessageDialog(IM_VerifyPo.this, "Partial receipt data not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> itemIds = po.getItemIds();
            List<Integer> remainingQuantities = receive.getRemainingQuantities();
            if (itemIds.size() != remainingQuantities.size()) {
            JOptionPane.showMessageDialog(IM_VerifyPo.this, "Data mismatch for PO " + orderId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
            }
            for (int i = 0; i < itemIds.size(); i++) {
                Item item = inventorydatamanager.finditemid(itemIds.get(i));
                if (item != null) {
                    int newQuantity = item.getInstockquantity() + receive.getRemainingQuantities().get(i);
                    inventorydatamanager.updateItem(
                        item.getItemid(), item.getItemname(), item.getItemdesc(), item.getSupplierid(),
                        item.getUnitprice(), item.getRetailprice(), newQuantity,
                        item.getReorderlevel(), LocalDate.now().toString(),
                        item.isDeleted()
                    );
                }
            }

            pomanager.updateReceiveStatus(orderId, "order received");
            partialReceivedDataManager.removePartialReceived(orderId);
            JOptionPane.showMessageDialog(IM_VerifyPo.this, "Remaining quantities received", "Success", JOptionPane.INFORMATION_MESSAGE);
            fillTable1FromTxtFile(jTable1, inventorydatamanager);
            fillTable2FromPartialReceiveds(jTable2, inventorydatamanager);
            jTable2.clearSelection(); // Clear selection after updating table
            jTable2.revalidate();
            jTable2.repaint();
        }
    }




    


    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(238, 238, 253));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setText("  Omega Wholesale Sdn Bhd ");

        jButton6.setText("Item Management");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setText("Generate stock report");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("______________________________________________");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("(OWSB)");

        jButton11.setText("Home");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jButton11)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(52, 52, 52)
                .addComponent(jButton6)
                .addGap(18, 18, 18)
                .addComponent(jButton8)
                .addGap(155, 155, 155)
                .addComponent(jButton11)
                .addContainerGap(165, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Order Id","Supplier Id","Item Id", "Item Name", "Quantity", "Action"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Order Id","Supplier Id","Item Id", "Item Name", "Remain Quantity", "Action"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jLabel11.setFont(new java.awt.Font("Algerian", 0, 24)); // NOI18N
        jLabel11.setText("partial received table");

        jLabel12.setFont(new java.awt.Font("Algerian", 0, 24)); // NOI18N
        jLabel12.setText("Verify Purchase order");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(262, 262, 262)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(275, 275, 275)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1441, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        new IM_inventory(inventorymanager,inventorydatamanager,supplierdatamanager).setVisible(true);
        this.dispose();    
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        new IM_StockReport(inventorymanager,inventorydatamanager,supplierdatamanager,pomanager).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        new IM_MainPage().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(IM_VerifyPo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(IM_VerifyPo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(IM_VerifyPo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(IM_VerifyPo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new IM_VerifyPo().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
