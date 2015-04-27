package hu.domain.ccc.commoncostscalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Contacts;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.content.SharedPreferences;


public class AddItemActivity extends ActionBarActivity {

    Button itemaddButton; // tétel hozzáadása gomb
    ListView user_list; // lista a projekt tagjainak megtekintéséhez
    EditText elnevezesET; // tétel neve
    EditText osszegET; // tétel összege
    EditText leirasET; // tétel leírása
    EditText darabszamET; // darabszám


    String session;
    String PrefFileName = "data";
    SharedPreferences settings;
    String project_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session", "");



        itemaddButton = (Button) findViewById(R.id.itemAdd);
        elnevezesET = (EditText) findViewById(R.id.elnevezes);
        osszegET = (EditText) findViewById(R.id.tetel_osszeg);
        leirasET = (EditText) findViewById(R.id.tetel_leiras);
        darabszamET = (EditText) findViewById(R.id.tetel_darabszam);

        Bundle b = getIntent().getExtras();
        ArrayList<Users> users = b.getParcelableArrayList("users");
        project_id = b.getString("projekt_id");
        final UsersAdapter adapter = new UsersAdapter(users,R.layout.listitem_item);
        user_list = (ListView) findViewById(R.id.resztvevok);
        user_list.setAdapter(adapter);



        user_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        itemaddButton = (Button) findViewById(R.id.itemAdd);

        itemaddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Users> users = new ArrayList<Users>();


                for (int i = 0; i < adapter.getCount();i++)
                {
                    Users user = (Users)adapter.getItem(i);
                    if(user.isChecked())
                        users.add(user);

                }

                String tetel_elnevezes = elnevezesET.getText().toString().trim();
                String tetel_leiras = leirasET.getText().toString().trim();
                String tetel_osszeg = osszegET.getText().toString().trim();
                String tetel_darabszam = darabszamET.getText().toString().trim();


                if(tetel_elnevezes.isEmpty() || tetel_osszeg.isEmpty() || users.size() == 0)
                {
                    Toast.makeText(AddItemActivity.this, "Ez így kevés lesz...",Toast.LENGTH_SHORT);
                    return; // valami nincs kitöltve, nem csinálunk semmit
                }

                ArrayList<NameValuePair> data = new ArrayList<>();
                data.add(new BasicNameValuePair("action", "add_item"));
                data.add(new BasicNameValuePair("session", session));
                data.add(new BasicNameValuePair("project_id", project_id));
                data.add(new BasicNameValuePair("name", tetel_elnevezes));
                data.add(new BasicNameValuePair("description",tetel_leiras ));
                data.add(new BasicNameValuePair("amount",tetel_darabszam ));
               // data.add(new BasicNameValuePair("tömb",new ArrayList<>() ));
               /* Downloader connection = new Downloader(data);
                connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                    public void onDownloadSuccess(String result) {
                        try {
                            JSONArray response = new JSONArray(result);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject temp = response.getJSONObject(i);
                                usersItems.add(new Users(temp.getString("username"), temp.getString("email"), temp.getString("firstname"), temp.getString("lastname")));
                            }
                        }catch (Exception e) {
                            usersItems.add(new Users("Nincs találat!", "","",""));
                        }
                        usersAdapter = new UsersAdapter(usersItems, R.layout.listitem_users);
                        user_list.setAdapter(usersAdapter);
                    }
                    public void onDownloadFailed(String message) {
                        Toast.makeText(ProjectViewActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
                connection.start();*/


                Intent i = new Intent();
                i.putExtra("users",users);
                if (getParent() != null)
                    getParent().setResult(RESULT_OK,i);
                else
                    setResult(RESULT_OK,i);
                finish();
            }
        });


                /*user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!users.contains((int)id)) {
                            users.add((int) id);
                            selectedItems.add(usersItems.get(position));
                            selectedAdapter = new UsersAdapter(selectedItems);
                            selected_user_list.setAdapter(selectedAdapter);
                            selected_user_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int pos, long id) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(NewProjectActivity.this);

                                    builder.setTitle("Törlés");
                                    builder.setMessage("Biztosan törölni szeretnéd?");

                                    builder.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            users.remove(pos);
                                            selectedItems.remove(pos);
                                            selectedAdapter = new UsersAdapter(selectedItems);
                                            selected_user_list.setAdapter(selectedAdapter);
                                            dialog.dismiss();
                                        }

                                    });
                                    builder.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    return true;
                                }
                            });
                            Toast.makeText(NewProjectActivity.this, "Hozzaádva!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(NewProjectActivity.this, "Ez a felhasználó már szerepel a kiválasztott résztvevők között!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }); */


    }

       /* startProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name=nameET.getText().toString();
                description=descriptionET.getText().toString();

                //check név, description, users
                if (!name.isEmpty() && !description.isEmpty() && !users.isEmpty()) {
                    ArrayList<NameValuePair> data = new ArrayList<>();
                    data.add(new BasicNameValuePair("action", "create_project"));
                    data.add(new BasicNameValuePair("session", session));
                    data.add(new BasicNameValuePair("name", name));
                    data.add(new BasicNameValuePair("description", description));


                    final int size = users.size();
                    for (int i = 0; i < size; i++){
                        data.add(new BasicNameValuePair("user_id[]", users.get(i).toString()));
                    }

                    Downloader connection = new Downloader(data);
                    connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                        public void onDownloadSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                int ret = response.getInt("project_id");
                                if (ret>0) {
                                    Toast.makeText(NewProjectActivity.this, "A projekt sikeresen létrejött!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else if (ret==-1){
                                    Toast.makeText(NewProjectActivity.this, "Valami hiba történt!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        public void onDownloadFailed(String message) {
                            Toast.makeText(NewProjectActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    connection.start();
                }
                else{
                    Toast.makeText(NewProjectActivity.this, "Hiányzó adatok!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }*/




    @Override
    protected void onPause() {
        super.onPause();
    }
}
