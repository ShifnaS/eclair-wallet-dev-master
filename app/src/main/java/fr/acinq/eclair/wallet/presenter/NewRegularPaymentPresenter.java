package fr.acinq.eclair.wallet.presenter;

import org.json.JSONException;

public interface NewRegularPaymentPresenter {
    void scan();
    void confirm() ;
    void onTextChanged(CharSequence s, int start, int before, int count);
}
