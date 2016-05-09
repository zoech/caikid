package com.imzoee.caikid.utils.misc;

import rx.Observable;
import rx.Subscriber;

import com.imzoee.caikid.dao.User;
import com.imzoee.caikid.fragment.MeFragment;
import com.imzoee.caikid.fragment.OrderFragment;
import com.imzoee.caikid.model.CaikidCart;

/**
 * Created by zoey on 2016/5/1.
 *
 * Here is a cluster of interface that provide PUB-SUB functions across
 * all components (activity, fragments, etc). The very common use of
 * these interface is to alert the activity or fragment to refresh
 * their view, commonly after user getting some new data from the
 * server side.
 * A basically situation: when the user login,
 * we alert all the activity that display information about the user.
 */
public class ObservablesFactory {

    /**
     * alert those components related to user information.
     * currently these components include:
     * 1. OrderFragment
     * 2. MeFragment
     *
     * @param user
     * User object of the new login user. If it is null,
     * then it alert all those related components with
     * user logout signal. Otherwise, this method alert
     * all the components with login signal, and user
     * is the login user.
     */
    public static void loginStateObservable(final User user){
        Observable<User> loginStateObservable = Observable.create(new Observable.OnSubscribe<User>(){
            @Override
            public void call(Subscriber<? super User> subscriber) {
                subscriber.onNext(user);
                subscriber.onCompleted();
            }
        });

        Subscriber<User> orderLogoutSubscriber = OrderFragment.getLoginStateSubscriber();
        Subscriber<User> meLogoutSubscriber = MeFragment.getLoginStateSubscriber();
        if (orderLogoutSubscriber != null){
            loginStateObservable.subscribe(orderLogoutSubscriber);
        }
        if (meLogoutSubscriber != null) {
            loginStateObservable.subscribe(meLogoutSubscriber);
        }
    }

    /**
     * alert those components related to cart action.
     *
     * @param cart
     * CaijkidCart object, representing the current cart status.
     */
    public static void cartActionObservable(final CaikidCart cart){
        Observable<CaikidCart> cartObservable = Observable.create(new Observable.OnSubscribe<CaikidCart>(){
            @Override
            public void call(Subscriber<? super CaikidCart> subscriber) {
                subscriber.onNext(cart);
                subscriber.onCompleted();
            }
        });
/*
        Subscriber<User> orderLogoutSubscriber = OrderFragment.getLoginStateSubscriber();
        Subscriber<User> meLogoutSubscriber = MeFragment.getLoginStateSubscriber();
        if (orderLogoutSubscriber != null){
            loginStateObservable.subscribe(orderLogoutSubscriber);
        }
        if (meLogoutSubscriber != null) {
            loginStateObservable.subscribe(meLogoutSubscriber);
        }
        */
    }
}
