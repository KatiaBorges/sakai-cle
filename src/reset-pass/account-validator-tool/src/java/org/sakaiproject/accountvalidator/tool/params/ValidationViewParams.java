/**
 * $Id: ValidationViewParams.java 105078 2012-02-24 23:00:38Z ottenhoff@longsight.com $
 * $URL: https://source.sakaiproject.org/svn/reset-pass/trunk/account-validator-tool/src/java/org/sakaiproject/accountvalidator/tool/params/ValidationViewParams.java $
 * DeveloperHelperService.java - entity-broker - Apr 13, 2008 5:42:38 PM - azeckoski
 **************************************************************************
 * Copyright (c) 2008, 2009 The Sakai Foundation
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
 */
package org.sakaiproject.accountvalidator.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class ValidationViewParams extends SimpleViewParameters {
	
	public String tokenId;
	
	public ValidationViewParams() {
		
	}
	
	public ValidationViewParams(String viewId, String tokenId) {
		this.viewID = viewId;
		this.tokenId = tokenId;
	}
	
}
