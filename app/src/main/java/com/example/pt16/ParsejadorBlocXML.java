package com.example.pt16;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

class ParsejadorBlocXML implements ParsejadorBloc {

    private class BlocXML {
        String dateBegin;
        Double temperature;
        String weatherName;
        String weatherIcon;

        BlocXML() {
            this.dateBegin = null;
            this.temperature = null;
            this.weatherName = null;
            this.weatherIcon = null;
        }

        boolean validFields() {
            return this.dateBegin != null
                    && this.temperature != null
                    && this.weatherName != null
                    && this.weatherIcon != null
            ;
        }
    }

    @Override
    public ArrayList<Bloc> parseja(String cityName, String data) throws XmlPullParserException, IOException {
        ArrayList<Bloc> blocs = new ArrayList<>();

        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        // TODO A dos webs posen això.
//        xmlPullParserFactory.setNamespaceAware(true);
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

        xmlPullParser.setInput(new StringReader(data));

        int eventType = xmlPullParser.getEventType();

        BlocXML blocXML = new BlocXML();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tagName = xmlPullParser.getName();
                Log.d("ParsejadorBlocXML", tagName);

                switch (tagName) {
                    case "time":
                        Log.d("ParsejadorBlocXML", "<time>");

                        // Amb el format XML, la data ve amb una T entre la data i el temps. Per a
                        // facilitar-ne el parseig la substitueixo amb un espai, que es el que hi
                        // ha quan es fa servir el format JSON.
                        blocXML.dateBegin = xmlPullParser.getAttributeValue(null, "from")
                            .replace("T", " ")
                        ;
                        break;
                    case "temperature":
                        blocXML.temperature = Double.valueOf(
                                xmlPullParser.getAttributeValue(null, "value")
                        );
                        break;
                    case "symbol":
                        blocXML.weatherName = xmlPullParser.getAttributeValue(null, "name");
                        blocXML.weatherIcon = xmlPullParser.getAttributeValue(null, "var");
                        break;
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                String tagName = xmlPullParser.getName();

                if (tagName.equals("time")) {
                    Log.d("ParsejadorBlocXML", "</time>");

                    if (blocXML.validFields()) {
                        Log.d("ParsejadorBlocXML", blocXML.dateBegin + " -> " + blocXML.temperature);
                        blocs.add(new Bloc(
                                cityName,
                                blocXML.dateBegin,
                                blocXML.temperature,
                                blocXML.weatherName,
                                blocXML.weatherIcon
                        ));

                    } else {
                        Log.d("ParsejadorBlocXML", "Error parsing <time> - skipping");
                    }

                    blocXML = new BlocXML();
                }
            }

            eventType = xmlPullParser.next();
        }

        return blocs;
    }
}