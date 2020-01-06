package com.example.pt16;

public class Bloc {
    // TODO String? LocalTime?
    private String hourBegin;
    private double temperature;
    // TODO Ho guardem aquí o ho calculem a partir de la temperature quan calqui?
//    private boolean faFred;

    public Bloc(String hourBegin, double temperature) {
        this.hourBegin = hourBegin;
        this.temperature = temperature;
    }

    public String getHourBegin() {
        return hourBegin;
    }

    public double getTemperature() {
        return temperature;
    }
}
