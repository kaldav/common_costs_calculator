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
import org.json.JSONObject;

import java.util.List;

public class RegisterActivity extends ActionBarActivity {

    TextView usernameTV;
    Button registerBTN;
    ServerConnect connection;
    JSONObject ret;
    List<NameValuePair> parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connection = new ServerConnect("http://ccc.elitemagyaritasok.info");
        registerBTN.findViewById(R.id.buttonRegister);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameters.clear();
                parameters.add(new BasicNameValuePair("action", "registration"));
                parameters.add(new BasicNameValuePair("username", "david"));
                ret = connection.Action(parameters);
                Toast.makeText(RegisterActivity.this,ret.toString(),Toast.LENGTH_LONG).show();
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
