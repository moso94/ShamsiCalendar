package ir.mosobhani.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class Planner_Calendar_Dialog extends DialogFragment {

    final List<MonthCellDescriptor> selectedCells = new ArrayList<MonthCellDescriptor>();
    final List<PersianCalendar> selectedCals = new ArrayList<PersianCalendar>();
    final List<PersianCalendar> highlightedCals = new ArrayList<PersianCalendar>();
    final MonthView.Listener listener = new CellClickedListener();
    private Context context;
    private Button button;
    private RelativeLayout relativeLayout_1;
    private boolean resultok = false;
    private PersianCalendar minCal;
    private PersianCalendar maxCal;
    private PersianCalendar jalaliCal;
    private PersianCalendar today;
    private MonthView monthView = null;
    private MonthDescriptor month;
    private List<List<MonthCellDescriptor>> cells = new ArrayList<>();

    //--------------------------------------------Fragment Methods-------------------------------------------------
    public Planner_Calendar_Dialog() {
    }

    public static Planner_Calendar_Dialog newInstance(String title) {
        Planner_Calendar_Dialog frag = new Planner_Calendar_Dialog();
        //Bundle args = new Bundle();
        //args.putString("title", title);
        //frag.setArguments(args);
        return frag;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.BOTTOM);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //Log.i("dddd", getArguments().getString("selected"));

        return inflater.inflate(R.layout.fragment_planner__calendar, container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        context = getContext();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!resultok) {
            Intent i = new Intent().putExtra("selected", "");
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, i);
            dismiss();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        relativeLayout_1 = (RelativeLayout) view.findViewById(R.id.rl_cal_grid);
        button = (Button) view.findViewById(R.id.button_cal_done);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                PersianCalendar start = minDate(selectedCals);
                PersianCalendar end = maxDate(selectedCals);
                try {
                    i.putExtra("start", start.toJSON());
                    i.putExtra("end", end.toJSON());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                resultok = true;
                dismiss();
            }
        });


        today = new PersianCalendar();
        ViewGroup parent = (ViewGroup) view.findViewById(android.R.id.content);
        LayoutInflater inflater = LayoutInflater.from(context);
        Locale locale = Locale.getDefault();
        jalaliCal = new PersianCalendar();
        monthView = MonthView.create(parent, context, inflater, listener, today, locale);
        month = new MonthDescriptor(today.get(MONTH), today.get(YEAR), jalaliCal.getTime(), jalaliCal.getPersianMonthName());
        Bundle arg = getArguments();
        if (arg != null) {
            try {
                PersianCalendar tmpStartCal = new PersianCalendar(getArguments().getString("start"));
                PersianCalendar tmpEndCal = new PersianCalendar(getArguments().getString("end"));
                if (!containsDate(selectedCals, tmpStartCal))
                    selectedCals.add(tmpStartCal);
                if (!containsDate(selectedCals, tmpEndCal))
                    selectedCals.add(tmpEndCal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getMonthCells(jalaliCal);
        relativeLayout_1.addView(monthView);

//        ////////////////////////////////////////////////// when click on > ///////////////////////////////////////////////

        monthView.rightVector.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jalaliCal.addPersianDate(MONTH, +1);
                Date date = jalaliCal.getTime();
                //final JalaliCalendar jalaliCalandar = new JalaliCalendar(today);
                month = new MonthDescriptor(date.getMonth(), date.getYear(), today.getTime(), jalaliCal.getPersianMonthName());
                getMonthCells(jalaliCal);

            }
        });

