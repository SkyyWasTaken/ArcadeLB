package com.skyywastaken.arcadelb.leaderboard.format;

import com.skyywastaken.arcadelb.util.ConfigManager;

public enum RowType {
    HEADER(RowFormatType.CENTERED) {
        @Override
        public int getColor() {
            return ConfigManager.getHeaderColor();
        }
    },
    CURRENT_PLAYER(RowFormatType.SCORE) {
        @Override
        public int getColor() {
            return ConfigManager.getYourNameColor();
        }
    },
    OTHER_PLAYER(RowFormatType.SCORE) {
        @Override
        public int getColor() {
            return ConfigManager.getOthersNameColor();
        }
    },
    DIVIDER(RowFormatType.CENTERED) {
        @Override
        public int getColor() {
            return ConfigManager.getMiscColor();
        }
    },
    INFO(RowFormatType.CENTERED) {
        @Override
        public int getColor() {
            return ConfigManager.getMiscColor();
        }
    };
    public final RowFormatType FORMAT_TYPE;

    RowType(RowFormatType passedFormatType) {
        this.FORMAT_TYPE = passedFormatType;
    }

    public abstract int getColor();
}
