package app.gametec.com.gametec.FragmentsPackages;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import app.gametec.com.gametec.AdapterPackages.MachineListAdapter;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
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
public class MachineFragment extends Fragment {

    RecyclerView machineRecycler;

    public MachineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_machine, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        /*recycler view builder*/
        recyclerViewBuilder();

        /*network call here*/
        getMachineList();
    }

    private void initView() {
        View view = getView();
        if (view != null) {
            machineRecycler = view.findViewById(R.id.machine_recycler_view);
        }


    }

    private void recyclerViewBuilder() {
        machineRecycler.setHasFixedSize(true);
        machineRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /*get machine list */

    private void getMachineList() {

        final ACProgressFlower dialog = Utility.StartProgressDialog(getActivity(), getString(R.string.loading));

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);

        final Call<String> machineCall = networkInterface.getMachineList(storage.getAccessType() + " " + storage.getAccessToken());
        machineCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body() != null) {

                    JSONObject jsonObject = null;
                    try {


                        jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (isSuccess) {
                            Gson gson = new Gson();
                            Machine machine = gson.fromJson(response.body(), Machine.class);
                            MachineListAdapter adapter = new MachineListAdapter(machine.getData().getMachines(), getActivity());
                            machineRecycler.setAdapter(adapter);
                            Utility.DismissDialog(dialog);

                        } else {

                            Utility.showDialog(getActivity(), message);
                            Utility.DismissDialog(dialog);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                Utility.DismissDialog(dialog);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Activity activiy = getActivity();
                if (activiy != null && isAdded()) {
                    Utility.DismissDialog(dialog);
                    if (t instanceof SocketTimeoutException) {
                        Utility.DismissDialog(dialog);
                        Toast.makeText(activiy, getString(R.string.connection_timeout), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}
