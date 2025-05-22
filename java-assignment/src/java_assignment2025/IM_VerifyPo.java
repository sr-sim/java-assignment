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
    private List<PartialReceipt> partialReceipts;
    /**
     * Creates new form IM_VerifyPo
     */
    public IM_VerifyPo(InventoryManager inventorymanager,PurchaseOrderManager pomanager,InventoryDataManager inventorydatamanager, SupplierDataManager supplierdatamanager) {
   
            this.inventorymanager = (InventoryManager)Session.getCurrentUser();
            this.inventorydatamanager = inventorydatamanager;
            this.supplierdatamanager = supplierdatamanager;
             this.pomanager = pomanager;
             this.partialReceipts = new ArrayList<>();
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
        ActionButtonEditor buttonEditor = new ActionButtonEditor(jTable1, inventorydatamanager, true);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(buttonEditor);
        jTable1.getColumnModel().getColumn(5).setCellEditor(buttonEditor);
        

        // Populate the table
        fillTable1FromTxtFile(jTable1, inventorydatamanager);
        autoAdjustColumnWidths(jTable1); // Adjust columns after filling
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
        ActionButtonEditor remainEditor = new ActionButtonEditor(jTable2, inventorydatamanager, false);
        jTable2.getColumnModel().getColumn(5).setCellRenderer(remainEditor);
        jTable2.getColumnModel().getColumn(5).setCellEditor(remainEditor);
        fillTable2FromPartialReceipts(jTable2, inventorydatamanager);   
        autoAdjustColumnWidths(jTable2);
        jTable2.repaint();
        jTable2.revalidate(); // Added: Ensure layout is updated after population
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
                System.out.println("Action column width set to: " + preferredWidth);
            }

            tableColumn.setPreferredWidth(Math.min(preferredWidth, maxWidth));
        }
    }
    public void fillTable1FromTxtFile( JTable jTable1,InventoryDataManager inventorydatamanager) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        try {
            List<PurchaseOrder> poList = pomanager.getpolist();
            if (poList == null || poList.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No purchase orders found in " + pomanager.getpofilepath()+" (File does not exist or is empty)", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (PurchaseOrder po : pomanager.getpolist()) {
                if (po.getOrderStatus().equalsIgnoreCase("approved") &&
                    po.getVerifyStatus().equalsIgnoreCase("pending") &&
                    po.getReceiveStatus().equalsIgnoreCase("not received")) {
                    List<String> itemIds = po.getItemIds();
                    List<String> quantities = po.getQuantities();
                    List<String> supplierIds = po.getSupplierIds();
                    List<String> itemNames = new ArrayList<>();
                    for (String itemId : itemIds) {
                        Item item = inventorydatamanager.finditemid(itemId);
                        itemNames.add(item != null ? item.getItemname() : "Unknown Item (" + itemId +")");
                    }
                    
                   
                    // Add rows for each item, with Action only in the last row
                    for (int i = 0; i < itemIds.size(); i++) {
                        Object actionValue = (i == itemIds.size() - 1) ? "" : null; // Explicitly set action value
                        Object[] row = {
                            i == 0 ? po.getOrderId() : "", // Order Id only in                                                                                                           first sub-row
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
            model.fireTableDataChanged(); // Added: Notify table of data change
         } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading purchase orders from " + pomanager.getpofilepath() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void fillTable2FromPartialReceipts(JTable jTable2, InventoryDataManager inventorydatamanager) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        for (PurchaseOrder po : pomanager.getpolist()) {
            if (po.getOrderStatus().equalsIgnoreCase("approved") &&
                po.getVerifyStatus().equalsIgnoreCase("pending") &&
                po.getReceiveStatus().equalsIgnoreCase("partial received")) {
                PartialReceipt receipt = partialReceipts.stream()
                        .filter(r -> r.orderId.equals(po.getOrderId()))
                        .findFirst()
                        .orElse(null);
                if (receipt != null) {
                    List<String> itemIds = po.getItemIds();
                    for (int i = 0; i < itemIds.size(); i++) {
                        Object[] row = {
                            i == 0 ? po.getOrderId() : "",
                            i < po.getSupplierIds().size() ? po.getSupplierIds().get(i) : "",
                            i < itemIds.size() ? itemIds.get(i) : "",
                            i < receipt.itemNames.size() ? receipt.itemNames.get(i) : "",
                            i < receipt.remainingQuantities.size() ? String.valueOf(receipt.remainingQuantities.get(i)) : "",
                            i == itemIds.size() - 1 ? "" : null
                        };
                        model.addRow(row);
                    }
                }
            }
        }
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
        private final InventoryDataManager inventoryDataManager;
        private final boolean isMainTable;
        private String orderId;
        private Object currentValue;

            public ActionButtonEditor(JTable table, InventoryDataManager inventoryDataManager, boolean isMainTable) {
            this.table = table;
            this.inventoryDataManager = inventoryDataManager;
            this.isMainTable = isMainTable;

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
                if (isMainTable) {
                    updateReceiveStatus("received");
                } else {
                    completePartialReceipt();
                }
                fireEditingStopped();
                table.revalidate();
                table.repaint();
            });
            if (isMainTable && partialReceivedButton != null) {
                partialReceivedButton.addActionListener(e -> {
                    showPartialReceiptPanel();
                    fireEditingStopped();
                    table.revalidate();
                    table.repaint();
                });
            }
        }

    private void updateReceiveStatus(String status) {
    PurchaseOrder po = pomanager.findpoid(orderId);
    if (po != null) {
        if (!po.getReceiveStatus().equals("not received")) {
            JOptionPane.showMessageDialog(table, "Status already set to " + po.getReceiveStatus(), "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Update inventory before updating PO status
        List<String> itemIds = po.getItemIds();
        List<String> quantities = po.getQuantities();
        for (int i = 0; i < itemIds.size(); i++) {
            Item item = inventoryDataManager.finditemid(itemIds.get(i));
            if (item != null) {
                int newQuantity = item.getInstockquantity() + Integer.parseInt(quantities.get(i));;
                inventoryDataManager.updateItem(
                    item.getItemid(), item.getItemname(), item.getItemdesc(), item.getSupplierid(),
                    item.getUnitprice(), item.getRetailprice(), newQuantity,
                    item.getReorderlevel(), item.getReorderstatus(), LocalDate.now().toString(),
                    item.isDeleted()
                );
            }
        }
        pomanager.updateReceiveStatus(orderId, status, true);
        JOptionPane.showMessageDialog(table, "Receive status updated to " + status, "Success", JOptionPane.INFORMATION_MESSAGE);
        fillTable1FromTxtFile(table, inventoryDataManager);
        fillTable2FromPartialReceipts(jTable2, inventoryDataManager);
    } else {
        JOptionPane.showMessageDialog(table, "Purchase Order not found", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void showPartialReceiptPanel() {
        PurchaseOrder po = pomanager.findpoid(orderId);
        if (po == null) {
            JOptionPane.showMessageDialog(table, "Purchase Order not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!po.getReceiveStatus().equals("not received")) {
            JOptionPane.showMessageDialog(table, "Status already set to " + po.getReceiveStatus(), "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2));
        List<String> itemIds = po.getItemIds();
        List<String> quantities = po.getQuantities();
        List<String> itemNames = new ArrayList<>();
        List<JTextField> quantityFields = new ArrayList<>();
        for (String itemId : itemIds) {
            Item item = inventoryDataManager.finditemid(itemId);
            String itemName = item != null ? item.getItemname() : "Unknown Item";
            itemNames.add(itemName);
            panel.add(new JLabel(itemName + " Quantity:"));
            JTextField field = new JTextField(5);
            quantityFields.add(field);
            panel.add(field);
        }

        int result = JOptionPane.showConfirmDialog(table, panel, "Enter Received Quantities", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            List<Integer> receivedQuantities = new ArrayList<>();
            List<Integer> remainingQuantities = new ArrayList<>();
            boolean allReceived = true;
            for (int i = 0; i < itemIds.size(); i++) {
                try {
                    int received = Integer.parseInt(quantityFields.get(i).getText());
                    int ordered = Integer.parseInt(quantities.get(i));
                    if (received < 0 || received > ordered) {
                        JOptionPane.showMessageDialog(table, "Invalid quantity for " + itemNames.get(i), "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    receivedQuantities.add(received);
                    int remaining = ordered - received;
                    remainingQuantities.add(remaining);
                    if (remaining > 0) allReceived = false;

                    Item item = inventoryDataManager.finditemid(itemIds.get(i));
                    if (item != null) {
                        int newQuantity = item.getInstockquantity() + received;
                        inventoryDataManager.updateItem(
                            item.getItemid(), item.getItemname(), item.getItemdesc(), item.getSupplierid(),
                           item.getUnitprice(), item.getRetailprice(), newQuantity,
                            item.getReorderlevel(), item.getReorderstatus(), LocalDate.now().toString(),
                            item.isDeleted()
                        );
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(table, "Invalid input for " + itemNames.get(i), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            po.setReceiveStatus(allReceived ? "received" : "partial received");
            po.setVerifyStatus(allReceived ? "verified" : "pending");
//            pomanager.updatePurchaseOrderInFile(po);

            if (!allReceived) {
                partialReceipts.add(new PartialReceipt(orderId, itemIds, itemNames, remainingQuantities));
            }

            JOptionPane.showMessageDialog(table, "Partial receipt processed", "Success", JOptionPane.INFORMATION_MESSAGE);
            fillTable1FromTxtFile(jTable1, inventoryDataManager);
            fillTable2FromPartialReceipts(jTable2, inventoryDataManager);
        }
    }
        
    private void completePartialReceipt() {
    PurchaseOrder po = pomanager.findpoid(orderId);
    if (po == null) {
        JOptionPane.showMessageDialog(table, "Purchase Order not found", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    PartialReceipt receipt = partialReceipts.stream()
            .filter(r -> r.orderId.equals(orderId))
            .findFirst()
            .orElse(null);
    if (receipt == null) {
        JOptionPane.showMessageDialog(table, "Partial receipt data not found", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    List<String> itemIds = po.getItemIds();
    for (int i = 0; i < itemIds.size(); i++) {
        Item item = inventoryDataManager.finditemid(itemIds.get(i));
        if (item != null) {
            int newQuantity = item.getInstockquantity() + receipt.remainingQuantities.get(i);
            inventoryDataManager.updateItem(
                item.getItemid(), item.getItemname(), item.getItemdesc(), item.getSupplierid(),
                item.getUnitprice(), item.getRetailprice(), newQuantity,
                item.getReorderlevel(), item.getReorderstatus(), LocalDate.now().toString(),
                item.isDeleted()
            );
        }
    }

    pomanager.updateReceiveStatus(orderId, "received", false);
    partialReceipts.remove(receipt);
    JOptionPane.showMessageDialog(table, "Remaining quantities received", "Success", JOptionPane.INFORMATION_MESSAGE);
    fillTable1FromTxtFile(jTable1, inventoryDataManager);
    fillTable2FromPartialReceipts(jTable2, inventoryDataManager);
}

        @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Object modelValue = table.getModel().getValueAt(row, column);
        System.out.println("Renderer - Row " + row + " Value: " + (modelValue != null ? modelValue.toString() : "null") +
                          " (Param Value: " + (value != null ? value.toString() : "null") + ")");

        // Reset the renderer panel
        rendererPanel.removeAll();
        rendererPanel.setVisible(false);

        // Show buttons only if the value is an empty string ("")
        if (modelValue instanceof String && ((String) modelValue).equals("")) {
            rendererPanel.add(receivedButton);
            if (isMainTable && partialReceivedButton != null) {
                rendererPanel.add(partialReceivedButton);
            }

            orderId = getOrderIdForRow(row);
            PurchaseOrder po = pomanager.findpoid(orderId);
            boolean isEnabled = po != null && (isMainTable ? po.getReceiveStatus().equals("not received") 
                    : po.getReceiveStatus().equals("partial received"));

            receivedButton.setEnabled(isEnabled);
            if (isMainTable && partialReceivedButton != null) {
                partialReceivedButton.setEnabled(isEnabled);
            }

            rendererPanel.setVisible(true);
        }

        rendererPanel.revalidate();
        rendererPanel.repaint();
        return rendererPanel;
    }

        @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Object modelValue = table.getModel().getValueAt(row, column);
        System.out.println("Editor - Row " + row + " Value: " + (modelValue != null ? modelValue.toString() : "null") +
                          " (Param Value: " + (value != null ? value.toString() : "null") + ")");

        currentValue = modelValue;

        // Reset the editor panel
        editorPanel.removeAll();
        editorPanel.setVisible(false);

        // Show buttons only if the value is an empty string ("")
        if (modelValue instanceof String && ((String) modelValue).equals("")) {
            editorPanel.add(receivedButton);
            if (isMainTable && partialReceivedButton != null) {
                editorPanel.add(partialReceivedButton);
            }

            orderId = getOrderIdForRow(row);
            PurchaseOrder po = pomanager.findpoid(orderId);
            boolean isEnabled = po != null && (isMainTable ? po.getReceiveStatus().equals("not received") 
                    : po.getReceiveStatus().equals("partial received"));

            receivedButton.setEnabled(isEnabled);
            if (isMainTable && partialReceivedButton != null) {
                partialReceivedButton.setEnabled(isEnabled);
            }

            editorPanel.setVisible(true);
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
        for (int i = row; i >= 0; i--) {
            String orderId = (String) table.getModel().getValueAt(i, 0);
            if (orderId != null && !orderId.isEmpty()) {
                return orderId;
            }
        }
        return "";
    }

}
    private class PartialReceipt {
        String orderId;
        List<String> itemIds;
        List<String> itemNames;
        List<Integer> remainingQuantities;

        PartialReceipt(String orderId, List<String> itemIds, List<String> itemNames, List<Integer> remainingQuantities) {
            this.orderId = orderId;
            this.itemIds = itemIds;
            this.itemNames = itemNames;
            this.remainingQuantities = remainingQuantities;
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
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

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

        jButton7.setText("Purchase order verification");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Generate stock report");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setText("Purchase Order");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("______________________________________________");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("(OWSB)");

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
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel9)))
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
                .addComponent(jButton7)
                .addGap(18, 18, 18)
                .addComponent(jButton8)
                .addGap(18, 18, 18)
                .addComponent(jButton10)
                .addContainerGap(261, Short.MAX_VALUE))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(1441, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //        new SM_ItemEntry(salesmanager,inventorydatamanager,supplierdatamanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //        new SM_SupplierEntry(salesmanager,supplierdatamanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        //        new SM_PurchaseOrder(salesmanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

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
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
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