//////////////////////////////////////////////////////  when click on <  /////////////////////////////////////////////////////
        monthView.leftVector.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jalaliCal.addPersianDate(MONTH, -1);
                Date date = jalaliCal.getTime();
                //final JalaliCalendar jalaliCalandar = new JalaliCalendar(today);
                month = new MonthDescriptor(date.getMonth(), date.getYear(), today.getTime(), jalaliCal.getPersianMonthName());
                getMonthCells(jalaliCal);
            }
        });
    }

    //--------------------------------------------Calendar-------------------------------------------------
    void getMonthCells(PersianCalendar cal) {
        PersianCalendar jalaliCal = new PersianCalendar();
        jalaliCal.setPersianDate(cal.getPersianYear(), cal.getPersianMonth(), cal.getPersianDay());
        maxCal = new PersianCalendar();
        minCal = new PersianCalendar();
        maxCal.addPersianDate(YEAR, +1);

        cells = new ArrayList<List<MonthCellDescriptor>>();
        //setMidnight(jalaliCal);
        //setMidnight(maxCal);
        //setMidnight(minCal);
        int currentMonth = jalaliCal.getPersianMonth();
        int nextMonth = currentMonth + 1;
        if (nextMonth > 12)
            nextMonth -= 12;
        //jalaliCal.setPersianDate(1394,9,1);
        //jalaliCal.set(2015,Calendar.DECEMBER,22);
        jalaliCal.SetDayOfMonth(1);
        jalaliCal.addPersianDate(DATE, -jalaliCal.getDayOfWeek());

        PersianCalendar minSelectedCal = minDate(selectedCals);
        PersianCalendar maxSelectedCal = maxDate(selectedCals);
        while (jalaliCal.getPersianMonth() != nextMonth) {
            List<MonthCellDescriptor> weekCells = new ArrayList<MonthCellDescriptor>();
            cells.add(weekCells);
            for (int c = 0; c < 7; c++) {
                boolean isCurrentMonth = jalaliCal.getPersianMonth() == currentMonth;
                boolean isSelected = isCurrentMonth && selectedCals.size() > 0 && (containsDate(selectedCals, jalaliCal) || betweenDates(jalaliCal, minSelectedCal, maxSelectedCal));
                boolean isSelectable = isCurrentMonth && betweenDates(jalaliCal, minCal, maxCal);
                boolean isToday = sameDate(jalaliCal, today);
                boolean isHighlighted = containsDate(highlightedCals, jalaliCal);
                int value = 0;
                if (isCurrentMonth)
                    value = jalaliCal.getPersianDay();

                MonthCellDescriptor.RangeState rangeState = MonthCellDescriptor.RangeState.NONE;
                if (selectedCals.size() > 1) {
                    if (sameDate(minSelectedCal, jalaliCal)) {
                        rangeState = MonthCellDescriptor.RangeState.FIRST;
                    } else if (sameDate(maxDate(selectedCals), jalaliCal)) {
                        rangeState = MonthCellDescriptor.RangeState.LAST;
                    } else if (betweenDates(jalaliCal, minSelectedCal, maxSelectedCal)) {
                        rangeState = MonthCellDescriptor.RangeState.MIDDLE;
                    }
                }
                MonthCellDescriptor tmpCell = new MonthCellDescriptor(jalaliCal, isCurrentMonth, isSelectable, isSelected, isToday, isHighlighted, value, rangeState);
                if (isSelected)
                    selectedCells.add(tmpCell);
                weekCells.add(tmpCell);
                jalaliCal.addPersianDate(DATE, 1);
            }
        }

        UpdateCalView();
    }

    private boolean doSelectDate(PersianCalendar date, MonthCellDescriptor cell) {

        for (MonthCellDescriptor selectedCell : selectedCells) {
            selectedCell.setRangeState(MonthCellDescriptor.RangeState.NONE);
        }

        if (selectedCals.size() > 1) {
            // We've already got a range selected: clear the old one.
            clearOldSelections();
        } else if (selectedCals.size() == 1 && (date.before(selectedCals.get(0)) || date.equals(selectedCals.get(0)))) {
            // We're moving the start of the range back in time: clear the old start date.
            clearOldSelections();
        }

        if (date != null) {
            // Select a new cell.
            if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
                selectedCells.add(cell);
                cell.setSelected(true);
            }
            selectedCals.add(date);

            if (selectedCells.size() > 1) {
                // Select all days in between start and end.
                PersianCalendar start = selectedCells.get(0).getDate();
                PersianCalendar end = selectedCells.get(1).getDate();
                selectedCells.get(0).setRangeState(MonthCellDescriptor.RangeState.FIRST);
                selectedCells.get(1).setRangeState(MonthCellDescriptor.RangeState.LAST);


                for (List<MonthCellDescriptor> week : cells) {
                    for (MonthCellDescriptor singleCell : week) {
                        if (betweenDates(singleCell.getDate(), start, end) && !containsDate(selectedCals, singleCell.getDate())) {
                            singleCell.setSelected(true);
                            singleCell.setRangeState(MonthCellDescriptor.RangeState.MIDDLE);
                            selectedCells.add(singleCell);
                        }
                    }
                }
            }
        }

        UpdateCalView();
        return date != null;
    }

    private void clearOldSelections() {
        for (MonthCellDescriptor selectedCell : selectedCells) {
            selectedCell.setSelected(false);
        }
        UpdateCalView();
        selectedCells.clear();
        selectedCals.clear();
    }

    private void UpdateCalView() {
        monthView.init(month, cells);
    }

    private class CellClickedListener implements MonthView.Listener {
        @Override
        public void handleClick(MonthCellDescriptor cell) {
            if (cell != null && cell.isSelectable()) {
                boolean wasSelected = doSelectDate(cell.getDate(), cell);
            } else
                Toast.makeText(context, "شما از امروز به بعد میتوانید برنامه ریزی کنید!!!", Toast.LENGTH_LONG);
        }
    }
}
