# 2026-05-13 14:21:00
CREATE INDEX idx_belong_id ON be_base_20240901.base_im_friend (belong_id, friend_id, id DESC);

CREATE INDEX idx_target_user ON be_base_20240901.base_im_session_ref_user (target_id, user_id, session_id);

# 2026-05-22 17:00:00
ALTER TABLE be_base_20240901.base_im_session_ref_user
    ADD enable_flag tinyint(1) NOT NULL COMMENT '是否可用：在删除好友、退出群聊、被踢出群聊之后变化';