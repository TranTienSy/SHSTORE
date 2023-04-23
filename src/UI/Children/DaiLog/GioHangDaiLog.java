/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package UI.Children.DaiLog;

import DAO.GGSanPhamDAO;
import DAO.SanPhamDAO;
import HELP.CellRenderer;
import MODEL_SWING.CellEditorButton;
import MODEL_SWING.cellRendererButton;
import Models.CTHDDatHang;
import Models.GGSanPham;
import Models.SanPham;
import UI.Children.PanelTrangChu_KH;
import Utils.DDTienTe;
import Utils.MsgBox;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author TIEN SY
 */
public class GioHangDaiLog extends javax.swing.JDialog {

    /**
     * Creates new form GioHangDaiLog
     */
    public GioHangDaiLog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon/icons8-grocery-store-32.png")));
        this.setLocationRelativeTo(null);
        fillTable();
    }
    GGSanPhamDAO gg = new GGSanPhamDAO();
    SanPhamDAO sanPhamDAO = new SanPhamDAO();
    List<CTHDDatHang> listCTHD;
    gifLoadDiaLog gif = new gifLoadDiaLog(null, false);

    public void fillTable() {
        tblCTHDD.getColumnModel().getColumn(5).setCellRenderer(new cellRendererButton());
        tblCTHDD.getColumnModel().getColumn(5).setCellEditor(new CellEditorButton());
        DefaultTableModel model = (DefaultTableModel) tblCTHDD.getModel();
        model.setRowCount(0);
        listCTHD = PanelTrangChu_KH.listCT;
        if (listCTHD != null) {
            BufferedImage bfimage[] = new BufferedImage[listCTHD.size()];
            for (int i = 0; i < listCTHD.size(); i++) {
                SanPham sanPham = sanPhamDAO.selectbyID(listCTHD.get(i).getMaSP());
                ByteArrayInputStream byteArray = new ByteArrayInputStream(sanPham.getHinh());
                try {
                    bfimage[i] = ImageIO.read(byteArray);
                } catch (IOException ex) {
                    System.out.println(ex);
                }

                JButton btnXoa = new JButton("");
                btnXoa.setBorder(null);
                btnXoa.setIcon(new ImageIcon("src\\icon\\delete.png"));
                btnXoa.setForeground(Color.red);
                btnXoa.setFont(new Font("tohama", Font.BOLD, 12));
                btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
                GGSanPham sp = gg.selectByCheckHieuLuc(listCTHD.get(i).getMaSP());
                if (sp == null) {
                    Object row[] = {null, sanPham.getTenSP(), listCTHD.get(i).getSL(), DDTienTe.FormatVND(listCTHD.get(i).getGiaBan()), listCTHD.get(i).getTongTien(), btnXoa};
                    model.addRow(row);
                } else {
                    Object row[] = {null, sanPham.getTenSP(), listCTHD.get(i).getSL(), "<HTML><strike>" + DDTienTe.FormatVND(sanPham.getGiaBan()) + "</strike>" + " - " + DDTienTe.FormatVND(listCTHD.get(i).getGiaBan()), listCTHD.get(i).getTongTien(), btnXoa};
                    model.addRow(row);
                }

            }
            tblCTHDD.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer(bfimage));
            setTongTien();
        }
    }

    public void setTongTien() {
        Double tongTien = 0.0;
        for (CTHDDatHang ct : PanelTrangChu_KH.listCT) {
            tongTien += ct.getTongTien();
        }
        txtTongTien.setText(DDTienTe.FormatVND(tongTien));
    }

    public void thayDoiGiaTriTT() {
        int index = tblCTHDD.getSelectedRow();
        if (index != -1) {
            try {
                Object soLuongUpdate = tblCTHDD.getValueAt(index, 2);
                String soLuongString = String.valueOf(soLuongUpdate);
                int soLuongInt = Integer.parseInt(soLuongString);
                SanPham sp = sanPhamDAO.selectbyID(listCTHD.get(index).getMaSP());
                if (soLuongInt > sp.getSlTrenKe()) {
                    MsgBox.alert(this, "Số lượng không đủ chỉ mua tối đa " + sp.getSlTrenKe() + " sản phẩm !");
                    tblCTHDD.setValueAt(listCTHD.get(index).getSL(), index, 2);
                } else {
                    listCTHD.get(index).setSL(soLuongInt);
                    tblCTHDD.setValueAt(DDTienTe.FormatVND(listCTHD.get(index).getTongTien()), index, 4);
                    setTongTien();
                }
            } catch (Exception e) {
//                MsgBox.alert(this, "Số lượng không được nhập chữ");
                if (tblCTHDD.getRowCount() > 0) {
                    tblCTHDD.setValueAt(listCTHD.get(index).getSL(), index, 2);
                }

            }

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblCTHDD = new HELP.Table();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblCTHDD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hình", "Tên Sản Phẩm", "Số lượng", "Đơn giá", "Tổng tiền", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCTHDD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblCTHDDMouseReleased(evt);
            }
        });
        tblCTHDD.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblCTHDDPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tblCTHDD);
        if (tblCTHDD.getColumnModel().getColumnCount() > 0) {
            tblCTHDD.getColumnModel().getColumn(5).setMaxWidth(60);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("GIỎ HÀNG");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tổng tiền");

        txtTongTien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTongTien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTongTien.setText("0 đ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Thanh Toán");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblCTHDDMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCTHDDMouseReleased
        if (tblCTHDD.getSelectedColumn() == 5) {
            PanelTrangChu_KH.listCT.remove(tblCTHDD.getSelectedRow());
            setTongTien();
            fillTable();
            PanelTrangChu_KH.setTienGioHang();
        }
    }//GEN-LAST:event_tblCTHDDMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ThanhToan_KHDialog thanhToan = new ThanhToan_KHDialog(null, true);
        this.dispose();
        gif.setVisible(true);
        new Thread() {
            @Override
            public void run() {
                thanhToan.setVisible(true);
                gif.dispose();
                this.stop();
            }
        }.start();


    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblCTHDDPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblCTHDDPropertyChange
        if (evt.getNewValue() == null) {
            gif.setVisible(true);
            new Thread() {
                @Override
                public void run() {
                    thayDoiGiaTriTT();
                    gif.dispose();
                }
            }.start();
        }
    }//GEN-LAST:event_tblCTHDDPropertyChange

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
            java.util.logging.Logger.getLogger(GioHangDaiLog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GioHangDaiLog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GioHangDaiLog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GioHangDaiLog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GioHangDaiLog dialog = new GioHangDaiLog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private HELP.Table tblCTHDD;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}
