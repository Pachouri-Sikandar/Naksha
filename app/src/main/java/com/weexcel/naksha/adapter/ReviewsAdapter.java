package com.weexcel.naksha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weexcel.naksha.R;
import com.weexcel.naksha.helper.ReviewInfoResultObject;
import com.weexcel.naksha.model.MenuItem;

import java.util.ArrayList;

/**
 * Created by Ankit on 05-Feb-15.
 */
public class ReviewsAdapter extends BaseAdapter {
    final Context cont;
    ImageView showImage;
    TextView name, rating;
    ArrayList<ReviewInfoResultObject> list;

    public ReviewsAdapter(Context cont, ArrayList<ReviewInfoResultObject> list) {
        this.cont = cont;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vv = li.inflate(R.layout.list_reviews, viewGroup, false);
        showImage = (ImageView) vv.findViewById(R.id.imageUser);
        name = (TextView) vv.findViewById(R.id.txtName);
        rating = (TextView) vv.findViewById(R.id.txtRating);
        ReviewInfoResultObject rr = list.get(i);

        showImage.setImageResource(R.drawable.ic_user);
        name.setText(rr.getAuthorIndividual());
        rating.setText(rr.getRatingIndividual());
        return vv;
    }
}
