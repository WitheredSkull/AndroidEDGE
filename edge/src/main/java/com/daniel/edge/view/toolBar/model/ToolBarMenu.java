package com.daniel.edge.view.toolBar.model;

public enum ToolBarMenu {
    Back("LEFT"), Title("CENTER"), More("RIGHT");
    private String details;

    ToolBarMenu(String details) {
        this.details = details;
    }
}
