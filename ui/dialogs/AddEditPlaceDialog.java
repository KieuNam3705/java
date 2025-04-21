package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTextField;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IDiaDiemService;
import com.tourmanagement.entity.DiaDiem;
import com.tourmanagement.exception.BusinessLogicException;

import javax.swing.*;
import java.awt.*;

public class AddEditPlaceDialog extends JDialog {
    private IDiaDiemService diaDiemService;
    private DiaDiem diaDiem;
    private boolean isEditMode;
    private boolean saveSuccess = false;
    
    // UI Components
    private CustomTextField tenDiaDiemField;
    private JTextArea moTaArea;
    private CustomTextField diaChiField;
    private CustomTextField thanhPhoField;
    private CustomTextField quocGiaField;
    private JTextArea dacDiemArea;
    
    public AddEditPlaceDialog(Frame parent, DiaDiem diaDiem) {
        super(parent, diaDiem == null ? "Thêm Địa Điểm Mới" : "Sửa Địa Điểm", true);
        this.diaDiemService = ServiceFactory.getInstance().getDiaDiemService();
        this.diaDiem = diaDiem;
        this.isEditMode = (diaDiem != null);
        
        initComponents();
        if (isEditMode) {
            loadPlaceData();
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
        
        CustomLabel titleLabel = new CustomLabel(isEditMode ? "SỬA THÔNG TIN ĐỊA ĐIỂM" : "THÊM ĐỊA ĐIỂM MỚI");
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
        
        // Tên địa điểm
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Tên địa điểm:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        tenDiaDiemField = new CustomTextField(25);
        formPanel.add(tenDiaDiemField, gbc);
        
        // Mô tả
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        moTaArea = new JTextArea(4, 25);
        moTaArea.setLineWrap(true);
        moTaArea.setWrapStyleWord(true);
        JScrollPane moTaScrollPane = new JScrollPane(moTaArea);
        formPanel.add(moTaScrollPane, gbc);
        
        // Reset grid height
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        diaChiField = new CustomTextField(25);
        formPanel.add(diaChiField, gbc);
        
        // Thành phố
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Thành phố:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        thanhPhoField = new CustomTextField(20);
        formPanel.add(thanhPhoField, gbc);
        
        // Quốc gia
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Quốc gia:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        quocGiaField = new CustomTextField(20);
        quocGiaField.setText("Việt Nam"); // Default
        formPanel.add(quocGiaField, gbc);
        
        // Đặc điểm
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Đặc điểm:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        dacDiemArea = new JTextArea(4, 25);
        dacDiemArea.setLineWrap(true);
        dacDiemArea.setWrapStyleWord(true);
        JScrollPane dacDiemScrollPane = new JScrollPane(dacDiemArea);
        formPanel.add(dacDiemScrollPane, gbc);
        
        // Reset grid height
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
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
        saveButton.addActionListener(e -> savePlace());
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void loadPlaceData() {
        if (diaDiem != null) {
            tenDiaDiemField.setText(diaDiem.getTenDiaDiem());
            moTaArea.setText(diaDiem.getMoTa());
            diaChiField.setText(diaDiem.getDiaChi());
            thanhPhoField.setText(diaDiem.getThanhPho());
            quocGiaField.setText(diaDiem.getQuocGia());
            
            if (diaDiem.getDacDiem() != null) {
                dacDiemArea.setText(diaDiem.getDacDiem());
            }
        }
    }
    
    private void savePlace() {
        try {
            // Validate and get data from form
            String tenDiaDiem = tenDiaDiemField.getText().trim();
            if (tenDiaDiem.isEmpty()) {
                throw new BusinessLogicException("Tên địa điểm không được để trống");
            }
            
            String moTa = moTaArea.getText().trim();
            if (moTa.isEmpty()) {
                throw new BusinessLogicException("Mô tả không được để trống");
            }
            
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
            
            String dacDiem = dacDiemArea.getText().trim();
            
            // Create or update place
            if (isEditMode) {
                // Update existing place
                diaDiem.setTenDiaDiem(tenDiaDiem);
                diaDiem.setMoTa(moTa);
                diaDiem.setDiaChi(diaChi);
                diaDiem.setThanhPho(thanhPho);
                diaDiem.setQuocGia(quocGia);
                diaDiem.setDacDiem(dacDiem);
                
                boolean success = diaDiemService.sua(diaDiem);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật địa điểm thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể cập nhật địa điểm", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new place
                DiaDiem newPlace = new DiaDiem();
                newPlace.setTenDiaDiem(tenDiaDiem);
                newPlace.setMoTa(moTa);
                newPlace.setDiaChi(diaChi);
                newPlace.setThanhPho(thanhPho);
                newPlace.setQuocGia(quocGia);
                newPlace.setDacDiem(dacDiem);
                
                boolean success = diaDiemService.them(newPlace);
                if (success) {
                    saveSuccess = true;
                    JOptionPane.showMessageDialog(this, 
                        "Thêm địa điểm mới thành công", 
                        "Thành Công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể thêm địa điểm mới", 
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