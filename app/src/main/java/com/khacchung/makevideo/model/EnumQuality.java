package com.khacchung.makevideo.model;

import com.khacchung.makevideo.application.MyApplication;

public enum EnumQuality {

    QUALITY_240P(MyApplication.PX_240P) {
        @Override
        public int getWidth() {
            return 426;
        }

        @Override
        public int getHeight() {
            return 240;
        }
    },
    QUALITY_360P(MyApplication.PX_360P) {
        @Override
        public int getWidth() {
            return 640;
        }

        @Override
        public int getHeight() {
            return 360;
        }
    },
    QUALITY_480P(MyApplication.PX_480P) {
        @Override
        public int getWidth() {
            return 854;
        }

        @Override
        public int getHeight() {
            return 480;
        }
    },
    QUALITY_720P(MyApplication.PX_720P) {
        @Override
        public int getWidth() {
            return 1280;
        }

        @Override
        public int getHeight() {
            return 720;
        }
    };

    private String name;

    EnumQuality(String name) {
        this.name = name;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public static EnumQuality getByName(String name) {
        switch (name) {
            case MyApplication.PX_240P:
                return EnumQuality.QUALITY_240P;
            case MyApplication.PX_360P:
                return EnumQuality.QUALITY_360P;
            case MyApplication.PX_480P:
                return EnumQuality.QUALITY_480P;
            case MyApplication.PX_720P:
                return EnumQuality.QUALITY_720P;
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
