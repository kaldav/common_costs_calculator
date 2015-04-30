package hu.domain.ccc.commoncostscalculator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

        if (view == null) {
            view = View.inflate(parent.getContext(),R.layout.listitem_project_connect_refuse,null);
            holder= new ViewHolder();
            holder.descTextView = (TextView) view.findViewById(R.id.projectDescription);
            holder.nameTextView = (TextView) view.findViewById(R.id.projectName);
            holder.refuse = (Button) view.findViewById(R.id.elutasitButton);
            holder.connect = (Button) view.findViewById(R.id.csatlakozasButton);
            holder.refuse.setTag(Integer.toString(i));
            holder.connect.setTag(Integer.toString(i));
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder)view.getTag();
        }


        Projects proji = items.get(i);

        holder.nameTextView.setText(proji.getName());
        holder.descTextView.setText(proji.getDescription());
        holder.refuse.setTag(Integer.toString(i));
        holder.connect.setTag(Integer.toString(i));

        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ha elutasítja !! PHP ra vár, de már eltud távolítani, csak rosszat
                int i = 0;
                String projekt = ((TextView) parent.findViewById(R.id.projectName)).getText().toString();
                while (!projekt.matches(items.get(i).getName().toString())) {
                    i++;
                }
                items.remove(i);
                notifyDataSetChanged();
            }
        });

        holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ha elfogadja itt törénik majd valami !! PHP ra vár, de már eltud távolítani csak rosszat
                //int i=0;
                //String projekt = ((TextView)parent.findViewById(R.id.projectName)).getText().toString();
                //while (projekt!=items.get(i).getName().toString()) {
                //    i++;
                //}
                items.remove((int)v.getTag());
                notifyDataSetChanged();
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
