package fr.acinq.eclair.wallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.adapters.NotificationAdapter;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.utils.Constants;

public class NotificationActivity extends AppCompatActivity {
  RecyclerView recyclerView;
  private JSONArray list;
  private NotificationAdapter mAdapter;
  OnItemClick onItemClick;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);
    recyclerView=findViewById(R.id.recyclerview);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
    setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

    final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    Date cDate = new Date();
    String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));


    VolleyLog.DEBUG = true;
    RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

    final String url = String.format(String.format(Constants.URL_NOTIFICATION_LIST+date+"/"+deviceId));
    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
      jo -> {


        try
        {
          Log.e("RESPonse ","********** n "+jo);
          String status = jo.getString("status");
          boolean error = jo.getBoolean("error");
          if (!error)
          {
            if (status.equalsIgnoreCase("success"))
            {
              list=jo.getJSONArray("message");
              mAdapter = new NotificationAdapter(list,getApplicationContext(),onItemClick);
              recyclerView.setAdapter(mAdapter);


            }
            else
            {
              Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
            }
          }
          else
          {
            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

          }

        } catch (JSONException e)
        {
          e.printStackTrace();
        }


      },
      error -> Log.e("Error.Response", "************************"+error.getMessage())
    );
    queue.add(getRequest);





    ////////////////////////////////////////////////////
    onItemClick=new OnItemClick() {
      @Override
      public void onClick(String invoice_id) {
        Toast.makeText(getApplicationContext(), "invoice id "+invoice_id, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SendPaymentActivity.class);
        intent.putExtra(SendPaymentActivity.EXTRA_INVOICE, "lightning:"+invoice_id);
        intent.putExtra(SendPaymentActivity.EXTRA_D, "payment_desc");
        startActivity(intent);
      }
    };

    ////////////////////////////////////////////////////
  }

  public interface OnItemClick {
    void onClick (String invoice_id);
  }
}
