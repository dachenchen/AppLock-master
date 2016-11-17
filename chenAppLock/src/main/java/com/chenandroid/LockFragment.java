package com.chenandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenandroid.adapter.LockAdapter;
import com.chenandroid.sortlist.PinyinComparator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

/**
 * Created by App-Android on 2016/11/3.
 */
public class LockFragment  extends Fragment {
    private static final String TAG = LockFragment.class.getSimpleName();
    private LockAdapter adapter;
    private List<App> apps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frgament_lock, null);

        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onViewCreated: " );
        TextView textview = (TextView) view.findViewById(R.id.textview);
        PinyinComparator pinyinComparator= new PinyinComparator();
        RecyclerView viewById = (RecyclerView) view.findViewById(R.id.recy);
        viewById.setLayoutManager(new LinearLayoutManager(getActivity()));
        apps = App.listAll(App.class);
         for (App app: apps ){
             String s = Pinyin4jUtil.converterToFirstSpell(app.getName());
             String sortString = s.substring(0, 1).toUpperCase();
             if (sortString.matches("[A-Z]")) {
                 app.setSortLetters(sortString.toUpperCase());
             } else {
                 app.setSortLetters("#");
             }


         }

         if ( apps.size()==0){
             Log.e(TAG, "onViewCreated: sdfdsfa" );
             textview.setVisibility(View.VISIBLE);
         }
        Collections.sort(apps, pinyinComparator);

        Log.e(TAG, "onViewCreated: apps"+ apps.size() );
        adapter = new LockAdapter(null);
        viewById.setAdapter(adapter);


         adapter.setNewData(apps);


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
         if ( !hidden){
             apps = App.listAll(App.class);
             adapter.setNewData(apps);
         }
    }

    @Subscribe
    public void onEventMainThread(String object) {

    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
