/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bzb.cloud.products.controller;

import com.bzb.cloud.products.exception.ProductNotFoundException;
import com.bzb.cloud.products.model.Product;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProductsController {

  @GetMapping(value = "/products/{productId}")
  public Product getProductDetails(@PathVariable("productId") String productId) {
    log.info("Fetching details for productId: {}", productId);
    return fetchProductDetails(productId);
  }

  private Product fetchProductDetails(String productId) {
    if ("1234".equals(productId)) {
      return Product.builder().id(productId).productName("USB C Cable").price(new BigDecimal("10.99"))
          .weight(new BigDecimal("99.8")).description("Universal USB-C Cable").manufacturer("Manufacturer A").build();
    } else if ("2345".equals(productId)) {
      return Product.builder().id(productId).productName("Power Bank").price(new BigDecimal("30.50"))
          .weight(new BigDecimal("358")).description("Portable power bank").manufacturer("Manufacturer B").build();
    }
    throw new ProductNotFoundException(String.format("Product with id: %s not found.", productId));
  }
}
