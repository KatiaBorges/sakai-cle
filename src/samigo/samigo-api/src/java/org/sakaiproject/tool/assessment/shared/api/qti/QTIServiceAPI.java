/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-api/src/java/org/sakaiproject/tool/assessment/shared/api/qti/QTIServiceAPI.java $
 * $Id: QTIServiceAPI.java 120835 2013-03-06 13:29:53Z azeckoski@unicon.net $
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



package org.sakaiproject.tool.assessment.shared.api.qti;

import org.w3c.dom.Document;

import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;

public interface QTIServiceAPI
{
  /**
   * Import an assessment XML document in QTI format, extract & persist the data.
   * @param document the assessment XML document in QTI format
   * @param qtiVersion either 1=QTI VERSION 1.2  or 2=QTI Version 2.0
   * @return a persisted assessment
   */
  public AssessmentIfc createImportedAssessment(Document document,
                                                int qtiVersion);

  /**
   * Import an assessment XML document in QTI format, extract & persist the data.
   * @param documentPath the pathname to a file with the assessment XML document in QTI format
   * @param qtiVersion either 1=QTI VERSION 1.2  or 2=QTI Version 2.0
   * @param siteId the site the assessment will be associated with
   * @return a persisted assessment
   */
  public AssessmentIfc createImportedAssessment(String documentPath, int qtiVersion, String siteId);

  /**
   * Import an item XML document in QTI format, extract & persist the data.
   * @param document the item XML document in QTI format
   * @param qtiVersion either 1=QTI VERSION 1.2  or 2=QTI Version 2.0
   * @return a persisted item
   */
  public ItemDataIfc createImportedItem(Document document, int qtiVersion);

  /**
   * Get an assessment in Document form.
   *
   * Note:  this service requires a Faces context.
   *
   * @param assessmentId the assessment's Id
   * @param qtiVersion either 1=QTI VERSION 1.2  or 2=QTI Version 2.0
   * @return the Document with the assessment data
   */
  public Document getExportedAssessment(String assessmentId, int qtiVersion);

  /**
   * Get an assessment in String form.
   *
   * Note:  this service requires a Faces context.
   *
   * @param assessmentId the assessment's Id
   * @param qtiVersion either 1=QTI VERSION 1.2  or 2=QTI Version 2.0
   * @return the Document with the assessment data
   */
  public String getExportedAssessmentAsString(String assessmentId, int qtiVersion);

  /**
   * Get an item in Document form.
   *
   * Note:  this service requires a Faces context.
   *
   * @param itemId the item's Id
   * @param qtiVersion either 1=QTI VERSION 1.2  or 2=QTI Version 2.0
   * @return the Document with the assessment data
   */
  public Document getExportedItem(String itemId, int qtiVersion);

  /**
   * Get an item bank in Document form.
   *
   * Note:  this service requires a Faces context.
   *
   * @param itemIds an array of item ids
   * @param qtiVersion either 1=QTI VERSION 1.2  or 2=QTI Version 2.0
   * @return the Document with the item bank
   */
  public Document getExportedItemBank(String itemIds[], int qtiVersion);

}
