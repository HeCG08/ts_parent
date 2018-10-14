package cn.rumoss.ts.qa.client.impl;

import cn.rumoss.ts.entity.Result;
import cn.rumoss.ts.entity.StatusCode;
import cn.rumoss.ts.qa.client.LabelClient;
import org.springframework.stereotype.Component;

/**
 * LabelClient的失败处理类
 */
@Component
public class LabelClientImpl implements LabelClient {
    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.REMOTEERROR,"熔断器开启了...");
    }
}
