package com.azhar.komik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.azhar.komik.R;
import com.azhar.komik.model.ModelChapter;

import java.util.List;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

public class AdapterChapter extends RecyclerView.Adapter<AdapterChapter.ViewHolder> {

    private List<ModelChapter> items;
    private AdapterChapter.onSelectData onSelectData;
    private Context mContext;

    public interface onSelectData {
        void onSelected(ModelChapter modelChapter);
    }

    public AdapterChapter(Context context, List<ModelChapter> items, AdapterChapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelChapter data = items.get(position);

        holder.btnChapter.setText(data.getChapterTitle());
        holder.btnChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectData.onSelected(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llChapter;
        public Button btnChapter;

        public ViewHolder(View itemView) {
            super(itemView);
            llChapter = itemView.findViewById(R.id.llChapter);
            btnChapter = itemView.findViewById(R.id.btnChapter);
        }
    }
}
