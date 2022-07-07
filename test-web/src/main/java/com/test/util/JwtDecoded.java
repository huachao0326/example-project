package com.test.util;

import lombok.Builder;
import lombok.Data;

/**
 * 功能描述:
 *
 * @author huachao
 * @create 2022/3/30 18:10
 * @since 1.0
 */
@Data
public class JwtDecoded {

    private String alg;

    private String pubKey;

    private String data;

    @Builder
    public JwtDecoded(String alg, String pubKey, String data) {
        this.alg = alg;
        this.pubKey = pubKey;
        this.data = data;
    }
}
