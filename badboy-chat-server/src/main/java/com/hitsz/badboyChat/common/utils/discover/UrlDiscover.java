package com.hitsz.badboyChat.common.utils.discover;

import com.hitsz.badboyChat.common.chat.domain.dto.UrlInfo;
import org.jsoup.nodes.Document;

import java.util.Map;

public interface UrlDiscover {

    /**
     * 获取链接对应的标题，logo，描述信息，并组装相应的映射关系
     * @param content
     * @return
     */
    Map<String, UrlInfo> getContentMap(String content);

    /**
     * 获取url对应的标题，logo，描述信息
     * @param url
     * @return
     */
    UrlInfo getContent(String url);

    /**
     * 获取链接对应的标题
     * @param document
     * @return
     */
    String getTitle(Document document);

    /**
     *  获取链接对应的logo
     * @param document
     * @return
     */
    String getImage(String url,Document document);

    /**
     *  获取链接对应的描述信息
     * @param document
     * @return
     */
    String getDescription(Document document);

}
