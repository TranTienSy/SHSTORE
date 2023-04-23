/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package UI.Children.DaiLog;

import DAO.CTHDDatHangDAO;
import DAO.GGHoaDonDAO;
import DAO.GGSanPhamDAO;
import DAO.HDDatHangDAO;
import DAO.KeHangDAO;
import DAO.KhachHangDAO;
import DAO.SanPhamDAO;
import HELP.CellRenderer;
import Models.CTHDDatHang;
import Models.GGHoaDon;
import Models.GGSanPham;
import Models.HDDatHang;
import Models.KhachHang;
import Models.SanPham;
import UI.Children.LSDonHangPanel;
import UI.Children.PanelTrangChu_KH;
import UI.FormKH;
import Utils.Auth_KH;
import Utils.DDTienTe;
import Utils.MsgBox;
import Utils.Validation;
import Utils.XDate;
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
public class ThanhToan_KHDialog extends javax.swing.JDialog {

    /**
     * Creates new form ThanhToan_KHDialog
     */
    public ThanhToan_KHDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon/icons8-grocery-store-32.png")));
        this.setLocationRelativeTo(null);
        init();

    }
    SanPhamDAO sanPhamDAO = new SanPhamDAO();
    GGSanPhamDAO ggDAO = new GGSanPhamDAO();
    GGHoaDonDAO ggHDDAO = new GGHoaDonDAO();
    HDDatHangDAO hdDatHangDAO = new HDDatHangDAO();
    CTHDDatHangDAO ctHDDatHangDAO = new CTHDDatHangDAO();
    KhachHangDAO khDAO = new KhachHangDAO();
    KeHangDAO keHangDAO = new KeHangDAO();

    public void init() {
        fillTable();
        setFormHD(Auth_KH.user);
        btnHuy.setVisible(false);
        txtDiemHienCo.setText(String.valueOf(Auth_KH.user.getDiem()));
        rdoTienMat.setSelected(true);
    }

    public void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblXemLaiSP.getModel();
        model.setRowCount(0);
        List<CTHDDatHang> listCTHD = PanelTrangChu_KH.listCT;

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
                GGSanPham sp = ggDAO.selectByCheckHieuLuc(listCTHD.get(i).getMaSP());

                Object row[] = {null, sanPham.getTenSP() + " - SL ( " + listCTHD.get(i).getSL() + " )", DDTienTe.FormatVND(listCTHD.get(i).getTongTien())};
                model.addRow(row);
            }
            tblXemLaiSP.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer(bfimage));
            getTamTinh();
            txtSLSP.setText("(" + listCTHD.size() + " sản phẩm)");
            getThanhTien();
        }
    }

    public void suDungMaGG() {
        GGHoaDon ggHD = ggHDDAO.selectbyIDandNgayAPDung(txtMaGG.getText());

        if (ggHD == null) {
            MsgBox.alert(this, "Mã giảm giá không hợp lệ !");
        } else {
            if (ggHD.getHoaDonAD() > getTamTinh()) {
                MsgBox.alert(this, "Mã giảm giá này chỉ áp dụng cho HĐ trên " + ggHD.getHoaDonAD());
            } else {
                getTienGG(ggHD);
                txtMaGG.setEnabled(false);
                btnADmaGG.setVisible(false);
                btnHuy.setVisible(true);
                getThanhTien();
            }
        }
    }

    public Double getTienGG(GGHoaDon ggHD) {
        Double tienGG = 0.0;
        if (ggHD.isHinhThuc() == true) {
            tienGG = ggHD.getMucGG();
            txtTienGG.setText(String.valueOf(tienGG));
        } else {
            tienGG = getTamTinh() * ggHD.getMucGG() / 100;
            txtTienGG.setText(String.valueOf(tienGG));
        }
        return tienGG;
    }

    public Double getTamTinh() {
        Double tamTinh = 0.0;
        for (CTHDDatHang ct : PanelTrangChu_KH.listCT) {
            tamTinh += ct.getTongTien();
        }
        txtTamTinh.setText(DDTienTe.FormatVND(tamTinh));
        return tamTinh;
    }

    public void setFormHD(KhachHang kh) {
        txtHoTen.setText(kh.getTenKH());
        txtSDT.setText(kh.getSDT());
        txtDiaChi.setText(kh.getDiaChi());
    }

    public Double getThanhTien() {
        Double thanhTien = (getTamTinh() + 35000) - (Double.parseDouble(txtTienGG.getText()) + Integer.parseInt(txtDiemSD.getText()));

        if (thanhTien <= 0) {
            txtTongCong.setText(DDTienTe.FormatVND(0.0));
            thanhTien = 0.0;
        } else {
            txtTongCong.setText(DDTienTe.FormatVND(thanhTien));
        }
        return thanhTien;
    }

    public HDDatHang getHDDatHang() {
        HDDatHang hdDatHang = new HDDatHang();
        hdDatHang.setSdtKH(Auth_KH.user.getSDT());
        hdDatHang.setTrangThai(1);
        hdDatHang.setNgayDat(XDate.toString(XDate.getDate(), "yyyy-MM-dd HH:mm:ss"));
        hdDatHang.setDiaChiGH(txtDiaChi.getText());
        if (txtMaGG.isEnabled() == false) {
            hdDatHang.setMaGG(txtMaGG.getText());
        } else {
            hdDatHang.setMaGG(null);
        }
        hdDatHang.setHinhThucTT(rdoTienMat.isSelected() ? true : false);
        hdDatHang.setTienKM(Double.parseDouble(txtTienGG.getText()));
        hdDatHang.setDiemSD(Integer.parseInt(txtDiemSD.getText()));
        return hdDatHang;
    }

    public void thanhToan() {
        try {

            if (MsgBox.confrim(this, "Bạn thật sự muốn thanh toán ?")) {
                String maHD = hdDatHangDAO.insertHDreturnMaHD(getHDDatHang());
                ctHDDatHangDAO.insert(maHD, PanelTrangChu_KH.listCT);
                MsgBox.alert(this, "Thanh toán thành công - Cảm ơn bạn đã mua hàng");
                PanelTrangChu_KH.listCT.clear();
                keHangDAO.setListSPTrenKe(keHangDAO.selectBySeach(null, null, null, null, null, null));
                PanelTrangChu_KH.fillTbale();
                LSDonHangPanel.fillTableHD(null, null, null, XDate.toString(XDate.getDate(), "yyyy-MM-dd"), "1", Auth_KH.user.getSDT());
                tichDiemKH();
                this.dispose();
            }

        } catch (Exception e) {
            MsgBox.alert(this, "Thanh toán thất bại");
        }
    }

    public void tichDiemKH() {
        khDAO.tichDiem(getThanhTien(), Auth_KH.user.getSDT());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgrHinhThucTT = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        rdoOnline = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        rdoTienMat = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblXemLaiSP = new HELP.Table();
        jSeparator2 = new javax.swing.JSeparator();
        txtMaGG = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtDiemHienCo = new javax.swing.JLabel();
        btnSDDiem = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        txtTamTinh = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtTienGG = new javax.swing.JLabel();
        txtDiemSD = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        txtTongCong = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtSLSP = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnADmaGG = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Thông tin nhận hàng");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel2.setText("Họ và tên");

        txtHoTen.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel3.setText("Địa chỉ");

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane1.setViewportView(txtDiaChi);

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel5.setText("Ghi chú");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(204, 0, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Vui lòng kiểm tra hoặc chỉnh sửa thông tin giao hàng");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel4.setText("Mã KH - SĐT");

        txtSDT.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addComponent(txtHoTen)
                    .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addGap(23, 23, 23))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Thanh Toán");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bgrHinhThucTT.add(rdoOnline);
        rdoOnline.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdoOnline.setText("Thanh toán online");

        bgrHinhThucTT.add(rdoTienMat);
        rdoTienMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdoTienMat.setText("Thanh toán tiền mặt");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dollar.png"))); // NOI18N

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/credit-card.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rdoTienMat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rdoOnline)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoTienMat, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(rdoOnline, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(235, 235, 235));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Đơn Hàng");

        tblXemLaiSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Hình", "Tên SP + Số lượng", "Tổng tiền"
            }
        ));
        tblXemLaiSP.setRowHeight(60);
        jScrollPane3.setViewportView(tblXemLaiSP);
        if (tblXemLaiSP.getColumnModel().getColumnCount() > 0) {
            tblXemLaiSP.getColumnModel().getColumn(1).setMinWidth(190);
        }

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel10.setText("Mã giảm giá");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel11.setText("Điểm hiện có: ");

        txtDiemHienCo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtDiemHienCo.setForeground(new java.awt.Color(204, 51, 0));
        txtDiemHienCo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtDiemHienCo.setText("0");

        btnSDDiem.setBackground(new java.awt.Color(0, 204, 255));
        btnSDDiem.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnSDDiem.setForeground(new java.awt.Color(255, 255, 255));
        btnSDDiem.setText("Sử dụng");
        btnSDDiem.setBorder(null);
        btnSDDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSDDiemActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Tạm Tính");

        txtTamTinh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTamTinh.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTamTinh.setText("0 đ");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Giảm giá");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Điểm SD");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Phí vận chuyển");

        txtTienGG.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTienGG.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTienGG.setText("0");

        txtDiemSD.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDiemSD.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtDiemSD.setText("0");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("35.000 đ");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setText("Tổng cộng");

        txtTongCong.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtTongCong.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTongCong.setText("100.000 đ");

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Đặt Hàng");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 102, 102));
        jLabel23.setText("< quay vể giỏ hàng");
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
        });

        txtSLSP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSLSP.setText("(0 sản phẩm)");

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnADmaGG.setBackground(new java.awt.Color(0, 204, 255));
        btnADmaGG.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnADmaGG.setForeground(new java.awt.Color(255, 255, 255));
        btnADmaGG.setText("Áp dụng");
        btnADmaGG.setBorder(null);
        btnADmaGG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnADmaGGActionPerformed(evt);
            }
        });
        jPanel5.add(btnADmaGG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 79, 30));

        btnHuy.setBackground(new java.awt.Color(204, 0, 0));
        btnHuy.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("Hủy mã");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });
        jPanel5.add(btnHuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4)
                    .addComponent(jSeparator3)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTamTinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTienGG, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDiemSD, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTongCong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtSLSP))
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtDiemHienCo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSDDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtMaGG, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(9, 9, 9)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtSLSP)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMaGG, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11)
                    .addComponent(btnSDDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiemHienCo))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtTamTinh))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtTienGG))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtDiemSD))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtTongCong))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnADmaGGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnADmaGGActionPerformed
        suDungMaGG();
    }//GEN-LAST:event_btnADmaGGActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        btnADmaGG.setVisible(true);
        btnHuy.setVisible(false);
        txtMaGG.setEnabled(true);
        txtMaGG.setText("");
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnSDDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSDDiemActionPerformed
        String diem = MsgBox.prompt(this, "Nhập điểm muốn sử dụng");
        if (Integer.parseInt(diem) > Integer.parseInt(txtDiemHienCo.getText())) {
            MsgBox.alert(this, "Bạn chỉ sử dụng tối đa " + txtDiemHienCo.getText());
        } else {
            Double diem10pt = getTamTinh() * 10 / 100;
            if (Double.parseDouble(diem) > diem10pt) {
                MsgBox.alert(this, "Bạn chỉ sử dụng tối đa " + diem10pt.toString() + " dựa trên 10% giá trị đơn hàng !");
            } else {
                txtDiemSD.setText(diem);
                getThanhTien();
            }
        }
    }//GEN-LAST:event_btnSDDiemActionPerformed

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        this.dispose();
        GioHangDaiLog ghDL = new GioHangDaiLog(null, true);
        ghDL.setVisible(true);
    }//GEN-LAST:event_jLabel23MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (txtDiaChi.getText().trim().isEmpty()) {
            MsgBox.alert(this, "Vui Lòng Nhập địa chỉ giao hàng");
            return;
        }
        thanhToan();
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
            java.util.logging.Logger.getLogger(ThanhToan_KHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThanhToan_KHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThanhToan_KHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThanhToan_KHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThanhToan_KHDialog dialog = new ThanhToan_KHDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup bgrHinhThucTT;
    private javax.swing.JButton btnADmaGG;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSDDiem;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JRadioButton rdoOnline;
    private javax.swing.JRadioButton rdoTienMat;
    private HELP.Table tblXemLaiSP;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JLabel txtDiemHienCo;
    private javax.swing.JLabel txtDiemSD;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaGG;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JLabel txtSLSP;
    private javax.swing.JLabel txtTamTinh;
    private javax.swing.JLabel txtTienGG;
    private javax.swing.JLabel txtTongCong;
    // End of variables declaration//GEN-END:variables
}
