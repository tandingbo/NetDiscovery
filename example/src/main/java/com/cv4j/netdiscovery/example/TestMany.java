package com.cv4j.netdiscovery.example;

import com.cv4j.netdiscovery.core.Spider;
import com.cv4j.netdiscovery.core.SpiderEngine;
import com.cv4j.netdiscovery.core.domain.Request;
import com.cv4j.netdiscovery.core.downloader.vertx.VertxDownloader;

/**
 * 测试在大量请求下，因为爬虫提前的关闭而队列剩余大量请求无法消费的问题
 *
 * @author bdq
 * @date 2018/9/19
 */
public class TestMany {

    public static void main(String[] args) {
        Spider spider = Spider.create()
                .downloader(new VertxDownloader())
                .maxRetries(1); // 重试次数
        for (int i = 0; i < 10000; i++) {
            Request request = new Request("https://www.google.com");
            request.checkDuplicate(false);
            request.onErrorRequest(new Request.OnErrorRequest() {
                @Override
                public void process(Request request) {
                    System.out.println("process on error request!");
                }
            });
            spider.request(request);
        }
        SpiderEngine spiderEngine = SpiderEngine.create();
        spiderEngine.addSpider(spider);
        spiderEngine.httpd(9000)
                .run();
    }
}
