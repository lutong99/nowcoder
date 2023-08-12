package org.example.nowcoder.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class SensitiveFilter {

    public static final String REPLACEMENT = "***";
    private final TrieNode root;

    @Value("${community.sensitive.file.path}")
    private String sensitiveFilePath;

    private SensitiveFilter() {
        root = new TrieNode();
    }

    @PostConstruct
    public void init() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(sensitiveFilePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))
        ) {
            String keyword;
            while ((keyword = bufferedReader.readLine()) != null) {
                addKeyword2Tree(keyword);
            }
        } catch (IOException e) {
            log.error("读取敏感词文件失败: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤字符串
     * @return 过滤后的字符串
     */
    public String filter(String text) {

        if (StringUtils.isBlank(text)) {
            return null;
        }

        StringBuilder filtered = new StringBuilder();
        // 三个指针
        TrieNode temp = root;
        int begin = 0;
        int position = 0;
        int textLen = text.length();
        char[] chars = text.toCharArray();
        while (position < textLen) {
            char c = chars[position];
            if (isSymbol(c)) {
                if (temp == root) {
                    filtered.append(c);
                    begin++;
                }
                position++;
                continue; // 如果是符号就继续，符号在敏感词中间就不加入(continue，在中间和敏感词一块儿替换)，在敏感词外就加入
            }

            temp = temp.getSubNode(c);
            if (temp == null) {
                filtered.append(chars[begin]); // 以Begin 开头的字符串不是敏感词，进行begin下一个字符开头的词的判断
                position = ++begin;
                temp = root;
            } else {
                if (temp.isEnd) {
                    filtered.append(REPLACEMENT);
                    begin = ++position;
                    temp = root; // 新一轮敏感词判定
                } else {
                    position++;
                }
            }
        }

        while (begin < position) {
            filtered.append(chars[begin++]);
        }

        return filtered.toString();
    }

    private boolean isSymbol(char c) {
        // 0x2e80 ~ 0x9FFF 是东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private void addKeyword2Tree(String keyword) {
        int len = keyword.length();
        char[] chars = keyword.toCharArray();
        TrieNode temp = root;
        for (int i = 0; i < len; i++) {
            char c = chars[i];
            TrieNode subNode = temp.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                temp.putSubNode(c, subNode);
            }
            temp = subNode;
            if (i == len - 1) {
                temp.setEnd(true);
            }
        }
    }

    private static class TrieNode {

        private final Map<Character, TrieNode> nodes;
        private boolean isEnd;

        public TrieNode() {
            nodes = new HashMap<>();
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public TrieNode getSubNode(Character key) {
            return nodes.get(key);
        }

        public void putSubNode(Character key, TrieNode subNode) {
            nodes.put(key, subNode);
        }
    }

}
