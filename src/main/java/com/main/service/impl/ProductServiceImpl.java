package com.main.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.main.dto.ProductDto;
import com.main.dto.ProductResponse;
import com.main.model.Product;
import com.main.repository.ProductRepository;
import com.main.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Boolean saveProduct(ProductDto productDto) {

		Product map = mapper.map(productDto, Product.class);
		Product save = productRepository.save(map);

		if (ObjectUtils.isEmpty(save)) {
			return false;
		}
		return true;
	}

	@Override
	public List<ProductDto> getAllProduct() {
		List<Product> all = productRepository.findAll();
		List<ProductDto> productDtosList = all.stream().map(produt -> mapper.map(produt, ProductDto.class))
				.collect(Collectors.toList());
		return productDtosList;
	}

	@Override
	public ProductDto getProductById(Integer id) {
		Optional<Product> byId = productRepository.findById(id);
		if (byId.isPresent()) {
			Product product = byId.get();
			ProductDto map = mapper.map(product, ProductDto.class);
			return map;
		}
		return null;
	}

	@Override
	public Boolean deleteProductById(Integer id) {
		Optional<Product> byId = productRepository.findById(id);
		if (byId.isPresent()) {
			Product product = byId.get();
			productRepository.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public ProductResponse getProductWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = null;
		if (sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepository.findAll(pageable);

		List<Product> products = page.getContent();

		List<ProductDto> list = products.stream().map(prod -> mapper.map(prod, ProductDto.class)).toList();
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		boolean first = page.isFirst();
		boolean last = page.isLast();

		ProductResponse productResponse = ProductResponse.builder().products(list).totalElements(totalElements)
				.totalPages(totalPages).isFirst(first).isLast(last).pageSize(pageSize).build();
		return productResponse;
	}

}
