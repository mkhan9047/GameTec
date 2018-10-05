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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_recyer_row, parent, false);
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


    class FeatureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button button;

        public FeatureHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.feature_btn);
            itemView.setOnClickListener(this);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, featuresItems.get(getAdapterPosition()).getSlug()
                            , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, FragmentContainerActivity.class);
                    intent.putExtra("flag", featuresItems.get(getAdapterPosition()).getSlug());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {



        }
    }
}
