package fr.acinq.eclair.wallet.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.adapters.ManageRegularPaymentAdapter;
import fr.acinq.eclair.wallet.api.SingletonRequestQueue;
import fr.acinq.eclair.wallet.databinding.FragmentManageRegularPamentBinding;
import fr.acinq.eclair.wallet.events.RecyclerTouchListener;

import fr.acinq.eclair.wallet.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageRegularPamentFragment extends Fragment {

  RecyclerView recyclerView;
  private JSONArray list;
  private ManageRegularPaymentAdapter mAdapter;
    public ManageRegularPamentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentManageRegularPamentBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_manage_regular_pament,container,false);
        View root=binding.getRoot();


      // GET DEVICE ID
      final String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
      recyclerView=root.findViewById(R.id.recyclerview);


      RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
      recyclerView.setLayoutManager(mLayoutManager);
      recyclerView.setItemAnimator(new DefaultItemAnimator());
      recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));




      VolleyLog.DEBUG = true;
      RequestQueue queue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();

      final String url = String.format(String.format(Constants.URL_ManageRegular+deviceId));
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
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                  @Override
                  public void onClick(View view, int position) {

                   try
                   {
                     JSONObject jo=list.getJSONObject(position);

                     Fragment fragment = new InviceScheduleDetailsFragment();
                     Bundle bundle = new Bundle();
                     bundle.putString("schedule_id", jo.getString("schedule_id"));
                     bundle.putInt("status", jo.getInt("status"));
                     fragment.setArguments(bundle);
                     FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();
                   }
                   catch (Exception e)
                   {
                     e.printStackTrace();
                   }
                  }

                  @Override
                  public void onLongClick(View view, int position) {

                  }
                }));

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




        return  root;
    }

}
