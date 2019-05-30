package com.example.assignment;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CredentialForm extends Fragment {

    private final List<String> loaOptions;
    private Button btn;
    private EditText username;
    private EditText password;
    private EditText retype;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText dob;
    private EditText height;
    private EditText weight;
    private EditText address;
    private EditText postcode;
    private EditText steps;

    private Spinner loa;
    RadioGroup gender;

    private final Users newUser;
    private final Credentials newCredential;

    public CredentialForm() {
        // Required empty public constructor
        loaOptions = new ArrayList<String>();
        loaOptions.add("1");
        loaOptions.add("2");
        loaOptions.add("3");
        loaOptions.add("4");
        loaOptions.add("5");

        newUser = new Users();
        newCredential = new Credentials();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_credential_form, container, false);
        //newUser.setGender("M");


        initViews(rootView);
        initListners();

        return rootView;
    }

    private void initViews(View rootView) {
        btn = (Button) rootView.findViewById(R.id.sign_up_form_button);
        username = (EditText) rootView.findViewById(R.id.credential_form_username);
        password = (EditText) rootView.findViewById(R.id.credential_form_password);
        retype = (EditText) rootView.findViewById(R.id.credential_form_retypr);
        name = (EditText) rootView.findViewById(R.id.user_form_name);
        surname = (EditText) rootView.findViewById(R.id.user_form_lastname);
        email = (EditText) rootView.findViewById(R.id.user_form_email);
        dob = (EditText) rootView.findViewById(R.id.user_form_dob);
        height = (EditText) rootView.findViewById(R.id.user_form_height);
        weight = (EditText) rootView.findViewById(R.id.user_form_weight);
        address = (EditText) rootView.findViewById(R.id.user_form_address);
        postcode = (EditText) rootView.findViewById(R.id.user_form_postcode);
        steps = (EditText) rootView.findViewById(R.id.user_form_steps);

        loa = (Spinner) rootView.findViewById(R.id.user_form_loa);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, loaOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loa.setAdapter(spinnerAdapter);

        gender = (RadioGroup) rootView.findViewById(R.id.user_form_gender);
    }

    // this creates the new user and credentials
    private class SignupAsyncTask extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            Users newUser = (Users) params[0];
            Credentials newCredential = (Credentials) params[1];
            return RestClient.signUp(newUser, newCredential);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main, new LoginForm()).commit();
                //fragmentManager.popBackStack();
            } else
                Toast.makeText(getActivity(), "Sign up Failed", Toast.LENGTH_LONG).show();
        }
    }

    // this checks if the email already exists
    private class CheckEmail extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            return RestClient.checkEmail(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                email.setError("email already exist");
                email.setText("");
            } else email.setError(null);
        }
    }

    //checks if the username exists
    private class CheckUsername extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            return RestClient.checkUsername(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                username.setError("username already exist");
                username.setText("");
            } else username.setError(null);
        }
    }

    private void initListners() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    setAttributes();
                    SignupAsyncTask signupAsyncTask = new SignupAsyncTask();
                    signupAsyncTask.execute(newUser, newCredential);
                }


            }
        });

        loa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected != null) {
                    newUser.setLevelofactivity(selected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected = (RadioButton) group.findViewById(checkedId);

                if (selected.isChecked()) {
                    if (selected.getText().equals("Male")) {
                        newUser.setGender("M");
                    } else {
                        newUser.setGender("F");
                    }
                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CheckEmail task = new CheckEmail();
                    task.execute(email.getText().toString());
                }
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CheckUsername task = new CheckUsername();
                    task.execute(username.getText().toString());
                }
            }
        });
    }

    private boolean validate() {
        //checks if any of the field is empty
        if (username.getText().toString().isEmpty()
                || password.getText().toString().isEmpty()
                || retype.getText().toString().isEmpty()
                || name.getText().toString().isEmpty()
                || surname.getText().toString().isEmpty()
                || email.getText().toString().isEmpty()
                || dob.getText().toString().isEmpty()
                || height.getText().toString().isEmpty()
                || weight.getText().toString().isEmpty()
                || address.getText().toString().isEmpty()
                || postcode.getText().toString().isEmpty()
                || steps.getText().toString().isEmpty()
                || newUser.getGender() == null
                || newUser.getLevelofactivity() == null) {
            Toast.makeText(getActivity(), "All Fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }
        //check if the email is valid
        else if (!validEmail(email.getText().toString())) {
            Toast.makeText(getActivity(), "Email not Valid", Toast.LENGTH_SHORT).show();
            email.setError("Not Valid");
            return false;
        }
        //check if the password is longer than 8 characters
        else if (password.getText().toString().length() < 8) {
            Toast.makeText(getActivity(), "Password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
            password.setError("Should be at least 8 characters");
            return false;
        }
        //check if the retype passwords match
        else if (!password.getText().toString().equals(retype.getText().toString())) {
            Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            password.setError("passwords don't match");
            return false;
        }
        //check if the date format is correct
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                newUser.setDob(formatter.parse(dob.getText().toString()));
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Incorrect date format", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    //Validation for email
    private boolean validEmail(String email) {
        String pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(pattern);
    }

    //set the new information for user and credential
    private void setAttributes() {
        newUser.setName(name.getText().toString());
        newUser.setSurname(surname.getText().toString());
        newUser.setEmail(email.getText().toString());
        newUser.setHeight(new BigDecimal(height.getText().toString()));
        newUser.setWeight(new BigDecimal(weight.getText().toString()));
        newUser.setAddress(address.getText().toString());
        newUser.setPostcode(postcode.getText().toString());
        newUser.setStepspermile(Integer.parseInt(steps.getText().toString()));

        newCredential.setUsername(username.getText().toString());
        newCredential.setPasswordhash(password.getText().toString());
        newCredential.setSignupdate(new Date());
        //newCredential.setUserid(newUser);

    }


}
