package com.gamedoora.backend.proxy.aggregation.routes;

import com.gamedoora.backend.proxy.aggregation.enrichment.clients.RetriableRolesServicesClient;
import com.gamedoora.backend.proxy.aggregation.enrichment.clients.RetriableSkillsServicesClient;
import com.gamedoora.backend.proxy.aggregation.enrichment.clients.RetriableUsersServicesClient;
import feign.FeignException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserProfileRoute extends RouteBuilder {
    private RetriableRolesServicesClient retriableRolesServicesClient;
    private RetriableSkillsServicesClient retriableSkillsServicesClient;
    private RetriableUsersServicesClient retriableUsersServicesClient;

    @Override
    public void configure() throws Exception {
        onException(FeignException.class)
                .maximumRedeliveries(3)
                .logStackTrace(true)
                .handled(false);

        from("direct:userQuery")
                .to("bean:retriableUsersServicesClient?method=getRolesForUserByEmail")
                .end();
        from("direct:userRoleQuery")
                .to("bean:retriableRolesServicesClient?method=getRolesForUserByEmail")
                .end();
        from("direct:userSkillsQuery")
                .to("bean:retriableSkillsServicesClient?method=getSkillsForUserByEmail")
                .end();
        from("direct:addUserRoleQuery")
                .to("bean:retriableUsersServicesClient?method=addRolesForUser")
                .end();
        from("direct:addUserSkillQuery")
                .to("bean:retriableUsersServicesClient?method=addSkillsForUser")
                .end();
    }


}
