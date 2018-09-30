package com.example.thomas.hangangn.view.fragment.detail;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.thomas.hangangn.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ParkInfoFragment extends Fragment {


    @BindView(R.id.fragment_park_info_tv)
    TextView mTv;
    Unbinder unbinder;

    @BindView(R.id.fragment_park_info_web_view)
    WebView mWebView;

    private static final String INFO = "info";
    private String info;


    public static ParkInfoFragment newInstance(String param1) {
        ParkInfoFragment fragment = new ParkInfoFragment();
        Bundle args = new Bundle();
        args.putString(INFO, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            info = getArguments().getString(INFO);
        }
    }

    public ParkInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_park_info, container, false);
        unbinder = ButterKnife.bind(this, view);

        System.out.println("parkInfo"+info);

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.setWebChromeClient(new WebChromeClient());
//        mWebView.loadUrl("http://hangang.seoul.go.kr/archives/46585");
        new BackgroundWorker().execute();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.pauseTimers();
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

    class BackgroundWorker extends AsyncTask<Void, Void, Void> {
        Handler uiHandler = new Handler();

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document htmlDocument = Jsoup.connect("http://hangang.seoul.go.kr/archives/" + info).get();
                Element element = htmlDocument.select("div:not(.map_1) div[id=post_content]").first();

                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();


                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(html !=null){
                                try{
                                    mWebView.loadData(html, "text/html", "utf-8");
                                }catch (NullPointerException e){

                                }
                            }

                        }
                    });


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
