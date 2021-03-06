package app.gametec.com.gametec.FragmentsPackages;


import android.content.Intent;
import android.graphics.Color;
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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Clock;
import app.gametec.com.gametec.ModelPackages.Door;
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
public class DoorFragment extends Fragment {

    Button doorOn, doorOff;
    private boolean doorON = false;
    ImageButton back_button;
    TextView last_update_time;
    Button UpdateBtn;

    public DoorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_door, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onInit();


        /*back button*/
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });


        if (Utility.isInternetAvailable(getContext())) {
            DoorCall();
        } else {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }


        /*update button click*/
        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.isInternetAvailable(getContext())) {
                    PostDoorUpdate();
                } else {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void onInit() {

        View view = getView();

        if (view != null) {
            doorOff = view.findViewById(R.id.doorOffButton);
            doorOn = view.findViewById(R.id.doorOnButton);
            back_button = view.findViewById(R.id.door_back);
            last_update_time = view.findViewById(R.id.door_last_update_time);
            UpdateBtn = view.findViewById(R.id.door_update_btn);
        }


        /*set status */
        SetStatus();

    }

    private void SetStatus() {

        doorOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorON = false;
                FocusChangeListener();
            }
        });

        doorOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorON = true;
                FocusChangeListener();
            }
        });
    }


    private void FocusChangeListener() {

        if (doorON) {

            doorOn.setBackgroundResource(R.drawable.red_btn_bg);
            doorOff.setBackgroundResource(R.drawable.border_balck);
            doorOn.setTextColor(Color.WHITE);
            doorOff.setTextColor(Color.DKGRAY);

        } else {

            doorOff.setBackgroundResource(R.drawable.green_btn_bg);
            doorOn.setBackgroundResource(R.drawable.border_balck);
            doorOff.setTextColor(Color.WHITE);
            doorOn.setTextColor(Color.DKGRAY);
        }

    }


    private void DoorCall() {
        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> alarmCall = networkInterface.getDoor(storage.getAccessType() + " " + storage.getAccessToken(), storage.getCurrentMachine());
        alarmCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                /*token expiration handling*/

                switch (response.code()) {
                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }

                /*end of token expiration */

                if (response.body() != null) {

                    JSONObject jsonObject = null;
                    try {


                        jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (isSuccess) {

                            Gson gson = new Gson();

                            Door door = gson.fromJson(response.body(), Door.class);

                                if (door != null) {
                                    if (door.getData().getDoorOpening().getStatus() == 1) {

                                        doorON = true;

                                        FocusChangeListener();

                                    } else if (door.getData().getDoorOpening().getStatus() == 0) {

                                        doorON = false;

                                        FocusChangeListener();

                                    }

                                    last_update_time.setText(door.getData().getDoorOpening().getLastUpdate());
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


    private void PostDoorUpdate() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));

        int status;

        if (doorON) {

            status = 1;

        } else {

            status = 0;
        }

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> updateFeaturesCall = networkInterface.PostDoorUpdate(storage.getAccessType() + " " + storage.getAccessToken(), status, storage.getCurrentMachine());
        updateFeaturesCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                /*token expiration handling*/

                switch (response.code()) {
                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }

                /*end of token expiration */
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
