package hu.domain.ccc.commoncostscalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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


public class NewProjectActivity extends ActionBarActivity{
    EditText userSearchInput;
    String PrefFileName = "data";
    ListView user_list;
    ListView selected_user_list;
    UsersAdapter adapter;
    UsersAdapter selectedAdapter;
    Timer timer;
    TimerTask timertask;
    String SearchString;
    String PSearchString;
    SharedPreferences settings;
    String session;
    List<Integer> users;
    ArrayList<Users> selectedItems;
    ArrayList<Users> usersItems;

    final Handler handler = new Handler();
    Button addUser;
    Button startProject;
    EditText nameET;
    EditText descriptionET;

    String name;
    String description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session", "");

        addUser = (Button)findViewById(R.id.addUser);
        startProject = (Button)findViewById(R.id.newProjectCreate);
        selected_user_list = (ListView) findViewById(R.id.selected_user_list);
        nameET = (EditText)findViewById(R.id.newProjectName);
        descriptionET= (EditText)findViewById(R.id.newPRojectDescription);


        selectedItems = new ArrayList<>();
        users = new ArrayList<>();

        addUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(NewProjectActivity.this);
                dialog.setContentView(R.layout.add_user_dialog);
                dialog.setTitle("Felhasználók keresése");
                userSearchInput = (EditText) dialog.findViewById(R.id.user_search_edittext);
                user_list = (ListView) dialog.findViewById(R.id.user_search_list);
                userSearchInput.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        StopTimerTask();
                        StartTimer();
                    }
                    public void afterTextChanged(Editable s) {}
                });
                user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!users.contains((int)id)) {
                            users.add((int) id);
                            selectedItems.add(usersItems.get(position));
                            selectedAdapter = new UsersAdapter(selectedItems, R.layout.listitem_users);
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
                                            selectedAdapter = new UsersAdapter(selectedItems, R.layout.listitem_users);
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
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.cancelButton);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        startProject.setOnClickListener(new View.OnClickListener() {
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
                                    Intent returnIntent = new Intent();
                                    setResult(RESULT_OK, returnIntent);
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

    }
    public void InitializeTimerTask(){
        timertask = new TimerTask() {
            public void run() {
            handler.post(new Runnable() {
                public void run() {
                SearchString = userSearchInput.getText().toString();
                if(!SearchString.equals("") && !SearchString.equals(PSearchString)) { //nem üres és változott a legutóbbi lekérdezés óta

                    PSearchString = SearchString; //előző állapot felülírása
                    usersItems = new ArrayList<>();

                    ArrayList<NameValuePair> data = new ArrayList<>();
                    data.add(new BasicNameValuePair("action", "search_users"));
                    data.add(new BasicNameValuePair("session", session));
                    data.add(new BasicNameValuePair("search", SearchString));
                    Downloader connection = new Downloader(data);
                    connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                        public void onDownloadSuccess(String result) {
                            try {
                                JSONArray response = new JSONArray(result);
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject temp = response.getJSONObject(i);
                                    usersItems.add( new Users(temp.getString("id"),temp.getString("username"), temp.getString("email"), temp.getString("firstname"), temp.getString("lastname")) );
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                                usersItems.add(new Users("","Nincs találat!", "", "", ""));
                            }
                            adapter = new UsersAdapter(usersItems, R.layout.listitem_users);
                            user_list.setAdapter(adapter);
                        }
                        public void onDownloadFailed(String message) {
                            Toast.makeText(NewProjectActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    connection.start();

                }
                }
            });
            }
        };
    }

    public void StartTimer(){
        timer = new Timer();
        InitializeTimerTask();
        timer.schedule(timertask,500, 500); //500 ms után indul, 500 ms interval
    }

    public void StopTimerTask(){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    protected void onPause() {
        super.onPause();
        StopTimerTask();
    }
}
