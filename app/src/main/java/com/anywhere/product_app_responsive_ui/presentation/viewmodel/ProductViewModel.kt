package com.anywhere.product_app_responsive_ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.anywhere.product_app_responsive_ui.data.remote.RetrofitClient
import com.anywhere.product_app_responsive_ui.data.repo.ProductRepositoryImpl
import com.anywhere.product_app_responsive_ui.domain.repo.ProductRepository
import com.anywhere.product_app_responsive_ui.presentation.model.ProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(val productRepository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            productRepository.getProducts()
                .onSuccess { products ->
                    _uiState.value = ProductUiState.Success(products)
                }
                .onFailure { exception ->
                    _uiState.value = ProductUiState.Error(
                        exception.message ?: "Error occurred"
                    )
                }
        }
    }
}

class ProductListViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {

            val apiService = RetrofitClient.apiService
            val repository = ProductRepositoryImpl(
                apiService = apiService
            )

            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(
                productRepository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}