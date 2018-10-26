package com.example.konrad.qponyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

import static android.R.id.list;
import static com.example.konrad.qponyapp.GlobalVariables.currenciesRates;

/**
 * Created by Konrad on 23.10.2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {
    // Ponizej znajduja sie wszystkie szczegoly implementacyjne dotyczace Adaptera dla RecyclerView, ktory wyswietla dane w liscie

    private Context context1;
    private List<SingleCurrencyRate> currenciesRatesList;

    public  static List<SingleCurrencyRate> getList() {
        List<SingleCurrencyRate> listt = new ArrayList<>();
        listt.addAll(currenciesRates);
        return listt;
    }

    // Deklaracja layoutu definiujacego pojedynczy wiersz listy z kursami walut w relacji do EUR
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleline, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context1, R.string.greeting, Toast.LENGTH_SHORT).show();

            }
        });
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SingleCurrencyRate singleCurrencyRate = currenciesRatesList.get(position);

        if(singleCurrencyRate.isTheFirstElementOfDate){
            holder.currencyLabel.setText("- Kursy na dzień: " + singleCurrencyRate.getDateOfCurrencyRate() + ": ");
            holder.relativeLay.setBackgroundResource(R.color.blue);
        }
        if(!singleCurrencyRate.isTheFirstElementOfDate){

            // Ponizej zaokraglenie kursow walut do 2 miejsca po przecinku oraz zakolorowanie nazw poszczegolnych kursow oraz ich wartosci
            // w celu zwiekszenia kontrastu i czytelnosci listy
            double f = singleCurrencyRate.currencyRate;
            String rate = String.format(Locale.US, "%.2f", f);
            String currency = singleCurrencyRate.getCurrencyName();
            Spannable spannableString = new SpannableString("- " + currency + " : " + rate);
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#038d00")), 8, (8 + rate.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.currencyLabel.setText(spannableString);
            holder.relativeLay.setBackgroundResource(R.color.yellowish);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = new LinearLayout(context1);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imageView = new ImageView(context1);
                int width = 600;
                int height = 600;
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height, Gravity.CENTER_HORIZONTAL);
                parms.bottomMargin = 15;
                parms.topMargin = 30;
                parms.leftMargin = 10;
                parms.rightMargin = 10;
                imageView.setLayoutParams(parms);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context1);
                alertDialog.setView(linearLayout);
                alertDialog.setTitle("Selected Currency Rate");
                alertDialog.setMessage("Selected Currency: " + "'" + singleCurrencyRate.getCurrencyName()+ "'" +"\n"+ "Currency Rate: " + singleCurrencyRate.getCurrencyRate() + " EUR"
                        +"\n"+ "Release Date: " + singleCurrencyRate.getDateOfCurrencyRate());
                alertDialog.create();
                alertDialog.show();

            }
        });
    }



    // Zwracamy rozmiar listy, chyba ze jest nullem to obslugujemy wyjatek blokiem try, catch
    @Override
    public int getItemCount() {
        int size = 0;
        try {
            size = currenciesRatesList.size();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context1, R.string.connection_problem, Toast.LENGTH_LONG).show();
        }
        return size;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView currencyLabel;
        public RelativeLayout relativeLay;

        public MyViewHolder(View itemView) {
            super(itemView);
            currencyLabel = (TextView) itemView.findViewById(R.id.singleCurrencyLabel);
            relativeLay = (RelativeLayout) itemView.findViewById(R.id.singleRecyclerViewLine);

        }

        @Override
        public void onClick(View v) {
        }
    }
    public RecyclerViewAdapter(List<SingleCurrencyRate> currenciesRates, Context context) {
        this.currenciesRatesList = currenciesRates;
        this.context1 = context;
    }
}
