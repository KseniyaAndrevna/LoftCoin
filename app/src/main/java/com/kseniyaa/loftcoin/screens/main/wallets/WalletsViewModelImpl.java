package com.kseniyaa.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kseniyaa.loftcoin.App;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Transaction;
import com.kseniyaa.loftcoin.data.db.model.Wallet;
import com.kseniyaa.loftcoin.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletsViewModelImpl extends WalletsViewModel {

    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> walletsVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> newWalletVisible = new MutableLiveData<>();
    private MutableLiveData<List<Wallet>> walletsItems = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> transactionsItems = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    private Database database;

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);

        database = ((App) getApplication()).getDatabase();
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public LiveData<List<Wallet>> wallets() {
        return walletsItems;
    }

    @Override
    public LiveData<List<Transaction>> transactions() {
        return transactionsItems;
    }

    @Override
    public LiveData<Boolean> walletsVisible() {
        return walletsVisible;
    }

    @Override
    public LiveData<Boolean> newWalletVisible() {
        return newWalletVisible;
    }

    @Override
    public void onCurrencySelected(CoinEntyti coin) {
        Wallet wallet = randomWallet(coin);
        List<Transaction> transactions = randomTransactions(wallet);

        Disposable disposable = Observable.fromCallable(() -> {
                    database.saveWallet(wallet);
                    database.saveTransaction(transactions);
                    return new Object();
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();

        disposables.add(disposable);
    }


    @Override
    public void getWallets() {
        getWalletsInner();
    }

    @Override
    public void onWalletChange(int position) {
        Wallet wallet = walletsItems.getValue().get(position);
        getTransaction(wallet.walletId);
    }

    private void getWalletsInner() {
        Disposable disposable = database.getWallets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        wallets -> {
                            if (wallets.isEmpty()) {
                                walletsVisible.setValue(false);
                                newWalletVisible.setValue(true);
                            } else {
                                walletsVisible.setValue(true);
                                newWalletVisible.setValue(false);

                                if (walletsItems.getValue() == null || walletsItems.getValue().isEmpty()) {
                                    Wallet wallet = wallets.get(0);
                                    String walletId = wallet.walletId;
                                    getTransaction(walletId);
                                }

                                walletsItems.setValue(wallets);
                            }
                        });
        disposables.add(disposable);
    }

    private void getTransaction(String walletId) {
        Disposable disposable = database.getTransactions(walletId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions -> transactionsItems.setValue(transactions));

        disposables.add(disposable);
    }

    private Wallet randomWallet(CoinEntyti coin) {
        Random random = new Random();
        return new Wallet(UUID.randomUUID().toString(),  10 + random.nextDouble(),coin);
    }

    private List<Transaction> randomTransactions(Wallet wallet) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            transactions.add(randomTransaction(wallet));
        }

        return transactions;
    }

    private Transaction randomTransaction(Wallet wallet) {
        Random random = new Random();

        long startDate = 1504787514000L;
        long newDate = System.currentTimeMillis();
        long date = startDate + (long) (random.nextDouble() * (newDate - startDate));

        double amount = 2 * random.nextDouble();
        boolean amountSign = random.nextBoolean();

        return new Transaction(wallet.walletId, amountSign ? amount : -amount, date, wallet.coin);
    }
}
