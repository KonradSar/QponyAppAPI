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

import static com.example.konrad.qponyapp.GlobalVariables.listOfCurrenciesLabelsFromApiNBP;

/**
 * Created by Konrad on 24.10.2018.
 */

public class RestAPICurrenciesLabels extends AsyncTask<Void, Void, List<SingleCurrencyLabel>> {
    private static final String CODE = "code";
    private static final String GET = "GET";
    @Override
    protected List<SingleCurrencyLabel> doInBackground(Void... params) {
        try {
            //tabele, ktore rozpatrujemy ponizej
            String[] tables = {"a", "b"};
            //iterujemy przez tablice tabel a,b,c
            List<String> result = new ArrayList<>();
            for (String table : tables) {
                URL url = null;
                url = new URL("http://api.nbp.pl/api/exchangerates/tables/" + table + "/?format=json");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(GET);
                http.connect();
                int responseCode = http.getResponseCode();
                if (responseCode == 200) { //jeżeli ok TO:
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(http.getInputStream()));
                    String line = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        JSONArray ratesArray = (JSONArray) obj.get("rates");
                        List<SingleCurrencyLabel> listOfCurrenciesLabels = new ArrayList<>();
                        for (int x = 0; x < ratesArray.length(); x++) {
                            JSONObject o = (JSONObject) ratesArray.get(x);
                            listOfCurrenciesLabels.add(x, new SingleCurrencyLabel(o.get(CODE).toString()));
                            listOfCurrenciesLabelsFromApiNBP.add(new SingleCurrencyLabel(o.get(CODE).toString()));
                        }
                    }
                } else {
                    System.out.println("tabla " + table + " nie obsluzona");
                }

            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfCurrenciesLabelsFromApiNBP;

    }
}
