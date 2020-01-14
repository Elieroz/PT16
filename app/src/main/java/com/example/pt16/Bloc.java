package com.example.pt16;

class Bloc {
    private static final double COLD_THRESHOLD = 15.0;

    // TODO String? LocalTime?
    private String hourBegin;
    private double temperature;
    // TODO Ho guardem aquí o ho calculem a partir de la temperature quan calqui?
//    private boolean faFred;
    private String weather;
    private String icon;

    private static double kelvinToCelsius(double temperature) {
        return Math.round((temperature - 273.15) * 100.0) / 100.0;
    }

    /**
     * @param hourBegin
     * @param temperature Temperatura en graus Kelvin.
     */
    Bloc(String hourBegin, double temperature, String weather, String icon) {
        this.hourBegin = hourBegin;
        this.temperature = Bloc.kelvinToCelsius(temperature);
        this.weather = weather;
        this.icon = icon;
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

//    int weatherToColor() {
//        switch (this.weather) {
//            case "Clouds":
//                return Color.GRAY;
//            case "Rain":
//                return Color.CYAN;
//            case "Snow":
//                return Color.LTGRAY;
//            case ""
//        }
//    }
}
