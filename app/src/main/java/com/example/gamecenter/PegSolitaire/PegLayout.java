package com.example.gamecenter.PegSolitaire;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class PegLayout extends LinearLayout {

    private int row;
    private int col;

    public PegLayout(Context context, int r, int c) {
        super(context);
        row = r;
        col = c;
    }

    public PegLayout(Context context, AttributeSet attrs, int r, int c) {
        super(context, attrs);
        row = r;
        col = c;
    }

    public PegLayout(Context context, AttributeSet attrs, int defStyle, int r, int c) {
        super(context, attrs, defStyle);
        row = r;
        col = c;
    }

    public void setRow(int r) {
        row = r;
    }

    public int getRow() {
        return row;
    }

    public void setColumn(int c) {
        col = c;
    }

    public int getColumn() {
        return col;
    }

    public boolean isEmpty() {
        if (this.getChildCount() == 0) {
            return true;
        }
        return false;

    }



}
