package com.sgcai.android.aop;

import android.app.Application;

import com.sgcai.android.aop.callback.PermissionCallback;
import com.sgcai.android.aop.callback.StartActivityIntercept;

import java.util.ArrayList;
import java.util.List;

public class AopSDK {
    private AopSDK() {

    }

    static Application mApplication;
    static List<StartActivityIntercept> mIntercepts;
    static List<PermissionCallback> mPermissionCallback;

    public static Application getApplication() {
        return mApplication;
    }

    public static List<PermissionCallback> getPermissionCallback() {
        return mPermissionCallback;
    }

    public static List<StartActivityIntercept> getIntercepts() {
        return mIntercepts;
    }

    public static void init(Application application) {
        mApplication = application;
        mIntercepts = new ArrayList<>();
        mPermissionCallback = new ArrayList<>();
    }

    public static void addStartActivityIntercept(StartActivityIntercept... intercept) {
        if (mIntercepts == null) {
            throw new RuntimeException("please check init first!");
        }
        if (intercept == null || intercept.length == 0) {
            return;
        }
        for (StartActivityIntercept in : intercept) {
            if (!mIntercepts.contains(in)) {
                mIntercepts.add(in);
            }
        }
    }

    public static void removeStartActivityIntercept(StartActivityIntercept... intercept) {
        if (mIntercepts == null) {
            throw new RuntimeException("please check init first!");
        }
        if (intercept == null || intercept.length == 0) {
            return;
        }
        for (StartActivityIntercept in : intercept) {
            if (mIntercepts.contains(in)) {
                mIntercepts.remove(intercept);
            }
        }

    }



    public static void clearIntercepts() {
        if (mIntercepts == null) {
            throw new RuntimeException("please check init first!");
        }
        mIntercepts.clear();
    }


    public static void addPermissionCallback(PermissionCallback... callbacks) {
        if (mPermissionCallback == null) {
            throw new RuntimeException("please check init first!");
        }
        if (callbacks == null || callbacks.length == 0) {
            return;
        }
        for (PermissionCallback in : callbacks) {
            if (!mPermissionCallback.contains(in)) {
                mPermissionCallback.add(in);
            }
        }
    }

    public static void removePermissionCallback(PermissionCallback... callbacks) {
        if (mPermissionCallback == null) {
            throw new RuntimeException("please check init first!");
        }
        if (callbacks == null || callbacks.length == 0) {
            return;
        }
        for (PermissionCallback in : callbacks) {
            if (mPermissionCallback.contains(in)) {
                mPermissionCallback.remove(in);
            }
        }
    }

}
