/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/trunk/api/src/main/java/org/sakaiproject/user/api/AuthenticationException.java $
 * $Id: AuthenticationException.java 105077 2012-02-24 22:54:29Z ottenhoff@longsight.com $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2008 Sakai Foundation
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

package org.sakaiproject.user.api;

/**
 * <p>
 * AuthenticationException models errors in authentication.
 * </p>
 */
public class AuthenticationException extends Exception
{
	private static final long serialVersionUID = 3977014054785135157L;

	public AuthenticationException()
	{
		super();
	}

	public AuthenticationException(String msg)
	{
		super(msg);
	}
}
