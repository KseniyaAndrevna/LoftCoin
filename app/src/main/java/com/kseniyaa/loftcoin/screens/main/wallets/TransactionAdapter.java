package com.kseniyaa.loftcoin.screens.main.wallets;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kseniyaa.loftcoin.R;
import com.kseniyaa.loftcoin.data.db.model.QuoteEntity;
import com.kseniyaa.loftcoin.data.db.model.Transaction;
import com.kseniyaa.loftcoin.data.model.Fiat;
import com.kseniyaa.loftcoin.data.prefs.Prefs;
import com.kseniyaa.loftcoin.utils.CurrencyFormatter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private static final String TAG = "TransactionAdapter";

    private Prefs prefs;

    private List<Transaction> transactions = Collections.emptyList();

    public TransactionAdapter(Prefs prefs) {
        this.prefs = prefs;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view, prefs);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bind(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.transaction_icon)
        ImageView icon;

        @BindView(R.id.transaction_crypto_amount)
        TextView cryptoAmount;

        @BindView(R.id.transaction_fiat_amount)
        TextView fiatAmount;

        @BindView(R.id.transaction_date)
        TextView date;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

        private Prefs prefs;

        TransactionViewHolder(@NonNull View itemView, Prefs prefs) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.prefs = prefs;
        }

        public void bind(Transaction transaction) {
            bindIcon(transaction);
            bindCryptoAmount(transaction);
            bindFiatAmount(transaction);
            bindDate(transaction);
        }

        private void bindIcon(Transaction transaction) {
            if (transaction.amount < 0) {
                icon.setImageResource(R.drawable.ic_transaction_expense);
            } else {
                icon.setImageResource(R.drawable.ic_transaction_incomes);
            }
        }

        private void bindFiatAmount(Transaction transaction) {

            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quote = transaction.coin.getQuote(fiat);

            if (transaction.amount < 0) {
                fiatAmount.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.percent_down));
                double amount = Math.abs(transaction.amount) * quote.price;
                String value = "- " + currencyFormatter.format(amount,false);
                fiatAmount.setText(itemView.getContext().getString(R.string.currency_amount,value,fiat.symbol));
            } else {
                fiatAmount.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.percent_up));
                double amount = Math.abs(transaction.amount) * quote.price;
                String value = "+ " + currencyFormatter.format(amount,false);
                fiatAmount.setText(itemView.getContext().getString(R.string.currency_amount,value,fiat.symbol));
            }
        }

        private void bindCryptoAmount(Transaction transaction) {
            if (transaction.amount < 0) {
                String value = "- " + currencyFormatter.format(Math.abs(transaction.amount), true);
                cryptoAmount.setText(itemView.getContext().getString(R.string.currency_amount,value,transaction.coin.symbol));
            } else {
                String value = "+ " + currencyFormatter.format(Math.abs(transaction.amount), true);
                cryptoAmount.setText(itemView.getContext().getString(R.string.currency_amount,value,transaction.coin.symbol));
            }
        }

        private void bindDate(Transaction transaction) {
            Date date = new Date( transaction.date);
            this.date.setText(dateFormat.format(date));
        }
    }
}
