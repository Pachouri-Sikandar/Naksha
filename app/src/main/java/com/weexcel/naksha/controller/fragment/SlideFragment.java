package com.weexcel.naksha.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weexcel.naksha.R;
import com.weexcel.naksha.adapter.MenuItems_Adapter;
import com.weexcel.naksha.helper.CommunicatorSlide;
import com.weexcel.naksha.model.MenuItem;
import com.weexcel.naksha.util.CommonUtil;

import java.util.ArrayList;

public class SlideFragment extends android.app.Fragment implements AdapterView.OnItemClickListener
{
    ListView lv;
    ArrayList<MenuItem> list;
    MenuItems_Adapter adapter;
    CommunicatorSlide communicatorSlideListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.list_items_menu, null);
        lv = (ListView)v.findViewById(R.id.listitems_menu);
        lv.setOnItemClickListener(this);
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list = new ArrayList<MenuItem>();
        list.add(new MenuItem(R.drawable.ic_launcher, "Bank"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Bar"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Bus Stop"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Coffee Shop"));
        list.add(new MenuItem(R.drawable.ic_launcher, "School"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Store"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Food"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Movie Theater"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Railway Station"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Airport"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Police Station"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Hospital"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Fire Station"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Gas Station"));
        list.add(new MenuItem(R.drawable.ic_launcher, "Parking"));

        adapter = new MenuItems_Adapter(getActivity(), list, R.layout.items_menu);
        lv.setAdapter(adapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicatorSlideListener =(CommunicatorSlide)getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i)
        {
            case 0:
                communicatorSlideListener.hideDrawer();
                CommonUtil.showToast(getActivity(), "Clicked 1st");
                break;
            case 1:
                CommonUtil.showToast(getActivity(), "Clicked 2nd");
                break;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        communicatorSlideListener = null;
    }
}
