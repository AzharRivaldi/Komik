package com.azhar.komik.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azhar.komik.R;
import com.azhar.komik.model.ModelKomik;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

public class AdapterKomik extends RecyclerView.Adapter<AdapterKomik.ViewHolder> {

    private List<ModelKomik> items;
    private AdapterKomik.onSelectData onSelectData;
    private Context mContext;

    public interface onSelectData {
        void onSelected(ModelKomik modelKomik);
    }

    public AdapterKomik(Context context, List<ModelKomik> items, AdapterKomik.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_komik, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelKomik data = items.get(position);

        if (data.getType().equals("Manhua"))
            holder.tvType.setTextColor(Color.parseColor("#ff27AE60"));
        else if (data.getType().equals("Manhwa"))
            holder.tvType.setTextColor(Color.parseColor("#ffF2994A"));
        else if (data.getType().equals("Manga"))
            holder.tvType.setTextColor(Color.parseColor("#ffE8505B"));

        Glide.with(mContext)
                .load(data.getThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.imgPhoto);

        holder.tvTitle.setText(data.getTitle());
        holder.tvType.setText(data.getType());
        holder.tvDate.setText(data.getUpdated());
        holder.cvTerbaru.setOnClickListener(new View.OnClickListener() {
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

        public TextView tvTitle;
        public TextView tvType;
        public TextView tvDate;
        public CardView cvTerbaru;
        public ImageView imgPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            cvTerbaru = itemView.findViewById(R.id.cvTerbaru);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
