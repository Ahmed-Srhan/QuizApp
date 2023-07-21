package com.example.quizapp.repository;

import androidx.annotation.NonNull;

import com.example.quizapp.model.QuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class QuestionRepository {
    private FirebaseFirestore firebaseFirestore;
    private String quizId;
    private OnQuestionLoad onQuestionLoad;
    private OnResultSet onResultSet;
    private OnResultLoad onResultLoad;
    private HashMap<String, Long> result;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ;

    public QuestionRepository(OnQuestionLoad onQuestionLoad, OnResultSet onResultSet, OnResultLoad onResultLoad) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        result = new HashMap<>();
        this.onQuestionLoad = onQuestionLoad;
        this.onResultSet = onResultSet;
        this.onResultLoad = onResultLoad;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void setResult(HashMap<String, Object> result) {
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("result").document(currentUser)
                .set(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onResultSet.onSubmit();

                        } else {
                            onResultSet.onError(task.getException());
                        }
                    }
                });
    }

    public void getResult() {
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("result").document(currentUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            result.put("correctAnswer", task.getResult().getLong("correctAnswer"));
                            result.put("noAnswer", task.getResult().getLong("noAnswer"));
                            result.put("wrongAnswer", task.getResult().getLong("wrongAnswer"));

                            onResultLoad.onResultLoad(result);
                        } else
                            onResultLoad.onError(task.getException());

                    }
                });
    }

    public void getQuestion() {
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            onQuestionLoad.onLoad(task.getResult().toObjects(QuestionModel.class));
                        } else
                            onQuestionLoad.onError(task.getException());

                    }
                });
    }

    public interface OnQuestionLoad {
        void onLoad(List<QuestionModel> questionModels);

        void onError(Exception e);
    }

    public interface OnResultSet {
        boolean onSubmit();

        void onError(Exception e);
    }

    public interface OnResultLoad {
        void onResultLoad(HashMap<String, Long> result);

        void onError(Exception e);
    }
}
