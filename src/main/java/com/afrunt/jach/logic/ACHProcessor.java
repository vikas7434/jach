/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.afrunt.jach.logic;

import com.afrunt.jach.metadata.ACHMetadata;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Andrii Frunt
 */
class ACHProcessor implements ACHErrorMixIn, ACHFieldConversionSupport {
    static final String LINE_SEPARATOR = Optional.ofNullable(System.getProperty("line.separator")).orElse("\n");

    private ACHMetadata metadata;

    private Map<Integer, Method> methodsCache = new HashMap<>();

    public ACHProcessor(ACHMetadata metadata) {
        this.metadata = metadata;
    }

    ACHMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Map<Integer, Method> getMethodsCache() {
        return methodsCache;
    }
}
