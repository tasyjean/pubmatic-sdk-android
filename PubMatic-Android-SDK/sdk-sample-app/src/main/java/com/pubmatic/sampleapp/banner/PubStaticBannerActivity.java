/*
 * PubMatic Inc. (�PubMatic�) CONFIDENTIAL
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
package com.pubmatic.sampleapp.banner;

import android.app.Activity;
import android.os.Bundle;

import com.pubmatic.sampleapp.R;
import com.pubmatic.sdk.banner.PMBannerAdView;

public class PubStaticBannerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pubmatic_activity_static_banner);

		PMBannerAdView banner = (PMBannerAdView)findViewById(R.id.banner);
		banner.setFeatureSupportHandler(new PMBannerAdView.BannerAdViewDelegate.FeatureSupportHandler() {
			@Override
			public Boolean shouldSupportSMS(PMBannerAdView adView) {
				return true;
			}

			@Override
			public Boolean shouldSupportPhone(PMBannerAdView adView) {
				return true;
			}

			@Override
			public Boolean shouldSupportCalendar(PMBannerAdView adView) {
				return true;
			}

			@Override
			public Boolean shouldSupportStorePicture(PMBannerAdView adView) {
				return true;
			}

			@Override
			public boolean shouldStorePicture(PMBannerAdView sender, String url) {
				return true;
			}

			@Override
			public boolean shouldAddCalendarEntry(PMBannerAdView sender, String calendarProperties) {
				return true;
			}
		});
	}

}
