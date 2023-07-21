package com.example.quizapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quizapp.model.QuizListModel;
import com.example.quizapp.repository.QuizListRepository;

import java.util.List;

public class QuizListViewModel extends ViewModel implements QuizListRepository.OnQuizListLoad {
    private static final String TAG = "QuizListViewModelError";
    private QuizListRepository repository;
    private MutableLiveData<List<QuizListModel>> quizListMutableLiveData;

    public QuizListViewModel() {
        repository = new QuizListRepository(this);
        quizListMutableLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<QuizListModel>> getQuizListMutableLiveData() {
        return quizListMutableLiveData;
    }

    public void getQuizList() {
        repository.getQuizList();
    }

    @Override
    public void onLisLoad(List<QuizListModel> quizListModels) {
        quizListMutableLiveData.setValue(quizListModels);
    }

    @Override
    public void onError(Exception e) {
        Log.d(TAG, "onError: " + e.getMessage());

    }
}
