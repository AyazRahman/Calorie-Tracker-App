package com.example.assignment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment {

    private View rootView;
    private EditText pieDate;
    private Button pieBtn;
    private PieChart pieChart;
    private SharedPreferences user;

    public PieChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        init();
        initListners();
        return rootView;
    }

    private void init() {
        pieChart = (PieChart) rootView.findViewById(R.id.report_pie_chart);
        pieDate = (EditText) rootView.findViewById(R.id.pie_chart_date);
        pieBtn = (Button) rootView.findViewById(R.id.pie_chart_btn);
        user = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }

    private void initListners() {
        pieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pieDate.getText().toString().isEmpty()) {
                    RetireveDataAsync task = new RetireveDataAsync();
                    task.execute();
                } else {
                    Toast.makeText(getActivity(), "Date is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        pieDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pieDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                dialog.show();
            }
        });


    }

    private class RetireveDataAsync extends AsyncTask<Void, Void, String> {
        @Override
        public String doInBackground(Void... params) {
            String date = pieDate.getText().toString();
            int userid = user.getInt("id", -1);

            return RestClient.pieData(date, userid);
        }

        @Override
        public void onPostExecute(String result) {
            //checks if a valid entry is returned
            if (result.length() > 4) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    setUpPieChart((float) jsonObject.getDouble("caloriesconsumed"), (float) jsonObject.getDouble("caloriesburned"), (float) jsonObject.getDouble("calorieremaining"));
                } catch (Exception e) {
                }


            } else {
                Toast.makeText(getActivity(), "No report to show", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpPieChart(float... params) {
        List<PieEntry> data = new ArrayList<PieEntry>();

        data.add(new PieEntry(params[0], "Calories Consumed"));
        data.add(new PieEntry(params[1], "Calories Burned"));
        data.add(new PieEntry(params[2], "Calories Remaining"));


        PieDataSet dataSet = new PieDataSet(data, "");

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        PieData pieData = new PieData(dataSet);

        pieData.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextSize(12f);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.invalidate();
        pieChart.setVisibility(View.VISIBLE);
    }

}
