package hu.domain.ccc.commoncostscalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    Button logoutBTN;
    String PrefFileName = "data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.this.setResult(1); //alapból visszára ne a belépésre dobjon
        logoutBTN = (Button)findViewById(R.id.logout);
        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //session törlése szerver
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("action", "logout");
                ServerConnect post = new ServerConnect(data);
                try {
                    post.execute("http://ccc.elitemagyaritasok.info");
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }

                //session törlése kliens
                SharedPreferences settings = getSharedPreferences(PrefFileName, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("session", "");
                editor.commit();

                //visszalépés
                MainActivity.this.setResult(0); //ilyenkor viszont a beléptető screenre dobjon
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
