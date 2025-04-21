package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IDatTourService;
import com.tourmanagement.service.interfaces.IKhachHangService;
import com.tourmanagement.service.interfaces.ITourService;
import com.tourmanagement.entity.DatTour;
import com.tourmanagement.entity.KhachHang;
import com.tourmanagement.entity.Tour;
import com.tourmanagement.ui.dialogs.AddEditBookingDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class BookingManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IDatTourService datTourService;
    private IKhachHangService khachHangService;
    private ITourService tourService;
    
    private CustomTable bookingTable;
    private SearchPanel searchPanel;
    private CustomButton addButton;
    private CustomButton viewButton;
    private CustomButton updateStatusButton;
    private CustomButton deleteButton;
    
    private JComboBox<String> statusFilterComboBox;
    
    public BookingManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        datTourService = serviceFactory.getDatTourService();
        khachHangService = serviceFactory.getKhachHangService();
        tourService = serviceFactory.getTourService();
        
        setupPanel();
        loadBookingData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Quản Lý Đặt Tour");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addButton = new CustomButton("Thêm Đặt Tour");
        addButton.setStylePrimary();
        
        viewButton = new CustomButton("Xem Chi Tiết");
        viewButton.setStyleSecondary();
        
        updateStatusButton = new CustomButton("Cập Nhật Trạng Thái");
        updateStatusButton.setStyleSecondary();
        
        deleteButton = new CustomButton("Xóa Đặt Tour");
        deleteButton.setStyleDanger();
        
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
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
        
        filterPanel.add(new JLabel("Lọc theo trạng thái:"));
        
        String[] statusOptions = {"Tất cả", "Chờ xác nhận", "Đã xác nhận", "Đã hủy", "Hoàn thành", "Đang diễn ra"};
        statusFilterComboBox = new JComboBox<>(statusOptions);
        statusFilterComboBox.addActionListener(e -> filterBookingsByStatus());
        
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
        
        bookingTable = new CustomTable();
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        
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
        searchPanel.addSearchListener(e -> searchBookings());
        
        // Add action listeners for buttons
        addButton.addActionListener(e -> showAddBookingDialog());
        viewButton.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                showViewBookingDialog(bookingId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một đặt tour để xem chi tiết.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        updateStatusButton.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                showUpdateStatusDialog(bookingId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một đặt tour để cập nhật trạng thái.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                confirmAndDeleteBooking(bookingId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một đặt tour để xóa.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void loadBookingData() {
        // Lấy danh sách đặt tour từ service
        List<DatTour> bookings = datTourService.layDanhSach();
        
        updateBookingTable(bookings);
    }
    
    private void updateBookingTable(List<DatTour> bookings) {
        // Tạo model cho bảng
        String[] columnNames = {"Mã Đặt Tour", "Khách Hàng", "Tour", "Ngày Đặt", "Số Khách", "Tổng Tiền", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Thêm dữ liệu vào model
        for (DatTour booking : bookings) {
            // Lấy thông tin khách hàng và tour
            KhachHang khachHang = khachHangService.timTheoMa(booking.getMaKH());
            Tour tour = tourService.timTheoMa(booking.getMaTour());
            
            String customerName = (khachHang != null) ? khachHang.getHoTen() : "Không xác định";
            String tourName = (tour != null) ? tour.getTenTour() : "Không xác định";
            
            Object[] rowData = {
                booking.getMaDatTour(),
                customerName,
                tourName,
                dateFormat.format(booking.getNgayDat()),
                booking.getSoLuongNguoi(),
                booking.getTongTien().toString(),
                booking.getTrangThai()
            };
            model.addRow(rowData);
        }
        
        bookingTable.setModel(model);
    }
    
    private void searchBookings() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            loadBookingData(); // Nếu không có text tìm kiếm, hiển thị tất cả
            return;
        }
        
        // Tìm kiếm đặt tour (giả sử có phương thức tìm kiếm trong service)
        // Trong thực tế, bạn có thể cần triển khai logic tìm kiếm phức tạp hơn
        
        // Ví dụ: tìm kiếm theo khách hàng
        List<KhachHang> khachHangs = khachHangService.timTheoTen(searchText);
        if (khachHangs.isEmpty()) {
            // Nếu không tìm thấy khách hàng, hiển thị bảng trống
            bookingTable.setModel(new DefaultTableModel(
                new String[]{"Mã Đặt Tour", "Khách Hàng", "Tour", "Ngày Đặt", "Số Khách", "Tổng Tiền", "Trạng Thái"}, 
                0
            ));
            return;
        }
        
        // Lấy danh sách đặt tour của các khách hàng tìm thấy
        List<DatTour> results = null;
        for (KhachHang kh : khachHangs) {
            List<DatTour> datTours = datTourService.layDanhSachTheoKhachHang(kh.getMaKH());
            if (results == null) {
                results = datTours;
            } else {
                results.addAll(datTours);
            }
        }
        
        // Hiển thị kết quả
        if (results != null && !results.isEmpty()) {
            updateBookingTable(results);
        } else {
            bookingTable.setModel(new DefaultTableModel(
                new String[]{"Mã Đặt Tour", "Khách Hàng", "Tour", "Ngày Đặt", "Số Khách", "Tổng Tiền", "Trạng Thái"}, 
                0
            ));
        }
    }
    
    private void filterBookingsByStatus() {
        String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
        
        if ("Tất cả".equals(selectedStatus)) {
            loadBookingData(); // Hiển thị tất cả
            return;
        }
        
        // Lấy danh sách đặt tour theo trạng thái
        List<DatTour> filteredBookings = datTourService.layDanhSachTheoTrangThai(selectedStatus);
        updateBookingTable(filteredBookings);
    }
    
    private void showAddBookingDialog() {
        // Lấy mã nhân viên hiện tại từ session hoặc context
    int currentStaffId = getCurrentStaffId(); // Bạn cần implement phương thức này
    
    AddEditBookingDialog dialog = new AddEditBookingDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        null, 
        currentStaffId
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadBookingData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng thêm đặt tour sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showViewBookingDialog(int bookingId) {
         int currentStaffId = getCurrentStaffId(); 
    
    DatTour selectedBooking = datTourService.timTheoMa(bookingId);
    
    AddEditBookingDialog dialog = new AddEditBookingDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), 
        selectedBooking,
        currentStaffId
    );
    dialog.setVisible(true);
    
    if (dialog.isSaveSuccess()) {
        loadBookingData();
    }
        JOptionPane.showMessageDialog(this, "Chức năng xem chi tiết đặt tour " + bookingId + " sẽ được triển khai", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showUpdateStatusDialog(int bookingId) {
        // Lấy thông tin đặt tour
        DatTour booking = datTourService.timTheoMa(bookingId);
        if (booking == null) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy đặt tour với mã: " + bookingId, 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tạo dialog để cập nhật trạng thái
        String[] statusOptions = {"Chờ xác nhận", "Đã xác nhận", "Đã hủy", "Hoàn thành", "Đang diễn ra"};
        String selectedStatus = (String) JOptionPane.showInputDialog(
            this,
            "Chọn trạng thái mới cho đặt tour #" + bookingId,
            "Cập Nhật Trạng Thái Đặt Tour",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusOptions,
            booking.getTrangThai() // Giá trị mặc định là trạng thái hiện tại
        );
        
        // Nếu người dùng hủy hoặc không chọn gì
        if (selectedStatus == null) {
            return;
        }
        
        // Cập nhật trạng thái
        try {
            boolean success = datTourService.capNhatTrangThai(bookingId, selectedStatus);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Cập nhật trạng thái thành công.",
                    "Thành Công",
                    JOptionPane.INFORMATION_MESSAGE);
                loadBookingData(); // Tải lại dữ liệu
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không thể cập nhật trạng thái đặt tour.",
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
    
    private void confirmAndDeleteBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa đặt tour với mã: " + bookingId + "?",
            "Xác Nhận Xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = datTourService.xoa(bookingId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Xóa đặt tour thành công.",
                        "Thành Công",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadBookingData(); // Tải lại dữ liệu
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể xóa đặt tour.",
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
    private int getCurrentStaffId() {
    return 0;
}
}