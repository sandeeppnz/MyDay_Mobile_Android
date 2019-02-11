package csdanz.locapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Console;


public class page1 extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView text_agreement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);

        text_agreement = (TextView) findViewById(R.id.text_agreement);
        text_agreement.setText(Html.fromHtml(getString(R.string.page1_textbox)));
        text_agreement.setMovementMethod(new ScrollingMovementMethod());


        btnLogin = (Button) findViewById(R.id.btnNext);
        btnLogin.setOnClickListener(this);

    }



    private void UserLogin() {
        finish();
        startActivity(new Intent(getApplicationContext(),page2.class));

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
