package hu.domain.ccc.commoncostscalculator;

import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Gergely on 2015.04.28..
 */
public class ItemsAdapter extends BaseAdapter {
    ArrayList<Items> itemList;

    public ItemsAdapter(ArrayList<Items> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = View.inflate(parent.getContext(),R.layout.listitem_item, null);
            holder = new ViewHolder();
            holder.tetel_elnevezes = (TextView) convertView.findViewById(R.id.tetel_elnevezese);
            holder.tetel_osszeg = (TextView) convertView.findViewById(R.id.tetel_osszege);
            convertView.setTag(holder);
        }
        else
        {
           holder = (ViewHolder)convertView.getTag();
        }

        Items item = itemList.get(position);

        holder.tetel_elnevezes.setText(item.name);
        holder.tetel_osszeg.setText(Integer.toString(item.sum));



        return convertView;
    }

    static class ViewHolder {
        TextView tetel_elnevezes;
        TextView tetel_osszeg;
    }
}
