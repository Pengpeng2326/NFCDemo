package com.tapped.nfc;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class BeamActivity extends Activity implements CreateNdefMessageCallback, 
OnNdefPushCompleteCallback {
	NfcAdapter mNfcAdapter;
	EditText mEditText;
	
	private static final String MIME_TYPE = "application/com.tapped.nfc";
	private static final String PACKAGE_NAME = "com.tapped.nfc";
	private static final int MESSAGE_SENT = 1;
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SENT:
				Toast.makeText(getApplicationContext(), "Message sent!",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beam);
		
		mEditText = (EditText) findViewById(R.id.beam_edit_text);
		
		// Check for available NFC Adapter
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if(mNfcAdapter == null) {
			Toast.makeText(this, "Sorry, NFC is not available on this device", 
					Toast.LENGTH_SHORT).show();
		}
		
		// Register callback to set NDEF message
		mNfcAdapter.setNdefPushMessageCallback(this, this);
		// Register callback to listen for message-sent success
		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beam, menu);
		return true;
	}
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		String text = mEditText.getText().toString();
		NdefMessage msg = new NdefMessage(new NdefRecord[] {
				//NfcUtils.createRecord(MIME_TYPE, text.getBytes()), 
				NdefRecord.createApplicationRecord(PACKAGE_NAME)
				
		});
		return msg;
	}
	
	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	}

}
