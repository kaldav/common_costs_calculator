package hu.domain.ccc.commoncostscalculator;

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

public class RegisterActivity extends ActionBarActivity {

    Button registerBTN;
    Button toLogin;
    String username;
    String firstname;
    String lastname;
    String email;
    String password;
    String passwordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBTN = (Button)findViewById(R.id.buttonRegister);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((TextView)findViewById(R.id.username)).getText().toString();
                firstname = ((TextView)findViewById(R.id.first_name)).getText().toString();
                lastname = ((TextView)findViewById(R.id.last_name)).getText().toString();
                email = ((TextView)findViewById(R.id.email)).getText().toString();
                password = ((TextView)findViewById(R.id.password)).getText().toString();
                passwordAgain = ((TextView)findViewById(R.id.password_again)).getText().toString();

                //check values
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || firstname.isEmpty() || lastname.isEmpty()){
                    Toast.makeText(RegisterActivity.this,getString(R.string.missed_data),Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(passwordAgain)){
                    Toast.makeText(RegisterActivity.this,getString(R.string.passwords_doesnt_match),Toast.LENGTH_SHORT).show();
                }
                else {
                    ArrayList<NameValuePair> data = new ArrayList<>();
                    data.add(new BasicNameValuePair("action", "registration"));
                    data.add(new BasicNameValuePair("username", username));
                    data.add(new BasicNameValuePair("firstname", firstname));
                    data.add(new BasicNameValuePair("lastname", lastname));
                    data.add(new BasicNameValuePair("password", Crypto.md5(password)));
                    data.add(new BasicNameValuePair("email", email));
                    Downloader connection = new Downloader(data);
                    connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                        @Override
                        public void onDownloadSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                int ret = response.getInt("user_id");
                                if (ret>0) {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else if (ret==-1 || ret==-2){
                                    Toast.makeText(RegisterActivity.this, getString(R.string.wrong_username_or_email), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDownloadFailed(String message) {
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    connection.start();
                }
            }
        });

        //back to login
        toLogin = (Button) findViewById(R.id.linkToLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
