package com.example.konrad.qponyapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;
import static android.R.attr.x;
import static com.example.konrad.qponyapp.GlobalVariables.currenciesRates;
import static com.example.konrad.qponyapp.GlobalVariables.datesBackInTheFuture;
import static com.example.konrad.qponyapp.GlobalVariables.finalListOfCurrenciesIndexesNbp;

import static com.example.konrad.qponyapp.GlobalVariables.finalSortedListOfCurrenciesIndexesNbp;
import static com.example.konrad.qponyapp.GlobalVariables.listOfCurrenciesRatesFromHttpCall;

/**
 * Created by Konrad on 23.10.2018.
 */

// Ponizej uzywajac AsyncTaskow pobieramy dane zapytaniem GET i zapisujemy dane do listy walut wyswietlanej w recyclerze

public class RestAPI extends AsyncTask<Object, Object, List<SingleCurrencyRate>> {
    private static final String GET = "GET";

    @Override
    protected List<SingleCurrencyRate> doInBackground(Object[] params) {
        URL url = null;
        String newKey = "5c57fbb0c824b418b952ccc1bbaa7605";
        String oldKey = "df8c2c77195bbc49403ed690ee219b3e";
        try {
            for (String date : datesBackInTheFuture) {
                url = new URL("http://data.fixer.io/api/" + date + "?access_key=" + newKey);
                //rzutujemy url connection na HTTP
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                //wybiramy metodę żądania HTTP
                http.setRequestMethod(GET);
                //wysyłamy żądanie
                http.connect();
                //sprawdzamy odpowiedź
                int responseCode = http.getResponseCode();
                if (responseCode == 200) { //jeżeli ok TO:
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(http.getInputStream()));
                    String line = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONObject rates = (JSONObject) jsonObject.get("rates");
//                    List<SingleCurrencyRate> listOfCurrenciesRates = new ArrayList<>();
                    // Ladujemy do listy walut dodatkowy rekord stanowiacy element listy, ktory bedzie informowac o dacie
                    // aktualizacji wyswietlanych wynikow ponizej dla danej daty. Dodatkowo w celu wyroznienia kazdego
                    // pierwszego wiersza porcji kursow walut z danego dnia klasa przenosna bedzie oferowac pole wartosci boolean
                    // ktorw zadecyduje o backgroundzie wybranego elementu w RecyclerView w celu wyroznienia kolorem
                    listOfCurrenciesRatesFromHttpCall.add(new SingleCurrencyRate(jsonObject.get("date").toString(), true));
                    for(SingleCurrencyLabel element: finalSortedListOfCurrenciesIndexesNbp){
                        listOfCurrenciesRatesFromHttpCall.add(new SingleCurrencyRate(element.getLabelOfSelctedCurrency(), rates.getDouble(element.getLabelOfSelctedCurrency()), jsonObject.get("date").toString(), false));
                    }
                }
            }
            return listOfCurrenciesRatesFromHttpCall;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}