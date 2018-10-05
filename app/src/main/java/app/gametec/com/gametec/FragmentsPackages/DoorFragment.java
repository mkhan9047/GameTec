package app.gametec.com.gametec.FragmentsPackages;


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

import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Alarm;
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


        DoorCall();

        /*update button click*/
        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDoorUpdate();
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

            doorOn.setBackgroundResource(R.drawable.green_btn_bg);
            doorOff.setBackgroundResource(R.drawable.border_balck);
            doorOn.setTextColor(Color.WHITE);
            doorOff.setTextColor(Color.DKGRAY);

        } else {

            doorOff.setBackgroundResource(R.drawable.red_btn_bg);
            doorOn.setBackgroundResource(R.drawable.border_balck);
            doorOff.setTextColor(Color.WHITE);
            doorOn.setTextColor(Color.DKGRAY);
        }

    }


    private void DoorCall() {
        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), "Loading...");
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<Door> alarmCall = networkInterface.getDoor(storage.getAccessType() + " " + storage.getAccessToken());
        alarmCall.enqueue(new Callback<Door>() {
            @Override
            public void onResponse(Call<Door> call, Response<Door> response) {
                Door door = response.body();
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
                Utility.DismissDialog(flower, getContext());
            }

            @Override
            public void onFailure(Call<Door> call, Throwable t) {
                Utility.DismissDialog(flower, getContext());
            }
        });
    }


    private void PostDoorUpdate() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), "Loading...");

        int status;

        if (doorON) {

            status = 1;

        } else {

            status = 0;
        }

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostDoorUpdate(storage.getAccessType() + " " + storage.getAccessToken(), status);
        updateFeaturesCall.enqueue(new Callback<UpdateFeatures>() {
            @Override
            public void onResponse(Call<UpdateFeatures> call, Response<UpdateFeatures> response) {

                UpdateFeatures features = response.body();

                if (features != null) {

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
