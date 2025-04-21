package com.tourmanagement.ui.frame;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.ui.panels.*;
import com.tourmanagement.entity.TaiKhoan;
import com.tourmanagement.entity.NhanVien;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.INhanVienService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class AdminMainFrame extends JFrame {
    private TaiKhoan account;
    private NhanVien admin;
    
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JPanel mainPanel;
    
    // Sidebar menu items
    private SidebarMenuItem dashboardMenuItem;
    private SidebarMenuItem tourMenuItem;
    private SidebarMenuItem customerMenuItem;
    private SidebarMenuItem staffMenuItem;
    private SidebarMenuItem bookingMenuItem;
    private SidebarMenuItem paymentMenuItem;
    private SidebarMenuItem locationMenuItem;
    private SidebarMenuItem accommodationMenuItem;
    private SidebarMenuItem transportationMenuItem;
    private SidebarMenuItem accountMenuItem;
    private SidebarMenuItem reportMenuItem;
    
    public AdminMainFrame(TaiKhoan account) {
        this.account = account;
        this.admin = loadAdminInfo();
        
        setTitle("Quản Lý Tour Du Lịch - Trang Quản Trị");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        
        // Set initial panel
        showDashboard();
    }
    
    private NhanVien loadAdminInfo() {
        if (account.getMaNV() != null) {
            INhanVienService nhanVienService = ServiceFactory.getInstance().getNhanVienService();
            return nhanVienService.timTheoMa(account.getMaNV());
        }
        return null;
    }
    
    private void initComponents() {
        // Main container panel
        mainPanel = new JPanel(new BorderLayout());
        
        // Create sidebar
        createSidebar();
        
        // Create header
        JPanel headerPanel = createHeaderPanel();
        
        // Content panel (will be replaced with different panels)
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 246, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Add components to main panel
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
    }
    
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(30, 55, 153));
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        
        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(30, 55, 153));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        // Logo text
        JLabel logoLabel = new JLabel("ADMIN DASHBOARD");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        
        logoPanel.add(logoLabel);
        
        // Menu items panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(30, 55, 153));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create menu items with icons
        dashboardMenuItem = new SidebarMenuItem("Dashboard", null);
        tourMenuItem = new SidebarMenuItem("Quản lý Tour", null);
        customerMenuItem = new SidebarMenuItem("Quản lý Khách hàng", null);
        staffMenuItem = new SidebarMenuItem("Quản lý Nhân viên", null);
        bookingMenuItem = new SidebarMenuItem("Quản lý Đặt tour", null);
        paymentMenuItem = new SidebarMenuItem("Quản lý Thanh toán", null);
        locationMenuItem = new SidebarMenuItem("Quản lý Địa điểm", null);
        accommodationMenuItem = new SidebarMenuItem("Quản lý Chỗ ở", null);
        transportationMenuItem = new SidebarMenuItem("Quản lý Phương tiện", null);
        accountMenuItem = new SidebarMenuItem("Quản lý Tài khoản", null);
        reportMenuItem = new SidebarMenuItem("Báo cáo & Thống kê", null);
        
        // Add menu items to panel
        menuPanel.add(dashboardMenuItem);
        menuPanel.add(tourMenuItem);
        menuPanel.add(customerMenuItem);
        menuPanel.add(staffMenuItem);
        menuPanel.add(bookingMenuItem);
        menuPanel.add(paymentMenuItem);
        menuPanel.add(locationMenuItem);
        menuPanel.add(accommodationMenuItem);
        menuPanel.add(transportationMenuItem);
        menuPanel.add(accountMenuItem);
        menuPanel.add(reportMenuItem);
        
        // Add action listeners to menu items
        dashboardMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(dashboardMenuItem);
                showDashboard();
            }
        });
        
        tourMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(tourMenuItem);
                showTourManagement();
            }
        });
        
        customerMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(customerMenuItem);
                showCustomerManagement();
            }
        });
        
        staffMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(staffMenuItem);
                showStaffManagement();
            }
        });
        
        bookingMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(bookingMenuItem);
                showBookingManagement();
            }
        });
        
        paymentMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(paymentMenuItem);
                showPaymentManagement();
            }
        });
        
        locationMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(locationMenuItem);
                showLocationManagement();
            }
        });
        
        accommodationMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(accommodationMenuItem);
                showAccommodationManagement();
            }
        });
        
        transportationMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(transportationMenuItem);
                showTransportationManagement();
            }
        });
        
        accountMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(accountMenuItem);
                showAccountManagement();
            }
        });
        
        reportMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(reportMenuItem);
                showReports();
            }
        });
        
        // Add logout button at bottom
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(new Color(30, 55, 153));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        CustomButton logoutButton = new CustomButton("Đăng Xuất");
        logoutButton.setStyleDanger();
        logoutButton.addActionListener(e -> logout());
        
        logoutPanel.add(logoutButton);
        
        // Add panels to sidebar
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(menuPanel);
        sidebarPanel.add(Box.createVerticalGlue()); // Pushes logout to bottom
        sidebarPanel.add(logoutPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Header title (left side)
        JLabel headerTitle = new JLabel("Hệ Thống Quản Lý Tour Du Lịch");
        headerTitle.setFont(new Font("Arial", Font.BOLD, 16));
        
        // User info panel (right side)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        
        JLabel userLabel = new JLabel();
        if (admin != null) {
            userLabel.setText(admin.getHoTen() + " (Admin)");
        } else {
            userLabel.setText("Admin");
        }
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        userPanel.add(userLabel);
        
        // Add components to header
        headerPanel.add(headerTitle, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void selectMenuItem(SidebarMenuItem selected) {
        // Deselect all menu items
        dashboardMenuItem.setSelected(false);
        tourMenuItem.setSelected(false);
        customerMenuItem.setSelected(false);
        staffMenuItem.setSelected(false);
        bookingMenuItem.setSelected(false);
        paymentMenuItem.setSelected(false);
        locationMenuItem.setSelected(false);
        accommodationMenuItem.setSelected(false);
        transportationMenuItem.setSelected(false);
        accountMenuItem.setSelected(false);
        reportMenuItem.setSelected(false);
        
        // Select the clicked menu item
        selected.setSelected(true);
    }
    
    // Methods to show different panels
    private void showDashboard() {
        contentPanel.removeAll();
        DashboardPanel dashboard = new DashboardPanel();
        contentPanel.add(dashboard, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showTourManagement() {
        contentPanel.removeAll();
        TourManagementPanel tourPanel = new TourManagementPanel();
        contentPanel.add(tourPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showCustomerManagement() {
        contentPanel.removeAll();
        CustomerManagementPanel customerPanel = new CustomerManagementPanel();
        contentPanel.add(customerPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showStaffManagement() {
        contentPanel.removeAll();
        StaffManagementPanel staffPanel = new StaffManagementPanel();
        contentPanel.add(staffPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showBookingManagement() {
        contentPanel.removeAll();
        BookingManagementPanel bookingPanel = new BookingManagementPanel();
        contentPanel.add(bookingPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showPaymentManagement() {
        contentPanel.removeAll();
        PaymentManagementPanel paymentPanel = new PaymentManagementPanel();
        contentPanel.add(paymentPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showLocationManagement() {
        contentPanel.removeAll();
        PlaceManagementPanel placePanel = new PlaceManagementPanel();
        contentPanel.add(placePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAccommodationManagement() {
        contentPanel.removeAll();
        AccommodationManagementPanel accommodationPanel = new AccommodationManagementPanel();
        contentPanel.add(accommodationPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showTransportationManagement() {
        contentPanel.removeAll();
        TransportationManagementPanel transportationPanel = new TransportationManagementPanel();
        contentPanel.add(transportationPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAccountManagement() {
        contentPanel.removeAll();
        AccountManagementPanel accountPanel = new AccountManagementPanel();
        contentPanel.add(accountPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showReports() {
        contentPanel.removeAll();
        ReportPanel reportPanel = new ReportPanel();
        contentPanel.add(reportPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        }
    }
}