package com.pubmatic.sdk.nativead.bean;

public class PMTitleAssetRequest extends PMAssetRequest {

	/** Character length of title asset */
	public int length;

	public PMTitleAssetRequest(int assetId) { this.assetId = assetId; }

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
