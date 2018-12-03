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

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.activities.HomeActivity;
import fr.acinq.eclair.wallet.databinding.FragmentPaymentSuccessRegularBinding;
import fr.acinq.eclair.wallet.presenter.PaymentSuccess;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentSuccessRegularFragment extends Fragment {


    public PaymentSuccessRegularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPaymentSuccessRegularBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_payment_success_regular, container, false);
        View root=binding.getRoot();

        Bundle bundle = getArguments();
        if(bundle==null)
        {
          binding.success.setText("payment successfull \n \n \n \n Thank You For using Lightning Collect");
        }
        else
        {
          String date=bundle.getString("date");
          binding.success.setText("Your regular payment has been set up.\n \nYour first payment is scheduled for "+date+".\n \n \n \n Thank you for using Lightning Collect");
        }



        binding.setPaymentSuccess(new PaymentSuccess() {
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
