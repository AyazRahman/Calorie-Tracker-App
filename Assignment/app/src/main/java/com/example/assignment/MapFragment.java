package com.example.assignment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private View rootView;
    private GoogleMap mMap;
    private LatLng userLocation;
    private Button parkBtn;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        parkBtn = (Button) rootView.findViewById(R.id.show_parks);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        parkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLocation != null) {
                    GetParksAsync task = new GetParksAsync();
                    task.execute();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GetUserAsync task = new GetUserAsync();
        task.execute();

    }

    private class GetUserAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Users u = RestClient.getUser(getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE).getInt("id", -1));
            String result = u.getAddress();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            userLocation = GeoCodeAPI.getUserCordinates(getContext(), result);
            //check if the user information is valid
            if (userLocation != null) {
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Address"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(userLocation).zoom(17).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                Toast.makeText(getActivity(), "User Address is invalid", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class GetParksAsync extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {

            return GeoCodeAPI.getParksLocation(userLocation);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            JSONArray parks = result;
            if (result.length() > 0) {
                parkBtn.setVisibility(View.INVISIBLE);
                mMap.animateCamera(CameraUpdateFactory.zoomBy(-5));
            } else {
                Toast.makeText(getActivity(), "No Parks to Show", Toast.LENGTH_LONG).show();
            }
            try {
                //add all the park markers
                for (int i = 0; i < parks.length(); i++) {
                    String name = parks.getJSONObject(i).getString("name");
                    JSONObject location = parks.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                    LatLng coordinate = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                    mMap.addMarker(new MarkerOptions().position(coordinate).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            } catch (Exception e) {
            }

        }
    }
}
