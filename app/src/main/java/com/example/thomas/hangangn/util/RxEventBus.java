package com.example.thomas.hangangn.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxEventBus {


    private static RxEventBus mInstance;
    private PublishSubject<Object> mSubject;

    private RxEventBus() {
        mSubject = PublishSubject.create();
    }

    public static RxEventBus getInstance() {
        if (mInstance == null) {
            mInstance = new RxEventBus();
        }
        return mInstance;
    }

    public void sendEvent(Object object) {
        mSubject.onNext(object);
    }

    public Observable<Object> getObservable() {
        return mSubject;
    }


}
