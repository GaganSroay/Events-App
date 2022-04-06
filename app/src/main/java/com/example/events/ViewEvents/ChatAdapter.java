package com.example.events.ViewEvents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Message> messages;
    private final HashMap<String, String> userNames;
    private final FirebaseVolleyRequest userNameRequest;

    public ChatAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        userNames = new HashMap<>();

        userNameRequest = new FirebaseVolleyRequest(context, "user/get_user");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_bubble_left, viewGroup, false);
            return new LeftChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_bubble_right, viewGroup, false);
            return new RightChatViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == 0) {
            LeftChatViewHolder leftHolder = (LeftChatViewHolder) viewHolder;
            leftHolder.messageView.setText(messages.get(position).message);
            if (!isItMe(position)) {
                HashMap<String, String> user_id = new HashMap<>();
                user_id.put("user_id", messages.get(position).getUid());
                userNameRequest.makePostRequest(user_id, new FirebaseVolleyRequest.GetResult() {
                    @Override
                    public void onResult(String result) {
                        try {
                            JSONObject res = new JSONObject(result);
                            String name = res.getString("name");
                            leftHolder.nameView.setText(name);
                            leftHolder.nameView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
            }
        } else if (viewHolder.getItemViewType() == 1) {
            RightChatViewHolder rightHolder = (RightChatViewHolder) viewHolder;
            rightHolder.messageView.setText(messages.get(position).message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewType(position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private boolean isItMe(int pos) {
        return messages.get(pos).me;
    }

    private int viewType(int pos) {
        return (isItMe(pos)) ? 1 : 0;
    }

    public static class LeftChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageView;
        TextView nameView;

        public LeftChatViewHolder(View view) {
            super(view);
            messageView = view.findViewById(R.id.text);
            nameView = view.findViewById(R.id.name);
            nameView.setVisibility(View.INVISIBLE);
        }
    }

    public static class RightChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageView;

        public RightChatViewHolder(View view) {
            super(view);
            messageView = view.findViewById(R.id.text);
        }
    }
}


