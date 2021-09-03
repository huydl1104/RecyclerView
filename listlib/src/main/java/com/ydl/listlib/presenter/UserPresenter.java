package com.ydl.listlib.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ydl.listlib.R;
import com.ydl.listlib.base.RecyclerViewPresenter;
import com.ydl.listlib.bean.User;
import com.ydl.listlib.extension.PopupConstant;

import java.util.ArrayList;
import java.util.List;


public class UserPresenter extends RecyclerViewPresenter<User> {
    private String TAG = "UserPresenter";
    private Adapter adapter;
    public UserPresenter(Context context) {
        super(context);
    }

    @Override
    public PopupConstant getConstant() {
        PopupConstant dims = new PopupConstant();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    public void onQuery(@Nullable CharSequence query) {
        List<User> all = User.USERS;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<User> list = new ArrayList<>();
            for (User u : all) {
                if (u.getFullname().toLowerCase().contains(query) ||
                        u.getUsername().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e(TAG, "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<User> data;

        protected class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;
            private TextView username;
            Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = itemView.findViewById(R.id.fullname);
                username = itemView.findViewById(R.id.username);
            }
        }

        protected void setData(@Nullable List<User> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("No user here!");
                holder.username.setText("Sorry!");
                holder.root.setOnClickListener(null);
                return;
            }
            final User user = data.get(position);
            holder.fullname.setText(user.getFullname());
            holder.username.setText("@" + user.getUsername());
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(user);
                }
            });
        }
    }
}
