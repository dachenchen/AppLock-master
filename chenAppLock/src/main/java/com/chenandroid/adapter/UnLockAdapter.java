package com.chenandroid.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenandroid.App;
import com.chenandroid.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 程序item
 */
public class UnLockAdapter extends BaseQuickAdapter<App> {
    public UnLockAdapter(List<App> data) {
        super(R.layout.list_item, data);
    }
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/chen";
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final App app) {
         baseViewHolder.setText(R.id.name,app.getName());
        ImageView view = baseViewHolder.getView(R.id.icon);
        Log.e(TAG, "convert: " + app.getIcon() );
        Button view1 = baseViewHolder.getView(R.id.button);
        view1.setText("加锁");
        view1.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                         app.setIcon(FILE_SAVEPATH+ app.getName() + ".png");
                         app.save();
                     mData.remove(app);

                     Intent intent = new Intent("android.intent.action.MAIN_BROADCAST");
                     mContext.sendBroadcast(intent);
                 }
             });
    }

    public boolean saveMyBitmap(Bitmap bmp, String bitName) {
        File dirFile = new File(FILE_SAVEPATH);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        boolean flag;
        File f = new File(FILE_SAVEPATH, bitName + ".png");
        FileOutputStream out = null;
        flag=false;
        try {
            out = new FileOutputStream(f);


            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
            flag=true;
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
