package pri.hzhu.model;

import java.util.Objects;

/**
 * @description: 分词出现频率
 * @author: pp_lan
 * @date: 2021/12/16 20:11
 */
public class WordFrequent {

    /**
     * 分词
     */
    private String word;

    /**
     * 出现频率
     */
    private int frequent;

    public WordFrequent() {
    }

    public WordFrequent(String word, int frequent) {
        this.word = word;
        this.frequent = frequent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WordFrequent that = (WordFrequent) o;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    //region setter and getter

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequent() {
        return frequent;
    }

    public void setFrequent(int frequent) {
        this.frequent = frequent;
    }
    //endregion
}
