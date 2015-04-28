package hu.domain.ccc.commoncostscalculator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = View.inflate(parent.getContext(),R.layout.listitem_project_connect_refus,null);
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


        Projects proji = items.get(i);

        holder.nameTextView.setText(proji.getName());
        holder.descTextView.setText(proji.getDescription());

        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ha elutasítja !! PHP ra vár
            }
        });

        holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ha elfogadja itt törénik majd valami !! PHP ra vár
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
