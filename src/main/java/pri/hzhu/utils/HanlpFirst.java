package pri.hzhu.utils;

import java.io.IOException;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/12/16 19:34
 */
public class HanlpFirst {

    public static void main(String[] args) throws IOException {
        double similarity = SimilarityUtils.calSimilarity("桑塔纳2020怀旧豪华型", "桑塔纳2020怀旧豪华款");
        System.out.println(similarity);
        double similarity2 = SimilarityUtils.calSimilarity("桑塔纳2021怀旧豪华型", "桑塔纳2020怀旧豪华款");
        System.out.println(similarity2);
    }
}
