package hu.domain.ccc.commoncostscalculator;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovács on 2015.05.07..
 */
public class Penzugyi_tetel_adapter extends BaseAdapter{

    private List<Penzugyi_tetel> items;
    int layoutResource; // a lista elemeket leíró layout
    ArrayList<Penzugyi_tetel> checked;


    public Penzugyi_tetel_adapter(List<Penzugyi_tetel> items, int layoutResource) {
        this.items = items;
        this.layoutResource = layoutResource;
        this.checked = new ArrayList<Penzugyi_tetel>();
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
        return 2;
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
        final Penzugyi_tetel ptetel = items.get(position);

        TextView kinekTartozomTextView = (TextView)myView.findViewById(R.id.kinek_tartozom);
        kinekTartozomTextView.setText(ptetel.getKinek());

        TextView mennyivelTartozomTextView = (TextView)myView.findViewById(R.id.mennyivel_tartozom);
        int osszeg = ptetel.getMennyivel();
        if (osszeg < 0)
        {
            //ő jön nekem
            mennyivelTartozomTextView.setText(Integer.toString(Math.abs(ptetel.getMennyivel())));
            mennyivelTartozomTextView.setTextColor(Color.rgb(30,120,40));
            //mennyivelTartozomTextView.setBackgroundColor(Color.WHITE);
        }
        else
        {
            //én jövök neki
            mennyivelTartozomTextView.setText(Integer.toString(Math.abs(ptetel.getMennyivel())));
            mennyivelTartozomTextView.setTextColor(Color.RED);
            //mennyivelTartozomTextView.setBackgroundColor(Color.WHITE);
        }



        return myView;
    }
}
