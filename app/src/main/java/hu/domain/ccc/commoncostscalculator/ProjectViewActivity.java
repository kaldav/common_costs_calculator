package hu.domain.ccc.commoncostscalculator;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ProjectViewActivity extends ActionBarActivity {

    int project_id;
    String PrefFileName = "data";
    SharedPreferences settings;
    String session;
    TextView descriptionTV;
    private UsersAdapter usersAdapter;
    ListView user_list;
    Button addItem;
    ListView itemList;
    ArrayList<Items> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);
        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session","");
        Intent intent = getIntent();
        project_id = intent.getIntExtra("project_id",0);
        descriptionTV = (TextView)findViewById(R.id.projectDescription);
        user_list = (ListView)findViewById(R.id.usersList);
        itemList = (ListView) findViewById(R.id.items);

        items = new ArrayList<Items>();

        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_project"));
        data.add(new BasicNameValuePair("project_id", String.valueOf(project_id)));
        data.add(new BasicNameValuePair("session", session));
        Downloader connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            @Override
            public void onDownloadSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    getSupportActionBar().setTitle(response.getString("name"));
                    descriptionTV.setText(response.getString("description"));
                }catch (Exception e){
                    Toast.makeText(ProjectViewActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDownloadFailed(String message) {
                Toast.makeText(ProjectViewActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        connection.start();

        final ArrayList<Users> usersItems = new ArrayList<>();

        data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_project_users"));
        data.add(new BasicNameValuePair("session", session));
        data.add(new BasicNameValuePair("project_id", String.valueOf(project_id)));
        connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            public void onDownloadSuccess(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject temp = response.getJSONObject(i);
                        usersItems.add(new Users(temp.getString("id"),temp.getString("username"), temp.getString("email"), temp.getString("firstname"), temp.getString("lastname")));
                    }
                }catch (Exception e) {
                    usersItems.add(new Users("","Nincs találat!", "","","")); // erre kéne szebb megoldás
                }
                usersAdapter = new UsersAdapter(usersItems, R.layout.listitem_users);
                user_list.setAdapter(usersAdapter);
            }
            public void onDownloadFailed(String message) {
                Toast.makeText(ProjectViewActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        connection.start();



        addItem = (Button) findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectViewActivity.this,AddItemActivity.class);

                i.putExtra("users", usersItems );
                i.putExtra("projekt_id",String.valueOf(project_id));
                startActivityForResult(i,0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        final Items item = bundle.getParcelable("item");
        items.add(item);

        itemList.setAdapter(new ItemsAdapter(items));

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(ProjectViewActivity.this);
                dialog.setContentView(R.layout.dialog_item_view);
                dialog.setTitle(item.getName());

                ((TextView)dialog.findViewById(R.id.tetel_elnevezes)).setText("Elnevezés: " + item.getName());
                ((TextView)dialog.findViewById(R.id.tetel_leiras)).setText("Leírás: " + item.getDescription());
                ((TextView) dialog.findViewById(R.id.tetel_osszeg)).setText("Összeg :" + Integer.toString(item.getSum()));
                ((TextView) dialog.findViewById(R.id.tetel_darabszam)).setText("Darabszám: " + Integer.toString(item.getCount()));
                ((ListView)dialog.findViewById(R.id.tetel_resztvevok)).setAdapter(new UsersAdapter(item.getUsers(),R.layout.listitem_users));

                dialog.show();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_view, menu);
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
