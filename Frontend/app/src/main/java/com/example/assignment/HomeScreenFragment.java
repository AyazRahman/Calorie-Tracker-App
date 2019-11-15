package com.example.assignment;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreenFragment extends Fragment {


    public HomeScreenFragment() {
        // Required empty public constructor
    }


    private StepsDatabase stepsDb = null;

    private View rootView;
    private TextView dailyGoal;
    private TextView dailyCalorieConsumed;
    private TextView dailyCalorieBurned;
    private TextView dailyStepsTaken;
    private TextView welcome;
    private TextView datetime;
    private SharedPreferences report;
    private SharedPreferences user;
    private Bundle bundle;
    private Button saveReport;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        init();
        initListners();
        bundle = getArguments();
        welcome.setText("Welcome " + bundle.get("name"));


        int value = report.getInt(user.getString("username", ""), -1);
        showDailyCalorie(value);


        //for showing the time on screen
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            setDateTime();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);

        return rootView;
    }

    private void init() {
        welcome = (TextView) rootView.findViewById(R.id.welcome_message);
        datetime = (TextView) rootView.findViewById(R.id.date_time);
        dailyGoal = rootView.findViewById(R.id.daily_goal);

        report = getActivity().getSharedPreferences("reportinfo", Context.MODE_PRIVATE);
        user = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        dailyCalorieConsumed = (TextView) rootView.findViewById(R.id.daily_calorieconsumed);
        dailyStepsTaken = (TextView) rootView.findViewById(R.id.daily_stepstaken);
        dailyCalorieBurned = (TextView) rootView.findViewById(R.id.daily_calorieburned);
        saveReport = (Button) rootView.findViewById(R.id.save_report);


        stepsDb = Room.databaseBuilder(getActivity(), StepsDatabase.class, "StepsDatabase").fallbackToDestructiveMigration().build();
    }

    private void initListners() {
        dailyGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.daily_goal_dialog);
                EditText field = (EditText) dialog.findViewById(R.id.daily_calorie);
                //read the value from shared preference
                final int value = report.getInt(user.getString("username", ""), -1);
                // if the value exist
                if (value != -1) {
                    field.setText(Integer.toString(value));
                }
                Button dialogBtn = (Button) dialog.findViewById(R.id.daily_calorie_btn);
                dialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText field = (EditText) dialog.findViewById(R.id.daily_calorie);
                        if (field.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Invalid Entry", Toast.LENGTH_SHORT).show();
                        } else {
                            //update the valid value in shared preference
                            int newValue = Integer.parseInt(field.getText().toString());
                            SharedPreferences.Editor editor = report.edit();
                            editor.putInt(user.getString("username", ""), newValue).apply();
                            showDailyCalorie(newValue);
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();

            }
        });


        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostReportAsync task = new PostReportAsync();
                task.execute();
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, new Intent(getContext(), DailyReportSave.class), 0);
        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

    }

    public class DailyReportSave extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            PostReportAsync task = new PostReportAsync();
            task.execute();
        }
    }


    private void setDateTime() {
        //sets the date time functionality
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mm");
        datetime.setText(dateFormat.format(date));
    }


    private class PostReportAsync extends AsyncTask<Void, Void, Void> {
        //post the report to the database
        @Override
        protected Void doInBackground(Void... params) {
            Report todaysReport = new Report();
            todaysReport.setCaloriesburned(new BigDecimal(dailyCalorieBurned.getText().toString().split(": ")[1]));
            todaysReport.setCaloriesconsumed(new BigDecimal(dailyCalorieConsumed.getText().toString().split(": ")[1]));
            todaysReport.setCaloriegoal(new BigDecimal(report.getInt(user.getString("username", ""), 0)));

            todaysReport.setStepstaken(Integer.parseInt(dailyStepsTaken.getText().toString().split(": ")[1]));
            todaysReport.setDate(new Date());
            int userid = user.getInt("id", -1);
            if (userid != -1) {
                RestClient.postReport(todaysReport, userid);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //clear steps
            ClearStepsAsync clearTask = new ClearStepsAsync();
            clearTask.execute();
            //remove daily goal
            report.edit().remove(user.getString("username", "")).commit();
            //update the view
            showDailyCalorie(report.getInt(user.getString("username", ""), -1));
        }
    }

    private class ClearStepsAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            stepsDb.stepsDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class DailyInfoAsync extends AsyncTask<Integer, Void, String[]> {
        @Override
        protected String[] doInBackground(Integer... params) {
            String[] result = new String[4];

            //get the information for calorie screen
            result[0] = RestClient.getTotalCalorieConsumed(params[0]);
            result[1] = Integer.toString(stepsDb.stepsDao().getTotalSteps());
            result[2] = RestClient.getCalorieBurnedRest(params[0]);
            result[3] = RestClient.getCalorieBurnedPerStep(params[0]);

            return result;
        }

        @Override
        protected void onPostExecute(String... result) {
            // update the calorie screen
            dailyCalorieConsumed.setText("Consumed: " + result[0]);
            dailyStepsTaken.setText("Steps Taken: " + result[1]);
            String value = String.format("Burned: %.2f", Integer.parseInt(result[1]) * Double.parseDouble(result[3]) + Double.parseDouble(result[2]));
            dailyCalorieBurned.setText(value);
        }
    }


    private void showDailyCalorie(int value) {
        if (value == -1) {
            dailyGoal.setText("Tap to Set Goal");
        } else {
            dailyGoal.setText("Today's Goal: " + value);
        }
        DailyInfoAsync task = new DailyInfoAsync();
        task.execute(user.getInt("id", -1));
    }

}
