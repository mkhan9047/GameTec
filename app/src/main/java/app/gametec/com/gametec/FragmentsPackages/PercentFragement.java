package app.gametec.com.gametec.FragmentsPackages;


import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.SocketTimeoutException;
import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.PercentControl;
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
public class PercentFragement extends Fragment {

    ImageButton backButton;
    TextView client, machine;
    TextView last_update_time;
    Button update_btn;

    public PercentFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_percent_fragement, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if (view != null) {
            backButton = view.findViewById(R.id.back_button);
            client = view.findViewById(R.id.client_percent);
            machine = view.findViewById(R.id.machine_percent);
            last_update_time = view.findViewById(R.id.percent_last_update);
            update_btn = view.findViewById(R.id.percent_update_btn);
        }


        /*back button*/
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });

        if (Utility.isInternetAvailable(getContext())) {
            getPercent();
        } else {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isInternetAvailable(getContext())) {
                    PostPercentControl();
                } else {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void getPercent() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), getString(R.string.loading));
        Storage storage = new Storage(getContext());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<PercentControl> percentControlCall = networkInterface.getPercent(storage.getAccessType() + " " + storage.getAccessToken());
        percentControlCall.enqueue(new Callback<PercentControl>() {
            @Override
            public void onResponse(Call<PercentControl> call, Response<PercentControl> response) {

                /*token expiration handling*/

                switch (response.code()) {
                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }

                /*end of token expiration */

                PercentControl control = response.body();
                if (control != null) {
                    client.setText(control.getData().getPercentControl().getUser());
                    machine.setText(control.getData().getPercentControl().getMachine());
                    last_update_time.setText(control.getData().getPercentControl().getLastUpdate());
                }

                Utility.DismissDialog(flower, getContext());
            }

            @Override
            public void onFailure(Call<PercentControl> call, Throwable t) {

                Utility.DismissDialog(flower, getActivity());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void PostPercentControl() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), getString(R.string.loading));

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostPercentControl(storage.getAccessType() + " " + storage.getAccessToken());
        updateFeaturesCall.enqueue(new Callback<UpdateFeatures>() {
            @Override
            public void onResponse(Call<UpdateFeatures> call, Response<UpdateFeatures> response) {

                /*token expiration handling*/

                switch (response.code()) {
                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }

                /*end of token expiration */

                UpdateFeatures features = response.body();

                if (features != null) {

                    Toast.makeText(getActivity(), features.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Utility.DismissDialog(flower, getActivity());
            }

            @Override
            public void onFailure(Call<UpdateFeatures> call, Throwable t) {
                Utility.DismissDialog(flower, getActivity());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

