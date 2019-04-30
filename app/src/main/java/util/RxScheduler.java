package util;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxScheduler {
    public interface IOTask<T> {
        void doOnIOThread();
    }

    public interface UITask<T> {
        void doOnUIThread();
    }

    public interface ExecuteTask<T> {
        void execute();
    }

    public abstract class Task<T> {
        private T t;

        public Task(T t) {
            this.t = t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public T getT() {
            return t;
        }

        public abstract void doOnUIThread();

        public abstract void doOnIOThread();
    }


    public static <T> void doOnIOThread(final IOTask<T> task) {
        Observable.just(task)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<IOTask<T>>() {
                    @Override
                    public void accept(IOTask<T> tioTask) throws Exception {
                        tioTask.doOnIOThread();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public static <T> void doOnUiThread(final UITask<T> task) {
        Observable.just(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UITask<T>>() {
                    @Override
                    public void accept(UITask<T> tuiTask) throws Exception {
                        tuiTask.doOnUIThread();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }
}
