package com.ramalho.library.database.model;

public enum EnumStatusRent {
    ACTIVE,
    RETURNED,
    OVERDUE;

    public String returnedStatus() {
        return "STATUS_" + this.name();
    }
}
