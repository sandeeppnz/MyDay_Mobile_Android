package csdanz.locapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Network;
import com.android.volley.Cache;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.AuthFailureError;


import com.android.volley.toolbox.Volley;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText etUsername, etPassword;
    //TextView tvRegisterLink;

    SharedPreferences prefs;
    private ProgressDialog progressBar;
    private String access_token;

    private String userName;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = new ProgressDialog(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        access_token = prefs.getString("access_token","-1");

        if(access_token != "-1")
        {
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class) );
        }

        Load_RememberMe();

        //tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        //tvRegisterLink.setOnClickListener(this);

    }


    private void Load_RememberMe()
    {

        userName = prefs.getString("userName","");
        password = prefs.getString("password","");

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        if(!userName.isEmpty())
        {
            etUsername.setText(userName);
        }
        if(!password.isEmpty())
        {
            etPassword.setText(password);
        }


    }

    private void Save_RememberMe(String _userName, String _password)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userName", _userName).commit();
        editor.putString("password", _password).commit();
    }




    private void UserLogin()
    {


        final String email = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

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

        progressBar.setMessage("Authenticating...");
        progressBar.show();


        //String url = "http://mydayapi20180414.azurewebsites.net/token";
        String url = "https://myday.aut.ac.nz/token";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_LONG).show();

                        try {

                                JSONObject person = new JSONObject(response);
                                String access_token = person.getString("access_token");
                                String userName = person.getString("userName");
                                String token_type = person.getString("token_type");

                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("access_token", access_token).commit();
                                editor.putString("token_type", token_type).commit();

                                Save_RememberMe(email,password);

                                //editor.putString("userName", userName).commit();
                                //Toast.makeText(getApplicationContext(), "Response:  " + userName, Toast.LENGTH_LONG).show();

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(), "Error On Response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        progressBar.dismiss();

                        //Log.i( "SUCCESS MSG" ,response.toString());
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //error.printStackTrace();
                        progressBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Unable to login, please check your user name and password" , Toast.LENGTH_SHORT).show();
                        //Log.e("Error.Response", error.networkResponse.toString());
                    }
                }
        )
 {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String> ();

                params.put("scope", "openid email phone profile offline_access roles");
                //params.put("resource", "http://mydayapi20180414.azurewebsites.net/");
                params.put("resource", "https://myday.aut.ac.nz/");


                params.put("grant_type","password");
                params.put("username",etUsername.getText().toString().trim());
                params.put("password",etPassword.getText().toString().trim());

                return params;
            }


            @Override
            protected VolleyError parseNetworkError(VolleyError response) {
                try { }
                catch (Exception e) { }
                return super.parseNetworkError(response);
            }
        };





//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_LONG).show();
//                        progressBar.dismiss();
//
//                        Log.i( "SUCCESS MSG" ,response.toString());
//                        finish();
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        //error.printStackTrace();
//                        progressBar.dismiss();
//                        Toast.makeText(getApplicationContext(), "Unable to login, please check your user name and password" , Toast.LENGTH_SHORT).show();
//                        //Log.e("Error.Response", error.networkResponse.toString());
//                    }
//                }
//        )
//        {
//
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded";
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError
//            {
//                Map<String, String>  params = new HashMap<String, String> ();
//
//                params.put("scope", "openid email phone profile offline_access roles");
//                params.put("resource", "http://mydayapi20180414.azurewebsites.net/");
//                params.put("grant_type","password");
//                params.put("username",etUsername.getText().toString().trim());
//                params.put("password",etPassword.getText().toString().trim());
//
//                return params;
//            }
//
//
//            @Override
//            protected VolleyError parseNetworkError(VolleyError response) {
//                try { }
//                catch (Exception e) { }
//                return super.parseNetworkError(response);
//            }
//        };



        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

        //String urlUserInfo = "http://mydayapi20180414.azurewebsites.net/api/Account/UserInfo";


    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLogin:
                UserLogin();
                break;

//            case R.id.tvRegisterLink:
//                finish();
//                startActivity(new Intent(this,Register.class));
//                break;

        }
    }
}
