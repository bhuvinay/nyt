package com.vgnary.nyt.thenewshour.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vgnary.nyt.thenewshour.BuildConfig;
import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.vgnary.nyt.thenewshour.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.vgnary.nyt.thenewshour.utils.LogUtils.makeLogTag;



public class AppJsonRequestManager extends AppRequestManager {

    private static AppJsonRequestManager mInstance;
    private static final String TAG = makeLogTag(AppJsonRequestManager.class);

    private AppJsonRequestManager(Context context) {
        super(context);
    }

    public static AppJsonRequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppJsonRequestManager(context);
        }
        return mInstance;
    }


    @Override
    public <T> void executeRequest(int requestMethod, final String url, final int requestId, final Class<T> responseClass, final OnNetworkResponseListener listener) throws JSONException, UnsupportedEncodingException {
        executeRequest(requestMethod, url, null, requestId, responseClass, listener);
    }

    @Override
    public <T> void executeRequest(final int requestMethod, final String url, final Map<String, String> requestParams, final int requestId, final Class<T> responseClass, final OnNetworkResponseListener listener) throws JSONException, UnsupportedEncodingException {

        final StringBuilder requestUrl = new StringBuilder(url);

        if (requestParams != null) {
            if (requestMethod == Request.Method.GET && requestParams.size() != 0) {
                requestUrl.append(urlEncodeUTF8(requestParams));
            } else if (requestMethod == Request.Method.POST) {
                //requestParams.put("")
            }
        }


        StringRequest jsonRequest = new StringRequest(requestMethod, requestUrl.toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                LogUtils.LOGD(TAG, "RESPONSE STRING :: " + response);


                if (listener != null) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    T parsedResponse = null;
                    try {
                        parsedResponse = gson.fromJson(response.toString(),
                                responseClass);
                        if (listener != null) {
                            BasicResponse responseData = (BasicResponse) parsedResponse;
                            //notify to listener when request is success
                            listener.onDownloadComplete(requestId, requestUrl.toString(), parsedResponse);
                        }
                    } catch (JsonParseException parseException) {
                        if (BuildConfig.DEBUG) {
                            throw new RuntimeException(requestUrl.toString() + "\n" + parseException.getMessage());
                        }
                        if (listener != null) {
                        }
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //simple logging of request/response
                if (listener != null) {
                    listener.onDownloadFailed(requestId, requestUrl.toString(), error);
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };

        try {
            jsonRequest.setTag(String.valueOf(requestId));
            logRequestPattern(jsonRequest);
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //execute request over network
        jsonRequest.setShouldCache(false);
        mNetworkController.addToRequestQueue(jsonRequest, String.valueOf(requestId));

    }


    public void cancelPendingRequest(final int requestID) {
        mNetworkController.cancelPendingRequests(String.valueOf(requestID));
    }

    @Override
    public <T> void executeJsoupRequest(int requestMethod, String url, final Map<String, String> requestParams, final int requestId, final Class<T> responseClass, final OnNetworkResponseListener listener) throws JSONException, UnsupportedEncodingException {
        final StringBuilder requestUrl = new StringBuilder(url);

        if (requestParams != null) {
            if (requestMethod == Request.Method.GET && requestParams.size() != 0) {
                requestUrl.append(urlEncodeUTF8(requestParams));
            } else if (requestMethod == Request.Method.POST) {
                //requestParams.put("")
            }
        }
        StringRequest jsonRequest = new StringRequest(requestMethod, requestUrl.toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                LogUtils.LOGD(TAG, "RESPONSE STRING :: " + response);


                if (listener != null) {
                    Document doc = Jsoup.parse(response);
                    T parsedResponse = null;
                    try {

                        if (listener != null) {
                            listener.onDownloadComplete(requestId, requestUrl.toString(), doc);
                        }
                    } catch (JsonParseException parseException) {
                        if (BuildConfig.DEBUG) {
                            throw new RuntimeException(requestUrl.toString() + "\n" + parseException.getMessage());
                        }
                        if (listener != null) {
                        }
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //simple logging of request/response
                if (listener != null) {
                    listener.onDownloadFailed(requestId, requestUrl.toString(), error);
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };

        try {
            jsonRequest.setTag(String.valueOf(requestId));
            logRequestPattern(jsonRequest);
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonRequest.setShouldCache(false);
        mNetworkController.addToRequestQueue(jsonRequest, String.valueOf(requestId));
    }
}
