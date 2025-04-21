package com.tourmanagement.ui.components;

import javax.swing.*;
import java.awt.*;

public class StatsCard extends JPanel {
    private String title;
    private String value;
    private String subtitle;
    private Color backgroundColor;
    
    public StatsCard(String title, String value, String subtitle, Color backgroundColor) {
        this.title = title;
        this.value = value;
        this.subtitle = subtitle;
        this.backgroundColor = backgroundColor;
        setupCard();
    }
    
    private void setupCard() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(backgroundColor);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(valueLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(subtitleLabel);
        
        add(infoPanel, BorderLayout.CENTER);
    }
}