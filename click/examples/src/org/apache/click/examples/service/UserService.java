/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.examples.service;

import org.apache.click.examples.domain.User;
import org.apache.click.extras.cayenne.CayenneTemplate;
import org.springframework.stereotype.Component;

/**
 * Provides a User Service.
 *
 * @see User
 */
@Component
public class UserService extends CayenneTemplate {

    public boolean isAuthenticatedUser(User user) {
        User user2 = getUser(user.getUsername());

        if (user2 != null && user2.getPassword().equals(user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    public User getUser(String username) {
        return (User) findObject(User.class, "username", username);
    }

    public User createUser(String fullName, String email, String username, String password) {
        User user = new User();
        getDataContext().registerNewObject(user);

        user.setEmail(email);
        user.setFullname(fullName);
        user.setUsername(username);
        user.setPassword(password);

        commitChanges();

        return user;
    }
}