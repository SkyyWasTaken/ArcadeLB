package com.skyywastaken.arcadelb.leaderboard.format;

import com.skyywastaken.arcadelb.util.ConfigManager;

public enum RowType {
    HEADER(FormatType.CENTERED) {
        @Override
        public int getColor() {
            return ConfigManager.getHeaderColor();
        }
    },
    CURRENT_PLAYER(FormatType.SCORE) {
        @Override
        public int getColor() {
            return ConfigManager.getYourNameColor();
        }
    },
    OTHER_PLAYER(FormatType.SCORE) {
        @Override
        public int getColor() {
            return ConfigManager.getOthersNameColor();
        }
    },
    DIVIDER(FormatType.CENTERED) {
        @Override
        public int getColor() {
            return ConfigManager.getMiscColor();
        }
    },
    INFO(FormatType.CENTERED) {
        @Override
        public int getColor() {
            return ConfigManager.getMiscColor();
        }
    };
    public final FormatType FORMAT_TYPE;

    RowType(FormatType passedFormatType) {
        this.FORMAT_TYPE = passedFormatType;
    }

    public abstract int getColor();
}
