package com.ugtechie.eshamber.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.squareup.picasso.Picasso;
import com.ugtechie.eshamber.R;
import com.ugtechie.eshamber.models.Farm;

import java.text.DecimalFormat;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FarmsAdapter extends RecyclerView.Adapter<FarmsAdapter.viewHolder> {
    private List<Farm> AvailableFarmsList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    //Adapter Constructor
    public FarmsAdapter(List<Farm> availableFarmsList) {
        AvailableFarmsList = availableFarmsList;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView textViewFarmTitle;
        TextView textViewFarmAmount;
        // TextView textViewFarmDuration;
        TextView textViewFarmRoi;
        LoaderImageView imageViewCardFarmImage;

        public viewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            textViewFarmTitle = itemView.findViewById(R.id.text_view_card_row_farm_title);
            imageViewCardFarmImage = itemView.findViewById(R.id.image_view_card_row_farm_image);
            textViewFarmAmount = itemView.findViewById(R.id.text_view_card_row_farm_Amount);
            // textViewFarmDuration = itemView.findViewById(R.id.text_view_card_farm_duration);
            textViewFarmRoi = itemView.findViewById(R.id.text_view_card_row_farm_roi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_farm_card_row, parent, false);
        viewHolder vh = new viewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Farm availableFarms = AvailableFarmsList.get(position);

        //Setting thousands number format
        DecimalFormat decim = new DecimalFormat("#,###.##");

        try {
            holder.textViewFarmTitle.setText(availableFarms.getFarmName());
            //holder.textViewFarmAmount.setText(availableFarms.getFarmAmount());
            holder.textViewFarmAmount.setText("UGX " + decim.format(Integer.parseInt(availableFarms.getFarmAmount())));
            holder.textViewFarmRoi.setText(availableFarms.getFarmROI() + "%");
            // holder.textViewFarmDuration.setText(availableFarms.getDuration());
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }

        try {
            if (availableFarms.getFarmImageUrl().isEmpty())
                holder.imageViewCardFarmImage.setImageResource(R.drawable.farm_image_placeholder);
            else
                Picasso.get().load(Uri.parse(availableFarms.getFarmImageUrl())).into(holder.imageViewCardFarmImage);
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
            holder.imageViewCardFarmImage.setImageResource(R.drawable.farm_image_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return AvailableFarmsList.size();
    }

}
