package com.main.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.main.dto.ProductDto;
import com.main.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/saveProduct")
	public ResponseEntity<?> saveProduct(@RequestBody ProductDto productDto) {
		Boolean saveProduct = productService.saveProduct(productDto);
		try {
			if (!saveProduct) {
				return new ResponseEntity<>("Product not saved", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>("Save Success", HttpStatus.CREATED);
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllProduct() {
		List<ProductDto> allProduct = productService.getAllProduct();
		try {
			if (CollectionUtils.isEmpty(allProduct)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(allProduct, HttpStatus.CREATED);
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
