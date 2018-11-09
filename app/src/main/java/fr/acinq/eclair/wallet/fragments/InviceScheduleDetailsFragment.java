package fr.acinq.eclair.wallet.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.activities.HomeActivity;
import fr.acinq.eclair.wallet.adapters.ManageRegularPaymentAdapter;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.databinding.FragmentInviceScheduleDetailsBinding;
import fr.acinq.eclair.wallet.events.RecyclerTouchListener;
import fr.acinq.eclair.wallet.presenter.InvoiceSchedulePresenter;
import fr.acinq.eclair.wallet.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class InviceScheduleDetailsFragment extends Fragment {

    TextView tv_schedule_no;
    RecyclerView recyclerView;
    private JSONArray list;
    private ManageRegularPaymentAdapter mAdapter;
    String schedule_id="";
    public InviceScheduleDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentInviceScheduleDetailsBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_invice_schedule_details,container,false);
        View root=binding.getRoot();

        Bundle bundle = getArguments();
        schedule_id=bundle.getString("schedule_id");
        tv_schedule_no=root.findViewById(R.id.schedule_no);
        tv_schedule_no.setText("Invoice Schedule: "+schedule_id);


        recyclerView=root.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));




      VolleyLog.DEBUG = true;
      RequestQueue queue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();

      final String url = String.format(String.format(Constants.URL_GETSCHEDULE_DETAILS+schedule_id));
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
                mAdapter = new ManageRegularPaymentAdapter(jo.getJSONArray("message"),getContext());
                recyclerView.setAdapter(mAdapter);


              }
              else
              {
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
              }
            }
            else
            {
              Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();

            }

          } catch (JSONException e)
          {
            e.printStackTrace();
          }


        },
        error -> Log.e("Error.Response", "************************"+error.getMessage())
      );
      queue.add(getRequest);


      binding.setInvoiceSchedulePresenter(new InvoiceSchedulePresenter() {
            @Override
            public void cancel() {

              VolleyLog.DEBUG = true;
              RequestQueue queue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();

              final String url = String.format(String.format(Constants.URL_CANCEL_PAYMENT+schedule_id));
              JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                jo -> {
                  //Log.e("Response", "*******************"+jo.toString());
                  // jo=res;
                  try
                  {
                    String status = jo.getString("status");
                    boolean error = jo.getBoolean("error");
                    if (!error)
                    {
                      if (status.equalsIgnoreCase("success"))
                      {
                         Toast.makeText(getContext(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                        Fragment fragment = new LightningFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                      //  fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                      }
                      else
                      {
                        Toast.makeText(getContext(), jo.getString("message"), Toast.LENGTH_SHORT).show();

                      }
                    }
                    else
                    {
                      Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();

                    }

                  } catch (JSONException e)
                  {
                    e.printStackTrace();
                  }


                },
                error -> Log.e("Error.Response", "************************"+error.getMessage())
              );
              queue.add(getRequest);



               /* Intent i=new Intent(getContext(), HomeActivity.class);
                startActivity(i);*/
            }
        });
        return root;
    }

}
