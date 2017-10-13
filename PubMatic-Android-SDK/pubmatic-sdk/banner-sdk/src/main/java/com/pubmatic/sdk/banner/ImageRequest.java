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

package com.pubmatic.sdk.banner;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pubmatic.sdk.banner.ui.GifDecoder;
import com.pubmatic.sdk.common.CommonConstants;

public class ImageRequest {
	public static ImageRequest create(int timeout, String url,
			String userAgent, boolean useGifDecoder, Handler handler) {
		if (handler == null)
			return null;

		ImageRequest imageRequest = new ImageRequest(timeout, url, userAgent,
													 useGifDecoder, handler);
		imageRequest.start();
		return imageRequest;
	}

	private final int timeout;
	private final String url;
	private final String userAgent;
	private final boolean useGifDecoder;
	private Handler handler = null;

	public ImageRequest(int timeout, String url, String userAgent,
			boolean useGifDecoder, Handler handler) {
		this.timeout = timeout;
		this.url = url;
		this.userAgent = userAgent;
		this.useGifDecoder = useGifDecoder;
		this.handler = handler;
	}

	public void cancel() {
		handler = null;
	}

	private void start() {
		RequestProcessor processor = new RequestProcessor();
		Background.getExecutor().execute(processor);
	}

	private class RequestProcessor implements Runnable {
		@Override
		public void run() {
			try {
				URL httpUrl = new URL(url);
				HttpURLConnection urlConnection = (HttpURLConnection) httpUrl
						.openConnection();
				urlConnection.setConnectTimeout(timeout * 1000);
				urlConnection.setRequestProperty(
						CommonConstants.HTTP_REQ_HEADER_USER_AGENT,
						userAgent);
				urlConnection.setRequestProperty(
						CommonConstants.HTTP_REQ_HEADER_CONNECTION,
						CommonConstants.HTTP_REQ_HEADER_CONNECTION_CLOSE);
				urlConnection.setDoInput(true);
				urlConnection
						.setRequestMethod(CommonConstants.HTTPMETHODGET);

				if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					if (handler != null) {
						handler.imageFailed(ImageRequest.this, null);
					}
					return;
				}

				final int bufferSize = 8192 * 4;
				InputStream inputStream = urlConnection.getInputStream();
				inputStream = new BufferedInputStream(inputStream, bufferSize);

				inputStream.mark(bufferSize);

				boolean isGif = false;
				if (useGifDecoder) {
					byte[] gifBuffer = new byte[3];
					inputStream.read(gifBuffer);
					if ((gifBuffer[0] == 'G') && (gifBuffer[1] == 'I')
							&& (gifBuffer[2] == 'F')) {
						isGif = true;
					}
					inputStream.reset();
				}

				Object imageObject = null;

				if (isGif) {
					GifDecoder gifDecoder = new GifDecoder();
					int status = gifDecoder.read(inputStream);

					if (status == GifDecoder.STATUS_OK) {
						imageObject = gifDecoder;
					}
				} else {
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

					if (bitmap != null) {
						imageObject = bitmap;
					}
				}

				if (imageObject != null) {
					handler.imageReceived(ImageRequest.this, imageObject);
				} else {
					handler.imageFailed(ImageRequest.this, null);
				}

				inputStream.close();
				urlConnection.disconnect();
			} catch (Exception ex) {
				handler.imageFailed(ImageRequest.this, ex);
			}
		}
	}

	public interface Handler {
		void imageFailed(ImageRequest request, Exception exception);

		void imageReceived(ImageRequest request, Object imageObject);
	}
}
