// Copyright 2013 Square, Inc.

package ir.mosobhani.shamsicalendar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.mosobhani.shamsicalendar.R;
import ir.mosobhani.shamsicalendar.model.MonthCellDescriptor;


public class CalendarCellView extends TextView {

    private static final int[] STATE_CURRENT_MONTH = {
            R.attr.state_current_month
    };
    private static final int[] STATE_TODAY = {
            R.attr.state_today
    };
    private static final int[] STATE_SELECTABLE = {
            R.attr.state_selectable
    };


    private boolean isCurrentMonth = false;
    private boolean isToday = false;
    public CalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //SetFont();

    }


    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
        refreshDrawableState();
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);
        //SetFont();

        mergeDrawableStates(drawableState, STATE_SELECTABLE);

        if (isCurrentMonth) {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }

        if (isToday) {
            mergeDrawableStates(drawableState, STATE_TODAY);
        }
        return drawableState;
    }
}
