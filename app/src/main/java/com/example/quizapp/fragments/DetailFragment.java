package com.example.quizapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.quizapp.databinding.FragmentDetailBinding;
import com.example.quizapp.model.QuizListModel;
import com.example.quizapp.viewmodel.QuizListViewModel;

import java.util.List;


public class DetailFragment extends Fragment {
    private QuizListViewModel viewModel;
    private NavController navController;
    private FragmentDetailBinding binding;
    private int position;
    private Long totalQueCount;
    private String quizId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(QuizListViewModel.class);
        position = DetailFragmentArgs.fromBundle(getArguments()).getPosition();
        viewModel.getQuizList();
        viewModel.getQuizListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                binding.detailFragmentTitle.setText(quizListModels.get(position).getTitle());
                binding.detailFragmentDifficulty.setText(quizListModels.get(position).getDifficulty());
                binding.detailFragmentQuestions.setText(String.valueOf(quizListModels.get(position).getQuestions()));
                binding.detailFragmentTitle.setText(quizListModels.get(position).getTitle());
                Glide.with(view).load(quizListModels.get(position).getImage()).into(binding.detailFragmentImage);
                totalQueCount = quizListModels.get(position).getQuestions();
                quizId = quizListModels.get(position).getQuizId();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.detailProgressBar.setVisibility(View.GONE);
            }
        }, 500);

        binding.startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragmentDirections.ActionDetailFragmentToQuizragment action =
                        DetailFragmentDirections.actionDetailFragmentToQuizragment();
                action.setQuizId(quizId);
                action.setTotalQueCount(totalQueCount);
                navController.navigate(action);
            }
        });


    }
}