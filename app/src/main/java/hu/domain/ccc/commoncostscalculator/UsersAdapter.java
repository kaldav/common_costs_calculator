package hu.domain.ccc.commoncostscalculator;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gergely on 2015.03.26..
 */
public class UsersAdapter extends BaseAdapter{

    private List<Users> items;
    int layoutResource; // a lista elemeket leíró layout
    ArrayList<Users> checked;

    public ArrayList<Users> getChecked() {
        return checked;
    }

    public UsersAdapter(List<Users> items, int layoutResource) {
        this.items = items;
        this.layoutResource = layoutResource;
        this.checked = new ArrayList<Users>();
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
        return (long)Integer.parseInt(items.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        final View myView;
       if(convertView == null) {
           myView = View.inflate(parent.getContext(),layoutResource, null);
           holder = new ViewHolder();

           holder.userNameTextView = (TextView)myView.findViewById(R.id.username);
           holder.nameTextView = (TextView)myView.findViewById(R.id.name);
           myView.setTag(holder);
       }
        else
       {
           myView = convertView;
           holder = (ViewHolder)convertView.getTag();
       }
        final Users user = items.get(position);

        holder.userNameTextView.setText(user.getUserName());
        holder.nameTextView.setText(user.getName());

        if(layoutResource == R.layout.listitem_item_add_user)
        {

            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    user.switchChecked();


                    if(user.isChecked())
                        myView.setBackgroundColor(Color.CYAN);
                    else
                        myView.setBackgroundColor(v.getResources().getColor(R.color.bg_login));

                }

            });

            if(user.isChecked())
                myView.setBackgroundColor(Color.CYAN);
            else
                myView.setBackgroundColor(myView.getResources().getColor(R.color.bg_login));


        }


        return myView;
    }

    static class ViewHolder {
        TextView userNameTextView;
        TextView nameTextView;
    }

    public void ClearItems()
    {
        items = null;
    }
}
