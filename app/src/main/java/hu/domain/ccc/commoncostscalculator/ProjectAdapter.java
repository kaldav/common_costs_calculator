package hu.domain.ccc.commoncostscalculator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rolu on 2015.03.25..
 */
public class ProjectAdapter extends BaseAdapter {


    private List<Projects> items;

    public ProjectAdapter(List<Projects> items) {
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = View.inflate(parent.getContext(),R.layout.listitem_project,null);
            holder= new ViewHolder();
            holder.dateTextView = (TextView) view.findViewById(R.id.projectStartDate);
            holder.descTextView = (TextView) view.findViewById(R.id.projectDescription);
            holder.nameTextView = (TextView) view.findViewById(R.id.projectName);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder)view.getTag();
        }


        Projects proji = items.get(i);

        holder.nameTextView.setText(proji.getName());
        holder.descTextView.setText(proji.getDescription());
        holder.dateTextView.setText(proji.getStartDate().toString());


        return view;
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView descTextView;
        TextView dateTextView;
    }
}


