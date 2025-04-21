package com.tourmanagement.ui.components;

import javax.swing.*;
import java.awt.*;

public class StatusBadge extends JLabel {
    
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_ACTIVE = 3;
    public static final int STATUS_COMPLETED = 4;
    
    public StatusBadge(String text, int status) {
        super(text);
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        setFont(new Font("Arial", Font.BOLD, 11));
        
        switch (status) {
            case STATUS_PENDING:
                setBackground(new Color(120, 93, 255)); // Tím
                setForeground(Color.WHITE);
                break;
            case STATUS_APPROVED:
                setBackground(new Color(92, 184, 92)); // Xanh lá
                setForeground(Color.WHITE);
                break;
            case STATUS_CANCELED:
                setBackground(new Color(217, 83, 79)); // Đỏ
                setForeground(Color.WHITE);
                break;
            case STATUS_ACTIVE:
                setBackground(new Color(66, 139, 202)); // Xanh dương
                setForeground(Color.WHITE);
                break;
            case STATUS_COMPLETED:
                setBackground(new Color(91, 192, 222)); // Xanh nhạt
                setForeground(Color.WHITE);
                break;
            default:
                setBackground(Color.LIGHT_GRAY);
                setForeground(Color.BLACK);
        }
    }
}