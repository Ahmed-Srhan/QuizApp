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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizapp.adapter.QuizListAdapter;
import com.example.quizapp.databinding.FragmentListBinding;
import com.example.quizapp.model.QuizListModel;
import com.example.quizapp.viewmodel.QuizListViewModel;

import java.util.List;


public class ListFragment extends Fragment implements QuizListAdapter.OnItemQuizListClick {
    private QuizListViewModel viewModel;
    private NavController navController;
    private FragmentListBinding binding;
    private QuizListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(QuizListViewModel.class);
        adapter = new QuizListAdapter(getContext(), this);
        binding.listQuizRecyclerview.setHasFixedSize(true);
        binding.listQuizRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listQuizRecyclerview.setAdapter(adapter);
        viewModel.getQuizList();
        viewModel.getQuizListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                adapter.setQuizListModels(quizListModels);
                binding.quizListProgressbar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        ListFragmentDirections.ActionListFragmentToDetailFragment action =
                ListFragmentDirections.actionListFragmentToDetailFragment();
        action.setPosition(position);
        navController.navigate(action);
    }
}