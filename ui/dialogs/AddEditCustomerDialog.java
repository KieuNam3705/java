package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IKhachHangService;
import com.tourmanagement.entity.KhachHang;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditCustomerDialog extends JDialog {
    private IKhachHangService khachHangService;
    private KhachHang khachHang;
    private boolean isEditMode;
    private boolean saveSuccess = false;
    
    // UI Components
    private CustomTextField hoTenField;
    private CustomTextField cmndField;
    private CustomTextField soDienThoaiField;
    private CustomTextField emailField;
    private CustomTextField diaChiField;
    private JFormattedTextField ngaySinhField;
    private JComboBox<String> gioiTinhComboBox;
    private CustomTextField quocTichField;
    
    public AddEditCustomerDialog(Frame parent, KhachHang khachHang) {
        super(parent, khachHang == null ? "Thêm Khách Hàng Mới" : "Sửa Thông Tin Khách Hàng", true);
        this.khachHangService = ServiceFactory.getInstance().getKhachHangService();
        this.khachHang = khachHang;
        this.isEditMode = (khachHang != null);
        
        initComponents();
        if (isEditMode) {
            loadCustomerData();
        }
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        CustomLabel titleLabel = new CustomLabel(isEditMode ? "SỬA THÔNG TIN KHÁCH HÀNG" : "THÊM KHÁCH HÀNG MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 55, 153));
        
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Họ tên:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        hoTenField = new CustomTextField(25);
        formPanel.add(hoTenField, gbc);
        
        // CMND/CCCD
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("CMND/CCCD:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        cmndField = new CustomTextField(15);
        formPanel.add(cmndField, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        soDienThoaiField = new CustomTextField(15);
        formPanel.add(soDienThoaiField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        emailField = new CustomTextField(25);
        formPanel.add(emailField, gbc);
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        diaChiField = new CustomTextField(25);
        formPanel.add(diaChiField, gbc);
        
        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ngày sinh:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ngaySinhField = new JFormattedTextField(dateFormat);
        ngaySinhField.setColumns(10);
        formPanel.add(ngaySinhField, gbc);
        
        // Giới tính
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Giới tính:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] gioiTinhOptions = {"Nam", "Nữ", "Khác"};
        gioiTinhComboBox = new JComboBox<>(gioiTinhOptions);
        formPanel.add(gioiTinhComboBox, gbc);
        
        // Quốc tịch
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Quốc tịch:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        quocTichField = new CustomTextField(15);
        quocTichField.setText("Việt Nam"); // Default value
        formPanel.add(quocTichField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        CustomButton saveButton = new CustomButton("Lưu");
        saveButton.setStylePrimary();
        
        CustomButton cancelButton = new CustomButton("Hủy");
        cancelButton.setStyleDanger();
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to dialog
        getContentPane().add(mainPanel);
        
        // Set dialog properties
        pack();
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Add action listeners
        saveButton.addActionListener(e -> saveCustomer());
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void loadCustomerData() {
        if (khachHang != null) {
            hoTenField.setText(khachHang.getHoTen());
            cmndField.setText(khachHang.getCmnd());
            soDienThoaiField.setText(khachHang.getSoDienThoai());
            
            if (khachHang.getEmail() != null) {
                emailField.setText(khachHang.getEmail());
            }
            
            if (khachHang.getDiaChi() != null) {
                diaChiField.setText(khachHang.getDiaChi());
            }
            
            if (khachHang.getNgaySinh() != null) {
                ngaySinhField.setValue(khachHang.getNgaySinh());
            }
            
            if (khachHang.getGioiTinh() != null) {
                gioiTinhComboBox.setSelectedItem(khachHang.getGioiTinh());
            }
            
            if (khachHang.getQuocTich() != null) {
                quocTichField.setText(khachHang.getQuocTich());
            }
        }
    }
    
    private void saveCustomer() {
        try {
            // Validate and get data from form
            String hoTen = hoTenField.getText().trim();
            if (hoTen.isEmpty()) {
                throw new BusinessLogicException("Họ tên không được để trống");
            }
            
            String cmnd = cmndField.getText().trim();
            if (cmnd.isEmpty()) {
                throw new BusinessLogicException("CMND/CCCD không được để trống");
            }
            
            if (!cmnd.matches("^(\\d{9}|\\d{12})$")) {
                throw new BusinessLogicException("CMND/CCCD phải là dãy 9 hoặc 12 chữ số");
            }
            
            String soDienThoai = soDienThoaiField.getText().trim();
            if (soDienThoai.isEmpty()) {
                throw new BusinessLogicException("Số điện thoại không được để trống");
            }
            
            if (!soDienThoai.matches("^0\\d{9,10}$")) {
                throw new BusinessLogicException("Số điện thoại không hợp lệ");
            }
            
            String email = emailField.getText().trim();
            // Email can be empty, but if provided, must be valid
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new BusinessLogicException("Định dạng email không hợp lệ");
            }
            
            String diaChi = diaChiField.getText().trim();
            
            // Parse date
            Date ngaySinh = null;
            try {
                if (!ngaySinhField.getText().trim().isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    ngaySinh = dateFormat.parse(ngaySinhField.getText());
                    
                    // Check if date is in the future
                    if (ngaySinh.after(new Date())) {
                        throw new BusinessLogicException("Ngày sinh không thể là ngày trong tương lai");
                    }
                }
            } catch (ParseException e) {
                throw new BusinessLogicException("Định dạng ngày sinh không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy");
            }
            
            String gioiTinh = (String) gioiTinhComboBox.getSelectedItem();
            String quocTich = quocTichField.getText().trim();
            
            // Create or update customer
            if (isEditMode) {
                // Update existing customer
                khachHang.setHoTen(hoTen);
                khachHang.setCmnd(cmnd);
                khachHang.setSoDienThoai(soDienThoai);
                khachHang.setEmail(email);
                khachHang.setDiaChi(diaChi);
                khachHang.setNgaySinh(ngaySinh);
                khachHang.setGioiTinh(gioiTinh);
                khachHang.setQuocTich(quocTich);
                
                boolean success = khachHangService.sua(khachHang);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật thông tin khách hàng thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể cập nhật thông tin khách hàng", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new customer
                KhachHang newCustomer = new KhachHang();
                newCustomer.setHoTen(hoTen);
                newCustomer.setCmnd(cmnd);
                newCustomer.setSoDienThoai(soDienThoai);
                newCustomer.setEmail(email);
                newCustomer.setDiaChi(diaChi);
                newCustomer.setNgaySinh(ngaySinh);
                newCustomer.setGioiTinh(gioiTinh);
                newCustomer.setQuocTich(quocTich);
                
                boolean success = khachHangService.them(newCustomer);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Thêm khách hàng mới thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể thêm khách hàng mới", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
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