package com.skyywastaken.arcadelb.stats.game;

public enum FormatType {
    MILLISECOND {
        @Override
        public String formatScore(int scoreToFormat) {
            String paddedString = String.format("%03d", scoreToFormat);
            int decimalPlace = paddedString.length() - 3;
            return paddedString.substring(0, decimalPlace) + "." + paddedString.substring(decimalPlace) + "s";
        }
    }, SECOND {
        @Override
        public String formatScore(int scoreToFormat) {
            return scoreToFormat + "s";
        }
    }, NORMAL {
        @Override
        public String formatScore(int scoreToFormat) {
            return String.valueOf(scoreToFormat);
        }
    };

    abstract public String formatScore(int scoreToFormat);
}
