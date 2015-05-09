package hu.domain.ccc.commoncostscalculator;

import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

/**
 * Created by Rolu on 2015.04.28..
 */
public class ProjectConnectRefuseAdapter extends BaseAdapter {

    private List<Projects> items;

    public ProjectConnectRefuseAdapter(List<Projects> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    @Override
    public View getView(int i, View view, final ViewGroup parent) {
        ViewHolder holder;

        Projects proji = items.get(i);

        if (view == null) {
            view = View.inflate(parent.getContext(),R.layout.listitem_project_connect_refuse,null);
            holder= new ViewHolder();
            holder.descTextView = (TextView) view.findViewById(R.id.projectDescription);
            holder.nameTextView = (TextView) view.findViewById(R.id.projectName);
            holder.refuse = (Button) view.findViewById(R.id.elutasitButton);
            holder.connect = (Button) view.findViewById(R.id.csatlakozasButton);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder)view.getTag();
        }



        holder.nameTextView.setText(proji.getName());
        holder.descTextView.setText(proji.getDescription());
        holder.refuse.setTag(Integer.toString(i)+":"+Integer.toString(proji.getId()));
        holder.connect.setTag(Integer.toString(i)+":"+Integer.toString(proji.getId()));

        //elutasít egy projektet
        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ha elutasítja !! PHP ra vár, de már eltud távolítani, csak rosszat
                //int i = 0;
                //String projekt = ((TextView) parent.findViewById(R.id.projectName)).getText().toString();
                // while (!projekt.matches(items.get(i).getName().toString())) {
                //    i++;
               // }
               // items.remove(i);
               // notifyDataSetChanged();
                final String[] params = v.getTag().toString().split(":"); //Param 0: listitemId param 1: projiId

                SharedPreferences settings;
                String session;
                ArrayList<NameValuePair> data;
                String PrefFileName = "data";

                settings = parent.getContext().getSharedPreferences(PrefFileName, 0);
                session = settings.getString("session","");

                data = new ArrayList<>();
                data.add(new BasicNameValuePair("action", "refuse_project"));
                data.add(new BasicNameValuePair("session", session));
                data.add(new BasicNameValuePair("project_id", params[1]));

                Downloader connection = new Downloader(data);
                connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                    @Override
                    public void onDownloadSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                int res = response.getInt("success");
                                if (res==1)
                                {
                                    items.remove(Integer.parseInt(params[0]));
                                    notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(parent.getContext(), "Nem sikerült visszautasítani, probáld újra késöbb", Toast.LENGTH_LONG).show();
                                }
                            }
                         catch (Exception e) {
                                Toast.makeText(parent.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }


                    }

                    @Override
                    public void onDownloadFailed(String message) {
                        Toast.makeText(parent.getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

                connection.start();


            }
        });

        //csatlakozik egy projekthez
        holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ha elfogadja itt törénik majd valami !!

                //int i=0;
                //String projekt = ((TextView)parent.findViewById(R.id.projectName)).getText().toString();
                //while (projekt!=items.get(i).getName().toString()) {
                //    i++;
                //}
                final String[] params = v.getTag().toString().split(":"); //Param 0: listitemId param 1: projiId

                SharedPreferences settings;
                String session;
                ArrayList<NameValuePair> data;
                String PrefFileName = "data";

                settings = parent.getContext().getSharedPreferences(PrefFileName, 0);
                session = settings.getString("session","");

                data = new ArrayList<>();
                data.add(new BasicNameValuePair("action", "join_project"));
                data.add(new BasicNameValuePair("session", session));
                data.add(new BasicNameValuePair("project_id", params[1]));

                Downloader connection = new Downloader(data);
                connection.setOnConnectionListener(new Downloader.OnConnectionListener() {
                    @Override
                    public void onDownloadSuccess(String result) {
                        try {
                            //Ha 1 akkor sikeres
                            JSONObject response = new JSONObject(result);
                            int res = response.getInt("success");
                            if (res==1)
                            {
                                items.remove(Integer.parseInt(params[0]));
                                notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(parent.getContext(), "Nem sikerült visszautasítani, probáld újra késöbb", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(parent.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDownloadFailed(String message) {
                        Toast.makeText(parent.getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

                connection.start();
            }
        });

        return view;
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView descTextView;
        Button refuse;
        Button connect;

    }
}
