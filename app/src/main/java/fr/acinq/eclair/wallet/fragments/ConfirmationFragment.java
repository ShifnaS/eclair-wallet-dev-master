package fr.acinq.eclair.wallet.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;

import fr.acinq.eclair.wallet.R;

import fr.acinq.eclair.wallet.activities.FragmentCommunicator;
import fr.acinq.eclair.wallet.activities.HomeActivity;
import fr.acinq.eclair.wallet.activities.SendPaymentActivity;
import fr.acinq.eclair.wallet.databinding.FragmentConfirmationBinding;
import fr.acinq.eclair.wallet.presenter.ConfirmationPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationFragment extends Fragment implements FragmentCommunicator {

  public ConfirmationFragment() {
    // Required empty public constructor
  }
  String day="";
  String invoice_id="";
  SharedPreferences sp;
  SharedPreferences.Editor ed;
  int m=0;
  String payment_desc="";
  TextView bt_amount;
  Button bt_confirm;
  FragmentConfirmationBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding= DataBindingUtil.inflate(inflater,R.layout.fragment_confirmation, container, false);
    new HomeActivity(this);


    View root=binding.getRoot();
    sp=PreferenceManager.getDefaultSharedPreferences(getContext());
    ed=sp.edit();
    Bundle bundle = getArguments();
    String amount=bundle.getString("amount");
    String note=bundle.getString("notification");

    //Toast.makeText(getContext(), "Amount "+amount, Toast.LENGTH_SHORT).show();


    invoice_id=bundle.getString("invoice_id");
    day=bundle.getString("day");
    String month=bundle.getString("month");
    String frequency=bundle.getString("frequency");

    int dayy=Integer.parseInt(day);
    Calendar c = Calendar.getInstance();
    int dom = c.get(Calendar.DAY_OF_MONTH);
    if(dayy==dom)
    {
      if(frequency.equals("2"))
      {
        payment_desc="Your next Payment will be on "+month+" "+day;

      }
      else
      {
        m=c.get(Calendar.MONTH)+2;
        month=getMonthInString(m);
        payment_desc="Your next Payment will be on "+month+" "+day;

      }


    }

    else
    {
      payment_desc="Your next Payment will be on "+month+" "+day;

    }


  // Toast.makeText(getContext(), "day "+day+" month"+month, Toast.LENGTH_SHORT).show();
    bt_amount=root.findViewById(R.id.amount);
    bt_confirm=root.findViewById(R.id.confirm);

    bt_amount.setText("The amount due to be paid today is: "+amount+"BTC");




    binding.setConfirmationPresenter(new ConfirmationPresenter() {
      @Override
      public void confirm() {
        Toast.makeText(getContext(), "Please wait until transaction complete", Toast.LENGTH_LONG).show();
        bt_confirm.setEnabled(false);
        bt_amount.setText("The amount due to be paid today is: "+amount+"BTC \n Do not press Back Button...\nPlease wait until transaction complete");
        bt_confirm.setBackgroundColor(getResources().getColor(R.color.primary_light_x1));
      //  binding.confirm.setVisibility(View.GONE);
        Intent intent = new Intent(getContext(), SendPaymentActivity.class);
        intent.putExtra(SendPaymentActivity.EXTRA_INVOICE, "lightning:"+invoice_id);
        intent.putExtra(SendPaymentActivity.EXTRA_D, payment_desc+","+note);
       // intent.putExtra(SendPaymentActivity.EXTRA_NOTIFICATION, note);

        startActivity(intent);


      }

      @Override
      public void cancel() {

      }

      @Override
      public void back() {
        Fragment fragment = new LightningFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
        //  fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
      }
    });

    return root;
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onStop() {
    super.onStop();

  }


  @Override
  public void passData(String msg) {
    //Toast.makeText(getContext(), "hiiii "+msg, Toast.LENGTH_SHORT).show();
    if(msg.equals("success"))
    {
      Fragment fragment = new PaymentSuccessRegularFragment();
      FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
      //  fragmentTransaction.addToBackStack(null);
      //fragmentTransaction.commit();
      fragmentTransaction.commitAllowingStateLoss();

    }
    else
    {
      bt_amount.setText("Woops, something went wrong. There was an error making your payment. \n" +
        "Please try again and if the problem persists, please drop us note with what went wrong to \n" +
        "contact@lightningcollect.com.\n");
      binding.back.setVisibility(View.VISIBLE);
      binding.confirm.setVisibility(View.GONE);
    }

  }


  public String getMonthInString(int m)
  {
    String[]monthName={"January","February","March", "April", "May", "June", "July",
      "August", "September", "October", "November",
      "December"};

    return  monthName[m-1];
  }
}
