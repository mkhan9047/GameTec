package app.gametec.com.gametec.FragmentsPackages;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChangeNetworkModuleFragment extends Fragment {

    Button btnBluetooth;

    public ChangeNetworkModuleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_network_module, container, false);
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
                    ((FragmentContainerActivity) activity).FragmentTransition(new ConnectBluetoothFragment());
                }
            }
        });

    }

    private void initViews(){

        View view = getView();

        if(view != null){

            btnBluetooth = view.findViewById(R.id.btn_bluetooth);


        }

    }

}
