/*
 * PubMatic Inc. (PubMatic) CONFIDENTIAL
 * Unpublished Copyright (c) 2006-2014 PubMatic, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of PubMatic. The intellectual and technical concepts contained
 * herein are proprietary to PubMatic and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from PubMatic.  Access to the source code contained herein is hereby forbidden to anyone except current PubMatic employees, managers or contractors who have executed
 * Confidentiality and Non-disclosure agreements explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret, of  PubMatic.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF PubMatic IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.
 */

package com.pubmatic.sdk.common.network;

import com.pubmatic.sdk.common.CommonConstants;
import com.pubmatic.sdk.common.PMLogger;

import java.net.HttpURLConnection;
import java.net.URL;

public class AdTracking {

    public static void invokeTrackingUrl(final int timeout, final String url,
            final String userAgent) {
        new Thread(new Runnable() {
            // Thread to stop network calls on the UI thread
            @Override
            public void run() {

                HttpURLConnection httpUrlConnection = null;
                int responseCode = 0;

                try {
                    httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
                    if (httpUrlConnection != null) {

                        httpUrlConnection.setRequestMethod(CommonConstants.HTTPMETHODGET);
                        httpUrlConnection.setRequestProperty(CommonConstants.REQUEST_HEADER_CONNECTION, CommonConstants.REQUEST_HEADER_CONNECTION_VALUE_CLOSE);
                        httpUrlConnection.setRequestProperty(CommonConstants.REQUEST_HEADER_USER_AGENT, userAgent);
                        httpUrlConnection.setConnectTimeout(timeout * 1000);

                        responseCode = httpUrlConnection.getResponseCode();

                        if (responseCode != HttpURLConnection.HTTP_OK)
                        {
                            PMLogger.logEvent("PM-TrackerEvent : Error while invoking tracking URL : " + url + "HttpResponse:"+responseCode,
                                              PMLogger.PMLogLevel.Custom);
                            return;
                        }else{
                            PMLogger.logEvent("PM-TrackerEvent : Ad Tracker fired successfully",
                                              PMLogger.PMLogLevel.Custom);
                        }
                    }
                } catch (Exception ex) {
                    PMLogger.logEvent("PM-TrackerEvent : Error while invoking tracking URL : " + url,
                                      PMLogger.PMLogLevel.Custom);
                } finally {
                    if(httpUrlConnection !=null) {
                        httpUrlConnection.disconnect();
                        httpUrlConnection= null;
                    }
                }
            }
        }).start();
    }


    /**
     * This method is used for sending multiple trackers typically in case of
     * Native ads or may be Video Ads.
     *
     * @param timeout
     * @param urls
     *            - tracker urls
     * @param userAgent
     */
    public static void invokeTrackingUrl(int timeout, String[] urls, String userAgent) {

        if (urls != null) {
            for (String url : urls) {
                invokeTrackingUrl(timeout, url, userAgent);
            }
        }
    }
}
