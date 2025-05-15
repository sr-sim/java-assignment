/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_assignment2025;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Calendar;
import java.util.Locale;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
//import static java_assignment2025.FinanceReport.exportJTableToJasper;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Isaac
 */
public class FinanceGReport extends javax.swing.JFrame {
    private PurchaseOrderManager poManager;
    private InventoryDataManager inventoryManager;
    /**
     * Creates new form FinanceGReport
     */
    public FinanceGReport() {
        initComponents();
        inventoryManager = new InventoryDataManager();
        poManager = new PurchaseOrderManager();
        loadPOsIntoTable();
    }
    
    public void loadPOsIntoTable() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // clear table

    for (PurchaseOrder po : poManager.getpolist()) {
        String itemNames = "";
        for (String itemId : po.getItemIds()) {
            itemNames += inventoryManager.findItemNameById(itemId) + " | ";
        }
        itemNames = itemNames.replaceAll(" \\| $", ""); // Remove trailing "|"


        model.addRow(new Object[] {
            po.getOrderId(),
            po.getRequestId(),
            po.getUserId(),
            String.join("|", po.getItemIds()),
            itemNames,
            String.join("|", po.getQuantities()),
            po.getAmount(),
            String.join("|", po.getSupplierIds()),
            po.getOrderDate(),
            po.getOrderStatus(),
            po.getPaymentStatus()
        });
        resizeColumnWidths(jTable1);
    }
    
    
}
    private void generateWeeklyReport(){
        Calendar cal = Calendar.getInstance();
        int currentWeek = cal.get(Calendar.WEEK_OF_MONTH);
        FinanceReport report = new FinanceReport(poManager);
        List<PurchaseOrder> filtered = report.filterByWeek(String.valueOf(currentWeek));
        showReportPopup(filtered, "paid pos - week" + currentWeek);
        
    }
    private void exportJTableToJasperReport() {
    try {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        List<PurchaseOrderReportEntry> reportList = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            String poid = model.getValueAt(i, 0).toString();       // PO Id
            String itemIds = model.getValueAt(i, 3).toString();    // Item Id
            String itemNames = model.getValueAt(i, 4).toString();  // Item Name
            String quantities = model.getValueAt(i, 5).toString(); // Quantity
            String amount = model.getValueAt(i, 6).toString();     // Amount
            String orderDate = model.getValueAt(i, 8).toString();  // Order Date

            reportList.add(new PurchaseOrderReportEntry(poid, itemIds, itemNames, quantities, orderDate, amount));
        }

        // Load .jrxml or .jasper file (compiled)
        JasperReport report = JasperCompileManager.compileReport("C:\\Users\\Isaac\\JaspersoftWorkspace\\MyReports\\FinanceReportTemplate.jrxml");

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(reportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "Purchase Order Financial Report");

        JasperPrint print = JasperFillManager.fillReport(report, parameters, ds);
        JasperViewer.viewReport(print, false);
        JasperExportManager.exportReportToPdfFile(print, "PO_Table_Report_" + System.currentTimeMillis() + ".pdf");

        JOptionPane.showMessageDialog(this, "PDF Exported Successfully!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
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
        
        JButton downloadBtn = new JButton("Download PDF");

        downloadBtn.addActionListener(e -> {
            try {
                exportTableToPDF(table, title);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to export PDF: " + ex.getMessage());
            }
        });
    
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalLabel, BorderLayout.CENTER);
        southPanel.add(downloadBtn, BorderLayout.EAST);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(southPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void exportTableToPDF(JTable table, String title) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);
        PDFont font = PDType1Font.HELVETICA;
        PDFont boldFont = PDType1Font.HELVETICA_BOLD;

        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float y = yStart;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float rowHeight = 20;
        float tableBottomY = 100;

        content.setFont(boldFont, 18);
        content.beginText();
        content.newLineAtOffset(margin, y);
        content.showText(title);
        content.endText();
        y -= 30;

    // Draw header
        content.setFont(boldFont, 12);
        content.beginText();
        content.newLineAtOffset(margin, y);
        content.showText("PO ID      Amount               Order Date        Supplier       Status");
        content.endText();

        y -= rowHeight;
        content.setStrokingColor(0, 0, 0); // black line

        // Draw header bottom line
        content.moveTo(margin, y);
        content.lineTo(margin + tableWidth, y);
        content.stroke();

        content.setFont(font, 11);

        // Table body
        for (int row = 0; row < table.getRowCount(); row++) {
            if (y < tableBottomY) break; // avoid writing off page

            y -= rowHeight;
            String poId = table.getValueAt(row, 0).toString();
            String amount = table.getValueAt(row, 1).toString();
            String date = table.getValueAt(row, 2).toString();
            String supplier = table.getValueAt(row, 3).toString();
            String status = table.getValueAt(row, 4).toString();

            content.beginText();
            content.newLineAtOffset(margin, y);
            content.showText(String.format("%-10s %-20s %-15s %-12s %-10s", poId, amount, date, supplier, status));
            content.endText();

            // Draw horizontal line below row
            content.moveTo(margin, y - 5);
            content.lineTo(margin + tableWidth, y - 5);
            content.stroke();
        }

        // Total amount
        double totalAmount = 0;
        for (int row = 0; row < table.getRowCount(); row++) {
            String amountStr = table.getValueAt(row, 1).toString().replace("RM", "").trim();
            try {
                totalAmount += Double.parseDouble(amountStr);
            } catch (NumberFormatException ignored) {}
        }

        y -= 40;
        content.setFont(boldFont, 12);
        content.beginText();
        content.newLineAtOffset(margin, y);
        content.showText("Total Amount Spent: RM " + String.format("%.2f", totalAmount));
        content.endText();

        content.close();

        String fileName = "FinanceReport_" + System.currentTimeMillis() + ".pdf";
        document.save(fileName);
        document.close();

        JOptionPane.showMessageDialog(null, "PDF saved as " + fileName);
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
        donDeleteMe = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        rejectBtn = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        genpdf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "PO Id","PR Id","Created by", "Item Id", "Item Name", "Quantity", "Amount", "Supplier Name", "Order Date", "Status", "Payment Status", "Received?"
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

    donDeleteMe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    donDeleteMe.setText("Generate Monthly");
    donDeleteMe.setToolTipText("");
    donDeleteMe.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            donDeleteMeActionPerformed(evt);
        }
    });

    jLabel11.setFont(new java.awt.Font("Algerian", 0, 24)); // NOI18N
    jLabel11.setText("Financial overview");

    jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    jLabel13.setText("Purchase Order Table");

    jPanel3.setBackground(new java.awt.Color(238, 238, 253));

    jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
    jLabel2.setText("  Omega Wholesale Sdn Bhd ");

    jButton6.setText("Item LIst");
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

    jButton8.setText("Process Payment");
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

    jButton10.setText("Received Order");
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
                        .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(jButton9)
            .addGap(18, 18, 18)
            .addComponent(jButton10)
            .addContainerGap(192, Short.MAX_VALUE))
    );

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

    rejectBtn.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    rejectBtn.setText("Generate Weekly");
    rejectBtn.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rejectBtnActionPerformed(evt);
        }
    });

    jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

    genpdf.setFont(new java.awt.Font("Segoe UI Black", 1, 13)); // NOI18N
    genpdf.setText("Generate pdf");
    genpdf.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            genpdfActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(278, 278, 278)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(406, 406, 406)
                        .addComponent(jLabel11)))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                    .addGap(159, 159, 159)
                    .addComponent(rejectBtn)
                    .addGap(121, 121, 121)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(donDeleteMe)
                    .addGap(64, 64, 64)
                    .addComponent(genpdf)))
            .addContainerGap(330, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(40, 40, 40)
            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel13))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(30, 30, 30)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(donDeleteMe)
                .addComponent(rejectBtn)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(genpdf))
            .addGap(102, 102, 102))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void donDeleteMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_donDeleteMeActionPerformed
        String selectedMonth = jComboBox2.getSelectedItem().toString();

        FinanceReport report = new FinanceReport(poManager);
        List<PurchaseOrder> filtered = report.filterByMonth(selectedMonth);

        showReportPopup(filtered, "Paid POs - " + selectedMonth);
    }//GEN-LAST:event_donDeleteMeActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //        new SM_ItemEntry(salesmanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //        new SM_SupplierEntry(salesmanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //        new SM_DailySalesEntry(salesmanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        //        new SM_PurchaseRequisition(salesmanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        //        new SM_PurchaseOrder(salesmanager).setVisible(true);
        //        this.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

    }//GEN-LAST:event_jTextField1KeyReleased

    private void rejectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejectBtnActionPerformed
        generateWeeklyReport();
    }//GEN-LAST:event_rejectBtnActionPerformed

    private void genpdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genpdfActionPerformed
//        exportJTableToJasper(jTable1);
    }//GEN-LAST:event_genpdfActionPerformed

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
            java.util.logging.Logger.getLogger(FinanceGReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinanceGReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinanceGReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinanceGReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FinanceGReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton donDeleteMe;
    private javax.swing.JButton genpdf;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton rejectBtn;
    // End of variables declaration//GEN-END:variables
}
