package fr.acinq.eclair.wallet.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fr.acinq.eclair.CoinUnit;
import fr.acinq.eclair.wallet.R;

import fr.acinq.eclair.wallet.activities.NotificationActivity;
import fr.acinq.eclair.wallet.databinding.FragmentLightningBinding;
import fr.acinq.eclair.wallet.presenter.LightningPresenter;
import fr.acinq.eclair.wallet.utils.WalletUtils;


public class LightningFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match



    public LightningFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentLightningBinding binding= DataBindingUtil.inflate(inflater,
                R.layout.fragment_lightning, container, false);
        View root = binding.getRoot();
        binding.setLightningPresenter(new LightningPresenter() {
            @Override
            public void regularPayment() {
                String tag="Lightning";
                Fragment fragment = new NewRegularPaymentFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment,tag);
                fragmentTransaction.addToBackStack(tag);
                fragmentTransaction.commit();
            }

            @Override
            public void manageregularPayment() {
              String tag="Manage";
              Fragment fragment = new ManageRegularPamentFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment,tag);
                fragmentTransaction.addToBackStack(tag);
                fragmentTransaction.commit();
            }

          @Override
          public void notifications() {
            Intent i=new Intent(getContext(),NotificationActivity.class);
            startActivity(i);
            getActivity().finish();
          }
        });
        return root;
    }
  public void refreshList() {
   /* final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
    final CoinUnit prefUnit = WalletUtils.getPreferredCoinUnit(prefs);
    final String fiatCode = WalletUtils.getPreferredFiat(prefs);
    final boolean displayBalanceAsFiat = WalletUtils.shouldDisplayInFiat(prefs);*/
  }

}
