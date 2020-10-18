package crazy.developer.landlord_sgp_project.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import crazy.developer.landlord_sgp_project.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    TextInputLayout editTextEmail, editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(MainActivity.this);


        editTextEmail =  findViewById(R.id.edit_text_Email);
        editTextPassword =  findViewById(R.id.edit_text_Password);


        findViewById(R.id.text_view_Register).setOnClickListener(this);
        findViewById(R.id.button_Login).setOnClickListener(this);
    }

    private void userLogin() {

        showProgressBar();
//        Intent intent=new Intent(MainActivity.this,DashboardActivity.class);
//        startActivity(intent);

        String email = editTextEmail.getEditText().getText().toString().trim();
        String password = editTextPassword.getEditText().getText().toString().trim();



        if (email.isEmpty()) {
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length 6");
            editTextPassword.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });

    }


        @Override
        public void onClick (View view){
            switch (view.getId()) {
                case R.id.text_view_Register:
                    finish();
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    break;

                case R.id.button_Login:
                    userLogin();
                    break;
            }
        }

    public static void showProgressBar() {
        progressDialog.show();

        progressDialog.setContentView(R.layout.progress_dialog);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }


}