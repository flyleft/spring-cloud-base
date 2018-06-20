package me.flyleft.choerodon.demo;

import io.choerodon.core.swagger.ChoerodonRouteData;
import io.choerodon.swagger.annotation.ChoerodonExtraData;
import io.choerodon.swagger.custom.extra.ExtraData;
import io.choerodon.swagger.custom.extra.ExtraDataManager;

@ChoerodonExtraData
public class CustomExtraDataManager implements ExtraDataManager {
    @Override
    public ExtraData getData() {
        ChoerodonRouteData choerodonRouteData = new ChoerodonRouteData();
        choerodonRouteData.setName("cdemo");
        choerodonRouteData.setPath("/cdemo/**");
        choerodonRouteData.setServiceId("choerodon-demo-service111");
        extraData.put(ExtraData.ZUUL_ROUTE_DATA, choerodonRouteData);
        return extraData;
    }
}