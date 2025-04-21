package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IKhachHangService;
import com.tourmanagement.entity.KhachHang;
import com.tourmanagement.ui.dialogs.AddEditCustomerDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomerManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IKhachHangService khachHangService;
    
    private CustomTable customerTable;
    private SearchPanel searchPanel;
    private CustomButton addButton;
    private CustomButton editButton;
    private CustomButton deleteButton;
    
    public CustomerManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        khachHangService = serviceFactory.getKhachHangService();
        
        setupPanel();
        loadCustomerData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Customer Management");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addButton = new CustomButton("Thêm ");
        addButton.setStylePrimary();
        
        editButton = new CustomButton("Sửa");
        editButton.setStyleSecondary();
        
        deleteButton = new CustomButton("Xóa");
        deleteButton.setStyleDanger();
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Search panel
        searchPanel = new SearchPanel();
        searchPanel.addSearchListener(e -> searchCustomers());
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        customerTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(customerTable);
        
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
        addButton.addActionListener(e -> showAddCustomerDialog());
        editButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                int customerId = (int) customerTable.getValueAt(selectedRow, 0);
                showEditCustomerDialog(customerId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cần chọn khách hàng để sửa", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                int customerId = (int) customerTable.getValueAt(selectedRow, 0);
                confirmAndDeleteCustomer(customerId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cần chọn khách hàng để xóa", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void loadCustomerData() {
        // Lấy danh sách khách hàng từ service
        List<KhachHang> customers = khachHangService.layDanhSach();
        
        // Tạo model cho bảng
        String[] columnNames = {"Mã KH", "Họ Têm", "CMND/CCCD", "SĐT", "Email", "Giới tính", "Quốc tịch"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Thêm dữ liệu vào model
        for (KhachHang customer : customers) {
            Object[] rowData = {
                customer.getMaKH(),
                customer.getHoTen(),
                customer.getCmnd(),
                customer.getSoDienThoai(),
                customer.getEmail(),
                customer.getGioiTinh(),
                customer.getQuocTich()
            };
            model.addRow(rowData);
        }
        
        customerTable.setModel(model);
    }
    
    private void searchCustomers() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            loadCustomerData(); // Nếu không có text tìm kiếm, hiển thị tất cả
            return;
        }
        
        // Tìm khách hàng theo tên
        List<KhachHang> searchResults = khachHangService.timTheoTen(searchText);
        
        // Tạo model cho bảng
        String[] columnNames = {"ID", "Full Name", "CMND/CCCD", "Phone", "Email", "Gender", "Nationality"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Thêm dữ liệu vào model
        for (KhachHang customer : searchResults) {
            Object[] rowData = {
                customer.getMaKH(),
                customer.getHoTen(),
                customer.getCmnd(),
                customer.getSoDienThoai(),
                customer.getEmail(),
                customer.getGioiTinh(),
                customer.getQuocTich()
            };
            model.addRow(rowData);
        }
        
        customerTable.setModel(model);
    }
    
    private void showAddCustomerDialog() {
        AddEditCustomerDialog dialog = new AddEditCustomerDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadCustomerData();
    }
        JOptionPane.showMessageDialog(this, "Add Customer Dialog will be implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showEditCustomerDialog(int customerId) {
        KhachHang selectedCustomer = khachHangService.timTheoMa(customerId);
    
    AddEditCustomerDialog dialog = new AddEditCustomerDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        selectedCustomer
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadCustomerData();
    }
        JOptionPane.showMessageDialog(this, "Edit Customer Dialog for ID: " + customerId + " will be implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void confirmAndDeleteCustomer(int customerId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete customer with ID: " + customerId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = khachHangService.xoa(customerId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Customer deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadCustomerData(); // Refresh data
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete customer.",
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
}