// Copyright 2012 Square, Inc.
package ir.mosobhani.shamsicalendar.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ir.mosobhani.shamsicalendar.R;
import ir.mosobhani.shamsicalendar.calendar.PersianCalendar;
import ir.mosobhani.shamsicalendar.model.MonthCellDescriptor;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class CalendarView extends LinearLayout {
    public static String[] WEEK_NAME = new String[]{"ش", "ی", "د", "س", "چ", "پ", "ج"};
    public LinearLayout rightVector;
    public LinearLayout leftVector;
    TextView title;
    CalendarGridView grid;
    private Listener listener;

    PersianCalendar toy;
    private PersianCalendar jalaliCal;
    private Context context;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        jalaliCal = new PersianCalendar();
        toy = new PersianCalendar();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.month, null, false);
        addView(view);
        title = (TextView) findViewById(R.id.title);
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
        rightVector = (LinearLayout) findViewById(R.id.right);
        leftVector = (LinearLayout) findViewById(R.id.left);

        rightVector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                jalaliCal.addPersianDate(MONTH, +1);
                getMonthCells(jalaliCal);
            }
        });

        leftVector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                jalaliCal.addPersianDate(MONTH, -1);
                getMonthCells(jalaliCal);
            }
        });

        grid.setDividerColor(ContextCompat.getColor(context, R.color.divider));
        grid.setDayTextColor(R.drawable.custom_calendar_text_selector);
        grid.setHeaderTextColor(ContextCompat.getColor(context, R.color.header_text));
        grid.setDayBackground(R.drawable.custom_calendar_bg_selector);
        grid.setDisplayHeader(true);
        title.setTextColor(ContextCompat.getColor(context, R.color.monthColor));

        Calendar today = Calendar.getInstance();
        today.setTime(toy.getTime());

        final int originalDayOfWeek = Calendar.WEDNESDAY;
        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) grid.getChildAt(0);
        for (
                int offset = 0;
                offset < 7; offset++)

        {
            today.set(Calendar.DAY_OF_WEEK, getDayOfWeek(firstDayOfWeek, offset));
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            //TODO: translate to persian
            String ss = WEEK_NAME[6 - offset];//weekdayNameFormat.format(today.getTime());
            textView.setText(ss);
        }

        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);

        getMonthCells(jalaliCal);
    }

    void getMonthCells(PersianCalendar calendar) {
        PersianCalendar cal = new PersianCalendar(calendar);

//        ArrayList<List<MonthCellDescriptor>> cells = new ArrayList<>();
        title.setText(cal.getPersianMonthName());
        int currentMonth = cal.getPersianMonth();
        int nextMonth = currentMonth + 1;
        if (nextMonth > 12)
            nextMonth -= 12;
        cal.SetDayOfMonth(1);
        cal.addPersianDate(DATE, -cal.getDayOfWeek());
//
//        while (cal.getPersianMonth() != nextMonth) {
//            List<MonthCellDescriptor> weekCells = new ArrayList<>();
//            cells.add(weekCells);
//            for (int c = 0; c < 7; c++) {
//                boolean isCurrentMonth = cal.getPersianMonth() == currentMonth;
//                boolean isToday = sameDate(cal, toy);
//                int value = 0;
//                if (isCurrentMonth)
//                    value = cal.getPersianDay();
//
//
//                MonthCellDescriptor tmpCell = new MonthCellDescriptor();
//                tmpCell.setDate(cal);
//                tmpCell.setCurrentMonth(isCurrentMonth);
//                tmpCell.setToday(isToday);
//                tmpCell.setValue(value);
//                weekCells.add(tmpCell);
//                cal.addPersianDate(DATE, 1);
//            }
//        }
        int numRows = 0;
        for (int weekCount = 0; weekCount < 6; weekCount++) {
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(weekCount + 1);
            weekRow.setListener(listener);
            if (cal.getPersianMonth() != nextMonth) {
                for (int dayCount = 0; dayCount < 7; dayCount++) {
                    CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(6 - dayCount);
                    if (cal.getPersianMonth() == currentMonth) {
                        String cellDate = cal.getPersianDayStr();
                        if (!cellView.getText().equals(cellDate)) {
                            cellView.setText(cellDate);
                        }
                        cellView.setClickable(true);
                        cellView.setEnabled(true);
                        cellView.setCurrentMonth(true);
                        cellView.setToday(sameDate(cal, toy));
                    } else {
                        cellView.setText("");
                        cellView.setClickable(false);
                    }
                    cal.addPersianDate(DATE, 1);
                }
                numRows++;
            } else {
                weekRow.setVisibility(GONE);
            }
        }

        grid.setNumRows(numRows);
//        for (int i = 0; i < 6; i++) {
//            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i + 1);
//            weekRow.setListener(listener);
//            if (i < numRows) {
//                weekRow.setVisibility(VISIBLE);
//                List<MonthCellDescriptor> week = cells.get(i);
//                for (int c = 0; c < week.size(); c++) {
//                    MonthCellDescriptor cell = week.get(6 - c);
//                    CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(c);
//
//                    if (cell.getValue() == 0) {
//                        cellView.setText("");
//                        cellView.setClickable(false);
//                    } else {
//                        String cellDate = toPersianNumber(String.valueOf(cell.getValue()));
//                        if (!cellView.getText().equals(cellDate)) {
//                            cellView.setText(cellDate);
//                        }
//                        cellView.setClickable(true);
//                    }
//                    cellView.setEnabled(cell.isCurrentMonth());
//
//                    cellView.setCurrentMonth(cell.isCurrentMonth());
//                    cellView.setToday(cell.isToday());
//                    cellView.setTag(cell);
//                }
//            } else {
//                weekRow.setVisibility(GONE);
//            }
//        }
    }

    private static PersianCalendar minDate(List<PersianCalendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(0);
    }

    private static PersianCalendar maxDate(List<PersianCalendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(selectedCals.size() - 1);
    }

    private static boolean containsDate(List<PersianCalendar> selectedCals, PersianCalendar cal) {
        for (PersianCalendar selectedCal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                return true;
            }
        }
        return false;
    }

    private static boolean betweenDates(PersianCalendar cal, PersianCalendar minCal, PersianCalendar maxCal) {
        boolean minimumIf = false;
        boolean maximumIf = false;
        if (minCal != null)
            if (cal.getPersianYear() > minCal.getPersianYear()) {
                minimumIf = true;
            } else if (cal.getPersianYear() == minCal.getPersianYear()) {
                if (cal.getPersianMonth() > minCal.getPersianMonth()) {
                    minimumIf = true;
                } else if (cal.getPersianMonth() == minCal.getPersianMonth())
                    if (cal.getPersianDay() >= minCal.getPersianDay())
                        minimumIf = true;
            }
        if (maxCal != null)
            if (cal.getPersianYear() < maxCal.getPersianYear()) {
                maximumIf = true;
            } else if (cal.getPersianYear() == maxCal.getPersianYear()) {
                if (cal.getPersianMonth() < maxCal.getPersianMonth()) {
                    maximumIf = true;
                } else if (cal.getPersianMonth() == maxCal.getPersianMonth())
                    if (cal.getPersianDay() <= maxCal.getPersianDay())
                        maximumIf = true;
            }

        return minimumIf && maximumIf;
    }

    private static boolean sameDate(PersianCalendar cal, PersianCalendar selectedDate) {
        return cal.getPersianYear() == selectedDate.getPersianYear()
                && cal.getPersianMonth() == selectedDate.getPersianMonth()
                && cal.getPersianDay() == selectedDate.getPersianDay();
    }


    private static int getDayOfWeek(int firstDayOfWeek, int offset) {
        int dayOfWeek = firstDayOfWeek + offset;
        return 8 - dayOfWeek;
    }


    public interface Listener {
        void handleClick(MonthCellDescriptor cell);
    }
}
