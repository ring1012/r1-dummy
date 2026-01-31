package org.litepal;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.annotation.Column;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.exceptions.DatabaseGenerateException;
import org.litepal.parser.LitePalAttr;
import org.litepal.tablemanager.model.AssociationsModel;
import org.litepal.tablemanager.model.ColumnModel;
import org.litepal.tablemanager.model.TableModel;
import org.litepal.tablemanager.typechange.BlobOrm;
import org.litepal.tablemanager.typechange.BooleanOrm;
import org.litepal.tablemanager.typechange.DateOrm;
import org.litepal.tablemanager.typechange.DecimalOrm;
import org.litepal.tablemanager.typechange.NumericOrm;
import org.litepal.tablemanager.typechange.OrmChange;
import org.litepal.tablemanager.typechange.TextOrm;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* loaded from: classes.dex */
public abstract class LitePalBase {
    private static final int GET_ASSOCIATIONS_ACTION = 1;
    private static final int GET_ASSOCIATION_INFO_ACTION = 2;
    public static final String TAG = "LitePalBase";
    private Collection<AssociationsInfo> mAssociationInfos;
    private Collection<AssociationsModel> mAssociationModels;
    private OrmChange[] typeChangeRules = {new NumericOrm(), new TextOrm(), new BooleanOrm(), new DecimalOrm(), new DateOrm(), new BlobOrm()};
    private Map<String, List<Field>> classFieldsMap = new HashMap();

    protected TableModel getTableModel(String className) throws ClassNotFoundException {
        String tableName = DBUtility.getTableNameByClassName(className);
        TableModel tableModel = new TableModel();
        tableModel.setTableName(tableName);
        tableModel.setClassName(className);
        List<Field> supportedFields = getSupportedFields(className);
        for (Field field : supportedFields) {
            ColumnModel columnModel = convertFieldToColumnModel(field);
            tableModel.addColumnModel(columnModel);
        }
        return tableModel;
    }

    protected Collection<AssociationsModel> getAssociations(List<String> classNames) throws ClassNotFoundException {
        if (this.mAssociationModels == null) {
            this.mAssociationModels = new HashSet();
        }
        this.mAssociationModels.clear();
        for (String className : classNames) {
            analyzeClassFields(className, 1);
        }
        return this.mAssociationModels;
    }

    protected Collection<AssociationsInfo> getAssociationInfo(String className) throws ClassNotFoundException {
        if (this.mAssociationInfos == null) {
            this.mAssociationInfos = new HashSet();
        }
        this.mAssociationInfos.clear();
        analyzeClassFields(className, 2);
        return this.mAssociationInfos;
    }

