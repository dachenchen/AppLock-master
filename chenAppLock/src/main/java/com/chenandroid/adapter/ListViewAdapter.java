package com.chenandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenandroid.App;
import com.chenandroid.R;
import com.chenandroid.UnLockApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by MOON on 1/19/2016.
 */
public class ListViewAdapter extends BaseAdapter{

    private static final String TAG = ListViewAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater listContainer;
    private List<UnLockApp> listItems;
    ListItemView listItemView = null;

    private SQLiteDatabase db;
    private String dataBaseName = "kiplening";
    private String tableName = "app";
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/chen/";
    public ListViewAdapter(Context context) {
        this.context = context;
        //创建视图容器
        listContainer = LayoutInflater.from(context);

    }

     public  void  setDate( List<UnLockApp> listItems){
         this.listItems = listItems;
          notifyDataSetChanged();
     }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //TODO 获取布局信息
        if (convertView == null){
            listItemView = new ListItemView();
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.list_item, null);
            listItemView.line3= (LinearLayout) convertView.findViewById(R.id.line3);
            //获取控件对象
            listItemView.icon = (ImageView)convertView.findViewById(R.id.icon);
            listItemView.name = (TextView)convertView.findViewById(R.id.name);
            listItemView.info = (TextView)convertView.findViewById(R.id.info);
            listItemView.tvLetter = (TextView)convertView.findViewById(R.id.catalog);
            listItemView.button = (Button)convertView.findViewById(R.id.button);

            //设置控件集到convertView
            convertView.setTag(listItemView);
        }else {
            listItemView = (ListItemView)convertView.getTag();
        }

        final UnLockApp app = listItems.get(position);
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            listItemView.tvLetter.setVisibility(View.VISIBLE);
            listItemView.tvLetter.setText(app.getSortLetters());
        }else{
            listItemView.tvLetter.setVisibility(View.GONE);

        }
        Log.e(TAG, "getView: 不显示"+ app.getIcon());
        listItemView.icon.setImageBitmap(getLoacalBitmap(app.getIcon()));
        listItemView.name.setText(app.getName());

        listItemView.button.setText("加锁");


        final View finalConvertView = convertView;
        listItemView.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                     App unLockApp = new App();
                     unLockApp.setIcon(FILE_SAVEPATH+app.getName()+".png");
                     unLockApp.setName(app.getName());
                     unLockApp.setPackageName(app.getPackageName());
                     unLockApp.save();
                AnimationSet as=new AnimationSet(true);
                TranslateAnimation tr= new TranslateAnimation(0, 800, 0, 0);
                tr.setDuration(300);
                as.addAnimation(tr);
                finalConvertView.startAnimation(as);
                as.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        v.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setClickable(true);
                        listItems.remove(position);
                        app.delete();

                        notifyDataSetChanged();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });



                //thread.start();
                // 更改表中的顺

                    Intent intent = new Intent("android.intent.action.MAIN_BROADCAST");

                    intent.putExtra("status", "true");
                    context.sendBroadcast(intent);
//                }else{
//                    Intent intent = new Intent("android.intent.action.MAIN_BROADCAST");
//                    intent.putExtra("status","false");
//                    context.sendBroadcast(intent);
//                }

            }
        });
        return convertView;
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
    /**
     * 封装两个视图组件的类
     */

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
    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return listItems.get(position).getSortLetters().charAt(0);
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = listItems.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }
    class ListItemView {
        ImageView icon;
        TextView name;
        TextView info;
        TextView tvLetter;
        Button button;
        LinearLayout line3;
    }



}
