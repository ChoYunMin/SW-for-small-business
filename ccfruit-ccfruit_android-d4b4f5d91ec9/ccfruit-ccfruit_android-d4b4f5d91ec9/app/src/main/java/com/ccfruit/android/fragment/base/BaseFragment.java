package com.ccfruit.android.fragment.base;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ccfruit.android.network.NetworkTask;
import com.ccfruit.android.util.Dialog;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by betas on 12/30/15.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener, NetworkTask.OnRequestListener {
    private int layout = 0;
    private View root = null;
    private boolean loaded = false;

    protected NetworkTask network = null;

    public void onCreate(Bundle savedInstanceState, int layout) {
        super.onCreate(savedInstanceState);

        this.network = new NetworkTask(getActivity(), this);
        this.layout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;

        if(layout != 0)
            view = inflater.inflate(this.layout, container, false);
        else
            view = super.onCreateView(inflater, container, savedInstanceState);

        this.root = view;

        onPreLoad();
        onLoad();

        loaded = true;

        return view;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void onPreLoad() {}
    public abstract void onLoad();

    public void onClickEvent(int id, View v) {}

    @Override
    public final void onClick(View v) {
        onClickEvent(v.getId(), v);
    }

    public View findViewById(int id) {
        return root.findViewById(id);
    }

    public Context getContext() { return getActivity(); }

    public void sendNetworkTask(int id, NetworkTask.RequestType params) {
        network.send(id, params, true);
    }
    public void sendNetworkTask(int id, NetworkTask.RequestType params, boolean dialog) {
        network.send(id, params, dialog);
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

    public void startActivity(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }

    public void log(Object str) {
        Log.e(this.getClass().getSimpleName(), "" + str);
    }
    public void alert(String msg) {
        new Dialog(getContext()).alert(msg);
    }
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void focus(int id) {
        ((EditText)findViewById(id)).selectAll();
    }
}
