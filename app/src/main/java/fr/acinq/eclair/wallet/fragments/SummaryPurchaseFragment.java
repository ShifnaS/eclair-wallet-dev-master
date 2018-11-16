package fr.acinq.eclair.wallet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.app.Config;
import fr.acinq.eclair.wallet.databinding.FragmentSummaryPurchaseBinding;
import fr.acinq.eclair.wallet.models.LocalChannel;
import fr.acinq.eclair.wallet.models.ScheduleDataList;
import fr.acinq.eclair.wallet.presenter.SummaryPurchasePresenter;
import fr.acinq.eclair.wallet.utils.Constants;
import fr.acinq.eclair.wallet.viewmodel.SummaryPurchaseViewModel;
import scala.Int;


public class SummaryPurchaseFragment extends Fragment {

    int monthInt=0;
    String[] day;
    String dayy="1";
    String s="";
    int mm=0;
    private SummaryPurchaseViewModel summaryPurchaseViewModel,summaryPurchaseViewModel1,summaryPurchaseViewModel2;
    public SummaryPurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSummaryPurchaseBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_summary_purchase, container, false);
        View root=binding.getRoot();
        summaryPurchaseViewModel = new SummaryPurchaseViewModel(getContext());
        Bundle bundle = getArguments();
        binding.setSummarypurchase(summaryPurchaseViewModel);
        binding.day.setVisibility(View.INVISIBLE);

        String id=bundle.getString("_id");
        String service_name=bundle.getString("service_name");
        Double im_cost=bundle.getDouble("immediate_cost");
        String amount=bundle.getString("amount");
        String frequency=bundle.getString("frequency");
        String payment_day=bundle.getString("payment_date");
        String qr_code=bundle.getString("qr_code");
        String schedule_type=bundle.getString("schedule_type");
        String schedule_id=bundle.getString("schedule_id");
        int payment_month=bundle.getInt("payment_month");
        String note=bundle.getString("note");
       // String invoice_id=bundle.getString("invoice_id");
     //   Toast.makeText(getContext(), ""+immediate_cost, Toast.LENGTH_SHORT).show();
        double amt=Double.parseDouble(amount);
        String amt_btc=BigDecimal.valueOf(amt).toPlainString();
        String immediate_cost=BigDecimal.valueOf(im_cost).toPlainString();
        String days="";

        day = new String[28];
        for(int i=0;i<28;i++)
        {
          int d=i+1;
          day[i]=""+d;
        }
        if(schedule_type.equals("1"))
        {

          binding.day.setVisibility(View.VISIBLE);
        }
        else
        {

          binding.day.setVisibility(View.INVISIBLE);
        }




        if(schedule_type.equals("1"))
        {
          //static
          if(frequency.equals("1"))
          {
            //monthly
            s="Summary of Purchase \n\n" +
              "Payment Name= "+note+" \n\n Cost = "+amt_btc +"\n Frequency = monthly \n\n ";
              dayy="1 of every month";
              mm=0;
          }
          else
          {
            //annually
            int m=getMonth(1);
            String month=getMonthInString(m);
            s="Summary of Purchase \n\n" +
              "Payment Name= "+note+" \n\n Cost = "+amt_btc +"\n Frequency = annually \n\n ";
            dayy="1 of the "+month;
            mm=m;

          }
        }
        else
        {
          //dynamic
          if(frequency.equals("1"))
          {
            //monthly
            if(service_name.equalsIgnoreCase("A"))
            {
              //service A
              s="Summary of Purchase \n\n Name – Service "+service_name+" \n Immediate Payment Cost = "+immediate_cost+" \n\n\n\n" +
                "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
              dayy=payment_day+" of the every month";

            }
            else
            {
              // other services
              s="Summary of Purchase \n\n Name – Service "+service_name+" \n\n\n\n" +
                "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
              dayy=payment_day+" of the every month";

            }
            mm=0;


          }
          else
          {
            //annually
            if(service_name.equalsIgnoreCase("Blast Off Subscription"))
            {
              String month=getMonthInString(payment_month);
              s="Summary of Purchase \n\n Name – Service "+service_name+" \n Immediate Payment Cost = "+immediate_cost+" \n\n\n\n" +
                "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
              dayy=payment_day+" of the "+month;
              mm=payment_month;

            }
            else
            {
              String month=getMonthInString(payment_month);
              s="Summary of Purchase \n\n Name – Service "+service_name+" \n\n\n\n" +
                "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
              dayy=payment_day+" of the "+month;
              mm=payment_month;

            }

          }
        }

      summaryPurchaseViewModel=new SummaryPurchaseViewModel(s,dayy);
      binding.setSummarypurchase(summaryPurchaseViewModel);



      binding.np.setMinValue(0); //from array first value
      binding.np.setMaxValue(day.length-1); //to array last value

      binding.np.setDisplayedValues(day);

      binding.np.setWrapSelectorWheel(true);



      binding.setSummaryPurchasePresenter(new SummaryPurchasePresenter() {
            @Override
            public void confirm() {

           /*   if(schedule_type.equals("1"))
              {*/
              final String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
              SharedPreferences pref = getContext().getSharedPreferences(Config.SHARED_PREF, 0);
              String regId = pref.getString("regId", null);
              Map<String, String> postParam= new HashMap<>();
                String month_day=binding.tvDate.getText().toString().trim();
                String md[]=month_day.split(" ");
                postParam.put("paymentDay", md[0]);
                postParam.put("service_name", service_name);
                postParam.put("paymentAmount", amount);
                postParam.put("frequency", frequency);
                postParam.put("schedule_id", schedule_id);
                postParam.put("schedule_type", schedule_type);
                postParam.put("qr_code", qr_code);
                postParam.put("device_id", deviceId);
                postParam.put("firebase_id", regId);


                if(frequency.equals("2"))
                {
                  postParam.put("payment_month", ""+mm);
                 // int month=getMonthInString(md[3]);
                  monthInt=mm;
                }
                else
                {
                  int day=Integer.parseInt(md[0]);
                  int month=getMonth(day);
                  monthInt=month;
                 // Toast.makeText(getContext(), "month "+month+" day "+day, Toast.LENGTH_SHORT).show();
                  postParam.put("payment_month", ""+month);

                }
                try
                {


                  Log.e("Payment data","Day "+md[0]+"Month "+md[3]);
                  VolleyLog.DEBUG = true;
                  RequestQueue queue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();



                  JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL_InvoiceID, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                      @Override
                      public void onResponse(JSONObject jsonObject) {
                        Log.e("RESPONSE", jsonObject.toString());
                     //   Toast.makeText(getContext(), "Response "+jsonObject.toString(), Toast.LENGTH_SHORT).show();
                        try {
                          Boolean error;
                          String invoice_id="";

                          if(!jsonObject.getBoolean("error"))
                          {
                            if(jsonObject.getString("message").equals("success"))
                            {
                              invoice_id=jsonObject.getString("response");
                              String month=getMonthInString(monthInt);
                              Fragment fragment = new ConfirmationFragment();
                              Bundle bundle = new Bundle();
                              bundle.putString("invoice_id", invoice_id);
                              bundle.putString("amount", ""+immediate_cost);
                              bundle.putString("month", ""+month);
                              bundle.putString("day", ""+md[0]);

                              fragment.setArguments(bundle);

                              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                              //fragmentManager.popBackStackImmediate();

                              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                              fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                             // fragmentTransaction.addToBackStack(null);
                              fragmentTransaction.commit();
                            }
                            else
                            {
                              Toast.makeText(getContext(), ""+jsonObject.getString("response"), Toast.LENGTH_SHORT).show();

                            }
                          }
                          else
                          {
                            Toast.makeText(getContext(), ""+jsonObject.getString("response"), Toast.LENGTH_SHORT).show();
                          }


                        }
                        catch (JSONException e) {
                          e.printStackTrace();
                        }


                        //jo =jsonObject;
                      }
                    }, error -> VolleyLog.d("VOLLEY EROOR", "Error: " + error.getMessage())) {


                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                      HashMap<String, String> headers = new HashMap<String, String>();
                      headers.put("Content-Type", "application/json; charset=utf-8");
                      return headers;
                    }



                  };

                  queue.add(jsonObjReq);

                }
                catch (Exception e)
                {
                  Log.e("ERRROR","====== "+e.getMessage());


                }


             /* }
              else
              {

              }*/

            }

            @Override
            public void cancel() {
              Fragment fragment = new LightningFragment();
              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
              //  fragmentTransaction.addToBackStack(null);
              //fragmentTransaction.commit();
              fragmentTransaction.commitAllowingStateLoss();
            }

        @Override
        public void onValChange(int old, int newval) {

          if(schedule_type.equals("1"))
          {
            //static
            if(frequency.equals("1"))
            {
              //monthly
              s="Summary of Purchase \n\n" +
              "Payment Name= "+note+" \n\n Cost = "+amt_btc +"\n Frequency = monthly \n\n ";
              dayy=day[newval]+" of every month";
              mm=0;
            }
            else
            {
              //annually
              String m=day[newval];
              int mmm=Integer.parseInt(m);
              int m1=getMonth(mmm);
              String month=getMonthInString(m1);
              s="Summary of Purchase \n\n" +
              "Payment Name= "+note+" \n\n  Cost = "+amt_btc +"\n Frequency = annually \n\n ";
              dayy=day[newval]+" of the "+month;
              mm=m1;

            }
          }
          else
          {
            //dynamic
            if(frequency.equals("1"))
            {
              //monthly
              if(service_name.equalsIgnoreCase("A"))
              {
                //service A
                s="Summary of Purchase \n\n Name – Service "+service_name+" \n Immediate Payment Cost = "+immediate_cost+" \n\n\n\n" +
                  "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
                dayy=payment_day+" of the every month";
                mm=0;
              }
              else
              {
                // other services
                s="Summary of Purchase \n\n Name – Service "+service_name+" \n\n\n\n" +
                  "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
                dayy=payment_day+" of the every month";
                mm=0;
              }

            }
            else
            {
              //annually
              if(service_name.equalsIgnoreCase("A"))
              {
                String month=getMonthInString(payment_month);

                s="Summary of Purchase \n\n Name – Service "+service_name+" \n Immediate Payment Cost = "+immediate_cost+" \n\n\n\n" +
                  "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
                dayy=payment_day+" of the "+payment_month;
                mm=payment_month;
              }
              else
              {
                String month=getMonthInString(payment_month);

                s="Summary of Purchase \n\n Name – Service "+service_name+" \n\n\n\n" +
                  "Cost = "+amt_btc +"\n Frequency = monthly\n\n" ;
                dayy=payment_day+" of the "+payment_month;
                mm=payment_month;
              }

            }
          }
          summaryPurchaseViewModel=new SummaryPurchaseViewModel(s,dayy);
          binding.setSummarypurchase(summaryPurchaseViewModel);
        }


      });

        return root;
    }
  public int getMonth(int day)
  {
    int month;
    Calendar c = Calendar.getInstance();


    int dom = c.get(Calendar.DAY_OF_MONTH);
    if(day<=dom)
    {
      month=c.get(Calendar.MONTH)+2;

    }
    else
    {
      month=c.get(Calendar.MONTH)+1;

    }

    return  month;
  }
  public String getMonthInString(int m)
  {
    String[]monthName={"January","February","March", "April", "May", "June", "July",
      "August", "September", "October", "November",
      "December"};

    return  monthName[m-1];
  }

}
