package com.kseniyaa.loftcoin;

import android.app.Application;

import com.kseniyaa.loftcoin.data.api.Api;
import com.kseniyaa.loftcoin.data.api.ApiInitialaizer;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.DatabaseInitialaizer;
import com.kseniyaa.loftcoin.data.db.realm.DatabaseImplRealm;
import com.kseniyaa.loftcoin.data.prefs.Prefs;
import com.kseniyaa.loftcoin.data.prefs.PrefsImpl;

public class App extends Application {

    private Prefs prefs;
    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new PrefsImpl(this);
        api = new ApiInitialaizer().init();
        new DatabaseInitialaizer().init(this);
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public Api getApi () {
        return api;
    }

    public Database getDatabase () {
        return new DatabaseImplRealm();
    }
}
