package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntityTree;
import java.math.BigDecimal;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyEntityUtil {

    /**
     * 获取不为 null对象
     */
    @NotNull
    public static <T> T getNotNullObject(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 获取不为 null对象，并且去掉前后空格的 字符串
     */
    @NotNull
    public static String getNotNullAndTrimStr(String str) {
        return getNotNullStr(StrUtil.trim(str), "");
    }

    /**
     * 获取不为 null对象的 字符串
     */
    @NotNull
    public static String getNotNullStr(String str) {
        return getNotNullStr(str, "");
    }

    /**
     * 如果为空，则返回默认值
     */
    @NotNull
    public static String getNotNullStr(String str, String defaultStr) {
        return StrUtil.isBlank(str) ? defaultStr : str;
    }

    /**
     * 获取不为 null对象的 parentId字符串
     */
    @NotNull
    public static Long getNotNullPid(Long aLong) {
        return getNotNullLong(aLong, TempConstant.TOP_PID);
    }

    /**
     * 如果 parentId为 0，则设置为 null
     */
    public static void handlePid(TempEntityTree<?> tempEntityTree) {

        if (tempEntityTree != null && TempConstant.TOP_PID.equals(tempEntityTree.getPid())) {
            tempEntityTree.setPid(null);
        }

    }

    /**
     * 获取不为 null对象的 BigDecimal
     */
    @NotNull
    public static BigDecimal getNotNullBigDecimal(BigDecimal bigDecimal) {
        return getNotNullBigDecimal(bigDecimal, BigDecimal.ZERO);
    }

    /**
     * 获取不为 null对象的 BigDecimal，如果为空，则返回默认值
     */
    @NotNull
    public static BigDecimal getNotNullBigDecimal(BigDecimal bigDecimal, BigDecimal defaultBigDecimal) {
        return bigDecimal == null ? defaultBigDecimal : bigDecimal;
    }

    /**
     * 获取不为 null对象的 OrderNo
     */
    @NotNull
    public static Integer getNotNullOrderNo(Integer integer) {
        return getNotNullInt(integer, 0);
    }

    /**
     * 获取不为 null对象的 Integer
     */
    @NotNull
    public static Integer getNotNullInt(Integer integer) {
        return getNotNullInt(integer, -1);
    }

    /**
     * 获取不为 null对象的 Integer，如果为空，则返回默认值
     */
    @NotNull
    public static Integer getNotNullInt(Integer integer, Integer defaultInt) {
        return integer == null ? defaultInt : integer;
    }

    /**
     * 获取不为 null对象的 Long
     */
    @NotNull
    public static Long getNotNullLong(Long aLong) {
        return getNotNullLong(aLong, -1L);
    }

    /**
     * 获取不为 null对象的 Long，如果为空，则返回默认值
     */
    @NotNull
    public static Long getNotNullLong(Long aLong, Long defaultLong) {
        return aLong == null ? defaultLong : aLong;
    }

    /**
     * number为 -1的，设置为 null
     */
    @Nullable
    public static <T> T removeNumberDefault(@Nullable T t) {

        if (t == null) {
            return null;
        }

        Map<String, Object> map = BeanUtil.beanToMap(t);

        for (Map.Entry<String, Object> item : map.entrySet()) {

            if (item.getValue() == null) {
                continue;
            }
            if ((item.getValue() instanceof Integer) && ((int)item.getValue() == -1)) {
                item.setValue(null);
                continue;
            }
            if ((item.getValue() instanceof Byte) && ((byte)item.getValue() == -1)) {
                item.setValue(null);
                continue;
            }
            if ((item.getValue() instanceof Long) && ((long)item.getValue() == -1)) {
                item.setValue(null);
            }

        }

        return BeanUtil.toBean(map, ClassUtil.getClass(t));

    }

}
