package com.example.pt16;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BlocAdapter extends RecyclerView.Adapter<BlocAdapter.BlocViewHolder> {
    private ArrayList<Bloc> blocs;
    private int blocLayout;

    BlocAdapter(ArrayList<Bloc> blocs, int blocLayout) {
        this.blocs = blocs;
        this.blocLayout = blocLayout;
    }

    @NonNull
    @Override
    public BlocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(this.blocLayout, parent, false)
        ;
        return new BlocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlocViewHolder holder, int position) {
        Bloc bloc = this.blocs.get(position);

        holder.hourBegin.setText(bloc.getHourBegin());
        holder.temperature.setText(Double.toString(bloc.getTemperature()));
//        if (bloc.isCold()) {
//            holder.icon.setImageResource(R.drawable.cold);
//        } else {
//            holder.icon.setImageResource(R.drawable.hot);
//        }
        new BlocAdapter.ImageLoad(
                "http://openweathermap.org/img/wn/" + bloc.getIcon() + "@2x.png",
                holder.icon
        ).execute();
    }

    @Override
    public int getItemCount() {
        return this.blocs.size();
    }

    static class BlocViewHolder extends RecyclerView.ViewHolder {
        TextView hourBegin;
        TextView temperature;
        ImageView icon;

        BlocViewHolder(@NonNull View itemView) {
            super(itemView);

            this.hourBegin = itemView.findViewById(R.id.hourBegin);
            this.temperature = itemView.findViewById(R.id.temperature);
            this.icon = itemView.findViewById(R.id.icon);
        }
    }

    /**
     * Una classe que descarrega una imatge en segon pla i l'assigna a un ImageView.
     */
    private static class ImageLoad extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private WeakReference<ImageView> icon;

        public ImageLoad(String url, ImageView icon) {
            this.url = url;
            this.icon = new WeakReference<>(icon);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(this.url).openConnection();
                connection.connect();

                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            this.icon.get().setImageBitmap(bitmap);
        }
    }
}
