package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IThongKeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ReportPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IThongKeService thongKeService;
    
    private JPanel statisticsPanel;
    private JPanel topToursPanel;
    private JPanel revenueByLocationPanel;
    
    private JComboBox<String> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> reportTypeComboBox;
    
    private CustomButton generateReportButton;
    
    private Date fromDate;
    private Date toDate;
    
    public ReportPanel() {
        serviceFactory = ServiceFactory.getInstance();
        thongKeService = serviceFactory.getThongKeService();
        
        setupPanel();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Báo Cáo & Thống Kê");
        titleLabel.setHeaderStyle();
        
        // Filter panel for reports
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filterPanel.setBackground(new Color(245, 246, 250));
        
        // Year selection
        filterPanel.add(new JLabel("Năm:"));
        String[] years = {"2025", "2024", "2023", "2022", "2021"};
        yearComboBox = new JComboBox<>(years);
        filterPanel.add(yearComboBox);
        
        // Month selection
        filterPanel.add(new JLabel("Tháng:"));
        String[] months = {"Tất cả", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        monthComboBox = new JComboBox<>(months);
        filterPanel.add(monthComboBox);
        
        // Report type selection
        filterPanel.add(new JLabel("Loại báo cáo:"));
        String[] reportTypes = {"Doanh thu", "Lượng khách", "Tour bán chạy", "Doanh thu theo địa điểm"};
        reportTypeComboBox = new JComboBox<>(reportTypes);
        filterPanel.add(reportTypeComboBox);
        
        // Generate report button
        generateReportButton = new CustomButton("Tạo Báo Cáo");
        generateReportButton.setStylePrimary();
        filterPanel.add(generateReportButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Main content panel with statistics and charts
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 246, 250));
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(filterPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Statistics summary panels
        statisticsPanel = createStatisticsPanel();
        contentPanel.add(statisticsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Create panels for detailed reports
        JPanel detailedReportsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        detailedReportsPanel.setBackground(new Color(245, 246, 250));
        
        topToursPanel = createTopToursPanel();
        revenueByLocationPanel = createRevenueByLocationPanel();
        
        detailedReportsPanel.add(topToursPanel);
        detailedReportsPanel.add(revenueByLocationPanel);
        
        contentPanel.add(detailedReportsPanel);
        
        // Add to scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add action listener for generate report button
        generateReportButton.addActionListener(e -> generateReport());
        
        // Initialize with current data
        initializeDateRange();
        generateReport();
    }
    
    private void initializeDateRange() {
        // Mặc định hiển thị báo cáo của tháng hiện tại
        Calendar cal = Calendar.getInstance();
        
        // Ngày cuối của tháng
        toDate = cal.getTime();
        
        // Ngày đầu của tháng
        cal.set(Calendar.DAY_OF_MONTH, 1);
        fromDate = cal.getTime();
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(245, 246, 250));
        
        // Các thẻ thống kê sẽ được cập nhật khi tạo báo cáo
        panel.add(new StatsCard(
            "Tổng Doanh Thu", 
            "0 VNĐ", 
            "Đang tải dữ liệu...", 
            new Color(21, 101, 192) // Màu xanh dương
        ));
        
        panel.add(new StatsCard(
            "Số Tour Đã Tổ Chức", 
            "0", 
            "Đang tải dữ liệu...", 
            new Color(125, 95, 255) // Màu tím
        ));
        
        panel.add(new StatsCard(
            "Tổng Số Khách Hàng", 
            "0", 
            "Đang tải dữ liệu...", 
            new Color(255, 168, 1) // Màu vàng/cam
        ));
        
        return panel;
    }
    
    private JPanel createTopToursPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        CustomLabel titleLabel = new CustomLabel("Top Tour Bán Chạy");
        titleLabel.setSubheaderStyle();
        
        // Table for top tours
        String[] columnNames = {"Tên Tour", "Số Lượt Đặt", "Doanh Thu", "Giá Tour"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        CustomTable table = new CustomTable();
        table.setModel(model);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRevenueByLocationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        CustomLabel titleLabel = new CustomLabel("Doanh Thu Theo Địa Điểm");
        titleLabel.setSubheaderStyle();
        
        // Table for revenue by location
        String[] columnNames = {"Địa Điểm", "Thành Phố", "Quốc Gia", "Doanh Thu", "Số Lượt Ghé Thăm"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        CustomTable table = new CustomTable();
        table.setModel(model);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void generateReport() {
        // Lấy năm và tháng được chọn
        int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        
        // Thiết lập khoảng thời gian báo cáo
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        
        calFrom.set(Calendar.YEAR, selectedYear);
        calTo.set(Calendar.YEAR, selectedYear);
        
        if ("Tất cả".equals(selectedMonth)) {
            // Nếu chọn "Tất cả" thì lấy cả năm
            calFrom.set(Calendar.MONTH, Calendar.JANUARY);
            calFrom.set(Calendar.DAY_OF_MONTH, 1);
            
            calTo.set(Calendar.MONTH, Calendar.DECEMBER);
            calTo.set(Calendar.DAY_OF_MONTH, 31);
        } else {
            // Nếu chọn tháng cụ thể
            int month = Integer.parseInt(selectedMonth) - 1; // Calendar.MONTH bắt đầu từ 0
            
            calFrom.set(Calendar.MONTH, month);
            calFrom.set(Calendar.DAY_OF_MONTH, 1);
            
            calTo.set(Calendar.MONTH, month);
            calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        
        fromDate = calFrom.getTime();
        toDate = calTo.getTime();
        
        // Cập nhật các báo cáo
        updateStatisticsCards();
        updateTopToursTable();
        updateRevenueByLocationTable();
    }
    
    private void updateStatisticsCards() {
        // Lấy dữ liệu thống kê từ service
        double tongDoanhThu = thongKeService.tongDoanhThu(fromDate, toDate);
        int tongSoTour = thongKeService.tongSoTourDaTo(fromDate, toDate);
        int tongSoKhachHang = thongKeService.tongSoKhachHang(fromDate, toDate);
        
        // Format doanh thu
        String formattedRevenue = String.format("%,.0f VNĐ", tongDoanhThu);
        
        // Cập nhật các thẻ thống kê
        statisticsPanel.removeAll();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateRangeText = "Từ " + dateFormat.format(fromDate) + " đến " + dateFormat.format(toDate);
        
        statisticsPanel.add(new StatsCard(
            "Tổng Doanh Thu", 
            formattedRevenue, 
            dateRangeText, 
            new Color(21, 101, 192) // Màu xanh dương
        ));
        
        statisticsPanel.add(new StatsCard(
            "Số Tour Đã Tổ Chức", 
            String.valueOf(tongSoTour), 
            dateRangeText, 
            new Color(125, 95, 255) // Màu tím
        ));
        
        statisticsPanel.add(new StatsCard(
            "Tổng Số Khách Hàng", 
            String.valueOf(tongSoKhachHang), 
            dateRangeText, 
            new Color(255, 168, 1) // Màu vàng/cam
        ));
        
        statisticsPanel.revalidate();
        statisticsPanel.repaint();
    }
    
    private void updateTopToursTable() {
        // Lấy danh sách top 10 tour bán chạy
        List<Map<String, Object>> topTours = thongKeService.top10TourBanChay(fromDate, toDate);
        
        // Cập nhật bảng
        CustomTable table = (CustomTable) ((JScrollPane) topToursPanel.getComponent(1)).getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        // Thêm dữ liệu mới
        for (Map<String, Object> tour : topTours) {
            Object[] rowData = {
                tour.get("tenTour"),
                tour.get("soLuotDat"),
                String.format("%,.0f VNĐ", ((BigDecimal) tour.get("doanhThu")).doubleValue()),
                String.format("%,.0f VNĐ", ((BigDecimal) tour.get("giaTour")).doubleValue())
            };
            model.addRow(rowData);
        }
    }
    
    private void updateRevenueByLocationTable() {
        // Lấy doanh thu theo địa điểm
        List<Map<String, Object>> revenueByLocation = thongKeService.doanhThuTheoDiaDiem(fromDate, toDate);
        
        // Cập nhật bảng
        CustomTable table = (CustomTable) ((JScrollPane) revenueByLocationPanel.getComponent(1)).getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        // Thêm dữ liệu mới
        for (Map<String, Object> location : revenueByLocation) {
            Object[] rowData = {
                location.get("tenDiaDiem"),
                location.get("thanhPho"),
                location.get("quocGia"),
                String.format("%,.0f VNĐ", ((BigDecimal) location.get("doanhThu")).doubleValue()),
                location.get("soLuotGheTham")
            };
            model.addRow(rowData);
        }
    }
}