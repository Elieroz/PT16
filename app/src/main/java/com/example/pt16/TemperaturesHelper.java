package com.example.pt16;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TemperaturesHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "CREATE TABLE horesTemperatures (TODO)";
    private static final String INSERT_TEMPERATURA_HORA = "INSERT INTO horesTemperatures ...";

    TemperaturesHelper(Context context) {
        // TODO Versió? Ni idea de què vol dir.
        super(context, "temperatures", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TemperaturesHelper.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    boolean estaCiutatDescarregada(String nomCiutat) {
        return false;
    }

    void guarda(String nomCiutat, List<Bloc> blocs) {

    }

    public List<Bloc> llegeix(String nomCiutat) {
        return new ArrayList<>();
    }
}
