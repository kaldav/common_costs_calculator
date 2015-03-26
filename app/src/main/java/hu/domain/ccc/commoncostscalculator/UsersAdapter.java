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

    public UsersAdapter(List<Users> items) {
        this.items = items;
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
           view = View.inflate(parent.getContext(),R.layout.listitem_users ,null);
       }
       Users user = items.get(position);
        TextView userNameTextView = (TextView)view.findViewById(R.id.username);
        userNameTextView.setText(user.getUserName());

        TextView emailTextView = (TextView)view.findViewById(R.id.email);
        emailTextView.setText(user.getEmail());

        return view;
    }

    public void ClearItems()
    {
        items = null;
    }
}