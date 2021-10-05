package com.tcmyxc;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 8:45
 * @description : todo
 */
public class FragmentManagerWrapper {

    // 单例模式
    private volatile static FragmentManagerWrapper instance = null;

    public static FragmentManagerWrapper getInstance() {
        if (instance == null) {
            synchronized (FragmentManagerWrapper.class){
                if (instance == null){
                    instance = new FragmentManagerWrapper();
                }
            }
        }

        return instance;
    }

    private HashMap<String, Fragment> hashMap = new HashMap<>();

    public Fragment createFragment(Class<?> clazz){
        return createFragment(clazz, true);
    }

    public Fragment createFragment(Class<?> clazz, boolean isObtain) {
        Fragment result = null;
        String className = clazz.getName();
        // 如果hashmap 中有className，则直接获取，否则通过反射创建一个实例
        if(hashMap.containsKey(className)){
            result = hashMap.get(className);
        }
        else{
            try {
                result = (Fragment) Class.forName(className).newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        // 如果需要存起来，则存起来
        if(isObtain){
            hashMap.put(className, result);
        }

        return result;
    }
}
