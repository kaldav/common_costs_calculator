package hu.domain.ccc.commoncostscalculator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        return items.get(i).getId();
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
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder)view.getTag();
        }


        Projects proji = items.get(i);

        if(proji.isClosed())
            holder.imageView.setVisibility(View.VISIBLE);
        holder.nameTextView.setText(proji.getName());
        holder.descTextView.setText(proji.getDescription());
        String times[] = proji.getStartTime().toString().split(" ");
        holder.dateTextView.setText(times[5]+" "+times[1]+ " "+ times[2]);


        return view;
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView descTextView;
        TextView dateTextView;
        ImageView imageView;
    }
}


