package com.tourmanagement.ui.panels;

import com.tourmanagement.ui.components.*;
import com.tourmanagement.service.ServiceFactory;
import com.tourmanagement.service.interfaces.IHoaDonService;
import com.tourmanagement.service.interfaces.IChiTietThanhToanService;
import com.tourmanagement.service.interfaces.IDatTourService;
import com.tourmanagement.entity.HoaDon;
import com.tourmanagement.entity.ChiTietThanhToan;
import com.tourmanagement.entity.DatTour;
import com.tourmanagement.ui.dialogs.AddPaymentDialog;
import com.tourmanagement.ui.dialogs.ViewInvoiceDialog;
import com.tourmanagement.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentManagementPanel extends JPanel {
    private ServiceFactory serviceFactory;
    private IHoaDonService hoaDonService;
    private IChiTietThanhToanService chiTietThanhToanService;
    private IDatTourService datTourService;
    
    private CustomTable invoiceTable;
    private CustomTable paymentDetailTable;
    private SearchPanel searchPanel;
    private CustomButton addPaymentButton;
    private CustomButton viewInvoiceButton;
    private CustomButton printInvoiceButton;
    
    private JComboBox<String> statusFilterComboBox;
    
    public PaymentManagementPanel() {
        serviceFactory = ServiceFactory.getInstance();
        hoaDonService = serviceFactory.getHoaDonService();
        chiTietThanhToanService = serviceFactory.getChiTietThanhToanService();
        datTourService = serviceFactory.getDatTourService();
        setupPanel();
        loadInvoiceData();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 246, 250));
        
        CustomLabel titleLabel = new CustomLabel("Quản Lý Thanh Toán");
        titleLabel.setHeaderStyle();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 246, 250));
        
        addPaymentButton = new CustomButton("Thêm Thanh Toán");
        addPaymentButton.setStylePrimary();
        
        viewInvoiceButton = new CustomButton("Xem Chi Tiết");
        viewInvoiceButton.setStyleSecondary();
        
        printInvoiceButton = new CustomButton("In Hóa Đơn");
        printInvoiceButton.setStyleSecondary();
        
        buttonPanel.add(addPaymentButton);
        buttonPanel.add(viewInvoiceButton);
        buttonPanel.add(printInvoiceButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Search and filter panel
        JPanel searchFilterPanel = new JPanel(new BorderLayout());
        searchFilterPanel.setBackground(new Color(245, 246, 250));
        
        searchPanel = new SearchPanel();
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(new Color(245, 246, 250));
        
        filterPanel.add(new JLabel("Trạng thái:"));
        
        String[] statusOptions = {"Tất cả", "Đã thanh toán", "Chưa thanh toán", "Thanh toán một phần"};
        statusFilterComboBox = new JComboBox<>(statusOptions);
        statusFilterComboBox.addActionListener(e -> filterInvoicesByStatus());
        
        filterPanel.add(statusFilterComboBox);
        
        searchFilterPanel.add(searchPanel, BorderLayout.WEST);
        searchFilterPanel.add(filterPanel, BorderLayout.EAST);
        
        // Tables panel
        JPanel tablesPanel = new JPanel(new BorderLayout());
        tablesPanel.setBackground(new Color(245, 246, 250));
        
        // Invoices table
        JPanel invoiceTablePanel = new JPanel(new BorderLayout());
        invoiceTablePanel.setBackground(Color.WHITE);
        invoiceTablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        CustomLabel invoiceLabel = new CustomLabel("Danh Sách Hóa Đơn");
        invoiceLabel.setSubheaderStyle();
        
        invoiceTable = new CustomTable();
        JScrollPane invoiceScrollPane = new JScrollPane(invoiceTable);
        
        invoiceTablePanel.add(invoiceLabel, BorderLayout.NORTH);
        invoiceTablePanel.add(invoiceScrollPane, BorderLayout.CENTER);
        
        // Payment details table
        JPanel paymentDetailPanel = new JPanel(new BorderLayout());
        paymentDetailPanel.setBackground(Color.WHITE);
        paymentDetailPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        CustomLabel detailLabel = new CustomLabel("Chi Tiết Thanh Toán");
        detailLabel.setSubheaderStyle();
        
        paymentDetailTable = new CustomTable();
        JScrollPane detailScrollPane = new JScrollPane(paymentDetailTable);
        
        paymentDetailPanel.add(detailLabel, BorderLayout.NORTH);
        paymentDetailPanel.add(detailScrollPane, BorderLayout.CENTER);
        
        // Add tables to panel with split layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, invoiceTablePanel, paymentDetailPanel);
        splitPane.setResizeWeight(0.6); // 60% top, 40% bottom
        splitPane.setDividerSize(5);
        splitPane.setContinuousLayout(true);
        
        tablesPanel.add(splitPane, BorderLayout.CENTER);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 246, 250));
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(searchFilterPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(tablesPanel);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Add search listener
        searchPanel.addSearchListener(e -> searchInvoices());
        
        // Add action listeners for buttons
        addPaymentButton.addActionListener(e -> {
            int selectedRow = invoiceTable.getSelectedRow();
            if (selectedRow != -1) {
                int invoiceId = (int) invoiceTable.getValueAt(selectedRow, 0);
                showAddPaymentDialog(invoiceId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một hóa đơn để thêm thanh toán.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
     viewInvoiceButton.addActionListener(e -> {
    int selectedRow = invoiceTable.getSelectedRow();
    if (selectedRow != -1) {
        try {
            int invoiceId = (int) invoiceTable.getValueAt(selectedRow, 0);
            
            // Kiểm tra hóa đơn có tồn tại không
            HoaDon hoaDon = hoaDonService.timTheoMa(invoiceId);
            if (hoaDon == null) {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy hóa đơn với mã: " + invoiceId, 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Mở dialog xem chi tiết hóa đơn
            ViewInvoiceDialog dialog = new ViewInvoiceDialog(
                (Frame) SwingUtilities.getWindowAncestor(this), 
                invoiceId
            );
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, 
            "Vui lòng chọn một hóa đơn để xem chi tiết.", 
            "Chưa Chọn", 
            JOptionPane.WARNING_MESSAGE);
    }
});
        
        printInvoiceButton.addActionListener(e -> {
            int selectedRow = invoiceTable.getSelectedRow();
            if (selectedRow != -1) {
                int invoiceId = (int) invoiceTable.getValueAt(selectedRow, 0);
                printInvoice(invoiceId);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn một hóa đơn để in.", 
                    "Chưa Chọn", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Add selection listener for invoice table
        invoiceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow != -1) {
                    int invoiceId = (int) invoiceTable.getValueAt(selectedRow, 0);
                    loadPaymentDetails(invoiceId);
                }
            }
        });
    }
    
    private void loadInvoiceData() {
        // Lấy danh sách hóa đơn từ service
        List<HoaDon> invoices = hoaDonService.layDanhSach();
        
        updateInvoiceTable(invoices);
    }
    
    private void updateInvoiceTable(List<HoaDon> invoices) {
        // Tạo model cho bảng
        String[] columnNames = {"Mã HĐ", "Mã Đặt Tour", "Tổng Tiền", "Đã Thanh Toán", "Còn Lại", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Thêm dữ liệu vào model
        for (HoaDon invoice : invoices) {
            Object[] rowData = {
                invoice.getMaHoaDon(),
                invoice.getMaDatTour(),
                invoice.getTongTien() + " VNĐ",
                invoice.getDaThanhToan() + " VNĐ",
                invoice.getConLai() + " VNĐ",
                invoice.getTrangThai()
            };
            model.addRow(rowData);
        }
        
        invoiceTable.setModel(model);
    }
    
    private void loadPaymentDetails(int invoiceId) {
        // Lấy danh sách chi tiết thanh toán của hóa đơn
        List<ChiTietThanhToan> paymentDetails = chiTietThanhToanService.layDanhSachTheoHoaDon(invoiceId);
        
        // Tạo model cho bảng
        String[] columnNames = {"Mã TT", "Ngày Thanh Toán", "Số Tiền", "Phương Thức", "Ghi Chú"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        // Thêm dữ liệu vào model
        for (ChiTietThanhToan payment : paymentDetails) {
            Object[] rowData = {
                payment.getMaThanhToan(),
                dateFormat.format(payment.getNgayThanhToan()),
                payment.getSoTien() + " VNĐ",
                payment.getPhuongThucThanhToan(),
                payment.getGhiChu()
            };
            model.addRow(rowData);
        }
        
        paymentDetailTable.setModel(model);
    }
    
    private void searchInvoices() {
        String searchText = searchPanel.getSearchText().trim();
        
        if (searchText.isEmpty()) {
            loadInvoiceData(); // Nếu không có text tìm kiếm, hiển thị tất cả
            return;
        }
        
        // Tìm kiếm hóa đơn (giả sử tìm theo mã đặt tour)
        try {
            int bookingId = Integer.parseInt(searchText);
            HoaDon invoice = hoaDonService.timTheoDatTour(bookingId);
            
            List<HoaDon> searchResults = new ArrayList<>();
            if (invoice != null) {
                searchResults.add(invoice);
            }
            
            updateInvoiceTable(searchResults);
        } catch (NumberFormatException e) {
            // Nếu không phải số, hiển thị thông báo
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập mã đặt tour hợp lệ.",
                "Lỗi Tìm Kiếm",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void filterInvoicesByStatus() {
        String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
        
        if ("Tất cả".equals(selectedStatus)) {
            loadInvoiceData(); // Hiển thị tất cả
            return;
        }
        
        // Lấy danh sách hóa đơn theo trạng thái
        List<HoaDon> filteredInvoices = hoaDonService.layDanhSachTheoTrangThai(selectedStatus);
        updateInvoiceTable(filteredInvoices);
    }
    
   private void showAddPaymentDialog(int invoiceId) {
    // Kiểm tra mã nhân viên hiện tại
    int currentStaffId = SessionManager.getInstance().getCurrentStaffId();
    
    // Kiểm tra đăng nhập
    if (currentStaffId == 0) {
        JOptionPane.showMessageDialog(this, 
            "Vui lòng đăng nhập để thực hiện thao tác.", 
            "Chưa Đăng Nhập", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Kiểm tra hóa đơn có tồn tại không
    try {
        HoaDon hoaDon = hoaDonService.timTheoMa(invoiceId);
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy hóa đơn với mã: " + invoiceId, 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mở dialog thêm thanh toán
        AddPaymentDialog dialog = new AddPaymentDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            invoiceId,
            currentStaffId
        );
        dialog.setVisible(true);
        
        // Nếu thêm thanh toán thành công, refresh dữ liệu
        if (dialog.isSaveSuccess()) {
            loadInvoiceData();
            // Nếu có hóa đơn được chọn, load lại chi tiết thanh toán
            int selectedRow = invoiceTable.getSelectedRow();
            if (selectedRow != -1) {
                int selectedInvoiceId = (int) invoiceTable.getValueAt(selectedRow, 0);
                loadPaymentDetails(selectedInvoiceId);
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Lỗi: " + e.getMessage(), 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
}

private void printInvoice(int invoiceId) {
    try {
        // Kiểm tra hóa đơn có tồn tại không
        HoaDon hoaDon = hoaDonService.timTheoMa(invoiceId);
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy hóa đơn với mã: " + invoiceId, 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TODO: Triển khai chức năng in hóa đơn
        // Hiện tại chỉ hiển thị thông báo
        JOptionPane.showMessageDialog(this, 
            "Chức năng in hóa đơn đang được phát triển.\nHóa đơn #" + invoiceId, 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Lỗi: " + e.getMessage(), 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
}
}