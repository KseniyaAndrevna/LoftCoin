package com.kseniyaa.loftcoin.screens.main.rate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kseniyaa.loftcoin.App;
import com.kseniyaa.loftcoin.R;
import com.kseniyaa.loftcoin.data.api.Api;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntityMapper;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.model.Fiat;
import com.kseniyaa.loftcoin.data.prefs.Prefs;
import com.kseniyaa.loftcoin.job.JobHelper;
import com.kseniyaa.loftcoin.job.JobHelperImpl;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RateFragment extends Fragment implements RateView,
        Toolbar.OnMenuItemClickListener,
        CurrencyDialog.CurrencyDialogListener,
        RateAdapter.Listener {

    private static final String LAYOUT_MANAGER_STATE = "layout_manager_state";

    @BindView(R.id.rate_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rate_content)
    ViewGroup content;

    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.rate_recycler)
    RecyclerView recycler;

    @BindView(R.id.rate_progress)
    ViewGroup progress;

    private RatePresenter presenter;
    private RateAdapter adapter;
    Unbinder unbinder;
    private Parcelable layoutManagerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();

        if (activity == null) {
            return;
        }

        Api api = ((App) getActivity().getApplication()).getApi();
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();
        Database mainDatabase = ((App) getActivity().getApplication()).getDatabase();
        Database workerDatabase = ((App) getActivity().getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();
        JobHelper jobHelper = new JobHelperImpl(getContext());

        presenter = new RatePresenterImpl(api, prefs, mainDatabase, workerDatabase, mapper,jobHelper);

        adapter = new RateAdapter(prefs);
        adapter.setHasStableIds(true);
        adapter.setListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.rate_fragment_title);
        toolbar.inflateMenu(R.menu.menu_rate);
        toolbar.setOnMenuItemClickListener(this);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(() ->
                presenter.onRefresh()
        );

        if (savedInstanceState != null) {
            layoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }

        Fragment fragment = getFragmentManager() != null ? getFragmentManager().findFragmentByTag(CurrencyDialog.TAG) : null;
        if (fragment != null) {
            ((CurrencyDialog) fragment).setListener(this);
        }

        presenter.attachView(this);
        presenter.getRate();
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_STATE, Objects.requireNonNull(recycler.getLayoutManager()).onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCoins(List<CoinEntyti> coins) {
        adapter.setCoins(coins);

        if (layoutManagerState != null) {
            Objects.requireNonNull(recycler.getLayoutManager()).onRestoreInstanceState(layoutManagerState);
            layoutManagerState = null;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_currency:
                presenter.onMenuItemCurrencyClick();
                return true;
        }
        return false;
    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    @Override
    public void showCurrencyDialog() {
        CurrencyDialog currencyDialog = new CurrencyDialog();
        currencyDialog.setListener(this);
        currencyDialog.show(getFragmentManager(), CurrencyDialog.TAG);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onCurrencySelected(Fiat currency) {
        presenter.onFiatCurrencySelected(currency);
    }

    @Override
    public void onRateLongClick(String symbol) {
        presenter.onRateLongClick(symbol);
    }
}


























