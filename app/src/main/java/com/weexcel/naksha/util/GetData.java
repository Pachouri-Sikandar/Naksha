package com.weexcel.naksha.util;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Ankit on 30-Jan-15.
 */
public class GetData extends AsyncTask<String,String,String>
{
    String url;
    Context cnt;
    String data;
    Result res;
    String place;
    public GetData(Context cnt,String url,Result res,String place)
    {
       this.cnt=cnt;
       this.url=url;
        this.res=res;
        this.place=place;
    }

    @Override
    protected String doInBackground(String... strings) {
        data=CommonUtil.getJSON(url);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
      DialogClass.getMaterialDialog(cnt,place);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //DialogClass.Logout();
        DialogClass.materialLogout();
        res.TransferData(data);
    }
}
