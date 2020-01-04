package com.example.pt16;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {
    private List<String> values;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeader;
        TextView txtFooter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtHeader = itemView.findViewById(R.id.firstLine);
            this.txtFooter = itemView.findViewById(R.id.secondLine);
        }
    }

    public MyAdapter(List<String> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.example_item, parent, false);

        MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(v);

        Log.d("test", "onCreateViewHolder");

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final String name = this.values.get(position);

        // TODO yolo. Sense el casteig no compila... Copiat malament?
        ((ViewHolder)holder).txtHeader.setText("?" + name);
//        holder.txtHeader.setOnClickListener();

        ((ViewHolder)holder).txtFooter.setText("Footer: " + name);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
