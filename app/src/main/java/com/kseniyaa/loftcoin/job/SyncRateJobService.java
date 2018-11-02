package com.kseniyaa.loftcoin.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kseniyaa.loftcoin.App;
import com.kseniyaa.loftcoin.R;
import com.kseniyaa.loftcoin.data.api.Api;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntityMapper;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.QuoteEntity;
import com.kseniyaa.loftcoin.data.model.Fiat;
import com.kseniyaa.loftcoin.data.prefs.Prefs;
import com.kseniyaa.loftcoin.screens.main.MainActivity;
import com.kseniyaa.loftcoin.utils.CurrencyFormatter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SyncRateJobService extends JobService {

    public static final String TAG = "SyncRateJobService";

    public static final String EXTRA_SYMBOL = "symbol";

    private static final String NOTIFICATION_CHANNEL_RATE_CHANGED = "RATE_CHANGED";
    private static final int NOTIFICATION_ID_RATE_CHANGED = 10;

    private Api api;
    private Database database;
    private Prefs prefs;
    private CoinEntityMapper mapper;
    private CurrencyFormatter formatter;

    private Disposable disposable;

    private String symbol = "BTC";

    @Override
    public void onCreate() {
        super.onCreate();

        api = ((App) getApplication()).getApi();
        database = ((App) getApplication()).getDatabase();
        prefs = ((App) getApplication()).getPrefs();

        mapper = new CoinEntityMapper();
        formatter = new CurrencyFormatter();

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob");
        doJob(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        Log.i(TAG, "onStopJob");
        return false;
    }

    private void doJob(JobParameters params) {
        symbol = params.getExtras().getString(EXTRA_SYMBOL, "BTC");

        disposable = api.ticker(prefs.getFiatCurrency().name(), "array")
                .subscribeOn(Schedulers.io())
                .map(response -> mapper.mapCoins(response.data))
                .subscribe(
                        coinEntytis -> {
                            handleCoin(coinEntytis);
                            jobFinished(params, false);
                        },

                        error -> System.out.println(TAG + "Error!!"+ error)

                );
    }

    private void handleCoin(List<CoinEntyti> newCoins) {

        database.open();

        Fiat fiat = prefs.getFiatCurrency();

        CoinEntyti oldCoin = database.getCoin(symbol);
        CoinEntyti newCoin = findCoin(newCoins, symbol);

        if (oldCoin != null && newCoin != null) {
            QuoteEntity oldQuote = oldCoin.getQuote(fiat);
            QuoteEntity newQuote = newCoin.getQuote(fiat);

            if (newQuote.price != oldQuote.price + 1) {
                double priceDiff = newQuote.price - oldQuote.price;

                String priceDiffString;
                String price = formatter.format(Math.abs(priceDiff), false);

                if (priceDiff > 0) {
                    priceDiffString = "+ " + price + " " + fiat.symbol;
                } else {
                    priceDiffString = "- " + price + " " + fiat.symbol;
                }

                showRateChangeNotification(newCoin, priceDiffString);
            }
        }

        database.saveCoins(newCoins);
        database.close();
    }


    private CoinEntyti findCoin(List<CoinEntyti> newCoins, String symbol) {
        for (CoinEntyti coin : newCoins) {
            if (coin.symbol.equals(symbol)) {
                return coin;
            }
        }
        return null;
    }

    private void showRateChangeNotification(CoinEntyti newCoin, String priseDiff) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_RATE_CHANGED);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(newCoin.name);
        builder.setContentText(getString(R.string.notification_rate_change_body, priseDiff));
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_RATE_CHANGED,
                    getString(R.string.notification_rate_changed),
                    NotificationManager.IMPORTANCE_HIGH
            );

            manager.createNotificationChannel(channel);
        }

        manager.notify(newCoin.symbol, NOTIFICATION_ID_RATE_CHANGED, notification);

    }

}
