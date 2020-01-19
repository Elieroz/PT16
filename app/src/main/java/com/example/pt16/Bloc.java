package com.example.pt16;

import java.time.format.DateTimeFormatter;

class Bloc {
    private String cityName;
    private String dateBegin;
    private double temperature;
    private String weatherName;
    private String weatherIcon;

    private static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM HH:mm");
    private static DateTimeFormatter DTF_HOUR = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter DTF_DAY = DateTimeFormatter.ofPattern("dd/MM");

    /**
     * @param temperature Temperatura en graus Kelvin.
     */
    Bloc(String cityName, String dateBegin, double temperature, String weatherName, String weatherIcon) {
        this.cityName = cityName;
        this.dateBegin = dateBegin;
        this.temperature = temperature;
        this.weatherName = weatherName;
        this.weatherIcon = weatherIcon;
    }

    String getCityName() {
        return this.cityName;
    }

    String getDateBegin() {
        return this.dateBegin;
    }

//    String getDateBeginFormatted() {
//        return Bloc.DTF.format(BlocSQLiteContract.parseToDate(this.dateBegin));
//    }

    String getHourBegin() {
        return Bloc.DTF_HOUR.format(BlocSQLiteContract.parseToDate(this.dateBegin));
    }

    String getDay() {
        return Bloc.DTF_DAY.format(BlocSQLiteContract.parseToDate(this.dateBegin));
    }

    double getTemperature() {
        return this.temperature;
    }

    String getWeatherName() {
        return this.weatherName;
    }

    String getWeatherIcon() {
        return this.weatherIcon;
    }

    boolean isStartOfNewDay() {
        return this.dateBegin.endsWith("00:00:00");
    }
}
