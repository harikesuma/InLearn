package com.example.inlearn.api;

import com.example.inlearn.data.model.ResponseComment;
import com.example.inlearn.data.model.ResponseCommentLike;
import com.example.inlearn.data.model.ResponseDetailQuestion;
import com.example.inlearn.data.model.ResponseEditProfile;
import com.example.inlearn.data.model.ResponseKategori;
import com.example.inlearn.data.model.ResponseNotification;
import com.example.inlearn.data.model.ResponsePostComment;
import com.example.inlearn.data.model.ResponsePostQuestion;
import com.example.inlearn.data.model.ResponseQuestion;
import com.example.inlearn.data.model.ResponseTopOfUser;
import com.example.inlearn.data.model.ResponseUser;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("user_name") String userName,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerRequest(@Field("name") String nama,
                                       @Field("user_name") String userName,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("jenis_kelamin") String jenisKelamin);

    @Multipart
    @POST("profile")
    Call<ResponseEditProfile> editProfileRequest(@Part MultipartBody.Part image,
                                                 @Part("id") RequestBody id,
                                                 @Part("user_name") RequestBody userName,
                                                 @Part("name") RequestBody name);

    @GET("kategori")
    Call<ResponseKategori> getKategori();



    @Multipart
    @POST("pertanyaan/postPertanyaan")
    Call<ResponsePostQuestion> postingQuestionRequest(@Part MultipartBody.Part image,
                                                      @Part("user_id") RequestBody userId,
                                                      @Part("question")  RequestBody question,
                                                      @Part("kategori_id")  RequestBody kategori);


    @POST("pertanyaan/showPertanyaan")
    Call<ResponseQuestion> getQuestionList();


    @GET("pertanyaan/showDetailPertanyaan/{id}")
    Call<ResponseDetailQuestion> getDetailQuestion(@Path("id") int id);

    @GET("pertanyaan/showComment/{id}")
    Call<ResponseComment> getComment(@Path("id") int id);

    @POST("pertanyaan/showComment/like/{id}")
    Call<ResponseCommentLike> commentLike(@Path("id") int id,
                                          @Query("user_id") String userId);

    @POST("pertanyaan/postJawaban")
    Call<ResponsePostComment> postingCommentRequest(@Query("pertanyaan_id") int pertanyaan_id,
                                                    @Query("user_id") String userId,
                                                    @Query("jawaban") String jawaban);


    @POST("user/historyQuestionActivity/{id}")
    Call<ResponseQuestion> getUserQuestionHistory(@Path("id") String id);

    @POST("user/historyAnswerActivity/{id}")
    Call<ResponseComment> getUserCommentHistory(@Path("id") String id);

    @POST("user/historyAnswerActivity/delete/{id}")
    Call<ResponseComment> deleteComment(@Path("id") int id);

    @POST("user/historyQuestionActivity/delete/{id}")
    Call<ResponseQuestion> deleteQuestion(@Path("id") int id);

    @GET("user/historyQuestionActivity/edit/{id}")
    Call<ResponseDetailQuestion> showEditQuestion(@Path("id") int id);

    @POST("user/historyQuestionActivity/update/{id}")
    Call<ResponseQuestion> editQuestion(@Path("id") int id,
                                        @Query("pertanyaan") String question,
                                        @Query("kategori_id") String kategori);

    @GET("pertanyaan/topOfUser")
    Call<ResponseTopOfUser> getTopOfuser();

    @GET("user/getAllUser")
    Call<ResponseUser> getAllUser();

    @GET("fcm/add/{token}")
    Call<ResponseBody> insertToken(@Path("token") String token,
                                   @Query("user_id") String userId);

    @GET("fcm/deleting/{token}")
    Call<ResponseBody> deleteToken(@Path("token") String token,
                                   @Query("user_id") String userId);

    @GET("fcm/getAllNotification")
    Call<ResponseNotification> getAllNotification();
}
