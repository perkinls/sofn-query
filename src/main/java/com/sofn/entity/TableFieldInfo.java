package com.sofn.entity;

public class TableFieldInfo {
    private String tableFieldId;
    private String tableId;//外键
    private String tableFieldName;
    private String tableFieldDesc;

    public String getTableFieldId() {
        return tableFieldId;
    }

    public void setTableFieldId(String tableFieldId) {
        this.tableFieldId = tableFieldId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableFieldName() {
        return tableFieldName;
    }

    public void setTableFieldName(String tableFieldName) {
        this.tableFieldName = tableFieldName;
    }

    public String getTableFieldDesc() {
        return tableFieldDesc;
    }

    public void setTableFieldDesc(String tableFieldDesc) {
        this.tableFieldDesc = tableFieldDesc;
    }
}
