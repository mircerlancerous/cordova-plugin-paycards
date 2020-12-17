package com.otb.cordova.paycards;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import cards.pay:paycardsrecognizer:1.1.0;
import cards.pay.paycardsrecognizer.sdk.Card;
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent;
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent.CancelReason;

public class PayCardsPlugin extends CordovaPlugin {
	private CallbackContext callback = null;
	static final int REQUEST_CODE_SCAN_CARD = 1;
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		
		
	}
	
	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

		if (action.equalsIgnoreCase("canScan")) {
			canScan(callbackContext);
		}
		else if(action.equalsIgnoreCase("scan")){
			scan(callbackContext);
		}
		else{
			//invalid action
			return false;
		}
		
		return true;
	}
	
	private void canScan(CallbackContext callbackContext){
		PluginResult result = new PluginResult(PluginResult.Status.OK,"");
		callbackContext.sendPluginResult(result);
	}
	
	private void scan(CallbackContext callbackContext){
		//Intent intent = new ScanCardIntent.Builder(this).build();
		Intent intent = new ScanCardIntent.Builder(this.cordova.getActivity().getApplicationContext()).build();
		
		final CordovaPlugin plugin = (CordovaPlugin) this;
		plugin.cordova.setActivityResultCallback(plugin);
		plugin.cordova.startActivityForResult(plugin, intent, REQUEST_CODE_SCAN_CARD);
		//startActivityForResult(intent, REQUEST_CODE_SCAN_CARD);
		
		callback = callbackContext;
		PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
		result.setKeepCallback(true);
		callbackContext.sendPluginResult(result);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE_SCAN_CARD){
			PluginResult result = new PluginResult(PluginResult.Status.OK,"no data");
			
			if(resultCode == Activity.RESULT_OK){
				Card card = data.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD);
				String cardData = "card_number:" + card.getCardNumber() + "\n"
					+ "card_number_redacted:" + card.getCardNumberRedacted() + "\n"
					+ "card_holder:" + card.getCardHolderName() + "\n"
					+ "card_expiry:" + card.getExpirationDate();
				result = new PluginResult(PluginResult.Status.OK,cardData);
			}
			else if(resultCode == Activity.RESULT_CANCELED){
				//scan cancelled
				result = new PluginResult(PluginResult.Status.ERROR,"scan cancelled");
			}
			else{
				//scan failed
				result = new PluginResult(PluginResult.Status.ERROR,"scan failed");
			}
			
			callback.sendPluginResult(result);
		}
	}
}