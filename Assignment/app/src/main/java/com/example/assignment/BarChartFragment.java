package com.example.assignment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BarChartFragment extends Fragment {

    private View rootView;
    private EditText startDate;
    private EditText endDate;
    private Button barBtn;
    private BarChart barChart;
    private SharedPreferences user;


    public BarChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        init();
        initListners();
        return rootView;
    }

    private void init() {
        startDate = (EditText) rootView.findViewById(R.id.bar_chart_date1);
        endDate = (EditText) rootView.findViewById(R.id.bar_chart_date2);
        barBtn = (Button) rootView.findViewById(R.id.bar_chart_btn);
        barChart = (BarChart) rootView.findViewById(R.id.report_bar_chart);
        user = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

    }

    private void initListners() {
        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()) {
                    RetireveDataAsync task = new RetireveDataAsync();
                    task.execute();
                } else {
                    Toast.makeText(getActivity(), "Date is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
    }

    private class RetireveDataAsync extends AsyncTask<Void, Void, Void> {
        String[] dates;
        float[] caloriesconsumed;
        float[] caloriesburned;

        @Override
        public Void doInBackground(Void... params) {
            String start = startDate.getText().toString();
            String end = endDate.getText().toString();
            int userid = user.getInt("id", -1);
            try {
                JSONArray jsonArray = new JSONArray(RestClient.barData(start, end, userid));
                dates = new String[jsonArray.length()];
                caloriesburned = new float[jsonArray.length()];
                caloriesconsumed = new float[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    dates[i] = jsonArray.getJSONObject(i).getString("date").substring(0, 10);
                    caloriesburned[i] = (float) jsonArray.getJSONObject(i).getDouble("caloriesburned");
                    caloriesconsumed[i] = (float) jsonArray.getJSONObject(i).getDouble("caloriesconsumed");
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            //checks if there is at least one entry
            if (dates.length > 0) {
                setUpBarChart(dates, caloriesburned, caloriesconsumed);


            } else {
                Toast.makeText(getActivity(), "No report to show", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpBarChart(String[] dates, float[] caloriesburned, float[] caloriesconsumed, float... params) {
        BarChart barChart = (BarChart) rootView.findViewById(R.id.report_bar_chart);
        ArrayList<BarEntry> burnedEntry = new ArrayList<BarEntry>();
        ArrayList<BarEntry> consumedEntry = new ArrayList<BarEntry>();

        for (int i = 0; i < caloriesburned.length; i++) {
            burnedEntry.add(new BarEntry(i, caloriesburned[i]));
            consumedEntry.add(new BarEntry(i, caloriesconsumed[i]));
        }


        BarDataSet dataset1 = new BarDataSet(consumedEntry, "Calories Consumed");
        BarDataSet dataset2 = new BarDataSet(burnedEntry, "Calories Burned");
        dataset1.setColor(ColorTemplate.MATERIAL_COLORS[0]);
        dataset2.setColor(ColorTemplate.MATERIAL_COLORS[1]);
        dataset1.setValueTextSize(10f);
        dataset2.setValueTextSize(10f);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextSize(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        //xAxis.setLabelRotationAngle(90f);
        //xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(dates.length);


        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setAxisMinimum(0);

        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setTextSize(15f);
        barChart.setDragEnabled(true);
        //barChart.setVisibleXRangeMaximum(3);


        BarData barData = new BarData(dataset1, dataset2);
        barData.setBarWidth(0.3f);
        barChart.setData(barData);
        barChart.groupBars(0, 0.2f, 0.1f);
        //barChart.getRendererXAxis().getPaintAxisLabels().setTextAlign(Paint.Align.RIGHT);

        barChart.notifyDataSetChanged();
        barChart.invalidate();
        this.barChart.setVisibility(View.VISIBLE);

    }


}
