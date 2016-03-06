package com.vgnary.nyt.thenewshour.network;

import com.android.volley.VolleyError;

/**
 * Interface implemented by entity making some network request to handle success and failure cases.
 *
 */
public interface OnNetworkResponseListener<T> {

    /**
     * Handles case of network communication when server successfully serves request by android
     * application.
     *
     * @param  requestId  unique id assigned by caller while making network request
     * @param  requestUrl url for network communication
     * @return T generaic class type to which response is parsed by using gson library
     * @see    AppRequestManager#executeRequest(int, String, int, Class, OnNetworkResponseListener)
     *
     */
    public void onDownloadComplete(int requestId, String requestUrl, T responseData);

    /**
     * Handles case of network communication when network request failed due to number of reasons.
     *
     * @param  requestId  unique id assigned by caller while making network request
     * @param  requestUrl url for network communication
     * @param  networkError information about network error that caused request to fail
     * @see    AppRequestManager#executeRequest(int, String, int, Class, OnNetworkResponseListener)
     *
     */
    public void onDownloadFailed(int requestId, String requestUrl, VolleyError networkError);

}