    protected List<Field> getSupportedFields(String className) throws ClassNotFoundException {
        List<Field> fieldList = this.classFieldsMap.get(className);
        if (fieldList != null) {
            return fieldList;
        }
        List<Field> supportedFields = new ArrayList<>();
        try {
            Class<?> dynamicClass = Class.forName(className);
            Field[] fields = dynamicClass.getDeclaredFields();
            for (Field field : fields) {
                Column annotation = (Column) field.getAnnotation(Column.class);
                if (annotation == null || !annotation.ignore()) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers)) {
                        Class<?> fieldTypeClass = field.getType();
                        String fieldType = fieldTypeClass.getName();
                        if (BaseUtility.isFieldTypeSupported(fieldType)) {
                            supportedFields.add(field);
                        }
                    }
                }
            }
            this.classFieldsMap.put(className, supportedFields);
            return supportedFields;
        } catch (ClassNotFoundException e) {
            throw new DatabaseGenerateException(DatabaseGenerateException.CLASS_NOT_FOUND + className);
        }
    }

    protected boolean isCollection(Class<?> fieldType) {
        return isList(fieldType) || isSet(fieldType);
    }

    protected boolean isList(Class<?> fieldType) {
        return List.class.isAssignableFrom(fieldType);
    }

    protected boolean isSet(Class<?> fieldType) {
        return Set.class.isAssignableFrom(fieldType);
    }

    protected boolean isIdColumn(String columnName) {
        return "_id".equalsIgnoreCase(columnName) || TtmlNode.ATTR_ID.equalsIgnoreCase(columnName);
    }

    protected String getForeignKeyColumnName(String associatedTableName) {
        return BaseUtility.changeCase(String.valueOf(associatedTableName) + "_id");
    }

    private void analyzeClassFields(String className, int action) throws ClassNotFoundException {
        try {
            Class<?> dynamicClass = Class.forName(className);
            Field[] fields = dynamicClass.getDeclaredFields();
            for (Field field : fields) {
                if (isPrivateAndNonPrimitive(field)) {
                    oneToAnyConditions(className, field, action);
                    manyToAnyConditions(className, field, action);
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new DatabaseGenerateException(DatabaseGenerateException.CLASS_NOT_FOUND + className);
        }
    }

    private boolean isPrivateAndNonPrimitive(Field field) {
        return Modifier.isPrivate(field.getModifiers()) && !field.getType().isPrimitive();
    }

    private void oneToAnyConditions(String className, Field field, int action) throws ClassNotFoundException {
        Class<?> fieldTypeClass = field.getType();
        if (LitePalAttr.getInstance().getClassNames().contains(fieldTypeClass.getName())) {
            Class<?> reverseDynamicClass = Class.forName(fieldTypeClass.getName());
            Field[] reverseFields = reverseDynamicClass.getDeclaredFields();
            boolean reverseAssociations = false;
            for (Field reverseField : reverseFields) {
                if (!Modifier.isStatic(reverseField.getModifiers())) {
                    Class<?> reverseFieldTypeClass = reverseField.getType();
                    if (className.equals(reverseFieldTypeClass.getName())) {
                        if (action == 1) {
                            addIntoAssociationModelCollection(className, fieldTypeClass.getName(), fieldTypeClass.getName(), 1);
                        } else if (action == 2) {
                            addIntoAssociationInfoCollection(className, fieldTypeClass.getName(), fieldTypeClass.getName(), field, reverseField, 1);
                        }
                        reverseAssociations = true;
                    } else if (isCollection(reverseFieldTypeClass)) {
                        String genericTypeName = getGenericTypeName(reverseField);
                        if (className.equals(genericTypeName)) {
                            if (action == 1) {
                                addIntoAssociationModelCollection(className, fieldTypeClass.getName(), className, 2);
                            } else if (action == 2) {
                                addIntoAssociationInfoCollection(className, fieldTypeClass.getName(), className, field, reverseField, 2);
                            }
                            reverseAssociations = true;
                        }
                    }
                }
            }
            if (!reverseAssociations) {
                if (action == 1) {
                    addIntoAssociationModelCollection(className, fieldTypeClass.getName(), fieldTypeClass.getName(), 1);
                } else if (action == 2) {
                    addIntoAssociationInfoCollection(className, fieldTypeClass.getName(), fieldTypeClass.getName(), field, null, 1);
                }
            }
        }
    }

    private void manyToAnyConditions(String className, Field field, int action) throws ClassNotFoundException {
        if (isCollection(field.getType())) {
            String genericTypeName = getGenericTypeName(field);
            if (LitePalAttr.getInstance().getClassNames().contains(genericTypeName)) {
                Class<?> reverseDynamicClass = Class.forName(genericTypeName);
                Field[] reverseFields = reverseDynamicClass.getDeclaredFields();
                boolean reverseAssociations = false;
                for (Field reverseField : reverseFields) {
                    if (!Modifier.isStatic(reverseField.getModifiers())) {
                        Class<?> reverseFieldTypeClass = reverseField.getType();
                        if (className.equals(reverseFieldTypeClass.getName())) {
                            if (action == 1) {
                                addIntoAssociationModelCollection(className, genericTypeName, genericTypeName, 2);
                            } else if (action == 2) {
                                addIntoAssociationInfoCollection(className, genericTypeName, genericTypeName, field, reverseField, 2);
                            }
                            reverseAssociations = true;
                        } else if (isCollection(reverseFieldTypeClass)) {
                            String reverseGenericTypeName = getGenericTypeName(reverseField);
                            if (className.equals(reverseGenericTypeName)) {
                                if (action == 1) {
                                    addIntoAssociationModelCollection(className, genericTypeName, null, 3);
                                } else if (action == 2) {
                                    addIntoAssociationInfoCollection(className, genericTypeName, null, field, reverseField, 3);
                                }
                                reverseAssociations = true;
                            }
                        }
                    }
                }
                if (!reverseAssociations) {
                    if (action == 1) {
                        addIntoAssociationModelCollection(className, genericTypeName, genericTypeName, 2);
                    } else if (action == 2) {
                        addIntoAssociationInfoCollection(className, genericTypeName, genericTypeName, field, null, 2);
                    }
                }
            }
        }
    }

    private void addIntoAssociationModelCollection(String className, String associatedClassName, String classHoldsForeignKey, int associationType) {
        AssociationsModel associationModel = new AssociationsModel();
        associationModel.setTableName(DBUtility.getTableNameByClassName(className));
        associationModel.setAssociatedTableName(DBUtility.getTableNameByClassName(associatedClassName));
        associationModel.setTableHoldsForeignKey(DBUtility.getTableNameByClassName(classHoldsForeignKey));
        associationModel.setAssociationType(associationType);
        this.mAssociationModels.add(associationModel);
    }

    private void addIntoAssociationInfoCollection(String selfClassName, String associatedClassName, String classHoldsForeignKey, Field associateOtherModelFromSelf, Field associateSelfFromOtherModel, int associationType) {
        AssociationsInfo associationInfo = new AssociationsInfo();
        associationInfo.setSelfClassName(selfClassName);
        associationInfo.setAssociatedClassName(associatedClassName);
        associationInfo.setClassHoldsForeignKey(classHoldsForeignKey);
        associationInfo.setAssociateOtherModelFromSelf(associateOtherModelFromSelf);
        associationInfo.setAssociateSelfFromOtherModel(associateSelfFromOtherModel);
        associationInfo.setAssociationType(associationType);
        this.mAssociationInfos.add(associationInfo);
    }

    private String getGenericTypeName(Field field) {
        Type genericType = field.getGenericType();
        if (genericType == null || !(genericType instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Class<?> genericArg = (Class) parameterizedType.getActualTypeArguments()[0];
        return genericArg.getName();
    }

    private ColumnModel convertFieldToColumnModel(Field field) {
        String columnType = null;
        String fieldType = field.getType().getName();
        for (OrmChange ormChange : this.typeChangeRules) {
            columnType = ormChange.object2Relation(fieldType);
            if (columnType != null) {
                break;
            }
        }
        boolean nullable = true;
        boolean unique = false;
        String defaultValue = "";
        Column annotation = (Column) field.getAnnotation(Column.class);
        if (annotation != null) {
            nullable = annotation.nullable();
            unique = annotation.unique();
            defaultValue = annotation.defaultValue();
        }
        ColumnModel columnModel = new ColumnModel();
        columnModel.setColumnName(field.getName());
        columnModel.setColumnType(columnType);
        columnModel.setIsNullable(nullable);
        columnModel.setIsUnique(unique);
        columnModel.setDefaultValue(defaultValue);
        return columnModel;
    }
}
