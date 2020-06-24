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
package com.bzb.cloud.users.controller;

import com.bzb.cloud.users.exception.UserNotFoundException;
import com.bzb.cloud.users.model.SalesList;
import com.bzb.cloud.users.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class UsersController {

  private static final String SALES_ENDPOINT = "/sales?userId={userId}";

  private final RestTemplate restTemplate;

  @Autowired
  public UsersController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping(value = "/users/{userId}")
  public ResponseEntity<User> getUserDetails(@PathVariable("userId") String userId) {
    log.info("Fetching user data for userId: {}.", userId);
    User user = fetchUserDetails(userId);

    log.info("Fetching recent sales from sales-service for userId: {}.", userId);
    SalesList salesList = restTemplate.getForObject(SALES_ENDPOINT, SalesList.class, userId);
    user.setRecentPurchases(salesList.getSales());

    return ResponseEntity.ok(user);
  }

  private User fetchUserDetails(String userId) {
    if ("10001".equals(userId)) {
      return User.builder().id(userId).name("Max Tester").age(25).email("max@testme.com").build();
    }
    throw new UserNotFoundException(String.format("User with id: %s not found.", userId));
  }
}
