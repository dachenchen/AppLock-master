package com.chenandroid.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenandroid.App;
import com.chenandroid.R;
import com.chenandroid.UnLockApp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * 程序item
 */
public class LockAdapter extends BaseQuickAdapter<App> {
    public LockAdapter(List<App> data) {
        super(R.layout.list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final App app) {
         baseViewHolder.setText(R.id.name,app.getName());
        ImageView view = baseViewHolder.getView(R.id.icon);
        Log.e(TAG, "convert: " + app.getIcon() );
        Bitmap loacalBitmap = getLoacalBitmap(app.getIcon());
        view.setImageBitmap(loacalBitmap);
        Button view1 = baseViewHolder.getView(R.id.button);
        view1.setText("解锁");
        view1.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

//                          EventBus.getDefault().post("unlock");

                          UnLockApp  unLockApp= new UnLockApp();
                          unLockApp.setPackageName(app.getPackageName());
                          unLockApp.setName(app.getName());
                          unLockApp.setIcon(app.getIcon());
                     unLockApp.setSortLetters(app.getSortLetters());
                     unLockApp.save();

                     boolean delete = app.delete();
                     if (delete){
                         mData.remove(app);
                          notifyDataSetChanged();
                          Intent intent = new Intent("android.intent.action.MAIN_BROADCAST");
                          mContext.sendBroadcast(intent);
                      }

                 }
             });
    }

    public  Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
