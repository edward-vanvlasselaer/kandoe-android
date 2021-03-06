package be.kdg.kandoe.kandoe.exception;

import android.util.Log;

import retrofit.Callback;

/**
 * Created by Edward on 14/03/2016.
 */
public abstract class AbstractExceptionCallback<T> implements Callback<T> {
    @Override
    public void onFailure(Throwable t) {
        Log.e("","RETROFIT FAILED: " + t.getMessage());
    }
}
