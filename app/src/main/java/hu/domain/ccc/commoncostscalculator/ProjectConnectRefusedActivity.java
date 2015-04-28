package hu.domain.ccc.commoncostscalculator;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProjectConnectRefusedActivity extends ActionBarActivity {

    List<Projects> projectItems;
    ListView projectList;
    ProjectConnectRefuseAdapter projectAdapter;

    String PrefFileName = "data";
    SharedPreferences settings;
    String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_connect_refused);

        projectList = (ListView)findViewById(R.id.items);

        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session","");

        projectItems = new ArrayList<>();

        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_invites"));
        data.add(new BasicNameValuePair("session", session));
        Downloader connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            @Override
            public void onDownloadSuccess(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject temp = response.getJSONObject(i);
                        ///////////////////////////////String name, String description, int id
                        projectItems.add(new Projects(temp.getString("name"),temp.getString("description"),temp.getInt("id")));
                    }
                }
                    catch (Exception e){
                    Toast.makeText(ProjectConnectRefusedActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                projectAdapter = new ProjectConnectRefuseAdapter(projectItems);
                projectList.setAdapter(projectAdapter);

            }

            @Override
            public void onDownloadFailed(String message) {
                Toast.makeText(ProjectConnectRefusedActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        connection.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_connect_refused, menu);
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
