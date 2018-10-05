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
import app.gametec.com.gametec.ModelPackages.Bluetooth;
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
public class BluetoothFragment extends Fragment {


    Button bluetoothOn, bluetoothOff;
    private boolean bluetoothON = false;
    ImageButton back_button;
    TextView bluetooth_last_updateTime;
    Button update;

    public BluetoothFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluethooth, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onInit();

        BluetoothCall();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostBluetoothUpdate();
            }
        });
    }

    private void onInit() {

        View view = getView();

        if (view != null) {
            bluetoothOff = view.findViewById(R.id.bluetoothOffButton);
            bluetoothOn = view.findViewById(R.id.bluetoothOnButton);
            back_button = view.findViewById(R.id.back_button);
            bluetooth_last_updateTime = view.findViewById(R.id.bluetooth_last_update_time);
            update = view.findViewById(R.id.bluetooth_update_btn);
        }


        /*set status */
        SetStatus();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });

    }

    private void SetStatus() {
        bluetoothOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothON = false;
                FocusChangeListener();
            }
        });

        bluetoothOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothON = true;
                FocusChangeListener();
            }
        });
    }


    private void FocusChangeListener() {

        if (bluetoothON) {

            bluetoothOn.setBackgroundResource(R.drawable.green_btn_bg);
            bluetoothOff.setBackgroundResource(R.drawable.border_balck);
            bluetoothOn.setTextColor(Color.WHITE);
            bluetoothOff.setTextColor(Color.DKGRAY);

        } else {

            bluetoothOff.setBackgroundResource(R.drawable.red_btn_bg);
            bluetoothOn.setBackgroundResource(R.drawable.border_balck);
            bluetoothOff.setTextColor(Color.WHITE);
            bluetoothOn.setTextColor(Color.DKGRAY);
        }

    }


    private void BluetoothCall() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), "Loading...");
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<Bluetooth> alarmCall = networkInterface.getBluetooth(storage.getAccessType() + " " + storage.getAccessToken());
        alarmCall.enqueue(new Callback<Bluetooth>() {
            @Override
            public void onResponse(Call<Bluetooth> call, Response<Bluetooth> response) {

                Bluetooth bluetooth = response.body();

                if (bluetooth != null) {

                    if (bluetooth.getData().getBluetooth().getStatus() == 1) {
                        bluetoothON = true;
                        FocusChangeListener();
                    } else {
                        bluetoothON = false;
                        FocusChangeListener();
                    }

                    bluetooth_last_updateTime.setText(bluetooth.getData().getBluetooth().getLastUpdate());
                }

                Utility.DismissDialog(flower, getContext());
            }

            @Override
            public void onFailure(Call<Bluetooth> call, Throwable t) {

                Utility.DismissDialog(flower, getContext());
            }
        });
    }

    private void PostBluetoothUpdate() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), "Loading...");

        int status;

        if (bluetoothON) {

            status = 1;

        } else {

            status = 0;
        }

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostBluetoothUpdate(storage.getAccessType() + " " + storage.getAccessToken(), status);
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
                Utility.DismissDialog(flower, getContext());
            }
        });
    }

}
