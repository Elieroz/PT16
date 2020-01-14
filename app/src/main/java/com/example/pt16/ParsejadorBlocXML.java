package com.example.pt16;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

class ParsejadorBlocXML implements ParsejadorBloc {

    @Override
    public ArrayList<Bloc> parseja(String data) throws XmlPullParserException, IOException {
        ArrayList<Bloc> blocs = new ArrayList<>();

        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        // TODO A dos webs posen això.
//        xmlPullParserFactory.setNamespaceAware(true);
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

        xmlPullParser.setInput(new StringReader(data));

        int eventType = xmlPullParser.getEventType();

        String horaInici = null;
        Double temperatura = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tagName = xmlPullParser.getName();
                Log.d("ParsejadorBlocXML", tagName);

                if (tagName.equals("time")) {
                    Log.d("ParsejadorBlocXML", "<time>");

                    // TODO Guardar hores per a saber de quina franja de temps són les tags següents.
                    horaInici = xmlPullParser.getAttributeValue(null, "from");
                } else if (tagName.equals("temperature")) {
                    // TODO Llegir l'atribut value.
                    temperatura = Double.valueOf(
                            xmlPullParser.getAttributeValue(null, "value")
                    );
                } // TODO Altres etiquetes.
            } else if (eventType == XmlPullParser.END_TAG) {
                String tagName = xmlPullParser.getName();

                if (tagName.equals("time")) {
                    Log.d("ParsejadorBlocXML", "</time>");

                    if (horaInici == null || temperatura == null) {
                        // TODO
                        System.exit(666);
                    } else {
                        Log.d("ParsejadorBlocXML", horaInici + " -> " + temperatura);
                        blocs.add(new Bloc(horaInici, temperatura));
                        horaInici = null;
                        temperatura = null;
                    }
                }
            }

            eventType = xmlPullParser.next();
        }

        return blocs;
    }
}