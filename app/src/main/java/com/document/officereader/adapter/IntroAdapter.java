package com.document.officereader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.document.officereader.R;
import com.document.officereader.model.Guid;

import java.util.ArrayList;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.IntroAdapterHolder> {

    private ArrayList<Guid> mHelpGuid;
    Context context;

    public IntroAdapter(ArrayList<Guid> mHelpGuid,Context context) {
        this.mHelpGuid = mHelpGuid;
        this.context = context;
    }

    @NonNull
    @Override
    public IntroAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guid, parent, false);
        return new IntroAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroAdapterHolder holder, int i) {
       Guid helpGuidModel = mHelpGuid.get(i);
        Glide.with(context).load(helpGuidModel.getImg()).into(holder.img_guide);
        holder.tv_title.setText(helpGuidModel.getTitle());
        holder.tv_contnet.setText(helpGuidModel.getContent());

    }

    @Override
    public int getItemCount() {
        if (mHelpGuid != null) {
            return mHelpGuid.size();
        } else {
            return 0;
        }
    }

    public class IntroAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView img_guide;
        private TextView tv_title,tv_contnet;


        public IntroAdapterHolder(@NonNull View itemView) {
            super(itemView);
            img_guide = itemView.findViewById(R.id.img_guide);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_contnet = itemView.findViewById(R.id.tv_content);

        }
    }
}
