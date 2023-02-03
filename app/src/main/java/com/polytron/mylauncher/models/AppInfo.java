package com.polytron.mylauncher.models;

import android.graphics.drawable.Drawable;

public class AppInfo {
    CharSequence label;
    CharSequence packageName;
    boolean selected;
    Drawable icon;

    public AppInfo(CharSequence label, CharSequence packageName, Drawable icon, boolean selected) {
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
        this.selected = selected;
    }

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getPackageName() {
        return packageName;
    }

    public void setPackageName(CharSequence packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
