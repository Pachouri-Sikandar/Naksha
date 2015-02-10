package com.weexcel.naksha.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weexcel.naksha.R;
import com.weexcel.naksha.helper.ReviewInfoResultObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 06-Feb-15.
 */
public class EntityReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    ArrayList<ReviewInfoResultObject> reviews_list;
    Context context;
    LayoutInflater inflator;
    public EntityReviewsAdapter(Context context,ArrayList<ReviewInfoResultObject> reviews_list){
        this.context=context;
        inflator=LayoutInflater.from(context);
        this.reviews_list=reviews_list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_ITEM)
        {
            View v = inflator.inflate(R.layout.list_reviews, null);
            return new ReviewsViewHolder(v);
        }
        else if (i == TYPE_HEADER)
        {
            View v = inflator.inflate(R.layout.header_recyclerview, null);
            return new ReviewsViewHolder(v);
        }

        throw new RuntimeException("there is no type match");
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof ReviewsViewHolder)
        {
//            ReviewInfoResultObject resultobject = getItem(i);
            ReviewsViewHolder.txtName.setText("lol");

//         ReviewsViewHolder.txtRating.setText("Rating:"+" "+resultobject.getRatingIndividual()+"");
//            ReviewsViewHolder.indRating.setRating(resultobject.getRatingIndividual());
        }
        if (holder instanceof ReviewsHeader)
        {
            ReviewsHeader.txtHeader.setText("Haoooo");
        }
    }

    @Override
    public int getItemCount() {
        return reviews_list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    public static class ReviewsViewHolder extends RecyclerView.ViewHolder
    {
        protected static TextView txtName;
        protected static TextView txtRating;
        protected static ImageView imageUser;
        protected static RatingBar indRating;

        public ReviewsViewHolder(View v)
        {
            super(v);
            txtName=(TextView)v.findViewById(R.id.txtName);
            txtRating=(TextView)v.findViewById(R.id.txtRating);
            imageUser=(ImageView)v.findViewById(R.id.imageUser);
            indRating = (RatingBar) v.findViewById(R.id.individualRatingBar);
        }
    }

    public static class ReviewsHeader extends RecyclerView.ViewHolder
    {

        protected static TextView txtHeader;

        public ReviewsHeader(View v) {
            super(v);
            txtHeader = (TextView)v.findViewById(R.id.headerRecyclerView);
        }
    }
}
