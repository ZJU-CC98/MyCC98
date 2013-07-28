package tk.djcrazy.libCC98.util;

/**
 * Created by DJ on 13-7-28.
 */
public
interface RequestResultListener<T> {


    public void onReuqestComplete(T result);

    public void onReuqestError();

}
