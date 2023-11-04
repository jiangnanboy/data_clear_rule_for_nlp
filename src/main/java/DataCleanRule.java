import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @date 2023/11/4 21:59
 */
public class DataCleanRule {

    /**
     * 1.filter data with english title
     * @param text
     */
    public static boolean isEnglish(String text) {
        var flag = true;
        for(var i = 0; i < text.length(); i++) {
            var c = text.charAt(i);
            if(isChinese(c)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        return c >= '\u4e00' && c <= '\u9fa5';
    }

    /**
     * 2.filter empty data
     * @param text
     */
    public static boolean isBlank(String text) {
        return StringUtils.isBlank(text);
    }

    /**
     * 3.filter the data that contains less than 10 characters
     * @param text
     * @param size
     */
    public static boolean isLessThanSize(String text, int size) {
        return text.length() <= size;
    }

    /**
     * @param text
     * @return
     */
    public static boolean isLessThanSize(String text) {
        return isLessThanSize(text, 10);
    }

    /**
     * 4.filter the data that contains less than 50% Chinese characters
     * @param text
     * @param percent
     */
    public static boolean isLessThanPercent(String text, double percent) {
        var flag = true;
        var regex = "[\\u4e00-\\u9fa5]";
        var result = findAllMatch(regex, text);
        if(1.0 * result.length() / text.length() >= percent) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public static boolean isLessThanPercent(String text) {
        return isLessThanPercent(text, 0.5);
    }

    /**
     * 5.the ratio of Chinese, English and numbers is too low
     * @param text
     * @param percent
     * @return
     */
    public static boolean isLessThanPercentChineseEnglishNumber(String text, double percent) {
        var flag = true;
        var regexChinese = "[\\u4e00-\\u9fa5]";
        var regexEnglish = "[a-zA-Z]";
        var regexNum = "[0-9]+";
        var resultChinese = findAllMatch(regexChinese, text);
        var resultEnglish = findAllMatch(regexEnglish, text);
        var resultNum = findAllMatch(regexNum, text);
        if(1.0 * (resultChinese.length() + resultEnglish.length() + resultNum.length()) / text.length() >= percent) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public static boolean isLessThanPercentChineseEnglishNumber(String text) {
        return isLessThanPercentChineseEnglishNumber(text, 0.4);
    }

    /**
     * 6.too much or too little punctuation
     * @param text
     */
    public static boolean isMuchOrLittlePunctuation(String text, double littlePercent, double muchPercent) {
        var flag = true;
        var punctuation = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~。？！，、；：：”“（）——……《 》▪—";
        List<String> punctuationList = Arrays.stream(punctuation.split("")).collect(Collectors.toList());
        long punctuationCount = Arrays.stream(text.split(""))
                .filter(word -> punctuationList.contains(word))
                .count();
        if((1.0 * punctuationCount / text.length() >= littlePercent) || (1.0 * punctuationCount / text.length() <= muchPercent)) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public static boolean isMuchOrLittlePunctuation(String text) {
        return isMuchOrLittlePunctuation(text, 0.005, 0.5);
    }

    /**
     * 7.whether the last token of a paragraph is a punctuation
     * @param text
     */
    public static boolean isNotLastTokenPunctuation(String text, String[] punctuation) {
        var lastToken = text.substring(text.length() - 1).trim();
        var flag = true;
        if(StringUtils.equalsAny(lastToken, punctuation)) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param text
     * @return
     */
    public static boolean isLastTokenPunctuation(String text) {
        return isNotLastTokenPunctuation(text, new String[]{"。", ".", "!", "！", "?", "？", "……", "…"});
    }

    /**
     * 8.whether the content contains ID card number, mobile number, telephone number , email address, url, ip etc
     * @param text
     * @return
     */
    public static boolean isIDPhoneEMailUrlIp(String text) {
        var flag = false;
        var urlRegex = "(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        var emailRegex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        var idRegex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        var mobileRegex = "(\\+\\d+)?1[3458]\\d{9}$";
        var telephoneRegex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        var ipRegex = "\\d+.\\d+.\\d+.\\d+";
        if(isMatch(urlRegex, text) || isMatch(emailRegex, text) || isMatch(idRegex, text) ||
           isMatch(mobileRegex, text) || isMatch(telephoneRegex, text) || isMatch(ipRegex, text)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 9.for consecutive special symbols, the character is deleted
     * @param text
     * @return
     */
    public static String consecutiveSpecialSymbol(String text) {
        var consecutiveRegex = "[-]{4,}|[.]{4,}|[=]{4,}";
        return matchReplacement(consecutiveRegex, text);
    }

    /**
     * 10.if a special symbol has no meaning, only characters are deleted
     * @param text
     * @return
     */
    public static String specialSymbolNoMeaning(String text) {
        var specialRegex = "[\\¤\\⌒\\々\\〓\\▌\\◇\\▲\\△\\▪\\★\\◆\\▼\\●\\▽\\◁\\☆\\○]";
        return matchReplacement(specialRegex, text);
    }

    /**
     * @param regex
     * @param str
     * @return
     */
    public static String findAllMatch(String regex, String str) {
        var sb = new StringBuilder();
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(str);
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    /**
     * @param regex
     * @param str
     * @return
     */
    public static boolean isMatch(String regex, String str) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @param regex
     * @param str
     * @return
     */
    public static String matchReplacement(String regex, String str) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

}


