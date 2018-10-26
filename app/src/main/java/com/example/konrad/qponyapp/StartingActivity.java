package com.example.konrad.qponyapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.attr.button;
import static com.example.konrad.qponyapp.GlobalVariables.currenciesRates;
import static com.example.konrad.qponyapp.GlobalVariables.finalListOfCurrenciesIndexesNbp;
import static com.example.konrad.qponyapp.GlobalVariables.finalSortedListOfCurrenciesIndexesNbp;


public class StartingActivity extends AppCompatActivity {
    Button startButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        startButton = (Button) findViewById(R.id.startBtn);
        textView = (TextView) findViewById(R.id.greetingTextView);

        // Ponizej animacja text view w Activity startowym , z ktorego przechodzimy do wlasciwej listy
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(200);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textView.startAnimation(anim);
        showUsersInformation();

        startButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Na poczatek cofamy sie o 100 dni wstecz
                MainView.prepareListOfDatesFromThePast(100);
                if(isNetworkAvailable()){
                    prepareCurrenciesLabelsFromApiNBP();
                    getCurrenciesRates();
                    Intent intent = new Intent(StartingActivity.this, MainView.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void getCurrenciesRates() {
        // Ponizej uzywajac API fixer.io pobieram liste z kursami walut w stosunku do EUR i wyswietlam w RecyclerView
        // w kolejnym Activity
        AsyncTask<Object, Object, List<SingleCurrencyRate>> executeSecond = new RestAPI().execute();
        try {
            currenciesRates = executeSecond.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void prepareCurrenciesLabelsFromApiNBP() {
        // Zamiast wpisywac ""z palca" wszystkie rekordy dotyczace nazw walut uzyje innego API NBP w celu pobrania
        // ponad 160 oficjalnych labelek kursow walut swiata i na podstawie takiej listy przeiteruje po kolejnym API
        // z kursami walut w stosunku do EUR czyli https://fixer.io/quickstart,
        AsyncTask<Void, Void, List<SingleCurrencyLabel>> execute = new RestAPICurrenciesLabels().execute();
        try {
            GlobalVariables.finalListOfCurrenciesIndexesNbp = execute.get();
            // Ponizej usuwamy elementy, ktorych nie ma na liscie walut z https://fixer.io/quickstart, oraz dodajemy brakujace pozycje
            deleteAndAddAdditionalLabelsToFinalListNBP();
            // Ponizej sortujemy liste, ktora uzyjemy ponizej w kolejnym AsyncTask do pobrania konkretnych wartosci dla walut o nazwach z posortowanej ponizszej listy
            sortListOfFinalListNBP();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void showUsersInformation() {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        int width = 600;
        int height = 600;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height, Gravity.CENTER_HORIZONTAL);
        parms.bottomMargin = 15;
        parms.topMargin = 30;
        parms.leftMargin = 10;
        parms.rightMargin = 10;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StartingActivity.this);
        alertDialog.setView(linearLayout);
        alertDialog.setTitle(R.string.warning);
        alertDialog.setMessage(R.string.details);
        alertDialog.create();
        alertDialog.show();
    }

    private void sortListOfFinalListNBP() {
        List<String> unsortedStringList = new ArrayList<String>();
        for(SingleCurrencyLabel string: finalListOfCurrenciesIndexesNbp){
            unsortedStringList.add(string.getLabelOfSelctedCurrency());
        }
        Collections.sort(unsortedStringList);
        for(String sortedStringElement: unsortedStringList){
            finalSortedListOfCurrenciesIndexesNbp.add(new SingleCurrencyLabel(sortedStringElement));
        }
        finalSortedListOfCurrenciesIndexesNbp.remove(0);
    }

    private void deleteAndAddAdditionalLabelsToFinalListNBP() {
        int[] elementsCountToRemove = {0, 38, 54, 83, 108};
        for(int element: elementsCountToRemove){
            finalListOfCurrenciesIndexesNbp.remove(element);
        }
        // Do listy dodajemy kilkanascie dodatkowych brakujacych w API NBP walut lecz nie wymaga to pisania setek linii kodu
        String[] elementsToAdd = {"AED", "BMD", "BTC", "BTN", "BYR", "CLF", "CUC", "FKP", "GGP", "IMP", "JEP", "KPW", "KYD", "LTL",
                "LVL", "MRO", "PLN", "SHP", "STD", "VEF", "XAG", "XAU", "ZMK", "ZWL"};
        for(String element: elementsToAdd){
            finalListOfCurrenciesIndexesNbp.add(new SingleCurrencyLabel(element));
        }
    }

    // Metoda sprawdzajaca czy mamy dostep do internetu. Pamietam o dodaniu pozwolenia w Manifest dotyczacego NetworkState
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
