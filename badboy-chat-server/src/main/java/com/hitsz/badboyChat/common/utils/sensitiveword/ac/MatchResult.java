package com.hitsz.badboyChat.common.utils.sensitiveword.ac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author badboy
 * @version 1.0
 * Create by 2024/2/22 16:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchResult {

    private Integer startIndex;

    private Integer endIndex;
}
