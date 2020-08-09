package com.azhar.komik.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.SCROLLBARS_OUTSIDE_OVERLAY;

abstract class BaseLayoutMargin extends RecyclerView.ItemDecoration {

    private final MarginDelegate marginDelegate;
    private final int spanCount;
    private final int spacing;
    private OnClickLayoutMarginItemListener listener;

    BaseLayoutMargin(int spanCount, @Px int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.marginDelegate = new MarginDelegate(spanCount, spacing);
    }

    void setOnClickLayoutMarginItemListener(OnClickLayoutMarginItemListener listener) {
        this.listener = listener;
    }

    void setPadding(RecyclerView rv, @Px int margin) {
        this.setPadding(rv, margin, margin, margin, margin);
    }

    void setPadding(RecyclerView rv,
                    @Px int top,
                    @Px int bottom,
                    @Px int left,
                    @Px int right) {
        rv.setClipToPadding(false);
        rv.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        rv.setPadding(left, top, right, bottom);
    }

    MarginDelegate getMarginDelegate() {
        return marginDelegate;
    }

    void calculateMargin(Rect outRect, int position, int spanCurrent, int itemCount,
                         int orientation, boolean isReverse, boolean isRTL) {
        marginDelegate.calculateMargin(outRect, position, spanCurrent, itemCount, orientation, isReverse, isRTL);
    }

    public int getSpacing() {
        return spacing;
    }

    public int getSpanCount() {
        return spanCount;
    }

    void setupClickLayoutMarginItem(Context context,
                                    View view,
                                    int position,
                                    int spanCurrent,
                                    RecyclerView.State state) {
        if (listener != null)
            view.setOnClickListener(onClickItem(context, view, position, spanCurrent, state));
    }

    @NonNull
    private View.OnClickListener onClickItem(final Context context, final View view,
                                             final int position, final int currentSpan, final RecyclerView.State state) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(context, view, position, currentSpan, state);
            }
        };
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

}
