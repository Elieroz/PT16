package com.example.pt16;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public interface ParsejadorBloc {
    ArrayList<Bloc> parseja(String data) throws XmlPullParserException, IOException;
}
