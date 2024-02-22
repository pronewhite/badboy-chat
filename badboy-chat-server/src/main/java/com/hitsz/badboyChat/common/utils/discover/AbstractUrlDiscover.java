package com.hitsz.badboyChat.common.utils.discover;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.hitsz.badboyChat.common.chat.domain.dto.UrlInfo;
import com.hitsz.badboyChat.common.utils.FutureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.util.Pair;

import javax.annotation.Nullable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/20 12:54
 */
@Slf4j
public abstract class AbstractUrlDiscover implements UrlDiscover{

    //链接识别的正则
    private static final Pattern PATTERN = Pattern.compile("((http|https)://)?(www.)?([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?");

    @Nullable
    @Override
    public Map<String, UrlInfo> getContentMap(String content) {
        if(StringUtils.isBlank(content)){
            return new HashMap<>();
        }
        // findAll表示从给定内容中根据正则表达式匹配出对应的内容，参数group为0表示整个正则表达式匹配的内容，为1表示第一个括号内的匹配内容，以此类推
        List<String> urlList = ReUtil.findAll(PATTERN, content, 0);
        // 并行访问链接获取UrlInfo
        List<CompletableFuture<Pair<String, UrlInfo>>> futures = urlList.stream().map(url ->
                CompletableFuture.supplyAsync(() -> {
                    UrlInfo urlInfo = getContent(url);
                    return Objects.nonNull(urlInfo) ? Pair.of(url, urlInfo) : null;
                })
        ).collect(Collectors.toList());
        // 定义了一系列的步骤等待执行
        CompletableFuture<List<Pair<String, UrlInfo>>> future = FutureUtil.sequenceNonNull(futures);
        // join方法的调用确保所有的异步操作（即获取UrlInfo）都执行完成之后再组装结果，这里当前线程会阻塞
        return future.join().stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (a,b) -> a));
    }

    @Override
    public UrlInfo getContent(String url) {
        Document document = getUrlDocument(assemable(url));
        if(Objects.isNull(document)){
            return null;
        }
        return UrlInfo.builder()
                .image(getImage(url,document))
                .title(getTitle(document))
                .description(getDescription(document))
                .build();
    }

    private Document getUrlDocument(String matchUrl) {
        try {
            Connection connect = Jsoup.connect(matchUrl);
            connect.timeout(2000);
            return connect.get();
        } catch (Exception e) {
            log.error("find error:url:{}", matchUrl, e);
        }
        return null;
    }

    private String assemable(String url) {
        if (!StrUtil.startWith(url, "http")) {
            return "http://" + url;
        }

        return url;
    }

    /**
     * 判断链接是否有效
     * @param href
     * @return
     */
    protected Boolean isConnect(String href){
        URL url;
        int state;
        //下载链接类型
        String fileType;
        try {
            url = new URL(href);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            state = httpURLConnection.getResponseCode();
            fileType = httpURLConnection.getHeaderField("Content-Disposition");
            //如果成功200，缓存304，移动302都算有效链接，并且不是下载链接
            if ((state == 200 || state == 302 || state == 304) && fileType == null) {
                return true;
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
