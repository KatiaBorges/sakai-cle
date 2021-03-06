/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/trunk/kernel-impl/src/main/java/org/sakaiproject/user/impl/PasswordPolicyProviderDefaultImpl.java $
 * $Id: PasswordPolicyProviderDefaultImpl.java 130605 2013-10-18 12:53:50Z azeckoski@unicon.net $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2008, 2009, 2010 Sakai Foundation
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

package org.sakaiproject.user.impl;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.user.api.PasswordPolicyProvider;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService.PasswordRating;

/**
 * This is the default implementation of the Password policy provider.
 * 
 * https://jira.sakaiproject.org/browse/KNL-1123
 */
public class PasswordPolicyProviderDefaultImpl implements PasswordPolicyProvider {

    /** Our log (commons). */
    private static Log logger = LogFactory.getLog(PasswordPolicyProviderDefaultImpl.class);

    /** value for minimum password entropy */
    private static final int DEFAULT_MIN_ENTROPY = 16;

    /** value for maximum password sequence length */
    private static final int DEFAULT_MAX_SEQ_LENGTH = 3;

    /** sakai.property for minimum password entropy */
    private static final String SAK_PROP_MIN_PASSWORD_ENTROPY = "user.password.minimum.entropy";

    /** sakai.property for maximum password sequence length */
    private static final String SAK_PROP_MAX_PASSWORD_SEQ_LENGTH = "user.password.maximum.sequence.length";

    /** array of all lower case characters (used for calculating password entropy) */
    private static final char[] CHARS_LOWER = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /** array of all upper case characters (used for calculating password entropy) */
    private static final char[] CHARS_UPPER = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    /** array of all digit characters (used for calculating password entropy) */
    private static final char[] CHARS_DIGIT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /** array of all special characters (used for calculating password entropy) */
    private static final char[] CHARS_SPECIAL = { '!', '$', '*', '+', '-', '.', '=', '?', '@', '^', '_', '|', '~' };

    /** value for minimum password entropy */
    private int minEntropy = DEFAULT_MIN_ENTROPY;

    /** value for maximum password sequence length */
    private int maxSequenceLength = DEFAULT_MAX_SEQ_LENGTH;


    /**
     * Default zero-arg constructor
     * DO NOT USE
     */
    PasswordPolicyProviderDefaultImpl() {
        this(null);
    }

    /**
     * @param serverConfigurationService
     */
    public PasswordPolicyProviderDefaultImpl(ServerConfigurationService serverConfigurationService) {
        this.serverConfigurationService = serverConfigurationService;
        init();
    }


    /**
     * Initialization method (Spring)
     */
    public void init() {
        // Get the values from sakai.properties
        if (serverConfigurationService == null) {
            serverConfigurationService = (ServerConfigurationService) ComponentManager.get(org.sakaiproject.component.api.ServerConfigurationService.class);
        }
        if (serverConfigurationService != null) {
            minEntropy = serverConfigurationService.getInt(SAK_PROP_MIN_PASSWORD_ENTROPY, minEntropy);
            maxSequenceLength = serverConfigurationService.getInt(SAK_PROP_MAX_PASSWORD_SEQ_LENGTH, maxSequenceLength);
        }
        logger.info("PasswordPolicyProviderDefaultImpl.init(): minEntropy="+minEntropy+", maxSequenceLength="+maxSequenceLength);
    }

    /**
     * Destroy method (Spring)
     */
    public void destroy() {
        if (logger.isDebugEnabled())
            logger.debug("PasswordPolicyProviderDefaultImpl.destroy()");
    }

    public PasswordRating validatePassword(String password, User user) {
        if (logger.isDebugEnabled())
            logger.debug("PasswordPolicyProviderDefaultImpl.validatePassword( " + password + " )");

        // If the password is null, it's invalid
        if (password == null) {
            return PasswordRating.FAILED; // SHORT CIRCUIT
        }

        /* If the password contains X number of characters from their display ID, it's invalid
         * (where X is the maximum password sequence length defined in sakai.properties)
         */
        if (user != null) {
            String userDisplayID = user.getDisplayId();
            if (userDisplayID != null) {
                int length = userDisplayID.length();
                for (int i = 0; i < length - (maxSequenceLength - 1); i++) {
                    String sub = userDisplayID.substring(i, i + maxSequenceLength);
                    if (password.indexOf(sub) > -1) {
                        return PasswordRating.FAILED; // SHORT CIRCUIT
                    }
                }
            }
        }

        // Count the number of character sets used in the password
        int characterSets = 0;
        characterSets += isCharacterSetPresentInPassword(CHARS_LOWER, password);
        characterSets += isCharacterSetPresentInPassword(CHARS_UPPER, password);
        characterSets += isCharacterSetPresentInPassword(CHARS_DIGIT, password);
        characterSets += isCharacterSetPresentInPassword(CHARS_SPECIAL, password);

        // Calculate and verify the password strength
        int strength = password.length() * characterSets;
        if (strength < minEntropy) {
            return PasswordRating.FAILED; // SHORT CIRCUIT
        }

        // The password has passed all requirements, therefore the password is valid
        return PasswordRating.PASSED_UNRATED;
    }

    /**
     * Determine if the given character set is present in the given password string.
     * 
     * @param characterSet
     *            the set of characters to check for
     * @param password
     *            the password to search for the charachter set in
     * @return 1 if the character set is present in the password, 0 otherwise
     */
    private int isCharacterSetPresentInPassword(char[] characterSet, String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Arrays.binarySearch(characterSet, password.charAt(i)) >= 0) {
                return 1; // SHORT CIRCUIT
            }
        }
        return 0;
    }


    private ServerConfigurationService serverConfigurationService;
    public void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {
        this.serverConfigurationService = serverConfigurationService;
    }

}
