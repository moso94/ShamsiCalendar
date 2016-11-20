package ir.mosobhani.shamsicalendar.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.mosobhani.shamsicalendar.calendar.PersianCalendar;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;


public class CalendarRowView extends ViewGroup implements View.OnClickListener {
    private boolean isHeaderRow;
    private CalendarView.OnCalenderClick onCalenderClick = null;

    public CalendarRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        child.setOnClickListener(this);
        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int rowHeight = 0;
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            int l = ((c + 0) * totalWidth) / 7;
            int r = ((c + 1) * totalWidth) / 7;
            int cellSize = r - l;
            int cellWidthSpec = makeMeasureSpec(cellSize, EXACTLY);
            int cellHeightSpec = isHeaderRow ? makeMeasureSpec(cellSize, AT_MOST) : cellWidthSpec;
            child.measure(cellWidthSpec, cellHeightSpec);
            if (child.getMeasuredHeight() > rowHeight) {
                rowHeight = child.getMeasuredHeight();
            }
        }
        final int widthWithPadding = totalWidth + getPaddingLeft() + getPaddingRight();
        final int heightWithPadding = rowHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthWithPadding, heightWithPadding);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        long start = System.currentTimeMillis();
        int cellHeight = bottom - top;
        int width = (right - left);
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            int l = ((c + 0) * width) / 7;
            int r = ((c + 1) * width) / 7;
            child.layout(l, 0, r, cellHeight);
        }

    }

    public void setIsHeaderRow(boolean isHeaderRow) {
        this.isHeaderRow = isHeaderRow;
    }

    @Override
    public void onClick(View v) {
        if (onCalenderClick != null) {
            onCalenderClick.handleClick((PersianCalendar) v.getTag());
        }
    }

    public void setOnCalenderClick(CalendarView.OnCalenderClick onCalenderClick) {
        this.onCalenderClick = onCalenderClick;
    }

    public void setCellBackground(int resId) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setBackgroundResource(resId);
        }
    }

    public void setCellTextColor(int resId) {
        for (int i = 0; i < getChildCount(); i++) {
            ((TextView) getChildAt(i)).setTextColor(resId);
        }
    }

    public void setCellTextColor(ColorStateList colors) {
        for (int i = 0; i < getChildCount(); i++) {
            ((TextView) getChildAt(i)).setTextColor(colors);
        }
    }

    public void setTypeface(Typeface typeface) {
        for (int i = 0; i < getChildCount(); i++) {
            ((TextView) getChildAt(i)).setTypeface(typeface);
        }
    }
}
