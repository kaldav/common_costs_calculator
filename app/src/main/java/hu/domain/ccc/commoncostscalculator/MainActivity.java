package hu.domain.ccc.commoncostscalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {
    Button logoutBTN;
    String PrefFileName = "data";
    List<Projects> projectItems = new ArrayList<>();
    ListView projectList;
    ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.this.setResult(1); //alapból visszára ne a belépésre dobjon

        SharedPreferences settings = getSharedPreferences(PrefFileName, 0);
        String session = settings.getString("session","");

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("action", "get_projects");
        data.put("session", session);
        ServerConnect post = new ServerConnect(data);
        try {
            //JSON feldolgozása
            JSONArray response = new JSONArray(post.execute("http://ccc.elitemagyaritasok.info").get());

            for(int i=0;i<response.length();i++) {
            JSONObject temp = response.getJSONObject(i);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            projectItems.add(new Projects(temp.getString("name"),format.parse(temp.getString("start_date")),temp.getString("description")));
            }

            projectList = (ListView)findViewById(R.id.Project_list);
            projectAdapter = new ProjectAdapter(projectItems);
            projectList.setAdapter(projectAdapter);

            /*int ret = response.getInt("user_id");
            if (ret>0) {
                Toast.makeText(RegisterActivity.this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (ret==-1 || ret==-2){
                Toast.makeText(RegisterActivity.this, getString(R.string.wrong_username_or_email), Toast.LENGTH_SHORT).show();
            }*/

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

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
        if (id == R.id.logoutBTN) {
            //session törlése szerver
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("action", "logout");
            ServerConnect post = new ServerConnect(data);
            try {
                post.execute("http://ccc.elitemagyaritasok.info");
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }
}
