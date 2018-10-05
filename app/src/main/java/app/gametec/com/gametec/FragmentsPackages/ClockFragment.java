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
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Alarm;
import app.gametec.com.gametec.ModelPackages.Clock;
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
public class ClockFragment extends Fragment {

    ImageButton back_button;
    TextView clockTime, last_update_time;
    Button button;

    public ClockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clock, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if (view != null) {

            clockTime = view.findViewById(R.id.clock_time);
            last_update_time = view.findViewById(R.id.clock_last_update_time);
            back_button = view.findViewById(R.id.back_button);
            button = view.findViewById(R.id.clock_update_btn);

        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });

        ClockCall();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostClockUpdate();
            }
        });

    }


    private void ClockCall() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), "Loading...");
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<Clock> alarmCall = networkInterface.getClock(storage.getAccessType() + " " + storage.getAccessToken());
        alarmCall.enqueue(new Callback<Clock>() {
            @Override
            public void onResponse(Call<Clock> call, Response<Clock> response) {

                Clock clock = response.body();

                if (clock != null) {

                    clockTime.setText(clock.getData().getClock().getTime());
                    last_update_time.setText(clock.getData().getClock().getLastUpdate());

                }

                Utility.DismissDialog(flower, getContext());
            }

            @Override
            public void onFailure(Call<Clock> call, Throwable t) {
                Utility.DismissDialog(flower, getContext());
            }
        });
    }


    private void PostClockUpdate() {

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostClockUpdate(storage.getAccessType() + " " + storage.getAccessToken());
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