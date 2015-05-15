/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.module.org_alfresco_module_rm.classification;

import org.alfresco.query.PagingResults;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.NoSuchPersonException;

import java.util.List;

/**
 * This service offers access to users' security clearance levels.
 *
 * @author Neil Mc Erlean
 * @author David Webster
 * @since 3.0
 */
public interface SecurityClearanceService
{
	/**
	 * Indicates whether the currently authenticated user has clearance to see the
	 * provided node.
	 * <p>
	 * Note that users, regardless of their clearance level, are always cleared to see a node that has no classification 
	 * applied.
	 * 
	 * @param  nodeRef	node reference
	 * @return boolean	true if cleared to see node, false otherwise
	 */
	boolean hasClearance(NodeRef nodeRef);
	
    /**
     * Get the currently authenticated user's security clearance.
     *
     * @return the security clearance for the currently authenticated user.
     * @throws NoSuchPersonException if the current user's person node cannot be found.
     */
    SecurityClearance getUserSecurityClearance();

    /**
     * Get users' security clearances.
     *
     * @param queryParams parameters for the query.
     * @return security clearances for the specified page of users.
     */
    PagingResults<SecurityClearance> getUsersSecurityClearance(UserQueryParams queryParams);

    /**
     * Set the clearance level for a user.
     *
     * @param userName The username of the user.
     * @param clearanceId The identifier for the new clearance level.
     * @return the user's security clearance
     */
    SecurityClearance setUserSecurityClearance(String userName, String clearanceId);

    /**
     * Returns an immutable list of the defined clearance levels.
     *
     * @return clearance levels in descending order from highest to lowest
     * (where fewer users have access to the highest clearance levels
     * and therefore access to the most restricted documents).
     */
    List<ClearanceLevel> getClearanceLevels();
}