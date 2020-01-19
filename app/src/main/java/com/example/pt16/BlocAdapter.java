package com.example.pt16;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlocAdapter extends RecyclerView.Adapter<BlocAdapter.BlocViewHolder> {
    private ArrayList<Bloc> blocs;
    private int blocLayout;
    private TemperatureUnit temperatureUnit;

    enum TemperatureUnit {
        Celsius,
        Kelvin,
        Fahrenheit,
    }

    private static double roundToTwoXifres(double num) {
        return Math.round(num * 100.0) / 100.0;
    }

    private static double kelvinToCelsius(double temperature) {
        return BlocAdapter.roundToTwoXifres(temperature - 273.15);
    }

    private static double kelvinToFahrenheit(double temperature) {
        return BlocAdapter.roundToTwoXifres(9.0/5.0 * (temperature - 273.15) + 32);
    }

    BlocAdapter(ArrayList<Bloc> blocs, int blocLayout, TemperatureUnit temperatureUnit) {
        this.blocs = blocs;
        this.blocLayout = blocLayout;
        this.temperatureUnit = temperatureUnit;
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

        double temperature = bloc.getTemperature();
        String unit = " K";
        if (this.temperatureUnit == TemperatureUnit.Celsius) {
            temperature = BlocAdapter.kelvinToCelsius(temperature);
            unit = " ºC";
        } else if (this.temperatureUnit == TemperatureUnit.Fahrenheit) {
            temperature = BlocAdapter.kelvinToFahrenheit(temperature);
            unit = " F";
        }

        holder.temperature.setText(
                String.format("%s%s", temperature, unit)
        );

        holder.hourBegin.setText(bloc.getHourBegin());

        if (position == 0 || bloc.isStartOfNewDay()) {
            holder.day.setText(bloc.getDay());
        } else {
            // Resulta que si no es fa això no s'esborra el text quan es fa scroll... He llegit que
            // té a veure amb com el RecyclerView recicla elements i tal.
            holder.day.setText("");
        }

        Picasso.get()
                .load(
                "http://openweathermap.org/img/wn/"
                    + bloc.getWeatherIcon()
                    + "@2x.png"
                )
                .into(holder.weatherIcon)
        ;
    }

    @Override
    public int getItemCount() {
        return this.blocs.size();
    }

    static class BlocViewHolder extends RecyclerView.ViewHolder {
        TextView hourBegin;
        TextView day;
        TextView temperature;
        ImageView weatherIcon;

        BlocViewHolder(@NonNull View itemView) {
            super(itemView);

            this.hourBegin = itemView.findViewById(R.id.hourBegin);
            this.day = itemView.findViewById(R.id.day);
            this.temperature = itemView.findViewById(R.id.temperature);
            this.weatherIcon = itemView.findViewById(R.id.weatherIcon);
        }
    }
}
