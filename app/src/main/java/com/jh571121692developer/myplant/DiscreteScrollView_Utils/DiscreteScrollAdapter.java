package com.jh571121692developer.myplant.DiscreteScrollView_Utils;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jh571121692developer.myplant.DetailsActivity;
import com.jh571121692developer.myplant.R;
import com.jh571121692developer.myplant.addDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DiscreteScrollAdapter  extends RecyclerView.Adapter<DiscreteScrollAdapter.ViewHolder> {

    private int itemWidth;
    private int itemHeight;
    private ArrayList<DetailData> data;
    private Context context;
    private String detailsKey;

    public DiscreteScrollAdapter(Context context, ArrayList<DetailData> data) {
        this.data = data;
        this.context = context;
        detailsKey = context.getString(R.string.detailsKey);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Activity context = (Activity)recyclerView.getContext();
        Point windowDimensions = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(windowDimensions);
        itemWidth = Math.round(windowDimensions.x * 0.6f);
        itemHeight = Math.round(windowDimensions.y * 0.6f);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_discrete_plant_card, parent, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                itemWidth, itemWidth);
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(position == data.size() - 1){

            holder.addimage.setVisibility(View.VISIBLE);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, addDetailsActivity.class));
                }
            });
        }
        else{
            Glide.with(holder.itemView.getContext())
                    .load(data.get(position).getImageUri())
                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(detailsKey, data.get(position));
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        private View overlay;
        private ImageView image;
        private ImageView addimage;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            overlay = itemView.findViewById(R.id.overlay);
            addimage = itemView.findViewById(R.id.discrete_cardview_addButton);
        }

        public void setOverlayColor(@ColorInt int color) {
            overlay.setBackgroundColor(color);
        }
    }
}
