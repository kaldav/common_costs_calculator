package hu.domain.ccc.commoncostscalculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends ActionBarActivity {

    Button registerBTN;
    Button toLogin;
    String username;
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
                email = ((TextView)findViewById(R.id.email)).getText().toString();
                password = ((TextView)findViewById(R.id.password)).getText().toString();
                passwordAgain = ((TextView)findViewById(R.id.password_again)).getText().toString();

                //check values
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this,getString(R.string.missed_data),Toast.LENGTH_LONG);
                }
                else if (!password.equals(passwordAgain)){
                    Toast.makeText(RegisterActivity.this,getString(R.string.passwords_doesnt_match),Toast.LENGTH_LONG);
                }
                else {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("action", "registration");
                    data.put("username", username);
                    data.put("password", password);
                    data.put("email", email);
                    ServerConnect post = new ServerConnect(data);
                    try {
                        Toast.makeText(RegisterActivity.this, post.execute("http://ccc.elitemagyaritasok.info").get(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
