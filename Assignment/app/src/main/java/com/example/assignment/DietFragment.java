package com.example.assignment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DietFragment extends Fragment {

    private ArrayList<Food> foodItems;

    private View rootView;
    private Spinner category;
    private Spinner food;
    private ImageView image;

    private TextView calorie;
    private TextView fat;
    private TextView srvUnit;
    private TextView srvAmt;
    private TextView snippet;

    private EditText quantity;

    private Button addConsumption;
    private Button addFood;

    private SharedPreferences user;


    public DietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_diet, container, false);
        init();
        initListners();
        return rootView;
    }

    private void init() {
        foodItems = new ArrayList<Food>();

        category = (Spinner) rootView.findViewById(R.id.diet_category);
        RetireveDataAsync task = new RetireveDataAsync();
        task.execute();
        food = (Spinner) rootView.findViewById(R.id.diet_food);

        calorie = (TextView) rootView.findViewById(R.id.diet_calorie);
        fat = (TextView) rootView.findViewById(R.id.diet_fat);
        srvUnit = (TextView) rootView.findViewById(R.id.diet_srvunit);
        srvAmt = (TextView) rootView.findViewById(R.id.diet_srvamt);
        image = (ImageView) rootView.findViewById(R.id.diet_image);
        snippet = (TextView) rootView.findViewById(R.id.diet_snippet);

        addConsumption = (Button) rootView.findViewById(R.id.diet_add_consumption);
        addFood = (Button) rootView.findViewById(R.id.diet_add_food);

        quantity = (EditText) rootView.findViewById(R.id.diet_quantity);

        user = rootView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);


    }

    private void initListners() {
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                GetFoodDataAsync task = new GetFoodDataAsync();
                task.execute(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        food.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Food selected = foodItems.get(position);
                calorie.setText(selected.getCalorieamount().toString());
                fat.setText(selected.getFat().toString());
                srvUnit.setText(selected.getServingunit());
                srvAmt.setText(selected.getServingamount().toString());
                GetImageAsync task = new GetImageAsync();
                task.execute(selected.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quantity.getText().toString().isEmpty()) {
                    PostConsumption task = new PostConsumption();
                    task.execute(quantity.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }

            }
        });
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_content, new AddFoodFragment()).addToBackStack("add food").commit();
            }
        });
    }

    private class RetireveDataAsync extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        public ArrayList<String> doInBackground(Void... params) {
            return RestClient.getFoodCategory();
        }

        @Override
        public void onPostExecute(ArrayList<String> result) {
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, result);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category.setAdapter(spinnerAdapter);
        }
    }

    private class GetFoodDataAsync extends AsyncTask<String, Void, ArrayList<Food>> {
        @Override
        public ArrayList<Food> doInBackground(String... params) {
            return RestClient.getFoodItems(params[0]);
        }

        @Override
        public void onPostExecute(ArrayList<Food> result) {
            foodItems = result;
            ArrayList<String> options = new ArrayList<String>();
            for (Food row : foodItems) {
                options.add(row.getName());
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, options);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            food.setAdapter(spinnerAdapter);
        }
    }

    private class GetImageAsync extends AsyncTask<String, Void, Void> {
        private Bitmap img = null;
        private String snip = null;

        @Override
        public Void doInBackground(String... params) {
            // for getting the snippet
            snip = SearchGoogleApi.getSnippet(SearchGoogleApi.search(food.getSelectedItem().toString(),
                    new String[]{"num"},
                    new String[]{"1"})).replace("\n", " ").replace("...", "");
            // for getting the pic
            img = SearchGoogleApi.getImage(SearchGoogleApi.search(food.getSelectedItem().toString(),
                    new String[]{"num", "searchType"},
                    new String[]{"1", "image"}));
            return null;
        }

        @Override
        public void onPostExecute(Void result) {

            //set the image and snippet
            image.setImageBitmap(img);
            snippet.setText(snip);
        }
    }

    private class PostConsumption extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            Consumption newConsumption = new Consumption();
            newConsumption.setUserid(RestClient.getUser(user.getInt("id", -1)));
            newConsumption.setFoodid(foodItems.get(food.getSelectedItemPosition()));
            newConsumption.setDate(new Date());
            newConsumption.setQuantity(new BigDecimal(params[0]));
            RestClient.postConsumption(newConsumption);
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.commit();
            Toast.makeText(getActivity(), "Entry added to Consumption", Toast.LENGTH_LONG).show();
        }
    }

}
