package hu.domain.ccc.commoncostscalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity {
    String PrefFileName = "data";
    List<Projects> projectItems;
    ListView projectList;
    ProjectAdapter projectAdapter;
    int g=0;
    Button newProjectButton;
    SharedPreferences settings;
    String session;
    ArrayList<NameValuePair> data;
    Downloader connection;

    /* Called when the second activity's finished */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //loadProjects();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjects();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session","");

        MainActivity.this.setResult(1); //alapból visszára ne a belépésre dobjon
        newProjectButton= (Button)findViewById(R.id.newProjectButton);




        projectList = (ListView) findViewById(R.id.Project_list);

        newProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,NewProjectActivity.class);
                startActivityForResult(i, 1);
                //Result-ért hívd és ha visszajön akkor frissiteni kell a listát!!
                //Térj vissza projektel és add hozzá az adapterhez és akkor rajzoltasd ki,
                // adapter igyis ugyis létre van hozva ekkor már
            }
        });
        loadProjects();
    }

    public void loadProjects(){
        data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_projects"));
        data.add(new BasicNameValuePair("session", session));

        connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            public void onDownloadSuccess(String result) {
                if (result.startsWith("null")) //Ha nincs projekt amit elfogadnia kéne
                {
                    Toast.makeText(MainActivity.this, "Itt az ideje létrehozni egy projektet", Toast.LENGTH_LONG).show();
                    projectItems = new ArrayList<Projects>();
                    // Ha új projektet hoz létre elég hozzáadni
                    projectList = (ListView) findViewById(R.id.Project_list);
                    projectAdapter = new ProjectAdapter(projectItems);
                    projectList.setAdapter(projectAdapter);
                }
                else {
                    try {
                        projectItems = new ArrayList<>();
                        JSONArray response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject temp = response.getJSONObject(i);
                            //Date time parsolás
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                            boolean asd = temp.getInt("is_closed") == 0 ? false : true ;
                            g=temp.getInt("is_closed");
                            projectItems.add(new Projects(temp.getString("name"), format.parse(temp.getString("start_time")), temp.getString("description"), Integer.parseInt(temp.getString("id")),asd,Integer.parseInt(temp.getString("creator_id"))));
                            int a = 0;
                        }
                        //projekt lista születése
                        projectAdapter = new ProjectAdapter(projectItems);
                        projectList.setAdapter(projectAdapter);
                        projectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent in = new Intent(MainActivity.this, ProjectViewActivity.class);
                                in.putExtra("project_id", (int) id);
                                in.putExtra("open",projectItems.get(position).isClosed());
                                startActivity(in);
                            }
                        });
                    } catch (JSONException e) {
                        //Ha null jön vissza nem lehet tömbé alakítani->szépítést igényel, tűzoltűsnak jó
                        Toast.makeText(MainActivity.this, "Itt az ideje létrehozni egy projektet", Toast.LENGTH_LONG).show();
                        //Akkor is legyen ilyen ha nincs benne semmi
                        projectItems = new ArrayList<Projects>();
                        projectList = (ListView) findViewById(R.id.Project_list);
                        projectAdapter = new ProjectAdapter(projectItems);
                        projectList.setAdapter(projectAdapter);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
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
        else if(id == R.id.invitesBTN)
        {
            Intent i = new Intent(MainActivity.this,ProjectConnectRefusedActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
