package app.gametec.com.gametec.AdapterPackages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.FragmentsPackages.AdminFragment;
import app.gametec.com.gametec.FragmentsPackages.MachineFragment;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.Machine;
import app.gametec.com.gametec.R;

public class MachineListAdapter extends RecyclerView.Adapter<MachineListAdapter.MachineListHolder> {

    private List<Machine.MachinesItem> machinesItems;
    private Activity context;
    Storage storage;

    public MachineListAdapter(List<Machine.MachinesItem> machinesItems, Activity context) {
        this.machinesItems = machinesItems;
        this.context = context;
        storage = new Storage(context);
    }

    @NonNull
    @Override
    public MachineListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_recyer_row, parent, false);
        return new MachineListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MachineListHolder holder, int position) {

        holder.button.setText(machinesItems.get(position).getName());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("id_is = ",  String.valueOf(machinesItems.get(holder.getAdapterPosition()).getId()));
                storage.saveCurrentMachine(machinesItems.get(holder.getAdapterPosition()).getId());
                ((FragmentContainerActivity) context).FragmentTransition(new AdminFragment());

            }
        });
    }

    @Override
    public int getItemCount() {
        return machinesItems.size();
    }

    class MachineListHolder extends RecyclerView.ViewHolder {
        Button button;

        public MachineListHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.feature_btn);

        }


    }
}
