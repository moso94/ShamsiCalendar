package ir.mosobhani.calendar;

import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import ir.mosobhani.shamsicalendar.calendar.PersianCalendar;
import ir.mosobhani.shamsicalendar.views.CalendarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale locale = new Locale("fa");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.setOnCalenderClick(new CalendarView.OnCalenderClick() {
            @Override
            public void handleClick(PersianCalendar clickedDay) {

            }
        });
    }


}
