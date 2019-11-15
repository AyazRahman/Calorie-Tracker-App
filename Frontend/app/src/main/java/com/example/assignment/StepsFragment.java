package com.example.assignment;


import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    private Button addSteps;
    private ListView listSteps;
    private StepsDatabase stepsDb = null;
    private List<Steps> allSteps;
    private List<HashMap<String, String>> stepsListArray;
    private String[] colHead;
    private int[] dataCell;
    private SimpleAdapter myListAdapter;

    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        init(rootView);
        initListners();


        return rootView;
    }

    private void init(View rootView) {
        addSteps = (Button) rootView.findViewById(R.id.steps_btn_add);
        listSteps = (ListView) rootView.findViewById(R.id.steps_listview);
        colHead = new String[]{"TIME", "STEPS"};
        dataCell = new int[]{R.id.step_cell_time, R.id.step_cell_steps};
        stepsListArray = new ArrayList<HashMap<String, String>>();
        stepsDb = Room.databaseBuilder(getActivity(), StepsDatabase.class, "StepsDatabase").fallbackToDestructiveMigration().build();
        ReadStepsDatabase initialize = new ReadStepsDatabase();
        initialize.execute();
    }

    private void initListners() {

        //for adding a new step entry
        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.steps_dialog);
                final EditText value = (EditText) dialog.findViewById(R.id.steps_step_value);
                Button addBtn = (Button) dialog.findViewById(R.id.steps_add_btn);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (value.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Invalid Entry", Toast.LENGTH_SHORT).show();
                        } else {

                            int stp = Integer.parseInt(value.getText().toString());
                            Date date = new Date();
                            DateFormat format = new SimpleDateFormat("HH:mm aa");
                            Steps newValue = new Steps(stp, format.format(date));
                            AddStep addTask = new AddStep();
                            addTask.execute(newValue);
                            dialog.cancel();
                        }
                    }
                });
                dialog.show();
            }
        });

        //for updating or deleting the step from list select
        listSteps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.steps_dialog);
                final EditText value = (EditText) dialog.findViewById(R.id.steps_step_value);
                //populate with the existing value
                value.setText(Integer.toString(allSteps.get(position).getSteps()));
                final Button updateBtn = (Button) dialog.findViewById(R.id.steps_update_btn);
                updateBtn.setVisibility(View.VISIBLE);
                Button deleteBtn = (Button) dialog.findViewById(R.id.steps_delete_btn);
                deleteBtn.setVisibility(View.VISIBLE);

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (value.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Invalid Entry", Toast.LENGTH_SHORT).show();
                        } else {
                            //update the step entry for valid entry
                            Steps updateStep = allSteps.get(position);
                            //update locally
                            updateStep.setSteps(Integer.parseInt(value.getText().toString()));
                            //update on the DB
                            UpdateSteps updateTask = new UpdateSteps();
                            updateTask.execute(updateStep);
                            dialog.cancel();
                        }
                    }
                });

                //for deleting the step entry
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete locally
                        Steps deleteStep = allSteps.remove(position);
                        DeleteStep deleteTask = new DeleteStep();
                        //delete on the DB
                        deleteTask.execute(deleteStep);
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    private class AddStep extends AsyncTask<Steps, Void, Void> {
        @Override
        protected Void doInBackground(Steps... params) {
            allSteps.add(params[0]);
            stepsDb.stepsDao().insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateAdpater();
        }
    }

    private class DeleteStep extends AsyncTask<Steps, Void, Void> {
        @Override
        protected Void doInBackground(Steps... params) {
            stepsDb.stepsDao().delete(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateAdpater();
        }
    }

    private class ReadStepsDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //Steps test = new Steps(250, "11:56");
            //stepsDb.stepsDao().insert(test);
            allSteps = stepsDb.stepsDao().getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateAdpater();
        }
    }

    private class UpdateSteps extends AsyncTask<Steps, Void, Void> {
        @Override
        protected Void doInBackground(Steps... params) {
            //Steps test = new Steps(250, "11:56");
            //stepsDb.stepsDao().insert(test);
            stepsDb.stepsDao().updateSteps(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateAdpater();
        }
    }

    //update the listView adapter whenever there is a change
    public void updateAdpater() {
        if (!allSteps.isEmpty() || allSteps != null) {
            stepsListArray = new ArrayList<HashMap<String, String>>();
            for (Steps item : allSteps) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(colHead[0], item.getTime());
                map.put(colHead[1], Integer.toString(item.getSteps()));
                stepsListArray.add(map);
            }
            myListAdapter = new SimpleAdapter(getActivity(), stepsListArray, R.layout.steps_list_cell, colHead, dataCell);
            listSteps.setAdapter(myListAdapter);
        }
    }

}
