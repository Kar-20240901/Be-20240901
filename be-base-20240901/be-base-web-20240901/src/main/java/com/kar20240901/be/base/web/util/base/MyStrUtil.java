package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import java.util.function.Consumer;

public class MyStrUtil {

    /**
     * 限制字符串长度，如果超过指定长度，截取指定长度
     */
    public static String maxLength(CharSequence string, int length) {

        Assert.isTrue(length > 0);

        if (null == string) {
            return null;
        }

        if (string.length() <= length) {
            return string.toString();
        }

        return StrUtil.sub(string, 0, length);

    }

    /**
     * 根据最大长度，拆分字符串，然后把拆分结果多次执行：consumer
     *
     * @return 截取的次数
     */
    public static int subWithMaxLengthAndConsumer(String content, int maxLength, Consumer<String> consumer) {

        int subCount = 0;

        if (StrUtil.isBlank(content)) {
            return subCount;
        }

        int length = content.length() / maxLength;

        if (length == 0) {

            consumer.accept(content);

        } else {

            for (int i = 0; i < length + 1; i++) {

                String subStr = StrUtil.subWithLength(content, i * maxLength, maxLength);

                if (StrUtil.isBlank(subStr)) {
                    break;
                }

                subCount = subCount + 1;

                consumer.accept(subStr);

            }

        }

        return subCount;

    }

    /**
     * 根据最大字节，拆分字符串，然后把拆分结果多次执行：consumer
     *
     * @return 截取的次数
     */
    public static int subWithMaxByteLengthAndConsumer(String content, int maxByteLength, Consumer<String> consumer) {

        int subCount = 0;

        if (StrUtil.isBlank(content)) {
            return subCount;
        }

        int byteLength = StrUtil.utf8Bytes(content).length;

        if (byteLength <= maxByteLength) {

            consumer.accept(content);

        } else {

            StrBuilder strBuilder = StrUtil.strBuilder();

            int currentByteLength = 0; // 当前：字节长度

            for (int i = 0; i < content.length(); i++) {

                char charAt = content.charAt(i);

                int checkByteLength = StrUtil.utf8Bytes(String.valueOf(charAt)).length;

                currentByteLength = currentByteLength + checkByteLength;

                if (currentByteLength > maxByteLength) {

                    consumer.accept(strBuilder.toStringAndReset());

                    subCount = subCount + 1;

                    strBuilder.append(charAt);

                    currentByteLength = checkByteLength;

                } else {

                    strBuilder.append(charAt);

                }

            }

            String str = strBuilder.toStringAndReset();

            if (StrUtil.isNotBlank(str)) {

                consumer.accept(str);

                subCount = subCount + 1;

            }

        }

        return subCount;

    }

}
