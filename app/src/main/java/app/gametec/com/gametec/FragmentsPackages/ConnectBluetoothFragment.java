package app.gametec.com.gametec.FragmentsPackages;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import app.gametec.com.gametec.ActivityPackages.AuthnicateActivity;
import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ConnectBluetoothFragment extends Fragment {

    Button turnOnBluetooth;
    Button pairBluetooth;
    Switch btStatus;
    Button btn_back;
    BroadcastReceiver bluetoothStatuReceiver;

    Activity activity;
    BluetoothAdapter adapter;

    public ConnectBluetoothFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect_bluetooth, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();

        if (getActivity() != null) {
            activity = getActivity();
        }

        adapter = BluetoothAdapter.getDefaultAdapter();

        turnOnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adapter != null) {
                    if (!adapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activity.startActivityForResult(enableBtIntent, 1);
                    } else {
                        Toast.makeText(activity, R.string.bluetooh_already_enabled, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(activity, R.string.bluetooth_not_support, Toast.LENGTH_SHORT).show();
                }


            }
        });

        pairBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getActivity() instanceof FragmentContainerActivity) {
                    ((FragmentContainerActivity) activity).FragmentTransition(new AdminFragment());
                } else if (getActivity() instanceof AuthnicateActivity) {
                    ((AuthnicateActivity) activity).FragmentTransition(new SwitchNetworkFragment());
                }

            }
        });

        if (adapter != null)
            if (adapter.isEnabled()) {
                btStatus.setChecked(true);
            } else {

                btStatus.setChecked(false);

            }


        bluetoothStatuReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                            == BluetoothAdapter.STATE_OFF) {

                        btStatus.setChecked(false);
                    } else {

                        btStatus.setChecked(true);
                    }
                }
            }


        };


        activity.registerReceiver(bluetoothStatuReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(bluetoothStatuReceiver);
    }

    private void initViews() {

        View view = getView();

        if (view != null) {
            btn_back = view.findViewById(R.id.btn_back);
            turnOnBluetooth = view.findViewById(R.id.btn_turn_on_bluetooth);
            pairBluetooth = view.findViewById(R.id.btn_pair_bluetooth);
            btStatus = view.findViewById(R.id.swt_status);
        }


    }


}
