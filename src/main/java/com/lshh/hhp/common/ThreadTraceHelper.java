package com.lshh.hhp.common;

public class ThreadTraceHelper {
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<String>();
    private static final ThreadLocal<String> PARENT_TRACE_ID = new ThreadLocal<String>();

    public static void setTraceId(String id) {
        TRACE_ID.set(id);
    }

    public static void removeTraceId() {
        TRACE_ID.remove();
    }

    public static String getTraceId() {
        return TRACE_ID.get();
    }


    public static void setParentTraceId(String id) {
        PARENT_TRACE_ID.set(id);
    }

    public static void removeParentTraceId() {
        PARENT_TRACE_ID.remove();
    }

    public static String getParentTraceId() {
        return PARENT_TRACE_ID.get();
    }


}
