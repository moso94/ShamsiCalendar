package ir.mosobhani.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    public static final int PLANNER_DATE = 4;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarDialog();
            }
        });
    }


    void showCalendarDialog() {

        Planner_Calendar_Dialog dataDialog = Planner_Calendar_Dialog.newInstance("Some Title");
//        dataDialog.setTargetFragment(this, PLANNER_DATE);
//        PersianCalendar start = _dataBasePlan.getPlanner_date_start();
//        PersianCalendar end = _dataBasePlan.getPlanner_date_end();
        Bundle args = null;
//        if (start != null && end != null) {
//            args = new Bundle();
//
//            if (!start.equals("") && !end.equals("")) {
//                try {
//                    args.putString("start", start.toJSON());
//                    args.putString("end", end.toJSON());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        dataDialog.setArguments(args);
        dataDialog.show(this.getSupportFragmentManager(), "fragment_edit_name");
    }
}
