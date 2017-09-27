package ru.metal.dto;

import javafx.beans.property.Property;
import ru.metal.dto.annotations.PredicateField;
import ru.metal.dto.annotations.ValidatableCollection;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.FxHelper;
import ru.metal.security.ejb.dto.AbstractDto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by User on 01.09.2017.
 */
public abstract class FxEntity<T extends AbstractDto> extends AbstractDto {
    public abstract T getEntity();

    Map<String, Map<String, Boolean>> errorFields;

    public boolean hasError() {
        errorFields = validate();
        for (Map<String, Boolean> entry : errorFields.values()) {
            for (Boolean value : entry.values()) {
                if (value) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasError(String fieldName, String transportGuid) {
        if (errorFields == null) {
            errorFields = validate();
        }
        Map<String, Boolean> fields = errorFields.get(transportGuid);
        if (fields != null) {
            return fields.get(fieldName);
        }
        return false;
    }

    public boolean hasError(String fieldName) {
        if (errorFields == null) {
            errorFields = validate();
        }
        return hasError(fieldName, getTransportGuid());
    }

    private Map<String, Map<String, Boolean>> validateSub(Object validatedObject, String transportGuid) {

        List<Field> allDeclaredFields = getAllDeclaredFields(validatedObject.getClass());
        List<Field> predicateFields = getAnnotatedField(validatedObject.getClass(), PredicateField.class);
        for (Field field : allDeclaredFields) {
            try {
                boolean hasError = false;
                boolean isAccesible = field.isAccessible();
                field.setAccessible(true);
                Object fieldValue = field.get(validatedObject);
                field.setAccessible(isAccesible);

                if (fieldValue instanceof Collection) {
                    Collection collection = (Collection) fieldValue;
                    ValidatableCollection annotation = field.getAnnotation(ValidatableCollection.class);
                    boolean result = true;
                    if (annotation != null) {
                        result = resultPredicate(validatedObject, predicateFields, annotation.predicateName());
                        if (result) {
                            int collectionSize = collection == null ? 0 : collection.size();
                            if (collectionSize > annotation.maxSize()) {
                                hasError = true;
                            }
                            if (collection.size() < annotation.minSize()) {
                                hasError = true;
                            }
                        }
                    }
                    for (Object o : collection) {
                        if (!(o instanceof FxEntity)) {
                            break;
                        } else if (result) {
                            Map<String, Map<String, Boolean>> resultValidate = validateSub(o, ((FxEntity) o).getTransportGuid());
                            errorFields.putAll(resultValidate);

                        }
                    }
                } else {
                    Object value;
                    if (fieldValue instanceof Property) {
                        Property property = (Property) fieldValue;
                        value = property.getValue();
                        if (value != null && value.toString().isEmpty()) {
                            value = null;
                        }
                    } else {
                        value = fieldValue;
                    }
                    ValidatableField annotation = field.getAnnotation(ValidatableField.class);
                    boolean result=true;
                    if (annotation != null) {
                        result = resultPredicate(validatedObject, predicateFields, annotation.predicateName());
                        if (result) {
                            if (!annotation.nullable() && value == null) {
                                hasError = true;
                            }
                            String regexp = annotation.regexp();
                            if (!hasError && value != null && !regexp.isEmpty()) {
                                boolean regexpNotValid;
                                if (!(value instanceof Date)) {
                                    regexpNotValid = !value.toString().toLowerCase().matches(regexp.toLowerCase());
                                } else {
                                    regexpNotValid = false;
                                }
                                if (regexpNotValid) {
                                    hasError = true;
                                }
                            }
                        }


                    }
                    if (result && value instanceof FxEntity) {
                        Map<String, Map<String, Boolean>> validateResult = validateSub(value, ((FxEntity) value).getTransportGuid());
                        errorFields.putAll(validateResult);
                    }
                }
                if (!errorFields.containsKey(transportGuid)) {
                    errorFields.put(transportGuid, new HashMap<>());
                }
                if (hasError) {
                    errorFields.get(transportGuid).put(field.getName(), true);
                } else {
                    errorFields.get(transportGuid).put(field.getName(), false);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return errorFields;
    }

    private boolean resultPredicate(Object obj, List<Field> predicates, String predicateName) {
        if (!predicateName.isEmpty()) {
            for (Field predicateField : predicates) {
                if (predicateField.getName().equals(predicateName)) {
                    boolean accessible = predicateField.isAccessible();
                    predicateField.setAccessible(true);
                    Predicate predicate;
                    try {
                        predicate = (Predicate) predicateField.get(obj);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    predicateField.setAccessible(accessible);
                    return predicate.test(obj);
                }
            }
        }
        return true;
    }

    private Map<String, Map<String, Boolean>> validate() {
        errorFields = new HashMap<>();
        return validateSub(this, this.getTransportGuid());
    }

    private List<Field> getAnnotatedField(final Class clazz, final Class<? extends Annotation> annotationClass) {
        List<Field> allFields = getAllDeclaredFields(clazz);
        List<Field> resultFields = new ArrayList<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(annotationClass)) {
                resultFields.add(field);
            }
        }
        return resultFields;
    }

    private List<Field> getAllDeclaredFields(Class<?> type) {
        return getAllDeclaredFields(new LinkedList<Field>(), type);
    }

    private List<Field> getAllDeclaredFields(List<Field> fields, Class<?> type) {
        for (final Field f : type.getDeclaredFields()) {

            boolean exists = false;
            for (final Field ff : fields) {
                if (f.getName().equals(ff.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) fields.add(f);
        }

        if (type.getSuperclass() != null) {
            fields = getAllDeclaredFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public abstract <E extends FxEntity> FxHelper<FxEntity<T>, T> getHelper();
}
