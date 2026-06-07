package com.main.service;

import java.util.List;

import com.main.dto.ProductDto;
import com.main.dto.ProductResponse;
import com.main.model.Product;

public interface ProductService {
	public Boolean saveProduct(ProductDto productDto);

	public List<ProductDto> getAllProduct();

	public ProductDto getProductById(Integer id);

	public Boolean deleteProductById(Integer id);

	public ProductResponse getProductWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);
}
