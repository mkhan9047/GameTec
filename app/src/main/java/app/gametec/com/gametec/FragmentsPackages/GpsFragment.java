package app.gametec.com.gametec.FragmentsPackages;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Location;
import app.gametec.com.gametec.ModelPackages.UpdateFeatures;
import app.gametec.com.gametec.Networking.NetworkInterface;
import app.gametec.com.gametec.Networking.RetrofitClient;
import app.gametec.com.gametec.R;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class GpsFragment extends Fragment implements OnMapReadyCallback {


    GoogleMap map;
    LatLng latLng;
    TextView last_update_time;
    Button updateBtn;


    public GpsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gps, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        View view = getView();

        if (view != null) {

            last_update_time = view.findViewById(R.id.gps_last_update_time);
            updateBtn = view.findViewById(R.id.gps_update_btn);

        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PostGpsUpdate();

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        getLocation();


    }


    private void getLocation() {
        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), "Loading...");
        Storage storage = new Storage(getContext());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<Location> locationCall = networkInterface.getLocation(storage.getAccessType() + " " + storage.getAccessToken());
        locationCall.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {

                Location location = response.body();

                if (location != null) {

                    latLng = new LatLng(Double.parseDouble(location.getData().getGps().getLatitude()), Double.parseDouble(location.getData().getGps().getLongitude()));

                    map.addMarker(new MarkerOptions().position(latLng));
                    /**
                     * moving camera of the map to the marker point lat and long
                     */
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    /**
                     * setting up zoom setting the map
                     */
                    map.setMinZoomPreference(10);
                    map.setMaxZoomPreference(15);

                    last_update_time.setText(location.getData().getGps().getLastUpdate());
                }
                Utility.DismissDialog(flower, getContext());
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Utility.DismissDialog(flower, getContext());
            }
        });
    }



    private void PostGpsUpdate(){

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), "Loading...");
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostGpsUpdate(storage.getAccessType() + " " + storage.getAccessToken());
        updateFeaturesCall.enqueue(new Callback<UpdateFeatures>() {
            @Override
            public void onResponse(Call<UpdateFeatures> call, Response<UpdateFeatures> response) {

                UpdateFeatures features = response.body();

                if(features != null){

                    Toast.makeText(getActivity(), features.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Utility.DismissDialog(flower, getActivity());
            }

            @Override
            public void onFailure(Call<UpdateFeatures> call, Throwable t) {

                Utility.DismissDialog(flower, getActivity());

            }
        });
    }
}
