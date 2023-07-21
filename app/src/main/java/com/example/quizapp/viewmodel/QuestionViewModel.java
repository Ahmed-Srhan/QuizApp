package com.example.quizapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quizapp.model.QuestionModel;
import com.example.quizapp.repository.QuestionRepository;

import java.util.HashMap;
import java.util.List;

public class QuestionViewModel extends ViewModel implements QuestionRepository.OnQuestionLoad, QuestionRepository.OnResultSet, QuestionRepository.OnResultLoad {
    private static final String TAG = "QuestionListViewModelError";
    private MutableLiveData<List<QuestionModel>> questionListMutableLiveData;
    private QuestionRepository repository;
    private MutableLiveData<HashMap<String, Long>> resultMutableLiveData;

    public QuestionViewModel() {
        questionListMutableLiveData = new MutableLiveData<>();
        resultMutableLiveData = new MutableLiveData<>();
        repository = new QuestionRepository(this, this, this);
    }

    public MutableLiveData<List<QuestionModel>> getQuestionListMutableLiveData() {
        return questionListMutableLiveData;
    }

    public void setQuizId(String quizId) {
        repository.setQuizId(quizId);
    }

    public void getQuestion() {
        repository.getQuestion();
    }

    public void setResult(HashMap<String, Object> result) {
        repository.setResult(result);
    }

    public void getResult() {
        repository.getResult();
    }

    public MutableLiveData<HashMap<String, Long>> getResultMutableLiveData() {
        return resultMutableLiveData;
    }

    @Override
    public void onLoad(List<QuestionModel> questionModels) {
        questionListMutableLiveData.setValue(questionModels);

    }

    @Override
    public boolean onSubmit() {
        return true;
    }

    @Override
    public void onResultLoad(HashMap<String, Long> result) {
        resultMutableLiveData.setValue(result);

    }

    @Override
    public void onError(Exception e) {
        Log.d(TAG, "onError: " + e.getMessage());
    }
}
