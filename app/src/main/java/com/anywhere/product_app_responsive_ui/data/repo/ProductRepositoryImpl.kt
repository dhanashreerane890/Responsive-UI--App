package com.anywhere.product_app_responsive_ui.data.repo

import com.anywhere.product_app_responsive_ui.data.model.Product
import com.anywhere.product_app_responsive_ui.data.service.ApiService
import com.anywhere.product_app_responsive_ui.domain.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(private val apiService: ApiService) : ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProducts()
            Result.success(response.products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}