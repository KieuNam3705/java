package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IPhuongTienService;
import com.tourmanagement.entity.PhuongTien;
import com.tourmanagement.ui.dialogs.AddEditTransportationDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TransportationManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IPhuongTienService phuongTienService;
    
    private CustomTable transportTable;
    private SearchPanel searchPanel;
    private CustomButton addButton;
    private CustomButton editButton;
    private CustomButton deleteButton;
    private CustomButton updateStatusButton;
    
    private JComboBox<String> typeFilterComboBox;
    private JComboBox<String> statusFilterComboBox;
    
    public TransportationManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        phuongTienService = serviceFactory.getPhuongTienService();
        
        setupPanel();
        loadTransportData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Quản Lý Phương Tiện");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addButton = new CustomButton("Thêm Phương Tiện");
        addButton.setStylePrimary();
        
        editButton = new CustomButton("Sửa Phương Tiện");
        editButton.setStyleSecondary();
        
        updateStatusButton = new CustomButton("Cập Nhật Trạng Thái");
        updateStatusButton.setStyleSecondary();
        
        deleteButton = new CustomButton("Xóa Phương Tiện");
        deleteButton.setStyleDanger();
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(deleteButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Search and filter panel
        JPanel searchFilterPanel = new JPanel(new BorderLayout());
        searchFilterPanel.setBackground(new Color(245, 246, 250));
        
        searchPanel = new SearchPanel();
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(new Color(245, 246, 250));
        
        // Type filter
        filterPanel.add(new JLabel("Loại:"));
        
        // Populate transport types from database
        ArrayList<String> transportTypes = new ArrayList<>();
        transportTypes.add("Tất cả");
        List<PhuongTien> allTransports = phuongTienService.layDanhSach();
        for (PhuongTien transport : allTransports) {
            if (!transportTypes.contains(transport.getLoaiPhuongTien())) {
                transportTypes.add(transport.getLoaiPhuongTien());
            }
        }
        
        typeFilterComboBox = new JComboBox<>(transportTypes.toArray(new String[0]));
        typeFilterComboBox.addActionListener(e -> filterTransports());
        
        filterPanel.add(typeFilterComboBox);
        
        // Status filter
        filterPanel.add(new JLabel("Trạng thái:"));
        
        String[] statuses = {"Tất cả", "Sẵn sàng", "Đang sử dụng", "Bảo trì", "Ngưng hoạt động"};
        statusFilterComboBox = new JComboBox<>(statuses);
        statusFilterComboBox.addActionListener(e -> filterTransports());
        
        filterPanel.add(statusFilterComboBox);
        
        searchFilterPanel.add(searchPanel, BorderLayout.WEST);
        searchFilterPanel.add(filterPanel, BorderLayout.EAST);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        transportTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(transportTable);
        
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
        searchPanel.addSearchListener(e -> searchTransports());
        
        // Add action listeners for buttons
        addButton.addActionListener(e -> showAddTransportDialog());
        editButton.addActionListener(e -> {
            int selectedRow = transportTable.getSelectedRow();
            if (selectedRow != -1) {
                int transportId = (int) transportTable.getValueAt(selectedRow, 0);
                showEditTransportDialog(transportId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một phương tiện để sửa.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        updateStatusButton.addActionListener(e -> {
            int selectedRow = transportTable.getSelectedRow();
            if (selectedRow != -1) {
                int transportId = (int) transportTable.getValueAt(selectedRow, 0);
                showUpdateStatusDialog(transportId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một phương tiện để cập nhật trạng thái.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = transportTable.getSelectedRow();
            if (selectedRow != -1) {
                int transportId = (int) transportTable.getValueAt(selectedRow, 0);
                confirmAndDeleteTransport(transportId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một phương tiện để xóa.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void loadTransportData() {
        // Lấy danh sách phương tiện từ service
        List<PhuongTien> transports = phuongTienService.layDanhSach();
        
        updateTransportTable(transports);
    }
    
    private void updateTransportTable(List<PhuongTien> transports) {
        // Tạo model cho bảng
        String[] columnNames = {"Mã PT", "Tên Phương Tiện", "Loại", "Biển Số", "Số Chỗ", "Giá Thuê", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Thêm dữ liệu vào model
        for (PhuongTien transport : transports) {
            Object[] rowData = {
                transport.getMaPhuongTien(),
                transport.getTenPhuongTien(),
                transport.getLoaiPhuongTien(),
                transport.getBienSo(),
                transport.getSoChoNgoi(),
                transport.getGiaThue() + " VNĐ",
                transport.getTrangThai()
            };
            model.addRow(rowData);
        }
        
        transportTable.setModel(model);
    }
    
    private void searchTransports() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            filterTransports(); // Áp dụng bộ lọc hiện tại
            return;
        }
        
        // Tìm phương tiện theo tên
        List<PhuongTien> searchResults = phuongTienService.timTheoTen(searchText);
        
       // Áp dụng thêm bộ lọc (nếu có)
        String selectedType = (String) typeFilterComboBox.getSelectedItem();
        String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
        
        List<PhuongTien> filteredResults = new ArrayList<>(searchResults);
        
        // Lọc theo loại phương tiện
        if (!"Tất cả".equals(selectedType)) {
            filteredResults.removeIf(transport -> !transport.getLoaiPhuongTien().equals(selectedType));
        }
        
        // Lọc theo trạng thái
        if (!"Tất cả".equals(selectedStatus)) {
            filteredResults.removeIf(transport -> !transport.getTrangThai().equals(selectedStatus));
        }
        
        updateTransportTable(filteredResults);
    }
    
    private void filterTransports() {
        String selectedType = (String) typeFilterComboBox.getSelectedItem();
        String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
        
        // Nếu cả hai là "Tất cả", hiển thị tất cả
        if ("Tất cả".equals(selectedType) && "Tất cả".equals(selectedStatus)) {
            loadTransportData();
            return;
        }
        
        // Lấy danh sách đầy đủ phương tiện
        List<PhuongTien> transports = phuongTienService.layDanhSach();
        List<PhuongTien> filteredTransports = new ArrayList<>(transports);
        
        // Lọc theo loại
        if (!"Tất cả".equals(selectedType)) {
            filteredTransports.removeIf(transport -> !transport.getLoaiPhuongTien().equals(selectedType));
        }
        
        // Lọc theo trạng thái
        if (!"Tất cả".equals(selectedStatus)) {
            filteredTransports.removeIf(transport -> !transport.getTrangThai().equals(selectedStatus));
        }
        
        updateTransportTable(filteredTransports);
    }
    
    private void showAddTransportDialog() {
      AddEditTransportationDialog dialog = new AddEditTransportationDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadTransportData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng thêm phương tiện sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showEditTransportDialog(int transportId) {
       PhuongTien selectedTransport = phuongTienService.timTheoMa(transportId);
    
    AddEditTransportationDialog dialog = new AddEditTransportationDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        selectedTransport
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadTransportData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng sửa phương tiện " + transportId + " sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showUpdateStatusDialog(int transportId) {
        // Lấy thông tin phương tiện
        PhuongTien transport = phuongTienService.timTheoMa(transportId);
        if (transport == null) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy phương tiện với mã: " + transportId, 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tạo dialog để cập nhật trạng thái
        String[] statusOptions = {"Sẵn sàng", "Đang sử dụng", "Bảo trì", "Ngưng hoạt động"};
        String selectedStatus = (String) JOptionPane.showInputDialog(
            this,
            "Chọn trạng thái mới cho phương tiện #" + transportId,
            "Cập Nhật Trạng Thái Phương Tiện",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusOptions,
            transport.getTrangThai() // Giá trị mặc định là trạng thái hiện tại
        );
        
        // Nếu người dùng hủy hoặc không chọn gì
        if (selectedStatus == null) {
            return;
        }
        
        // Cập nhật trạng thái
        try {
            boolean success = phuongTienService.capNhatTrangThai(transportId, selectedStatus);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Cập nhật trạng thái thành công.",
                    "Thành Công",
                    JOptionPane.INFORMATION_MESSAGE);
                loadTransportData(); // Tải lại dữ liệu
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không thể cập nhật trạng thái phương tiện.",
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
    
    private void confirmAndDeleteTransport(int transportId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa phương tiện với mã: " + transportId + "?",
            "Xác Nhận Xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = phuongTienService.xoa(transportId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Xóa phương tiện thành công.",
                        "Thành Công",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadTransportData(); // Tải lại dữ liệu
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể xóa phương tiện.",
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