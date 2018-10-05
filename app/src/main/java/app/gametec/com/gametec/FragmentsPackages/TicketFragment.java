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
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Alarm;
import app.gametec.com.gametec.ModelPackages.Ticket;
import app.gametec.com.gametec.ModelPackages.UpdateFeatures;
import app.gametec.com.gametec.Networking.NetworkInterface;
import app.gametec.com.gametec.Networking.RetrofitClient;
import app.gametec.com.gametec.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TicketFragment extends Fragment {

    ImageButton back_button;
    Button updateBtn;
    TextView ticket_total, last_updatetime;


    public TicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        View view = getView();

        if (view != null) {
            back_button = view.findViewById(R.id.back_button);
            ticket_total = view.findViewById(R.id.ticket_total);
            last_updatetime = view.findViewById(R.id.ticket_last_update_time);
            updateBtn = view.findViewById(R.id.ticket_update_btn);
        }


        /*back button*/
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((FragmentContainerActivity) Objects.requireNonNull(getActivity())).FragmentTransition(new AdminFragment());
                }
            }
        });

        TicketCall();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               PostTicketUpdate();
            }
        });
    }

    private void TicketCall() {
        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<Ticket> alarmCall = networkInterface.getTicket(storage.getAccessType() + " " + storage.getAccessToken());
        alarmCall.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {

               Ticket ticket = response.body();

               if(ticket != null){
                   ticket_total.setText(String.valueOf(ticket.getData().getTickets().getMonthTotal()));
                   last_updatetime.setText(ticket.getData().getTickets().getLastUpdate());
               }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {



            }
        });
    }

    private void PostTicketUpdate(){

        Storage storage = new Storage(getActivity());
        NetworkInterface networkInterface = RetrofitClient.getRetrofit().create(NetworkInterface.class);
        Call<UpdateFeatures> updateFeaturesCall = networkInterface.PostTicketUdpate(storage.getAccessType() + " " + storage.getAccessToken());
        updateFeaturesCall.enqueue(new Callback<UpdateFeatures>() {
            @Override
            public void onResponse(Call<UpdateFeatures> call, Response<UpdateFeatures> response) {

                UpdateFeatures features = response.body();

                if(features != null){

                    Toast.makeText(getActivity(), features.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateFeatures> call, Throwable t) {

            }
        });
    }

}

