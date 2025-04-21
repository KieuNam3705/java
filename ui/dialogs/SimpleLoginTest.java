import javax.swing.*;
import java.awt.*;
import com.tourmanagement.ui.dialogs.LoginDialog;

/**
 * Class đơn giản để kiểm tra giao diện LoginDialog
 */
public class SimpleLoginTest {
    public static void main(String[] args) {
        // Thiết lập look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Chạy LoginDialog
        SwingUtilities.invokeLater(() -> {
            JFrame dummyFrame = new JFrame("Dummy Frame");
            dummyFrame.setSize(100, 100);
            dummyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummyFrame.setVisible(true);
            
            // Hiển thị dialog
            LoginDialog dialog = new LoginDialog(dummyFrame);
            dialog.setVisible(true);
            
            // Khi dialog đóng, thoát chương trình
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
        });
    }
}