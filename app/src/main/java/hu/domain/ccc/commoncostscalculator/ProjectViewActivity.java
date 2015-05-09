package hu.domain.ccc.commoncostscalculator;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProjectViewActivity extends ActionBarActivity {

    int project_id;
    int project_sajat_id;
    int project_tulaj_id;
    String PrefFileName = "data";
    SharedPreferences settings;
    String session;
    TextView descriptionTV;
    private UsersAdapter usersAdapter;
    private Penzugyi_tetel_adapter penzugyAdapter;
    ListView user_list;
    Button addItem;
    ListView itemList;
    ArrayList<Items> items;
    MenuItem projekt_zaras;
    ListView penzugyek;
    boolean opene;

    @Override
    protected void onResume() {
        super.onResume();
        GetItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);
        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session", "");
        Intent intent = getIntent();
        opene = intent.getBooleanExtra("open", true);
        project_id = intent.getIntExtra("project_id", 0);
        descriptionTV = (TextView) findViewById(R.id.projectDescription);
        user_list = (ListView) findViewById(R.id.usersList);
        penzugyek = (ListView) findViewById(R.id.penzugyek);
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
                    project_tulaj_id = response.getInt("creator_id");
                } catch (Exception e) {
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
                        usersItems.add(new Users(temp.getString("id"), temp.getString("username"), temp.getString("email"), temp.getString("firstname"), temp.getString("lastname")));
                    }
                } catch (Exception e) {
                    usersItems.add(new Users("", "Nincs találat!", "", "", "")); // erre kéne szebb megoldás
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
        if ( opene)
        {
            addItem.setEnabled(false);

        }

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectViewActivity.this, AddItemActivity.class);

                i.putExtra("users", usersItems);
                i.putExtra("projekt_id", String.valueOf(project_id));
                startActivity(i);

            }
        });

        GetItems();
        //
        AdminBeallit();
        Penzugyek_kiirasa();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
        //a

    protected void Projekt_zaras()
    {
        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "close_project"));
        data.add(new BasicNameValuePair("session", session));
        data.add(new BasicNameValuePair("project_id", Integer.toString(project_id)));
        Downloader connection2 = new Downloader(data);
        connection2.setOnConnectionListener(new Downloader.OnConnectionListener() {
            @Override
            public void onDownloadSuccess(String result) {

                try {
                    JSONObject response = new JSONObject(result);
                    int ret = response.getInt("success");
                    if (ret==0) { //nincs admin
                        Toast.makeText(ProjectViewActivity.this, "Sikertelen zárás!" + project_sajat_id, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ProjectViewActivity.this, "Sikeres zárás!" + project_sajat_id, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDownloadFailed(String message) {

            }
        });
        connection2.start();

    }
    protected void AdminBeallit()
    {
        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_own_id"));
        data.add(new BasicNameValuePair("session", session));
        Downloader connection2 = new Downloader(data);
        connection2.setOnConnectionListener(new Downloader.OnConnectionListener() {
                                               @Override
                                               public void onDownloadSuccess(String result) {

                                                   try {
                                                       JSONObject response = new JSONObject(result);
                                                       int ret = response.getInt("user_id");
                                                       if (ret==-1) { //nincs admin

                                                       }
                                                       else {
                                                        project_sajat_id = ret;
                                                        if (project_sajat_id == project_tulaj_id)
                                                        {
                                                            projekt_zaras.setEnabled(true);
                                                        }
                                                           else {
                                                            projekt_zaras.setEnabled(false);
                                                        }
                                                        //Toast.makeText(ProjectViewActivity.this, "admin: " + project_sajat_id, Toast.LENGTH_SHORT).show();
                                                       }
                                                   } catch (JSONException e) {
                                                       e.printStackTrace();
                                                   }
                                               }

                                               @Override
                                               public void onDownloadFailed(String message) {

                                               }
                                           });
        connection2.start();



    }

    protected void Penzugyek_kiirasa()
    {

        final ArrayList<Penzugyi_tetel> penzugyItems = new ArrayList<>();

        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_project_info"));
        data.add(new BasicNameValuePair("session", session));
        data.add(new BasicNameValuePair("project_id", String.valueOf(project_id)));
        Downloader connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            public void onDownloadSuccess(String result) {
                if (!result.startsWith("null")) {

                    try {
                        JSONArray response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject temp = response.getJSONObject(i);
                            String kinek = temp.getString("to_user");
                            //int osszeg = Integer.getInteger(temp.getString("value"));
                            double osszeg = Double.parseDouble(temp.getString("value"));
                            penzugyItems.add(new Penzugyi_tetel(kinek, (int) osszeg));
                        }
                    } catch (Exception e) {
                        Toast.makeText(ProjectViewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                    penzugyAdapter = new Penzugyi_tetel_adapter(penzugyItems, R.layout.listitem_penzugyi_tetel);
                    penzugyek.setAdapter(penzugyAdapter);


            }

            public void onDownloadFailed(String message) {
                Toast.makeText(ProjectViewActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        connection.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_view, menu);
        projekt_zaras = (MenuItem) menu.findItem(R.id.action_project_finish);
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
        else if (id == R.id.action_project_finish){

            Projekt_zaras();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }


    void GetItems() {
        final ArrayList<Items> item_list = new ArrayList<>();
        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("action", "get_items"));
        data.add(new BasicNameValuePair("session", session));
        data.add(new BasicNameValuePair("project_id", String.valueOf(project_id)));
        Downloader connection = new Downloader(data);
        connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
            public void onDownloadSuccess(String result) {  //itemek letöltése
                if (result.startsWith("null")) //Ha nincs projekt amit elfogadnia kéne
                {
                    Toast.makeText(ProjectViewActivity.this, "Nincs még tétel", Toast.LENGTH_LONG).show();
                    
                    //item_list = new ArrayList<Projects>();
                    //item_list = (ListView) findViewById(R.id.Project_list); /
                    //projectAdapter = new ProjectAdapter(projectItems);
                    //projectList.setAdapter(projectAdapter);
                }
                else {
                    try {
                        JSONArray response = new JSONArray(result);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject temp = response.getJSONObject(i);
                            item_list.add(new Items(
                                    temp.getString("name"),
                                    temp.getString("description"),
                                    Integer.parseInt(temp.getString("value")),
                                    0, //Integer.parseInt(temp.getString("amount")),
                                    new ArrayList<Users>(),
                                    temp.getString("id")
                            ));
                            final ItemsAdapter adapter = new ItemsAdapter(item_list);
                            itemList.setAdapter(adapter);
                            itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //itemről részletes info
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    final Items item = (Items) adapter.getItem(position);
                                    final Dialog dialog = new Dialog(ProjectViewActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.dialog_item_view);
                                    //dialog.setTitle(item.getName());

                                    //userek lekérdezése az item-hez
                                    final ArrayList<Users> users = new ArrayList<>();
                                    ArrayList<NameValuePair> data = new ArrayList<>();
                                    data.add(new BasicNameValuePair("action", "get_item_users"));
                                    data.add(new BasicNameValuePair("session", session));
                                    data.add(new BasicNameValuePair("item_id", item.getId()));
                                    Downloader connection = new Downloader(data);
                                    connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                                        public void onDownloadSuccess(String result) {
                                            try {
                                                JSONArray response = new JSONArray(result);
                                                for (int i = 0; i < response.length(); i++) {
                                                    JSONObject temp = response.getJSONObject(i);
                                                    users.add(new Users(temp.getString("user_id"), temp.getString("username"), temp.getString("email"), temp.getString("firstname"), temp.getString("lastname")));
                                                }

                                                item.setUsers(users);


                                            ((TextView)dialog.findViewById(R.id.tetel_elnevezes)).setText("Elnevezés: " + item.getName());
                                            ((TextView)dialog.findViewById(R.id.tetel_leiras)).setText("Leírás: " + item.getDescription());
                                            ((TextView) dialog.findViewById(R.id.tetel_osszeg)).setText("Összeg: " + Integer.toString(item.getSum()));
                                            ((ListView)dialog.findViewById(R.id.tetel_resztvevok)).setAdapter(new UsersAdapter(item.getUsers(),R.layout.listitem_users));


                                                dialog.show();

                                            } catch (Exception e) {
                                                Toast.makeText(ProjectViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        public void onDownloadFailed(String message) {
                                            Toast.makeText(ProjectViewActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    connection.start();


                                }
                            });
                        }
                    } catch (Exception e) {
                        Toast.makeText(ProjectViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            public void onDownloadFailed(String message) {
                Toast.makeText(ProjectViewActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        connection.start();
    }


}
