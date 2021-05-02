/**
 * 
 */
package com.ayansh.spellathon.android.UI;

import java.util.ArrayList;
import java.util.List;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.billingutil.IabHelper;
import com.ayansh.spellathon.android.billingutil.IabResult;
import com.ayansh.spellathon.android.billingutil.Inventory;
import com.ayansh.spellathon.android.billingutil.Purchase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Varun Verma
 *
 */
public class ActivatePremiumFeatures extends AppCompatActivity
		implements IabHelper.OnIabSetupFinishedListener, IabHelper.QueryInventoryFinishedListener,
					OnClickListener, IabHelper.OnIabPurchaseFinishedListener {

	private static final String TAG = "InAppBilling";
	private IabHelper billingHelper;
	private TextView prodName, prodDesc, prodHelp;
	private Button buy, cancel;
	private String prodKey;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.premium_features);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		setTitle("Activate Premium Features");

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);
		
		prodKey = Application.constants.getProductKey("ShowMeanings");
		
		prodName = (TextView) findViewById(R.id.product_name);
		prodDesc = (TextView) findViewById(R.id.product_desc);
		prodHelp = (TextView) findViewById(R.id.product_help);
		
		prodName.setText("Premium Feature - Show meaning of words");
		prodDesc.setText("See the meaning of words, and remove all ads.");
		
		showProductHelp();
		
		buy = (Button) findViewById(R.id.buy);
		buy.setText("$ 1.00");
		buy.setOnClickListener(this);
		
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		
		billingHelper = Application.get_instance().billingHelper;
		
		if(billingHelper == null){
			
			// Billing Helper
			billingHelper = new IabHelper(this, Application.constants.getPublicKey());
			
			// Start the set up
			billingHelper.startSetup(this);
		}
		else{
			// Query for Product Details
			List<String> productList = new ArrayList<String>();
			productList.add(prodKey);
			billingHelper.queryInventoryAsync(true, productList,this);
		}
		
	}

	private void showProductHelp() {

		String help = "After purchasing this product, " +
				"you will be able to see the meaning of the words. \n" +
				"All Ads will be removed from the app. \n\n" +
				"This is a one time purchase only.";

		prodHelp.setText(help);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case android.R.id.home:
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onIabSetupFinished(IabResult result) {

		if (!result.isSuccess()) {
			// Oh noes, there was a problem.
			Log.d(TAG, "Problem setting up In-app Billing: " + result);
		}
		else{
			Application.get_instance().billingHelper = billingHelper;
			
			// Query for Product Details
			List<String> productList = new ArrayList<String>();
			productList.add(prodKey);
			billingHelper.queryInventoryAsync(true, productList,this);
		}

	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inv) {
		
		if (result.isFailure()) {
			// TODO handle error
			return;
		}
		
		String title = inv.getSkuDetails(prodKey).getTitle();
		prodName.setText(title);
		
		String desc = inv.getSkuDetails(prodKey).getDescription();
		prodDesc.setText(desc);
		
		String price = inv.getSkuDetails(prodKey).getPrice();
		buy.setText(price);

	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.buy:
			buy();
			break;
			
		case R.id.cancel:
			finish();
			break;
		
		}
		
	}

	private void buy() {
		// Buy
		String devPayLoad = ""; //Application.get_instance().getEmail();
		billingHelper.launchPurchaseFlow(this, prodKey, 22, this, devPayLoad);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(billingHelper.handleActivityResult(requestCode, resultCode, data)){
			// Nothing
		}
		else{
			// Handle
		}
		
	}
	
	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase info) {

		if (result.isFailure()) {
			Log.d(TAG, "Error purchasing: " + result);
			return;
		}
		
		if(info.getSku().contentEquals(prodKey)){
			// Purchase was success
			Application.constants.setPremiumVersion(true);
		}
		
	}
	
}