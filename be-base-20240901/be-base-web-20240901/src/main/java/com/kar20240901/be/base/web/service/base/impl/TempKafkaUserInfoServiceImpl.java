package com.kar20240901.be.base.web.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.kafka.TempKafkaUserInfoMapper;
import com.kar20240901.be.base.web.model.domain.kafka.TempKafkaUserInfoDO;
import com.kar20240901.be.base.web.service.base.TempKafkaUserInfoService;
import org.springframework.stereotype.Service;

@Service
public class TempKafkaUserInfoServiceImpl extends ServiceImpl<TempKafkaUserInfoMapper, TempKafkaUserInfoDO>
    implements TempKafkaUserInfoService {
}
