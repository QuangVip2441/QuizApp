package com.example.quizapp.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizapp.R;
import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddModuleFragment extends Fragment {

    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionModule;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_module, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        //deleteAllModules();
        addModule("IU01","Hiểu biết về CNTT cơ bản",50);
        addModule("IU02", "Sử dụng máy tính cơ bản", 50);
        addModule("IU03", "Xử lý văn bản cơ bản", 50);
        addModule("IU04", "Sử dụng bảng tính cơ bản", 50);
        addModule("IU05", "Sử dụng trình chiếu cơ bản", 50);
        addModule("IU06", "Sử dụng Internet cơ bản", 50);
        return view;
    }
    private void deleteModule(String moduleId) {
        mRefCollectionModule.document(moduleId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void deleteAllModules() {
        mRefCollectionModule.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Lặp qua tất cả các tài liệu và xóa chúng
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String moduleId = document.getId();
                            deleteModule(moduleId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting documents: ", e);
                    }
                });
    }

    private void addModule(String name, String introduction, int numberQuestions) {
        Map<String, Object> module = new HashMap<>();
        module.put(Constant.Database.Module.NAME, name);
        module.put(Constant.Database.Module.INTRODUCTION, introduction);
        module.put(Constant.Database.Module.NUMBER_QUESTIONS, numberQuestions);

        mRefCollectionModule = mFirestore.collection(Constant.Database.Module.COLLECTION_MODULE);
        mRefCollectionModule.add(module).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                Map<String, Object> update = new HashMap<>();
                update.put(Constant.Database.Module.ID, id);
                mRefCollectionModule.document(id).update(update);
            }
        });
    }
}