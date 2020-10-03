package com.kjstudios.soundrecorder.show_recorded_audio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kjstudios.soundrecorder.R;

import java.util.ArrayList;

public class ShowRecordingsAdapter extends RecyclerView.Adapter<ShowRecordingsAdapter.MyViewHolder> {

    itemClicked itemClicked;
    action action;

    ArrayList<SavedRecodingModel> recordingsList;

    public ShowRecordingsAdapter(ArrayList<SavedRecodingModel> recordingsList, itemClicked itemClicked,
                                 action action) {
        this.recordingsList = recordingsList;
        this.itemClicked = itemClicked;
        this.action = action;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_recording_rv_itemview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.recordingName.setText(recordingsList.get(position).getRecordingName());
        holder.recordingDuration.setText(recordingsList.get(position).getDuration());
        holder.recordingTime.setText(recordingsList.get(position).getLastModified());
        holder.recordingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked.itemclicked(recordingsList.get(position).recordingName);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.action_clicked(recordingsList.get(position).recordingName, position, v.getId());
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.action_clicked(recordingsList.get(position).recordingName, position, v.getId());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.action_clicked(recordingsList.get(position).recordingName, position, v.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return recordingsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView recordingName, recordingDuration, recordingTime;
        ImageView edit, share, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recordingName = itemView.findViewById(R.id.recordingName);
            recordingDuration = itemView.findViewById(R.id.tv_recordingDuration);
            recordingTime = itemView.findViewById(R.id.recordingTime);

            edit = itemView.findViewById(R.id.edit);
            share = itemView.findViewById(R.id.share);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface itemClicked {
        public void itemclicked(String audioName);
    }

    public interface action {
        public void action_clicked(String audioName, int position, int id);
    }
}

