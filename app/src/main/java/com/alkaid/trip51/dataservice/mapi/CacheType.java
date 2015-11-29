package com.alkaid.trip51.dataservice.mapi;

/**
 * Created by alkaid on 2015/11/16.
 * 为volley框架添加缓存类型
 */
public enum CacheType {
    /**禁用缓存*/
    DISABLED,
    /**普通，即一直存到缓存上限时才清理*/
    NORMAL,
    /**1小时*/
    HOURLY,
    /**1天*/
    DAILY,
    /**由服务端response head控制*/
    SERVICE/*,
    CRITICAL*/
}
