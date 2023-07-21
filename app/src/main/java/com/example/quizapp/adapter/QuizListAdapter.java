package com.example.quizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizapp.databinding.EachQuizBinding;
import com.example.quizapp.model.QuizListModel;

import java.util.ArrayList;
import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {
    private List<QuizListModel> quizListModels = new ArrayList<>();

    private Context context;
    private OnItemQuizListClick onItemQuizListClick;

    public QuizListAdapter(Context context, OnItemQuizListClick onItemQuizListClick) {
        this.context = context;
        this.onItemQuizListClick = onItemQuizListClick;
    }

    public void setQuizListModels(List<QuizListModel> quizListModels) {
        this.quizListModels = quizListModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EachQuizBinding binding = EachQuizBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuizListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListViewHolder holder, int position) {
        QuizListModel model = quizListModels.get(position);
        holder.binding.quizTitleList.setText(model.getTitle());
        Glide.with(context).load(model.getImage()).into(holder.binding.quizImageList);

    }

    @Override
    public int getItemCount() {
        return quizListModels.size();
    }

    public interface OnItemQuizListClick {
        void onItemClick(int position);
    }

    class QuizListViewHolder extends RecyclerView.ViewHolder {
        EachQuizBinding binding;

        public QuizListViewHolder(@NonNull EachQuizBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemQuizListClick.onItemClick(getAdapterPosition());
                }
            });
        }
    }


}
