package csdanz.locapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class page2 extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    CheckBox term1;
    CheckBox term2;
    CheckBox term3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        btnLogin = (Button) findViewById(R.id.btnNext);
        btnLogin.setOnClickListener(this);


         term1 = (CheckBox) findViewById(R.id.term1);
         term2 = (CheckBox) findViewById(R.id.term2);
         term3 = (CheckBox) findViewById(R.id.term3);


    }

    private void UserLogin() {

        if(term1.isChecked() && term2.isChecked() && term3.isChecked())
        {
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }

        Toast.makeText(getApplicationContext(), "Please accept the terms to continue" , Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnNext:
                UserLogin();
                break;

        }
    }

}
