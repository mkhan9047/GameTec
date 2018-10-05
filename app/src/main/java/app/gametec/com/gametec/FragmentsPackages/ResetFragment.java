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

import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.MachineReset;
import app.gametec.com.gametec.ModelPackages.UpdateFeatures;
import app.gametec.com.gametec.Networking.NetworkInterface;
import app.gametec.com.gametec.Networking.RetrofitClient;
import app.gametec.com.gametec.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ResetFragment extends Fragment {


    ImageButton back_button;

    TextView resetTime, reset_last_update;

    Button resetBtn, updateBtn;

    public ResetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if (view != null) {

            back_button = view.findViewById(R.id.back_button);
            reset_last_update = view.findViewById(R.id.reset_last_update_time);
            resetTime = view.findViewById(R.id.reset_time);
            resetBtn = view.findViewById(R.id.Reset_onButton);
            updateBtn = view.findViewById(R.id.reset_update_btn);
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostResetUpdate();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostResetUpdate();
            }
        });


        onGetReset();
    }


    private void onGetReset() {

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        final Call<MachineReset> MachineCall = networkInterface.getMachine(storage.getAccessType() + " " + storage.getAccessToken());
        MachineCall.enqueue(new Callback<MachineReset>() {
            @Override
            public void onResponse(Call<MachineReset> call, Response<MachineReset> response) {
                MachineReset reset = response.body();
                if (reset != null) {
                    resetTime.setText(reset.getData().getResetMachine().getTime());
                    reset_last_update.setText(reset.getData().getResetMachine().getLastUpdate());
                }
            }

            @Override
            public void onFailure(Call<MachineReset> call, Throwable t) {

            }
        });
    }


    private void PostResetUpdate() {

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostResetUpdate(storage.getAccessType() + " " + storage.getAccessToken());
        updateFeaturesCall.enqueue(new Callback<UpdateFeatures>() {
            @Override
            public void onResponse(Call<UpdateFeatures> call, Response<UpdateFeatures> response) {

                UpdateFeatures features = response.body();

                if (features != null) {

                    Toast.makeText(getActivity(), features.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateFeatures> call, Throwable t) {

            }
        });
    }
}