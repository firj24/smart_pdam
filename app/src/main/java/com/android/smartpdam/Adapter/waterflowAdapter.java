package com.android.smartpdam.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.smartpdam.R;
import com.android.smartpdam.model.waterflowModel;

import java.util.List;

public class waterflowAdapter extends RecyclerView.Adapter<waterflowAdapter.WaterflowViewHolder> {

    private Context context;
    private List<waterflowModel> waterflowList;

    public waterflowAdapter(Context context, List<waterflowModel> waterflowList) {
        this.context = context;
        this.waterflowList = waterflowList;
    }

    @NonNull
    @Override
    public WaterflowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_waterflow, parent, false);
        return new WaterflowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterflowViewHolder holder, int position) {
        waterflowModel model = waterflowList.get(position);
        holder.textViewNama.setText(model.getNama());
        holder.textViewRate1.setText(String.valueOf(model.getRate1()));
        holder.textViewVolume1.setText(String.valueOf(model.getVolume1()));

    }

    @Override
    public int getItemCount() {
        return waterflowList.size();
    }

    public static class WaterflowViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNama, textViewRate1, textViewVolume1;

        public WaterflowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNama = itemView.findViewById(R.id.textViewNama);
            textViewRate1 = itemView.findViewById(R.id.textViewRate1);
            textViewVolume1 = itemView.findViewById(R.id.textViewVolume1);
        }
    }
}
