package com.mocean.mopubadapter.sample;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.moceanmobile.mast.MASTAdView.LogLevel;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.BannerAdListener;
import com.mopub.mobileads.MoceanBannerAdapter;

public class MoceanAdapterSampleAppActivity extends Activity {

	protected static final String TAG = MoceanAdapterSampleAppActivity.class
			.getSimpleName();
	private MoPubView moPubView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		moPubView = (MoPubView) findViewById(R.id.adview);
		moPubView.setAdUnitId("<add_mopub_aduint_id_here>"); //TODO: Add MoPub AdUnit Id
		moPubView.setBannerAdListener(moPubAdListener);
		
		// Local Extra parameters are optional
		Map<String, Object> localExtras = new HashMap<String, Object>();
		localExtras.put(MoceanBannerAdapter.KEY_MOCEAN_LOCATION_DETECTION_FLAG, Boolean.TRUE);
		// Remove/disable logging in production
		localExtras.put(MoceanBannerAdapter.KEY_MOCEAN_LOG_LEVEL, LogLevel.Debug);
		moPubView.setLocalExtras(localExtras);
		
		// Load MoPub Ad
		moPubView.loadAd();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			refreshAdView();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refreshAdView() {
		if (moPubView != null) {
			moPubView.loadAd();
		}
	}

	@Override
	protected void onDestroy() {
		moPubView.destroy();
		super.onDestroy();
	}

	private BannerAdListener moPubAdListener = new BannerAdListener() {

		@Override
		public void onBannerLoaded(MoPubView view) {
			Log.d(TAG, "onBannerLoaded");

		}

		@Override
		public void onBannerFailed(MoPubView view, MoPubErrorCode errorCode) {
			Log.d(TAG, "onBannerFailed. Error: " + errorCode.toString());

		}

		@Override
		public void onBannerExpanded(MoPubView view) {
			Log.d(TAG, "onBannerExpanded");

		}

		@Override
		public void onBannerCollapsed(MoPubView view) {
			Log.d(TAG, "onBannerCollapsed");

		}

		@Override
		public void onBannerClicked(MoPubView arg0) {
			Log.d(TAG, "onBannerClicked");

		}
	};

}