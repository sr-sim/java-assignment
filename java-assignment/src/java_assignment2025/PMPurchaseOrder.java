/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_assignment2025;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import static java_assignment2025.PurchaseOrderManager.findSupplierNameById;
import static java_assignment2025.TextFile.readFile;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Macy Khoo
 */
public class PMPurchaseOrder extends javax.swing.JFrame {
    private PurchaseOrder selectedPO;
    private PurchaseOrderManager pomanager;
    private InventoryDataManager inventorydatamanager;
    private PM_Edit_Purchase_Order editPO;
     //search function from Po list
    public void search(String str){
       DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
       TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
       jTable1.setRowSorter(sorter);
       sorter.setRowFilter(RowFilter.regexFilter("(?i)" + str));

    }

    /**
     * Creates new form PurchaseOrder
     */
    public PMPurchaseOrder() {
        initComponents();
        this.pomanager = new PurchaseOrderManager();
        this.inventorydatamanager = new InventoryDataManager();
        this.editPO = editPO;
        loadAllpofromtxtfile();
    }

    public void loadAllpofromtxtfile(){
       DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
       model.setRowCount(0); 
        
       for (PurchaseOrder po : pomanager.getpolist()) {
            String poid = po.getOrderId();
            String prid = po.getRequestId();
            String createdBy = po.getUserId();

            List<String> itemIdsList = po.getItemIds();
            String itemIds = String.join(",", itemIdsList);
            
            List<String> unitPriceList = po.getUnitPrices();
            String unitprice = String.join(",", unitPriceList);

            List<String> quantitiesList = po.getQuantities();
            String quantities = String.join(",", quantitiesList);

            double amount = po.getAmount();

            // Get item names
            List<String> itemNamesList = new ArrayList<>();
            for (String itemId : itemIdsList) {
                Item item = inventorydatamanager.finditemid(itemId.trim());
                itemNamesList.add(item != null ? item.getItemname() : "Unknown Item");
            }
            String itemNames = String.join(",", itemNamesList);

            // Get supplier names
            List<String> supplierIds = po.getSupplierIds();
            List<String> supplierNamesList = new ArrayList<>();
            for (String supplierId : supplierIds) {
                String name = PurchaseOrderManager.findSupplierNameById(supplierId); // No file path here
                supplierNamesList.add(name);
            }
            String supplierNames = String.join(",", supplierNamesList);
            
           

            model.addRow(new Object[]{
                poid,
                prid,
                createdBy,
                itemIds,
                itemNames,
                unitprice,
                quantities,
                String.format("%.2f", amount),
                supplierNames,
                po.getOrderDate(),
                po.getOrderStatus(),
                po.getVerifyStatus(),
                po.getPaymentStatus()
            });
    }}
       
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        donDeleteMe = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "PO Id","PR Id","PR Creator", "Item Id", "Item Name", "Unit Price", "Quantity", "Amount", "Supplier Name", "Order Date", "PO Status", "Order Received", "Payment Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false,false,false,true,true,true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        }

    );
    jScrollPane1.setViewportView(jTable1);

    jPanel3.setBackground(new java.awt.Color(238, 238, 253));

    jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
    jLabel2.setText("  Omega Wholesale Sdn Bhd ");

    jButton6.setText("Item List");
    jButton6.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton6ActionPerformed(evt);
        }
    });

    jButton7.setText("Supplier List");
    jButton7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton7ActionPerformed(evt);
        }
    });

    jButton8.setText("Purchase Requisition");
    jButton8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton8ActionPerformed(evt);
        }
    });

    jButton9.setText("Purchase Order");
    jButton9.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton9ActionPerformed(evt);
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
                        .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
            .addGap(76, 76, 76)
            .addComponent(jButton6)
            .addGap(27, 27, 27)
            .addComponent(jButton7)
            .addGap(28, 28, 28)
            .addComponent(jButton8)
            .addGap(28, 28, 28)
            .addComponent(jButton9)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

    jLabel11.setFont(new java.awt.Font("Algerian", 0, 24)); // NOI18N
    jLabel11.setText("Purchase ORDER");

    donDeleteMe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    donDeleteMe.setText("Delete");
    donDeleteMe.setToolTipText("");
    donDeleteMe.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            donDeleteMeActionPerformed(evt);
        }
    });

    jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    jLabel13.setText("Purchase Order Table");

    jLabel1.setFont(new java.awt.Font("STZhongsong", 2, 13)); // NOI18N
    jLabel1.setText("Search: ");

    jTextField1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField1ActionPerformed(evt);
        }
    });
    jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            jTextField1KeyReleased(evt);
        }
    });

    jButton1.setText("View ");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jButton2.setText("Edit");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(57, 57, 57)
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jButton1)
                    .addGap(18, 18, 18)
                    .addComponent(jButton2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(donDeleteMe))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel11)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(402, 402, 402)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1223, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(100, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(jLabel11)
                    .addGap(58, 58, 58)
                    .addComponent(jLabel13)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(101, 101, 101)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(125, 125, 125)
                    .addComponent(jLabel4)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(donDeleteMe))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))))
            .addGap(35, 129, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       new PM_ViewItem().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
         new PM_ViewSupplier().setVisible(true);
         this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
           new PM_PurchaseRequisition().setVisible(true);
           this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        new PMPurchaseOrder().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void donDeleteMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_donDeleteMeActionPerformed
                                         
        int selectedrow = jTable1.getSelectedRow();
        if (selectedrow != -1){
            String poid = jTable1.getValueAt(selectedrow, 0).toString();
            System.out.println("here"+ poid);
            int YesOrNo = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this purchase order?" + poid , "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (YesOrNo == JOptionPane.YES_OPTION){
                pomanager.deletepo(poid);
                loadAllpofromtxtfile();
                JOptionPane.showMessageDialog(null, "This purchase order deleted successfully");
            }
        }else{
                JOptionPane.showMessageDialog(null, "Please select a purchase order from the table");
            } 
    
    }//GEN-LAST:event_donDeleteMeActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String searchString = jTextField1.getText();
        search(searchString);
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Edit POs
        
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String approvedStatus = jTable1.getValueAt(selectedRow, 10).toString();

            if ("pending".equalsIgnoreCase(approvedStatus)) {
                String poId = jTable1.getValueAt(selectedRow, 0).toString();
                PurchaseOrder selectedPO = pomanager.findpoid(poId);

                // Create and show edit frame
                PM_Edit_Purchase_Order editFrame = new PM_Edit_Purchase_Order(selectedPO, pomanager, inventorydatamanager, true);
                editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Show main PO frame again after edit frame is closed
                editFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true); // This refers to the current PO JFrame
                    }
                });

                setVisible(false); // Hide main PO frame
                editFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Approved PO cannot be edited");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to edit.");
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String poId = jTable1.getValueAt(selectedRow, 0).toString();
            PurchaseOrder selectedPO = pomanager.findpoid(poId);

            // Create and open the View frame (false means view-only mode)
            PM_Edit_Purchase_Order viewFrame = new PM_Edit_Purchase_Order(selectedPO, pomanager, inventorydatamanager, false);
            viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // 👇 Re-show the PO JFrame after view frame is closed
            viewFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    setVisible(true); // This refers to the PO JFrame
                }
            });

            setVisible(false); // Hide main PO JFrame
            viewFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to view.");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

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
//            java.util.logging.Logger.getLogger(PMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(PMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(PMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(PMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PMPurchaseOrder().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton donDeleteMe;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
