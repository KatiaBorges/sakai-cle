/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-app/src/java/org/sakaiproject/tool/assessment/api/spring/SamigoApi.java $
 * $Id: SamigoApi.java 106463 2012-04-02 12:20:09Z david.horwitz@uct.ac.za $
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


package org.sakaiproject.tool.assessment.api.spring;

import org.sakaiproject.tool.assessment.api.SamigoApiFactory;
import org.sakaiproject.tool.assessment.shared.api.assessment.AssessmentServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.assessment.ItemServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.assessment.PublishedAssessmentServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.assessment.SectionServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.assessment.SecureDeliveryServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.common.MediaServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.common.TypeServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.grading.GradebookServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.grading.GradingServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.qti.QTIServiceAPI;
import org.sakaiproject.tool.assessment.shared.api.questionpool.QuestionPoolServiceAPI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * <p> </p>
 * <p>Description: Concrete class for SamigoApiFactory</p>
 * <p>Implements setters for Spring injection.</p>
 * @author Ed Smiley <esmiley@stanford.edu>
 *
 */
public class SamigoApi extends SamigoApiFactory
{
  private static Log log = LogFactory.getLog(SamigoApi.class);

  private AssessmentServiceAPI assessmentServiceAPI;
  private GradebookServiceAPI gradebookServiceAPI;
  private GradingServiceAPI gradingServiceAPI;
  private ItemServiceAPI itemServiceAPI;
  private MediaServiceAPI mediaServiceAPI;
  private PublishedAssessmentServiceAPI publishedAssessmentServiceAPI;
  private QTIServiceAPI qtiServiceAPI;
  private QuestionPoolServiceAPI questionPoolServiceAPI;
  private SectionServiceAPI sectionServiceAPI;
  private SecureDeliveryServiceAPI secureDeliveryServiceAPI;
  private TypeServiceAPI typeServiceAPI;

  public void setAssessmentServiceAPI(AssessmentServiceAPI assessmentServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: assessmentServiceAPI="+ assessmentServiceAPI);
    this.assessmentServiceAPI = assessmentServiceAPI;
  }
  public void setGradebookServiceAPI(GradebookServiceAPI gradebookServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: gradebookServiceAPI="+ gradebookServiceAPI);
    this.gradebookServiceAPI = gradebookServiceAPI;
  }
  public void setGradingServiceAPI(GradingServiceAPI gradingServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: gradingServiceAPI="+ gradingServiceAPI);
    this.gradingServiceAPI = gradingServiceAPI;
  }
  public void setItemServiceAPI(ItemServiceAPI itemServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: itemServiceAPI="+ itemServiceAPI);
    this.itemServiceAPI = itemServiceAPI;
  }
  public void setMediaServiceAPI(MediaServiceAPI mediaServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: mediaServiceAPI="+ mediaServiceAPI);
    this.mediaServiceAPI = mediaServiceAPI;
  }
  public void setPublishedAssessmentServiceAPI(PublishedAssessmentServiceAPI publishedAssessmentServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: publishedAssessmentServiceAPI="+ publishedAssessmentServiceAPI);
    this.publishedAssessmentServiceAPI = publishedAssessmentServiceAPI;
  }
  public void setQtiServiceAPI(QTIServiceAPI qtiServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: qtiServiceAPI="+ qtiServiceAPI);
    this.qtiServiceAPI = qtiServiceAPI;
  }
  public void setQuestionPoolServiceAPI(QuestionPoolServiceAPI questionPoolServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: questionPoolServiceAPI="+ questionPoolServiceAPI);
    this.questionPoolServiceAPI = questionPoolServiceAPI;
  }
  public void setSectionServiceAPI(SectionServiceAPI sectionServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: sectionServiceAPI="+ sectionServiceAPI);
    this.sectionServiceAPI = sectionServiceAPI;
  }
  public void setSecureDeliveryServiceAPI(SecureDeliveryServiceAPI secureDeliveryServiceAPI) {
	log.debug("Setting Samigo (Test and Quizzes) API, injecting: secureDeliveryServiceAPI="+ secureDeliveryServiceAPI);
	this.secureDeliveryServiceAPI = secureDeliveryServiceAPI;
  }
  public void setTypeServiceAPI(TypeServiceAPI typeServiceAPI)
  {
    log.debug("Setting Samigo (Test and Quizzes) API, injecting: typeServiceAPI="+ typeServiceAPI);
    this.typeServiceAPI = typeServiceAPI;
  }
  public AssessmentServiceAPI getAssessmentServiceAPI()
  {
    return assessmentServiceAPI;
  }
  public GradebookServiceAPI getGradebookServiceAPI()
  {
    return gradebookServiceAPI;
  }
  public GradingServiceAPI getGradingServiceAPI()
  {
    return gradingServiceAPI;
  }
  public MediaServiceAPI getMediaServiceAPI()
  {
    return mediaServiceAPI;
  }
  public PublishedAssessmentServiceAPI getPublishedAssessmentServiceAPI()
  {
    return publishedAssessmentServiceAPI;
  }
  public QTIServiceAPI getQtiServiceAPI()
  {
    return qtiServiceAPI;
  }
  public QuestionPoolServiceAPI getQuestionPoolServiceAPI()
  {
    return questionPoolServiceAPI;
  }
  public SectionServiceAPI getSectionServiceAPI()
  {
    return sectionServiceAPI;
  }
  public SecureDeliveryServiceAPI getSecureDeliveryServiceAPI()
  {
    return secureDeliveryServiceAPI;
  }
  public TypeServiceAPI getTypeServiceAPI()
  {
    return typeServiceAPI;
  }

  public ItemServiceAPI getItemServiceAPI()
  {
    return itemServiceAPI;
  }
}
