package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IHoaDonService;
import com.tourmanagement.service.interfaces.IChiTietThanhToanService;
import com.tourmanagement.service.interfaces.INhanVienService;
import com.tourmanagement.entity.HoaDon;
import com.tourmanagement.entity.NhanVien;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPaymentDialog extends JDialog {
    private IHoaDonService hoaDonService;
    private IChiTietThanhToanService chiTietThanhToanService;
    private INhanVienService nhanVienService;
    
    private HoaDon hoaDon;
    private NhanVien nhanVien;
    private boolean saveSuccess = false;
    
    // UI Components
    private JLabel maHoaDonLabel;
    private JLabel maDatTourLabel;
    private JLabel tongTienLabel;
    private JLabel daThanhToanLabel;
    private JLabel conLaiLabel;
    private JLabel trangThaiLabel;
    
    private CustomTextField soTienField;
    private JComboBox<String> phuongThucThanhToanComboBox;
    private JTextArea ghiChuArea;
    
    public AddPaymentDialog(Frame parent, int maHoaDon, int maNhanVien) {
        super(parent, "Thêm Thanh Toán Mới", true);
        
        this.hoaDonService = ServiceFactory.getInstance().getHoaDonService();
        this.chiTietThanhToanService = ServiceFactory.getInstance().getChiTietThanhToanService();
        this.nhanVienService = ServiceFactory.getInstance().getNhanVienService();
        
        this.hoaDon = hoaDonService.timTheoMa(maHoaDon);
        this.nhanVien = nhanVienService.timTheoMa(maNhanVien);
        
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(parent, 
                "Không tìm thấy hóa đơn với mã " + maHoaDon, 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        if (nhanVien == null) {
            JOptionPane.showMessageDialog(parent, 
                "Không tìm thấy thông tin nhân viên", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
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
        
        CustomLabel titleLabel = new CustomLabel("THÊM THANH TOÁN CHO HÓA ĐƠN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 55, 153));
        
        titlePanel.add(titleLabel);
        
        // Invoice info panel
        JPanel invoiceInfoPanel = new JPanel(new GridBagLayout());
        invoiceInfoPanel.setBackground(new Color(245, 250, 255));
        invoiceInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 240)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(5, 5, 5, 5);
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;
        
        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Mã hóa đơn
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 0;
        gbcInfo.anchor = GridBagConstraints.EAST;
        gbcInfo.weightx = 0.0;
        invoiceInfoPanel.add(new JLabel("Mã hóa đơn:"), gbcInfo);
        
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 0;
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.weightx = 1.0;
        maHoaDonLabel = new JLabel("#" + hoaDon.getMaHoaDon());
        maHoaDonLabel.setFont(new Font("Arial", Font.BOLD, 14));
        invoiceInfoPanel.add(maHoaDonLabel, gbcInfo);
        
        // Mã đặt tour
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 1;
        gbcInfo.anchor = GridBagConstraints.EAST;
        gbcInfo.weightx = 0.0;
        invoiceInfoPanel.add(new JLabel("Mã đặt tour:"), gbcInfo);
        
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 1;
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.weightx = 1.0;
        maDatTourLabel = new JLabel("#" + hoaDon.getMaDatTour());
        invoiceInfoPanel.add(maDatTourLabel, gbcInfo);
        
        // Tổng tiền
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 2;
        gbcInfo.anchor = GridBagConstraints.EAST;
        gbcInfo.weightx = 0.0;
        invoiceInfoPanel.add(new JLabel("Tổng tiền:"), gbcInfo);
        
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 2;
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.weightx = 1.0;
        tongTienLabel = new JLabel(currencyFormat.format(hoaDon.getTongTien()));
        tongTienLabel.setForeground(new Color(30, 55, 153));
        tongTienLabel.setFont(new Font("Arial", Font.BOLD, 14));
        invoiceInfoPanel.add(tongTienLabel, gbcInfo);
        
        // Đã thanh toán
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 3;
        gbcInfo.anchor = GridBagConstraints.EAST;
        gbcInfo.weightx = 0.0;
        invoiceInfoPanel.add(new JLabel("Đã thanh toán:"), gbcInfo);
        
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 3;
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.weightx = 1.0;
        daThanhToanLabel = new JLabel(currencyFormat.format(hoaDon.getDaThanhToan()));
        daThanhToanLabel.setForeground(new Color(41, 166, 41));
        invoiceInfoPanel.add(daThanhToanLabel, gbcInfo);
        
        // Còn lại
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 4;
        gbcInfo.anchor = GridBagConstraints.EAST;
        gbcInfo.weightx = 0.0;
        invoiceInfoPanel.add(new JLabel("Còn lại:"), gbcInfo);
        
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 4;
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.weightx = 1.0;
        conLaiLabel = new JLabel(currencyFormat.format(hoaDon.getConLai()));
        conLaiLabel.setForeground(new Color(204, 51, 0));
        conLaiLabel.setFont(new Font("Arial", Font.BOLD, 14));
        invoiceInfoPanel.add(conLaiLabel, gbcInfo);
        
        // Trạng thái
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 5;
        gbcInfo.anchor = GridBagConstraints.EAST;
        gbcInfo.weightx = 0.0;
        invoiceInfoPanel.add(new JLabel("Trạng thái:"), gbcInfo);
        
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 5;
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.weightx = 1.0;
        trangThaiLabel = new JLabel(hoaDon.getTrangThai());
        if ("Đã thanh toán".equals(hoaDon.getTrangThai())) {
            trangThaiLabel.setForeground(new Color(41, 166, 41));
        } else if ("Chưa thanh toán".equals(hoaDon.getTrangThai())) {
            trangThaiLabel.setForeground(new Color(204, 51, 0));
        } else {
            trangThaiLabel.setForeground(new Color(255, 165, 0));
        }
        invoiceInfoPanel.add(trangThaiLabel, gbcInfo);
        
        // Form panel for payment details
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Số tiền thanh toán
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Số tiền thanh toán:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        soTienField = new CustomTextField(15);
        // Đề xuất số tiền bằng số tiền còn lại
        if (hoaDon.getConLai() != null && hoaDon.getConLai().compareTo(BigDecimal.ZERO) > 0) {
            soTienField.setText(hoaDon.getConLai().toString());
        }
        formPanel.add(soTienField, gbc);
        
        // Phương thức thanh toán
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phương thức thanh toán:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] phuongThucOptions = {"Tiền mặt", "Chuyển khoản", "Thẻ tín dụng/ghi nợ", "Ví điện tử"};
        phuongThucThanhToanComboBox = new JComboBox<>(phuongThucOptions);
        formPanel.add(phuongThucThanhToanComboBox, gbc);
        
        // Ghi chú
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ghi chú:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        ghiChuArea = new JTextArea(3, 20);
        ghiChuArea.setLineWrap(true);
        ghiChuArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(ghiChuArea);
        formPanel.add(scrollPane, gbc);
        
        // Reset fill
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nhân viên thực hiện
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nhân viên thực hiện:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        JLabel nhanVienLabel = new JLabel(nhanVien.getHoTen() + " (#" + nhanVien.getMaNV() + ")");
        formPanel.add(nhanVienLabel, gbc);
        
        // Ngày thanh toán
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ngày thanh toán:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JLabel ngayThanhToanLabel = new JLabel(dateFormat.format(new Date()));
        formPanel.add(ngayThanhToanLabel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        CustomButton saveButton = new CustomButton("Thanh Toán");
        saveButton.setStylePrimary();
        
        CustomButton cancelButton = new CustomButton("Hủy");
        cancelButton.setStyleDanger();
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(invoiceInfoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(formPanel);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to dialog
        getContentPane().add(mainPanel);
        
        // Set dialog properties
        pack();
        
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Add action listeners
        saveButton.addActionListener(e -> savePayment());
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void savePayment() {
        try {
            // Validate and get data from form
            String soTienStr = soTienField.getText().trim();
            if (soTienStr.isEmpty()) {
                throw new BusinessLogicException("Vui lòng nhập số tiền thanh toán");
            }
            
            BigDecimal soTien;
            try {
                soTien = new BigDecimal(soTienStr.replace(",", ""));
            } catch (NumberFormatException e) {
                throw new BusinessLogicException("Số tiền không hợp lệ");
            }
            
            if (soTien.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessLogicException("Số tiền thanh toán phải lớn hơn 0");
            }
            
            if (hoaDon.getConLai() != null && soTien.compareTo(hoaDon.getConLai()) > 0) {
                throw new BusinessLogicException("Số tiền thanh toán không thể lớn hơn số tiền còn lại (" + 
                                                 hoaDon.getConLai() + " VNĐ)");
            }
            
            String phuongThucThanhToan = (String) phuongThucThanhToanComboBox.getSelectedItem();
            String ghiChu = ghiChuArea.getText().trim();
            
            // Thêm chi tiết thanh toán
            boolean success = chiTietThanhToanService.themChiTietThanhToan(
                hoaDon.getMaHoaDon(),
                new Date(),
                soTien,
                phuongThucThanhToan,
                ghiChu,
                nhanVien.getMaNV()
            );
            
            if (success) {
                saveSuccess = true;
                JOptionPane.showMessageDialog(this, 
                    "Thanh toán thành công", 
                    "Thành Công", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không thể thực hiện thanh toán", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (BusinessLogicException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Đã xảy ra lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSaveSuccess() {
        return saveSuccess;
    }
}