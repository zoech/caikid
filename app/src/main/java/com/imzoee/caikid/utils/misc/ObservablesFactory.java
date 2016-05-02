package com.imzoee.caikid.utils.misc;

import rx.Observable;
import rx.Subscriber;

import com.imzoee.caikid.dao.User;

/**
 * Created by zoey on 2016/5/1.
 */
public class ObservablesFactory {
    public static Observable<User> loginStateObservable(final User user){
        return Observable.create(new Observable.OnSubscribe<User>(){
            @Override
            public void call(Subscriber<? super User> subscriber) {
                subscriber.onNext(user);
                subscriber.onCompleted();
            }
        });
    }
}
