package tk.djcrazy.libCC98.util;

import android.util.Base64;
import android.util.Log;
import android.app.Application;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

import tk.djcrazy.MyCC98.config.Config;
import tk.djcrazy.libCC98.data.Issue;
import tk.djcrazy.MyCC98.application.MyApplication;

/**
 * Created by zsy on 8/13/13.
 */
@Singleton
public class IssueSender {
    private static final String HEADER_AUTH = "Authorization";
    private static final String AUTH_TOKEN = "token";
    private static final String HOST = "api.github.com";
    private static final String UA = "MyCC98";
    private static final String HEADER_UA = "User-Agent";
    private static final String CREATE_ISSUE = "/repos/djj0809/MyCC98/issues";
    private static final String CREATE_ISSUE_URL = "https://" + HOST + CREATE_ISSUE;

    private static final String TAG = "IssueSender";

    @Inject
    private Application mApplication;

    private Gson gson = new Gson();

    public void send(final Issue issue, Object tag, final RequestResultListener<String> listener) {
        StringRequest request = new StringRequest(Request.Method.POST, CREATE_ISSUE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listener.onRequestComplete(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onRequestError(error.getLocalizedMessage());
                    error.printStackTrace();
                }
            })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(HEADER_AUTH, getAuthString());
                params.put(HEADER_UA, UA);

                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return (gson.toJson(issue) + "\r\n").getBytes();
            }
        };
        request.setTag(tag);
        getApplication().mRequestQueue.add(request);
    }

    private String getAuthString() {
        return "Basic " + Base64.encodeToString((Config.GITHUB_USERNAME+ ":" + Config.GITHUB_TOKEN).getBytes(), Base64.NO_WRAP);
    }

    private MyApplication getApplication() {
        return (MyApplication) mApplication;
    }
}
