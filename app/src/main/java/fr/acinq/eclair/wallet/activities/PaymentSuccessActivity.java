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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.app.Config;
import fr.acinq.eclair.wallet.utils.Constants;

public class PaymentSuccessActivity extends EclairActivity  {

  public static final String EXTRA_PAYMENTSUCCESS_AMOUNT = "fr.acinq.eclair.wallet.EXTRA_PAYMENTSUCCESS_AMOUNT";
  public static final String EXTRA_PAYMENTSUCCESS_DESC = "fr.acinq.eclair.wallet.EXTRA_PAYMENTSUCCESS_DESC";
  public static final String EXTRA_IVOICE_ID = "fr.acinq.eclair.wallet.EXTRA_IVOICE_ID";

  private static final String TAG = "PaymentSuccessActivity";

  private Handler dismissHandler;
  private ImageView mCircleImage;
  private ImageView mCheckImage;
  private TextView mDescView;
  private TextView mDescView_success;
  static FragmentCommunicator fragmentCommunicator;

  public PaymentSuccessActivity() {
  }

  public PaymentSuccessActivity( FragmentCommunicator fragmentCommunicator) {
    this.fragmentCommunicator=fragmentCommunicator;
  }



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_success);
    //fragmentListner=(FragmentListner)this;
    //HomeActivity homeActivity=new HomeActivity();

    Intent intent = getIntent();
    String desc = intent.getStringExtra(EXTRA_PAYMENTSUCCESS_DESC);

    mDescView = findViewById(R.id.paymentsuccess_desc);
    mCircleImage = findViewById(R.id.paymentsuccess_circle);
    mCheckImage = findViewById(R.id.paymentsuccess_check);
    mDescView_success = findViewById(R.id.paymentsuccess);
    mDescView.setText(desc);


    if(intent.hasExtra(EXTRA_IVOICE_ID))
    {
    /*  String nodeid=app.nodePublicKey();
      Toast.makeText(app, "nodeId "+nodeid, Toast.LENGTH_SHORT).show();*/

      String data=intent.getStringExtra(EXTRA_IVOICE_ID);

      String my[]=data.split(",");

      String invoice_id=my[0];
      mDescView_success.setText(my[1]);


      final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
      SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
      String regId = pref.getString("regId", null);

      VolleyLog.DEBUG = true;
      RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

      final String url = String.format(String.format(Constants.URL_OK+invoice_id+"/"+deviceId+"/"+regId));
      JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
        jo -> {
          //Log.e("Response", "*******************"+jo.toString());
          // jo=res;
          try
          {
            String response = jo.getString("message");
            boolean error = jo.getBoolean("error");
            if (!error)
            {
              if (response.equalsIgnoreCase("success"))
              {
                if(my.length==3)
                {
                    Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                  fragmentCommunicator.passData("success");
                }



              }
              else
              {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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

    }
    else
    {
      Intent inten = new Intent(getBaseContext(), HomeActivity.class);
      inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(inten);
      finish();
    }

    tada();
    dismissHandler = new Handler();
    dismissHandler.postDelayed(() -> finish(), 3500);
  }

  private void tada() {
    mCircleImage.setAlpha(0f);
    mCircleImage.setScaleX(0.3f);
    mCircleImage.setScaleY(0.3f);

    mCheckImage.setAlpha(0f);
    mCheckImage.setScaleX(0.6f);
    mCheckImage.setScaleY(0.6f);

    mCircleImage.animate().alpha(1).scaleY(1).scaleX(1).setStartDelay(80).setInterpolator(new AnticipateOvershootInterpolator()).setDuration(350).start();
    mCheckImage.animate().alpha(1).scaleY(1).scaleX(1).setStartDelay(120).setInterpolator(new AnticipateOvershootInterpolator()).setDuration(500).start();
  }

  public void success_tap(View view) {
    tada();
  }

  public void success_dismiss(View view) {
    dismissHandler.removeCallbacks(null);
    finish();
  }


}
