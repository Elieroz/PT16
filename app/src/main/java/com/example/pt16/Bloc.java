package com.example.pt16;

class Bloc {
    private static final double COLD_THRESHOLD = 15.0;

    private String cityName;
    // TODO String? LocalTime?
    private String hourBegin;
    private double temperature;
    // TODO Ho guardem aquí o ho calculem a partir de la temperature quan calgui?
//    private boolean faFred;
    private String weather;
    private String icon;

    /**
     * @param hourBegin
     * @param temperature Temperatura en graus Kelvin.
     */
    Bloc(String cityName, String hourBegin, double temperature, String weather, String icon) {
        this.cityName = cityName;
        this.hourBegin = hourBegin;
        this.temperature = temperature;
        this.weather = weather;
        this.icon = icon;
    }

    public String getCityName() {
        return this.cityName;
    }

    String getHourBegin() {
        return this.hourBegin;
    }

    double getTemperature() {
        return this.temperature;
    }

    public String getWeather() {
        return this.weather;
    }

    public String getIcon() {
        return this.icon;
    }

    boolean isCold() {
        return this.temperature <= Bloc.COLD_THRESHOLD;
    }
}
