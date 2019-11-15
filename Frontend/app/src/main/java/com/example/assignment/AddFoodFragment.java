package com.example.assignment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment {

    private View rootView;
    private Spinner category;

    private Button searchBtn;
    private Button postFood;

    private EditText searchQuery;

    private ImageView image;
    private TextView snippet;

    private TextView name;
    private TextView calorie;
    private TextView fat;
    private TextView servUnit;
    private TextView servAmt;

    public AddFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_food, container, false);
        init();
        initListners();
        return rootView;
    }

    private void init() {
        searchBtn = (Button) rootView.findViewById(R.id.search_food);

        searchQuery = (EditText) rootView.findViewById(R.id.search_query);

        image = (ImageView) rootView.findViewById(R.id.diet_image);
        snippet = (TextView) rootView.findViewById(R.id.diet_snippet);
        name = (TextView) rootView.findViewById(R.id.add_food_name);
        calorie = (TextView) rootView.findViewById(R.id.diet_calorie);
        fat = (TextView) rootView.findViewById(R.id.diet_fat);
        servUnit = (TextView) rootView.findViewById(R.id.diet_srvunit);
        servAmt = (TextView) rootView.findViewById(R.id.diet_srvamt);
        category = (Spinner) rootView.findViewById(R.id.food_category);
        postFood = (Button) rootView.findViewById(R.id.post_food);
        RetireveDataAsync task = new RetireveDataAsync();
        task.execute();
    }

    private void initListners() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchQuery.getText().toString().isEmpty()) {
                    GetFoodAsync task = new GetFoodAsync();
                    task.execute(searchQuery.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Invalid Query", Toast.LENGTH_LONG).show();
                }

            }
        });
        postFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty()) {
                    PostFoodAsync task = new PostFoodAsync();
                    task.execute();
                } else {
                    Toast.makeText(getActivity(), "Search for a valid food", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class GetFoodAsync extends AsyncTask<String, Void, HashMap<String, String>> {
        private Bitmap img = null;
        private String snip = null;

        @Override
        public HashMap<String, String> doInBackground(String... params) {
            //for getting the snippet
            snip = SearchGoogleApi.getSnippet(SearchGoogleApi.search(params[0],
                    new String[]{"num"},
                    new String[]{"1"})).replace("\n", " ");
            //for getting the pic
            img = SearchGoogleApi.getImage(SearchGoogleApi.search(params[0],
                    new String[]{"num", "searchType"},
                    new String[]{"1", "image"}));
            HashMap<String, String> result = new HashMap<String, String>();
            try {
                //get the required food information from fatSecret
                result = FatSecretApi.getFoodItem(FatSecretApi.getFoodItems(params[0]));
            } catch (Exception e) {
            }
            return result;
        }

        @Override
        public void onPostExecute(HashMap<String, String> result) {
            //set the information retrieved
            image.setImageBitmap(img);
            snippet.setText(snip);
            name.setText(result.get("name"));
            calorie.setText(result.get("calories"));
            fat.setText(result.get("fat"));
            servAmt.setText(result.get("serving amount"));
            servUnit.setText(result.get("serving unit"));

        }
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

    private class PostFoodAsync extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {
            //get the food information
            Food newFood = new Food();
            newFood.setFat(new BigDecimal(String.format("%.02f", Double.parseDouble(fat.getText().toString()))));
            newFood.setCalorieamount(new BigDecimal(String.format("%.02f", Double.parseDouble(calorie.getText().toString()))));
            newFood.setCategory(category.getSelectedItem().toString());
            newFood.setServingamount(new BigDecimal(String.format("%.02f", Double.parseDouble(servAmt.getText().toString()))));
            newFood.setServingunit(servUnit.getText().toString());
            newFood.setName(name.getText().toString());
            //add the food
            RestClient.postFood(newFood);
            return null;
        }

        @Override
        public void onPostExecute(Void result) {

            FragmentManager fragmentManager = getActivity().getFragmentManager();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.commit();
            Toast.makeText(getActivity(), "New Food Added", Toast.LENGTH_LONG).show();
        }
    }
}
