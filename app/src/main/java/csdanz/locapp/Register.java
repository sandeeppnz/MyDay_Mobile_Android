package csdanz.locapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener  {

    Button btnLogin;
    EditText etName, etUsername, etPassword;

    private ProgressDialog progressBar;

//    private FirebaseAuth firebaseAuth;

//    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        progressBar = new ProgressDialog(this);

//        firebaseAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference();


        //etName = (EditText) findViewById(R.id.etName);


        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);

    }

    private void SaveUserInfo()
    {
        String email = etUsername.getText().toString().trim();
        String name = etName.getText().toString().trim();

        UserInfo userInfo = new UserInfo(name,email);
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        databaseReference.child(user.getUid()).setValue(userInfo);
        Toast.makeText(this,"Info saved",Toast.LENGTH_SHORT).show();


    }

    private void RegisterUser()
    {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setMessage("Registering User...");
        progressBar.show();



//        firebaseAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressBar.dismiss();
//                        if(task.isSuccessful())
//                        {
//                            SaveUserInfo();
//                            Toast.makeText(Register.this,"Registered successfully",Toast.LENGTH_SHORT).show();
//                            finish();
//                            startActivity(new Intent(Register.this,MainActivity.class));
//                        }
//                        else
//                        {
//                            Toast.makeText(Register.this,"Failed to register",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnRegister:
                RegisterUser();

                break;

        }
    }

}
