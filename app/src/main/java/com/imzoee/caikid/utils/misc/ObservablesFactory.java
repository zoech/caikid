package com.imzoee.caikid.utils.misc;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.imzoee.caikid.activity.CartActivity;
import com.imzoee.caikid.activity.MainActivity;
import com.imzoee.caikid.activity.OrderDetailActivity;
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
     * 3. CartActivity
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

        Subscriber<User> orderLoginSubscriber = OrderFragment.getLoginStateSubscriber();
        Subscriber<User> meLoginSubscriber = MeFragment.getLoginStateSubscriber();
        Subscriber<User> cartLoginSubscriber = CartActivity.getLoginSubscriber();
        if (orderLoginSubscriber != null){
            loginStateObservable.subscribe(orderLoginSubscriber);
        }
        if (meLoginSubscriber != null) {
            loginStateObservable.subscribe(meLoginSubscriber);
        }
        if (cartLoginSubscriber != null){
            loginStateObservable.subscribe(cartLoginSubscriber);
        }
    }

    /**
     * alert those components related to cart action.
     *
     * @param opt
     * CaijkidCart object, representing the current cart status.
     */
    public static void cartActionObservable(final String opt){
        Observable<String> cartObservable = Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(opt);
                subscriber.onCompleted();
            }
        });

        Subscriber<String> homeSubscriber = MainActivity.getCartSubscriber();
        Subscriber<String> cartSubscriber = CartActivity.getCartSubscriber();
        if (homeSubscriber != null){
            cartObservable.subscribe(homeSubscriber);
        }
        if (cartSubscriber != null){
            cartObservable.subscribe(cartSubscriber);
        }
    }

    /**
     * alert those components related to cart action, using
     * the hand make observable.
     *
     * @param obs
     * The observable manuel created. This method will use
     * the obs as the observable.
     */
    public static void cartActionObservable(Observable<String> obs){

        obs.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Subscriber<String> homeSubscriber = MainActivity.getCartSubscriber();
        Subscriber<String> cartSubscriber = CartActivity.getCartSubscriber();
        if (homeSubscriber != null){
            obs.subscribe(homeSubscriber);
        }
        if (cartSubscriber != null){
            obs.subscribe(cartSubscriber);
        }
    }

    public static void orderObservable(final String ignoreStr){
        Observable<String> cartObservable = Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(ignoreStr);
                subscriber.onCompleted();
            }
        });

        Subscriber<String> homeSubscriber = OrderFragment.getOrderSubscriber();
        Subscriber<String> detailSubscriber = OrderDetailActivity.getOrderSubscriber();
        if (homeSubscriber != null){
            cartObservable.subscribe(homeSubscriber);
        }
        if (detailSubscriber != null){
            cartObservable.subscribe(detailSubscriber);
        }
    }
}
