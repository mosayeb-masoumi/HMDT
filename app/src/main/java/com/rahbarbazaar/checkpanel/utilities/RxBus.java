package com.rahbarbazaar.checkpanel.utilities;

import io.reactivex.annotations.NonNull;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public final class RxBus {


    public static class DashboardModel {
        //this how to create our bus
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();

        public static Disposable subscribeDashboardModel(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }
        //use this method to send data
        public static void publishDashboardModel(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

//    public static class DashboardUpdateModel {
//        //this how to create our bus
//        private static final BehaviorSubject<Object> behaviorSubject
//                = BehaviorSubject.create();
//
//        public static Disposable subscribeDashboardUpdateModel(@NonNull Consumer<Object> action) {
//            return behaviorSubject.subscribe(action);
//        }
//        //use this method to send data
//        public static void publishDashboardUpdateModel(@NonNull Object message) {
//            behaviorSubject.onNext(message);
//        }
//    }

    public static class RegisterModel {
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();

        public static Disposable subscribeRegisterModel(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }

        public static void publishRegisterModel(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

    public static class ShoppingEdit {

        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();

        public static Disposable subscribeShoppingEdit(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }

        public static void publishShoppingEdit(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

    public static class BarcodeList {

        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();

        public static Disposable subscribeBarcodeList(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }

        public static void publishBarcodeList(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

}
