package org.alfresco.repo.web.scripts.roles;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Webscript for adding a user or a group to a role
 *
 * @author Tuna Aksoy
 * @since 2.1
 */
public class RmAuthoritiesPost extends AbstractRmAuthorities
{
    /**
     * @see org.springframework.extensions.webscripts.DeclarativeWebScript#executeImpl(org.springframework.extensions.webscripts.WebScriptRequest,
     *      org.springframework.extensions.webscripts.Status,
     *      org.springframework.extensions.webscripts.Cache)
     */
    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
    {
        NodeRef filePlan = getFilePlan(req);
        String roleId = getRoleId(req);
        String authorityName = getAuthorityName(req);

        filePlanRoleService.assignRoleToAuthority(filePlan, roleId, authorityName);

        return new HashMap<String, Object>();
    }
}
