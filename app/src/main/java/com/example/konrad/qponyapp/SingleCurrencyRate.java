package com.example.konrad.qponyapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Konrad on 23.10.2018.
 */

public class SingleCurrencyRate implements Parcelable {
    public String currencyName;
    public double currencyRate;
    public String dateOfCurrencyRate;
    public boolean isTheFirstElementOfDate;

    public SingleCurrencyRate(String currencyName, double currencyRate, String dateOfCurrencyRate, boolean isTheFirstElementOfDate) {
        this.currencyName = currencyName;
        this.currencyRate = currencyRate;
        this.dateOfCurrencyRate = dateOfCurrencyRate;
        this.isTheFirstElementOfDate = isTheFirstElementOfDate;
    }

    // konstruktor dzieki, ktoremu dodajemy dodatkowy wiersz okreslajacy date wyswietlanych wynikow ponizej dla danej daty
    public SingleCurrencyRate(String dateOfCurrencyRate, boolean isTheFirstElementOfDate) {
        this.dateOfCurrencyRate = dateOfCurrencyRate;
        this.isTheFirstElementOfDate = isTheFirstElementOfDate;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getDateOfCurrencyRate() {
        return dateOfCurrencyRate;
    }

    public void setDateOfCurrencyRate(String dateOfCurrencyRate) {
        this.dateOfCurrencyRate = dateOfCurrencyRate;
    }

    public boolean isTheFirstElementOfDate() {
        return isTheFirstElementOfDate;
    }

    public void setTheFirstElementOfDate(boolean theFirstElementOfDate) {
        isTheFirstElementOfDate = theFirstElementOfDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
