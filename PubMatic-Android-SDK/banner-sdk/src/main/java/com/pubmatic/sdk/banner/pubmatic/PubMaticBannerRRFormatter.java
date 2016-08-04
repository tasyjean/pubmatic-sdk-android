package com.pubmatic.sdk.banner.pubmatic;

import android.text.TextUtils;

import com.pubmatic.sdk.banner.BannerAdDescriptor;
import com.pubmatic.sdk.common.AdRequest;
import com.pubmatic.sdk.common.AdResponse;
import com.pubmatic.sdk.common.CommonConstants;
import com.pubmatic.sdk.common.CommonConstants.CONTENT_TYPE;
import com.pubmatic.sdk.common.RRFormatter;
import com.pubmatic.sdk.common.network.HttpRequest;
import com.pubmatic.sdk.common.network.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PubMaticBannerRRFormatter implements RRFormatter {

    private final static String kPubMatic_BidTag = "PubMatic_Bid";
    private static final String kecpm = "ecpm";
    private static final String kcreative_tag = "creative_tag";
    private static final String ktracking_url = "tracking_url";
    private static final String klanding_page = "landing_page";
    private static final String kclick_tracking_url = "click_tracking_url";
    private static final String kerror_code = "error_code";
    private static final String kerror_message = "error_string";
    private static final String UTF8_CHARSET = "UTF-8";

    private AdRequest mRequest;

    @Override
    public HttpRequest formatRequest(AdRequest request) {
        mRequest = request;
        PubMaticBannerAdRequest adRequest = (PubMaticBannerAdRequest) request;
        HttpRequest httpRequest = new HttpRequest(CONTENT_TYPE.URL_ENCODED);

        httpRequest.setUserAgent(adRequest.getUserAgent());
        httpRequest.setConnection("close");
        httpRequest.setRequestUrl(request.getAdServerURL());
        httpRequest.setRequestMethod(CommonConstants.HTTPMETHODPOST);
        httpRequest.setRequestType(CommonConstants.AD_REQUEST_TYPE.PUB_BANNER);
        httpRequest.setPostData(adRequest.getPostData());
        return httpRequest;
    }

    @Override
    public AdResponse formatResponse(HttpResponse response) {

        // Check whether the adResponse is null or not. If it is null then
        // it is an invalid ad, so sending the null response.
        if (response == null) {
            return null;
        }

        AdResponse pubResponse = new AdResponse();
        pubResponse.setRequest(mRequest);

        // Parsing of the response.
        try {
            JSONObject jsonObject = new JSONObject(response.getResponseData());
            JSONObject object = jsonObject.getJSONObject(kPubMatic_BidTag);
            return parseJSONResponse(object);
        }catch(JSONException je){
            je.printStackTrace();
            return null;
        }
    }

    @Override
    public AdResponse formatHeaderBiddingResponse(JSONObject response) {
        return parseJSONResponse(response);
    }

    private AdResponse parseJSONResponse(JSONObject response){

        if(response == null){
            return null;
        }

        AdResponse pubResponse = new AdResponse();

        Map<String, String> adInfo = new HashMap<String, String>();
        ArrayList<String> impressionTrackers = new ArrayList<String>();
        ArrayList<String> clickTrackers = new ArrayList<String>();
        adInfo.put("type", "thirdparty");

        try {
            // If there is an error from the server which happens when provided
            // wrong ad parameters, return the error with error code and error
            // message.
            String errorCode;
            if (!TextUtils.isEmpty(errorCode = response.optString(kerror_code))) {

                pubResponse.setErrorCode(errorCode);
                pubResponse.setErrorMessage(response.getString(kerror_message));
                return pubResponse;
            }

            // Check if json contains the creative_tag and tracking_url.
            // If these are missing then the ad is invalid. Return null else
            // return valid adInfo object.
            if (!TextUtils.isEmpty(response.optString(kcreative_tag))
                    && !TextUtils.isEmpty(response.optString(ktracking_url))) {

                adInfo.put("content", URLDecoder.decode(response.getString(kcreative_tag), UTF8_CHARSET));
                impressionTrackers.add( URLDecoder.decode(response.getString(ktracking_url), UTF8_CHARSET));

                // Setting ecpm if not null
                if (!response.isNull(kecpm)) {
                    adInfo.put("ecpm", response.getString(kecpm));
                }
                // Setting click_tracking_url if not null
                if (!response.isNull(kclick_tracking_url)) {
                    clickTrackers.add(URLDecoder.decode(response.getString(kclick_tracking_url), UTF8_CHARSET));
                }
                // Setting landing_page if not null
                if (!response.isNull(klanding_page)) {
                    adInfo.put("url", URLDecoder.decode(response.getString(klanding_page), UTF8_CHARSET));
                }
            }

            BannerAdDescriptor adDescriptor = new BannerAdDescriptor(adInfo);
            adDescriptor.setImpressionTrackers(impressionTrackers);
            adDescriptor.setClickTrackers(clickTrackers);
            pubResponse.setRenderable(adDescriptor);

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            response = null;
        }

        return pubResponse;
    }
}
