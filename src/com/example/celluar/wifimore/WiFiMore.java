package com.example.celluar.wifimore;

import android.util.Log;

import com.example.Integrity.IntegrityCheck;
import com.example.entities.Segment;

public class WiFiMore extends Thread {
	private static final String TAG = WiFiMore.class.getSimpleName();

	private int url;

	public WiFiMore(int url) {
		this.url = url;
	}

	@Override
	public void run() {
		IntegrityCheck IC = IntegrityCheck.getInstance();
		Segment Seg = IC.getSeg(url);
		if (Seg != null) {
			while (!Seg.checkIntegrity()) {
				Log.d(TAG, "no " + url);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			Log.d(TAG, "yes " + url);
		} else {
			Log.e(TAG, "a " + url);
		}
	}
}
