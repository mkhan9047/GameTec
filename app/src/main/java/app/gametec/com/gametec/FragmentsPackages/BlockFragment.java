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

import java.net.SocketTimeoutException;
import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Alarm;
import app.gametec.com.gametec.ModelPackages.Block;
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
public class BlockFragment extends Fragment {

    Button blockOn, blockOff;
    private boolean blockON = false;
    TextView block_last_update_time;
    Button update_btn;

    ImageButton back_button;

    public BlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_block, container, false);
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


        if (Utility.isInternetAvailable(getContext())) {
            BlockCall();

        } else {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isInternetAvailable(getContext())) {
                    PostBlockUpdate();

                } else {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onInit() {

        View view = getView();

        if (view != null) {
            blockOff = view.findViewById(R.id.blockOffButton);
            blockOn = view.findViewById(R.id.blockOnButton);
            back_button = view.findViewById(R.id.back_button);
            block_last_update_time = view.findViewById(R.id.block_last_update_time);
            update_btn = view.findViewById(R.id.block_update_btn);
        }

        /*default status*/
        blockON = true;
        FocusChangeListener();

        /*set status */
        SetStatus();
    }

    private void SetStatus() {
        blockOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockON = false;
                FocusChangeListener();
            }
        });

        blockOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockON = true;
                FocusChangeListener();
            }
        });
    }


    private void FocusChangeListener() {

        if (blockON) {

            blockOn.setBackgroundResource(R.drawable.green_btn_bg);
            blockOff.setBackgroundResource(R.drawable.border_balck);
            blockOn.setTextColor(Color.WHITE);
            blockOff.setTextColor(Color.DKGRAY);

        } else {

            blockOff.setBackgroundResource(R.drawable.red_btn_bg);
            blockOn.setBackgroundResource(R.drawable.border_balck);
            blockOff.setTextColor(Color.WHITE);
            blockOn.setTextColor(Color.DKGRAY);
        }

    }


    private void BlockCall() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), getString(R.string.loading));
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<Block> alarmCall = networkInterface.getBlock(storage.getAccessType() + " " + storage.getAccessToken());
        alarmCall.enqueue(new Callback<Block>() {
            @Override
            public void onResponse(Call<Block> call, Response<Block> response) {

                /*token expiration handling*/

                switch (response.code()) {
                    case 401:
                        Toast.makeText(getActivity(), R.string.session_expired, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }

                /*end of token expiration */

                Block block = response.body();

                if (block != null) {
                    if (block.getData().getBlockMachine().getStatus() == 1) {
                        blockON = true;
                        FocusChangeListener();
                    } else if (block.getData().getBlockMachine().getStatus() == 0) {
                        blockON = false;
                        FocusChangeListener();
                    }

                    block_last_update_time.setText(block.getData().getBlockMachine().getLastUpdate());
                }

                Utility.DismissDialog(flower, getContext());

            }

            @Override
            public void onFailure(Call<Block> call, Throwable t) {

                Utility.DismissDialog(flower, getActivity());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void PostBlockUpdate() {

        final ACProgressFlower flower = Utility.StartProgressDialog(getContext(), getString(R.string.loading));

        int status;

        if (blockON) {

            status = 1;

        } else {

            status = 0;
        }

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostBlockUpdate(storage.getAccessType() + " " + storage.getAccessToken(), status);

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
