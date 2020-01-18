package com.example.pt16;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TemperaturesHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyOpenWeather.db";

    TemperaturesHelper(Context context) {
        // TODO Versió? Ni idea de què vol dir.
        super(
                context,
                TemperaturesHelper.DATABASE_NAME,
                null,
                TemperaturesHelper.DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO O sigui, onCreate és per quan
        db.execSQL(SQLContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLContract.DROP_TABLE);
        this.onCreate(db);
    }

//    boolean cityAvailableAndUpToDate(String cityName) {
//        return this.estaCiutatDescarregada(cityName)
//                && this.cityInfoUpToDate(cityName)
//        ;
//    }

    boolean isCityInfoDownloaded(String nomCiutat) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                SQLContract.TABLE_NAME,
                new String[]{SQLContract.COL_NOM_CIUTAT},
                SQLContract.COL_NOM_CIUTAT + " = ?",
                new String[]{nomCiutat},
                null,
                null,
                null
        );

        // TODO dis works?!?!?!?!
        return cursor.moveToNext();
    }

    boolean isCityInfoUpToDate(String cityName) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                SQLContract.TABLE_NAME,
                new String[]{SQLContract.COL_DATA_INICI},
                SQLContract.COL_NOM_CIUTAT + " = ?",
                new String[]{cityName},
                null,
                null,
                SQLContract.COL_DATA_INICI + " ASC"
        );

        if (cursor.moveToNext()) {
            String dataIniciString = cursor.getString(0);
            LocalDateTime dataInici = SQLContract.parseToDate(dataIniciString);

            cursor.close();
            return dataInici.isAfter(LocalDateTime.now().minusMinutes(30));
        } else {
            cursor.close();
            return false;
        }
    }

    void guarda(List<Bloc> blocs) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Bloc bloc : blocs) {
            ContentValues values = new ContentValues();

            values.put(SQLContract.COL_NOM_CIUTAT, bloc.getCityName());
            values.put(SQLContract.COL_DATA_INICI, bloc.getHourBegin());
            values.put(SQLContract.COL_TEMPERATURE, bloc.getTemperature());
            values.put(SQLContract.COL_WEATHER, bloc.getWeather());
            values.put(SQLContract.COL_ICON, bloc.getIcon());

            db.insert(SQLContract.TABLE_NAME, null, values);
        }
    }

//    public List<Temp> llegeix(String nomCiutat) throws ParseException {
//
//        List<Temp> mostrar = new ArrayList<Temp>();
//        Temp ciutat;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String[] projection = {
//                TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT,
//                TemperaturesContract.NOMBRE_COLUMNA_HORES,
//                TemperaturesContract.NOMBRE_COLUMNA_TEMPS
//
//        };
//
//        String selection = TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT + " = ?";
//        String[] selectionArgs = {nomCiutat};
//
//          ...
//
//        return mostrar;
//
//    }

    ArrayList<Bloc> llegeix(String nomCiutat) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
//                SQLContract.COL_NOM_CIUTAT,
                SQLContract.COL_DATA_INICI,
                SQLContract.COL_TEMPERATURE,
                SQLContract.COL_WEATHER,
                SQLContract.COL_ICON
        };

        String selection = SQLContract.COL_NOM_CIUTAT + " = ?";

        String[] selectionArgs = {nomCiutat};

        Cursor cursor = db.query(
                SQLContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<Bloc> results = new ArrayList<>();

        while (cursor.moveToNext()) {
//            String nomCiutat = cursor.getString(1);
            String dataInici = cursor.getString(0);
            Double temperatura = cursor.getDouble(1);
            String weather = cursor.getString(2);
            String icon = cursor.getString(3);

            results.add(new Bloc(
                    nomCiutat,
                    dataInici,
                    temperatura,
                    weather,
                    icon
            ));
        }

        cursor.close();

        return results;
    }

    void deleteNotUpToDate(String cityName) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                SQLContract.TABLE_NAME,
                SQLContract.COL_NOM_CIUTAT + " = ?",
                new String[]{cityName}
        );
    }
}
