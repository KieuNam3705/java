package com.tourmanagement.ui.dialogs;

import com.tourmanagement.ui.components.CustomButton;
import com.tourmanagement.ui.components.CustomLabel;
import com.tourmanagement.ui.components.CustomTable;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IThongKeService;
import com.tourmanagement.service.interfaces.ITourService;
import com.tourmanagement.service.interfaces.IDiaDiemService;
import com.tourmanagement.entity.Tour;
import com.tourmanagement.entity.DiaDiem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ReportDetailsDialog extends JDialog {
    private IThongKeService thongKeService;
    private ITourService tourService;
    private IDiaDiemService diaDiemService;
    
    private String reportType;
    private Date fromDate;
    private Date toDate;
    private int year;
    private int month; // -1 for all months
    
    private JPanel contentPanel;
    private JPanel chartPanel;
    private CustomTable detailsTable;
    private CustomButton exportButton;
    private CustomButton closeButton;
    
    public ReportDetailsDialog(Frame parent, String reportType, Date fromDate, Date toDate, int year, int month) {
        super(parent, "Chi tiết báo cáo", true);
        
        this.thongKeService = ServiceFactory.getInstance().getThongKeService();
        this.tourService = ServiceFactory.getInstance().getTourService();
        this.diaDiemService = ServiceFactory.getInstance().getDiaDiemService();
        
        this.reportType = reportType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.year = year;
        this.month = month;
        
        initComponents();
        loadReportData();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        String titleText = "BÁO CÁO " + getReportTitle();
        CustomLabel titleLabel = new CustomLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 55, 153));
        
        titlePanel.add(titleLabel);
        
        // Date range info
        JPanel dateRangePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dateRangePanel.setBackground(Color.WHITE);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateRangeText = "Từ ngày " + dateFormat.format(fromDate) + " đến ngày " + dateFormat.format(toDate);
        JLabel dateRangeLabel = new JLabel(dateRangeText);
        dateRangeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        
        dateRangePanel.add(dateRangeLabel);
        
        // Content panel (will be populated based on report type)
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        
        // Chart panel
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ"));
        
        // Placeholder for chart (would be replaced with actual chart)
        JPanel chartPlaceholder = new JPanel();
        chartPlaceholder.setPreferredSize(new Dimension(600, 300));
        chartPlaceholder.setBackground(new Color(245, 245, 245));
        JLabel chartLabel = new JLabel("Biểu đồ sẽ được hiển thị tại đây", JLabel.CENTER);
        chartPlaceholder.add(chartLabel);
        
        chartPanel.add(chartPlaceholder, BorderLayout.CENTER);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Chi tiết dữ liệu"));
        
        detailsTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(detailsTable);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add chart and table to content panel
        contentPanel.add(chartPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        exportButton = new CustomButton("Xuất báo cáo");
        exportButton.setStyleSecondary();
        
        closeButton = new CustomButton("Đóng");
        closeButton.setStylePrimary();
        
        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);
        
        // Add to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(dateRangePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to dialog
        getContentPane().add(mainPanel);
        
        // Set dialog properties
        pack();
      
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        
        // Add action listeners
        closeButton.addActionListener(e -> dispose());
        exportButton.addActionListener(e -> exportReport());
    }
    
    private String getReportTitle() {
        switch (reportType) {
            case "Doanh thu":
                return "DOANH THU" + getTimePeriodString();
            case "Lượng khách":
                return "LƯỢNG KHÁCH" + getTimePeriodString();
            case "Tour bán chạy":
                return "TOUR BÁN CHẠY";
            case "Doanh thu theo địa điểm":
                return "DOANH THU THEO ĐỊA ĐIỂM";
            default:
                return "";
        }
    }
    
    private String getTimePeriodString() {
        if (month == -1) {
            return " NĂM " + year;
        } else {
            return " THÁNG " + month + "/" + year;
        }
    }
    
    private void loadReportData() {
        switch (reportType) {
            case "Doanh thu":
                loadRevenueReport();
                break;
            case "Lượng khách":
                loadCustomerReport();
                break;
            case "Tour bán chạy":
                loadTopToursReport();
                break;
            case "Doanh thu theo địa điểm":
                loadRevenueByLocationReport();
                break;
        }
    }
    
    private void loadRevenueReport() {
        // Get revenue data from service
        Map<String, Double> revenueData = thongKeService.doanhThuTheoThang(year);
        
        // Create table model
        String[] columnNames = {"Tháng", "Doanh Thu (VNĐ)", "So Với Tháng Trước"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Add data rows
        double previousRevenue = 0;
        for (int i = 1; i <= 12; i++) {
            String monthKey = "Tháng " + i;
            double revenue = revenueData.get(monthKey);
            
            String change = "";
            if (i > 1) {
                double percentChange = (revenue - previousRevenue) / (previousRevenue == 0 ? 1 : previousRevenue) * 100;
                change = String.format("%.2f%%", percentChange);
                if (percentChange > 0) {
                    change = "+" + change;
                }
            }
            
            Object[] rowData = {
                "Tháng " + i,
                currencyFormat.format(revenue),
                change
            };
            
            model.addRow(rowData);
            previousRevenue = revenue;
        }
        
        detailsTable.setModel(model);
        
        // If specific month is selected, highlight that month
        if (month != -1) {
            detailsTable.setRowSelectionInterval(month - 1, month - 1);
        }
        
        // TODO: Replace chart placeholder with actual chart
    }
    
    private void loadCustomerReport() {
        // Get customer count data from service
        Map<String, Integer> customerData = thongKeService.soLuongKhachTheoThang(year);
        
        // Create table model
        String[] columnNames = {"Tháng", "Số Lượng Khách", "So Với Tháng Trước"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add data rows
        int previousCount = 0;
        for (int i = 1; i <= 12; i++) {
            String monthKey = "Tháng " + i;
            int count = customerData.get(monthKey);
            
            String change = "";
            if (i > 1) {
                double percentChange = (count - previousCount) / (previousCount == 0 ? 1 : previousCount) * 100;
                change = String.format("%.2f%%", percentChange);
                if (percentChange > 0) {
                    change = "+" + change;
                }
            }
            
            Object[] rowData = {
                "Tháng " + i,
                count,
                change
            };
            
            model.addRow(rowData);
            previousCount = count;
        }
        
        detailsTable.setModel(model);
        
        // If specific month is selected, highlight that month
        if (month != -1) {
            detailsTable.setRowSelectionInterval(month - 1, month - 1);
        }
        
        // TODO: Replace chart placeholder with actual chart
    }
    
    private void loadTopToursReport() {
        // Get top tours data from service
        List<Map<String, Object>> topTours = thongKeService.top10TourBanChay(fromDate, toDate);
        
        // Create table model
        String[] columnNames = {"Mã Tour", "Tên Tour", "Số Lượt Đặt", "Doanh Thu", "Giá Tour"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Add data rows
        for (Map<String, Object> tour : topTours) {
            Object[] rowData = {
                tour.get("maTour"),
                tour.get("tenTour"),
                tour.get("soLuotDat"),
                currencyFormat.format(tour.get("doanhThu")),
                currencyFormat.format(tour.get("giaTour"))
            };
            
            model.addRow(rowData);
        }
        
        detailsTable.setModel(model);
        
        // TODO: Replace chart placeholder with actual chart
    }
    
    private void loadRevenueByLocationReport() {
        // Get revenue by location data from service
        List<Map<String, Object>> locationRevenue = thongKeService.doanhThuTheoDiaDiem(fromDate, toDate);
        
        // Create table model
        String[] columnNames = {"Mã Địa Điểm", "Tên Địa Điểm", "Thành Phố", "Quốc Gia", "Doanh Thu", "Số Lượt Ghé Thăm"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Add data rows
        for (Map<String, Object> location : locationRevenue) {
            Object[] rowData = {
                location.get("maDiaDiem"),
                location.get("tenDiaDiem"),
                location.get("thanhPho"),
                location.get("quocGia"),
                currencyFormat.format(location.get("doanhThu")),
                location.get("soLuotGheTham")
            };
            
            model.addRow(rowData);
        }
        
        detailsTable.setModel(model);
        
        // TODO: Replace chart placeholder with actual chart
    }
    
    private void exportReport() {
        // This would be implemented to export the report data
        JOptionPane.showMessageDialog(this, 
            "Chức năng xuất báo cáo sẽ được triển khai sau", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}