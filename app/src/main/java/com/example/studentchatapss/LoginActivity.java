package com.example.studentchatapss;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private Button loginButton,phoneloginbutton;
    private EditText userEmail,userPassword;
    private TextView needNewAccountLink, forgotPasswordLink;
    public static final String GROUP_CHAT_PREFS = "GROUP_CHAT_PREFS";
    private FirebaseAuth mAuth;
    public static final String TERMS = "TERMS";
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        InitializeFields();
        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUsertoLogin();
            }
        });
    }

    private void AllowUsertoLogin()
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
        else
        {
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                SendUserToVMainActivity();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }
    }

    private void InitializeFields()
    {
        loginButton = (Button) findViewById(R.id.login_button);

        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        needNewAccountLink = (TextView) findViewById(R.id.new_account_link);

    }
    protected void onStart() {
        super.onStart();

        if (currentUser!=null)
        {
            SendUserToVMainActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences =  getSharedPreferences(GROUP_CHAT_PREFS,Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean(TERMS,false)) {


            TermsandConditonsDialogFragment ts = new TermsandConditonsDialogFragment();
            Dialog dialog = ts.onCreateDialog(savedInstanceState);
            dialog.show();
        }

    }

    public void SendUserToGroupActivity()
    {
        Intent loginIntent = new Intent(LoginActivity.this,GroupChatActivity.class);
        startActivity(loginIntent);
    }

    public void SendUserToRegisterActivity()
    {
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }


    public void SendUserToVMainActivity()
    {
        Intent LoginIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(LoginIntent);
    }

    public class TermsandConditonsDialogFragment extends DialogFragment
    {



        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Thanks for downloading or app!Our app uses MySQL and Firbase for data storage. Please ackowledgee this information to continue using the app")
                    .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = LoginActivity.this.getSharedPreferences(GROUP_CHAT_PREFS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putBoolean(TERMS, true);
                            edit.commit();
                        }
                    });
            return builder.create();
        }
    }

}
