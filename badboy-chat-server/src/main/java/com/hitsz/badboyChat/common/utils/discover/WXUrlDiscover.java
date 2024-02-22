package com.hitsz.badboyChat.common.utils.discover;

import org.jsoup.nodes.Document;

import javax.annotation.Nullable;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/20 15:50
 */
public class WXUrlDiscover extends AbstractUrlDiscover {
    @Nullable
    @Override
    public String getTitle(Document document) {
        return document.getElementsByAttributeValue("property", "og:title").attr("content");
    }

    @Nullable
    @Override
    public String getDescription(Document document) {
        return document.getElementsByAttributeValue("property", "og:description").attr("content");
    }

    @Nullable
    @Override
    public String getImage(String url, Document document) {
        String href = document.getElementsByAttributeValue("property", "og:image").attr("content");
        return isConnect(href) ? href : null;
    }
}
