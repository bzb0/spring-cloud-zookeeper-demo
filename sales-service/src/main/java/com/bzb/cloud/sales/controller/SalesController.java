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
package com.bzb.cloud.sales.controller;

import com.bzb.cloud.sales.client.ProductsClient;
import com.bzb.cloud.sales.dto.PurchasedProductDTO;
import com.bzb.cloud.sales.dto.SaleDTO;
import com.bzb.cloud.sales.dto.SalesListDTO;
import com.bzb.cloud.sales.model.Product;
import com.bzb.cloud.sales.model.Sale;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SalesController {

  @LoadBalanced
  private final ProductsClient productsClient;

  @Autowired
  public SalesController(ProductsClient productsClient) {
    this.productsClient = productsClient;
  }

  @GetMapping(value = "/sales")
  public SalesListDTO getRecentSales(@RequestParam(name = "userId", required = true) String userId) {
    log.info("Fetching recent purchases for userId: {}", userId);
    List<Sale> sales = fetchRecentSales(userId);

    log.info("Fetching product details from products service.");
    return fetchProductDetails(sales);
  }

  private List<Sale> fetchRecentSales(String userId) {
    return Arrays.asList(Sale.builder().id("400021").userId(userId).date(LocalDate.now()).productQuantities(Map.of("1234", 2)).build(),
        Sale.builder().id("63551").userId(userId).date(LocalDate.now().minusMonths(5)).productQuantities(Map.of("2345", 1)).build());
  }

  private SalesListDTO fetchProductDetails(List<Sale> sales) {
    return SalesListDTO.builder().sales(sales.stream().map(sale -> {
      var productQuantities = getProductDetails(sale);
      return SaleDTO.builder().id(sale.getId()).date(sale.getDate()).products(productQuantities).build();
    }).collect(Collectors.toList())).build();
  }

  private List<PurchasedProductDTO> getProductDetails(Sale sale) {
    return sale.getProductQuantities().entrySet().stream().map(e -> {
      Product productDetails = productsClient.getProductsDetails(e.getKey());
      return PurchasedProductDTO.builder().product(productDetails).quantity(e.getValue()).build();
    }).collect(Collectors.toList());
  }
}
