package com.shuanglu.edge.View.ToolBar.Model;

public enum ToolBarMenu {
    Back("LEFT"), Title("CENTER"), More("RIGHT");
    private String details;

    ToolBarMenu(String details) {
        this.details = details;
    }
}
