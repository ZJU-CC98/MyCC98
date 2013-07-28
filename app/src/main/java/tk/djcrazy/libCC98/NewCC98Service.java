package tk.djcrazy.libCC98;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import tk.djcrazy.MyCC98.Config;
import tk.djcrazy.MyCC98.application.MyApplication;
import tk.djcrazy.MyCC98.bean.UpdateInfo;
import tk.djcrazy.libCC98.data.HotTopicEntity;
import tk.djcrazy.libCC98.util.RequestResultListener;

/**
 * Created by DJ on 13-7-28.
 */
@Singleton
public class NewCC98Service {


    @Inject
    private ICC98UrlManager mUrlManager;
    @Inject
    private Application mApplication;
    @Inject NewCC98Parser mCC98Parser;


    public void submitHotTopicRequest(final RequestResultListener<List<HotTopicEntity>> listRequestResultListener) {
        getApplication().mRequestQueue.add(new StringRequest(mUrlManager.getHotTopicUrl(),new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //do some parser job
                listRequestResultListener.onReuqestComplete(null);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listRequestResultListener.onReuqestError();
            }
        }));
    }


    public void submitUpdateRequest(final RequestResultListener<JSONObject> listener) {
        Log.w("submitUpdateRequest","submitUpdateRequest");
        getApplication().mRequestQueue.add(new StringRequest(Request.Method.GET, Config.UPDATE_LINK,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    listener.onReuqestComplete(new JSONObject(result));
                 } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onReuqestError();
                Log.w("submitUpdateRequest", volleyError.toString());
            }
        }));
    }

    public MyApplication getApplication() {
        return (MyApplication) mApplication;
    }
}
