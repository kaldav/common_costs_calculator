package hu.domain.ccc.commoncostscalculator;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class UserSearchActivity extends ActionBarActivity {

    EditText userSearchInput;
    String PrefFileName = "data";
    ListView user_list;
    private UsersAdapter adapter;
    Timer timer;
    TimerTask timertask;
    String SearchString;
    String PSearchString;

    final Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        userSearchInput = (EditText) findViewById(R.id.user_search_edittext);
        user_list = (ListView) findViewById(R.id.user_search_list);



        userSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               StopTimerTask();
                StartTimer();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void StartTimer()
    {
        timer = new Timer();
        InitializeTimerTask();
        timer.schedule(timertask,500, 1000); //500 ms után indul, 1000 ms interval
    }

    public void StopTimerTask()
    {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void InitializeTimerTask()
    {
        timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SearchString = userSearchInput.getText().toString();
                        if(!SearchString.equals("") && !SearchString.equals(PSearchString)) { //nem üres és változott a legutóbbi lekérdezés óta

                            PSearchString = SearchString; //előző állapot felülírása

                            final ArrayList<Users> usersItems = new ArrayList<Users>();
                            adapter = new UsersAdapter(usersItems);
                            user_list.setAdapter(adapter);

                            SharedPreferences settings = getSharedPreferences(PrefFileName, 0);
                            String session = settings.getString("session","");
                            HashMap<String, String> data = new HashMap<String, String>();
                            data.put("action", "search_users");
                            data.put("session", session);
                            data.put("search", SearchString);
                            ServerConnect post = new ServerConnect(data);

                            try {
                                //JSON feldolgozása

                                JSONArray response = new JSONArray(post.execute("http://ccc.elitemagyaritasok.info").get());

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject temp = response.getJSONObject(i);

                                    usersItems.add( new Users(temp.getString("username"), temp.getString("email")) );
                                }

                                adapter = new UsersAdapter(usersItems);
                                user_list.setAdapter(adapter);



                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_search, menu);
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
