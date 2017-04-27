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
package com.afrunt.jach.metadata;

import com.afrunt.beanmetadata.FieldMetadata;
import com.afrunt.jach.annotation.*;
import com.afrunt.jach.logic.StringUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.afrunt.jach.annotation.InclusionRequirement.*;

/**
 * @author Andrii Frunt
 */
public class ACHFieldMetadata extends FieldMetadata implements Comparable<ACHFieldMetadata> {

    public boolean isACHField() {
        return achAnnotation() != null;
    }

    private ACHField achAnnotation() {
        return getAnnotation(ACHField.class);
    }

    public String getAchFieldName() {
        return achAnnotation().name();
    }

    public boolean isMandatory() {
        return inclusionIs(MANDATORY);
    }

    public boolean isOptional() {
        return inclusionIs(OPTIONAL);
    }

    public boolean isBlank() {
        return inclusionIs(BLANK);
    }

    public boolean isRequired() {
        return inclusionIs(REQUIRED);
    }

    public boolean inclusionIs(InclusionRequirement requirement) {
        if (isAnnotatedWith(Inclusion.class)) {
            return getAnnotation(Inclusion.class).value().equals(requirement);
        } else {
            return achAnnotation().inclusion().equals(requirement);
        }
    }

    public List<String> getValues() {
        if (isAnnotatedWith(Values.class)) {
            return Arrays.asList(getAnnotation(Values.class).value());
        } else {
            return Arrays.asList(achAnnotation().values());
        }
    }

    public String getDateFormat() {
        return getOptionalAnnotation(DateFormat.class)
                .map(DateFormat::value)
                .orElse(achAnnotation().dateFormat());
    }

    public boolean isTypeTag() {
        return achAnnotation().typeTag();
    }

    public int getStart() {
        return achAnnotation().start();
    }

    public int getLength() {
        return achAnnotation().length();
    }

    public int getEnd() {
        return getStart() + getLength();
    }


    public boolean valueSatisfies(String value) {
        if (isBlank()) {
            return "".equals(value.trim());
        } else if (hasConstantValues()) {
            return valueSatisfiesToConstantValues(value);
        } else {
            return valueSatisfiesToFormat(value);
        }
    }

    public boolean valueSatisfiesToFormat(String value) {
        if (value.length() != getLength()) {
            return false;
        }

        if (isString()) {
            return true;
        } else if (isNumber()) {

            if (isOptional() && StringUtil.isBlank(value)) {
                return true;
            }

            if (!isFractional() && value.contains(".")) {
                return false;
            }

            try {
                if (BigDecimal.ZERO.compareTo(new BigDecimal(value.trim())) == 1) {
                    // Numbers should be positive or zero
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        } else if (isDate()) {
            SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
            try {
                sdf.parse(value);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean valueSatisfiesToConstantValues(String value) {
        if (hasConstantValues()) {
            return getConstantValues().contains(value) || (isOptional() && "".equals(value.trim()));
        } else {
            return true;
        }
    }

    public boolean hasConstantValues() {
        return getValues().size() > 0;
    }

    public Set<String> getConstantValues() {
        return Collections.unmodifiableSet(new HashSet<>(getValues()));
    }

    public int getDigitsAfterComma() {
        return 2;
    }

    public List<String> getPossibleValues() {
        List<String> result = new ArrayList<>();

        result.addAll(getConstantValues());

        if (result.isEmpty()) {
            if (isString()) {
                result.add("Any string value with maximum length of " + getLength());
            } else if (isNumber()) {
                result.add("Any numeric value with maximum length of " + getLength());
            } else if (isDate()) {
                result.add("Any date value with with pattern " + getDateFormat());
            } else {
                result.add("DEADBEAF");
            }
        }

        return result;
    }


    @Override
    public int compareTo(ACHFieldMetadata o) {
        return Integer.valueOf(getStart()).compareTo(o.getStart());
    }
}
