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
  String day="",month;
  String invoice_id="";
  SharedPreferences sp;
  SharedPreferences.Editor ed;



  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    FragmentConfirmationBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_confirmation, container, false);
    new HomeActivity(this);


    View root=binding.getRoot();
    sp=PreferenceManager.getDefaultSharedPreferences(getContext());
    ed=sp.edit();
    Bundle bundle = getArguments();
    String amount=bundle.getString("amount");
    invoice_id=bundle.getString("invoice_id");
    day=bundle.getString("day");
    String month=bundle.getString("month");

  // Toast.makeText(getContext(), "day "+day+" month"+month, Toast.LENGTH_SHORT).show();
    TextView bt_amount=root.findViewById(R.id.amount);
    Button bt_confirm=root.findViewById(R.id.confirm);

    bt_amount.setText("The amount due to be paid today is: "+amount+"BTC");




    binding.setConfirmationPresenter(new ConfirmationPresenter() {
      @Override
      public void confirm() {
        Toast.makeText(getContext(), "Please wait until transaction complete", Toast.LENGTH_LONG).show();
        String payment_desc="Your next Payment will be on month: "+month+" day:"+day;
        bt_confirm.setEnabled(false);
        bt_amount.setText("The amount due to be paid today is: "+amount+"BTC \n Do not press Back Button...\nPlease wait until transaction complete");
        bt_confirm.setBackgroundColor(getResources().getColor(R.color.primary_light_x1));
        Intent intent = new Intent(getContext(), SendPaymentActivity.class);
        intent.putExtra(SendPaymentActivity.EXTRA_INVOICE, "lightning:"+invoice_id);
        intent.putExtra(SendPaymentActivity.EXTRA_D, payment_desc);
        startActivity(intent);


      }

      @Override
      public void cancel() {

      }

      @Override
      public void back() {

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
    Fragment fragment = new PaymentSuccessRegularFragment();
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
    //  fragmentTransaction.addToBackStack(null);
    //fragmentTransaction.commit();
    fragmentTransaction.commitAllowingStateLoss();

  }
}
