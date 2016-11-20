package ir.mosobhani.shamsicalendar.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import ir.mosobhani.shamsicalendar.R;
import ir.mosobhani.shamsicalendar.calendar.PersianCalendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;


public class CalendarView extends LinearLayout {
    public static String[] WEEK_NAME = new String[]{"ش", "ی", "د", "س", "چ", "پ", "ج"};
    public LinearLayout rightVector;
    public LinearLayout leftVector;
    TextView title;
    CalendarGridView grid;


    private OnCalenderClick onCalenderClick = null;

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
                getMonthCells();
            }
        });

        leftVector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                jalaliCal.addPersianDate(MONTH, -1);
                getMonthCells();
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
            String ss = WEEK_NAME[6 - offset];
            textView.setText(ss);
        }

        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);

        getMonthCells();
    }

    void getMonthCells() {
        PersianCalendar cal = new PersianCalendar(jalaliCal);
        title.setText(cal.getPersianMonthName());
        int currentMonth = cal.getPersianMonth();
        int nextMonth = currentMonth + 1;
        if (nextMonth > 12)
            nextMonth -= 12;
        cal.SetDayOfMonth(1);
        cal.addPersianDate(DATE, -cal.getDayOfWeek());
        int numRows = 0;
        for (int weekCount = 0; weekCount < 6; weekCount++) {
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(weekCount + 1);
            if (onCalenderClick != null)
                weekRow.setOnCalenderClick(onCalenderClick);
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
                        cellView.setTag(new PersianCalendar(cal));
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

    public void setOnCalenderClick(OnCalenderClick onCalenderClick) {
        this.onCalenderClick = onCalenderClick;
        getMonthCells();
    }

    public interface OnCalenderClick {
        void handleClick(PersianCalendar clickedDay);
    }
}
