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
package com.bzb.cloud.sales.client;

import com.bzb.cloud.sales.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * A Feign HTTP client for the products service. The parameter <code>name</code> of the {@link FeignClient} annotation is set to the service-name of
 * the products service, which is registered with Zookeeper.
 */
@FeignClient(name = "products-service")
public interface ProductsClient {

  @GetMapping("/products/{productId}")
  Product getProductsDetails(@PathVariable(name = "productId") String productId);
}
