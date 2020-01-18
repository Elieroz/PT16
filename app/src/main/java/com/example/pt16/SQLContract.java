package com.example.pt16;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SQLContract {
    public static final String TABLE_NAME = "hores_temperatures";

    public static final String COL_NOM_CIUTAT = "nom_ciutat";
    public static final String COL_DATA_INICI = "data_inici";
    public static final String COL_TEMPERATURE = "temperature";
    public static final String COL_WEATHER = "weather";
    public static final String COL_ICON = "icon";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ("
                // TODO Guardem la ciutat a cada bloc, o fem una taula per a cada ciutat?
                + COL_NOM_CIUTAT + " VARCHAR(20), "
                // TODO No veig cap getDate(..) a Cursor... I com que es poden guardar
                //  es guarden en format ordenable (any-mes-dia-hora-minut-segon),
                //  pos les guardaré com a String i ja les parsejo després.
                + COL_DATA_INICI + " VARCHAR(19), "
                + COL_TEMPERATURE + " NUMBER, "
                // TODO És sempre de 3 caràcters?
                + COL_WEATHER + " VARHCAR(20), "
                + COL_ICON + " VARCHAR(3), "

                + " CONSTRAINT PK_" + TABLE_NAME + " PRIMARY KEY (" + COL_NOM_CIUTAT + ", " + COL_DATA_INICI + ")"
        + ");"
    ;

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";"
    ;

    // TODO Clar, per a mirar si cal tornar a descarregar no necessitem l'última
    //  data_inici, no? Perquè potser estem a les 17:00 i l'última que es té és
    //  de les 18:00, però només hi ha 18:00 i 21:00 perquè la p
    public static final String QUERY_SELECT_EARLIEST_CITY_DATA_INICI
        = "SELECT " + COL_DATA_INICI
            + " FROM " + TABLE_NAME
            + " WHERE " + COL_NOM_CIUTAT + " = ?"
            + " ORDER BY " + COL_DATA_INICI + " ASC"
            + " LIMIT 1"
            + ";"
    ;

    public static final String QUERY_SELECT_ALL_BY_CITY =
        "SELECT *"
            + " FROM " + SQLContract.TABLE_NAME
            + " WHERE " + SQLContract.COL_NOM_CIUTAT + " = ?"
            + " ORDER BY " + SQLContract.COL_DATA_INICI + " ASC"
            + ";"
    ;

    private static final DateTimeFormatter DATE_TIME_FORMATTER
        = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
    ;

    static LocalDateTime parseToDate(String dateString) {
        return LocalDateTime.parse(dateString, SQLContract.DATE_TIME_FORMATTER);
    }
}
