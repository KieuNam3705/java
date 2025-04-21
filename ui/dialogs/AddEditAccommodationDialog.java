package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IChoOService;
import com.tourmanagement.entity.ChoO;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class AddEditAccommodationDialog extends JDialog {
    private IChoOService choOService;
    private ChoO choO;
    private boolean isEditMode;
    private boolean saveSuccess = false;
    
    // UI Components
    private CustomTextField tenChoOField;
    private JComboBox<String> loaiChoOComboBox;
    private CustomTextField diaChiField;
    private CustomTextField thanhPhoField;
    private CustomTextField quocGiaField;
    private CustomTextField soDienThoaiField;
    private CustomTextField emailField;
    private CustomTextField websiteField;
    private JSpinner soSaoSpinner;
    private CustomTextField giaThueField;
    
    public AddEditAccommodationDialog(Frame parent, ChoO choO) {
        super(parent, choO == null ? "Thêm Chỗ Ở Mới" : "Sửa Chỗ Ở", true);
        this.choOService = ServiceFactory.getInstance().getChoOService();
        this.choO = choO;
        this.isEditMode = (choO != null);
        
        initComponents();
        if (isEditMode) {
            loadAccommodationData();
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
        
        CustomLabel titleLabel = new CustomLabel(isEditMode ? "SỬA THÔNG TIN CHỖ Ở" : "THÊM CHỖ Ở MỚI");
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
        
        // Tên chỗ ở
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Tên chỗ ở:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        tenChoOField = new CustomTextField(25);
        formPanel.add(tenChoOField, gbc);
        
        // Loại chỗ ở
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Loại chỗ ở:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        String[] loaiChoOOptions = {"Khách sạn", "Resort", "Homestay", "Nhà nghỉ", "Biệt thự", "Căn hộ", "Khác"};
        loaiChoOComboBox = new JComboBox<>(loaiChoOOptions);
        formPanel.add(loaiChoOComboBox, gbc);
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        diaChiField = new CustomTextField(25);
        formPanel.add(diaChiField, gbc);
        
        // Thành phố
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Thành phố:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        thanhPhoField = new CustomTextField(20);
        formPanel.add(thanhPhoField, gbc);
        
        // Quốc gia
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Quốc gia:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        quocGiaField = new CustomTextField(20);
        quocGiaField.setText("Việt Nam"); // Default
        formPanel.add(quocGiaField, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        soDienThoaiField = new CustomTextField(15);
        formPanel.add(soDienThoaiField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        emailField = new CustomTextField(25);
        formPanel.add(emailField, gbc);
        
        // Website
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Website:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        websiteField = new CustomTextField(25);
        formPanel.add(websiteField, gbc);
        
        // Số sao
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Số sao:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        SpinnerNumberModel soSaoModel = new SpinnerNumberModel(3, 1, 5, 1); // Default 3, min 1, max 5
        soSaoSpinner = new JSpinner(soSaoModel);
        formPanel.add(soSaoSpinner, gbc);
        
        // Giá thuê
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Giá thuê:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        giaThueField = new CustomTextField(15);
        formPanel.add(giaThueField, gbc);
        
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
        saveButton.addActionListener(e -> saveAccommodation());
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void loadAccommodationData() {
        if (choO != null) {
            tenChoOField.setText(choO.getTenChoO());
            loaiChoOComboBox.setSelectedItem(choO.getLoaiChoO());
            diaChiField.setText(choO.getDiaChi());
            thanhPhoField.setText(choO.getThanhPho());
            quocGiaField.setText(choO.getQuocGia());
            
            if (choO.getSoDienThoai() != null) {
                soDienThoaiField.setText(choO.getSoDienThoai());
            }
            
            if (choO.getEmail() != null) {
                emailField.setText(choO.getEmail());
            }
            
            if (choO.getWebsite() != null) {
                websiteField.setText(choO.getWebsite());
            }
            
            soSaoSpinner.setValue(choO.getSoSao());
            
            if (choO.getGiaThue() != null) {
                giaThueField.setText(choO.getGiaThue().toString());
            }
        }
    }
    
    private void saveAccommodation() {
        try {
            // Validate and get data from form
            String tenChoO = tenChoOField.getText().trim();
            if (tenChoO.isEmpty()) {
                throw new BusinessLogicException("Tên chỗ ở không được để trống");
            }
            
            String loaiChoO = (String) loaiChoOComboBox.getSelectedItem();
            
            String diaChi = diaChiField.getText().trim();
            if (diaChi.isEmpty()) {
                throw new BusinessLogicException("Địa chỉ không được để trống");
            }
            
            String thanhPho = thanhPhoField.getText().trim();
            if (thanhPho.isEmpty()) {
                throw new BusinessLogicException("Thành phố không được để trống");
            }
            
            String quocGia = quocGiaField.getText().trim();
            if (quocGia.isEmpty()) {
                throw new BusinessLogicException("Quốc gia không được để trống");
            }
            
            String soDienThoai = soDienThoaiField.getText().trim();
            // Validate phone number if provided
            if (!soDienThoai.isEmpty() && !soDienThoai.matches("^0\\d{9,10}$")) {
                throw new BusinessLogicException("Số điện thoại không hợp lệ");
            }
            
            String email = emailField.getText().trim();
            // Validate email if provided
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new BusinessLogicException("Định dạng email không hợp lệ");
            }
            
            String website = websiteField.getText().trim();
            
            int soSao = (Integer) soSaoSpinner.getValue();
            
            // Parse price
            BigDecimal giaThue;
            try {
                giaThue = new BigDecimal(giaThueField.getText().trim().replace(",", ""));
                if (giaThue.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessLogicException("Giá thuê phải lớn hơn 0");
                }
            } catch (NumberFormatException e) {
                throw new BusinessLogicException("Giá thuê không hợp lệ");
            }
            
            // Create or update accommodation
            if (isEditMode) {
                // Update existing accommodation
                choO.setTenChoO(tenChoO);
                choO.setLoaiChoO(loaiChoO);
                choO.setDiaChi(diaChi);
                choO.setThanhPho(thanhPho);
                choO.setQuocGia(quocGia);
                choO.setSoDienThoai(soDienThoai);
                choO.setEmail(email);
                choO.setWebsite(website);
                choO.setSoSao(soSao);
                choO.setGiaThue(giaThue);
                
                boolean success = choOService.sua(choO);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật chỗ ở thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể cập nhật chỗ ở", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new accommodation
                ChoO newAccommodation = new ChoO();
                newAccommodation.setTenChoO(tenChoO);
                newAccommodation.setLoaiChoO(loaiChoO);
                newAccommodation.setDiaChi(diaChi);
                newAccommodation.setThanhPho(thanhPho);
                newAccommodation.setQuocGia(quocGia);
                newAccommodation.setSoDienThoai(soDienThoai);
                newAccommodation.setEmail(email);
                newAccommodation.setWebsite(website);
                newAccommodation.setSoSao(soSao);
                newAccommodation.setGiaThue(giaThue);
                
                boolean success = choOService.them(newAccommodation);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Thêm chỗ ở mới thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể thêm chỗ ở mới", 
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