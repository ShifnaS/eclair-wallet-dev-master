package fr.acinq.eclair.wallet.presenter;

public interface SummaryPurchasePresenter {
    void confirm();
    void cancel();
    void onValChange(int old,int newval);

}
