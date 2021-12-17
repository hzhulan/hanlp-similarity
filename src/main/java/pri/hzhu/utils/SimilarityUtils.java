package pri.hzhu.utils;

import pri.hzhu.model.CommonException;
import pri.hzhu.model.Constants;
import pri.hzhu.model.WordFrequent;

import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/12/16 20:10
 */
public class SimilarityUtils {

    /**
     * 计算文本相似度
     * @param comparedStr
     * @param input
     * @return
     */
    public static double calSimilarity(String comparedStr, String input) {
        List<WordFrequent> wordFrequents1 = HanlpUtils.getFrequent(comparedStr);
        List<WordFrequent> wordFrequents2 = HanlpUtils.getFrequent(input);
        return calSimilarity(wordFrequents1, wordFrequents2);
    }

    /**
     * 计算相似度
     * @param firstWordFrequentList
     * @param secondWordFrequentList
     */
    public static double calSimilarity(List<WordFrequent> firstWordFrequentList, List<WordFrequent> secondWordFrequentList) {

        equalTheWords(firstWordFrequentList, secondWordFrequentList);

        return calSinSimilarity(firstWordFrequentList, secondWordFrequentList);
    }

    /**
     * 让分词都同步，但是频率会有不同，没有的频率为0
     */
    private static void equalTheWords(List<WordFrequent> firstWordFrequentList, List<WordFrequent> secondWordFrequentList) {
        // 第二个list包含所有第一个list的元素
        for (WordFrequent wordFrequent : firstWordFrequentList) {
            if (wordFrequent.getWord() == null) {
                throw new CommonException("分词不能为null");
            }

            if (!secondWordFrequentList.contains(wordFrequent)) {
                secondWordFrequentList.add(new WordFrequent(wordFrequent.getWord(), Constants.INT_0));
            }
        }

        if (firstWordFrequentList.size() < secondWordFrequentList.size()) {
            // 第一个list包含所有第二个list的元素
            for (WordFrequent wordFrequent : secondWordFrequentList) {

                if (wordFrequent.getWord() == null) {
                    throw new CommonException("分词不能为null");
                }

                if (!firstWordFrequentList.contains(wordFrequent)) {
                    firstWordFrequentList.add(new WordFrequent(wordFrequent.getWord(), Constants.INT_0));
                }
            }
        }

        firstWordFrequentList.sort(Comparator.comparing(WordFrequent::getWord));
        secondWordFrequentList.sort(Comparator.comparing(WordFrequent::getWord));
    }

    /**
     * 余弦计算相似度
     * @return
     */
    private static double calSinSimilarity(List<WordFrequent> firstWordFrequentList, List<WordFrequent> secondWordFrequentList) {

        try {
            double vProduct = 0;
            int sumSquare1 = 0;
            int sumSquare2 = 0;
            for (int i = 0; i < firstWordFrequentList.size(); ++i) {
                int firstFrequent = firstWordFrequentList.get(i).getFrequent();
                int secondFrequent = secondWordFrequentList.get(i).getFrequent();
                // 向量点积
                vProduct += firstFrequent * secondFrequent;
                // 求向量模的过程
                sumSquare1 += firstFrequent * firstFrequent;
                sumSquare2 += secondFrequent * secondFrequent;
            }


            // 两向量模的乘积
            double normProduct = Math.sqrt(sumSquare1 * sumSquare2);
            // 点积除以模乘积
            double similarity = vProduct / normProduct;

            return similarity;
        } catch (Exception e) {
            throw new CommonException("计算相似度异常", e);
        }
    }
}
