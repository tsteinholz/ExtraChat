package com.buuz135.api.blacklist;


public enum WordAction {
    KICK, COLOR, REPLACE, STRIKEOUT;

    public static WordAction fromString(String action) {
        if (action.equalsIgnoreCase(KICK.toString())) {
            return KICK;
        }
        if (action.equalsIgnoreCase(COLOR.toString())) {
            return COLOR;
        }
        if (action.equalsIgnoreCase(REPLACE.toString())) {
            return REPLACE;
        }
        if (action.equalsIgnoreCase(STRIKEOUT.toString())) {
            return STRIKEOUT;
        }
        return null;
    }
}
