package app.gametec.com.gametec.FragmentsPackages;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        final ACProgressFlower dialog = Utility.StartProgressDialog(getContext(), getString(R.string.loading));
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        final Call<Machine> machineCall = networkInterface.getMachineList(storage.getAccessType() + " " + storage.getAccessToken());
        machineCall.enqueue(new Callback<Machine>() {
            @Override
            public void onResponse(Call<Machine> call, Response<Machine> response) {

                Machine machine = response.body();

                if (machine != null) {

                    MachineListAdapter adapter = new MachineListAdapter(machine.getData().getMachines(), getActivity());
                    machineRecycler.setAdapter(adapter);
                    Utility.DismissDialog(dialog, getActivity());
                }

                Utility.DismissDialog(dialog, getActivity());

            }

            @Override
            public void onFailure(Call<Machine> call, Throwable t) {
                Utility.DismissDialog(dialog, getActivity());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
