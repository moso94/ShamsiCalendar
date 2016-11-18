// Copyright 2012 Square, Inc.

package ir.mosobhani.shamsicalendar.model;

import ir.mosobhani.shamsicalendar.calendar.PersianCalendar;

public class MonthCellDescriptor {
    public PersianCalendar getDate() {
        return date;
    }

    public void setDate(PersianCalendar date) {
        this.date = new PersianCalendar();
        this.date.setPersianDate(date.getPersianYear(), date.getPersianMonth(), date.getPersianDay());
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    private PersianCalendar date;
    private int value;
    private boolean isCurrentMonth;
    private boolean isToday;
}
