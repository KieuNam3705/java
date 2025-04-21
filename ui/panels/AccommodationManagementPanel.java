package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IChoOService;
import com.tourmanagement.entity.ChoO;
import com.tourmanagement.ui.dialogs.AddEditAccommodationDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AccommodationManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IChoOService choOService;
    
    private CustomTable accommodationTable;
    private SearchPanel searchPanel;
    private CustomButton addButton;
    private CustomButton editButton;
    private CustomButton deleteButton;
    
    private JComboBox<String> typeFilterComboBox;
    
    public AccommodationManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        choOService = serviceFactory.getChoOService();
        
        setupPanel();
        loadAccommodationData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Quản Lý Chỗ Ở");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addButton = new CustomButton("Thêm Chỗ Ở");
        addButton.setStylePrimary();
        
        editButton = new CustomButton("Sửa Chỗ Ở");
        editButton.setStyleSecondary();
        
        deleteButton = new CustomButton("Xóa Chỗ Ở");
        deleteButton.setStyleDanger();
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Search and filter panel
        JPanel searchFilterPanel = new JPanel(new BorderLayout());
        searchFilterPanel.setBackground(new Color(245, 246, 250));
        
        searchPanel = new SearchPanel();
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(new Color(245, 246, 250));
        
        filterPanel.add(new JLabel("Loại chỗ ở:"));
        
        // Populate accommodation types from database
        ArrayList<String> accommodationTypes = new ArrayList<>();
        accommodationTypes.add("Tất cả");
        List<ChoO> allAccommodations = choOService.layDanhSach();
        for (ChoO accommodation : allAccommodations) {
            if (!accommodationTypes.contains(accommodation.getLoaiChoO())) {
                accommodationTypes.add(accommodation.getLoaiChoO());
            }
        }
        
        typeFilterComboBox = new JComboBox<>(accommodationTypes.toArray(new String[0]));
        typeFilterComboBox.addActionListener(e -> filterAccommodationsByType());
        
        filterPanel.add(typeFilterComboBox);
        
        searchFilterPanel.add(searchPanel, BorderLayout.WEST);
        searchFilterPanel.add(filterPanel, BorderLayout.EAST);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        accommodationTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(accommodationTable);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 246, 250));
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(searchFilterPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(tablePanel);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Add search listener
        searchPanel.addSearchListener(e -> searchAccommodations());
        
        // Add action listeners for buttons
        addButton.addActionListener(e -> showAddAccommodationDialog());
        editButton.addActionListener(e -> {
            int selectedRow = accommodationTable.getSelectedRow();
            if (selectedRow != -1) {
                int accommodationId = (int) accommodationTable.getValueAt(selectedRow, 0);
                showEditAccommodationDialog(accommodationId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một chỗ ở để sửa.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = accommodationTable.getSelectedRow();
            if (selectedRow != -1) {
                int accommodationId = (int) accommodationTable.getValueAt(selectedRow, 0);
                confirmAndDeleteAccommodation(accommodationId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một chỗ ở để xóa.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void loadAccommodationData() {
        // Lấy danh sách chỗ ở từ service
        List<ChoO> accommodations = choOService.layDanhSach();
        
        updateAccommodationTable(accommodations);
    }
    
    private void updateAccommodationTable(List<ChoO> accommodations) {
        // Tạo model cho bảng
        String[] columnNames = {"Mã Chỗ Ở", "Tên Chỗ Ở", "Loại Chỗ Ở", "Thành Phố", "Quốc Gia", "Số Sao", "Giá Thuê"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Thêm dữ liệu vào model
        for (ChoO accommodation : accommodations) {
            Object[] rowData = {
                accommodation.getMaChoO(),
                accommodation.getTenChoO(),
                accommodation.getLoaiChoO(),
                accommodation.getThanhPho(),
                accommodation.getQuocGia(),
                accommodation.getSoSao(),
                accommodation.getGiaThue() + " VNĐ"
            };
            model.addRow(rowData);
        }
        
        accommodationTable.setModel(model);
    }
    
    private void searchAccommodations() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            loadAccommodationData(); // Nếu không có text tìm kiếm, hiển thị tất cả
            return;
        }
        
        // Tìm chỗ ở theo tên
        List<ChoO> searchResults = choOService.timTheoTen(searchText);
        updateAccommodationTable(searchResults);
    }
    
    private void filterAccommodationsByType() {
        String selectedType = (String) typeFilterComboBox.getSelectedItem();
        
        if ("Tất cả".equals(selectedType)) {
            loadAccommodationData(); // Hiển thị tất cả
            return;
        }
        
        // Lấy danh sách chỗ ở theo loại
        List<ChoO> filteredAccommodations = choOService.layDanhSachTheoLoai(selectedType);
        updateAccommodationTable(filteredAccommodations);
    }
    
    private void showAddAccommodationDialog() {
         AddEditAccommodationDialog dialog = new AddEditAccommodationDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadAccommodationData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng thêm chỗ ở sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showEditAccommodationDialog(int accommodationId) {
       ChoO selectedAccommodation = choOService.timTheoMa(accommodationId);
    
    AddEditAccommodationDialog dialog = new AddEditAccommodationDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        selectedAccommodation
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadAccommodationData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng sửa chỗ ở " + accommodationId + " sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void confirmAndDeleteAccommodation(int accommodationId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa chỗ ở với mã: " + accommodationId + "?",
            "Xác Nhận Xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = choOService.xoa(accommodationId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Xóa chỗ ở thành công.",
                        "Thành Công",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadAccommodationData(); // Tải lại dữ liệu
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể xóa chỗ ở.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}