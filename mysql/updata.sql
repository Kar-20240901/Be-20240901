# 2026-05-06 14:09:00
CREATE INDEX idx_user_show_session ON be_base_20240901.base_im_session_content_ref_user (user_id, show_flag, session_id, content_id);

CREATE INDEX idx_user_show_order ON be_base_20240901.base_im_session_ref_user (user_id, show_flag, order_no DESC, session_id DESC);
CREATE INDEX idx_session_enable_create_ts ON be_base_20240901.base_im_session_content (session_id, enable_flag, create_ts DESC);
CREATE INDEX idx_session_user ON be_base_20240901.base_im_session_ref_user (session_id, user_id);

ALTER TABLE be_base_20240901.base_im_session_ref_user
    DROP INDEX base_im_session_ref_user_user_id_IDX;
ALTER TABLE be_base_20240901.base_im_session_content
    DROP INDEX base_im_session_content_create_ts_IDX;
ALTER TABLE be_base_20240901.base_im_session_ref_user
    DROP INDEX base_im_session_ref_user_session_id_IDX;
ALTER TABLE be_base_20240901.base_im_session
    DROP INDEX base_im_session_last_receive_ts_IDX;
ALTER TABLE be_base_20240901.base_im_session
    DROP COLUMN last_receive_ts;

# 2026-04-17 10:47:00
ALTER TABLE be_base_20240901.base_im_session_content
    DROP INDEX create_ts;
CREATE INDEX base_im_session_content_create_ts_IDX USING BTREE ON be_base_20240901.base_im_session_content (create_ts, order_no, id);
CREATE INDEX base_im_session_ref_user_session_id_IDX USING BTREE ON be_base_20240901.base_im_session_ref_user (session_id);
CREATE INDEX base_im_session_last_receive_ts_IDX USING BTREE ON be_base_20240901.base_im_session (last_receive_ts);


# 2026-04-16 11:06:00
ALTER TABLE be_base_20240901.base_im_session_ref_user
    ADD order_no INT NOT NULL COMMENT '排序号（值越大越前面，默认为 0） 901 置顶';
