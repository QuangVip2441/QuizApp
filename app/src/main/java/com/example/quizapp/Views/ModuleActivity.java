package com.example.quizapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizapp.Models.ModuleModel;
import com.example.quizapp.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {

    private ShapeableImageView buttonQuizModule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        ArrayList<ModuleModel> models = new ArrayList<>();
        models.add(new ModuleModel("gH0DPmPCXqyd2tLvNFo2","IU01","Hiểu biết về CNTT cơ bản",50));
        models.add(new ModuleModel("nhzkAlsrBsEYuMwLt4tX","IU02","Sử dụng máy tính cơ bản",50));
        models.add(new ModuleModel("tKY7UM75QxiaEso7cg1P","IU03","Xử lý văn bản cơ bản",50));
        models.add(new ModuleModel("RIcs3Vkbw67snMZDrQph","IU04","Sử dụng bảng tính cơ bản",50));
        models.add(new ModuleModel("kVXcvqNDESn0XcGFZOwJ","IU05","Sử dụng trình chiếu cơ bản",50));
        models.add(new ModuleModel("yPcUWum0mjuL7mld9Yhy","IU06","Sử dụng Internet cơ bản",50));

        List<Integer> buttonIds = Arrays.asList(
                R.id.buttonQuizModule1,
                R.id.buttonQuizModule2,
                R.id.buttonQuizModule3,
                R.id.buttonQuizModule4,
                R.id.buttonQuizModule5,
                R.id.buttonQuizModule6
        );
        for (Integer buttonId : buttonIds) {
            buttonQuizModule = findViewById(buttonId);
            buttonQuizModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = buttonIds.indexOf(view.getId());
                    if (index >= 0 && index < models.size()) {
                        String moduleID = models.get(index).getId();
                        Intent intent = new Intent(ModuleActivity.this, StartActivity.class);
                        intent.putExtra("Key", moduleID);
                        startActivity(intent);

                    }
                }
            });
        }
    }
}