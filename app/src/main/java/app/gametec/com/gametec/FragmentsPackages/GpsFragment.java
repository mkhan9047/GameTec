package app.gametec.com.gametec.FragmentsPackages;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Door;
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
    ImageButton back_button;


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
            back_button = view.findViewById(R.id.back_button);

        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utility.isInternetAvailable(getContext())){
                    PostGpsUpdate();
                }else{
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }


            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (Utility.isInternetAvailable(getContext())) {
            getLocation();
        } else {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }


    }


    private void getLocation() {
        final ACProgressFlower   flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        Storage storage = new Storage(getContext());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> locationCall = networkInterface.getLocation(storage.getAccessType() + " " + storage.getAccessToken(), storage.getCurrentMachine());
        locationCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body() != null) {

                    JSONObject jsonObject = null;
                    try {


                        jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (isSuccess) {

                            Gson gson = new Gson();

                            Location location = gson.fromJson(response.body(), Location.class);
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
                            Utility.DismissDialog(flower);

                        } else {

                            Utility.showDialog(getActivity(), message);
                            Utility.DismissDialog(flower);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                Utility.DismissDialog(flower);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.DismissDialog(flower);
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void PostGpsUpdate() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> updateFeaturesCall = networkInterface.PostGpsUpdate(storage.getAccessType() + " " + storage.getAccessToken(), storage.getCurrentMachine());
        updateFeaturesCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body() != null) {

                    JSONObject jsonObject = null;
                    try {


                        jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (isSuccess) {

                            Gson gson = new Gson();
                            UpdateFeatures machine = gson.fromJson(response.body(), UpdateFeatures.class);
                            Toast.makeText(getActivity(), machine.getMessage(), Toast.LENGTH_SHORT).show();
                            Utility.DismissDialog(flower);

                        } else {

                            Utility.showDialog(getActivity(), message);
                            Utility.DismissDialog(flower);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                Utility.DismissDialog(flower);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.DismissDialog(flower);
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
