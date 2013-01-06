package net.ds3783.chatserver.filters.gproject;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.core.InputFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-11-12
 * Time: 17:14:51
 * To change this template use File | Settings | File Templates.
 */
public class KeywordInputFilterV1 extends InputFilter {

    private Log logger = LogFactory.getLog(KeywordInputFilterV1.class);

    public String keywords = "";
    public String replaceStr = "!@#$%^&*()_-+=~?<>";
    private TreeNode keywordTree = new TreeNode();

    /*
   初始化
    */
    public void init() {
        String[] arrKeywords = this.keywords.split(",");
        for (String sKey : arrKeywords) {
            char[] chars = sKey.toCharArray();
            TreeNode currentNode = keywordTree;
            for (int i = 0; i < chars.length; i++) {
                char ch = chars[i];
                if (!currentNode.contains(ch)) {
                    currentNode.addChild(ch, i == (chars.length - 1));
                }
                currentNode = currentNode.moveNext(ch);
            }
        }
    }

    @Override
    public Message filte(Client client, Message message) {
        if (MessageType.COMMAND_MESSAGE.equals(message.getType())) {
            return message;
        }
        if (MessageType.CHAT_MESSAGE.equals(message.getType()) && "SYSTEM".equals(message.getSubType())) {
            return message;
        }
        if (message != null && message.getContent() != null && message.getContent().length() > 0) {

            String content = message.getContent();
            //查找
            List<Indexes> result = doFilte(content.toCharArray(), keywordTree);
            //替换
            char[] chars = content.toCharArray();
            for (Indexes indexes : result) {
                for (int i = indexes.getStart(); i < indexes.getEnd(); i++) {
                    chars[i] = randomChar();
                }
            }
            message.setContent(new String(chars));
        }
        return message;
    }

    private char randomChar() {
        char[] chs = replaceStr.toCharArray();
        int result = (int) (Math.random() * chs.length);
        return chs[result];
    }

    private List<Indexes> doFilte(char[] chars, TreeNode treeNode) {
        List<Indexes> result = new ArrayList<Indexes>();
        for (int i = 0; i < chars.length; i++) {
            if (isNotKeyWord(chars[i])) {
                continue;
            }
            result.addAll(next(chars, i, treeNode, null));
        }
        return result;
    }

    private boolean isNotKeyWord(char ch) {
        if (ch > 127) {
            return false;
        }
        if (ch >= 'a' && ch <= 'z') {
            return false;
        }
        if (ch >= 'A' && ch <= 'Z') {
            return false;
        }
        if (ch >= '0' && ch <= '9') {
            return false;
        }
        return true;
    }

    private List<Indexes> next(char[] chars, int pos, TreeNode treeNode, Indexes indexs) {

        if (chars != null && chars.length > pos) {
            List<Indexes> results = new ArrayList<Indexes>();
            Character c = chars[pos];

            if (isNotKeyWord(c)) {
                results.addAll(next(chars, pos + 1, treeNode, indexs));
            } else if (treeNode.contains(c)) {
                TreeNode next = treeNode.moveNext(c);
                if (next.isEnd()) {
                    indexs.setEnd(pos + 1);
                    results.add(indexs);
                    indexs = indexs.clone();
                }
                if (indexs == null) {
                    indexs = new Indexes(pos, -1);
                }
                if (pos + 1 < chars.length) {
                    results.addAll(next(chars, pos + 1, next, indexs));
                }

            }
            return results;
        } else {
            return new ArrayList<Indexes>();
        }
    }


    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    private class Indexes implements Cloneable {
        private int start;
        private int end;


        private Indexes() {
        }

        private Indexes(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }


        @Override
        protected Indexes clone() {
            return new Indexes(start, end);
        }
    }

    private class TreeNode {
        private char value;
        private boolean isEnd = false;
        private HashMap<Character, TreeNode> next = new HashMap<Character, TreeNode>();

        private TreeNode() {
        }

        public TreeNode(char ch, boolean isEnd) {
            this.value = ch;
            this.isEnd = isEnd;
        }

        public void addChild(char ch, boolean isEnd) {
            next.put(ch, new TreeNode(ch, isEnd));
        }


        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public char getValue() {
            return value;
        }

        public void setValue(char value) {
            this.value = value;
        }

        public HashMap<Character, TreeNode> getNext() {
            return next;
        }

        public void setNext(HashMap<Character, TreeNode> next) {
            this.next = next;
        }

        public boolean contains(char ch) {
            return next.containsKey(ch);
        }

        public TreeNode moveNext(char ch) {
            return next.get(ch);
        }
    }

}