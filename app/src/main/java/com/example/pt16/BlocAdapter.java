package com.example.pt16;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BlocAdapter extends RecyclerView.Adapter<BlocAdapter.BlocViewHolder> {
    private ArrayList<Bloc> blocs;

    public BlocAdapter(ArrayList<Bloc> blocs) {
        this.blocs = blocs;
    }

    @NonNull
    @Override
    public BlocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.bloc_layout, parent, false)
        ;
        return new BlocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlocViewHolder holder, int position) {
        Bloc bloc = this.blocs.get(position);

        holder.hourBegin.setText(bloc.getHourBegin());
        holder.temperature.setText(Double.toString(bloc.getTemperature()));
//        holder.icon.setImageResource(bloc.getImageResource());
    }

    @Override
    public int getItemCount() {
        return this.blocs.size();
    }

    public static class BlocViewHolder extends RecyclerView.ViewHolder {
        public TextView hourBegin;
        public TextView temperature;
//        public ImageView icon;

        public BlocViewHolder(@NonNull View itemView) {
            super(itemView);

            this.hourBegin = itemView.findViewById(R.id.hourBegin);
            this.temperature = itemView.findViewById(R.id.temperature);
//            this.icon = itemView.findViewById(R.id.icon);
        }
    }
}
