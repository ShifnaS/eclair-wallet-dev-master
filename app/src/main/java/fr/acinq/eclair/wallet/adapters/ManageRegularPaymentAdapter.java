package fr.acinq.eclair.wallet.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.acinq.eclair.wallet.R;

public class ManageRegularPaymentAdapter extends RecyclerView.Adapter<ManageRegularPaymentAdapter.MyViewHolder> {
  JSONArray list;
  Context context;

  public ManageRegularPaymentAdapter(JSONArray list, Context context) {
    this.list = list;
    this.context = context;
  }

  @NonNull
  @Override
  public ManageRegularPaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.manage_regular_list, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ManageRegularPaymentAdapter.MyViewHolder holder, int position) {
    try
    {

        JSONObject data=list.getJSONObject(position);

      /*"_id": "5be037160537cc276c91606f",
      "regular_amount": 0.002,
      "payment_date": "2018-11-07T00:00:00.000Z"*/
        if(data.has("regular_amount")&&data.has("payment_date"))
        {
          holder.scedule_id.setText(data.getString("regular_amount"));
          holder.status.setText(data.getString("payment_date"));
        }
        else
        {
          String status="";
          int flag=data.getInt("payment_status");
          if(flag==1)
          {
            status="Yes";
          }
          else
          {
            status="No";
          }
          holder.scedule_id.setText(data.getString("schedule_id"));
          holder.status.setText(status);
        }

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  @Override
  public int getItemCount() {
    return list.length();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView scedule_id, status;
    public MyViewHolder(View itemView) {
      super(itemView);
      scedule_id =  itemView.findViewById(R.id.schedule_id);
      status =  itemView.findViewById(R.id.status);

    }
  }
}
