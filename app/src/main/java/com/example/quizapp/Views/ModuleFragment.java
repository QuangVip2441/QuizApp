package com.example.quizapp.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.quizapp.Models.ModuleModel;
import com.example.quizapp.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleFragment extends Fragment {

    private ShapeableImageView buttonQuizModule;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module, container, false);

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
            buttonQuizModule = view.findViewById(buttonId);
            buttonQuizModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = buttonIds.indexOf(view.getId());
                    if (index >= 0 && index < models.size()) {
                        String moduleID = models.get(index).getId();
                        StartFragment startFragment = new StartFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Key",moduleID);

                        startFragment.setArguments(bundle);


                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_module, startFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
        return view;
    }
}