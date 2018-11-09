package fr.acinq.eclair.wallet.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Observable;

import fr.acinq.eclair.wallet.api.ApiUrl;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.utils.Constants;

public class RegularPaymentViewModel extends Observable {
  int numberOfRequestsCompleted;
  //ArrayList<ScheduleList> mScheduleDataList = new ArrayList<>();
  static JSONObject jo;
  private static Context context;
  public final ObservableField<String> invoice_id = new ObservableField<>("");
  public final ObservableField<String> month_day = new ObservableField<>("");

  public RegularPaymentViewModel(Context context)
  {
    this.context = context;
  }




}
