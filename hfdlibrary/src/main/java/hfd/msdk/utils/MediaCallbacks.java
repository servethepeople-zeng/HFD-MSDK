package hfd.msdk.utils;

import hfd.msdk.model.HFDErrorCode;

public class MediaCallbacks {

    public MediaCallbacks(){

    }

    public interface MediaDataCallbacks<T>{
        public void onSuccess(T var1);
        public void onFailure(HFDErrorCode var1);
    }

    public interface CommonCallback<T>{
        public void onResult(T var1);
    }
}
