package com.weexcel.naksha.util;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.weexcel.naksha.R;

/**
 * Created by Ankit on 30-Jan-15.
 */
public class DialogClass
{
    static ProgressDialog dialog;
    @TargetApi(Build.VERSION_CODES.L)

    public static void getMaterialDialog(Context cnt, String place)
    {
        View v=LayoutInflater.from(cnt).inflate(R.layout.material_dialog,null);
        TextView text_ShowDialogMessage=(TextView)v.findViewById(R.id.text_ShowDialogMessage);
        text_ShowDialogMessage.setText("Looking for "+place+"...");
        if (dialog == null || !dialog.isShowing())
        {
            dialog = new ProgressDialog(cnt,ProgressDialog.THEME_HOLO_LIGHT);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(cnt.getResources().getColor(R.color.transparent)));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            dialog.setContentView(v);
        }
    }

    public static void materialLogout() {
        if(dialog!=null || dialog.isShowing())
        {
            dialog.dismiss();
        }

    }
}
