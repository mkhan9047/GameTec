package app.gametec.com.gametec.FragmentsPackages;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.Networking.NetworkInterface;
import app.gametec.com.gametec.Networking.RetrofitClient;
import app.gametec.com.gametec.R;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChangePercentengeFragment extends Fragment {

    EditText edt_percentenge;
    Button btn_percent_update;
    ImageButton back_button;

    public ChangePercentengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_percentenge, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new PercentFragement());
                }
            }
        });


        btn_percent_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_percentenge.getText().toString().length() != 0) {
                    if (getActivity() != null)
                        if (Utility.isInternetAvailable(getActivity())) {
                            Storage storage = new Storage(getActivity());
                            try {
                                if (!(Double.parseDouble(edt_percentenge.getText().toString()) > 100.0)) {
                                    setPercentenge(storage.getAccessType() + " " + storage.getAccessToken(), storage.getCurrentMachine(), Double.parseDouble(edt_percentenge.getText().toString()));
                                } else {
                                    Toast.makeText(getActivity(), R.string.invalid_percentenge, Toast.LENGTH_SHORT).show();
                                }

                            } catch (NumberFormatException e) {
                                Toast.makeText(getActivity(), R.string.invalid_percentenge, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(getActivity(), R.string.percent_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView() {
        View view = getView();
        if (view != null) {
            edt_percentenge = view.findViewById(R.id.edt_percentenge);
            btn_percent_update = view.findViewById(R.id.btn_percent_update);
            back_button = view.findViewById(R.id.back_button);
        }
    }

    private void setPercentenge(String token, int machineID, double percentenge) {
        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> setPercentengeCall = networkInterface.setPercentengeToMachine(token, machineID, percentenge);
        setPercentengeCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                switch (response.code()) {

                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;


                    case 200:
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                boolean isSuccess = jsonObject.getBoolean("success");
                                if (isSuccess) {
                                    Utility.DismissDialog(flower);
                                    String message = jsonObject.getString("message");
                                    Utility.showDialog(getActivity(), message);

                                } else {
                                    Utility.DismissDialog(flower);
                                    JSONArray messageArray = jsonObject.getJSONArray("message");
                                    Utility.showDialog(getActivity(), messageArray.getString(0));
                                }

                            } catch (JSONException e) {
                                Utility.DismissDialog(flower);
                                e.printStackTrace();
                            }

                        } else {
                            Utility.DismissDialog(flower);
                            Toast.makeText(getActivity(), R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 500:
                        Utility.DismissDialog(flower);
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utility.DismissDialog(flower);
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
