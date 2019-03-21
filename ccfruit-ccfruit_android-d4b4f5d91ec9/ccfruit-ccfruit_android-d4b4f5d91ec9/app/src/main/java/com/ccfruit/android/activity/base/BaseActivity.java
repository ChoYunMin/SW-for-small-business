package com.ccfruit.android.activity.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ccfruit.android.R;
import com.ccfruit.android.fragment.NavigationFragment;
import com.ccfruit.android.network.NetworkTask;
import com.ccfruit.android.util.Dialog;
import com.ccfruit.android.util.GraphicUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by betas on 12/30/15.
 */
public abstract class BaseActivity extends Activity implements NetworkTask.OnRequestListener {
    public enum Theme {
        FULL, NO_MENU, BACK, NO_TITLE
    }

    private DrawerLayout drawer = null;
    private View drawer_layout = null;
    private FrameLayout root_layout = null;
    private View view_root = null;

    private Handler handler = new Handler();

    protected NetworkTask network = null;

    protected void onCreate(Bundle savedInstanceState, int layout) {
        this.onCreate(savedInstanceState, layout, "", Theme.FULL);
    }
    protected void onCreate(Bundle savedInstanceState, int layout, Theme theme) {
        this.onCreate(savedInstanceState, layout, "", theme);
    }
    protected void onCreate(Bundle savedInstanceState, int layout, String title, Theme theme) {
        super.onCreate(savedInstanceState);

        network = new NetworkTask(this, this);

        if(theme != Theme.NO_TITLE) {
            setContentView(R.layout.activity_base);

            root_layout = (FrameLayout) findViewById(R.id.layout_base);
            view_root = getLayoutInflater().inflate(layout, root_layout);

            drawer = (DrawerLayout)super.findViewById(R.id.drawer_layout);
            if(drawer != null) {
                if(theme == Theme.FULL) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    this.drawer_layout = super.findViewById(R.id.navigation_drawer);
                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    drawer = null;
                    ((NavigationFragment) findFragmentById(R.id.nav_bar)).setMenu(false);
                    ((NavigationFragment) findFragmentById(R.id.nav_bar)).setTitle(title);
                }
            } else {
                ((NavigationFragment) findFragmentById(R.id.nav_bar)).setMenu(false);
            }

            if(theme == Theme.BACK) {
                ((NavigationFragment) findFragmentById(R.id.nav_bar)).setBack(true);
            } else {
                ((NavigationFragment) findFragmentById(R.id.nav_bar)).setBack(false);
            }
        } else {
            setContentView(layout);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void setTitle(String title) {
        ((NavigationFragment) findFragmentById(R.id.nav_bar)).setTitle(title);
    }

    public void startActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void setTimeout(int time, Runnable task) {
        handler.postDelayed(task, time);
    }
    public void killTimeout(Runnable task) {
        handler.removeCallbacks(task);
    }

    public void toggleSideMenu() {
        if(drawer != null) {
            if (drawer.isDrawerOpen(drawer_layout)) {
                drawer.closeDrawer(drawer_layout);
            } else {
                drawer.openDrawer(drawer_layout);
            }
        }
    }

    @Override
    public View findViewById(int id) {
        if(view_root != null)
            return view_root.findViewById(id);

        return super.findViewById(id);
    }

    public Fragment findFragmentById(int id) {
        return getFragmentManager().findFragmentById(id);
    }

    public int getHeaderHeight() {
        int stat_height = GraphicUtil.getStatusBarHeight(this);
        if(view_root != null) {
            return super.findViewById(R.id.nav_bar).getMeasuredHeight() + stat_height;
        }
        return stat_height;
    }

    @Override
    public void onBackPressed() {
        if(drawer != null && drawer.isDrawerOpen(drawer_layout)) {
            drawer.closeDrawer(drawer_layout);
        } else {
            super.onBackPressed();
        }
    }

    public void sendNetworkTask(int id, NetworkTask.RequestType params) {
        network.send(id, params, true);
    }
    public void sendNetworkTask(int id, NetworkTask.RequestType params, boolean dialog) {
        network.send(id, params, dialog);
    }

    public void sendImageTask(int id, String params, File file) {
        HashMap<Integer, File> files = new HashMap<Integer, File>();
        files.put(0, file);

        network.sendImage(id, params, files);
    }
    public void sendImageTask(int id, String params, HashMap<Integer, File> files) {
        network.sendImage(id, params, files);
    }

    @Override
    public final void onRequestEnd(NetworkTask.RPCResult result) {
        if(result.getCode() == 0)
            onNetworkTaskSuccess(result.getId(), result.getMeta(), result.getData());
        else
            onNetworkTaskError(result.getId(), result.getCode(), result.getMessage());
    }

    public void onNetworkTaskSuccess(int id, JSONObject meta, JSONObject result) {

    }
    public void onNetworkTaskError(int id, int code, String msg) {
        toast(msg + " (" + code + ")");
    }

    public void log(Object str) {
        Log.e(this.getClass().getSimpleName(), ""+str);
    }
    public void alert(String msg) {
        new Dialog(this).alert(msg);
    }
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void focus(int id) {
        ((EditText)findViewById(id)).selectAll();
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if(view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
