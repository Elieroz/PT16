package com.example.pt16;

class Bloc {
    private static final double COLD_THRESHOLD = 15.0;

    // TODO String? LocalTime?
    private String hourBegin;
    private double temperature;
    // TODO Ho guardem aquí o ho calculem a partir de la temperature quan calqui?
//    private boolean faFred;

    private static double kelvinToCelsius(double temperature) {
        return Math.round((temperature - 273.15) * 100.0) / 100.0;
    }

    /**
     *
     * @param hourBegin
     * @param temperature Temperatura en graus Kelvin.
     */
    Bloc(String hourBegin, double temperature) {
        this.hourBegin = hourBegin;
        this.temperature = Bloc.kelvinToCelsius(temperature);
    }

    String getHourBegin() {
        return hourBegin;
    }

    double getTemperature() {
        return temperature;
    }

    boolean isCold() {
        return this.temperature <= Bloc.COLD_THRESHOLD;
    }
}
