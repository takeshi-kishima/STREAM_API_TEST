package com.gmail.fukuoka.yahoo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gmail.fukuoka.yahoo.dto.MultipleKey;
import com.gmail.fukuoka.yahoo.dto.TestRecodeDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * メインクラス
 */
public class STREAM_API_TEST {
    final static Logger logger = LoggerFactory.getLogger(STREAM_API_TEST.class);
    /**
     *
     * @param args 起動引数
	 * 
     */
    public static void main(String[] args) {

        // Streamにて複数のキーでグルーピングや合計を求める場合、複数キーのBeanをつかってやってみるテスト
        // ############## ↓ 準備 ↓ ##############
        int size = 18;
        List<TestRecodeDto> dataList = new ArrayList<>(18);
        for (int i=0; i<size; i++) {
            TestRecodeDto recode = new TestRecodeDto();
            // いろいろやってますが、3つのキーをユニークにしてるだけ…
            System.out.println(Math.ceil((i+1)/6D));
            System.out.println((i % 3) + 1);
            System.out.println(Math.ceil((i+1)/3D));

            if (Math.ceil((i+1)/6D) == 1) {
                recode.setStrKey1("A");
            } else if (Math.ceil((i+1)/6D) == 2) {
                recode.setStrKey1("B");
            } else if (Math.ceil((i+1)/6D) == 3) {
                recode.setStrKey1("C");
            } else {
                recode.setStrKey1("XXX");
            }
            recode.setBigKey2(new BigDecimal((i % 3) + 1));
            recode.setBooKey3(Math.ceil((i+1)/3D) % 2 != 0);

            recode.setLabel("ラベル:" + recode.getStrKey1() + ":" + recode.getBigKey2() + ":" + recode.getBooKey3());
            recode.setValue(new BigDecimal(i));

            dataList.add(recode);
        }
        // ############## ↑ 準備 ↑ ##############

        // 絞り込み
        List<TestRecodeDto> filteredList = dataList.stream()
        // 10よりでかい
        .filter(dto -> BigDecimal.TEN.compareTo(Optional.ofNullable(dto.getValue()).orElse(BigDecimal.ZERO)) <= 0)
        // 100よりちいさい
		.filter(dto -> new BigDecimal(100).compareTo(Optional.ofNullable(dto.getValue()).orElse(BigDecimal.ZERO)) >= 0)
        .collect(Collectors.toList());

        // 一つを特定
        TestRecodeDto result = dataList.stream()
        // 10よりでかい
        .filter(dto -> BigDecimal.TEN.compareTo(Optional.ofNullable(dto.getValue()).orElse(BigDecimal.ZERO)) <= 0)
        // 100よりちいさい
		.filter(dto -> new BigDecimal(100).compareTo(Optional.ofNullable(dto.getValue()).orElse(BigDecimal.ZERO)) >= 0)
		// key1 DESC, key2 DESC (NULLは最後に)
		.sorted(
            Comparator.comparing(TestRecodeDto::getStrKey1, Comparator.nullsLast(Comparator.reverseOrder()))
            .thenComparing(TestRecodeDto::getBigKey2, Comparator.nullsLast(Comparator.reverseOrder()))
        )
		// の先頭を採用する
        .findFirst().orElseGet(() -> null);
        
        // SUM
        BigDecimal gigSum = dataList.stream().reduce(BigDecimal.ZERO, (bdValue, dto) -> bdValue.add(Optional.ofNullable(dto.getValue()).orElse(BigDecimal.ZERO)), (bdResult1, bdResult2) -> bdResult1.add(bdResult2));
        // GROUP BY
        final Map<MultipleKey, List<TestRecodeDto>> groupByMap = dataList.stream().collect(Collectors.groupingBy(dto -> new MultipleKey(dto.getStrKey1(), dto.getBigKey2(), dto.getBooKey3())));
        // GROUP BY SUM
        final Map<MultipleKey, BigDecimal> sumMap = dataList.stream().collect(Collectors.groupingBy(dto -> new MultipleKey(dto.getStrKey1(), dto.getBigKey2(), dto.getBooKey3()), Collectors.reducing(BigDecimal.ZERO, dto -> Optional.ofNullable(dto.getValue()).orElse(BigDecimal.ZERO), (x, y) -> x.add(y))));

        for (MultipleKey key : groupByMap.keySet()) {
            logger.info(key.toString());
            logger.info(sumMap.get(key).toString());
        }
    }
}
