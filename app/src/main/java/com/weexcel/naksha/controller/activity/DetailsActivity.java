package com.weexcel.naksha.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.weexcel.naksha.R;
import com.weexcel.naksha.adapter.EntityReviewsAdapter;
import com.weexcel.naksha.adapter.ReviewsAdapter;
import com.weexcel.naksha.helper.ReviewInfoResultObject;
import com.weexcel.naksha.util.CommonUtil;
import com.weexcel.naksha.util.GetData;
import com.weexcel.naksha.util.MaterialRippleLayout;
import com.weexcel.naksha.util.Result;

import java.util.ArrayList;


public class DetailsActivity extends ActionBarActivity {
    TextView showName, showAddress, showRating, showPriceLevel, showWebsite, showStatus,tv_place_name;
    RatingBar averageRatingBar;
    //ListView listViewReviews;
    MaterialRippleLayout btnCall, btnShare;
    Intent gettingValuesIntent;
    Bundle gettingValuesBundle;
    RecyclerView rev_reviews;
    String name, address, phoneNumber, website, status;
    int indRating, priceLevel;
    float averageRating;
    ArrayList<ReviewInfoResultObject> listReview;
    ReviewsAdapter reviewsAdapter;
    ImageView actionBarIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        rev_reviews=(RecyclerView)findViewById(R.id.rev_reviews);
        rev_reviews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        rev_reviews.setHasFixedSize(true);

        rev_reviews.addItemDecoration(new SpacesItemDecoration(8));
        LinearLayoutManager manager=new LinearLayoutManager(DetailsActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rev_reviews.setLayoutManager(manager);
        actionBarIcon = (ImageView) findViewById(R.id.img_SimpleMarkerActionBar);
        tv_place_name=(TextView)findViewById(R.id.tv_place_name);
        showName = (TextView) findViewById(R.id.txt_showTitle);
        showAddress = (TextView) findViewById(R.id.txt_showAddress);
        showRating = (TextView) findViewById(R.id.txt_showRating);
        averageRatingBar = (RatingBar) findViewById(R.id.averageRatingBar);
        showPriceLevel = (TextView) findViewById(R.id.txt_showPriceLevel);
        showWebsite = (TextView) findViewById(R.id.txt_showWebsite);
        showStatus = (TextView) findViewById(R.id.txt_showStatus);
        btnCall = (MaterialRippleLayout) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent call=new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:"+phoneNumber));
                    startActivity(call);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnShare = (MaterialRippleLayout) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_via)));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
       // listViewReviews = (ListView) findViewById(R.id.listShowReviews);

        if (savedInstanceState == null)
        {
            gettingValuesBundle = getIntent().getExtras();
        }
        else
        {
            System.out.println("inside not onsaved instance");
            gettingValuesBundle = savedInstanceState;
            System.out.println("getting"+gettingValuesBundle);
        }

        name = gettingValuesBundle.getString("NAME");
        address = gettingValuesBundle.getString("ADDRESS");
        averageRating = gettingValuesBundle.getFloat("AVGRATING");
        priceLevel = gettingValuesBundle.getInt("PRICELEVEL");
        phoneNumber = gettingValuesBundle.getString("PHONENUMBER");
        website = gettingValuesBundle.getString("WEBSITE");
        status = gettingValuesBundle.getString("STATUS");
        listReview = (ArrayList<ReviewInfoResultObject>) gettingValuesBundle.getSerializable("LISTVALUES");
        if(listReview!=null && listReview.size() > 0){
            rev_reviews.setVisibility(View.VISIBLE);
            rev_reviews.setAdapter(new EntityReviewsAdapter(this,listReview));
        }
        else{
            rev_reviews.setVisibility(View.GONE);
        }
        showName.setText(name);
        showAddress.setText(address);
        if (averageRating != 0)
        {
            showRating.setText("Rating: ");
            averageRatingBar.setRating(averageRating);
        }
        else
        {
            showRating.setText("Rating: Not Available");
        }
        if (priceLevel != 0)
        {
            showPriceLevel.setText(" "+priceLevel);
        }
        else
        {
            showPriceLevel.setText("Price Level: Not Available");
        }
        tv_place_name.setText(gettingValuesBundle.getString("SELECTEDPLACE"));
        showWebsite.setText(website);
        showStatus.setText(status);


        actionBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(gettingValuesBundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        public int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = space;
            if(parent.getChildPosition(view) == 0)
                outRect.top = space;
        }
    }


}
