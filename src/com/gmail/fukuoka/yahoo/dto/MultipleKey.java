package com.gmail.fukuoka.yahoo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class MultipleKey {
    /** キー1 */
    private final String strKey1;
    /** キー2 */
    private final BigDecimal bigKey2;
    /** キー3 */
    private final Boolean booKey3;
}