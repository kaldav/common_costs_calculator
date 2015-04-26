package hu.domain.ccc.commoncostscalculator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gergely on 2015.03.26..
 */
public class UsersAdapter extends BaseAdapter{

    private List<Users> items;
    int layoutResource; // a lista elemeket leíró layout

    public UsersAdapter(List<Users> items, int layoutResource) {
        this.items = items;
        this.layoutResource = layoutResource;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
       if(view == null) {

           view = View.inflate(parent.getContext(), layoutResource ,null);
       }
       Users user = items.get(position);
        TextView userNameTextView = (TextView)view.findViewById(R.id.username);
        userNameTextView.setText(user.getUserName());

        TextView emailTextView = (TextView)view.findViewById(R.id.name);
        emailTextView.setText(user.getName());

        return view;
    }

    public void ClearItems()
    {
        items = null;
    }
}
