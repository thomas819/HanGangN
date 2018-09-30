package com.example.thomas.hangangn.view.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.thomas.hangangn.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.fragment_favorites_WebView) WebView mWebView;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, view);

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        new FavoritesFragment.BackgroundWorker().execute();

        return view;
    }
    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {
        Handler uiHandler = new Handler();

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document htmlDocument = Jsoup.connect("http://hangang.seoul.go.kr/archives/category/office/hangang_news_office-n1").get();
                Element element = htmlDocument.select("div[id=sub_centent]").first();

                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (html != null) {
                                mWebView.loadData(html, "text/html", "utf-8");
                            }
                        } catch (NullPointerException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return false;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
