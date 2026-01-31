package org.greenrobot.greendao.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.identityscope.IdentityScopeObject;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

/* loaded from: classes.dex */
public final class DaoConfig implements Cloneable {
    public final String[] allColumns;
    public final Database db;
    private IdentityScope<?, ?> identityScope;
    public final boolean keyIsNumeric;
    public final String[] nonPkColumns;
    public final String[] pkColumns;
    public final Property pkProperty;
    public final Property[] properties;
    public final TableStatements statements;
    public final String tablename;

    public DaoConfig(Database db, Class<? extends AbstractDao<?, ?>> daoClass) {
        this.db = db;
        try {
            this.tablename = (String) daoClass.getField("TABLENAME").get(null);
            Property[] properties = reflectProperties(daoClass);
            this.properties = properties;
            this.allColumns = new String[properties.length];
            List<String> pkColumnList = new ArrayList<>();
            List<String> nonPkColumnList = new ArrayList<>();
            Property lastPkProperty = null;
            for (int i = 0; i < properties.length; i++) {
                Property property = properties[i];
                String name = property.columnName;
                this.allColumns[i] = name;
                if (property.primaryKey) {
                    pkColumnList.add(name);
                    lastPkProperty = property;
                } else {
                    nonPkColumnList.add(name);
                }
            }
            String[] nonPkColumnsArray = new String[nonPkColumnList.size()];
            this.nonPkColumns = (String[]) nonPkColumnList.toArray(nonPkColumnsArray);
            String[] pkColumnsArray = new String[pkColumnList.size()];
            this.pkColumns = (String[]) pkColumnList.toArray(pkColumnsArray);
            this.pkProperty = this.pkColumns.length != 1 ? null : lastPkProperty;
            this.statements = new TableStatements(db, this.tablename, this.allColumns, this.pkColumns);
            if (this.pkProperty != null) {
                Class<?> type = this.pkProperty.type;
                this.keyIsNumeric = type.equals(Long.TYPE) || type.equals(Long.class) || type.equals(Integer.TYPE) || type.equals(Integer.class) || type.equals(Short.TYPE) || type.equals(Short.class) || type.equals(Byte.TYPE) || type.equals(Byte.class);
            } else {
                this.keyIsNumeric = false;
            }
        } catch (Exception e) {
            throw new DaoException("Could not init DAOConfig", e);
        }
    }

    private static Property[] reflectProperties(Class<? extends AbstractDao<?, ?>> daoClass) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException {
        Class<?> propertiesClass = Class.forName(daoClass.getName() + "$Properties");
        Field[] fields = propertiesClass.getDeclaredFields();
        ArrayList<Property> propertyList = new ArrayList<>();
        for (Field field : fields) {
            if ((field.getModifiers() & 9) == 9) {
                Object fieldValue = field.get(null);
                if (fieldValue instanceof Property) {
                    propertyList.add((Property) fieldValue);
                }
            }
        }
        Property[] properties = new Property[propertyList.size()];
        Iterator<Property> it = propertyList.iterator();
        while (it.hasNext()) {
            Property property = it.next();
            if (properties[property.ordinal] != null) {
                throw new DaoException("Duplicate property ordinals");
            }
            properties[property.ordinal] = property;
        }
        return properties;
    }

    public DaoConfig(DaoConfig source) {
        this.db = source.db;
        this.tablename = source.tablename;
        this.properties = source.properties;
        this.allColumns = source.allColumns;
        this.pkColumns = source.pkColumns;
        this.nonPkColumns = source.nonPkColumns;
        this.pkProperty = source.pkProperty;
        this.statements = source.statements;
        this.keyIsNumeric = source.keyIsNumeric;
    }

    public DaoConfig clone() {
        return new DaoConfig(this);
    }

    public IdentityScope<?, ?> getIdentityScope() {
        return this.identityScope;
    }

    public void setIdentityScope(IdentityScope<?, ?> identityScope) {
        this.identityScope = identityScope;
    }

    public void initIdentityScope(IdentityScopeType type) {
        if (type == IdentityScopeType.None) {
            this.identityScope = null;
        } else {
            if (type == IdentityScopeType.Session) {
                if (this.keyIsNumeric) {
                    this.identityScope = new IdentityScopeLong();
                    return;
                } else {
                    this.identityScope = new IdentityScopeObject();
                    return;
                }
            }
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
