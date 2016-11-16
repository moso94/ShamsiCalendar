// Copyright 2012 Square, Inc.

package ir.mosobhani.calendar;

public class MonthCellDescriptor {
    private final PersianCalendar date;
    private final int value;
    private final boolean isCurrentMonth;
    private final boolean isToday;
    private final boolean isSelectable;
    private boolean isSelected;
    private boolean isHighlighted;
    private RangeState rangeState;

    public MonthCellDescriptor(PersianCalendar date, boolean currentMonth, boolean selectable, boolean selected,
                               boolean today, boolean highlighted, int value, RangeState rangeState) {
        this.date = new PersianCalendar();
        this.date.setPersianDate(date.getPersianYear(), date.getPersianMonth(), date.getPersianDay());
        isCurrentMonth = currentMonth;
        isSelectable = selectable;
        isHighlighted = highlighted;
        isSelected = selected;
        isToday = today;
        this.value = value;
        this.rangeState = rangeState;
    }

    public PersianCalendar getDate() {
        PersianCalendar tempCal = new PersianCalendar();
        tempCal.setPersianDate(date.getPersianYear(), date.getPersianMonth(), date.getPersianDay());
        return tempCal;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isHighlighted() {
        return isHighlighted;
    }

    void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isToday() {
        return isToday;
    }

    public RangeState getRangeState() {
        return rangeState;
    }

    public void setRangeState(RangeState rangeState) {
        this.rangeState = rangeState;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MonthCellDescriptor{"
                + "date="
                + date
                + ", value="
                + value
                + ", isCurrentMonth="
                + isCurrentMonth
                + ", isSelected="
                + isSelected
                + ", isToday="
                + isToday
                + ", isSelectable="
                + isSelectable
                + ", isHighlighted="
                + isHighlighted
                + ", rangeState="
                + rangeState
                + '}';
    }

    public enum RangeState {
        NONE, FIRST, MIDDLE, LAST
    }
}
