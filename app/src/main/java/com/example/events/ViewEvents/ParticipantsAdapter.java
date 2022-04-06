package com.example.events.ViewEvents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.events.Components.C;
import com.example.events.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {

    private final FirebaseFirestore db;
    ParticipantList pList;

    public ParticipantsAdapter(ParticipantList pList) {
        this.pList = pList;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public ParticipantsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.empty_text_view, viewGroup, false);
            return new ParticipantsAdapter.ViewHolder(v);
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.joined_event_participant_list_element, viewGroup, false);
        return new ParticipantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParticipantsAdapter.ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == 1) {
            Participants p = pList.get(position);
            db.collection(C.users)
                    .document(p.name).get().addOnCompleteListener(task -> {
                DocumentSnapshot snapshot = task.getResult();
                viewHolder.name.setText(snapshot.getString("name"));
            });
            viewHolder.role.setText(p.role);

            viewHolder.view.setOnClickListener(v -> {
                if (p.role.equals("p")) {

                }
            });
        } else if (viewHolder.getItemViewType() == 0) {
            if (position > 0) {
                ((TextView) viewHolder.view).setText("Participant");
            }
        }
    }


    @Override
    public int getItemCount() {
        return pList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (pList.get(position).isTitle) ? 0 : 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView role;
        View view;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            role = view.findViewById(R.id.role);
            this.view = view;
        }
    }
}
