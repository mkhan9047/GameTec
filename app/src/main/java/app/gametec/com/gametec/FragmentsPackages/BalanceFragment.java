package app.gametec.com.gametec.FragmentsPackages;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import app.gametec.com.gametec.AdapterPackages.FeatureListAdapter;
import app.gametec.com.gametec.AdapterPackages.MachineListAdapter;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Balance;
import app.gametec.com.gametec.ModelPackages.Features;
import app.gametec.com.gametec.ModelPackages.Machine;
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
public class BalanceFragment extends Fragment {

    ImageButton back_button;
    Button balance_update_btn;
    TextView in, out, total, last_update_time;

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        InitView();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });

        if (Utility.isInternetAvailable(getContext())) {

            BalanceCall();

        } else {

            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();

        }


        balance_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.isInternetAvailable(getContext())) {

                    PostBalanceUpdate();

                } else {

                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    private void InitView() {

        View view = getView();

        if (view != null) {

            in = view.findViewById(R.id.balance_in);
            out = view.findViewById(R.id.balance_out);
            total = view.findViewById(R.id.balance_total);
            last_update_time = view.findViewById(R.id.balance_last_update_time);
            back_button = view.findViewById(R.id.back_button);
            balance_update_btn = view.findViewById(R.id.balance_update_btn);

        }
    }

    private void BalanceCall() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> alarmCall = networkInterface.getBalance(storage.getAccessType() + " " + storage.getAccessToken(), storage.getCurrentMachine());
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

                if (response.body() != null) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (isSuccess) {
                            Gson gson = new Gson();
                            Balance balance = gson.fromJson(response.body(), Balance.class);
                            if (balance != null) {
                                in.setText(balance.getData().getBalance().getIn());
                                out.setText(balance.getData().getBalance().getOut());
                                total.setText(balance.getData().getBalance().getTotal());
                                last_update_time.setText(balance.getData().getBalance().getLastUpdate());
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


    private void PostBalanceUpdate() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> updateFeaturesCall = networkInterface.PostBalanceUpdate(storage.getAccessType() + " " + storage.getAccessToken(), storage.getCurrentMachine());
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



                Log.e("res", response.body());

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
