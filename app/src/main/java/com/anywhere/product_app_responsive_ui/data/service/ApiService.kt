package com.anywhere.product_app_responsive_ui.data.service

import com.anywhere.product_app_responsive_ui.data.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0
    ): ProductsResponse
}