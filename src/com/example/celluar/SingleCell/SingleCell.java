package com.example.celluar.SingleCell;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.example.Integrity.IntegrityCheck;
import com.example.celluar.CellularDown;
import com.example.entities.FileFragment;
import com.example.partyplayeractivity.MainFragment;

public class SingleCell extends Thread {
	private static final String TAG = SingleCell.class.getSimpleName();
	private int url;

	public SingleCell(int url) {
		super();
		this.url = url;
	}

	@Override
	public void run() {
		Log.d(TAG, "test " + url);
		HttpURLConnection connection = null;
		try {
			URL uurl = new URL(IntegrityCheck.URL_TAG + "?filename=" + url
					+ ".mp4&sessionid=lykfr9oyqipf2q3tvy2l73bao21" + url
					+ "&rate=" + MainFragment.rateTag);
			Log.d(TAG, "" + uurl);
			while (true) {
				connection = (HttpURLConnection) uurl.openConnection();
				connection.setRequestMethod("POST");
				connection.setConnectTimeout(5000);
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setRequestProperty("Accept-Encoding", "");
				connection.setDoOutput(true);
				Log.d(TAG,
						"ResponseCode " + url + " "
								+ connection.getResponseCode());

				if (connection.getResponseCode() == 206) {
					InputStream in = connection.getInputStream();
					String contentRange = connection.getHeaderField(
							"Content-Range").toString();
					String range = contentRange.split(" ")[1].trim();
					String start = range.split("-")[0];
					String end = range.split("-")[1].split("/")[0];
					String total = range.split("-")[1].split("/")[1];
					Log.d(TAG, "Total " + url + " " + total);
					Log.d(TAG, "PieceStart " + url + " " + start);
					Log.d(TAG, "PieceEnd " + url + " " + end);
					int startOffset = Integer.parseInt(start);
					int endOffset = Integer.parseInt(end);
					int totalLength = Integer.parseInt(total);
					int pieceLength = endOffset - startOffset;

					byte[] tmpbuff = new byte[pieceLength];
					int hasRead = 0;
					while (hasRead < pieceLength) {
						hasRead += in.read(tmpbuff, hasRead, pieceLength
								- hasRead);
					}

					IntegrityCheck IC = IntegrityCheck.getInstance();
					IC.setSegLength(url, totalLength);
					FileFragment fm = new FileFragment(startOffset, endOffset,
							url);
					Log.d(TAG, "" + url + " " + fm);
					fm.setData(tmpbuff);
					IC.insert(url, fm);
				} else if (connection.getResponseCode() == 200) {
					Log.d(TAG, "else " + url);
					CellularDown.queryFragment(CellularDown.CellType.WiFiMore, url);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.d(TAG, "MalformedURLException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "IOException");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "Exception");
		} finally {
			connection.disconnect();
		}
	}

}