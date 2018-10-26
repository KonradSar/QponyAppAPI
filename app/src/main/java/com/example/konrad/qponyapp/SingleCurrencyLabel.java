package com.example.konrad.qponyapp;

/**
 * Created by Konrad on 24.10.2018.
 */

public class SingleCurrencyLabel {
    public String labelOfSelctedCurrency;

    public SingleCurrencyLabel(String labelOfSelctedCurrency) {
        this.labelOfSelctedCurrency = labelOfSelctedCurrency;
    }

    public String getLabelOfSelctedCurrency() {
        return labelOfSelctedCurrency;
    }

    public void setLabelOfSelctedCurrency(String labelOfSelctedCurrency) {
        this.labelOfSelctedCurrency = labelOfSelctedCurrency;
    }
}
