package app.gametec.com.gametec.FragmentsPackages;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import app.gametec.com.gametec.ActivityPackages.AuthnicateActivity;
import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.DialogPackage.FingerPrintDialog;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SwitchNetworkFragment extends Fragment {

    Button btnBluetooth, btnGsm;
    Activity activity;
    public SwitchNetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_switch_network, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if (activity != null) {
                    ((AuthnicateActivity) activity).FragmentTransition(new ConnectBluetoothFragment());
                }
            }
        });


        if(getActivity() != null){

             activity = getActivity();
        }


        btnGsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String got = ((AuthnicateActivity)activity).got;

                Storage storage = new Storage(activity);

                if (!storage.getHasPin()) {

                    if (storage.getHasPin()) {

                        ((AuthnicateActivity) activity).FragmentTransition(new InsertPinFragment());

                    } else {

                        ((AuthnicateActivity) activity).FragmentTransition(new SetPinFragment());
                    }

                } else {

                    if (got != null) {

                        if (got.equals("finger_print")) {
                            ((AuthnicateActivity)activity).layout.setBackgroundResource(R.drawable.splash_bg);
                            FingerPrintDialog dialog = new FingerPrintDialog();
                            dialog.show(getChildFragmentManager(), null);
                        }

                    } else {

                        if (storage.getHasPin()) {

                            ((AuthnicateActivity) getActivity()).FragmentTransition(new InsertPinFragment());

                        } else {

                            ((AuthnicateActivity) getActivity()).FragmentTransition(new SetPinFragment());
                        }

                    }

                }
            }
        });
    }

    private void initViews(){
        View view = getView();
        if(view != null){
            btnBluetooth = view.findViewById(R.id.btn_bluetooth);
            btnGsm = view.findViewById(R.id.btn_gsm);
        }
    }
}
