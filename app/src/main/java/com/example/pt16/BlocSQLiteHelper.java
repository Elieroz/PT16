package com.example.pt16;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlocSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyOpenWeather.db";

    BlocSQLiteHelper(Context context) {
        super(
                context,
                BlocSQLiteHelper.DATABASE_NAME,
                null,
                BlocSQLiteHelper.DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BlocSQLiteContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BlocSQLiteContract.DROP_TABLE);
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
                BlocSQLiteContract.TABLE_NAME,
                new String[]{BlocSQLiteContract.COL_NOM_CIUTAT},
                BlocSQLiteContract.COL_NOM_CIUTAT + " = ?",
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

        String[] projection = {BlocSQLiteContract.COL_DATA_INICI};
        String selection = BlocSQLiteContract.COL_NOM_CIUTAT + " = ?";
        String[] selectionArgs = {cityName};
        String orderBy = BlocSQLiteContract.COL_DATA_INICI + " ASC";

        Cursor cursor = db.query(
                BlocSQLiteContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );

        if (cursor.moveToNext()) {
            String dataIniciString = cursor.getString(0);
            LocalDateTime dataInici = BlocSQLiteContract.parseToDate(dataIniciString);

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

            values.put(BlocSQLiteContract.COL_NOM_CIUTAT, bloc.getCityName());
            values.put(BlocSQLiteContract.COL_DATA_INICI, bloc.getDateBegin());
            values.put(BlocSQLiteContract.COL_TEMPERATURE, bloc.getTemperature());
            values.put(BlocSQLiteContract.COL_WEATHER, bloc.getWeatherName());
            values.put(BlocSQLiteContract.COL_ICON, bloc.getWeatherIcon());

            db.insert(BlocSQLiteContract.TABLE_NAME, null, values);
        }
    }

    ArrayList<Bloc> llegeix(String nomCiutat) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                BlocSQLiteContract.COL_DATA_INICI,
                BlocSQLiteContract.COL_TEMPERATURE,
                BlocSQLiteContract.COL_WEATHER,
                BlocSQLiteContract.COL_ICON
        };

        String selection = BlocSQLiteContract.COL_NOM_CIUTAT + " = ?";

        String[] selectionArgs = {nomCiutat};

        Cursor cursor = db.query(
                BlocSQLiteContract.TABLE_NAME,
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
            double temperatura = cursor.getDouble(1);
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
                BlocSQLiteContract.TABLE_NAME,
                BlocSQLiteContract.COL_NOM_CIUTAT + " = ?",
                new String[]{cityName}
        );
    }
}
