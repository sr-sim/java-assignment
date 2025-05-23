/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_assignment2025;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Color; // Import for Color
import java.awt.Component; // Import for Component
import javax.swing.JTable; // Import for JTable
import javax.swing.JTextField; // Import for JTextField
import javax.swing.JLabel; // Import for JLabel
import javax.swing.table.DefaultTableCellRenderer; // Import for DefaultTableCellRenderer
/**
 *
 * @author hew
 */
public class IM_inventory extends javax.swing.JFrame {
    private InventoryManager inventorymanager;
    private SupplierDataManager supplierdatamanager;
    private PurchaseRequisitionManager prmanager ;
    private InventoryDataManager inventorydatamanager;
    private boolean hasShownLowStockAlert = false; // Flag to track if low stock alert has been shown

    /**
     * Creates new form IM_inventory
     */
    public IM_inventory(InventoryManager inventorymanager, InventoryDataManager inventorydatamanager, 
                        SupplierDataManager supplierdatamanager) {
        initComponents();
        this.inventorymanager = (InventoryManager) Session.getCurrentUser();
        this.inventorydatamanager = inventorydatamanager;
        this.supplierdatamanager = supplierdatamanager;
        String generateItemId = inventorydatamanager.generateItemId();
        if (jTable1 != null) {
            jTable1.setRowHeight(25);
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            jTable1.setDefaultEditor(Object.class, null); // Disable editing for all cells
            jTextField1.setText("");
            jTextField1.setEditable(false); // Make jTextField1 non-editable
            jTextField1.setFocusable(false);
            applyQuantityHighlighting();
        } else {
            JOptionPane.showMessageDialog(this, "Table initialization failed. Check GUI setup.", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
        fillTable1FromTxtFile();
    }
    
    private void applyQuantityHighlighting() {
        jTable1.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                          boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String itemId = (String) table.getModel().getValueAt(row, 0);
                Item item = inventorydatamanager.finditemid(itemId);
                if (item != null) {
                    int inStockQty = item.getInstockquantity();
                    int reorderLevel;
                    if (item.getReorderlevel() == null || item.getReorderlevel().trim().equals("0") || 
                        item.getReorderlevel().trim().equals("null")) {
                        reorderLevel = 50;
                    } else {
                        try {
                            reorderLevel = Integer.parseInt(item.getReorderlevel());
                        } catch (NumberFormatException e) {
                            reorderLevel = 50;
                        }
                    }
                    if (inStockQty < reorderLevel) {
                        cell.setForeground(Color.RED); // Red text for low stock
                    } else {
                        cell.setForeground(table.getForeground());
                        cell.setBackground(table.getBackground());
                    }
                } else {
                    cell.setForeground(table.getForeground());
                    cell.setBackground(table.getBackground());
                }
                return cell;
            }
        });
    }
   

    public void fillTable1FromTxtFile() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        List<String> lowStockItems = new ArrayList<>();
        for (Item item : inventorydatamanager.getinventorylist()) {
            if (item.isDeleted()) {
            continue; // Skip deleted items
            }
            String itemId = item.getItemid();
            String itemName = item.getItemname();
            String itemDescription = item.getItemdesc();
            String supplierId = item.getSupplierid();
            int inStockQty = item.getInstockquantity();
            double unitPrice = item.getUnitprice();
            double retailPrice = item.getRetailprice();
            String lastModifiedDate = item.getLastmodifieddate();

            Supplier supplier = supplierdatamanager.findsupplierid(supplierId);
            String supplierName = (supplier != null) ? supplier.getSuppliername() : "Unknown Supplier";

            model.addRow(new Object[]{
                itemId, itemName, itemDescription,
                supplierId, supplierName,
                inStockQty, unitPrice, retailPrice,
                lastModifiedDate
            });

            int reorderLevel;
            if (item.getReorderlevel() == null || item.getReorderlevel().trim().equals("0") || 
                item.getReorderlevel().trim().equals("null")) {
                reorderLevel = 50;
            } else {
                try {
                    reorderLevel = Integer.parseInt(item.getReorderlevel());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing reorder level for item " + itemId + ": " + e.getMessage());
                    reorderLevel = 50;
                }
            }

            if (inStockQty < reorderLevel) {
                lowStockItems.add(String.format("%s (%s): %d (Threshold: %d)", 
                                               itemName, itemId, inStockQty, reorderLevel));
            }
        }

        // Show low stock alert only if it hasn't been shown yet
        if (!lowStockItems.isEmpty() && !hasShownLowStockAlert) {
            String message = "The following items are low on stock:\n\n" + String.join("\n", lowStockItems) + 
                            "\n\nPlease consider reordering these items.";
            JOptionPane.showMessageDialog(this, message, "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
            hasShownLowStockAlert = true; // Set flag to prevent future popups
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Item Id", "Item Name", "Description", "Supplier Id", "Supplier Name", "Quanity", "Unit Price", "Retail Price", "Last Modified Date"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        jTable1.getAccessibleContext().setAccessibleName("");

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
                .addContainerGap(403, Short.MAX_VALUE))
        );

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Quantity Low stock alert:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(421, 421, 421))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(433, 433, 433))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < jTable1.getRowCount()) {
            String itemId = jTable1.getValueAt(selectedRow, 0).toString();
            Item item = inventorydatamanager.finditemid(itemId);
            if (item != null) {
                String reorderLevel = item.getReorderlevel();
                if (reorderLevel == null || reorderLevel.trim().equals("0") || reorderLevel.trim().equals("null")) {
                    jTextField1.setText("50");
                } else {
                    try {
                        Integer.parseInt(reorderLevel);
                        jTextField1.setText(reorderLevel);
                    } catch (NumberFormatException e) {
                        jTextField1.setText("50");
                    }
                }
                try {
                    jTable1.setRowSelectionInterval(selectedRow, selectedRow);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error selecting row: " + e.getMessage());
                }
            } else {
                jTextField1.setText("");
                System.err.println("Item not found for ID: " + itemId);
            }
        } else {
            jTextField1.setText("");
            System.err.println("Selected row index is out of bounds: " + selectedRow);
        }
    }//GEN-LAST:event_jTable1MouseClicked

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //delete item button
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String itemId = jTable1.getValueAt(selectedRow, 0).toString();
            String status = inventorydatamanager.getItemDeletionStatus(itemId);

            switch (status) {
                case "can_delete":
                    int yesOrNo = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this item? " + itemId,
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (yesOrNo == JOptionPane.YES_OPTION) {
                        inventorydatamanager.markitemasDeleted(itemId);
                        fillTable1FromTxtFile();
                        jTextField1.setText("");
                        applyQuantityHighlighting(); // Re-apply renderer
                        JOptionPane.showMessageDialog(null, "Item deleted successfully");
                    }
                    break;
                case "cannot_delete_pending_pr":
                    JOptionPane.showMessageDialog(null,
                        "Cannot delete this item because it is in a pending Purchase Requisition\n" +
                        "Please delete the pending PR first");
                    break;
                case "cannot_delete_approved_pr_po_not_paid":
                    JOptionPane.showMessageDialog(null,
                        "Cannot delete this item because its PR is approved and PO is not paid or not fully approved");
                    break;
                case "cannot_delete_rejected_pr":
                    JOptionPane.showMessageDialog(null,
                        "Cannot delete this item because it is in a rejected PR\n" +
                        "Please delete the PR first");
                    break;
                default:
                    JOptionPane.showMessageDialog(null,
                        "This item cannot be deleted due to unknown status");
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item from the table");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //edit button to edit the default quantity for low stock alert, after they clicked
        //will pop a panel for them to modify the the default quantity
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String itemId = jTable1.getValueAt(selectedRow, 0).toString();
            Item item = inventorydatamanager.finditemid(itemId);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Item not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            JLabel reorderLabel = new JLabel("Reorder Level:");
            JTextField reorderField = new JTextField(item.getReorderlevel() != null && 
                                                    !item.getReorderlevel().trim().equals("0") && 
                                                    !item.getReorderlevel().trim().equals("null") ? 
                                                    item.getReorderlevel() : "50");
            panel.add(reorderLabel);
            panel.add(reorderField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Reorder Level", 
                                                     JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newReorderLevel = reorderField.getText().trim();
                try {
                    int reorderLevelValue = Integer.parseInt(newReorderLevel);
                    if (reorderLevelValue < 0) {
                        JOptionPane.showMessageDialog(this, "Reorder level must be non-negative", 
                                                     "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    inventorydatamanager.updateItem(
                        item.getItemid(), item.getItemname(), item.getItemdesc(), 
                        item.getSupplierid(), item.getUnitprice(), item.getRetailprice(),
                        item.getInstockquantity(), newReorderLevel,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        item.isDeleted()
                    );
                    fillTable1FromTxtFile();
                    jTextField1.setText(newReorderLevel);
                    applyQuantityHighlighting(); // Re-apply renderer
                    JOptionPane.showMessageDialog(this, "Reorder level updated successfully");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid reorder level. Please enter a number.", 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to edit");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        //text field that show the quantity of low stock alert
        
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
//    /*public static void main(String args[]) {
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
//            java.util.logging.Logger.getLogger(IM_inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(IM_inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(IM_inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(IM_inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new IM_inventory().setVisible(true);
//            }
//        });
//    }
//    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
