# 2026-04-16 11:06:00
ALTER TABLE be_base_20240901.base_im_session_ref_user
    ADD order_no INT NOT NULL COMMENT '排序号（值越大越前面，默认为 0） 901 置顶';
