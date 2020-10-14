package com.woshidaniu.db.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.woshidaniu.db.core.annotation.FieldMapper;
import com.woshidaniu.db.core.annotation.Parameter;
import com.woshidaniu.db.core.annotation.TableMapper;
import com.woshidaniu.db.core.mapper.domain.FieldMapperModel;
import com.woshidaniu.db.core.mapper.domain.TableMapperModel;
import com.woshidaniu.db.core.mapper.domain.UniqueKeyType;

/**
 * 
 * @className: SQLBuilder
 * @description: 通过注解生成sql
 * @author : kangzhidong
 * @date : 下午12:48:03 2013-10-12
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class DynamicSQLUtils {
   
	/**
     * 缓存TableMapper
     */
    private static HashMap<Class<?>, TableMapperModel> tableMapperCache = new HashMap<Class<?>, TableMapperModel>(128);

    /**
     * 由传入的dto对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
     * 
     * @param dtoClass
     * @return TableMapper
     */
    public static TableMapperModel buildTableMapper(Class<?> dtoClass) {

        HashMap<String, FieldMapperModel> fieldMapperCache = null;
        ArrayList<FieldMapperModel> fieldMapperList = null;
        Field[] fields = null;

        FieldMapper fieldMapperAnnotation = null;
        FieldMapperModel fieldMapper = null;
        TableMapperModel tableMapper = null;
        synchronized (tableMapperCache) {
            tableMapper = tableMapperCache.get(dtoClass);
            if (tableMapper != null) {
                return tableMapper;
            }
            tableMapper = new TableMapperModel();
            
            Annotation[] classAnnotations = dtoClass.getDeclaredAnnotations();
            if (classAnnotations.length == 0) {
                throw new RuntimeException("Class " + dtoClass.getName() + " has no annotation !");
            }
            for (Annotation an : classAnnotations) {
                if (an instanceof TableMapper) {
                    tableMapper.setTableAnnotation(an);
                }
            }
            if (tableMapper.getTableAnnotation() == null) {
                throw new RuntimeException("Class " + dtoClass.getName() + " has no 'TableMapperAnnotation', " + "which has the database table information," + " I can't build 'TableMapper' for it.");
            }
            fields = dtoClass.getDeclaredFields();
            fieldMapperCache = new HashMap<String, FieldMapperModel>();
            fieldMapperList = new ArrayList<FieldMapperModel>();
            Annotation[] fieldAnnotations = null;
            for (Field field : fields) {
                fieldAnnotations = field.getDeclaredAnnotations();
                if (fieldAnnotations.length == 0) {
                    continue;
                }
                for (Annotation an : fieldAnnotations) {
                    if (an instanceof FieldMapper) {
                        fieldMapperAnnotation = (FieldMapper) an;
                        fieldMapper = new FieldMapperModel();
                        fieldMapper.setFieldName(field.getName());
                        fieldMapper.setColumnName(fieldMapperAnnotation.column());
                        fieldMapper.setJdbcType(fieldMapperAnnotation.jdbcType());
                        fieldMapperCache.put(fieldMapperAnnotation.column(), fieldMapper);
                        fieldMapperList.add(fieldMapper);
                    }
                }
            }
            tableMapper.setFieldCache(fieldMapperCache);
            tableMapper.setFieldList(fieldMapperList);
            tableMapperCache.put(dtoClass, tableMapper);
            return tableMapper;
        }
    }

    /**
     * 从注解里获取唯一键信息
     * 
     * @param tableMapper
     * @return
     */
    public static String[] buildUniqueKey(TableMapperModel tableMapper) {
        TableMapper tma = (TableMapper) tableMapper.getTableAnnotation();
        String[] uniqueKeyNames = null;
        if (tma.uniqueKeyType().equals(UniqueKeyType.Single)) {
            uniqueKeyNames = new String[1];
            uniqueKeyNames[0] = tma.uniqueKey();
        } else {
            uniqueKeyNames = tma.uniqueKey().split(",");
        }
        return uniqueKeyNames;
    }

	public static <T> Object[] collectParameters(T entity) {
		List<Object> parameters = new ArrayList<Object>();
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Annotation annotation = field.getAnnotation(Parameter.class);
			if (annotation!=null) {
				try {
					parameters.add(field.get(entity)!=null?field.get(entity):"");
				} catch (Exception e) {
					
				}
				break;
			} else {
				continue;
			}
		}
		return (String[]) parameters.toArray();
	}
	
}
