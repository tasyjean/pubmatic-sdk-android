package com.pubmatic.headerbiddingsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.pubmatic.sdk.headerbidding.PMAdSize;
import com.pubmatic.sdk.headerbidding.PMPrefetchManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BannerDemoScreen extends AppCompatActivity {

    // To track all adViews on this page.
    private Set<PublisherAdView> adViews = new HashSet<>();

    private List<HeaderBiddingBannerHelper.AdSlotInfo> adSlotInfoList;
    private HeaderBiddingBannerHelper hbBannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        PublisherAdView adView1 = (PublisherAdView) findViewById(R.id.publisherAdView1);
        PublisherAdView adView2 = (PublisherAdView) findViewById(R.id.publisherAdView2);

        adViews.add(adView1);
        adViews.add(adView2);

        List<PMAdSize>   adSizes1 = new ArrayList<>(1);
        adSizes1.add(new PMAdSize(320, 50));
        HeaderBiddingBannerHelper.AdSlotInfo adSlotInfo1 = new HeaderBiddingBannerHelper.AdSlotInfo("DMDemo", adSizes1, adView1);

        List<PMAdSize>   adSizes2 = new ArrayList<>(1);
        adSizes2.add(new PMAdSize(320, 50));
        HeaderBiddingBannerHelper.AdSlotInfo adSlotInfo2 = new HeaderBiddingBannerHelper.AdSlotInfo("DMDemo2", adSizes2, adView2);

        adSlotInfoList = new ArrayList<>(2);
        adSlotInfoList.add(adSlotInfo1);
        adSlotInfoList.add(adSlotInfo2);

        // For Adapter
        hbBannerHelper = new HeaderBiddingBannerHelper(this, adSlotInfoList);
        hbBannerHelper.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        adSlotInfoList.clear();

        for (PublisherAdView adView : adViews)
            adView.destroy();

        adViews.clear();

        if(hbBannerHelper !=null) {
            //hbBannerHelper.destroy();
            hbBannerHelper = null;
        }
    }
}

