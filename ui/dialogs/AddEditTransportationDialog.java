package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IPhuongTienService;
import com.tourmanagement.entity.PhuongTien;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;

public class AddEditTransportationDialog extends JDialog {
    private IPhuongTienService phuongTienService;
    private PhuongTien phuongTien;
    private boolean isEditMode;
    private boolean saveSuccess = false;
    
    // UI Components
    private JComboBox<String> loaiPhuongTienComboBox;
    private CustomTextField tenPhuongTienField;
    private CustomTextField bienSoField;
    private JSpinner soChoNgoiSpinner;
    private CustomTextField congTyField;
    private CustomTextField giaThueField;
    private JComboBox<String> trangThaiComboBox;
    
    public AddEditTransportationDialog(Frame parent, PhuongTien phuongTien) {
        super(parent, phuongTien == null ? "Thêm Phương Tiện Mới" : "Sửa Phương Tiện", true);
        this.phuongTienService = ServiceFactory.getInstance().getPhuongTienService();
        this.phuongTien = phuongTien;
        this.isEditMode = (phuongTien != null);
        
        initComponents();
        if (isEditMode) {
            loadTransportData();
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
        
        CustomLabel titleLabel = new CustomLabel(isEditMode ? "SỬA THÔNG TIN PHƯƠNG TIỆN" : "THÊM PHƯƠNG TIỆN MỚI");
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
        
        // Loại phương tiện
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Loại phương tiện:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] loaiPhuongTienOptions = {"Xe khách", "Xe du lịch", "Tàu hỏa", "Tàu thủy", "Máy bay", "Xe limousine"};
        loaiPhuongTienComboBox = new JComboBox<>(loaiPhuongTienOptions);
        formPanel.add(loaiPhuongTienComboBox, gbc);
        
        // Tên phương tiện
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tên phương tiện:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        tenPhuongTienField = new CustomTextField(25);
        formPanel.add(tenPhuongTienField, gbc);
        
        // Biển số
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Biển số:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        bienSoField = new CustomTextField(15);
        formPanel.add(bienSoField, gbc);
        
        // Số chỗ ngồi
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Số chỗ ngồi:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SpinnerNumberModel soChoNgoiModel = new SpinnerNumberModel(4, 1, 100, 1); // Default 4, min 1, max 100
        soChoNgoiSpinner = new JSpinner(soChoNgoiModel);
        formPanel.add(soChoNgoiSpinner, gbc);
        
        // Công ty
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Công ty:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        congTyField = new CustomTextField(25);
        formPanel.add(congTyField, gbc);
        
        // Giá thuê
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Giá thuê:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        giaThueField = new CustomTextField(15);
        formPanel.add(giaThueField, gbc);
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] trangThaiOptions = {"Sẵn sàng", "Đang sử dụng", "Bảo trì", "Ngưng hoạt động"};
        trangThaiComboBox = new JComboBox<>(trangThaiOptions);
        formPanel.add(trangThaiComboBox, gbc);
        
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
        saveButton.addActionListener(e -> saveTransportation());
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void loadTransportData() {
        if (phuongTien != null) {
            loaiPhuongTienComboBox.setSelectedItem(phuongTien.getLoaiPhuongTien());
            tenPhuongTienField.setText(phuongTien.getTenPhuongTien());
            
            if (phuongTien.getBienSo() != null) {
                bienSoField.setText(phuongTien.getBienSo());
            }
            
            soChoNgoiSpinner.setValue(phuongTien.getSoChoNgoi());
            
            if (phuongTien.getCongTy() != null) {
                congTyField.setText(phuongTien.getCongTy());
            }
            
            giaThueField.setText(String.valueOf(phuongTien.getGiaThue()));
            trangThaiComboBox.setSelectedItem(phuongTien.getTrangThai());
        }
    }
    
    private void saveTransportation() {
        try {
            // Validate and get data from form
            String loaiPhuongTien = (String) loaiPhuongTienComboBox.getSelectedItem();
            
            String tenPhuongTien = tenPhuongTienField.getText().trim();
            if (tenPhuongTien.isEmpty()) {
                throw new BusinessLogicException("Tên phương tiện không được để trống");
            }
            
            String bienSo = bienSoField.getText().trim();
            // Biển số có thể để trống nếu là phương tiện không có biển số (ví dụ: tàu, máy bay)
            
            int soChoNgoi = (Integer) soChoNgoiSpinner.getValue();
            if (soChoNgoi <= 0) {
                throw new BusinessLogicException("Số chỗ ngồi phải lớn hơn 0");
            }
            
            String congTy = congTyField.getText().trim();
            
            // Parse giá thuê
            double giaThue;
            try {
                giaThue = Double.parseDouble(giaThueField.getText().trim().replace(",", ""));
                if (giaThue < 0) {
                    throw new BusinessLogicException("Giá thuê không thể là số âm");
                }
            } catch (NumberFormatException e) {
                throw new BusinessLogicException("Giá thuê không hợp lệ");
            }
            
            String trangThai = (String) trangThaiComboBox.getSelectedItem();
            
            // Create or update transportation
            if (isEditMode) {
                // Update existing transportation
                phuongTien.setLoaiPhuongTien(loaiPhuongTien);
                phuongTien.setTenPhuongTien(tenPhuongTien);
                phuongTien.setBienSo(bienSo);
                phuongTien.setSoChoNgoi(soChoNgoi);
                phuongTien.setCongTy(congTy);
                phuongTien.setGiaThue(giaThue);
                phuongTien.setTrangThai(trangThai);
                
                boolean success = phuongTienService.sua(phuongTien);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật phương tiện thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể cập nhật phương tiện", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new transportation
                PhuongTien newTransport = new PhuongTien();
                newTransport.setLoaiPhuongTien(loaiPhuongTien);
                newTransport.setTenPhuongTien(tenPhuongTien);
                newTransport.setBienSo(bienSo);
                newTransport.setSoChoNgoi(soChoNgoi);
                newTransport.setCongTy(congTy);
                newTransport.setGiaThue(giaThue);
                newTransport.setTrangThai(trangThai);
                
                boolean success = phuongTienService.them(newTransport);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Thêm phương tiện mới thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể thêm phương tiện mới", 
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