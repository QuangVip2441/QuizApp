package com.example.quizapp.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.MainActivity;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email, edt_password;
    private TextView txt_forgotpassword;
    private Button btn_login;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        txt_forgotpassword = findViewById(R.id.txt_forgotpassword);
        btn_login = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String filteredInput = filterSpecialCharacters(input);
                if (!input.equals(filteredInput)) {
                    // Replace the input text with filtered text
                    edt_password.setText(filteredInput);
                    // Move the cursor to the end of the filtered text
                    edt_password.setSelection(filteredInput.length());
                }
            }
        });
    }

    private String filterSpecialCharacters(String input) {
        StringBuilder filteredInput = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                // Keep letters, digits, and whitespaces
                filteredInput.append(c);
            }
        }
        return filteredInput.toString();
    }

    private void loginUser(){
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edt_email.setError("Email rỗng");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edt_password.setError("Mật khẩu rỗng");
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //String userID = mAuth.getCurrentUser().getUid();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //intent.putExtra("userID", userID);
                    startActivity(intent);
                }else
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
            }
        });
    }
}