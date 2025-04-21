package com.tourmanagement.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {
    private CustomTextField searchField;
    private CustomButton searchButton;
    
    public SearchPanel() {
        setupPanel();
    }
    
    private void setupPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        searchField = new CustomTextField(20);
        searchButton = new CustomButton("Search");
        searchButton.setStylePrimary();
        
        add(new JLabel("Search: "));
        add(searchField);
        add(searchButton);
    }
    
    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
        
        // Also add listener to handle Enter key in text field
        searchField.addActionListener(listener);
    }
    
    public String getSearchText() {
        return searchField.getText();
    }
    
    public void setSearchText(String text) {
        searchField.setText(text);
    }
}