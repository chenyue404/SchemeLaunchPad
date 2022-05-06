package com.cy.schemelaunchpad.json;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public class FloatTypeAdapter extends DoubleTypeAdapter {

    @Override
    public Number read(JsonReader in) throws IOException {
        Number number = super.read(in);
        if (number != null) {
            try {
                return number.floatValue();
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return null;
    }
}
