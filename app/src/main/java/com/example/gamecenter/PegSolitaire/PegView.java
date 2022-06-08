package com.example.gamecenter.PegSolitaire;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class PegView extends androidx.appcompat.widget.AppCompatImageView {

    private int row;
    private int col;

    public PegView(Context context, int r, int c) {
        super(context);
        row = r;
        col = c;
    }

    public PegView(Context context, AttributeSet attrs, int r, int c) {
        super(context, attrs);
        row = r;
        col = c;
    }

    public PegView(Context context, AttributeSet attrs, int defStyle, int r, int c) {
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

    public boolean move(PegLayout oldSquare, PegLayout newSquare, PegLayout[][] squares) {

        int newRow = newSquare.getRow();
        int newCol = newSquare.getColumn();

        int oldRow = oldSquare.getRow();
        int oldCol = oldSquare.getColumn();


        if (newSquare.isEmpty()) {
            if (((Math.abs(newRow - oldRow) == 2) && (newCol == oldCol)) ||
                    (Math.abs(newCol - oldCol) == 2) && (newRow == oldRow)) {
                if ((oldCol - newCol == -2) && (!squares[newRow][newCol - 1].isEmpty())) {
                    squares[newRow][newCol - 1].removeAllViews();
                } else if ((oldCol - newCol == 2) && (!squares[newRow][newCol + 1].isEmpty())) {
                    squares[newRow][newCol + 1].removeAllViews();
                } else if ((oldRow - newRow == -2) && (!squares[newRow - 1][newCol].isEmpty())) {
                    squares[newRow - 1][newCol].removeAllViews();
                } else if ((oldRow - newRow == 2) && (!squares[newRow + 1][newCol].isEmpty())) {
                    squares[newRow + 1][newCol].removeAllViews();
                } else {
                    return false;
                }
                oldSquare.removeView(this);
                newSquare.addView(this);
                this.row = newRow;
                this.col = newCol;
                this.setVisibility(View.VISIBLE);
                return true;
            }

        }
        return false;
    }




}
