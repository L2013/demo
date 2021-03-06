package com.yinuo.demo.server.mgt.consts;

/**
 * @author liang
 * 所有的字面值尽量都定义常量，这样后续如果改名则可以在集中地方改；而如果常量名要重命名也更方便（利用IDEA）
 * 可以避免查找替换的痛苦
 */
public class PathConstant {
    public static final String USER_FIND = "user/find";
    public static final String PAGE_INDEX = "index";
    public static final String PAGE_INDEX_ORIGIN = "index/origin";
    public static final String DOWNLOAD = "download";
    public static final String DOWNLOAD_FROM_REMOTE = "downloadFromRemote";
    public static final String LUCENE = "lucene";
}
