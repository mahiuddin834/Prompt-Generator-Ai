package com.itnation.promptai.AiModel;

public interface ResponseCallback {

    void onResponse(String response);
    void onError(Throwable throwable);
}
