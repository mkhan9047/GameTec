package app.gametec.com.gametec.FragmentsPackages;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.AdapterPackages.FeatureListAdapter;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Features;
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
    TextView role;


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

      /*  if (view != null) {

            balance = view.findViewById(R.id.btn_balance);
            alarm = view.findViewById(R.id.btn_alarm);
            ticket = view.findViewById(R.id.btn_ticket);
            gps = view.findViewById(R.id.btn_gps);
            clock = view.findViewById(R.id.btn_clock);
            door = view.findViewById(R.id.btn_door);
            block = view.findViewById(R.id.btn_block);
            percent = view.findViewById(R.id.btn_percent);
            reset = view.findViewById(R.id.btn_reset);
            bluetooth = view.findViewById(R.id.btn_bluetooth);
            signOut = view.findViewById(R.id.btn_sign_out);

        }
*/

        if (view != null) {

            role = view.findViewById(R.id.role);
            featureRecycler = view.findViewById(R.id.feature_recylerview);

        }


        Storage storage = new Storage(getActivity());
        role.setText(storage.getRole());

        featureRecycler.setHasFixedSize(true);
        featureRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        FeaturesCall();
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

        final ACProgressFlower dialog = Utility.StartProgressDialog(getContext(), "Loading...");

        Storage storage = new Storage(getContext());

        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);

        Call<Features> featuresCall = networkInterface.getFeatures(storage.getAccessType() + " " + storage.getAccessToken());

        featuresCall.enqueue(new Callback<Features>() {
            @Override
            public void onResponse(Call<Features> call, Response<Features> response) {

                Features features = response.body();

                if (features != null) {


                    adapter = new FeatureListAdapter(features.getData().getFeatures(), getActivity());
                    Utility.DismissDialog(dialog, getActivity());

                    featureRecycler.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<Features> call, Throwable t) {
                Utility.DismissDialog(dialog, getActivity());
            }
        });
    }
}
