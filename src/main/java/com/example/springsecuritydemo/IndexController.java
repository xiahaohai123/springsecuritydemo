/*
 * Copyright 2002-2016 the original author or authors.
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
package com.example.springsecuritydemo;

import com.example.springsecuritydemo.databind.AliasProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * Controller for "/".
 * @author Joe Grandja
 */
@RestController
public class IndexController {

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @AliasProcessor
    @GetMapping("list")
    public ResponseEntity<?> getSessionList(SessionRequest sessionRequest) {
        System.out.println(sessionRequest);
        return ResponseEntity.ok(sessionRequest);
    }

    @GetMapping("list1")
    public ResponseEntity<?> getSessionList1(@RequestBody SessionRequest sessionRequest) {
        System.out.println(sessionRequest);
        return ResponseEntity.ok(sessionRequest);
    }
}
