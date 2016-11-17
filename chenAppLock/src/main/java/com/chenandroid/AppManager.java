package com.chenandroid;

import android.app.Activity;

import java.util.Stack;

/**
 * activity 管理列
 * Created by App-Android on 2016/10/10.
 */
public class AppManager {

    /**
     *  保存list集合
     */
     private Stack<Activity> aclist=new Stack<>();
         private  static AppManager appmanager=new AppManager();
    //  创建实例
     public static AppManager getInstance(){
         return appmanager;
     }
    /**
     * 添加activity 到集合
     */
     public void  addActivty(Activity activity){
          //  判断不为空,  没有关闭, 添加到集合
          if (!activity.isFinishing()&&activity!=null){
              aclist.add(activity);
          }
     }

    /**
     *  获取栈顶的Activity
     * @return
     */
    public  Activity  getlastElement(){
         return  aclist.lastElement();
     }
    /**
     * 获取指定类名的Activity

     * @return
     */
    public Activity getActivity(Class<? extends Activity> cls){
         //  遍历栈
          for (Activity activity:aclist){
           // 判断输入的类名
               if ( activity.getClass().equals(cls)){
                   return activity;
               }
          }
          return  null;
     }
    /**
     *  移除activity
     *
     */
    public  void  removeActivity(Activity ac){
          if ( ac!=null){
              aclist.remove(ac);
          }
     }

    /**
     * 移除并关闭activity
     * @param clazz
     */
    public void removeAndFinishActivity(Class<? extends Activity> clazz){
        for(Activity activity : aclist){
            if(activity.getClass().equals(clazz)){
                removeAndFinishActivity(activity);
                break ;
            }
        }
    }

    /**
     * 从Activity中移除并且关闭Activity
     * @param activity
     */
    public void removeAndFinishActivity(Activity activity){
        if(activity != null && !activity.isFinishing()){
            aclist.remove(activity);
            activity.finish();
            activity = null ;
        }
    }

}
