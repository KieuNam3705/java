package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.INhanVienService;
import com.tourmanagement.entity.NhanVien;
import com.tourmanagement.ui.dialogs.AddEditStaffDialog;
import com.tourmanagement.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class StaffManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private INhanVienService nhanVienService;
    
    private CustomTable staffTable;
    private SearchPanel searchPanel;
    private CustomButton addButton;
    private CustomButton editButton;
    private CustomButton deleteButton;
    
    public StaffManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        nhanVienService = serviceFactory.getNhanVienService();
        
        setupPanel();
        loadStaffData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Staff Management");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addButton = new CustomButton("Add Staff");
        addButton.setStylePrimary();
        
        editButton = new CustomButton("Edit Staff");
        editButton.setStyleSecondary();
        
        deleteButton = new CustomButton("Delete Staff");
        deleteButton.setStyleDanger();
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Search panel
        searchPanel = new SearchPanel();
        searchPanel.addSearchListener(e -> searchStaff());
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        staffTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(staffTable);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 246, 250));
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(searchPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(tablePanel);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Add action listeners
        addButton.addActionListener(e -> showAddStaffDialog());
        editButton.addActionListener(e -> {
            int selectedRow = staffTable.getSelectedRow();
            if (selectedRow != -1) {
                int staffId = (int) staffTable.getValueAt(selectedRow, 0);
                showEditStaffDialog(staffId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select a staff member to edit.", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = staffTable.getSelectedRow();
            if (selectedRow != -1) {
                int staffId = (int) staffTable.getValueAt(selectedRow, 0);
                confirmAndDeleteStaff(staffId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select a staff member to delete.", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void loadStaffData() {
        // Lấy danh sách nhân viên từ service
        List<NhanVien> staffList = nhanVienService.layDanhSach();
        
        // Tạo model cho bảng
        String[] columnNames = {"ID", "Full Name", "Position", "CMND/CCCD", "Phone", "Email", "Gender"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Thêm dữ liệu vào model
        for (NhanVien staff : staffList) {
            Object[] rowData = {
                staff.getMaNV(),
                staff.getHoTen(),
                staff.getChucVu(),
                staff.getCmnd(),
                staff.getSoDienThoai(),
                staff.getEmail(),
                staff.getGioiTinh()
            };
            model.addRow(rowData);
        }
        
        staffTable.setModel(model);
    }
    
    private void searchStaff() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            loadStaffData(); // Nếu không có text tìm kiếm, hiển thị tất cả
            return;
        }
        
        // Tìm nhân viên theo tên
        List<NhanVien> searchResults = nhanVienService.timTheoTen(searchText);
        
        // Tạo model cho bảng
        String[] columnNames = {"ID", "Full Name", "Position", "CMND/CCCD", "Phone", "Email", "Gender"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Thêm dữ liệu vào model
        for (NhanVien staff : searchResults) {
            Object[] rowData = {
                staff.getMaNV(),
                staff.getHoTen(),
                staff.getChucVu(),
                staff.getCmnd(),
                staff.getSoDienThoai(),
                staff.getEmail(),
                staff.getGioiTinh()
            };
            model.addRow(rowData);
        }
        
        staffTable.setModel(model);
    }
    
    private void showAddStaffDialog() {
       AddEditStaffDialog dialog = new AddEditStaffDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadStaffData();
    }
        JOptionPane.showMessageDialog(this, "Add Staff Dialog will be implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showEditStaffDialog(int staffId) {
        NhanVien selectedStaff = nhanVienService.timTheoMa(staffId);
    
    AddEditStaffDialog dialog = new AddEditStaffDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        selectedStaff
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadStaffData();
    }
        JOptionPane.showMessageDialog(this, "Edit Staff Dialog for ID: " + staffId + " will be implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void confirmAndDeleteStaff(int staffId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete staff with ID: " + staffId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = nhanVienService.xoa(staffId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Staff member deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadStaffData(); // Refresh data
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete staff member.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
     private int getCurrentStaffId() {
        return SessionManager.getInstance().getCurrentStaffId();
    }
}