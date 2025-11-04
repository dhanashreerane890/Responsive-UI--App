package com.anywhere.product_app_responsive_ui.presentation.model

import com.anywhere.product_app_responsive_ui.data.model.Product

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}