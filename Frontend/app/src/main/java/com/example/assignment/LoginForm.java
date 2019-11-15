package com.example.assignment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginForm extends Fragment {

    View rootView;

    public LoginForm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login_form, container, false);


        Button login = (Button) rootView.findViewById(R.id.login);
        Button signup = (Button) rootView.findViewById(R.id.sign_up);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) rootView.findViewById(R.id.username)).getText().toString();
                String password = ((EditText) rootView.findViewById(R.id.password)).getText().toString();
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                //Check if username and password are empty
                if (!(username.isEmpty()) && !(password.isEmpty())) {
                    loginAsyncTask.execute(username, password);
                } else {
                    Toast.makeText(getActivity(), "Invalid Entry", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main, new CredentialForm()).addToBackStack("login").commit();

            }
        });

        return rootView;
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, HashMap<String, String>> {
        private ProgressBar loading;


        @Override
        protected void onPreExecute() {
            loading = (ProgressBar) rootView.findViewById(R.id.login_progress);
            loading.setVisibility(View.VISIBLE);
        }

        @Override

        protected HashMap<String, String> doInBackground(String... params) {
            // get the information from the rest method
            return RestClient.login(params[0], Credentials.md5Hash(params[1]));
        }

        @Override
        protected void onPostExecute(HashMap<String, String> response) {
            // size will be 3 if there is a valid user
            if (response.size() == 3) {
                Bundle bundle = new Bundle();
                bundle.putString("name", response.get("name"));

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE).edit();
                editor.putInt("id", Integer.parseInt(response.get("id")));
                editor.putString("username", response.get("username"));
                editor.apply();
                Intent intent = new Intent(getActivity(), HomeScreen.class);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
            }
            loading.setVisibility(View.GONE);
        }
    }

}
