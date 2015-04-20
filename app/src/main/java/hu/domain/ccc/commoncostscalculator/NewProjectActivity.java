package hu.domain.ccc.commoncostscalculator;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Timer;
import java.util.TimerTask;


public class NewProjectActivity extends ActionBarActivity{
    EditText userSearchInput;
    String PrefFileName = "data";
    ListView user_list;
    private UsersAdapter adapter;
    Timer timer;
    TimerTask timertask;
    String SearchString;
    String PSearchString;
    SharedPreferences settings;
    String session;

    final Handler handler = new Handler();
    Button addUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        settings = getSharedPreferences(PrefFileName, 0);
        session = settings.getString("session","");
        addUser = (Button)findViewById(R.id.addUser);
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
                        dialog.dismiss();
                        //user lementése
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
    }

    public void StartTimer(){
        timer = new Timer();
        InitializeTimerTask();
        timer.schedule(timertask,500, 1000); //500 ms után indul, 1000 ms interval
    }

    public void StopTimerTask(){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void InitializeTimerTask(){
        timertask = new TimerTask() {
            public void run() {
            handler.post(new Runnable() {
                public void run() {
                SearchString = userSearchInput.getText().toString();
                if(!SearchString.equals("") && !SearchString.equals(PSearchString)) { //nem üres és változott a legutóbbi lekérdezés óta

                    PSearchString = SearchString; //előző állapot felülírása

                    final ArrayList<Users> usersItems = new ArrayList<Users>();
                    adapter = new UsersAdapter(usersItems);
                    user_list.setAdapter(adapter);

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
                                    usersItems.add( new Users(temp.getString("username"), temp.getString("email")) );
                                }
                                adapter = new UsersAdapter(usersItems);
                                user_list.setAdapter(adapter);
                            }catch (Exception e) {
                                e.printStackTrace();
                                usersItems.add(new Users("Nincs találat!", ""));
                                adapter = new UsersAdapter(usersItems);
                                user_list.setAdapter(adapter);
                            }
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

    @Override
    protected void onPause() {
        super.onPause();
        StopTimerTask();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_project, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
