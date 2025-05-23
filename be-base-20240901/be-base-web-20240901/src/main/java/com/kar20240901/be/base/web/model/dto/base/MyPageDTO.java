package com.kar20240901.be.base.web.model.dto.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@Schema(description = "分页参数，查询所有：pageSize = -1，默认：current = 1，pageSize = 10")
public class MyPageDTO {

    @Schema(description = "第几页")
    private long current = 1;

    @Schema(description = "每页显示条数")
    private long pageSize = 10;

    @Schema(description = "排序字段")
    private MyOrderDTO order;

    /**
     * 判断前端是否，传递了 order字段
     */
    public boolean orderEmpty() {

        return getOrder() == null || StrUtil.isBlank(order.getName());

    }

    /**
     * 分页属性拷贝
     */
    @NotNull
    public <T> Page<T> page() {

        Page<T> page = new Page<>();

        page.setCurrent(getCurrent());
        page.setSize(getPageSize());

        return page;

    }

    /**
     * 分页属性拷贝-排序
     */
    @NotNull
    public <T> Page<T> pageOrder() {

        return pageOrder(true);

    }

    /**
     * 分页属性拷贝 toUnderlineCaseFlag：一般为 true 备注：order by 和 group by 可以使用别名，where 里面不能使用别名 注意：group by 比 order by 先执行，order
     * by 不会对 group by 内部进行排序
     */
    @NotNull
    public <T> Page<T> pageOrder(boolean toUnderlineCaseFlag) {

        Page<T> page = page();

        if (orderEmpty()) {
            return page;
        }

        // 添加 orderList里面的排序规则
        page.orders().add(orderToOrderItem(getOrder(), toUnderlineCaseFlag));

        return page;

    }

    /**
     * 自定义的排序规则，转换为 mybatis plus 的排序规则
     *
     * @param toUnderlineCaseFlag 是否：驼峰转下划线
     */
    @NotNull
    public static OrderItem orderToOrderItem(MyOrderDTO order, boolean toUnderlineCaseFlag) {

        OrderItem orderItem = new OrderItem();

        orderItem.setColumn(toUnderlineCaseFlag ? StrUtil.toUnderlineCase(order.getName()) : order.getName());

        if (StrUtil.isNotBlank(order.getValue())) {
            orderItem.setAsc("descend".equals(order.getValue()) == false);
        }

        return orderItem;

    }

    /**
     * 分页属性拷贝-增加：默认创建时间 倒序排序
     */
    @NotNull
    public <T> Page<T> createTimeDescDefaultOrderPage() {

        return createTimeDescDefaultOrderPage(true);

    }

    /**
     * 分页属性拷贝-增加：默认更新时间 倒序排序
     */
    @NotNull
    public <T> Page<T> updateTimeDescDefaultOrderPage() {

        return updateTimeDescDefaultOrderPage(true);

    }

    /**
     * 分页属性拷贝-增加：id 倒序排序
     */
    @NotNull
    public <T> Page<T> idDescDefaultOrderPage() {

        return idDescDefaultOrderPage(true);

    }

    /**
     * 分页属性拷贝-增加：默认创建时间 倒序排序
     */
    @NotNull
    public <T> Page<T> createTimeDescDefaultOrderPage(boolean toUnderlineFlag) {

        Page<T> page = pageOrder(toUnderlineFlag);

        if (orderEmpty()) {
            page.orders().add(createTimeOrderItem(toUnderlineFlag));
        }

        return page;

    }

    /**
     * 分页属性拷贝-增加：默认更新时间 倒序排序
     */
    @NotNull
    public <T> Page<T> updateTimeDescDefaultOrderPage(boolean toUnderlineFlag) {

        Page<T> page = pageOrder(toUnderlineFlag);

        if (orderEmpty()) {
            page.orders().add(updateTimeOrderItem(toUnderlineFlag));
        }

        return page;

    }

    /**
     * 分页属性拷贝-增加：id 倒序排序
     */
    @NotNull
    public <T> Page<T> idDescDefaultOrderPage(boolean toUnderlineFlag) {

        Page<T> page = pageOrder(toUnderlineFlag);

        if (orderEmpty()) {
            page.orders().add(idOrderItem());
        }

        return page;

    }

    /**
     * 分页属性拷贝-增加：任意字段 倒序排序
     */
    @NotNull
    public <T> Page<T> fieldDescDefaultOrderPage(String fieldName, boolean toUnderlineFlag) {

        Page<T> page = pageOrder(toUnderlineFlag);

        if (orderEmpty()) {
            page.orders().add(fieldOrderItem(fieldName, toUnderlineFlag));
        }

        return page;

    }

    /**
     * 获取：默认的创建时间排序
     */
    @NotNull
    public static OrderItem createTimeOrderItem(boolean toUnderlineFlag) {

        if (toUnderlineFlag) {

            return OrderItem.desc("create_time");

        }

        return OrderItem.desc("createTime");

    }

    /**
     * 获取：默认的修改时间排序
     */
    @NotNull
    public static OrderItem updateTimeOrderItem(boolean toUnderlineFlag) {

        if (toUnderlineFlag) {

            return OrderItem.desc("update_time");

        }

        return OrderItem.desc("updateTime");

    }

    /**
     * 获取：默认的 id排序
     */
    @NotNull
    public static OrderItem idOrderItem() {

        return OrderItem.desc("id");

    }

    /**
     * 获取：任意字段倒序排序
     */
    @NotNull
    public static OrderItem fieldOrderItem(String fieldName, boolean toUnderlineFlag) {

        if (toUnderlineFlag) {

            return OrderItem.desc(StrUtil.toUnderlineCase(fieldName));

        }

        return OrderItem.desc(fieldName);

    }

}
