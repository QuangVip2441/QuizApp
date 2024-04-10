package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quizapp.Models.ChoiceModel;
import com.example.quizapp.Models.ModuleModel;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;
import com.example.quizapp.adapters.ModuleSpinnerAdapter;
import com.example.quizapp.ultils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddQuestionFragment extends Fragment {


    private Spinner spinnerModule;
    private TextInputEditText editContent;
    private TextInputEditText editAnswerA;
    private TextInputEditText editAnswerB;
    private TextInputEditText editAnswerC;
    private TextInputEditText editAnswerD;
    private RadioButton radioA;
    private RadioButton radioB;
    private RadioButton radioC;
    private RadioButton radioD;
    private FloatingActionButton buttonSave;
    private ModuleModel mSelectedModule;
    private QuestionModel mQuestion;
    private ArrayList<ModuleModel> mModules;
    // Firebase
    private FirebaseFirestore mFirestore;
    private CollectionReference mRefCollectionModule;
    private DocumentReference mRefDocModule;
    private CollectionReference mRefCollectionQuestion;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_question, container, false);
        spinnerModule = view.findViewById(R.id.spinnerModule);
        editContent = view.findViewById(R.id.editContent);
        editAnswerA = view.findViewById(R.id.editAnswerA);
        editAnswerB = view.findViewById(R.id.editAnswerB);
        editAnswerC = view.findViewById(R.id.editAnswerC);
        editAnswerD = view.findViewById(R.id.editAnswerD);
        radioA = view.findViewById(R.id.radioA);
        radioB = view.findViewById(R.id.radioB);
        radioC = view.findViewById(R.id.radioC);
        radioD= view.findViewById(R.id.radioD);
        buttonSave = view.findViewById(R.id.buttonSave);

        mFirestore = FirebaseFirestore.getInstance();
        mModules = new ArrayList<>();

        loadModules();

        // Listeners
        spinnerModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedModule = (ModuleModel) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFieldsValid() == true) {
                    addQuestion();
                    clearData();
                    refreshFragment();
                }else
                    Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private boolean isFieldsValid() {
        String content = editContent.getText().toString().trim();
        String answerA = editAnswerA.getText().toString().trim();
        String answerB = editAnswerB.getText().toString().trim();
        String answerC = editAnswerC.getText().toString().trim();
        String answerD = editAnswerD.getText().toString().trim();

        if (content.isEmpty() || answerA.isEmpty() || answerB.isEmpty() || answerC.isEmpty() || answerD.isEmpty()) {
            return false;
        }
        if (!radioA.isChecked() && !radioB.isChecked() && !radioC.isChecked() && !radioD.isChecked()) {
            return false;
        }

        return true;
    }

    private void clearData() {
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(editContent); // Thêm tất cả các EditText của bạn vào danh sách
        editTextList.add(editAnswerA);
        editTextList.add(editAnswerB);
        editTextList.add(editAnswerC);
        editTextList.add(editAnswerD);

        // Đặt null cho tất cả EditText trong danh sách
        for (EditText editText : editTextList) {
            editText.setText(null);
        }

        radioA.setChecked(false);
        radioB.setChecked(false);
        radioC.setChecked(false);
        radioD.setChecked(false);
    }
    public void refreshFragment() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }
    private void loadModules() {
        mRefCollectionModule = mFirestore
                .collection(Constant.Database.Module.COLLECTION_MODULE);
        mRefCollectionModule
                .orderBy(Constant.Database.Module.NAME, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mModules.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                ModuleModel module = new ModuleModel(
                                        (String) data.get(Constant.Database.Module.ID),
                                        (String) data.get(Constant.Database.Module.NAME),
                                        (String) data.get(Constant.Database.Module.INTRODUCTION),
                                        (Long) data.get(Constant.Database.Module.NUMBER_QUESTIONS)
                                );
                                mModules.add(module);
                            }

                            if (mModules != null) {
                                ModuleSpinnerAdapter adapter = new ModuleSpinnerAdapter(
                                        getActivity(),
                                        R.layout.layout_item_module_spinner,
                                        mModules
                                );
                                spinnerModule.setAdapter(adapter);
                            }
                        }
                    }
                });
    }

    private void addQuestion() {
        String content = editContent.getText().toString();
        ArrayList<ChoiceModel> choices = new ArrayList<>();
        choices.add(new ChoiceModel("A", editAnswerA.getText().toString()));
        choices.add(new ChoiceModel("B", editAnswerB.getText().toString()));
        choices.add(new ChoiceModel("C", editAnswerC.getText().toString()));
        choices.add(new ChoiceModel("D", editAnswerD.getText().toString()));
        mQuestion = new QuestionModel(
                content,
                choices,
                checkChoices()
        );

        HashMap<String, Object> map = new HashMap<>();
        map.put(Constant.Database.Question.CONTENT, mQuestion.getContent());
        map.put(Constant.Database.Question.CHOICES, mQuestion.getChoices());
        map.put(Constant.Database.Question.CORRECT, mQuestion.getCorrect());

        mRefDocModule = mFirestore
                .collection(Constant.Database.Module.COLLECTION_MODULE)
                .document(mSelectedModule.getId());
        mRefCollectionQuestion = mRefDocModule.collection(Constant.Database.Question.COLLECTION_QUESTIONS);

        mRefCollectionQuestion.add(mQuestion)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        Map<String, Object> update = new HashMap<>();
                        update.put(Constant.Database.Module.ID, id);
                        mRefCollectionQuestion.document(id).update(update);
                    }
                });
    }

    private String checkChoices() {
        if (radioA.isChecked()) {
            return "A";
        } else if (radioB.isChecked()) {
            return "B";
        } else if (radioC.isChecked()) {
            return "C";
        } else {
            return "D";
        }
    }
}