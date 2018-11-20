/*
 * Copyright 2018 ACINQ SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.acinq.eclair.wallet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.List;

import fr.acinq.eclair.wallet.BuildConfig;
import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.adapters.LightningErrorListAdapter;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.app.Config;
import fr.acinq.eclair.wallet.models.LightningPaymentError;
import fr.acinq.eclair.wallet.utils.Constants;

public class PaymentFailureActivity extends EclairActivity {

  public static final String EXTRA_PAYMENT_HASH = BuildConfig.APPLICATION_ID + "EXTRA_PAYMENT_HASH";
  public static final String EXTRA_PAYMENT_DESC = BuildConfig.APPLICATION_ID + "EXTRA_PAYMENT_DESC";
  public static final String EXTRA_PAYMENT_SIMPLE_ONLY = BuildConfig.APPLICATION_ID + "EXTRA_SIMPLE_ONLY";
  public static final String EXTRA_PAYMENT_SIMPLE_MESSAGE = BuildConfig.APPLICATION_ID + "EXTRA_SIMPLE_MESSAGE";
  public static final String EXTRA_PAYMENT_ERRORS = BuildConfig.APPLICATION_ID + "EXTRA_PAYMENT_ERRORS";
  public static final String EXTRA_IVOICE_ID = BuildConfig.APPLICATION_ID + "EXTRA_IVOICE_ID";

  private static final String TAG = "PaymentFailureActivity";

  private View mPaymentDescView;
  private TextView mPaymentDescValue;
  private TextView mMessageView;
  private View mDetails;
  private Button mShowDetailed;
  private ImageButton mClose;
  private RecyclerView mErrorsView;

  static FragmentCommunicator fragmentCommunicator;

  public PaymentFailureActivity() {
  }

  public PaymentFailureActivity( FragmentCommunicator fragmentCommunicator) {
    this.fragmentCommunicator=fragmentCommunicator;
  }



  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_failure);
    mMessageView = findViewById(R.id.paymentfailure_simple);
    mDetails = findViewById(R.id.payment_failure_details);
    mPaymentDescView = findViewById(R.id.paymentfailure_paymentdesc);
    mPaymentDescValue = findViewById(R.id.paymentfailure_paymentdesc_value);
    mShowDetailed = findViewById(R.id.paymentfailure_show_details);
    mErrorsView = findViewById(R.id.paymentfailure_errors);

    final Intent intent = getIntent();
    final String paymentDescription = intent.getStringExtra(EXTRA_PAYMENT_DESC);
    final String simpleMessage = intent.getStringExtra(EXTRA_PAYMENT_SIMPLE_MESSAGE);
    final boolean displaySimpleMessageOnly = intent.getBooleanExtra(EXTRA_PAYMENT_SIMPLE_ONLY, true);
    final String data = intent.getStringExtra(EXTRA_IVOICE_ID);
    String my[]=data.split(",");
    String invoice_id=my[0];

   // Toast.makeText(app, "Payment invoice id "+intent.getStringExtra(EXTRA_IVOICE_ID), Toast.LENGTH_SHORT).show();

    final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
    String regId = pref.getString("regId", null);

    mPaymentDescValue.setText(paymentDescription);
    if (displaySimpleMessageOnly) {
      mMessageView.setText(simpleMessage);
      mShowDetailed.setVisibility(View.GONE);
      mPaymentDescView.setVisibility(View.VISIBLE);
    } else {
      final List<LightningPaymentError> errors = intent.getParcelableArrayListExtra(EXTRA_PAYMENT_ERRORS);
      mMessageView.setText(getString(R.string.paymentfailure_error_size, errors.size()));

      mErrorsView.setHasFixedSize(true);
      mErrorsView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
      mErrorsView.setAdapter(new LightningErrorListAdapter(errors));

      mShowDetailed.setOnClickListener(view -> {
        mShowDetailed.setVisibility(View.GONE);
        mPaymentDescView.setVisibility(View.VISIBLE);
        mErrorsView.setVisibility(View.VISIBLE);
      });
    }

    VolleyLog.DEBUG = true;
    RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

    final String url = String.format(String.format(Constants.URL_OK+invoice_id+"/"+deviceId+"/"+regId+"/1"));
    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
      jo -> {

        try
        {
          String response = jo.getString("message");
          boolean error = jo.getBoolean("error");
          if (!error)
          {
            Toast.makeText(app, "Payment Failed", Toast.LENGTH_SHORT).show();

            if(!invoice_id.equals("A"))
            {
              if(my.length==4)
              {
                Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
                finish();
              }
              else
              {
                fragmentCommunicator.passData("failed");
              }



            }


          }
          else
          {
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

          }

        } catch (JSONException e)
        {
          e.printStackTrace();
        }


      },
      error -> Log.e("Error.Response", "************************"+error.getMessage())
    );
    queue.add(getRequest);




    mClose = findViewById(R.id.paymentfailure_close);
    mClose.setOnClickListener(view -> finish());

    // animation
    final ImageView mSadImage = findViewById(R.id.paymentfailure_sad);
    mSadImage.setAlpha(0f);
    mSadImage.setScaleX(0.6f);
    mSadImage.setScaleY(0.6f);
    mSadImage.animate().alpha(1).scaleX(1).scaleY(1)
      .setInterpolator(new AnticipateOvershootInterpolator()).setStartDelay(80).setDuration(500).start();
  }
}
