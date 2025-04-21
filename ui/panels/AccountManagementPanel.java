    package com.tourmanagement.ui.panels;

    import com.tourmanagement.ui.components.*;
    import com.tourmanagement.service.ServiceFactory;
    import com.tourmanagement.service.interfaces.ITaiKhoanService;
    import com.tourmanagement.service.interfaces.IKhachHangService;
    import com.tourmanagement.service.interfaces.INhanVienService;
    import com.tourmanagement.entity.TaiKhoan;
    import com.tourmanagement.entity.KhachHang;
    import com.tourmanagement.entity.NhanVien;
    import com.tourmanagement.exception.BusinessLogicException;
    // Import Controller mới
    import com.tourmanagement.ui.controller.AddEditAccountController;
    // Import Dialog (không chứa logic nữa)
    import com.tourmanagement.ui.dialogs.AddEditAccountDialog; // Cần import để kiểm tra isSaveSuccess() hoặc thông qua controller

    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import java.awt.*;
    import java.util.ArrayList;
    import java.util.List;

    public class AccountManagementPanel extends JPanel {
        private ServiceFactory serviceFactory;
        private ITaiKhoanService taiKhoanService;
        private IKhachHangService khachHangService;
        private INhanVienService nhanVienService;

        private CustomTable accountTable;
        private SearchPanel searchPanel;
        private CustomButton addButton;
        private CustomButton editButton;
        // private CustomButton resetPasswordButton; // Đã bỏ
        private CustomButton deleteButton; // Thêm nút xóa
        private CustomButton toggleLockButton; // Đổi tên nút Khóa/Mở cho rõ ràng

        private JComboBox<String> accountTypeComboBox;

        public AccountManagementPanel() {
            serviceFactory = ServiceFactory.getInstance();
            taiKhoanService = serviceFactory.getTaiKhoanService();
            khachHangService = serviceFactory.getKhachHangService();
            nhanVienService = serviceFactory.getNhanVienService();

            setupPanel();
            loadAccountData();
        }

        private void setupPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(245, 246, 250));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Header with title and buttons
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(245, 246, 250));

            CustomLabel titleLabel = new CustomLabel("Quản Lý Tài Khoản");
            titleLabel.setHeaderStyle();

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(245, 246, 250));

            addButton = new CustomButton("Thêm Tài Khoản");
            addButton.setStylePrimary();

            editButton = new CustomButton("Sửa Tài Khoản");
            editButton.setStyleSecondary();

            // resetPasswordButton = new CustomButton("Đặt Lại Mật Khẩu"); // Đã bỏ
            // resetPasswordButton.setStyleSecondary(); // Đã bỏ

            deleteButton = new CustomButton("Xóa Tài Khoản"); // Thêm nút xóa
            deleteButton.setStyleDanger();

            toggleLockButton = new CustomButton("Khóa/Mở Tài Khoản"); // Đổi tên nút Khóa/Mở
            toggleLockButton.setStyleSecondary(); // Có thể chọn style khác

            buttonPanel.add(addButton);
            buttonPanel.add(editButton);
            // buttonPanel.add(resetPasswordButton); // Đã bỏ
            buttonPanel.add(deleteButton); // Thêm nút xóa
            buttonPanel.add(toggleLockButton); // Nút khóa/mở

            headerPanel.add(titleLabel, BorderLayout.WEST);
            headerPanel.add(buttonPanel, BorderLayout.EAST);

            // Search and filter panel
            JPanel searchFilterPanel = new JPanel(new BorderLayout());
            searchFilterPanel.setBackground(new Color(245, 246, 250));

            searchPanel = new SearchPanel();

            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            filterPanel.setBackground(new Color(245, 246, 250));

            filterPanel.add(new JLabel("Loại tài khoản:"));

            String[] accountTypes = {"Tất cả", "Admin", "Nhân viên", "Khách hàng"};
            accountTypeComboBox = new JComboBox<>(accountTypes);
            accountTypeComboBox.addActionListener(e -> filterAccountsByType());

            filterPanel.add(accountTypeComboBox);

            searchFilterPanel.add(searchPanel, BorderLayout.WEST);
            searchFilterPanel.add(filterPanel, BorderLayout.EAST);

            // Table panel
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));

            accountTable = new CustomTable();
            JScrollPane scrollPane = new JScrollPane(accountTable);

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
            searchPanel.addSearchListener(e -> searchAccounts());

            // Add action listeners for buttons
            addButton.addActionListener(e -> showAddAccountDialog());

            editButton.addActionListener(e -> {
                int selectedRow = accountTable.getSelectedRow();
                if (selectedRow != -1) {
                    int accountId = (int) accountTable.getValueAt(selectedRow, 0);
                    showEditAccountDialog(accountId);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một tài khoản để sửa.",
                        "Chưa Chọn",
                        JOptionPane.WARNING_MESSAGE);
                }
            });

            // resetPasswordButton.addActionListener(e -> { /* Logic removed */ }); // Đã bỏ

            deleteButton.addActionListener(e -> { // Listener cho nút xóa
                int selectedRow = accountTable.getSelectedRow();
                if (selectedRow != -1) {
                    int accountId = (int) accountTable.getValueAt(selectedRow, 0);
                    deleteAccount(accountId);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một tài khoản để xóa.",
                        "Chưa Chọn",
                        JOptionPane.WARNING_MESSAGE);
                }
            });

            toggleLockButton.addActionListener(e -> { // Listener cho nút khóa/mở
                int selectedRow = accountTable.getSelectedRow();
                if (selectedRow != -1) {
                    int accountId = (int) accountTable.getValueAt(selectedRow, 0);
                    String currentStatus = (String) accountTable.getValueAt(selectedRow, 5);
                    toggleAccountLock(accountId, currentStatus);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một tài khoản để khóa/mở khóa.",
                        "Chưa Chọn",
                        JOptionPane.WARNING_MESSAGE);
                }
            });
        }

        private void loadAccountData() {
            // Lấy danh sách tài khoản từ service
            List<TaiKhoan> accounts = taiKhoanService.layDanhSach();

            updateAccountTable(accounts);
        }

        private void updateAccountTable(List<TaiKhoan> accounts) {
            // Tạo model cho bảng
            String[] columnNames = {"Mã TK", "Tên Đăng Nhập", "Loại Tài Khoản", "Tên Người Dùng", "Ngày Tạo", "Trạng Thái"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Không cho phép sửa trực tiếp trên bảng
                }
            };

            // Thêm dữ liệu vào model
            for (TaiKhoan account : accounts) {
                String userName = getUserName(account);

                Object[] rowData = {
                    account.getMaTaiKhoan(),
                    account.getTenDangNhap(),
                    account.getLoaiTaiKhoan(),
                    userName,
                    account.getNgayTao(), // Sử dụng chuỗi NgayTao từ entity
                    account.getTrangThai()
                };
                model.addRow(rowData);
            }

            accountTable.setModel(model);
        }

        private String getUserName(TaiKhoan account) {
            // Lấy tên người dùng dựa vào loại tài khoản
            String userName = ""; // Mặc định là rỗng

            if (account.getMaKH() != null && account.getMaKH() > 0) {
                // Nếu có liên kết với khách hàng
                KhachHang khachHang = khachHangService.timTheoMa(account.getMaKH());
                if (khachHang != null) {
                    userName = khachHang.getHoTen();
                }
            } else if (account.getMaNV() != null && account.getMaNV() > 0) {
                // Nếu có liên kết với nhân viên
                NhanVien nhanVien = nhanVienService.timTheoMa(account.getMaNV());
                if (nhanVien != null) {
                    userName = nhanVien.getHoTen();
                }
            }
            // Nếu không có MaKH hoặc MaNV, userName vẫn là rỗng

            return userName;
        }

        private void searchAccounts() {
            String searchText = searchPanel.getSearchText().trim();

            if (searchText.isEmpty()) {
                loadAccountData(); // Nếu không có text tìm kiếm, hiển thị tất cả
                return;
            }

            // Tìm kiếm tài khoản theo tên đăng nhập
            TaiKhoan account = taiKhoanService.timTheoTenDangNhap(searchText);
            List<TaiKhoan> searchResults = new ArrayList<>();
            if (account != null) {
                searchResults.add(account);
            }

            updateAccountTable(searchResults);
        }

        private void filterAccountsByType() {
            String selectedType = (String) accountTypeComboBox.getSelectedItem();

            if ("Tất cả".equals(selectedType)) {
                loadAccountData(); // Hiển thị tất cả
                return;
            }

            // Lấy danh sách tài khoản từ service
            List<TaiKhoan> allAccounts = taiKhoanService.layDanhSach();
            List<TaiKhoan> filteredAccounts = new ArrayList<>();

            // Lọc theo loại tài khoản
            for (TaiKhoan account : allAccounts) {
                if (selectedType.equalsIgnoreCase(account.getLoaiTaiKhoan())) {
                    filteredAccounts.add(account);
                }
            }

            updateAccountTable(filteredAccounts);
        }

        private void showAddAccountDialog() {
            // Sử dụng Controller để hiển thị và quản lý dialog
            AddEditAccountController controller = new AddEditAccountController(
                (Frame) SwingUtilities.getWindowAncestor(this), // Parent Frame
                null // TaiKhoan object is null for add mode
            );
            boolean saveSuccess = controller.showDialog(); // showDialog() sẽ trả về true nếu lưu thành công

            if (saveSuccess) {
                loadAccountData(); // Tải lại dữ liệu nếu có thay đổi
            }
        }

        private void showEditAccountDialog(int accountId) {
            TaiKhoan selectedAccount = taiKhoanService.timTheoMa(accountId);

            if (selectedAccount != null) {
                // Sử dụng Controller để hiển thị và quản lý dialog
                AddEditAccountController controller = new AddEditAccountController(
                    (Frame) SwingUtilities.getWindowAncestor(this), // Parent Frame
                    selectedAccount // TaiKhoan object for edit mode
                );
                boolean saveSuccess = controller.showDialog(); // showDialog() sẽ trả về true nếu lưu thành công

                if (saveSuccess) {
                    loadAccountData(); // Tải lại dữ liệu nếu có thay đổi
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy tài khoản để sửa.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void deleteAccount(int accountId) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa tài khoản này?",
                "Xác Nhận Xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = taiKhoanService.xoa(accountId);
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                            "Xóa tài khoản thành công.",
                            "Thành Công",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadAccountData(); // Tải lại dữ liệu
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Không thể xóa tài khoản.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (BusinessLogicException e) {
                    JOptionPane.showMessageDialog(this,
                        "Lỗi: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                        "Đã xảy ra lỗi khi xóa tài khoản: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void toggleAccountLock(int accountId, String currentStatus) {
            boolean isCurrentlyActive = "Hoạt động".equals(currentStatus);
            String newStatus = isCurrentlyActive ? "Khóa" : "Hoạt động";
            String actionName = isCurrentlyActive ? "khóa" : "mở khóa";

            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn " + actionName + " tài khoản này?",
                "Xác Nhận",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = taiKhoanService.capNhatTrangThai(accountId, !isCurrentlyActive);
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                            "Đã " + actionName + " tài khoản thành công.",
                            "Thành Công",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadAccountData(); // Tải lại dữ liệu
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Không thể " + actionName + " tài khoản.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (BusinessLogicException e) {
                    JOptionPane.showMessageDialog(this,
                        "Lỗi: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                        "Đã xảy ra lỗi khi " + actionName + " tài khoản: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }