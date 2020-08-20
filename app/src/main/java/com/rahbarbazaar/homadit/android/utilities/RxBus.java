package com.rahbarbazaar.homadit.android.utilities;

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


    public static class TotalEditProductData {
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();
        public static Disposable subscribeTotalEditProductData(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }

        public static void publishTotalEditProductData(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }


    public static class MemberPrizeLists {
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();
        public static Disposable subscribeMemberPrizeLists(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }

        public static void publishMemberPrizeLists(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }


    public static class ProfileInfo {
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();
        public static Disposable subscribeProfileInfo(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }
        public static void publishProfileInfo(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

    public static class HistoryList0 {
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();
        public static Disposable subscribeHistoryList0(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }
        public static void publishHistoryList0(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

    public static class TransactionAmountList0 {
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();
        public static Disposable subscribeTransactionAmountList0(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }
        public static void publishTransactionAmountList0(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

    public static class Lottary {
        private static final BehaviorSubject<Object> behaviorSubject
                = BehaviorSubject.create();
        public static Disposable subscribeLottary(@NonNull Consumer<Object> action) {
            return behaviorSubject.subscribe(action);
        }
        public static void publishLottary(@NonNull Object message) {
            behaviorSubject.onNext(message);
        }
    }

//    public static class ActiveList0 {
//        private static final BehaviorSubject<Object> behaviorSubject
//                = BehaviorSubject.create();
//        public static Disposable subscribeActiveList0(@NonNull Consumer<Object> action) {
//            return behaviorSubject.subscribe(action);
//        }
//        public static void publishActiveList0(@NonNull Object message) {
//            behaviorSubject.onNext(message);
//        }
//    }


}
