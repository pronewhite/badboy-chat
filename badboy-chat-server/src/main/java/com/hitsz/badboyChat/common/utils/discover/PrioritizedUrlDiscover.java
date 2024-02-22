package com.hitsz.badboyChat.common.utils.discover;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/20 15:51
 */
public class PrioritizedUrlDiscover extends AbstractUrlDiscover {

    private List<UrlDiscover> urlDiscoverList = new ArrayList<>();

    public PrioritizedUrlDiscover() {
        urlDiscoverList.add(new CommonUrlDiscover());
        urlDiscoverList.add(new WXUrlDiscover());
    }

    @Override
    public String getTitle(Document document) {
        for (UrlDiscover urlDiscover : urlDiscoverList) {
            String title = urlDiscover.getTitle(document);
            if (title != null) {
                return title;
            }
        }
        return null;
    }

    @Override
    public String getImage(String url, Document document) {
        for (UrlDiscover urlDiscover : urlDiscoverList) {
            String image = urlDiscover.getImage(url, document);
            if (image != null) {
                return image;
            }
        }
        return null;
    }


    @Override
    public String getDescription(Document document) {
        for (UrlDiscover urlDiscover : urlDiscoverList) {
            String description = urlDiscover.getDescription(document);
            if (description != null) {
                return description;
            }
        }
        return null;
    }
}
