package com.ugtechie.eshamber.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ugtechie.eshamber.R;
import com.ugtechie.eshamber.models.Farm;

public class AvailableFarmsAdapter extends FirestoreRecyclerAdapter<Farm, AvailableFarmsAdapter.AvailableFarmsHolder> {
    private  OnItemClickListener listener;

    public AvailableFarmsAdapter(@NonNull FirestoreRecyclerOptions<Farm> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AvailableFarmsHolder holder, int position, @NonNull Farm model) {
        holder.textViewFarmTitle.setText(model.getFarmName());
        holder.textViewFarmDuration.setText(model.getDuration());
        holder.textViewFarmAmount.setText(model.getFarmAmount());
        holder.textViewFarmRoi.setText(model.getFarmROI());
        //holder.textViewDescription.setText(model.getFarmDescription());
    }

    @NonNull
    @Override
    public AvailableFarmsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_farms_card,parent, false);
        return new AvailableFarmsHolder(v);
    }

    class AvailableFarmsHolder extends RecyclerView.ViewHolder {
        TextView textViewFarmTitle;
        TextView textViewFarmAmount;
        TextView textViewFarmDuration;
        TextView textViewFarmRoi;



        public AvailableFarmsHolder(@NonNull View itemView) {
            super(itemView);
            textViewFarmTitle = itemView.findViewById(R.id.card_farm_title);
            textViewFarmAmount = itemView.findViewById(R.id.card_farm_card_amount);
            textViewFarmDuration = itemView.findViewById(R.id.text_view_card_farm_duration);
            textViewFarmRoi = itemView.findViewById(R.id.text_view_card_roi);
            //textViewDescription = itemView.findViewById(R.id.card_farmDescription);

            //Setting click listener on the farm card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    public interface  OnItemClickListener {
        void  onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
