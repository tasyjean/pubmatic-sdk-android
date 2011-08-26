package com.adserver.adview;

import android.widget.Button;

public class VisibilityButtonRunnable implements Runnable {
	private Button button;
	private int visibility;
	
	public VisibilityButtonRunnable(Button button, int visibility) {
		this.button = button;
		this.visibility = visibility;
	}

	public void run() {
		if(button != null) {
			button.setVisibility(visibility);
		}
	}
}