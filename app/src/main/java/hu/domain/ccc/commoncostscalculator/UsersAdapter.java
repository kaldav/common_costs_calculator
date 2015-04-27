package hu.domain.ccc.commoncostscalculator;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
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
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
       if(view == null) {

           view = View.inflate(parent.getContext(), layoutResource ,null);
       }
        final Users user = items.get(position);
        TextView userNameTextView = (TextView)view.findViewById(R.id.username);
        userNameTextView.setText(user.getUserName());

        TextView emailTextView = (TextView)view.findViewById(R.id.name);
        emailTextView.setText(user.getName());

        if(layoutResource == R.layout.listitem_item)
        {

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    user.switchChecked();


                    if(user.isChecked())
                        v.setBackgroundColor(Color.CYAN);
                    else
                        v.setBackgroundColor(R.color.bg_login);

                }

            });

            if(user.isChecked())
                view.setBackgroundColor(Color.CYAN);
            else
                view.setBackgroundColor(R.color.bg_login);


           // CheckBox c = (CheckBox) view.findViewById(R.id.check);
            //if(c.isChecked())
              //  checked.add(user);

        }


        return view;
    }

    public void ClearItems()
    {
        items = null;
    }
}
