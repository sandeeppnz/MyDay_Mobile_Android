package csdanz.locapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.proximi.proximiiolibrary.ProximiioAPI;
import io.proximi.proximiiolibrary.ProximiioGeofence;
import io.proximi.proximiiolibrary.ProximiioListener;
import io.proximi.proximiiolibrary.ProximiioOptions;
import io.proximi.proximiiomap.ProximiioMapHelper;
import io.proximi.proximiiomap.ProximiioMapView;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Proximi
    private ProximiioAPI proximiioAPI;
    private ProximiioMapHelper mapHelper;
    private static final String APP_TAG = "CSDA";
    public static final String AUTH_PROXIMI = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImlzcyI6ImY1ZDY3M2EwLWM3NjMtNGZjYi1hYjU1LWM1Nzg2ZTU2NmVhOSIsInR5cGUiOiJhcHBsaWNhdGlvbiIsImFwcGxpY2F0aW9uX2lkIjoiZTYwMjQ1ZjUtMDgwYi00YTI1LWJlZTItNTY3MzY3ZDM4MjA2In0.rfayawnBYYVBa_UZvuX4Noa-pbDcKDgV3Sgi-qEl5qs";

    private TextView UserName;
    private TextView txtUsernameMain, txtEmailMain;

    private String AUTH_LOGIN;


    private String userName;
    private String access_token;
    private String token_type;
    private String visitorId;

    private Date currDateTime;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  Check if the login token available

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = prefs.getString("userName","-1");
        access_token = prefs.getString("access_token","-1");
        token_type = prefs.getString("token_type","-1");

        if(access_token == "-1")
        {
            finish();
            startActivity(new Intent(this,Login.class) );
            //startActivity(new Intent(this,page1.class) );
        }


        try
        {

            //Setup Proximi.io
        // For Android 8+, create a notification channel for notifications.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SharedPreferences preferences = getSharedPreferences("Proximi.io Map Demo", MODE_PRIVATE);
            if (!preferences.contains("notificationChannel")) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    NotificationChannel channel = new NotificationChannel(BackgroundListener.NOTIFICATION_CHANNEL_ID,
                            BackgroundListener.NOTIFICATION_CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                    preferences.edit()
                            .putBoolean("notificationChannel", true)
                            .apply();
                }
            }
        }



            ProximiioOptions options = new ProximiioOptions()
                    .setNotificationMode(ProximiioOptions.NotificationMode.ENABLED);

            // Create our Proximi.io listener
            proximiioAPI = new ProximiioAPI(APP_TAG, this, options);
            proximiioAPI.setListener(new ProximiioListener() {
                @Override
                public void geofenceEnter(ProximiioGeofence geofence) {
                    Log.d(APP_TAG, "Geofence enter: " + geofence.getName());
                }

                @Override
                public void geofenceExit(ProximiioGeofence geofence, @Nullable Long dwellTime) {
                    Log.d(APP_TAG, "Geofence exit: " + geofence.getName() + ", dwell time: " + String.valueOf(dwellTime));
                }

                @Override
                public void loginFailed(LoginError loginError) {
                    Log.e(APP_TAG, "LoginError! (" + loginError.toString() + ")");
                }
            });


            proximiioAPI.setAuth(AUTH_PROXIMI);
            proximiioAPI.setActivity(this);


            // Initialize the map
            ProximiioMapView mapView = findViewById(R.id.map);
            mapHelper = new ProximiioMapHelper.Builder(this, mapView, AUTH_PROXIMI, savedInstanceState)
                    .build();

            visitorId = proximiioAPI.getVisitorID();
            //TextView navEmail = (TextView) headerView.findViewById(R.id.txtEmailMain);
            //navEmail.setText(visitorId);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Unable to load map" , Toast.LENGTH_SHORT).show();
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = (TextView) headerView.findViewById(R.id.txtUsernameMain);
        navUsername.setText(userName);



        Post_Proximi_Data();

        navigationView.setNavigationItemSelectedListener(this);



    }


    // Common APIs
    @Override
    protected void onStart() {
        super.onStart();
        mapHelper.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapHelper.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapHelper.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapHelper.onDestroy();
        proximiioAPI.destroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapHelper.onLowMemory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        proximiioAPI.onActivityResult(requestCode, resultCode, data);
        mapHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        proximiioAPI.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        if (id == R.id.action_logout) {
//
//
//            prefs.edit().remove("access_token").commit();
//            finish();
//            startActivity(new Intent(getApplicationContext(),Login.class));
//            return true;
//
//
//        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        if (id == R.id.nav_myday) {

            Uri url = Uri.parse("https://myday.aut.ac.nz");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, url);
            finish();
            startActivity(launchBrowser);

        }
        else if (id  == R.id.nav_logout)
        {
            prefs.edit().remove("access_token").commit();
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
         }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void Post_Proximi_Data()
    {

        String url = "https://myday.aut.ac.nz/api/ProximiVisitors";

        // get Username and Visitor ID
        //Toast.makeText(getApplicationContext(),"Insert Proximi data",Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Response", response);
                        //Toast.makeText(getApplicationContext(), "Response:  " + response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //error.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Unable to login, please check your user name and password" , Toast.LENGTH_SHORT).show();
                        //Log.d("Error: ", error)
                    }
                }
        )
        {

//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("ID","0");
                params.put("UserId",userName);
                params.put("VisitorId",visitorId);
                currDateTime = Calendar.getInstance().getTime();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
                TimeZone tz = TimeZone.getDefault();

                params.put("CreatedDate", formatter.format(currDateTime).toString());
                params.put("TimeZone",tz.getID());

                return params;
            }


            @Override
            protected VolleyError parseNetworkError(VolleyError response) {
                try { }
                catch (Exception e) { }
                return super.parseNetworkError(response);
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }




}
