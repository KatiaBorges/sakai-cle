/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-services/src/java/org/sakaiproject/tool/assessment/integration/helper/standalone/AbstractSectionsImpl.java $
 * $Id: AbstractSectionsImpl.java 106463 2012-04-02 12:20:09Z david.horwitz@uct.ac.za $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
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


package org.sakaiproject.tool.assessment.integration.helper.standalone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.section.api.SectionAwareness;
import org.sakaiproject.tool.assessment.services.PersistenceService;


/**
 * borrowed gradebook's AbstractSectionsImpl class
 */

class AbstractSectionsImpl {
    private static final Log log = LogFactory.getLog(AbstractSectionsImpl.class);

    private SectionAwareness sectionAwareness;

	protected SectionAwareness getSectionAwareness() {
		return PersistenceService.getInstance().getSectionAwareness();
	}
	public void setSectionAwareness(SectionAwareness sectionAwareness) {
		this.sectionAwareness = sectionAwareness;
	}

}
