package com.demo.repo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.demo.dto.CustomQueryHolder;

@Transactional
@Repository
public class JpaRepo<K> extends AbstractJpaRepo<K> {

	public JpaRepo() {
		super();
	}

	public <T> void deleteById(final Integer id, final Class<T> clazz) {
		final T entity = masterEntityManager.find(clazz, id);
		masterEntityManager.remove(entity);

	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(final Class<T> clazz) {
		return slaveEntityManager.createQuery("from " + clazz.getName()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findPaginationByQuery(final CustomQueryHolder queryHolder, Integer firstResult, Integer maxResult) {
		final Query query = slaveEntityManager.createQuery(queryHolder.getQueryString());
		if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
			for (final Entry<String, Object> currentEntry : queryHolder.getInParamMap().entrySet()) {
				query.setParameter(currentEntry.getKey(), currentEntry.getValue());
			}
		}
		query.setFirstResult(firstResult == null ? 0 : firstResult);
		query.setMaxResults(maxResult == null ? 10 : maxResult);

		return query.getResultList();
	}
	
	

	@SuppressWarnings("unchecked")
	public <T> List<T> findByQuery(final CustomQueryHolder queryHolder) {
		final Query query = slaveEntityManager.createQuery(queryHolder.getQueryString());
		if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
			for (final String currentKey : queryHolder.getInParamMap().keySet()) {
				query.setParameter(currentKey, queryHolder.getInParamMap().get(currentKey));
			}
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByQueryFromMaster(final CustomQueryHolder queryHolder) {
		final Query query = masterEntityManager.createQuery(queryHolder.getQueryString());
		if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
			for (final String currentKey : queryHolder.getInParamMap().keySet()) {
				query.setParameter(currentKey, queryHolder.getInParamMap().get(currentKey));
			}
		}
		return query.getResultList();
	}

	public <T> T findByQueryAndReturnFirstElement(final CustomQueryHolder queryHolder) {
		final Query query = slaveEntityManager.createQuery(queryHolder.getQueryString());
		if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
			for (final String currentKey : queryHolder.getInParamMap().keySet()) {
				query.setParameter(currentKey, queryHolder.getInParamMap().get(currentKey));
			}
		}
		if (query.getResultList() != null && !query.getResultList().isEmpty()) {
			return (T) query.getSingleResult();
		}
		return null;
	}

	public <T> List<T> findByQueryWithMaxElements(final CustomQueryHolder queryHolder, final
			Integer maxElementSize) {
		final Query query = slaveEntityManager.createQuery(queryHolder.getQueryString());
		if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
			for (final String currentKey : queryHolder.getInParamMap().keySet()) {
				query.setParameter(currentKey, queryHolder.getInParamMap().get(currentKey));
			}
		}
		query.setMaxResults(maxElementSize);
		return query.getResultList();
	}

	public Boolean runQueryForUpdate(final CustomQueryHolder queryHolder) {
		// TODO Auto-generated method stub
		final Query query = masterEntityManager.createQuery(queryHolder.getQueryString());
		if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
			for (final Entry<String, Object> currentEntry : queryHolder.getInParamMap().entrySet()) {
				query.setParameter(currentEntry.getKey(), currentEntry.getValue());
			}
		}
		if (query.executeUpdate() > 0) {
			return true;
		}
		return false;
	}

	public <L, M> Map<L, M> findEntityMapByQuery(final CustomQueryHolder queryHolder) {
		final Map<L, M> resultMap = new HashMap<>();

		final Query query = slaveEntityManager.createQuery(queryHolder.getQueryString());
		if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
			for (final Entry<String, Object> currentEntry : queryHolder.getInParamMap().entrySet()) {
				query.setParameter(currentEntry.getKey(), currentEntry.getValue());
			}
		}

		for (final Map<String, Object> map : (List<Map<String, Object>>) query.getResultList()) {
			resultMap.put((L) map.get("0"), (M) map.get("1"));
		}
		return resultMap;
	}

	public <T> T findOne(final Integer id, final Class<T> clazz) {
		return slaveEntityManager.find(clazz, id);
	}

	public <R> List<R> getPOJOFromNativeQuery(final CustomQueryHolder queryHolder, Class<R> r, Map<Integer, String> fieldIndexMap) {
		try {
			final Query nativeQuery = slaveEntityManager.createNativeQuery(queryHolder.getQueryString());
			if (queryHolder.getInParamMap() != null && !queryHolder.getInParamMap().isEmpty()) {
				for (final Entry<String, Object> currentEntry : queryHolder.getInParamMap().entrySet()) {
					nativeQuery.setParameter(currentEntry.getKey(), currentEntry.getValue());
				}
			}
			return convertFromObjListToPOJOList(nativeQuery.getResultList(), r, fieldIndexMap);
		}
		catch (final Exception e) {
		}
		return null;
	}

	private <R> List<R> convertFromObjListToPOJOList(List<Object[]> objArrList, Class<R> resultClass, Map<Integer, String> fieldIndexMap) {
		try {
			List<R> resultList = new ArrayList<>();
			for (Object[] objArr : objArrList) {
				R resultObj = buildPOJOFromObjArr(resultClass, fieldIndexMap, objArr);
				resultList.add(resultObj);
			}

		}
		catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private <R> R buildPOJOFromObjArr(Class<R> resultClass, Map<Integer, String> fieldIndexMap, Object[] objArr)
			throws InstantiationException, IllegalAccessException {
		R resultObj = resultClass.newInstance();
		for (Map.Entry<Integer, String> fieldDetail : fieldIndexMap.entrySet()) {
			if (doesObjectContainField(resultObj, fieldDetail.getValue())) {
				setValueForField(resultObj, fieldDetail.getValue(), objArr[fieldDetail.getKey()], resultClass);
			}
		}
		return resultObj;
	}

	public static boolean doesObjectContainField(Object object, String fieldName) {
		return Arrays.stream(object.getClass().getDeclaredFields()).anyMatch(f -> f.getName().equals(fieldName));
	}

	public static <T> boolean setValueForField(Object object, String fieldName, Object value, Class<T> clazz) {

		Field field;
		try {
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			Object fieldValue = castToAppropriateDataType(value, field);
			field.set(object, fieldValue);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// log

		}
		return true;

	}

	private static Object castToAppropriateDataType(Object value, Field field) {

		try {
			Object fieldValue = null;
			if (value != null && !field.getType().equals(String.class)) {

				fieldValue = field.getType().getMethod("valueOf", String.class).invoke(null, value.toString());

			}
			else if (value != null && field.getType().equals(String.class)) {

				fieldValue = value.toString();

			}
			return fieldValue;
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}