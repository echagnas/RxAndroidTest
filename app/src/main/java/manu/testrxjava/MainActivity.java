package manu.testrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Observable<String> mFirstObservable;
    private Observable<Integer> mSecondObservable;
    private Observable<String> mThirdObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation of view.
        init();

        //Create Observables.
        createObservables();
    }

    /**
     * Initialisation of view.
     */
    private void init(){
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFirstObserver();
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSecondObserver();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActionForArray();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createThirdObserver();
            }
        });
    }

    /**
     * Create Observables.
     */
    private void createObservables(){
        //Create an Observable that just return a String when an observer is attached to it.
        mFirstObservable = Observable.just("Data from first Observable");

        //Create an Observable that return each Integer each after other.
        mSecondObservable = Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        //Create a complex Observable which control the call to callbacks.
        createObservable();
    }

    private void createFirstObserver(){
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Received '"+s+"'");
            }
        };

        Subscription subscription = mFirstObservable.subscribe(observer);
        //subscription.unsubscribe(); //detach the observer from its observable while the observable is still emmitting data.
    }

    private void createSecondObserver(){
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "Received '" + s + "'" + " bis");
            }
        };

        Subscription subscription = mFirstObservable.subscribe(action1);
        //subscription.unsubscribe(); //detach the observer from its observable while the observable is still emmitting data.

    }

    private void createActionForArray(){
        Action1<Integer> action1 = new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "Data from second Observable '"+integer+"'");
            }
        };

        mSecondObservable.subscribe(action1);
    }

    /**
     * Create a complex Observable which control the call to callbacks.
     */
    private void createObservable(){
        mThirdObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    subscriber.onNext("Data from third Observable");
                    subscriber.onCompleted();
                }catch(Exception e){
                    subscriber.onError(e);
                }
            }
        });
    }

    private void createThirdObserver(){
        if(mThirdObservable != null){
            mThirdObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Log.d(TAG, "Received '"+s+"'");
                        }
                    });
        }
    }

}
