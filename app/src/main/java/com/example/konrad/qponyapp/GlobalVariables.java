package com.example.konrad.qponyapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 23.10.2018.
 */

public class GlobalVariables {
    public static List<SingleCurrencyLabel> listOfCurrenciesLabelsFromApiNBP = new ArrayList<>();
    public static List<SingleCurrencyLabel> finalListOfCurrenciesIndexesNbp = new ArrayList<>();
    public static List<SingleCurrencyLabel> finalSortedListOfCurrenciesIndexesNbp = new ArrayList<>();
    public static List<SingleCurrencyRate> listOfCurrenciesRatesFromHttpCall = new ArrayList<>();
    public static List<SingleCurrencyRate> currenciesRates = new ArrayList<>();
    public static List<String> datesBackInTheFuture = new ArrayList<>();

}
