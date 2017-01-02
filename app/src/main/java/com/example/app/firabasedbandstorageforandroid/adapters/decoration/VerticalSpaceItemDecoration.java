package com.example.app.firabasedbandstorageforandroid.adapters.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tolik on 31.12.2016.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int verticalSpaceHeight;
    private int verticalSpaceFromFirstRow;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, int verticalSpaceFromFirstRow) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.verticalSpaceFromFirstRow = verticalSpaceFromFirstRow;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = verticalSpaceFromFirstRow;
        }
        outRect.bottom = verticalSpaceHeight;
    }
}
