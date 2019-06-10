package com.elminson.photoapp.api

import com.elminson.photoapp.models.PhotoList
import retrofit2.Call
import retrofit2.http.GET

/**
 *
 */
interface PhotoAPI {
    @GET("?key=<key>&q=cars&image_type=photo")
    fun getPhotos() : Call<PhotoList>
}