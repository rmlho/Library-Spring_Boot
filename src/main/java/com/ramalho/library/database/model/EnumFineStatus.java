package com.ramalho.library.database.model;

public enum EnumFineStatus {
    LATE,
    PAID;

    public String returnedStatus() {
        return "STATUS_" + this.name();
    }
}
