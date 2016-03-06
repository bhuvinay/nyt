package com.vgnary.nyt.thenewshour.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.vgnary.nyt.thenewshour.utils.LogUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static com.vgnary.nyt.thenewshour.utils.LogUtils.makeLogTag;


/**
 * Generic class representing particular type of network request handler.This class defines
 * common behaviour of those requests and provides common interface to be implemented by
 * concrete request handlers.
 * <p/>
 * In application there can be mainly following type of requests:-<br>
 * <ul>
 * <li>Simple GET/POST request to application server,that expects data to be returned
 * in json form.see {@link AppJsonRequestManager}</li>
 * <li>Simple GET request that is used for tracking some user behaviour.See {@link }</li>
 * <li>Multipart request that is used to send form and image data to server.</li>
 * </ul>
 * <p/>
 * <p>This class encalpsulates some common logging related information also ,so that it is easy
 * for developers to see what request was sent and what response came.</p>
 *
 */
public abstract class AppRequestManager {

    private static final String TAG = makeLogTag(AppRequestManager.class);

    /**
     * These are some simple patterns used to beautify logging information so that developers can see
     * what is what information.
     */
    public static final String AND_PATTERN = "&&&&&&&&&&&&&&&&&&&&&&&&&&&&& HEADER &&&&&&&&&&&&&&&&&&&&&&&&&&&&&";
    public static final String PLUS_PATTERN = "+++++++++++++++++++++++++++++ REQUEST SUCCESS +++++++++++++++++++++++++++++";
    public static final String X_PATTERN = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX REQUEST FAILED XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String STAR_PATTERN = "*************************** APP PARAMS ***************************";

    protected NetworkController mNetworkController;
    protected Context mAppContext;
    //User agent of application
    public static final String USER_AGENT = "AndroidShiksha";
    private static String mOsVersionName = null;
    private static int mOsSdkCode = -1;
    public static long mLastApiHitTimeStamp = 0;
    private String mGeneratedSessionId;
    public static boolean isAlreadyShowingForceUpgradeLayer;
    private boolean mIsNewSession = false;

    protected AppRequestManager(Context context) {
        this.mAppContext = context;
        this.mNetworkController = NetworkController.getInstance(context);
    }

    /**
     * Executes GET/POST network request and returns parsed response.This overloaded method does not
     * accept request params sent in GET/POST requests.
     *
     * @param requestMethod http method GET or POST
     * @param url           url to be hit for response
     * @param requestId     some unique id given to request by caller entity
     * @param responseClass this is class type to which response should be mapped in java object form
     * @param listener      listener implemented by caller entity to listen to failure or success of network request
     * @param <T>           generic of expected response
     * @throws JSONException                exception comes if malformed json comes in response
     * @throws UnsupportedEncodingException
     */
    protected <T> void executeRequest(int requestMethod, final String url, final int requestId, final Class<T> responseClass, final OnNetworkResponseListener listener) throws JSONException, UnsupportedEncodingException {
        LogUtils.LOGD(TAG, "Default implementation of method has no action");
    }

    /**
     * Executes GET/POST network request and returns parsed response.This overloaded method which
     * accepts request params sent in GET/POST requests.
     *
     * @param requestMethod http method GET or POST
     * @param url           url to be hit for response
     * @param requestId     some unique id given to request by caller entity
     * @param responseClass this is class type to which response should be mapped in java object form
     * @param listener      listener implemented by caller entity to listen to failure or success of network request
     * @param <T>           generic of expected response
     * @throws JSONException                exception comes if malformed json comes in response
     * @throws UnsupportedEncodingException
     */
    protected <T> void executeRequest(int requestMethod, final String url, final Map<String, String> requestParams, final int requestId, final Class<T> responseClass, final OnNetworkResponseListener listener) throws JSONException, UnsupportedEncodingException {
        LogUtils.LOGD(TAG, "Default implementation of method has no action");
    }

    /**
     * Executes request which tracks user behaviour.This type of request does not require {@link OnNetworkResponseListener}
     * generally because response is not required or it is not urgent to check if request was success
     * or failure.
     *
     * @param requestMethod http method GET or POST
     * @param url           url to be hit for response
     * @param requestParams parameters to be sent for tracking purpose
     * @throws UnsupportedEncodingException
     */
    protected void trackHit(int requestMethod, final String url, final Map<String, String> requestParams) throws UnsupportedEncodingException {
        LogUtils.LOGD(TAG, "Default implementation of method has no action");
    }

    protected <T> void uploadImage(final String url, final String fileName, final int requestId, final Class<T> responseClass, final OnNetworkResponseListener listener) throws UnsupportedEncodingException {
        LogUtils.LOGD(TAG, "Default implementation of method has no action");
    }


    /**
     * This method encodes or processes request parameters before sending over http.Example can be
     * it formats spaces to %20 before sending.
     *
     * @param map request parameters for GET method
     * @return formatted string to be sent in GET method
     * @throws UnsupportedEncodingException
     */
    protected String urlEncodeUTF8(Map<?, ?> map) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
                    URLEncoder.encode(entry.getValue().toString(), "UTF-8")
            ));
        }
        return sb.toString();
    }

    /**
     * Simply logs header information of of each and every request from android application.
     * This method is simply for developers for ease of development.
     *
     * @param request request initiated by android application
     * @throws AuthFailureError
     */
    protected void logRequestPattern(Request request) throws AuthFailureError {
        LogUtils.LOGD(TAG, AND_PATTERN);
        Map<String, String> headers = request.getHeaders();
        if (headers != null && headers.size() != 0) {
            String[] headerKeys = {"platformVersion", "source", "appVersionCode", "appVersionName", "deviceModel", "deviceManufacturer", "platformName", "platformOsSdk", "authKey", "authChecksum", "sessionId"};
            for (String headerKey : headerKeys) {
                String headerValue = headers.get(headerKey);
                if (headerValue != null) {
                    LogUtils.LOGD(TAG, headerKey.toUpperCase() + " :: " + headerValue);
                }
            }
        } else {
            LogUtils.LOGD(TAG, "There is not header values set for this request");
        }
        LogUtils.LOGD(TAG, AND_PATTERN);
    }

    /**
     * Appends header options from application side and sent to shiksha application server.These
     * parameters are used by shiksha server for logging purpose.
     *
     * @param headerParams key value pair map to store header values
     */
    /**
     * Indirectly extracts android platform name(like KITKAT,JELLYBEAN etc) using reflection api,
     * since there is no direct way to get platform name.
     */
    protected <T> void executeJsoupRequest(int requestMethod, String url, final Map<String, String> requestParams, final int requestId, final Class<T> responseClass, final OnNetworkResponseListener listener) throws JSONException, UnsupportedEncodingException {
        LogUtils.LOGD(TAG, "Default implementation of method has no action");
    }


}
