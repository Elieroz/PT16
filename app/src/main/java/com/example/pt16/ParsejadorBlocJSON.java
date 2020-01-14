package com.example.pt16;

import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParsejadorBlocJSON implements ParsejadorBloc {

    public static class BlocListJSON {
        public List<BlocJSON> list;
    }

    public static class BlocJSON {
        public String dt_txt;
        public TempJSON main;
    }

    public static class TempJSON {
        public double temp;
    }

    @Override
    public ArrayList<Bloc> parseja(String data) throws XmlPullParserException, IOException {
        ArrayList<Bloc> blocs = new ArrayList<>();

        Gson gson = new Gson();
        BlocListJSON blocListJSON = gson.fromJson(data, BlocListJSON.class);

        for (BlocJSON blocJSON : blocListJSON.list) {
            Bloc bloc = new Bloc(blocJSON.dt_txt, blocJSON.main.temp);
            blocs.add(bloc);
        }

        return blocs;
    }
}
