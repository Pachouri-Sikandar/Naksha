package com.weexcel.naksha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weexcel.naksha.R;
import com.weexcel.naksha.model.MenuItem;

import java.util.ArrayList;


public class MenuItems_Adapter extends BaseAdapter {
    final Context cont;
    ArrayList<MenuItem> items;
    int layout;
    TextView textview;
    ImageView imageview;
    LinearLayout clicked;
    int newposition = -1;


    public MenuItems_Adapter(Context cont, ArrayList<MenuItem> items, int layout) {
        this.cont = cont;
        this.items = items;
        this.layout = layout;
    }

    public MenuItems_Adapter(Context cont) {
        this.cont = cont;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vv = li.inflate(R.layout.items_menu, parent, false);
        textview = (TextView) vv.findViewById(R.id.txtview);
        imageview = (ImageView) vv.findViewById(R.id.imgview);
        final MenuItem pos = items.get(position);
        textview.setText(pos.getTitle());
        imageview.setImageResource(pos.getImage());
        return vv;
    }
}
