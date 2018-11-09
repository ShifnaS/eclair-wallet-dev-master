package fr.acinq.eclair.wallet.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScheduleDataList implements Serializable {

    @SerializedName("_id")
    public String _id;
    @SerializedName("service_name")
    public String  service_name;
    @SerializedName("immediate_cost")
    public Integer immediate_cost;
    @SerializedName("amount")
    public Integer   amount;
    @SerializedName("frequency")
    public Integer frequency;
    @SerializedName("payment_day")
    public Integer  payment_day;
    @SerializedName("qr_code")
    public String qr_code;
    @SerializedName("schedule_type")
    public Integer  schedule_type;
    @SerializedName("schedule_id")
    public String  schedule_id;
    @SerializedName("payment_month")
    public String  payment_month;

  public ScheduleDataList() {

  }

  public ScheduleDataList(String _id, String service_name, Integer immediate_cost, Integer amount, Integer frequency, Integer payment_day, String qr_code, Integer schedule_type, String schedule_id, String payment_month) {
    this._id = _id;
    this.service_name = service_name;
    this.immediate_cost = immediate_cost;
    this.amount = amount;
    this.frequency = frequency;
    this.payment_day = payment_day;
    this.qr_code = qr_code;
    this.schedule_type = schedule_type;
    this.schedule_id = schedule_id;
    this.payment_month = payment_month;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getService_name() {
    return service_name;
  }

  public void setService_name(String service_name) {
    this.service_name = service_name;
  }

  public Integer getImmediate_cost() {
    return immediate_cost;
  }

  public void setImmediate_cost(Integer immediate_cost) {
    this.immediate_cost = immediate_cost;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public Integer getFrequency() {
    return frequency;
  }

  public void setFrequency(Integer frequency) {
    this.frequency = frequency;
  }

  public Integer getPayment_day() {
    return payment_day;
  }

  public void setPayment_day(Integer payment_day) {
    this.payment_day = payment_day;
  }

  public String getQr_code() {
    return qr_code;
  }

  public void setQr_code(String qr_code) {
    this.qr_code = qr_code;
  }

  public Integer getSchedule_type() {
    return schedule_type;
  }

  public void setSchedule_type(Integer schedule_type) {
    this.schedule_type = schedule_type;
  }

  public String getSchedule_id() {
    return schedule_id;
  }

  public void setSchedule_id(String schedule_id) {
    this.schedule_id = schedule_id;
  }

  public String getPayment_month() {
    return payment_month;
  }

  public void setPayment_month(String payment_month) {
    this.payment_month = payment_month;
  }
}
