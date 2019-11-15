package com.example.assignment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private View rootView;
    private Spinner reportSelect;

    public ReportFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report, container, false);
        init();
        initListners();
        return rootView;
    }

    private void init() {
        reportSelect = (Spinner) rootView.findViewById(R.id.report_select);
        ArrayList<String> reportOptions = new ArrayList<String>();
        reportOptions.add("Daily Report");
        reportOptions.add("Overall Report");
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, reportOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportSelect.setAdapter(spinnerAdapter);
    }

    private void initListners() {
        reportSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //decides depending on the spinner selected value which report to display
                if (position == 0) {
                    fragmentTransaction.replace(R.id.report_screen, new PieChartFragment()).commit();
                } else {
                    fragmentTransaction.replace(R.id.report_screen, new BarChartFragment()).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
