package com.example.quizapp.fragments;

import android.os.Bundle;
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

import com.example.quizapp.R;
import com.example.quizapp.databinding.FragmentResultBinding;
import com.example.quizapp.viewmodel.QuestionViewModel;

import java.util.HashMap;


public class ResultFragment extends Fragment {
    private QuestionViewModel viewModel;
    private NavController navController;
    private String quizId;
    private FragmentResultBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        navController = Navigation.findNavController(view);
        quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();

        viewModel.setQuizId(quizId);
        viewModel.getResult();
        viewModel.getResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                Long correctAns = stringLongHashMap.get("correctAnswer");
                Long notAns = stringLongHashMap.get("noAnswer");
                Long wrongAns = stringLongHashMap.get("wrongAnswer");
                binding.correctAnswerTv.setText(correctAns.toString());
                binding.notAnsweredTv.setText(notAns.toString());
                binding.wrongAnswersTv.setText(wrongAns.toString());
                Long total = correctAns + wrongAns + notAns;
                Long percent = (correctAns * 100) / total;
                binding.resultPercentageTv.setText(percent.toString());
                binding.resultCountProgressBar.setProgress(percent.intValue());


            }
        });
        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_resultFragment_to_listFragment);
            }
        });
    }
}