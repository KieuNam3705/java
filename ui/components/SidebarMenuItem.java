package com.tourmanagement.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarMenuItem extends JPanel {
    private String text;
    private Icon icon;
    private boolean selected;
    private Color defaultBgColor = new Color(30, 55, 153);
    private Color hoverBgColor = new Color(45, 70, 168);
    private Color selectedBgColor = new Color(60, 85, 183);
    
    public SidebarMenuItem(String text, Icon icon) {
        this.text = text;
        this.icon = icon;
        this.selected = false;
        setupMenuItem();
    }
    
    private void setupMenuItem() {
        setLayout(new BorderLayout());
        setBackground(defaultBgColor);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(Color.WHITE);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
        contentPanel.setBackground(defaultBgColor);
        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(textLabel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    setBackground(hoverBgColor);
                    contentPanel.setBackground(hoverBgColor);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    setBackground(defaultBgColor);
                    contentPanel.setBackground(defaultBgColor);
                }
            }
        });
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            setBackground(selectedBgColor);
            for (Component c : getComponents()) {
                c.setBackground(selectedBgColor);
            }
        } else {
            setBackground(defaultBgColor);
            for (Component c : getComponents()) {
                c.setBackground(defaultBgColor);
            }
        }
    }
    
    public boolean isSelected() {
        return selected;
    }
}