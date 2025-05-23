/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_assignment2025;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import static java_assignment2025.FinanceReport.exportJTableToJasper;
import static java_assignment2025.PurchaseOrderManager.findSupplierNameById;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Isaac
 */

public class FMPurchaseOrder extends javax.swing.JFrame {
    
    private PurchaseOrderManager poManager;
    private InventoryDataManager inventoryManager;
    private FinanceManager fm;
    private File folder;
    private DefaultTableModel pdfTableModel;
    private File pdfFolder = new File(System.getProperty("user.dir"));



    /**
     * Creates new form FMPurchaseOrder
     */
    public FMPurchaseOrder() {
        this.fm = (FinanceManager) Session.getCurrentUser();
        initComponents();
        jScrollPane1.setHorizontalScrollBarPolicy(
        javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jTable1.getColumnModel().getColumn(10).setPreferredWidth(120);
        poManager = new PurchaseOrderManager();
        inventoryManager = new InventoryDataManager();
        loadPOsIntoTable();
        fillPDFTable();
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewSelectedPDF();
            }
        });

    }
    
    public void loadPOsIntoTable() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); //clear table

    for (PurchaseOrder po : poManager.getpolist()) {
        String itemNames = "";
        for (String itemId : po.getItemIds()) {
            itemNames += inventoryManager.findItemNameById(itemId) + " | ";
        }
        itemNames = itemNames.replaceAll(" \\| $", ""); //remove any leftover ""

        List<String> supplierIds = po.getSupplierIds();
            List<String> supplierNamesList = new ArrayList<>();
            for (String supplierId : supplierIds) {
                String name = findSupplierNameById(supplierId); 
                supplierNamesList.add(name);
            }
            String supplierNames = String.join(",", supplierNamesList);

        model.addRow(new Object[] {
            po.getOrderId(),
            po.getPoCreator(),
            po.getRequestId(),
            po.getUserId(),
            String.join("|", po.getItemIds()),
            itemNames,
            String.join("|", po.getUnitPrices()),
            String.join("|", po.getQuantities()),
            po.getAmount(),
            supplierNames,
            po.getOrderDate(),
            po.getOrderStatus(),
            po.getVerifyStatus(),
            po.getPaymentStatus()
        });
    }
    resizeColumnWidths(jTable1);
    
    
}
    private void updatePOStatus(String userid,String newStatus) {
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a PO to " + newStatus + ".");
        return;
    }

    String poId = jTable1.getValueAt(selectedRow, 0).toString();
    System.out.println("Selected PO ID: " + poId);

    PurchaseOrder po = poManager.findpoid(poId);
    if (po != null) {
        String currentStatus = po.getOrderStatus();
        
        if (currentStatus.equalsIgnoreCase("approved") || currentStatus.equalsIgnoreCase("rejected")) {
            JOptionPane.showMessageDialog(this, "Status is final and cannot be changed.");
            return;
        }
        
        String oldLine = po.toString();
        po.setPostatuschangeby(userid);
        po.setOrderStatus(newStatus);
        String newLine = po.toString();

        System.out.println("Old Line: " + oldLine);
        System.out.println("New Line: " + newLine);

        new TextFile().replaceLineByPOId(poManager.getpofilepath(), po.getOrderId(), newLine);
        JOptionPane.showMessageDialog(this, "PO " + poId + " has been marked as " + newStatus + ".");
        refreshTable();
    } else {
        JOptionPane.showMessageDialog(this, "Could not find the selected PO.");
    }
    refreshTable();
}


    private void refreshTable() {
        poManager.loadAllpofromtxtfile();
        loadPOsIntoTable();

    }

    private void resizeColumnWidths(JTable table) {
    for (int column = 0; column < table.getColumnCount(); column++) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        int preferredWidth = 75;
        int maxWidth = 300;
    
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Component headerComp = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false, false, 0, column);
        preferredWidth = Math.max(preferredWidth, headerComp.getPreferredSize().width);

        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
            Component c = table.prepareRenderer(cellRenderer, row, column);
            int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
            preferredWidth = Math.max(preferredWidth, width);
        }

        preferredWidth = Math.min(preferredWidth, maxWidth);
        tableColumn.setPreferredWidth(preferredWidth);
    }
}
    
    private void showReportPopup(List<PurchaseOrder> poList, String title){
        
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(800,400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(
        new Object[]{"PO ID", "Amount","Order Date", "Supplier", "Status"},
        0
        );
        
        for (PurchaseOrder po : poList){
            model.addRow(new Object[]{
                po.getOrderId(),
                String.format("RM %.2f", po.getAmount()),
                po.getOrderDate(),
                String.join("|", po.getSupplierIds()),
                po.getPaymentStatus()
            });
        }
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        double total = new FinanceReport(poManager).calculateTotalAmount(poList);
        JLabel totalLabel = new JLabel("Total Amount Spent: RM " + String.format("%.2f", total));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
//        JButton downloadBtn = new JButton("Download PDF");
//
//        downloadBtn.addActionListener(e -> {
//            try {
//                exportTableToPDF(table, title);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Failed to export PDF: " + ex.getMessage());
//            }
//        });
    
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalLabel, BorderLayout.CENTER);
//        southPanel.add(downloadBtn, BorderLayout.EAST);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(southPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void fillPDFTable() {
            pdfTableModel = new DefaultTableModel(new Object[]{"No", "PDF File Name"}, 0);
            jTable2.setModel(pdfTableModel); // assuming jTable2 is used for PDF list

            File[] files = pdfFolder.listFiles((dir, name) -> name.startsWith("Purchase_Order_Report_") && name.endsWith(".pdf"));
            if (files != null) {
                int count = 1;
                for (File file : files) {
                    pdfTableModel.addRow(new Object[]{count++, file.getName()});
                }
            }
        }
        private void viewSelectedPDF() {
            int row = jTable2.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row first.");
                return;
            }

            String filename = jTable2.getValueAt(row, 1).toString(); 
            File selectedPDF = new File(pdfFolder, filename); 
            openPDF(selectedPDF);
        }
        private void openPDF(File pdfFile) {
            try {
                if (Desktop.isDesktopSupported() && pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot open PDF file.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error opening PDF: " + e.getMessage());
                e.printStackTrace();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        donDeleteMe = new javax.swing.JButton();
        approveBtn = new javax.swing.JButton();
        rejectBtn = new javax.swing.JButton();
        donDeleteMe1 = new javax.swing.JButton();
        rejectBtn1 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "PO Id","PO Created","PR Id","PR Created", "Item Id", "Item Name","Unit Per Price" ,"Quantity", "Amount", "Supplier Name", "Order Date", "Status", "Received", "Payment Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false,false,true,true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        }

    );
    jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jScrollPane1.setViewportView(jTable1);

    jLabel11.setFont(new java.awt.Font("Algerian", 0, 24)); // NOI18N
    jLabel11.setText("MANAGE Purchase ORDER");

    jPanel3.setBackground(new java.awt.Color(238, 238, 253));

    jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
    jLabel2.setText("  Omega Wholesale Sdn Bhd ");

    jButton7.setText("Sales Report");
    jButton7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton7ActionPerformed(evt);
        }
    });

    jButton8.setText("Payment Report");
    jButton8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton8ActionPerformed(evt);
        }
    });

    jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
    jLabel8.setText("______________________________________________");

    jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
    jLabel9.setText("(OWSB)");

    jButton10.setText("Log Out");
    jButton10.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton10ActionPerformed(evt);
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
                    .addGap(79, 79, 79)
                    .addComponent(jLabel9))
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
            .addGap(151, 151, 151)
            .addComponent(jButton7)
            .addGap(18, 18, 18)
            .addComponent(jButton8)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
            .addComponent(jButton10)
            .addGap(28, 28, 28))
    );

    donDeleteMe.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    donDeleteMe.setText("Generate PDF");
    donDeleteMe.setToolTipText("");
    donDeleteMe.setPreferredSize(new java.awt.Dimension(121, 25));
    donDeleteMe.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            donDeleteMeActionPerformed(evt);
        }
    });

    approveBtn.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    approveBtn.setText("Approve");
    approveBtn.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            approveBtnActionPerformed(evt);
        }
    });

    rejectBtn.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    rejectBtn.setText("Reject");
    rejectBtn.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rejectBtnActionPerformed(evt);
        }
    });

    donDeleteMe1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    donDeleteMe1.setText("Generate Monthly");
    donDeleteMe1.setToolTipText("");
    donDeleteMe1.setPreferredSize(new java.awt.Dimension(72, 25));
    donDeleteMe1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            donDeleteMe1ActionPerformed(evt);
        }
    });

    rejectBtn1.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    rejectBtn1.setText("Pay");
    rejectBtn1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rejectBtn1ActionPerformed(evt);
        }
    });

    jComboBox2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
    jComboBox2.setPreferredSize(new java.awt.Dimension(72, 25));

    jButton1.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    jButton1.setText("Edit");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jButton9.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    jButton9.setText("View");
    jButton9.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton9ActionPerformed(evt);
        }
    });

    jTable2.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTable2MouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(jTable2);

    jButton3.setText("View PDF");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(296, 296, 296)
            .addComponent(rejectBtn1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(24, 24, 24)
            .addComponent(donDeleteMe1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap(330, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(239, 239, 239)))
                    .addGap(214, 214, 214))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jLabel11)
                    .addGap(322, 322, 322))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 804, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addGap(119, 119, 119)
                            .addComponent(approveBtn)
                            .addGap(120, 120, 120)
                            .addComponent(rejectBtn)
                            .addGap(108, 108, 108)
                            .addComponent(donDeleteMe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(40, 40, 40))))
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(43, 43, 43)
            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(37, 37, 37)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(donDeleteMe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(rejectBtn1))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton9)
                .addComponent(jButton1)
                .addComponent(approveBtn)
                .addComponent(rejectBtn)
                .addComponent(donDeleteMe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(34, 34, 34)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton3)
            .addContainerGap())
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        new FinanceDailySum().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        new FMCreatePayment().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void donDeleteMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_donDeleteMeActionPerformed
        exportJTableToJasper(jTable1);
        fillPDFTable();
    }//GEN-LAST:event_donDeleteMeActionPerformed

    private void approveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approveBtnActionPerformed
        String userid = fm.getUserId();        
        updatePOStatus(userid,"approved");
    }//GEN-LAST:event_approveBtnActionPerformed

    private void rejectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejectBtnActionPerformed
        String userid = fm.getUserId();
        updatePOStatus(userid,"rejected");
    }//GEN-LAST:event_rejectBtnActionPerformed

    private void donDeleteMe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_donDeleteMe1ActionPerformed
        String selectedMonth = jComboBox2.getSelectedItem().toString();

        FinanceReport report = new FinanceReport(poManager);
        List<PurchaseOrder> filtered = report.filterByMonth(selectedMonth);

        showReportPopup(filtered, "Paid POs - " + selectedMonth);
    }//GEN-LAST:event_donDeleteMe1ActionPerformed

    private void rejectBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejectBtn1ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) { // Check if any row is selected
            String poId = jTable1.getValueAt(selectedRow, 0).toString();
            new FinancePayment().processPayment(poId);
            refreshTable();
            //now use poId
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row.");
        } //example: "PO01"

    }//GEN-LAST:event_rejectBtn1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String approvedStatus = jTable1.getValueAt(selectedRow, 11).toString();

            if ("pending".equalsIgnoreCase(approvedStatus)) {
                String poId = jTable1.getValueAt(selectedRow, 0).toString();
                PurchaseOrder selectedPO = poManager.findpoid(poId);

                // Create and show edit frame
                PM_Edit_Purchase_Order editFrame = new PM_Edit_Purchase_Order(selectedPO, poManager, inventoryManager, true);
                editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Show main PO frame again after edit frame is closed
                editFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true); // This refers to the current PO JFrame
                        refreshTable();
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new Login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        viewSelectedPDF();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FMPurchaseOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FMPurchaseOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton approveBtn;
    private javax.swing.JButton donDeleteMe;
    private javax.swing.JButton donDeleteMe1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton rejectBtn;
    private javax.swing.JButton rejectBtn1;
    // End of variables declaration//GEN-END:variables
}
