package com.goldenapp.questionhub;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class FirebaseRepository {

    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Query quizRef = firebaseFirestore.collection("QuizList").whereEqualTo("visibility", "public");

    public FirebaseRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getQuizData() {
        quizRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirestoreTaskComplete.quizListDataAdded(task.getResult().toObjects(QuizListModel.class));
            } else {
                onFirestoreTaskComplete.onError(task.getException());
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void quizListDataAdded(List<QuizListModel> quizListModelsList);
        void onError(Exception e);
    }

}
