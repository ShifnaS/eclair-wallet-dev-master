package fr.acinq.eclair.wallet.fragments;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.databinding.FragmentNewRegularPaymentBinding;
import fr.acinq.eclair.wallet.models.ScheduleDataList;
import fr.acinq.eclair.wallet.presenter.NewRegularPaymentPresenter;
import fr.acinq.eclair.wallet.utils.Constants;
import fr.acinq.eclair.wallet.viewmodel.RegularPaymentViewModel;
import scala.Int;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewRegularPaymentFragment extends Fragment {
  private IntentIntegrator qrScan;
  private RegularPaymentViewModel regularPaymentViewModel;
  FragmentNewRegularPaymentBinding binding;
  JSONObject jo;
    public NewRegularPaymentFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,
               R.layout.fragment_new_regular_payment, container, false);
        View root = binding.getRoot();
        regularPaymentViewModel = new RegularPaymentViewModel(getContext());
        binding.setRegularpayment(regularPaymentViewModel);

        qrScan  = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
          @Override
          public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i("yfgf", "keyCode: " + keyCode);
            if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
              Log.i("dffdfffd", "onKey Back listener is working!!!");
              getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //  CallHomeActivity.s
           /* Intent i=new Intent(getContext(),HomeActivity.class);
            startActivity(i);*/
              return true;
            }
            return false;
          }
        });


        binding.setNewRegularPaymentPresenter(new NewRegularPaymentPresenter() {

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

           // final String invoice_id = binding.invoiceId.getText().toString().trim();
            if ( s.length() == 0) {
              binding.disable.setVisibility(View.VISIBLE);
              binding.confirm.setVisibility(View.GONE);
            } else {

              binding.disable.setVisibility(View.GONE);
              binding.confirm.setVisibility(View.VISIBLE);

            }
          }

          @Override
          public void scan() {

            qrScan.setPrompt(getActivity().getString(R.string.scan_bar_code));
            qrScan.setBeepEnabled(true);
            qrScan.setOrientationLocked(false);
            qrScan.setCameraId(0);  // Use a specific camera of the device
            qrScan.initiateScan();

          }

          @Override
          public void confirm() {

            try {


              final String invoice_id = binding.invoiceId.getText().toString().trim();
              //
              if (invoice_id.equals(""))
              {
                Toast.makeText(getContext(), "Please enter an invoice id or scan an invoice", Toast.LENGTH_SHORT).show();
              }
              else
                {
              //  JSONObject jo = RegularPaymentViewModel.sendInvoiceId(invoice_id);

                 //////////////////////////////////////////////////////////////////////////

                  VolleyLog.DEBUG = true;
                  RequestQueue queue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
                  final String url = String.format(String.format(Constants.URL_SCAN+invoice_id));
                  JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    jo -> {
                      Log.e("Response", "*******************"+jo.toString());
                     // jo=res;if(
                      if(jo!=null)
                      {
                        try
                        {
                          String schedule_id = "",_id, note="",schedule_type = "", service_name = "",  payment_date = "", qr_code = "", amount = "", frequency = "";
                          int stat,payment_month;
                          double immediate_cost;
                          String status = jo.getString("status");
                          boolean error = jo.getBoolean("error");
                         // Toast.makeText(getContext(), "status "+status, Toast.LENGTH_SHORT).show();
                          if (!error) {
                           // Toast.makeText(getContext(), "//status "+status, Toast.LENGTH_SHORT).show();

                            if (status.equalsIgnoreCase("success")) {
                            //  Toast.makeText(getContext(), "***status "+status, Toast.LENGTH_SHORT).show();


                              JSONArray jsonArray = jo.getJSONArray("response");
                              Log.e("JSON ARRAY","========= "+jsonArray.toString());
                              //now looping through all the elements of the json array
                              for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);



                                if (jsonObject.has("_id")) {
                                  _id = jsonObject.getString("_id");
                                } else {
                                  _id = "";
                                }
                                if (jsonObject.has("service_name")) {
                                  service_name = jsonObject.getString("service_name");
                                } else {
                                  service_name = "";
                                }
                                if (jsonObject.has("immediate_cost")) {
                                  if(jsonObject.isNull("immediate_cost"))
                                  {
                                    immediate_cost =0.0;
                                    //Toast.makeText(getContext(), "inside if", Toast.LENGTH_SHORT).show();
                                  }
                                  else
                                  {
                                   // Toast.makeText(getContext(), "inside else", Toast.LENGTH_SHORT).show();

                                    immediate_cost = jsonObject.getDouble("immediate_cost");
                                  }
                                } else {
                                  immediate_cost =0.0;
                                }
                                if (jsonObject.has("payment_month")) {
                                  payment_month = jsonObject.getInt("payment_month");
                                } else {
                                  payment_month = 0;
                                }
                                if (jsonObject.has("payment_day")) {
                                  payment_date = jsonObject.getString("payment_day");
                                } else {
                                  payment_date = "";
                                }
                                if (jsonObject.has("qr_code")) {
                                  qr_code = jsonObject.getString("qr_code");
                                } else {
                                  qr_code = "";
                                }
                                if (jsonObject.has("amount")) {
                                  amount = jsonObject.getString("amount");
                                } else {
                                  amount = "";
                                }
                                if (jsonObject.has("frequency")) {
                                  frequency = jsonObject.getString("frequency");
                                } else {
                                  frequency = "";
                                }
                                if (jsonObject.has("schedule_type")) {
                                  schedule_type = jsonObject.getString("schedule_type");
                                } else {
                                  frequency = "";
                                }
                                if (jsonObject.has("schedule_id")) {
                                  schedule_id = jsonObject.getString("schedule_id");
                                } else {
                                  schedule_id = "";
                                }
                                if (jsonObject.has("note")) {
                                  note = jsonObject.getString("note");
                                } else {
                                  note = "";
                                }

                                 // Toast.makeText(getContext(), "sssssssss", Toast.LENGTH_SHORT).show();
                                  Fragment fragment = new SummaryPurchaseFragment();
                                  Bundle bundle = new Bundle();
                                  bundle.putString("_id", _id);
                                  bundle.putString("service_name", service_name);
                                  bundle.putDouble("immediate_cost", immediate_cost);
                                  bundle.putString("amount", amount);
                                  bundle.putString("frequency", frequency);
                                  bundle.putString("payment_date", payment_date);
                                  bundle.putString("qr_code", qr_code);
                                  bundle.putString("schedule_type", schedule_type);
                                  bundle.putString("schedule_id", schedule_id);
                                  bundle.putInt("payment_month", payment_month);
                                  bundle.putString("note", note);
                                  fragment.setArguments(bundle);
                                  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                  FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                  fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                                  fragmentTransaction.addToBackStack(null);
                                  fragmentTransaction.commit();


                              }


                            }

                            else {
                              Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                            }
                          } else {
                            Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();

                          }

                        } catch (JSONException e) {
                          e.printStackTrace();
                          Log.e("Immediate cost error","Errror "+e.getMessage());
                        }
                      }
                      else
                      {
                        Toast.makeText(getContext(), "Invalid qr code", Toast.LENGTH_SHORT).show();
                      }



                    },
                    error -> Log.e("Error.Response", "************************"+error.getMessage())
                  );
                  queue.add(getRequest);

              }


            }
            catch (Exception e) {
              e.printStackTrace();
              Log.e("Error/////////", "//////////////////" + e.getMessage());
            }


          }


        });



        return root;
    }

  //Getting the scan results
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if (result != null) {
      //if qrcode has nothing in it
        if (result.getContents() == null)
        {
          Toast.makeText(getContext(), "Result Not Found", Toast.LENGTH_LONG).show();
        }
        else
        {
        try
        {
        //  Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_LONG).show();
          if(result.getContents().equals(""))
          {
            binding.disable.setVisibility(View.VISIBLE);
            binding.confirm.setVisibility(View.GONE);
          }
          else
          {
            binding.invoiceId.setText(""+result.getContents());
            binding.disable.setVisibility(View.GONE);
            binding.confirm.setVisibility(View.VISIBLE);

          }

        }
        catch (Exception e)
        {
          e.printStackTrace();
          binding.invoiceId.setText(""+result.getContents());
          //  Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_LONG).show();
        }
      }
    }
    else
      {
      super.onActivityResult(requestCode, resultCode, data);
    }

  }


}
