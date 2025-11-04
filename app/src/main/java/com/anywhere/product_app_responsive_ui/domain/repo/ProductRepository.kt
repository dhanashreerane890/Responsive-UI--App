package com.anywhere.product_app_responsive_ui.domain.repo

import com.anywhere.product_app_responsive_ui.data.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
}