package com.kseniyaa.loftcoin.screens.main.wallets;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kseniyaa.loftcoin.App;
import com.kseniyaa.loftcoin.R;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.prefs.Prefs;
import com.kseniyaa.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.kseniyaa.loftcoin.screens.currencies.CurrenciesBottomSheetListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {

    public static final String VIEW_PAGER_POS = "view_pager_pos";

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;

    @BindView(R.id.transaction_recycler)
    RecyclerView transactionRecycler;

    @BindView(R.id.new_wallet)
    ViewGroup newWallet;

    private WalletsViewModelImpl viewModel;
    private WalletsPagerAdapter walletsPagerAdapter;
    private TransactionAdapter transactionAdapter;

    private Integer restoredViewPagerPos;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();

        walletsPagerAdapter = new WalletsPagerAdapter(prefs);
        transactionAdapter = new TransactionAdapter(prefs);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.toolbar_title_wallet);
        toolbar.inflateMenu(R.menu.menu_wallets);

        transactionRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        transactionRecycler.setHasFixedSize(true);
        transactionRecycler.setAdapter(transactionAdapter);

        int screenWidth = getScreenWidth();
        int walletItemWidth = getResources().getDimensionPixelOffset(R.dimen.item_wallet_width);
        int walletItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_between_wallet_margin);
        int pageMargin = (screenWidth - walletItemWidth) - walletItemMargin;


        walletsPager.setPageMargin(-pageMargin);
        walletsPager.setOffscreenPageLimit(5);
        walletsPager.setAdapter(walletsPagerAdapter);
        walletsPager.setPageTransformer(false, new ZoomOutPageTransformer());

        Fragment bottomSheetSource = getFragmentManager().findFragmentByTag(CurrenciesBottomSheet.TAG);
        if (bottomSheetSource != null) {
            ((CurrenciesBottomSheet) bottomSheetSource).setListener(this);
        }

        if (savedInstanceState != null) {
            restoredViewPagerPos = savedInstanceState.getInt(VIEW_PAGER_POS, 0);
        }

        viewModel.getWallets();

        initInputs();
        initOutputs();
    }


    private void initOutputs() {
        newWallet.setOnClickListener(v -> viewModel.onNewWalletClick());

        toolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(item ->
        {
            viewModel.onNewWalletClick();
            return true;
        });

        walletsPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                viewModel.onWalletChange(position);
            }
        });

    }

    private void initInputs() {
        viewModel.wallets().observe(this, wallets -> {
            walletsPagerAdapter.setWallets(wallets);
            if (restoredViewPagerPos != null) {
                walletsPager.setCurrentItem(restoredViewPagerPos);
                restoredViewPagerPos = null;
            }
        });

        viewModel.walletsVisible().observe(this, visible -> walletsPager.setVisibility(visible ? View.VISIBLE : View.GONE));
        viewModel.newWalletVisible().observe(this, visible -> newWallet.setVisibility(visible ? View.VISIBLE : View.GONE));
        viewModel.selectCurrency().observe(this, o -> showCurrenciesBottomSheet());
        viewModel.transactions().observe(this, transactionModels -> transactionAdapter.setTransactions(transactionModels));
        viewModel.scrollToNewWallet().observe(this, o -> walletsPager.setCurrentItem(walletsPagerAdapter.getCount() - 1, true));

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(VIEW_PAGER_POS, walletsPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    private void showCurrenciesBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(getFragmentManager(), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);
    }


    @Override
    public void onCurrencySelected(CoinEntyti coin) {
        viewModel.onCurrencySelected(coin);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
