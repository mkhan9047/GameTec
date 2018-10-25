package app.gametec.com.gametec.AdapterPackages;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ModelPackages.FeaturesItem;
import app.gametec.com.gametec.R;

public class FeatureListAdapter extends RecyclerView.Adapter<FeatureListAdapter.FeatureHolder> {

    private List<FeaturesItem> featuresItems;
    private Activity context;
    Fragment fragment;

    public FeatureListAdapter(List<FeaturesItem> featuresItems, Activity context) {
        this.featuresItems = featuresItems;
        this.context = context;
    }

    @NonNull
    @Override
    public FeatureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_recyer_row, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_out_recycler_row, parent, false);
        }

        return new FeatureHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureHolder holder, int position) {
        holder.button.setText(featuresItems.get(position).getName());
    }

    @Override
    public int getItemCount() {

        return featuresItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (featuresItems.get(position).getSlug().equals("logout")) {
            return 1;
        } else {
            return 2;
        }


    }

    class FeatureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button button;

        public FeatureHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.feature_btn);
            itemView.setOnClickListener(this);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FragmentContainerActivity.class);
                    intent.putExtra("flag", featuresItems.get(getAdapterPosition()).getSlug());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    context.overridePendingTransition( R.anim.left_to_right, R.anim.right_to_left );
                }
            });
        }

        @Override
        public void onClick(View view) {


        }
    }
}
