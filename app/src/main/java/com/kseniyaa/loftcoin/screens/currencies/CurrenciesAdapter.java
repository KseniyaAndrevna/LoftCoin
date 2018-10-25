package com.kseniyaa.loftcoin.screens.currencies;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kseniyaa.loftcoin.R;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.model.Currency;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.CurrencyViewHolder> {

    private List<CoinEntyti> coins = Collections.emptyList();

    private CurrencyAdapterListener listener;

    public void setCoins(List<CoinEntyti> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    public void setListener(CurrencyAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        holder.bind(coins.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.symbol_text_curr)
        TextView symbolText;
        @BindView(R.id.icon_symbol_curr)
        ImageView symbolIcon;
        @BindView(R.id.currency_name_curr)
        TextView name;

        CurrencyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(CoinEntyti coin, CurrencyAdapterListener listener) {
            bindIcon(coin);
            bindName(coin);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCurrencyClick(coin);
                }
            });
        }

        private void bindIcon(CoinEntyti coin) {
            Currency currency = Currency.getCurrency(coin.symbol);

            if (currency != null) {
                symbolIcon.setVisibility(View.VISIBLE);
                symbolText.setVisibility(View.GONE);

                symbolIcon.setImageResource(currency.iconRes);
            } else {
                symbolIcon.setVisibility(View.GONE);
                symbolText.setVisibility(View.VISIBLE);

                symbolText.setText(String.valueOf(coin.symbol.charAt(0)));
            }
        }

        @SuppressLint("StringFormatInvalid")
        private void bindName(CoinEntyti coin) {
            name.setText(itemView.getContext().getString(R.string.currency_b_sheet_curr_name, coin.name, coin.symbol));
        }
    }
}























