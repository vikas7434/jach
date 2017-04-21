package com.afrunt.jach.domain;

import java.util.Arrays;

import static com.afrunt.jach.domain.RecordTypes.Constants.*;

/**
 * @author Andrii Frunt
 */
public enum RecordTypes {
    FILE_HEADER(FILE_HEADER_RECORD_TYPE_CODE), BATCH_HEADER(BATCH_HEADER_RECORD_TYPE_CODE),
    ENTRY_DETAIL(ENTRY_DETAIL_RECORD_TYPE_CODE), ADDENDA(ADDENDA_RECORD_TYPE_CODE),
    BATCH_CONTROL(BATCH_CONTROL_RECORD_TYPE_CODE), FILE_CONTROL(FILE_CONTROL_RECORD_TYPE_CODE);

    private String recordTypeCode;

    RecordTypes(String recordTypeCode) {
        this.recordTypeCode = recordTypeCode;
    }

    public String getRecordTypeCode() {
        return recordTypeCode;
    }

    public boolean is(String string) {
        return string.startsWith(recordTypeCode);
    }

    public static boolean validRecordTypeCode(String recordTypeCode) {
        return Arrays.stream(RecordTypes.values())
                .anyMatch(rt -> rt.getRecordTypeCode().equals(recordTypeCode));
    }

    public static class Constants {
        public static final String FILE_HEADER_RECORD_TYPE_CODE = "1";
        public static final String BATCH_HEADER_RECORD_TYPE_CODE = "5";
        public static final String ENTRY_DETAIL_RECORD_TYPE_CODE = "6";
        public static final String ADDENDA_RECORD_TYPE_CODE = "7";
        public static final String BATCH_CONTROL_RECORD_TYPE_CODE = "8";
        public static final String FILE_CONTROL_RECORD_TYPE_CODE = "9";
    }
}
