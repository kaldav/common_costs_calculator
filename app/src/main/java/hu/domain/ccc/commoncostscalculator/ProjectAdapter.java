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
        if (view == null) {
            view = View.inflate(parent.getContext(),R.layout.listitem_project,null);
        }

        Projects proji = items.get(i);

        TextView nameTextView = (TextView) view.findViewById(R.id.projectName);
        nameTextView.setText(proji.getName());
        TextView descTextView = (TextView) view.findViewById(R.id.projectDescription);
        descTextView.setText(proji.getDescription());
        TextView dateTextView = (TextView) view.findViewById(R.id.projectStartDate);
        dateTextView.setText(proji.getStartDate().toString());


        return view;
    }
}
