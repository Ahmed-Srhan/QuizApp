package com.example.quizapp.repository;

import androidx.annotation.NonNull;

import com.example.quizapp.model.QuizListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class QuizListRepository {
    private FirebaseFirestore firebaseFirestore;
    private OnQuizListLoad onQuizListLoad;

    public QuizListRepository(OnQuizListLoad onQuizListLoad) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.onQuizListLoad = onQuizListLoad;
    }

    public void getQuizList() {
        firebaseFirestore.collection("Quiz")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            onQuizListLoad.onLisLoad(task.getResult().toObjects(QuizListModel.class));
                        } else
                            onQuizListLoad.onError(task.getException());

                    }
                });
    }

    public interface OnQuizListLoad {
        void onLisLoad(List<QuizListModel> quizListModels);

        void onError(Exception e);
    }

}
