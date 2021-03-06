/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/providers/trunk/jldap/src/java/edu/amc/sakai/user/LdapConnectionLivenessValidator.java $
 * $Id: LdapConnectionLivenessValidator.java 105079 2012-02-24 23:08:11Z ottenhoff@longsight.com $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package edu.amc.sakai.user;

import com.novell.ldap.LDAPConnection;

/**
 * Pluggable strategy for verifying {@link LDAPConnection}
 * "liveness". Typically injected into a 
 * {@link PooledLDAPConnectionFactory}.
 * 
 * @author dmccallum@unicon.net
 *
 */
public interface LdapConnectionLivenessValidator {

	public boolean isConnectionAlive(LDAPConnection connectionToTest);
	
}
