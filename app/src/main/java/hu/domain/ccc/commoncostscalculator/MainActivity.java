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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    String PrefFileName = "data";
    List<Projects> projectItems = new ArrayList<>();
    ListView projectList;
    ProjectAdapter projectAdapter;
    Button newProjectButton;
    SharedPreferences settings;
    String session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session","");

        MainActivity.this.setResult(1); //alapból visszára ne a belépésre dobjon
        newProjectButton= (Button)findViewById(R.id.newProjectButton);

        newProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,NewProjectActivity.class);
                startActivity(i);
            }
        });

        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_projects"));
        data.add(new BasicNameValuePair("session", session));
        Downloader connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            @Override
            public void onDownloadSuccess(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject temp = response.getJSONObject(i);
                        DateFormat format = new SimpleDateFormat("yyyy-LL-dd");
                        projectItems.add(new Projects(temp.getString("name"), format.parse(temp.getString("start_date")), temp.getString("description"),Integer.parseInt(temp.getString("id"))));
                    }
                    projectList = (ListView) findViewById(R.id.Project_list);
                    projectAdapter = new ProjectAdapter(projectItems);
                    projectList.setAdapter(projectAdapter);
                }catch (JSONException e) {
                    //Ha null jön vissza nem lehet tömbé alakítani->szépítést igényel, tűzoltűsnak jó
                    Toast.makeText(MainActivity.this, "Itt az ideje létrehozni egy projektet", Toast.LENGTH_LONG).show();
                    //Akkor is legyen ilyen ha nincs benne semmi
                    projectList = (ListView) findViewById(R.id.Project_list);
                    projectAdapter = new ProjectAdapter(projectItems);
                    projectList.setAdapter(projectAdapter);

                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDownloadFailed(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        connection.start();

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
            ArrayList<NameValuePair> data = new ArrayList<>();
            data.add(new BasicNameValuePair("action", "logout"));
            data.add(new BasicNameValuePair("session", session));
            Downloader connection = new Downloader(data);
            connection.start();

            //session törlése kliens
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
