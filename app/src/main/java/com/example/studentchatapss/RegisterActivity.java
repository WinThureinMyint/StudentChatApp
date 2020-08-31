package com.example.studentchatapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText userEmail,userPassword;
    private TextView alreadyHaveAccountLink;

   // private DatabaseReference RootRef;

    private FirebaseAuth mAuth;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mAuth = FirebaseAuth.getInstance();
      //  RootRef = FirebaseDatabase.getInstance().getReference();
        InitializeFields();

        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount()
    {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }

        if(   (email.endsWith("@qmail.cuny.edu")) ||
                (email.endsWith("@lagcc.cuny.edu")) ||
                (email.endsWith("@bmcc.cuny.edu")) ||
                (email.endsWith("@citymail.cuny.edu")) ||
                (email.endsWith("@jj.cuny.edu")) ||
                (email.endsWith("@citytech.cuny.edu")) ||
                (email.endsWith("@lehman.cuny.edu")) ||
                (email.endsWith("@student.qcc.cuny.edu")) ||
                (email.endsWith("@myhunter.cuny.edu")) ||
                (email.endsWith("@bcmail.cuny.edu")) ||
                (email.endsWith("@bcc.cuny.edu")) ||
                (email.endsWith("@hostos.cuny.edu")) ||
                (email.endsWith("@cix.csicuny.edu")) ||
                (email.endsWith("@kbcc.cuny.edu")) ||
                (email.endsWith("@my.guttman.cuny.edu")) ||
                (email.endsWith("@yorkmail.cuny.edu"))

        )
        {
            loadingBar.setAccessibilityPaneTitle("Creating new account");
            loadingBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                SendUserToLoginActivity();
                                Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.setVisibility(View.GONE);
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.setVisibility(View.GONE) ;
                            }
                        }
                    });

        }

        else
        {
            Toast.makeText(this, "Email not a valid CUNY email", Toast.LENGTH_SHORT).show();

        }
    }

    private void InitializeFields()
    {

        createAccountButton = (Button) findViewById(R.id.register_button);
        userEmail = (EditText) findViewById(R.id.register_email);
        userPassword = (EditText) findViewById(R.id.register_password);
        alreadyHaveAccountLink = (TextView) findViewById(R.id.already_have_account);
    }

    public void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }

    /*
        public void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
     */
}
