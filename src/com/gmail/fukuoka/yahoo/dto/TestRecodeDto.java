package com.gmail.fukuoka.yahoo.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TestRecodeDto {
	/** キー1 */
    private String strKey1;
    /** キー2 */
    private BigDecimal bigKey2;
    /** キー3 */
    private Boolean booKey3;

    /** ラベル */
    private String label;
    /** 値 */
    private BigDecimal value;
}