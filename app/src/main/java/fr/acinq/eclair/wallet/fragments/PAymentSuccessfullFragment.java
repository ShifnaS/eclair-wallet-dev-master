package fr.acinq.eclair.wallet.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.activities.HomeActivity;
import fr.acinq.eclair.wallet.databinding.FragmentPaymentSuccessfullBinding;
import fr.acinq.eclair.wallet.presenter.PaymentSuccesfullPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PAymentSuccessfullFragment extends Fragment {


  public PAymentSuccessfullFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    FragmentPaymentSuccessfullBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_payment_successfull,container,false);

    View root=binding.getRoot();
    Bundle bundle = getArguments();

    String day=bundle.getString("day");
    String month=bundle.getString("month");
    TextView textView=root.findViewById(R.id.success);
    textView.setText("payment successfull  \n \n \n \n Your next Payment will be on month: "+month+" day:"+day);
    binding.setPaymentSuccesfullPresenter(new PaymentSuccesfullPresenter() {
      @Override
      public void ok() {

        Fragment fragment = new LightningFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(((ViewGroup)(getView().getParent())).getId(), fragment);
        //  fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

      }
    });
    return root;

  }

}
