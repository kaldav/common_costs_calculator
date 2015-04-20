package hu.domain.ccc.commoncostscalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StartActivity extends ActionBarActivity {

    Button registerBTN;
    Button loginBTN;
    String username;
    String password;
    String PrefFileName = "data";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(PrefFileName, 0);
        String session = settings.getString("session","");

        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "is_logged_in"));
        data.add(new BasicNameValuePair("session", session));
        Downloader connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            public void onDownloadSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    int ret = response.getInt("is_logged_in");
                    if (ret==1) { //be van lépve
                        Intent myIntent = new Intent(StartActivity.this, MainActivity.class);
                        startActivityForResult(myIntent, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onDownloadFailed(String message) {
                Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        connection.start();

        setContentView(R.layout.activity_start);

        registerBTN = (Button)findViewById(R.id.linkToRegister);
        registerBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(StartActivity.this, RegisterActivity.class);
                StartActivity.this.startActivity(myIntent);
            }
        });

        loginBTN = (Button)findViewById(R.id.loginBTN);
        loginBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = ((TextView)findViewById(R.id.username)).getText().toString();
                password = ((TextView)findViewById(R.id.password)).getText().toString();
                if (username.isEmpty()  || password.isEmpty()){
                    Toast.makeText(StartActivity.this,getString(R.string.missed_data),Toast.LENGTH_SHORT).show();
                }
                else {

                    ArrayList<NameValuePair> data = new ArrayList<>();
                    data.add(new BasicNameValuePair("action", "login"));
                    data.add(new BasicNameValuePair("username", username));
                    data.add(new BasicNameValuePair("password", Crypto.md5(password)));
                    Downloader connection = new Downloader(data);
                    connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                        public void onDownloadSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                int ret = response.getInt("user_id");
                                if (ret<0) {
                                    Toast.makeText(StartActivity.this, getString(R.string.wrong_username_or_password), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    SharedPreferences settings = getSharedPreferences(PrefFileName, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("session", response.getString("session"));
                                    editor.commit();

                                    Intent myIntent = new Intent(StartActivity.this, MainActivity.class);
                                    startActivityForResult(myIntent, 0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        public void onDownloadFailed(String message) {
                            Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    connection.start();
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) { //ha nem kijelentekzett, hanem visszát nyomott
            this.finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
