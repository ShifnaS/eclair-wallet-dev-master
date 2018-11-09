package fr.acinq.eclair.wallet.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.utils.Constants;
import ujson.Js;

public class SummaryPurchaseViewModel extends Observable {
  private Context context;
  public final ObservableField<String > summary = new ObservableField<>("");
  public final ObservableField<String > day = new ObservableField<>("");
  public final ObservableField<String > days = new ObservableField<>("");
  public final ObservableField<String > daymonth = new ObservableField<>("");
  JSONObject jo= new JSONObject();


  public SummaryPurchaseViewModel(Context context)
  {
    this.context = context;
  }

  public SummaryPurchaseViewModel(String text,String a)
  {
      this.summary.set(text);
      this.days.set(a);
  }



}
