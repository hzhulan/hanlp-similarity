package pri.hzhu.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import pri.hzhu.model.CommonException;
import pri.hzhu.model.Constants;
import pri.hzhu.model.WordFrequent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/12/16 20:41
 */
public class HanlpUtils {

    private static final String STOP_WORD_FILE = "src/main/resources/config/stop_word.txt";

    private static final List<String> STOP_WORD_LIST = new ArrayList<>();

    static {
        String file = "src/main/resources/config/stop_word.txt";


        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String temp = null;
            while ((temp = br.readLine()) != null) {
                STOP_WORD_LIST.add(temp.trim());
            }
        } catch (Exception e) {
            throw new CommonException("加载停用词异常", e);
        }
    }

    private static String removeIgnoreWord(String text) {
        for (String s : STOP_WORD_LIST) {
            text = text.replaceAll(s, "");
        }
        return text;
    }

    /**
     * 获取字符串的分词词频
     * @param text
     * @return
     */
    public static List<WordFrequent> getFrequent(String text) {
        text = removeIgnoreWord(text);
        List<Term> segmentList = getSegmentList(text);
//        List<Term> segmentList = getStandardSegmentList(text);
//        List<Term> segmentList = getDatSegmentList(text);
//        List<Term> segmentList = getNshortSegmentList(text);
        return getWordFrequency(segmentList);
    }

    /**
     * 使用标准分词器，将输入的字符串分词处理。
     *
     * @param text 文本
     * @return 切分后的单词
     */
    private static List<Term> getSegmentList(String text) {

        List<Term> segmentList = HanLP.segment(text);

        return dealTerm(segmentList);
    }

    private static List<Term> getDatSegmentList(String text) {
        Segment segment = HanLP.newSegment("dat");
        List<Term> segmentList = segment.seg(text.toCharArray());
        return segmentList;
    }

    private static List<Term> getNshortSegmentList(String text) {
        HanLP.Config.CoreStopWordDictionaryPath = STOP_WORD_FILE;
        Segment segment = HanLP.newSegment("nshort");
        List<Term> segmentList = segment.seg(text.toCharArray());
        return segmentList;
    }

    private static List<Term> dealTerm(List<Term> segmentList) {
        // 过滤器
        segmentList.removeIf(new Predicate<Term>() {
            /**
             * 过滤掉：长度为1的分词、标点符号
             */
            @Override
            public boolean test(Term term) {
                boolean flag = false;
                // 长度
                String real = term.word.trim();
                if (real.length() <= Constants.INT_1) {
                    flag = true;
                }

                // 词性以w开头的，为各种标点符号
                if (term.nature.startsWith(Constants.CHAR_W)) {
                    flag = true;
                }
                // 过滤掉代码
                if (term.nature.equals(Nature.nx)) {// 字母专名
                    flag = true;
                }
                return flag;
            }
        });
        return segmentList;
    }

    /**
     * 根据分词集合统计词频
     * @param segmentList 词频集合
     */
    private static List<WordFrequent> getWordFrequency(List<Term> segmentList) {
        List<WordFrequent> frequentList = new ArrayList<>();

        Map<String, List<Term>> countMap = segmentList.stream().collect(Collectors.groupingBy(t -> t.word));
        for (Map.Entry<String, List<Term>> entry : countMap.entrySet()) {
            String word = entry.getKey();
            List<Term> value = entry.getValue();

            frequentList.add(new WordFrequent(word, value == null ? Constants.INT_0 : value.size()));
        }

        return frequentList;
    }

}
