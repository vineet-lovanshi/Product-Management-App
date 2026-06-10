package com.main.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.dto.ProductDto;
import com.main.dto.ProductResponse;
import com.main.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/saveProduct")
	public ResponseEntity<?> saveProduct(@RequestBody ProductDto productDto) {

		try {
			validatProduct(productDto);
			Boolean saveProduct = productService.saveProduct(productDto);
			if (!saveProduct) {
				return new ResponseEntity<>("Product not saved", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>("Save Success", HttpStatus.CREATED);
	}

	private void validatProduct(ProductDto productDto) throws BadRequestException {
		if (productDto == null) {
			throw new BadRequestException("Request body is required");
		}

		if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {

			throw new BadRequestException("Name field is required");
		}

		if (productDto.getDescription() == null || productDto.getDescription().trim().isEmpty()) {

			throw new BadRequestException("Description field is required");
		}

		if (productDto.getPrice() == null) {
			throw new BadRequestException("Price is required");
		}

		if (productDto.getPrice() <= 0) {
			throw new BadRequestException("Price must be greater than 0");
		}

		if (productDto.getQuantity() == null) {
			throw new BadRequestException("Quantity is required");
		}

		if (productDto.getQuantity() <= 0) {
			throw new BadRequestException("Quantity must be greater than 0");
		}

	}

	@GetMapping("/getProducts")
	public ResponseEntity<?> getProductPagination(@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
			@RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
//		List<ProductDto> allProduct = productService.getAllProduct();
		ProductResponse productWithPagination = null;
//		String nameString = null;
//		nameString.toUpperCase();
		try {

			productWithPagination = productService.getProductWithPagination(pageNo, pageSize, sortBy, sortDir);
			if (ObjectUtils.isEmpty(productWithPagination)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(productWithPagination, HttpStatus.OK);
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		ProductDto productById = productService.getProductById(id);
		try {
			if (ObjectUtils.isEmpty(productById)) {
				return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(productById, HttpStatus.OK);
	}

	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		Boolean deleteProductById = productService.deleteProductById(id);
		try {
			if (!deleteProductById) {
				return new ResponseEntity<>("Product not deleted", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>("Deleted succesfully", HttpStatus.OK);
	}
}
