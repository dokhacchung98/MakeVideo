package com.khacchung.makevideo.mask;

import java.util.ArrayList;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.mask.FinalMaskBitmap.EFFECT;

public enum THEMES {
    Shine("Shine") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList<>();
            mEffects.add(EFFECT.PIN_WHEEL);
            mEffects.add(EFFECT.SKEW_RIGHT_SPLIT);
            mEffects.add(EFFECT.SKEW_LEFT_SPLIT);
            mEffects.add(EFFECT.SKEW_RIGHT_MEARGE);
            mEffects.add(EFFECT.SKEW_LEFT_MEARGE);
            mEffects.add(EFFECT.FOUR_TRIANGLE);
            mEffects.add(EFFECT.SQUARE_IN);
            mEffects.add(EFFECT.SQUARE_OUT);
            mEffects.add(EFFECT.CIRCLE_LEFT_BOTTOM);
            mEffects.add(EFFECT.CIRCLE_IN);
            mEffects.add(EFFECT.DIAMOND_OUT);
            mEffects.add(EFFECT.HORIZONTAL_COLUMN_DOWNMASK);
            mEffects.add(EFFECT.RECT_RANDOM);
            mEffects.add(EFFECT.CROSS_IN);
            mEffects.add(EFFECT.DIAMOND_IN);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.all_animation_2;
        }
    },
    Love("Love") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList<>();
            mEffects.add(EFFECT.CIRCLE_IN);
            mEffects.add(EFFECT.HORIZONTAL_RECT);
            mEffects.add(EFFECT.HORIZONTAL_COLUMN_DOWNMASK);
            mEffects.add(EFFECT.LEAF);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.love;
        }
    },
    CIRCLE_IN("Circle In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.circle_in;
        }
    },
    CIRCLE_LEFT_BOTTOM("Circle Left Bottom") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_LEFT_BOTTOM);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.circle_left_up;
        }
    },
    CIRCLE_OUT("Circle Out") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_OUT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.circle_out;
        }
    },
    CIRCLE_RIGHT_BOTTOM("Circle Right Bottom") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_RIGHT_BOTTOM);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.circle_right_bottom;
        }
    },
    DIAMOND_IN("Diamond In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.DIAMOND_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.daimond_in;
        }
    },
    DIAMOND_OUT("Diamond out") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.DIAMOND_OUT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.daimond_out;
        }
    },
    ECLIPSE_IN("Eclipse In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.ECLIPSE_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.eclipse_in;
        }
    },
    FOUR_TRIANGLE("Four Triangle") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.FOUR_TRIANGLE);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.four_train;
        }
    },
    OPEN_DOOR("Open Door") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.OPEN_DOOR);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.open_door;
        }
    },
    PIN_WHEEL("Pin Wheel") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.PIN_WHEEL);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pin_wheel;
        }
    },
    RECT_RANDOM("Rect Random") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.RECT_RANDOM);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.rect_rand;
        }
    },
    SKEW_LEFT_MEARGE("Skew Left Mearge") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SKEW_LEFT_MEARGE);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.skew_left_close;
        }
    },
    SKEW_RIGHT_MEARGE("Skew Right Mearge") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SKEW_RIGHT_MEARGE);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.skew_right_open;
        }
    },
    SQUARE_OUT("Square Out") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SQUARE_OUT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.square_out;
        }
    },
    SQUARE_IN("Square In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SQUARE_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.square_in;
        }
    },
    VERTICAL_RECT("Vertical Rect") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.VERTICAL_RECT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.vertical_ran;
        }
    };

    String name;

    public abstract ArrayList<EFFECT> getTheme();

    public abstract ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList);

    public abstract int getThemeDrawable();

    private THEMES(String string) {
        this.name = "";
        this.name = string;
    }

    public String getName() {
        return this.name;
    }
}
