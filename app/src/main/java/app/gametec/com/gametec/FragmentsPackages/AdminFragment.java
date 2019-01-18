package app.gametec.com.gametec.FragmentsPackages;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.AdapterPackages.FeatureListAdapter;
import app.gametec.com.gametec.AdapterPackages.MachineListAdapter;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Features;
import app.gametec.com.gametec.ModelPackages.Machine;
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
public class AdminFragment extends Fragment {


    RecyclerView featureRecycler;
    FeatureListAdapter adapter;
    Fragment fragment;
    TextView role, machine_name;
    ImageButton machine_reload, networkSwitch;


    public AdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();


        Storage storage = new Storage(getActivity());


        if (view != null) {

            networkSwitch = view.findViewById(R.id.change_network_type);
            role = view.findViewById(R.id.role);
            featureRecycler = view.findViewById(R.id.feature_recylerview);
            machine_reload = view.findViewById(R.id.machine_reload);
            machine_name = view.findViewById(R.id.machine_name);

        }


        machine_name.setText(storage.getCurrentMachineName().toUpperCase());

        networkSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if (activity != null) {
                    ((FragmentContainerActivity) activity).FragmentTransition(new ConnectBluetoothFragment());
                }
            }
        });

        machine_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if (activity != null) {
                    ((FragmentContainerActivity) activity).FragmentTransition(new MachineFragment());
                }

            }
        });


        role.setText(storage.getRole());

        featureRecycler.setHasFixedSize(true);
        featureRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        if (Utility.isInternetAvailable(getContext())) {

            FeaturesCall();

        } else {

            Utility.ShowToast(getContext(), "No internet!");
        }

        //  ButtonClickListener();

    }

    /*private void ButtonClickListener() {
        balance.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                fragment = new BalanceFragment();
                ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new AlarmFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }
            }
        });

        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new TicketFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }
            }
        });

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Not done yet!", Toast.LENGTH_SHORT).show();
            }
        });
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ClockFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }
            }
        });

        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new DoorFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new BlockFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }
            }
        });

        percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new PercentFragement();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new ResetFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }

            }
        });

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new BluetoothFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(fragment);
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Toast.makeText(getActivity(), "User Signed Out!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }
*/

    private void FeaturesCall() {

        final ACProgressFlower dialog = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));

        final Storage storage = new Storage(getContext());

        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);

        Call<String> featuresCall = networkInterface.getFeatures(storage.getAccessType() + " " + storage.getAccessToken());

        featuresCall.enqueue(new Callback<String>() {
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

                /*end of token expiration */



         /*       if (response.body() != null) {



                    if (features.isSuccess()) {
                        adapter = new FeatureListAdapter(features.getData().getFeatures(), getActivity());

                        Utility.DismissDialog(dialog);

                        featureRecycler.setAdapter(adapter);
                    } else {

                        Utility.DismissDialog(dialog);
                        Toast.makeText(getActivity(), features.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }*/

                if (response.body() != null) {

                    JSONObject jsonObject = null;
                    try {


                        jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (isSuccess) {
                            Gson gson = new Gson();
                            Features features = gson.fromJson(response.body(), Features.class);
                            FeatureListAdapter adapter =new FeatureListAdapter(features.getData().getFeatures(), getActivity());
                            featureRecycler.setAdapter(adapter);
                            Utility.DismissDialog(dialog);

                        } else {

                            Utility.showDialog(getActivity(), message);
                            Utility.DismissDialog(dialog);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

}

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Utility.DismissDialog(dialog);
                if(t instanceof SocketTimeoutException){

                    if(getActivity()!= null && isAdded())
                    Utility.DismissDialog(dialog);
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();

                }

            }


        });

    }

}