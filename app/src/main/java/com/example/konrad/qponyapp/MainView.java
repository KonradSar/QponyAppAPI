package com.example.konrad.qponyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.konrad.qponyapp.GlobalVariables.currenciesRates;
import static com.example.konrad.qponyapp.GlobalVariables.datesBackInTheFuture;
import static com.example.konrad.qponyapp.GlobalVariables.finalListOfCurrenciesIndexesNbp;
import static com.example.konrad.qponyapp.GlobalVariables.listOfCurrenciesRatesFromHttpCall;

public class MainView extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    static String FULLAPIDATA = "CURRENCIESRATES";
    FloatingActionButton floatingActionButtonGoDown;
    FloatingActionButton floatingActionButtonGoUp;




    // Obsluga wyjscia z aplikacji
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainView.this);
        alertDialog.setTitle(R.string.quit);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.create();
        alertDialog.show();
        return;
    }
    // Ponizej proba obsluzenia obrotu ekranu w nadpisanych dwoch metodach onSave i onRestore
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FULLAPIDATA, new ArrayList<SingleCurrencyRate>(recyclerViewAdapter.getList()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currenciesRates = savedInstanceState.getParcelableArrayList(FULLAPIDATA);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        recyclerView = (RecyclerView) findViewById(R.id.ratesRecycler);
        floatingActionButtonGoDown = (FloatingActionButton) findViewById(R.id.floatingbtnGoDownAction);
        floatingActionButtonGoUp = (FloatingActionButton) findViewById(R.id.floatingbtnGoUpAction);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(currenciesRates, MainView.this);
        recyclerView.setAdapter(recyclerViewAdapter);
        adjustDataForAdapter();
        floatingActionButtonGoDown.setVisibility(View.VISIBLE);
        floatingActionButtonGoUp.setVisibility(View.GONE);
        // Obsluga szybkiego scrollowania w dol za pomoca przycisku FAB
        floatingActionButtonGoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        floatingActionButtonGoDown.setVisibility(View.GONE);
                        floatingActionButtonGoUp.setVisibility(View.VISIBLE);
                        recyclerView.scrollToPosition(recyclerViewAdapter.getItemCount() - 1);
                    }
                });
            }
        });
        // Obsluga szybkiego scrollowania w gore za pomoca przycisku FAB
        floatingActionButtonGoUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        floatingActionButtonGoDown.setVisibility(View.VISIBLE);
                        floatingActionButtonGoUp.setVisibility(View.GONE);
                        recyclerView.scrollToPosition(0);
                    }
                });
            }
        });


    }
    public void adjustDataForAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(currenciesRates, MainView.this);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    // W ponizszej metodzie pobieramy aktualna date, parsujemy do formatu long date a nastepnie odejmujemy od uzyskanej aktualnej daty kolejne
    // dni w kolejnych iteracjach petli for az do limitu czyli 999999999 razy. Po kazdej iteracji date z historii dodajemy do listy typu
    // String, ktora przechowuje gotowe daty z przeszlosci uzywane jako zmienne kolejno w zapytaniach Http typu GET
    public static void prepareListOfDatesFromThePast(int n) {
        for (int i = 0; i <= n; i++) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date updatedData = calendar.getTime();
            String uploadedDateToAdd = df.format(updatedData);
            datesBackInTheFuture.add(uploadedDateToAdd);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}