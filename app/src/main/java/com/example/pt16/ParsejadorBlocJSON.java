package com.example.pt16;

import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParsejadorBlocJSON implements ParsejadorBloc {

    private class BlocListJSON {
        List<BlocJSON> list;
    }

    private class BlocJSON {
        String dt_txt;
        TempJSON main;
        List<WeatherJSON> weather;
    }

    private class TempJSON {
        double temp;
    }

    private class WeatherJSON {
        String main;
        String description;
        String icon;
    }

    @Override
    public ArrayList<Bloc> parseja(String data) throws XmlPullParserException, IOException {
        ArrayList<Bloc> blocs = new ArrayList<>();

        Gson gson = new Gson();
        BlocListJSON blocListJSON = gson.fromJson(data, BlocListJSON.class);

        for (BlocJSON blocJSON : blocListJSON.list) {
            Bloc bloc = new Bloc(blocJSON.dt_txt, blocJSON.main.temp, blocJSON.weather.get(0).main, blocJSON.weather.get(0).icon);
            blocs.add(bloc);
        }

        return blocs;
    }
}
