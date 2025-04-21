package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.ui.model.TourModel;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.ITourService;
import com.tourmanagement.entity.Tour;
import com.tourmanagement.ui.dialogs.AddEditTourDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TourManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private ITourService tourService;
    
    private CustomTable tourTable;
    private SearchPanel searchPanel;
    private CustomButton addButton;
    private CustomButton editButton;
    private CustomButton deleteButton;
    
    public TourManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        tourService = serviceFactory.getTourService();
        
        setupPanel();
        loadTourData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Tour Management");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addButton = new CustomButton("Thêm Tour");
        addButton.setStylePrimary();
        
        editButton = new CustomButton("Sửa Tour");
        editButton.setStyleSecondary();
        
        deleteButton = new CustomButton("Xóa Tour");
        deleteButton.setStyleDanger();
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Search panel
        searchPanel = new SearchPanel();
        searchPanel.addSearchListener(e -> searchTours());
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        tourTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(tourTable);
        
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
        addButton.addActionListener(e -> showAddTourDialog());
        editButton.addActionListener(e -> {
            int selectedRow = tourTable.getSelectedRow();
            if (selectedRow != -1) {
                int tourId = (int) tourTable.getValueAt(selectedRow, 0);
                showEditTourDialog(tourId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Chọn tour để sửa", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = tourTable.getSelectedRow();
            if (selectedRow != -1) {
                int tourId = (int) tourTable.getValueAt(selectedRow, 0);
                confirmAndDeleteTour(tourId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cần chọn tour để xóa", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void loadTourData() {
        // Lấy danh sách tour từ service
        List<Tour> tours = tourService.layDanhSach();
        
        // Tạo model cho bảng
        String[] columnNames = {"Mã Tour", " Tên Tour", "Ngày bắt đầu", "Ngày kết thúc", "Số ngày", "Số khách tôi đa", "Gía", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Thêm dữ liệu vào model
        for (Tour tour : tours) {
            Object[] rowData = {
                tour.getMaTour(),
                tour.getTenTour(),
                dateFormat.format(tour.getNgayBatDau()),
                dateFormat.format(tour.getNgayKetThuc()),
                tour.getSoNgay(),
                tour.getSoLuongKhachToiDa(),
                tour.getGiaTour().toString(),
                tour.getTrangThai()
            };
            model.addRow(rowData);
        }
        
        tourTable.setModel(model);
    }
    
    private void searchTours() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            loadTourData(); // Nếu không có text tìm kiếm, hiển thị tất cả
            return;
        }
        
        // Tìm tour theo tên
        List<Tour> searchResults = tourService.timTheoTen(searchText);
        
        // Tạo model cho bảng
        String[] columnNames = {"Mã Tour", " Tên Tour", "Ngày bắt đầu", "Ngày kết thúc", "Số ngày", "Số khách tôi đa", "Gía", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Thêm dữ liệu vào model
        for (Tour tour : searchResults) {
            Object[] rowData = {
                tour.getMaTour(),
                tour.getTenTour(),
                dateFormat.format(tour.getNgayBatDau()),
                dateFormat.format(tour.getNgayKetThuc()),
                tour.getSoNgay(),
                tour.getSoLuongKhachToiDa(),
                tour.getGiaTour().toString(),
                tour.getTrangThai()
            };
            model.addRow(rowData);
        }
        
        tourTable.setModel(model);
    }
    
    private void showAddTourDialog() {
        AddEditTourDialog dialog = new AddEditTourDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadTourData();
    }
        
    }
    
    private void showEditTourDialog(int tourId) {
         Tour selectedTour = tourService.timTheoMa(tourId);
    
    AddEditTourDialog dialog = new AddEditTourDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        selectedTour
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadTourData();
    }
        
    }
    
    private void confirmAndDeleteTour(int tourId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa tour có id : " + tourId + "?",
            "Xác nhận xóa ",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = tourService.xoa(tourId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Tour đã được xóa.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadTourData(); // Refresh data
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa tour.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}