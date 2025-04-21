package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.INhanVienService;
import com.tourmanagement.entity.NhanVien;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditStaffDialog extends JDialog {
    private INhanVienService nhanVienService;
    private NhanVien nhanVien;
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
    private JComboBox<String> chucVuComboBox;
    private JFormattedTextField ngayVaoLamField;
    private CustomTextField luongField;
    
    public AddEditStaffDialog(Frame parent, NhanVien nhanVien) {
        super(parent, nhanVien == null ? "Thêm Nhân Viên Mới" : "Sửa Thông Tin Nhân Viên", true);
        this.nhanVienService = ServiceFactory.getInstance().getNhanVienService();
        this.nhanVien = nhanVien;
        this.isEditMode = (nhanVien != null);
        
        initComponents();
        if (isEditMode) {
            loadStaffData();
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
        
        CustomLabel titleLabel = new CustomLabel(isEditMode ? "SỬA THÔNG TIN NHÂN VIÊN" : "THÊM NHÂN VIÊN MỚI");
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
        
        // Chức vụ
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Chức vụ:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] chucVuOptions = {"Quản lý", "Nhân viên bán hàng", "Nhân viên kế toán", "Hướng dẫn viên"};
        chucVuComboBox = new JComboBox<>(chucVuOptions);
        formPanel.add(chucVuComboBox, gbc);
        
        // Ngày vào làm
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ngày vào làm:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        ngayVaoLamField = new JFormattedTextField(dateFormat);
        ngayVaoLamField.setColumns(10);
        ngayVaoLamField.setValue(new Date()); // Default to current date
        formPanel.add(ngayVaoLamField, gbc);
        
        // Lương
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Lương:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        luongField = new CustomTextField(15);
        formPanel.add(luongField, gbc);
        
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
        saveButton.addActionListener(e -> saveStaff());
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void loadStaffData() {
        if (nhanVien != null) {
            hoTenField.setText(nhanVien.getHoTen());
            cmndField.setText(nhanVien.getCmnd());
            soDienThoaiField.setText(nhanVien.getSoDienThoai());
            
            if (nhanVien.getEmail() != null) {
                emailField.setText(nhanVien.getEmail());
            }
            
            if (nhanVien.getDiaChi() != null) {
                diaChiField.setText(nhanVien.getDiaChi());
            }
            
            if (nhanVien.getNgaySinh() != null) {
                ngaySinhField.setValue(nhanVien.getNgaySinh());
            }
            
            if (nhanVien.getGioiTinh() != null) {
                gioiTinhComboBox.setSelectedItem(nhanVien.getGioiTinh());
            }
            
            if (nhanVien.getChucVu() != null) {
                chucVuComboBox.setSelectedItem(nhanVien.getChucVu());
            }
            
            if (nhanVien.getNgayVaoLam() != null) {
                ngayVaoLamField.setValue(nhanVien.getNgayVaoLam());
            }
            
            if (nhanVien.getLuong() != null) {
                luongField.setText(nhanVien.getLuong().toString());
            }
        }
    }
    
    private void saveStaff() {
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
            String chucVu = (String) chucVuComboBox.getSelectedItem();
            
            // Parse ngày vào làm
            Date ngayVaoLam = null;
            try {
                if (!ngayVaoLamField.getText().trim().isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    ngayVaoLam = dateFormat.parse(ngayVaoLamField.getText());
                }
            } catch (ParseException e) {
                throw new BusinessLogicException("Định dạng ngày vào làm không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy");
            }
            
            // Parse lương
            BigDecimal luong = null;
            try {
                String luongStr = luongField.getText().trim();
                if (!luongStr.isEmpty()) {
                    luong = new BigDecimal(luongStr.replace(",", ""));
                    if (luong.compareTo(BigDecimal.ZERO) < 0) {
                        throw new BusinessLogicException("Lương không thể là số âm");
                    }
                }
            } catch (NumberFormatException e) {
                throw new BusinessLogicException("Lương không hợp lệ");
            }
            
            // Create or update staff
            if (isEditMode) {
                // Update existing staff
                nhanVien.setHoTen(hoTen);
                nhanVien.setCmnd(cmnd);
                nhanVien.setSoDienThoai(soDienThoai);
                nhanVien.setEmail(email);
                nhanVien.setDiaChi(diaChi);
                nhanVien.setNgaySinh(ngaySinh);
                nhanVien.setGioiTinh(gioiTinh);
                nhanVien.setChucVu(chucVu);
                nhanVien.setNgayVaoLam(ngayVaoLam);
                nhanVien.setLuong(luong);
                
                boolean success = nhanVienService.sua(nhanVien);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật thông tin nhân viên thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể cập nhật thông tin nhân viên", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new staff
                NhanVien newStaff = new NhanVien();
                newStaff.setHoTen(hoTen);
                newStaff.setCmnd(cmnd);
                newStaff.setSoDienThoai(soDienThoai);
                newStaff.setEmail(email);
                newStaff.setDiaChi(diaChi);
                newStaff.setNgaySinh(ngaySinh);
                newStaff.setGioiTinh(gioiTinh);
                newStaff.setChucVu(chucVu);
                newStaff.setNgayVaoLam(ngayVaoLam);
                newStaff.setLuong(luong);
                
                boolean success = nhanVienService.them(newStaff);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Thêm nhân viên mới thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể thêm nhân viên mới", 
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