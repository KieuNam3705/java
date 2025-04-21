package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IDiaDiemService;
import com.tourmanagement.entity.DiaDiem;
import com.tourmanagement.ui.dialogs.AddEditPlaceDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IDiaDiemService diaDiemService;
    
    private CustomTable placeTable;
    private SearchPanel searchPanel;
    private CustomButton addButton;
    private CustomButton editButton;
    private CustomButton deleteButton;
    
    private JComboBox<String> countryFilterComboBox;
    
    public PlaceManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        diaDiemService = serviceFactory.getDiaDiemService();
        
        setupPanel();
        loadPlaceData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Quản Lý Địa Điểm");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addButton = new CustomButton("Thêm Địa Điểm");
        addButton.setStylePrimary();
        
        editButton = new CustomButton("Sửa Địa Điểm");
        editButton.setStyleSecondary();
        
        deleteButton = new CustomButton("Xóa Địa Điểm");
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
        
        filterPanel.add(new JLabel("Quốc gia:"));
        
        // Populate country list from database
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Tất cả");
        List<DiaDiem> allPlaces = diaDiemService.layDanhSach();
        for (DiaDiem place : allPlaces) {
            if (!countries.contains(place.getQuocGia())) {
                countries.add(place.getQuocGia());
            }
        }
        
        countryFilterComboBox = new JComboBox<>(countries.toArray(new String[0]));
        countryFilterComboBox.addActionListener(e -> filterPlacesByCountry());
        
        filterPanel.add(countryFilterComboBox);
        
        searchFilterPanel.add(searchPanel, BorderLayout.WEST);
        searchFilterPanel.add(filterPanel, BorderLayout.EAST);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        placeTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(placeTable);
        
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
        searchPanel.addSearchListener(e -> searchPlaces());
        
        // Add action listeners for buttons
        addButton.addActionListener(e -> showAddPlaceDialog());
        editButton.addActionListener(e -> {
            int selectedRow = placeTable.getSelectedRow();
            if (selectedRow != -1) {
                int placeId = (int) placeTable.getValueAt(selectedRow, 0);
                showEditPlaceDialog(placeId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một địa điểm để sửa.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = placeTable.getSelectedRow();
            if (selectedRow != -1) {
                int placeId = (int) placeTable.getValueAt(selectedRow, 0);
                confirmAndDeletePlace(placeId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một địa điểm để xóa.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void loadPlaceData() {
        // Lấy danh sách địa điểm từ service
        List<DiaDiem> places = diaDiemService.layDanhSach();
        
        updatePlaceTable(places);
    }
    
    private void updatePlaceTable(List<DiaDiem> places) {
        // Tạo model cho bảng
        String[] columnNames = {"Mã Địa Điểm", "Tên Địa Điểm", "Thành Phố", "Quốc Gia", "Địa Chỉ"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Thêm dữ liệu vào model
        for (DiaDiem place : places) {
            Object[] rowData = {
                place.getMaDiaDiem(),
                place.getTenDiaDiem(),
                place.getThanhPho(),
                place.getQuocGia(),
                place.getDiaChi()
            };
            model.addRow(rowData);
        }
        
        placeTable.setModel(model);
    }
    
    private void searchPlaces() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            loadPlaceData(); // Nếu không có text tìm kiếm, hiển thị tất cả
            return;
        }
        
        // Tìm địa điểm theo tên
        List<DiaDiem> searchResults = diaDiemService.timTheoTen(searchText);
        updatePlaceTable(searchResults);
    }
    
    private void filterPlacesByCountry() {
        String selectedCountry = (String) countryFilterComboBox.getSelectedItem();
        
        if ("Tất cả".equals(selectedCountry)) {
            loadPlaceData(); // Hiển thị tất cả
            return;
        }
        
        // Lấy danh sách địa điểm theo quốc gia
        List<DiaDiem> filteredPlaces = diaDiemService.layDanhSachTheoQuocGia(selectedCountry);
        updatePlaceTable(filteredPlaces);
    }
    
    private void showAddPlaceDialog() {
      AddEditPlaceDialog dialog = new AddEditPlaceDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadPlaceData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng thêm địa điểm sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showEditPlaceDialog(int placeId) {
        AddEditPlaceDialog dialog = new AddEditPlaceDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadPlaceData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng sửa địa điểm " + placeId + " sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void confirmAndDeletePlace(int placeId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa địa điểm với mã: " + placeId + "?",
            "Xác Nhận Xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = diaDiemService.xoa(placeId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Xóa địa điểm thành công.",
                        "Thành Công",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadPlaceData(); // Tải lại dữ liệu
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể xóa địa điểm.",
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