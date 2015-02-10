package com.weexcel.naksha.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.squareup.picasso.Picasso;
import com.weexcel.naksha.R;
import com.weexcel.naksha.controller.fragment.NavigationDrawerFragment;
import com.weexcel.naksha.helper.GetLocation;
import com.weexcel.naksha.helper.NavigationDrawerCallbacks;
import com.weexcel.naksha.helper.BasicInfoResultObject;
import com.weexcel.naksha.helper.PlaceJSONParser;
import com.weexcel.naksha.helper.ReviewInfoResultObject;
import com.weexcel.naksha.util.CommonUtil;
import com.weexcel.naksha.util.DialogClass;
import com.weexcel.naksha.util.GetData;
import com.weexcel.naksha.util.Result;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyActivity extends ActionBarActivity implements NavigationDrawerCallbacks, Result {

    GoogleMap map;
    GetLocation getLocation;
    android.support.v7.widget.Toolbar mToolbar;
    NavigationDrawerFragment mNavigationDrawerFragment;
    String selectedPlace = "";
    ArrayList<BasicInfoResultObject> list = new ArrayList<BasicInfoResultObject>();
    ArrayList<ReviewInfoResultObject> listReviews = new ArrayList<ReviewInfoResultObject>();
    ImageView icon;

    TextView name, openNow;
    MarkerOptions markerOptions;
    View viewForDetails;

    Double gotLongitude, gotLatitude;
    String gotSinglePlaceID = "";
    String gotName = "";
    String gotIcon = "";
    String gotOpenNow = ""; // this is status...
    int gotRating = 0;
    long gotRadius;
    String gotAddressFromGeoLocation = "";
    String gotWebsite = "Website Link Missing";
    String gotPhoneNumber = "Contact Number Missing";
    String gotAuthorName = "";
    String gotAuthorURL = "";
    float gotAverageRating = 0;
    int gotPriceLevel;
    String selectedPlaceNameForIntent = "";
    double lati = 0, longi = 0;
    int checkingAPIHit = 0;
    Marker mainMarker;
    int differentMarkerIcon;

    private static final int SETTINGS_RESULT = 1;

    private MenuItem myActionMenuItem;
    private AutoCompleteTextView myActionEditText;
    PlacesTask placesTask;
    ParserTask parserTask;

    private static String API_KEY = "AIzaSyD9ghcPBIOoS5_O7x51ROlqb15pkxyBkHw";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        getLocation = new GetLocation(this);


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setBuildingsEnabled(true);

        DialogClass.getMaterialDialog(this, "Current Location");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(10000);
                    gotLatitude = getLocation.returnLocation().getLatitude();
                    gotLongitude = getLocation.returnLocation().getLongitude();
                    System.out.println("Location in thread: " + gotLatitude + "---" + gotLongitude);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLocation.returnLocation().getLatitude(), getLocation.returnLocation().getLongitude()), 12.0f));
                        }
                    });


                } catch (Exception e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogClass.materialLogout();
                    }
                });


            }
        }).start();

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);


        viewForDetails = getLayoutInflater().inflate(R.layout.details_on_marker, null, false);

        icon = (ImageView) viewForDetails.findViewById(R.id.icon);
        name = (TextView) viewForDetails.findViewById(R.id.name);
        openNow = (TextView) viewForDetails.findViewById(R.id.openNow);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, final String itemName)
    {
        selectedPlaceNameForIntent = itemName;
        selectedPlace = itemName;

        selectedPlace = selectedPlace.toLowerCase();
        if (selectedPlace.equalsIgnoreCase("airport"))
        {
            differentMarkerIcon = R.drawable.ic_marker_airport;
        }
        if (selectedPlace.equalsIgnoreCase("bank"))
        {
            differentMarkerIcon = R.drawable.ic_marker_bank;
        }
        if (selectedPlace.equalsIgnoreCase("bar"))
        {
            differentMarkerIcon = R.drawable.ic_marker_bar;
        }
        if (selectedPlace.equalsIgnoreCase("bus station"))
        {
            selectedPlace = "bus_station";
            differentMarkerIcon = R.drawable.ic_marker_busstop;
        }
        if (selectedPlace.equalsIgnoreCase("Coffee Shop"))
        {
            selectedPlace = "cafe";
            differentMarkerIcon = R.drawable.ic_marker_coffeeshop;
        }
        if (selectedPlace.equalsIgnoreCase("Fire Station"))
        {
            selectedPlace = "fire_station";
            differentMarkerIcon = R.drawable.ic_marker_firestation;
        }
        if (selectedPlace.equalsIgnoreCase("Food Court"))
        {
            selectedPlace = "food";
            differentMarkerIcon = R.drawable.ic_marker_food;
        }
        if (selectedPlace.equalsIgnoreCase("Gas Station"))
        {
            selectedPlace = "gas_station";
            differentMarkerIcon = R.drawable.ic_marker_gasstation;
        }
        if (selectedPlace.equalsIgnoreCase("hospital"))
        {
            differentMarkerIcon = R.drawable.ic_marker_hospital;
        }
        if (selectedPlace.equalsIgnoreCase("Movie Theater"))
        {
            selectedPlace = "movie_theater";
            differentMarkerIcon = R.drawable.ic_marker_movie_theater;
        }
        if (selectedPlace.equalsIgnoreCase("parking"))
        {
            differentMarkerIcon = R.drawable.ic_marker_parking;
        }
        if (selectedPlace.equalsIgnoreCase("Police Station"))
        {
            selectedPlace = "police";
            differentMarkerIcon = R.drawable.ic_marker_police;
        }
        if (selectedPlace.equalsIgnoreCase("Railway Station"))
        {
            selectedPlace = "train_station";
            differentMarkerIcon = R.drawable.ic_marker_railway_station;
        }
        if (selectedPlace.equalsIgnoreCase("school"))
        {
            differentMarkerIcon = R.drawable.ic_marker_school;
        }
        if (selectedPlace.equalsIgnoreCase("store"))
        {
            differentMarkerIcon = R.drawable.ic_marker_store;
        }
        System.out.println("Place: " + selectedPlace);

//        CommonUtil.showToast(getApplicationContext(), "Selected: "+selectedPlace);

        if(getLocation.returnLocation()!=null){
            gotLatitude = getLocation.returnLocation().getLatitude();
            gotLongitude = getLocation.returnLocation().getLongitude();
        }
        gotRadius = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("RADIUS", 1000);

        if (CommonUtil.checkInternetConnection(getApplicationContext()))
        {
            GetData data = new GetData(MyActivity.this, "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + gotLatitude + "," + gotLongitude + "&radius=" + gotRadius + "&types=" + selectedPlace + "&key="+API_KEY, MyActivity.this, itemName);
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            checkingAPIHit = 1;
        } else {
            CommonUtil.showSnackBar(MyActivity.this, "Check your Internet Connection");
//            CommonUtil.showToast(MyActivity.this, "Check your Internet Connection");
        }
    }

    @Override
    public void onBackPressed()
    {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public void TransferData(String data) {

        System.out.println("data" + data);
        if (data != null) {

            if (checkingAPIHit == 1) {

                map.clear();
                list.clear();

                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray results = jo.optJSONArray("results");
                    if (results.length() == 0) {
                        CommonUtil.showSnackBar(MyActivity.this, "No result found in the searched radius");
//                        CommonUtil.showToast(this, "No result found in the searched radius");
                    } else {

                        System.out.println("Result Length: " + results.length());
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject object = results.optJSONObject(i);
                            JSONObject location = object.optJSONObject("geometry").optJSONObject("location");
                            Double latitude = location.optDouble("lat");
                            Double longitude = location.optDouble("lng");
                            String name = object.optString("name");
                            String placeID = object.optString("place_id");

                            System.out.println("Lati: "+latitude+"---"+longitude);

                            System.out.println("PLaceID: " + placeID);

                            JSONObject opening = object.optJSONObject("opening_hours");
                            boolean openNow;
                            if (opening != null) {
                                openNow = opening.optBoolean("open_now");
                            } else {
                                openNow = false;
                            }

                            String openNowString = "";
                            if (openNow == true) {
                                openNowString = "Open";
                            } else {
                                openNowString = "Close";
                            }

                            String image = object.optString("icon");
                            int priceLevel = 0;

                            if (object.optInt("price_level") != 0)
                            {
                                priceLevel = object.optInt("price_level");
                            }

                            BasicInfoResultObject basicInfoResultObject = new BasicInfoResultObject(latitude, longitude, name, image, openNowString, placeID, priceLevel);
                            list.add(basicInfoResultObject);
                        }



                        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                return viewForDetails;
                            }
                        });

                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                mainMarker = marker;

                               // System.out.println("Getting clicked name: " + marker.getTitle());

                                gotName = marker.getTitle();

                                System.out.println("List Size coming: " + list.size());

                                for (int iter = 0; iter < list.size(); iter++) {
                                    if (list.get(iter).getName().equalsIgnoreCase(gotName)) {
                                        gotLatitude = list.get(iter).getLatitude();
                                        gotLongitude = list.get(iter).getLongitude();
                                        gotOpenNow = list.get(iter).getOpenNow();
                                        gotIcon = list.get(iter).getImage();
                                        gotSinglePlaceID = list.get(iter).getPlaceID();
                                        gotPriceLevel = list.get(iter).getPriceLevel();
                                    }
                                }

                                gotAddressFromGeoLocation = getAddressFromLocation(gotLatitude,gotLongitude,MyActivity.this,new GeocoderHandler());

                                name.setText(gotName);
                                openNow.setText(gotAddressFromGeoLocation);
                                try {
                                    Picasso.with(getApplicationContext()).load(gotIcon).into(icon);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mainMarker.showInfoWindow();

                                return true;
                            }
                        });

                        for (int i = 0; i < list.size(); i++) {

                            map.addMarker(new MarkerOptions().title(list.get(i).getName()).position
                                    (new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude())
                                    ).icon(BitmapDescriptorFactory.fromResource(differentMarkerIcon)));
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLocation.returnLocation().getLatitude(), getLocation.returnLocation().getLongitude()), 12.0f));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

                else if (checkingAPIHit == 2)
                {

                    try {
                        JSONObject jo = new JSONObject(data);
                        JSONObject result = jo.optJSONObject("result");

                        if (result == null) {
                        } else {

                                JSONArray reviews = result.optJSONArray("reviews");

                            if (reviews != null && reviews.length() != 0)
                            {
                                for (int i = 0; i < reviews.length(); i++)
                                {
                                 JSONObject object = reviews.optJSONObject(i);
                                    if (object.optString("author_name") != null)
                                    {
                                        gotAuthorName = object.optString("author_name");
                                    }
                                    else
                                    {

                                        gotAuthorName = "Name Missing";
                                    }
                                    if (object.optString("author_url") != null)
                                    {
                                        gotAuthorURL = object.optString("author_url");
                                    }
                                    else
                                    {

                                        gotAuthorURL = "URL Missing";
                                    }

                                    JSONArray aspects = object.optJSONArray("aspects");

                                    if (aspects != null && aspects.length() != 0)
                                    {
                                            JSONObject oo = aspects.optJSONObject(0);
                                            if (oo.optInt("rating") != 0)
                                            {
                                                gotRating = oo.optInt("rating");
                                            }
                                        else
                                            {
                                                gotRating = 0;
                                            }
                                    }
                                    ReviewInfoResultObject reviewInfoResultObject = new ReviewInfoResultObject(gotSinglePlaceID, gotWebsite, gotRating, gotAuthorName, gotAuthorURL);
                                    listReviews.add(reviewInfoResultObject);
                                }
                            }
                            else
                            {
                                gotAuthorName = "Name Missing";
                                gotAuthorURL = "URL Missing";
                                gotRating = 0;
                                ReviewInfoResultObject reviewInfoResultObject = new ReviewInfoResultObject(gotSinglePlaceID, gotWebsite, gotRating, gotAuthorName, gotAuthorURL);
                                listReviews.add(reviewInfoResultObject);
                            }

                            int tempRating = 0;
                            gotAverageRating = 0;

                            for (int ll = 0; ll < listReviews.size(); ll++)
                            {
                                tempRating = listReviews.get(ll).getRatingIndividual();
                                gotAverageRating = gotAverageRating + tempRating;
                                System.out.println("rating: "+listReviews.get(ll).getRatingIndividual());
                                System.out.println("author: "+listReviews.get(ll).getAuthorIndividual());
                                System.out.println("url: "+listReviews.get(ll).getAuthorURL());
                            }

                            System.out.println("Total: "+gotAverageRating);

                            gotAverageRating = gotAverageRating / listReviews.size();

                            System.out.println("Average: "+gotAverageRating);

                            if (result.optString("website").equalsIgnoreCase("")) {
                                gotWebsite = "Website Link Missing";
                            }
                            else{
                                gotWebsite = result.optString("website");
                            }

                            if (result.optString("international_phone_number").equals(""))
                            {
                                gotPhoneNumber = "Contact Number Missing";
                            }
                            else
                            {
                                gotPhoneNumber = result.optString("international_phone_number");
                            }

                            System.out.println("Web: "+gotWebsite+"---phone: "+gotPhoneNumber);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent openDetails = new Intent(MyActivity.this, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", gotName);
                    bundle.putString("ADDRESS", gotAddressFromGeoLocation);
                    bundle.putFloat("AVGRATING", gotAverageRating);
                    bundle.putInt("PRICELEVEL", gotPriceLevel);
                    bundle.putString("PHONENUMBER", gotPhoneNumber);
                    bundle.putString("WEBSITE", gotWebsite);
                    bundle.putString("STATUS", gotOpenNow);
                    bundle.putString("SELECTEDPLACE", selectedPlaceNameForIntent);
                    bundle.putSerializable("LISTVALUES",listReviews);
                    openDetails.putExtras(bundle);
                    startActivity(openDetails);

                }


            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    listReviews.clear();
                    GetData getReviewData = new GetData(MyActivity.this, "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + gotSinglePlaceID + "&key="+API_KEY, MyActivity.this, "details");
                    getReviewData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                    checkingAPIHit = 2;
                }
            });

        } else {
            CommonUtil.showSnackBar(MyActivity.this, getString(R.string.connection_timeout));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my, menu);

        // Here we get the action view we defined
        myActionMenuItem = menu.findItem(R.id.action_search);
        View actionView = myActionMenuItem.getActionView();

        // We then get the edit text view that is part of the action view
        if (actionView != null) {
            myActionEditText = (AutoCompleteTextView) actionView.findViewById(R.id.editTextSearchBox);
            myActionEditText.setThreshold(1);

            if (myActionEditText != null) {

                myActionEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        placesTask = new PlacesTask();
                        placesTask.execute(charSequence.toString());

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent callSettings = new Intent(this, SettingsActivity.class);
            startActivity(callSettings);
        }

        return super.onOptionsItemSelected(item);
    }
    public  String getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        {
                            sb.append(address.getAddressLine(i)).append(" ");
                        }
                        if (address.getLocality() != null)
                            sb.append(address.getLocality()).append(" ");

                        if (address.getPostalCode() != null)
                            sb.append(address.getPostalCode()).append(" ");

                        if (address.getCountryName() != null)
                            sb.append(address.getCountryName());

                        gotAddressFromGeoLocation = sb.toString();
                        System.out.println("result: "+gotAddressFromGeoLocation);
                    }
                } catch (Exception e) {
                    Log.e("gaga", "Unable connect to GeoCoder", e);
                } }
        };
        thread.start();
        return gotAddressFromGeoLocation;
}
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;

                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");

            System.out.println("data"+locationAddress);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";
            // Obtain browser key from https://code.google.com/apis/console
            String key = "key="+API_KEY;
            String input = "";
            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String types = "types=geocode";
            String sensor = "sensor=false";
            String parameters = input + "&" + types + "&" + sensor + "&" + key;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;
            try {
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }



    public class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { R.id.showAutoSuggest };
            // Creating a SimpleAdapter for the AutoCompleteTextView
            final SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, R.layout.items_autocomplete, from, to);
            // Setting the adapter
            myActionEditText.setAdapter(adapter);

            myActionEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String name = myActionEditText.getText().toString();
                    System.out.println("Clicked: "+name);
                    getLatNLongFromAddress(name);
                }
            });
        }
    }

    public void getLatNLongFromAddress(final String address) {
        map.clear();
        DialogClass.getMaterialDialog(this, address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(10000);

                    Geocoder coder = new Geocoder(MyActivity.this);
                    ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(address, 5);
                    for (Address add : adresses) {
                        longi = add.getLongitude();
                        lati = add.getLatitude();
                    }
                    System.out.println("Getting addresssss: " + longi + "---" + lati);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            map.addMarker(new MarkerOptions()
                                    .title(address)
                                    .position(new LatLng(lati, longi))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_simple_marker)));

                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, longi), 12.0f));

                        }
                    });


                } catch (Exception e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogClass.materialLogout();
                    }
                });
            }
        }).start();
    }
}
