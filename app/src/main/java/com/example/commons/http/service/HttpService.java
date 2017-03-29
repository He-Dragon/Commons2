package com.example.commons.http.service;

import com.example.commons.base.BaseHttpResult;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 阿龙 on 2017/3/3.
 * <p>
 * POST请求
 *
 * @Field("参数名字") 后面跟参数的值
 * @FieldMap Map<String, String>键值对传入
 * @Body 传一个类 好维护
 * 列子：
 * @FormUrlEncoded
 * @POST("book/reviews") Observable<BaseHttpResult<T>> addReviews(@Field("book") String bookId, @Field("title") String title);
 * @FormUrlEncoded
 * @POST("book/reviews") Observable<BaseHttpResult<T>> addReviews(@FieldMap Map<String, String> fields);
 * @FormUrlEncoded
 * @POST("book/reviews") Observable<BaseHttpResult<T>> addReviews(@Body Reviews reviews);
 * public class Reviews {
 * public String book;
 * public String title;
 * public String content;
 * public String rating;
 * }
 * <p>
 * <p>
 * <p>
 * <p>
 * GET请求
 * @Path("参数的name") 后面跟需要传的参数类型（@Path可以用于任何请求方式，包括Post，Put，Delete等等）
 * @QueryMap Map<String, String>类型的键值对
 * @Query 参数名字一样，后面的之不一样
 * 列子：
 * @GET("/api/4/news/before/{date}") Observable<BaseHttpResult<T>> getTheDaily(@Path("date") String date);
 * @GET("book/search") Observable<BaseHttpResult<T>> getSearchBooks(@QueryMap Map<String, String> options);
 * @GET("book/search") Observable<BaseHttpResult<T>> getSearchBooks(@Query("q") List<String> name);
 * <p>
 * <p>
 * <p>
 * <p>
 * 文件夹参数上传
 * 列子：
 * RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
 * .addFormDataPart("name", name)
 * .addFormDataPart("name", psd)
 * .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
 * .build();
 * @Multipart
 * @POST("upload/") Observable<BaseHttpResult<T>> register(@Body RequestBody body);
 * <p>
 *
 *     url：http://gank.io/api/data/福利/50/1
 *     https://api.douban.com/v2/movie/top250?start=0&count=10
 *
 *
 *
 *
 */

public interface HttpService {
    @POST("请求地址")
    Observable<BaseHttpResult> login(@FieldMap Map<String,String> map);


}
