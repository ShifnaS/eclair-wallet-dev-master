package fr.acinq.eclair.wallet.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.activities.NotificationActivity;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
  JSONArray list;
  Context context;
  String service_name="";

  NotificationActivity.OnItemClick onItemClick;

  public NotificationAdapter(JSONArray list, Context context, NotificationActivity.OnItemClick onItemClick) {
    this.list = list;
    this.context = context;
    this.onItemClick=onItemClick;
  }

  @NonNull
  @Override
  public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_row_notification, parent, false);

    return new NotificationAdapter.MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
    try
    {
      JSONObject data=list.getJSONObject(position);
     // holder.service.setText(data.getString("service_name"));

      JSONObject service=data.getJSONObject("schedule_parent_id");

      if(service.has("note"))
      {
        service_name=service.getString("note");
      }
      else  if(service.has("service_name"))
      {
        service_name=service.getString("service_name");
      }

      double amt=Double.parseDouble(data.getString("regular_amount"));
      String amt_btc=BigDecimal.valueOf(amt).toPlainString();

      String payment_date=data.getString("payment_date");
      String date[]=payment_date.split("T");

      holder.service.setText(service_name);
      holder.date.setText(date[0]);
      holder.amount.setText(amt_btc);
      String invoice_id=data.getString("invoice_id");


      String day[]=date[0].split("-");
      int m=Integer.parseInt(day[1]);
      int month=getMonth(m);
      String monthString=getMonthInString(month);
      String pDate=monthString+" "+day[2];
      holder.bt_pay.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {


          onItemClick.onClick(invoice_id,pDate,service_name);
        }
      });
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
    public TextView service, date,amount;
    Button bt_pay;
    public MyViewHolder(View itemView) {
      super(itemView);
      service =  itemView.findViewById(R.id.service);
      date =  itemView.findViewById(R.id.date);
      amount =  itemView.findViewById(R.id.amount);
      bt_pay=itemView.findViewById(R.id.pay);

    }
  }

  public int getMonth(int m)
  {
    int month;
    if(m==12)
    {
      month=1;
    }
    else
    {
      month=m+1;
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
