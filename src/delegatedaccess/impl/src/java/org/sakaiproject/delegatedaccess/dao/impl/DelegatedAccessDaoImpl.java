package org.sakaiproject.delegatedaccess.dao.impl;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.InvariantReloadingStrategy;
import org.apache.log4j.Logger;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.delegatedaccess.dao.DelegatedAccessDao;
import org.sakaiproject.delegatedaccess.util.DelegatedAccessConstants;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DelegatedAccessDaoImpl extends JdbcDaoSupport implements DelegatedAccessDao {

	private static final Logger log = Logger.getLogger(DelegatedAccessDaoImpl.class);
	private PropertiesConfiguration statements;
	private static int ORACLE_IN_CLAUSE_SIZE_LIMIT = 1000;
	private boolean oracle = false;
	/**
	 * init
	 */
	public void init() {
		log.info("init()");
		
		//setup the vendor
		String vendor = ServerConfigurationService.getInstance().getString("vendor@org.sakaiproject.db.api.SqlService", null);

		//initialise the statements
		initStatements(vendor);
		
		if(vendor != null && "oracle".equals(vendor)){
			oracle = true;
		}
	}
	
	/**
	 * Loads our SQL statements from the appropriate properties file
	 
	 * @param vendor	DB vendor string. Must be one of mysql, oracle, hsqldb
	 */
	private void initStatements(String vendor) {
		
		URL url = getClass().getClassLoader().getResource(vendor + ".properties"); 
		
		try {
			statements = new PropertiesConfiguration(); //must use blank constructor so it doesn't parse just yet (as it will split)
			statements.setReloadingStrategy(new InvariantReloadingStrategy());	//don't watch for reloads
			statements.setThrowExceptionOnMissing(true);	//throw exception if no prop
			statements.setDelimiterParsingDisabled(true); //don't split properties
			statements.load(url); //now load our file
		} catch (ConfigurationException e) {
			log.error(e.getClass() + ": " + e.getMessage(), e);
			return;
		}
	}
	
	/**
	 * Get an SQL statement for the appropriate vendor from the bundle
	
	 * @param key
	 * @return statement or null if none found. 
	 */
	private String getStatement(String key) {
		try {
			return statements.getString(key);
		} catch (NoSuchElementException e) {
			log.error("Statement: '" + key + "' could not be found in: " + statements.getFileName(), e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getDistinctSiteTerms(String termField) {
		try{
			return getJdbcTemplate().query(getStatement("select.distinctTerms"), new String[]{termField}, new RowMapper() {
			      public Object mapRow(ResultSet resultSet, int i) throws SQLException {
			          return resultSet.getString(1);
			        }
			      });
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
           return null;
		}
	}

	public String getSiteProperty(String propertyName, String siteId){
		try{
			return (String) getJdbcTemplate().queryForObject(getStatement("select.siteProperty"), new Object[]{propertyName, siteId}, new RowMapper() {
				
				 public Object mapRow(ResultSet resultSet, int i) throws SQLException {
					return resultSet.getString("VALUE");
				}
			});
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return null;
		}
	}
	
	public Map<String, List<String>> getNodesBySiteRef(String[] siteRefs, String hierarchyId){
		try{
			Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
			if(siteRefs == null || siteRefs.length == 0){
				return returnMap;
			}
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > siteRefs.length){
					subArraySize = (siteRefs.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(siteRefs, subArrayIndex, subArrayIndex + subArraySize);

				String query = getStatement("select.hierarchyNode");
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'"; //escape apostrophe
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query = query.replace("(?)", inParams);
				List<String[]> results =  (List<String[]>) getJdbcTemplate().query(query, new Object[]{hierarchyId}, new RowMapper() {

					public Object mapRow(ResultSet resultSet, int i) throws SQLException {
						return new String[]{resultSet.getString("title"), resultSet.getString("ID")};
					}
				});
				if(results != null){
					for(String[] result : results){
						if(result != null && result.length == 2){
							if(!returnMap.containsKey(result[0])){
								returnMap.put(result[0], new ArrayList<String>());
							}
							returnMap.get(result[0]).add(result[1]);
						}
					}
				}
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < siteRefs.length);
			
			return returnMap;
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return null;
		}
	}
	
	public List<String> getEmptyNonSiteNodes(String hierarchyId){
		try{
			return (List<String>) getJdbcTemplate().query(getStatement("select.emptyNodes"), new Object[]{hierarchyId}, new RowMapper() {
				
				 public Object mapRow(ResultSet resultSet, int i) throws SQLException {
					return resultSet.getString("ID");
				}
			});
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return null;
		}
	}
	
//	public void addSiteProperty(String siteId, String propertyName, String propertyValue){
//		try {
//			getJdbcTemplate().update(getStatement("insert.siteProperty"),
//				new Object[]{siteId, propertyName, propertyValue}
//			);
//		} catch (DataAccessException ex) {
//           log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage());
//		}
//	}
	
	public void updateSiteProperty(String[] siteIds, String propertyName, String propertyValue){
		try {
			if(siteIds == null || siteIds.length == 0){
				return;
			}
			String query = getStatement("update.siteProperty");
			
			if(oracle){
				//Create Replace query:
				String values = "";
				for(String siteId : siteIds){
					if(!"".equals(values)){
						values += " union ";
					}
					values += "select '" + siteId + "' SITE_ID, '" + propertyName + "' NAME, '" + propertyValue + "' VALUE from dual";
				}
				query = query.replace("?", values);
			}else{
				//Create Replace query:
				String values = "";
				for(String siteId : siteIds){
					if(!"".equals(values)){
						values += ",";
					}
					values += "('" + siteId + "', '" + propertyName + "','" + propertyValue + "')";
				}
				query = query + values;
			}
			
			getJdbcTemplate().update(query);
		} catch (DataAccessException ex) {
           log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
		}
	}
	
	public void removeSiteProperty(String[] siteIds, String propertyName){
		try{
			if(siteIds == null || siteIds.length == 0){
				return;
			}
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > siteIds.length){
					subArraySize = (siteIds.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(siteIds, subArrayIndex, subArrayIndex + subArraySize);

				String query1 = getStatement("delete.siteProperty");
				
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'";
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query1 = query1.replace("(?)", inParams);
				getJdbcTemplate().update(query1, new Object[]{propertyName});
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < siteIds.length);
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
		}
	}
	
	public List<String[]> searchSites(String titleSearch, Map<String, String> propsMap, String[] instructorIds, String insturctorType, boolean publishedOnly){
		try{
			if(titleSearch == null){
				titleSearch = "";
			}
			titleSearch ="%" + titleSearch + "%";
			Object[] params = new Object[]{titleSearch};
			String query = "";
			final boolean noInstructors = instructorIds == null || instructorIds.length == 0;
			//either grab the simple site search based on title or the one that limits by instructor ids
			if(noInstructors){
				query = getStatement("select.siteSearch");
			}else{
				if(DelegatedAccessConstants.ADVANCED_SEARCH_INSTRUCTOR_TYPE_MEMBER.equals(insturctorType)){
					query = getStatement("select.siteSearchMembers");
				}else{
					//default is instructor search
					query = getStatement("select.siteSearchInstructors");
				}
				String inParams = "(";
				//to be on the safe side, I added oracle limit restriction, but hopefully no one is searching for
				//more than 1000 instructors
				for(int i = 0; i < instructorIds.length && i < ORACLE_IN_CLAUSE_SIZE_LIMIT; i++){
					inParams += "'" + instructorIds[i].replace("'", "''") + "'";
					if(i < instructorIds.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query = query.replace("(:userIds)", inParams);
			}
			//add the site properties restrictions in the where clause
			if(propsMap != null && propsMap.size() > 0){
				params = new Object[1 + (propsMap.size() * 2)];
				params[0] = titleSearch;
				int i = 1;
				for(Entry<String, String> entry : propsMap.entrySet()){
					query += " " + getStatement("select.siteSearchPropWhere");
					params[i] = entry.getKey();
					i++;
					params[i] = entry.getValue();
					i++;
				}
			}
			if(publishedOnly){
				query += " " + getStatement("select.siteSearchPublishedOnly");
			}
			
			return (List<String[]>) getJdbcTemplate().query(query, params, new RowMapper() {
				
				 public Object mapRow(ResultSet resultSet, int i) throws SQLException {
					 if(noInstructors){
						 return new String[]{resultSet.getString("SITE_ID"), resultSet.getString("TITLE")};
					 }else{
						 return new String[]{resultSet.getString("SITE_ID"), resultSet.getString("TITLE"), resultSet.getString("USER_ID")};
					 }
				}
			});
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return new ArrayList<String[]>();
		}
	}
	
	public Map<String, Map<String, String>> searchSitesForProp(String[] props, String[] siteIds){
		try{
			Map<String, Map<String, String>> returnMap = new HashMap<String, Map<String, String>>();
			if(props == null || props.length == 0 || siteIds == null || siteIds.length == 0){
				return returnMap;
			}
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > siteIds.length){
					subArraySize = (siteIds.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(siteIds, subArrayIndex, subArrayIndex + subArraySize);

				String query = getStatement("select.sitesProp");
				String propInParams = "(";
				for(int i = 0; i < props.length; i++){
					propInParams += "'" + props[i].replace("'", "''") + "'";
					if(i < props.length - 1){
						propInParams += ",";
					}
				}
				propInParams += ")";
				query = query.replace("(:props)", propInParams);
				
				propInParams += ")";
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'";
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query = query.replace("(:siteIds)", inParams);
				List<String[]> results =  (List<String[]>) getJdbcTemplate().query(query, new RowMapper() {

					public Object mapRow(ResultSet resultSet, int i) throws SQLException {
						return new String[]{resultSet.getString("SITE_ID"), resultSet.getString("NAME"), resultSet.getString("VALUE")};
					}
				});
				if(results != null){
					for(String[] result : results){
						Map<String, String> propMap = new HashMap<String, String>();
						if(returnMap.containsKey(result[0])){
							propMap = returnMap.get(result[0]);
						}
						propMap.put(result[1], result[2]);
						returnMap.put(result[0], propMap);
					}
				}
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < siteIds.length);
			
			return returnMap;
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return null;
		}
	}
	
	public void cleanupOrphanedPermissions(){
		try {
			getJdbcTemplate().update(getStatement("delete.orphaned.permissions"));
		} catch (DataAccessException ex) {
           log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
		}
	}
	
	public Map<String, Set<String>> getNodesAndPermsForUser(String userId, String[] nodeIds){
		try{
			Map<String, Set<String>> returnMap = new HashMap<String, Set<String>>();
			if(nodeIds == null || nodeIds.length == 0){
				return returnMap;
			}
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > nodeIds.length){
					subArraySize = (nodeIds.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(nodeIds, subArrayIndex, subArrayIndex + subArraySize);

				String query = getStatement("select.nodes.and.perms.for.user");
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'";
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query = query.replace("(?)", inParams);
				List<String[]> results =  (List<String[]>) getJdbcTemplate().query(query, new Object[]{userId}, new RowMapper() {

					public Object mapRow(ResultSet resultSet, int i) throws SQLException {
						return new String[]{resultSet.getString("NODEID"), resultSet.getString("PERMISSION")};
					}
				});
				if(results != null){
					for(String[] result : results){
						if(result != null && result.length == 2){
							if(!returnMap.containsKey(result[0])){
								returnMap.put(result[0], new HashSet<String>());
							}
							returnMap.get(result[0]).add(result[1]);
						}
					}
				}
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < nodeIds.length);
			
			return returnMap;
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * DAC-40 Highlight Inactive Courses in site search
	 * requires the job "InactiveCoursesJob" attached in the jira
	 */
	
	public List<String> findActiveSites(String[] siteIds){
		List<String> returnList = new ArrayList<String>();
		
		if(siteIds == null || siteIds.length == 0){
			return returnList;
		}
		try{
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > siteIds.length){
					subArraySize = (siteIds.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(siteIds, subArrayIndex, subArrayIndex + subArraySize);

				String query = getStatement("select.activeSites");
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'";
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query = query.replace("(?)", inParams);
				List<String> results =  (List<String>) getJdbcTemplate().query(query, new RowMapper() {
					public Object mapRow(ResultSet resultSet, int i) throws SQLException {
						return resultSet.getString("SITE_ID");
					}
				});
				if(results != null){
					returnList.addAll(results);
				}
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < siteIds.length);

			return returnList;
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return null;
		}

	}
	
	public void removeAnonAndAuthRoles(String[] siteRefs){
		try{
			if(siteRefs == null || siteRefs.length == 0){
				return;
			}
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > siteRefs.length){
					subArraySize = (siteRefs.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(siteRefs, subArrayIndex, subArrayIndex + subArraySize);

				String query1 = getStatement("delete.anon.auth.roles");
				String query2 = getStatement("delete.anon.auth.permissions");
				
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'";
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query1 = query1.replace("(?)", inParams);
				query2 = query2.replace("(?)", inParams);
				getJdbcTemplate().update(query1);
				getJdbcTemplate().update(query2);
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < siteRefs.length);
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
		}
	}
	
	public void copyRole(String fromRealm, String fromRole, String[] toRealm, String toRole){
		if(toRealm == null || toRealm.length == 0){
			return;
		}
		try{
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > toRealm.length){
					subArraySize = (toRealm.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(toRealm, subArrayIndex, subArrayIndex + subArraySize);

				String query1 = getStatement("insert.copyrole");
				String query2 = getStatement("insert.copyroledesc");
				
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'";
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query1 = query1.replace("(?)", inParams);
				query2 = query2.replace("(?)", inParams);
				getJdbcTemplate().update(query1, new Object[]{fromRealm, fromRole, toRole});
				getJdbcTemplate().update(query2, new Object[]{toRole});
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < toRealm.length);
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
		}
	}
	
	public List<String> getDelegatedAccessUsers(){
		try{
			return getJdbcTemplate().query(getStatement("select.delegatedaccess.user"), new RowMapper() {
			      public Object mapRow(ResultSet resultSet, int i) throws SQLException {
			          return resultSet.getString("userId");
			        }
			      });
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
           return null;
		}
	}
	
	public List<String> getSitesWithDelegatedAccessTool(String[] siteIds){
		try{
			List<String> returnList = new ArrayList<String>();
			if(siteIds == null || siteIds.length == 0){
				return returnList;
			}
			
			int subArrayIndex = 0;
			do{
				int subArraySize = ORACLE_IN_CLAUSE_SIZE_LIMIT;
				if(subArrayIndex + subArraySize > siteIds.length){
					subArraySize = (siteIds.length - subArrayIndex);
				}
				String[] subSiteRefs = Arrays.copyOfRange(siteIds, subArrayIndex, subArrayIndex + subArraySize);

				String query = getStatement("select.delegatedaccess.user.hasworkspacetool");
				String inParams = "(";
				for(int i = 0; i < subSiteRefs.length; i++){
					inParams += "'" + subSiteRefs[i].replace("'", "''") + "'";
					if(i < subSiteRefs.length - 1){
						inParams += ",";
					}
				}
				inParams += ")";
				query = query.replace("(?)", inParams);
				List<String> results =  (List<String>) getJdbcTemplate().query(query, new RowMapper() {
					public Object mapRow(ResultSet resultSet, int i) throws SQLException {
						return resultSet.getString("SITE_ID");
					}
				});
				if(results != null){
					returnList.addAll(results);
				}
				subArrayIndex = subArrayIndex + subArraySize;
			}while(subArrayIndex < siteIds.length);

			return returnList;
		}catch (DataAccessException ex) {
			log.error("Error executing query: " + ex.getClass() + ":" + ex.getMessage(), ex);
			return null;
		}

	}
}
