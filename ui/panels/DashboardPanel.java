package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IThongKeService thongKeService;
    private ITourService tourService;
    private IDatTourService datTourService;
    
    public DashboardPanel() {
        serviceFactory = ServiceFactory.getInstance();
        thongKeService = serviceFactory.getThongKeService();
        tourService = serviceFactory.getTourService();
        datTourService = serviceFactory.getDatTourService();
        
        setupPanel();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        CustomLabel titleLabel = new CustomLabel("Dashboard");
        titleLabel.setHeaderStyle();
        
        // Stats Cards
        JPanel statsPanel = createStatsPanel();
        
        // Recent Tours
        JPanel toursPanel = createRecentToursPanel();
        
        // Recent Bookings
        JPanel bookingsPanel = createRecentBookingsPanel();
        
        // Layout
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 246, 250));
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tablesPanel.setBackground(new Color(245, 246, 250));
        tablesPanel.add(toursPanel);
        tablesPanel.add(bookingsPanel);
        
        contentPanel.add(tablesPanel);
        
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(245, 246, 250));
        
        // Lấy dữ liệu thống kê từ service
        // Trong thực tế, sẽ lấy số liệu từ thongKeService
        
        StatsCard tourCard = new StatsCard(
            "Total Tours", 
            "125", 
            "Increased by 15%", 
            new Color(125, 95, 255) // Màu tím
        );
        
        StatsCard revenueCard = new StatsCard(
            "Total Revenue", 
            "$45,000", 
            "Increased by 22%", 
            new Color(21, 101, 192) // Màu xanh dương
        );
        
        StatsCard customersCard = new StatsCard(
            "Total Customers", 
            "843", 
            "Increased by 7%", 
            new Color(255, 168, 1) // Màu vàng/cam
        );
        
        statsPanel.add(tourCard);
        statsPanel.add(revenueCard);
        statsPanel.add(customersCard);
        
        return statsPanel;
    }
    
    private JPanel createRecentToursPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        CustomLabel titleLabel = new CustomLabel("Recent Tours");
        titleLabel.setSubheaderStyle();
        
        // Lấy dữ liệu tour gần đây từ tourService
        // Đây là dữ liệu mẫu, cần thay bằng dữ liệu thực từ service
        String[] columnNames = {"Tour Name", "Start Date", "Status", "Bookings"};
        Object[][] data = {
            {"Hạ Long Bay Tour", "15/04/2023", "Active", "12/20"},
            {"Sapa Discovery", "20/04/2023", "Active", "8/15"},
            {"Phú Quốc Island", "01/05/2023", "Pending", "5/25"},
            {"Đà Lạt City Tour", "10/05/2023", "Pending", "3/15"}
        };
        
        CustomTable table = new CustomTable();
        table.loadData(data, columnNames);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRecentBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        CustomLabel titleLabel = new CustomLabel("Recent Bookings");
        titleLabel.setSubheaderStyle();
        
        // Lấy dữ liệu đặt tour gần đây từ datTourService
        // Đây là dữ liệu mẫu, cần thay bằng dữ liệu thực từ service
        String[] columnNames = {"Customer", "Tour", "Date", "Status"};
        Object[][] data = {
            {"Nguyễn Văn A", "Hạ Long Bay Tour", "12/04/2023", "Confirmed"},
            {"Trần Thị B", "Sapa Discovery", "14/04/2023", "Pending"},
            {"Lê Văn C", "Phú Quốc Island", "15/04/2023", "Confirmed"},
            {"Phạm Thị D", "Đà Lạt City Tour", "16/04/2023", "Canceled"}
        };
        
        CustomTable table = new CustomTable();
        table.loadData(data, columnNames);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return panel;
    }
}