package com.agadimi.retromockson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InstagramService
{
    @GET("{user_id}/?__a=1")
    Call<ResponseBody> getProfile(@Path("user_id") String userId);
}
