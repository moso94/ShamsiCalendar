// Copyright 2012 Square, Inc.
package ir.mosobhani.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MonthView extends LinearLayout {

    public static String[] WEEK_NAME = new String[]{"ش", "ی", "د", "س", "چ", "پ", "ج"};
    public LinearLayout rightVector;
    public LinearLayout leftVector;
    TextView title;
    CalendarGridView grid;
    private Listener listener;
    private boolean isRtl;

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static MonthView create(ViewGroup parent, Context context, LayoutInflater inflater, Listener listener, Calendar toy, Locale locale) {

        Resources res = context.getResources();
        int dividerColor = res.getColor(R.color.transparent);
        int dayBackgroundResId = R.drawable.custom_calendar_bg_selector;
        int dayTextColorResId = R.drawable.custom_calendar_text_selector;
        int titleTextColor = R.drawable.custom_calendar_text_selector;
        boolean displayHeader = true;
        int headerTextColor = res.getColor(R.color.custom_header_text);


        final MonthView view = (MonthView) inflater.inflate(R.layout.month, parent, false);
        view.setDividerColor(dividerColor);
        view.setDayTextColor(dayTextColorResId);
        view.setTitleTextColor(titleTextColor);
        view.setDisplayHeader(displayHeader);
        view.setHeaderTextColor(headerTextColor);

        Calendar today = Calendar.getInstance();
        today.setTime(toy.getTime());

        if (dayBackgroundResId != 0) {
            view.setDayBackground(dayBackgroundResId);
        }

        final int originalDayOfWeek = Calendar.WEDNESDAY;//today.get(Calendar.DAY_OF_WEEK);
        view.isRtl = true;//isRtl(locale);
        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) view.grid.getChildAt(0);
        for (int offset = 0; offset < 7; offset++) {
            today.set(Calendar.DAY_OF_WEEK, getDayOfWeek(firstDayOfWeek, offset, view.isRtl));
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            //TODO: translate to persian
            String ss = WEEK_NAME[6 - offset];//weekdayNameFormat.format(today.getTime());
            textView.setText(ss);
        }
        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);
        view.listener = listener;
        return view;
    }

    private static int getDayOfWeek(int firstDayOfWeek, int offset, boolean isRtl) {
        int dayOfWeek = firstDayOfWeek + offset;
        if (isRtl) {
            return 8 - dayOfWeek;
        }
        return dayOfWeek;
    }

    private static boolean isRtl(Locale locale) {
        // TODO convert the build to gradle and use getLayoutDirection instead of this (on 17+)?
        final int directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
                || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
        rightVector = (LinearLayout) findViewById(R.id.right);
        leftVector = (LinearLayout) findViewById(R.id.left);
    }

    public void init(MonthDescriptor month, List<List<MonthCellDescriptor>> cells) {

        long start = System.currentTimeMillis();
        title.setText(month.getLabel());

        final int numRows = cells.size();
        //String englishNumber = String.valueOf(numRows);
        //String PersianNumber = formatHelper.toPersianNumber(englishNumber);
        ///Integer.parseInt(PersianNumber);

        grid.setNumRows(numRows);
        for (int i = 0; i < 6; i++) {
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i + 1);
            weekRow.setListener(listener);
            if (i < numRows) {
                weekRow.setVisibility(VISIBLE);
                List<MonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = week.get(isRtl ? 6 - c : c);
                    CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(c);

                    if (cell.getValue() == 0) {
                        cellView.setText("");
                        cellView.setClickable(false);
                    } else {
                        String englishNumber = String.valueOf(cell.getValue());
                        String cellDate = englishNumber;//formatHelper.toPersianNumber(englishNumber);
                        if (!cellView.getText().equals(cellDate)) {
                            cellView.setText(cellDate);
                        }
                        cellView.setClickable(true);
                    }
                    cellView.setEnabled(cell.isCurrentMonth());


                    cellView.setSelectable(cell.isSelectable());
                    cellView.setSelected(cell.isSelected());
                    cellView.setCurrentMonth(cell.isCurrentMonth());
                    cellView.setToday(cell.isToday());
                    cellView.setRangeState(cell.getRangeState());
                    cellView.setHighlighted(cell.isHighlighted());
                    cellView.setTag(cell);
                }
            } else {
                weekRow.setVisibility(GONE);
            }
        }
    }

    public void setDividerColor(int color) {
        grid.setDividerColor(color);
    }

    public void setDayBackground(int resId) {
        grid.setDayBackground(resId);
    }

    public void setDayTextColor(int resId) {
        grid.setDayTextColor(resId);
    }

    public void setTitleTextColor(int color) {
        title.setTextColor(color);
    }

    public void setDisplayHeader(boolean displayHeader) {
        grid.setDisplayHeader(displayHeader);
    }

    public void setHeaderTextColor(int color) {
        grid.setHeaderTextColor(color);
    }

    public interface Listener {
        void handleClick(MonthCellDescriptor cell);
    }


}
