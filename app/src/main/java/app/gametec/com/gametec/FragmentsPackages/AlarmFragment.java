package app.gametec.com.gametec.FragmentsPackages;


import android.content.Intent;
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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.AdapterPackages.FeatureListAdapter;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Alarm;
import app.gametec.com.gametec.ModelPackages.Features;
import app.gametec.com.gametec.ModelPackages.UpdateFeatures;
import app.gametec.com.gametec.Networking.NetworkInterface;
import app.gametec.com.gametec.Networking.RetrofitClient;
import app.gametec.com.gametec.R;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.argb;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AlarmFragment extends Fragment {


    Button alarmOn, alarmOff;
    private boolean AlarmON = false;
    TextView alarmTime;
    Button updateBtn;
    ImageButton back_button;

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false);
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

        if (Utility.isInternetAvailable(getActivity())) {
            AlarmCall();
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Utility.isInternetAvailable(getActivity())) {
                    PostAlarmUpdate();
                } else {
                    Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void onInit() {

        View view = getView();

        if (view != null) {
            alarmOff = view.findViewById(R.id.AlarmOffButton);
            alarmOn = view.findViewById(R.id.AlarmOnButton);
            back_button = view.findViewById(R.id.back_button);
            alarmTime = view.findViewById(R.id.alarm_last_update_time);
            updateBtn = view.findViewById(R.id.alarm_update_btn);
        }


        /*set status */
        SetStatus();
    }

    private void SetStatus() {
        alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmON = false;
                FocusChangeListener();
            }
        });

        alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmON = true;
                FocusChangeListener();
            }
        });
    }


    private void FocusChangeListener() {

        if (AlarmON) {

            alarmOn.setBackgroundResource(R.drawable.green_btn_bg);
            alarmOff.setBackgroundResource(R.drawable.border_balck);
            alarmOn.setTextColor(Color.WHITE);
            alarmOff.setTextColor(Color.DKGRAY);

        } else {

            alarmOff.setBackgroundResource(R.drawable.red_btn_bg);
            alarmOn.setBackgroundResource(R.drawable.border_balck);
            alarmOff.setTextColor(Color.WHITE);
            alarmOn.setTextColor(Color.DKGRAY);
        }

    }


    private void AlarmCall() {
        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);
        Call<String> alarmCall = networkInterface.getAlarm(storage.getAccessType() + " " + storage.getAccessToken(), storage.getCurrentMachine());
        alarmCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                /*token expiration handling*/

                switch (response.code()){
                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }

                /*end of token expiration */

                if (response.body() != null) {

                    JSONObject jsonObject = null;
                    try {


                        jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (isSuccess) {
                            Gson gson = new Gson();
                            Alarm alarm = gson.fromJson(response.body(), Alarm.class);
                            if (alarm != null) {

                                alarmTime.setText(alarm.getData().getAlarm().getLastUpdate());

                                if (alarm.getData().getAlarm().getStatus() == 1) {

                                    AlarmON = true;

                                    FocusChangeListener();

                                } else if (alarm.getData().getAlarm().getStatus() == 0) {

                                    AlarmON = false;

                                    FocusChangeListener();
                                }
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

    private void PostAlarmUpdate() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));
        int status;

        if (AlarmON) {

            status = 1;

        } else {

            status = 0;
        }

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);

        Call<String> updateFeaturesCall = networkInterface.PostAlarmUpdate(storage.getAccessType() + " " + storage.getAccessToken(), status, storage.getCurrentMachine());
        updateFeaturesCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                /*token expiration handling*/

                switch (response.code()){
                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }

                /*end of token expiration */

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


}
