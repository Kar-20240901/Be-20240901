# 2026-05-13 14:21:00
CREATE INDEX idx_belong_id ON be_base_20240901.base_im_friend (belong_id, friend_id, id DESC);

CREATE INDEX idx_target_user ON be_base_20240901.base_im_session_ref_user (target_id, user_id, session_id);

