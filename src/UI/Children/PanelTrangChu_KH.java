/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UI.Children;

import COMPONENT.PanelSanpham;
import DAO.GGSanPhamDAO;
import DAO.KeHangDAO;
import DAO.SanPhamDAO;
import Models.CTHDDatHang;
import Models.HDBanLe;
import Models.SanPham;
import UI.Children.DaiLog.GioHangDaiLog;
import UI.Children.DaiLog.gifLoadDiaLog;
import UI.MainJF;
import Utils.DDTienTe;
import Utils.MsgBox;
import Utils.XImage;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author huuho
 */
public class PanelTrangChu_KH extends javax.swing.JPanel {

    public static List<CTHDDatHang> listCT = new ArrayList<>();

    public PanelTrangChu_KH() {

        initComponents();
        fillTbale();
    }

    public static KeHangDAO khDAO = new KeHangDAO();
    public static GGSanPhamDAO ggSPDAO = new GGSanPhamDAO();
    public static SanPhamDAO spDAO = new SanPhamDAO();
    gifLoadDiaLog gif = new gifLoadDiaLog(null, false);

    public static void fillTbale() {
        panelMucSP1.removeAll();
        for (SanPham spTrenKe : khDAO.getlistSPTrenKe()) {
            PanelSanpham sp = new PanelSanpham();
            if (ggSPDAO.selectByCheckHieuLuc(spTrenKe.getMaSP()) != null) {
                sp.setGiaContent("<HTML><strike>" + DDTienTe.FormatVND(spTrenKe.getGiaBan()) + "</strike>" + " " + DDTienTe.FormatVND(spTrenKe.getGiaGiam()));
            } else {
                sp.setGiaContent(DDTienTe.FormatVND(spTrenKe.getGiaBan()));
            }
            sp.setSoLuongContent(String.valueOf(spTrenKe.getSlTrenKe()));
            sp.setTitleContent(spTrenKe.getTenSP());
            sp.setIcon(XImage.toScaleImage(spTrenKe.getHinh()));
            panelMucSP1.add(sp);
            MouseListener listener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        CTHDDatHang ctHD = new CTHDDatHang();
                        ctHD.setMaSP(String.valueOf(spTrenKe.getMaSP()));
                        ctHD.setSL(Integer.parseInt("1"));
                        ctHD.setGiaBan((spTrenKe.getGiaGiam()));
                        boolean ck = false;
                        for (int i = 0; i < listCT.size(); i++) {
                            if (listCT.get(i).getMaSP().equals(ctHD.getMaSP())) {
                                SanPham sp = spDAO.selectbyID(ctHD.getMaSP());
                                if (listCT.get(i).getSL() >= sp.getSlTrenKe()) {
                                    MsgBox.alert(null, "Bạn chỉ mua tối đa SL là " + sp.getSlTrenKe() + "cho sản phẩm này");
                                    ck = true;
                                    break;
                                } else {
                                    listCT.get(i).setSL(listCT.get(i).getSL() + 1);
                                    MsgBox.alert(null, "Thêm vào giỏ hàng thành công");
                                    ck = true;
                                    setTienGioHang();
                                    break;
                                }

                            }
                        }
                        if (ck == false) {
                            SanPham sp = spDAO.selectbyID(ctHD.getMaSP());
                            if (sp.getSlTrenKe() == 0) {
                                MsgBox.alert(null, "Sản phẩm tạm hết hàng !");
                            } else {
                                listCT.add(ctHD);
                                MsgBox.alert(null, "Thêm vào giỏ hàng thành công");
                                setTienGioHang();
                            }

                        }
                    }

                }
            };
            sp.addMouseListener(listener);
        }
        setTienGioHang();
        panelMucSP1.repaint();
    }

    public static void setTienGioHang() {
        Double tien = 0.0;
        for (CTHDDatHang ct : listCT) {
            tien += ct.getTongTien();
        }
        lblTienGioHang.setText(DDTienTe.FormatVND(tien));
    }

    public void timKiemByKhoanGia() {
        int indexKhoanGia = cboKhoanGia.getSelectedIndex();
        if (indexKhoanGia == 0) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, null, null, null, null));
            cboTheoMuc.setSelectedIndex(0);
            this.fillTbale();
        } else if (indexKhoanGia == 1) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, null, 50000.0, null, null));
            this.fillTbale();
        } else if (indexKhoanGia == 2) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, 50000.0, 100000.0, null, null));
            this.fillTbale();
        } else if (indexKhoanGia == 3) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, 100000.0, 200000.0, null, null));
            this.fillTbale();
        } else if (indexKhoanGia == 4) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, 200000.0, null, null, null));
            this.fillTbale();
        }
    }

    public void sapXepTheoGia() {
        List<SanPham> listSP = khDAO.getlistSPTrenKe();
        if (cboSapXepGia.getSelectedIndex() == 2) {
            Collections.sort(listSP, new Comparator<SanPham>() {
                @Override
                public int compare(SanPham o1, SanPham o2) {
                    return (int) (o1.getGiaBan() - o2.getGiaBan());
                }
            });
        } else if (cboSapXepGia.getSelectedIndex() == 1) {
            Collections.sort(listSP, new Comparator<SanPham>() {
                @Override
                public int compare(SanPham o1, SanPham o2) {
                    return (int) (o2.getGiaBan() - o1.getGiaBan());
                }

            });
        }
        this.fillTbale();

    }

    public void timKiem() {
        int indexTheoMuc = cboTheoMuc.getSelectedIndex();
        if (indexTheoMuc == 0) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, null, null, null, null));
            cboKhoanGia.setSelectedIndex(0);
            fillTbale();
        } else if (indexTheoMuc == 1) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(txtNDTim.getText(), null, null, null, null, null));
            fillTbale();
        } else if (indexTheoMuc == 2) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, txtNDTim.getText(), null, null, null, null));
            fillTbale();
        } else if (indexTheoMuc == 3) {
            khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, null, null, null, txtNDTim.getText()));
            fillTbale();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDanhmuc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlSanpham = new javax.swing.JPanel();
        panelMucSP1 = new COMPONENT.PanelMucSP();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lblTienGioHang = new javax.swing.JLabel();
        cboSapXepGia = new javax.swing.JComboBox<>();
        cboKhoanGia = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cboTheoMuc = new javax.swing.JComboBox<>();
        txtNDTim = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        lblDanhmuc.setFont(new java.awt.Font("SansSerif", 3, 36)); // NOI18N
        lblDanhmuc.setForeground(new java.awt.Color(0, 102, 102));
        lblDanhmuc.setText("Cửa hàng SH");

        pnlSanpham.setLayout(new javax.swing.BoxLayout(pnlSanpham, javax.swing.BoxLayout.Y_AXIS));

        panelMucSP1.setTitlemucSP("Sản Phẩm");
        pnlSanpham.add(panelMucSP1);

        jScrollPane1.setViewportView(pnlSanpham);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/trolley.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblTienGioHang.setBackground(new java.awt.Color(255, 255, 255));
        lblTienGioHang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTienGioHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTienGioHang.setText("0 đ");
        lblTienGioHang.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(lblTienGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblTienGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        cboSapXepGia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboSapXepGia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp theo giá", "Giảm dần", "Tăng dần", " " }));
        cboSapXepGia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSapXepGiaItemStateChanged(evt);
            }
        });

        cboKhoanGia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboKhoanGia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Khoảng giá (ALL)", "Dưới 50.000VND", "50.000VND -100.000VND", "100.000VND - 200.000VND", "200.000VND Trở lên", " " }));
        cboKhoanGia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboKhoanGiaItemStateChanged(evt);
            }
        });

        cboTheoMuc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTheoMuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn mục tìm kiếm", "Mã Sản Phẩm", "Tên Sản Phẩm", "Barcode" }));
        cboTheoMuc.setToolTipText("");
        cboTheoMuc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTheoMucItemStateChanged(evt);
            }
        });

        txtNDTim.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 153, 153)));
        txtNDTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNDTimActionPerformed(evt);
            }
        });
        txtNDTim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNDTimKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNDTimKeyTyped(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Hiển thị (ALL)");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblDanhmuc, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 476, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNDTim, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboTheoMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboKhoanGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cboSapXepGia, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblDanhmuc))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 25, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNDTim, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboTheoMuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboSapXepGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboKhoanGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        GioHangDaiLog ghDaiLog = new GioHangDaiLog(null, true);
        gif.setVisible(true);
        new Thread() {
            @Override
            public void run() {
                ghDaiLog.setVisible(true);
                gif.dispose();
                this.stop();
            }
        }.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cboSapXepGiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSapXepGiaItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            gif.setVisible(true);
            new Thread() {
                @Override
                public void run() {
                    sapXepTheoGia();
                    gif.dispose();
                    this.stop();
                }
            }.start();
        }
    }//GEN-LAST:event_cboSapXepGiaItemStateChanged

    private void cboKhoanGiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboKhoanGiaItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            timKiemByKhoanGia();

        }


    }//GEN-LAST:event_cboKhoanGiaItemStateChanged

    private void cboTheoMucItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTheoMucItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTheoMucItemStateChanged

    private void txtNDTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNDTimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNDTimActionPerformed

    private void txtNDTimKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNDTimKeyTyped
        if (evt.getKeyChar() == 10) {
            timKiem();
        }
    }//GEN-LAST:event_txtNDTimKeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        khDAO.setListSPTrenKe(khDAO.selectBySeach(null, null, null, null, null, null));
        fillTbale();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtNDTimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNDTimKeyReleased

    }//GEN-LAST:event_txtNDTimKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboKhoanGia;
    private javax.swing.JComboBox<String> cboSapXepGia;
    private javax.swing.JComboBox<String> cboTheoMuc;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDanhmuc;
    public static javax.swing.JLabel lblTienGioHang;
    public static COMPONENT.PanelMucSP panelMucSP1;
    private javax.swing.JPanel pnlSanpham;
    private javax.swing.JTextField txtNDTim;
    // End of variables declaration//GEN-END:variables
}
