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
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final View myView;
       if(view == null) {

           myView = View.inflate(parent.getContext(), layoutResource ,null);
       }
        else
       {
           myView = view;
       }
        final Users user = items.get(position);
        TextView userNameTextView = (TextView)myView.findViewById(R.id.username);
        userNameTextView.setText(user.getUserName());

        TextView emailTextView = (TextView)myView.findViewById(R.id.name);
        emailTextView.setText(user.getName());

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

    public void ClearItems()
    {
        items = null;
    }
}
