package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTable;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IHoaDonService;
import com.tourmanagement.service.interfaces.IChiTietThanhToanService;
import com.tourmanagement.service.interfaces.IDatTourService;
import com.tourmanagement.entity.HoaDon;
import com.tourmanagement.entity.ChiTietThanhToan;
import com.tourmanagement.entity.DatTour;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ViewInvoiceDialog extends JDialog {
    private IHoaDonService hoaDonService;
    private IChiTietThanhToanService chiTietThanhToanService;
    private IDatTourService datTourService;
    
    private HoaDon hoaDon;
    private DatTour datTour;
    
    // UI Components
    private JPanel invoiceInfoPanel;
    private CustomTable paymentDetailsTable;
    private CustomButton printButton;
    private CustomButton closeButton;
    
    public ViewInvoiceDialog(Frame parent, int maHoaDon) {
        super(parent, "Chi tiết hóa đơn", true);
        
        this.hoaDonService = ServiceFactory.getInstance().getHoaDonService();
        this.chiTietThanhToanService = ServiceFactory.getInstance().getChiTietThanhToanService();
        this.datTourService = ServiceFactory.getInstance().getDatTourService();
        
        // Lấy thông tin hóa đơn
        this.hoaDon = hoaDonService.timTheoMa(maHoaDon);
        
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(parent, 
                "Không tìm thấy hóa đơn với mã " + maHoaDon, 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Lấy thông tin đặt tour
        this.datTour = datTourService.timTheoMa(hoaDon.getMaDatTour());
        
        initComponents();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        CustomLabel titleLabel = new CustomLabel("CHI TIẾT HÓA ĐƠN #" + hoaDon.getMaHoaDon());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 55, 153));
        
        titlePanel.add(titleLabel);
        
        // Invoice info panel
        invoiceInfoPanel = createInvoiceInfoPanel();
        
        // Payment details panel
        JPanel paymentDetailsPanel = createPaymentDetailsPanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        printButton = new CustomButton("In hóa đơn");
        printButton.setStyleSecondary();
        
        closeButton = new CustomButton("Đóng");
        closeButton.setStylePrimary();
        
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        
        // Add panels to main panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(invoiceInfoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(paymentDetailsPanel);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to dialog
        getContentPane().add(mainPanel);
        
        // Set dialog properties
        pack();
        
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        
        // Add action listeners
        closeButton.addActionListener(e -> dispose());
        printButton.addActionListener(e -> printInvoice());
    }
    
    private JPanel createInvoiceInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Format dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(245, 250, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 240)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Invoice header
        CustomLabel headerLabel = new CustomLabel("Thông tin hóa đơn");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        infoPanel.add(headerLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Mã hóa đơn
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Mã hóa đơn:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel maHoaDonLabel = new JLabel("#" + hoaDon.getMaHoaDon());
        maHoaDonLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(maHoaDonLabel, gbc);
        
        // Mã đặt tour
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Mã đặt tour:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel maDatTourLabel = new JLabel("#" + hoaDon.getMaDatTour());
        infoPanel.add(maDatTourLabel, gbc);
        
        // Thông tin đặt tour
        if (datTour != null) {
            // Tour name spacer
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            infoPanel.add(Box.createVerticalStrut(10), gbc);
            
            gbc.gridwidth = 1;
            
            // Khách hàng
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.EAST;
            infoPanel.add(new JLabel("Mã khách hàng:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel maKHLabel = new JLabel("#" + datTour.getMaKH());
            infoPanel.add(maKHLabel, gbc);
            
            // Ngày đặt
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.EAST;
            infoPanel.add(new JLabel("Ngày đặt:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel ngayDatLabel = new JLabel(dateFormat.format(datTour.getNgayDat()));
            infoPanel.add(ngayDatLabel, gbc);
            
            // Số lượng người
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.EAST;
            infoPanel.add(new JLabel("Số lượng người:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel soLuongNguoiLabel = new JLabel(String.valueOf(datTour.getSoLuongNguoi()));
            infoPanel.add(soLuongNguoiLabel, gbc);
        }
        
        // Payment spacer
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        infoPanel.add(Box.createVerticalStrut(10), gbc);
        
        gbc.gridwidth = 1;
        
        // Payment info header
        CustomLabel paymentHeaderLabel = new CustomLabel("Thông tin thanh toán");
        paymentHeaderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        infoPanel.add(paymentHeaderLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Tổng tiền
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Tổng tiền:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel tongTienLabel = new JLabel(currencyFormat.format(hoaDon.getTongTien()));
        tongTienLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tongTienLabel.setForeground(new Color(30, 55, 153));
        infoPanel.add(tongTienLabel, gbc);
        
        // Đã thanh toán
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Đã thanh toán:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel daThanhToanLabel = new JLabel(currencyFormat.format(hoaDon.getDaThanhToan()));
        daThanhToanLabel.setForeground(new Color(41, 166, 41)); // Green
        infoPanel.add(daThanhToanLabel, gbc);
        
        // Còn lại
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Còn lại:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel conLaiLabel = new JLabel(currencyFormat.format(hoaDon.getConLai()));
        if (hoaDon.getConLai().doubleValue() > 0) {
            conLaiLabel.setForeground(new Color(204, 51, 0)); // Red
        }
        infoPanel.add(conLaiLabel, gbc);
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel trangThaiLabel = new JLabel(hoaDon.getTrangThai());
        if ("Đã thanh toán".equals(hoaDon.getTrangThai())) {
            trangThaiLabel.setForeground(new Color(41, 166, 41)); // Green
        } else if ("Chưa thanh toán".equals(hoaDon.getTrangThai())) {
            trangThaiLabel.setForeground(new Color(204, 51, 0)); // Red
        } else {
            trangThaiLabel.setForeground(new Color(255, 165, 0)); // Orange
        }
        infoPanel.add(trangThaiLabel, gbc);
        
        // Phương thức thanh toán
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Phương thức thanh toán:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel phuongThucLabel = new JLabel(hoaDon.getPhuongThucThanhToan());
        infoPanel.add(phuongThucLabel, gbc);
        
        // Ghi chú
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(new JLabel("Ghi chú:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        JTextArea ghiChuArea = new JTextArea(3, 20);
        ghiChuArea.setText(hoaDon.getGhiChu() != null ? hoaDon.getGhiChu() : "");
        ghiChuArea.setEditable(false);
        ghiChuArea.setLineWrap(true);
        ghiChuArea.setWrapStyleWord(true);
        ghiChuArea.setBackground(new Color(245, 245, 245));
        JScrollPane ghiChuScrollPane = new JScrollPane(ghiChuArea);
        infoPanel.add(ghiChuScrollPane, gbc);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPaymentDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết thanh toán"));
        
        // Create table
        String[] columnNames = {"Mã TT", "Ngày thanh toán", "Số tiền", "Phương thức", "Ghi chú", "Nhân viên"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Load payment details
        List<ChiTietThanhToan> paymentDetails = chiTietThanhToanService.layDanhSachTheoHoaDon(hoaDon.getMaHoaDon());
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Add to table
        for (ChiTietThanhToan payment : paymentDetails) {
            Object[] rowData = {
                payment.getMaThanhToan(),
                dateFormat.format(payment.getNgayThanhToan()),
                currencyFormat.format(payment.getSoTien()),
                payment.getPhuongThucThanhToan(),
                payment.getGhiChu(),
                "#" + payment.getMaNV()
            };
            model.addRow(rowData);
        }
        
        paymentDetailsTable = new CustomTable(model);
        JScrollPane scrollPane = new JScrollPane(paymentDetailsTable);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void printInvoice() {
        // This would be implemented to print the invoice
        JOptionPane.showMessageDialog(this, 
            "Chức năng in hóa đơn sẽ được triển khai sau", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}