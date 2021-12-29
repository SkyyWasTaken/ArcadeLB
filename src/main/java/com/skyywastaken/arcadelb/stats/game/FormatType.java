package com.skyywastaken.arcadelb.stats.game;

import java.text.NumberFormat;

public enum FormatType {
    MILLISECOND {
        @Override
        public String formatScore(int scoreToFormat) {
            return NumberFormat.getInstance().format(scoreToFormat / 1000f) + "s";
        }
    }, SECOND {
        @Override
        public String formatScore(int scoreToFormat) {
            return NumberFormat.getInstance().format(scoreToFormat) + "s";
        }
    }, NORMAL {
        @Override
        public String formatScore(int scoreToFormat) {
            return NumberFormat.getInstance().format(scoreToFormat);
        }
    };

    abstract public String formatScore(int scoreToFormat);
}
