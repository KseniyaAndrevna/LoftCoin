package com.kseniyaa.loftcoin.screens.main.rate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kseniyaa.loftcoin.R;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.QuoteEntity;
import com.kseniyaa.loftcoin.data.model.Currency;
import com.kseniyaa.loftcoin.data.model.Fiat;
import com.kseniyaa.loftcoin.data.prefs.Prefs;
import com.kseniyaa.loftcoin.utils.CurrencyFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {

    private List<CoinEntyti> coins = Collections.emptyList();
    private Listener listener = null;


    private Prefs prefs;

    RateAdapter(Prefs prefs) {
        this.prefs = prefs;
    }

    public void setCoins(List<CoinEntyti> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate, parent, false);
        return new RateViewHolder(view, prefs);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        holder.bind(coins.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    @Override
    public long getItemId(int position) {
        return coins.get(position).id;
    }

    static class RateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.symbol_text)
        TextView symbolText;

        @BindView(R.id.icon_symbol)
        ImageView symbolIcon;

        @BindView(R.id.currency_name)
        TextView name;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.percent_change)
        TextView changePercent;

        private Random random = new Random();
        private Context context;
        private Prefs prefs;

        private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

        private static int[] colors = {
                0xFFF5FF30,
                0xFFFFFFFF,
                0xFF2ABDF5,
                0xFFFF7416,
                0xFF534FFF,
        };


        RateViewHolder(View itemView, Prefs prefs) {
            super(itemView);

            context = itemView.getContext();
            this.prefs = prefs;

            ButterKnife.bind(this, itemView);
        }

        void bind(CoinEntyti coin, int position, Listener listener) {
            bindIcon(coin);
            bindSymbol(coin);
            bindPrice(coin);
            bindBackground(position);
            bindPercentChange(coin);
            bindListener(coin, listener);
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
            }

        }

        private void bindSymbol(CoinEntyti coin) {
            name.setText(coin.symbol);
        }

        private void bindPrice(CoinEntyti coin) {
            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quote = coin.getQuote(fiat);
            String value = currencyFormatter.format(quote.price, false);

            price.setText(context.getString(R.string.currency_amount, value, fiat.symbol));
        }

        private void bindBackground(int position) {
            if (position % 2 == 0) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.item_bg_event));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.item_bg_odd));
            }
        }

        private void bindPercentChange(CoinEntyti coin) {
            QuoteEntity quote = coin.getQuote(prefs.getFiatCurrency());
            float percentChangeValue = quote.percentChange24h;

            //changePercent.setText(context.getString(R.string.rate_item_percent_change, percentChangeValue));
            if (percentChangeValue >= 0) {
                changePercent.setText(context.getString(R.string.rate_item_positive_percent_change, percentChangeValue));
                changePercent.setTextColor(context.getResources().getColor(R.color.percent_up));
            } else {
                changePercent.setText(context.getString(R.string.rate_item_percent_change, percentChangeValue));
                changePercent.setTextColor(context.getResources().getColor(R.color.percent_down));
            }

            Drawable background = symbolText.getBackground();
            Drawable wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);

            symbolText.setText(String.valueOf(coin.slug.charAt(0)));
        }

        private void bindListener(CoinEntyti coin, Listener listener) {
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onRateLongClick(coin.symbol);
                }
                return true;
            });
        }
    }

    interface Listener {
        void onRateLongClick(String symbol);
    }
}
