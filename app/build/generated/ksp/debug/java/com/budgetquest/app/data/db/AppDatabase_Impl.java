package com.budgetquest.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile CategoryDao _categoryDao;

  private volatile ExpenseDao _expenseDao;

  private volatile BudgetGoalDao _budgetGoalDao;

  private volatile CategoryLimitDao _categoryLimitDao;

  private volatile BadgeDao _badgeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `UserEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `username` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `securityQuestion` TEXT NOT NULL, `securityAnswer` TEXT NOT NULL, `xp` INTEGER NOT NULL, `level` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_UserEntity_username` ON `UserEntity` (`username`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `CategoryEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT NOT NULL, `emoji` TEXT NOT NULL, `colorHex` TEXT NOT NULL, FOREIGN KEY(`userId`) REFERENCES `UserEntity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_CategoryEntity_userId` ON `CategoryEntity` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ExpenseEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `categoryId` INTEGER NOT NULL, `amountZar` REAL NOT NULL, `date` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `description` TEXT NOT NULL, `receiptPhotoPath` TEXT, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `UserEntity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`categoryId`) REFERENCES `CategoryEntity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ExpenseEntity_userId` ON `ExpenseEntity` (`userId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ExpenseEntity_categoryId` ON `ExpenseEntity` (`categoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ExpenseEntity_date` ON `ExpenseEntity` (`date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `BudgetGoalEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `monthlyMin` REAL NOT NULL, `monthlyMax` REAL NOT NULL, `month` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_BudgetGoalEntity_userId_month` ON `BudgetGoalEntity` (`userId`, `month`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `CategoryLimitEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `categoryId` INTEGER NOT NULL, `limitAmount` REAL NOT NULL, `month` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_CategoryLimitEntity_userId_categoryId_month` ON `CategoryLimitEntity` (`userId`, `categoryId`, `month`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `BadgeEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `badgeKey` TEXT NOT NULL, `earnedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_BadgeEntity_userId_badgeKey` ON `BadgeEntity` (`userId`, `badgeKey`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1e380938aeed52016be0ae540cef26bb')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `UserEntity`");
        db.execSQL("DROP TABLE IF EXISTS `CategoryEntity`");
        db.execSQL("DROP TABLE IF EXISTS `ExpenseEntity`");
        db.execSQL("DROP TABLE IF EXISTS `BudgetGoalEntity`");
        db.execSQL("DROP TABLE IF EXISTS `CategoryLimitEntity`");
        db.execSQL("DROP TABLE IF EXISTS `BadgeEntity`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUserEntity = new HashMap<String, TableInfo.Column>(8);
        _columnsUserEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEntity.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEntity.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEntity.put("securityQuestion", new TableInfo.Column("securityQuestion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEntity.put("securityAnswer", new TableInfo.Column("securityAnswer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEntity.put("xp", new TableInfo.Column("xp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEntity.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEntity.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserEntity = new HashSet<TableInfo.Index>(1);
        _indicesUserEntity.add(new TableInfo.Index("index_UserEntity_username", true, Arrays.asList("username"), Arrays.asList("ASC")));
        final TableInfo _infoUserEntity = new TableInfo("UserEntity", _columnsUserEntity, _foreignKeysUserEntity, _indicesUserEntity);
        final TableInfo _existingUserEntity = TableInfo.read(db, "UserEntity");
        if (!_infoUserEntity.equals(_existingUserEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "UserEntity(com.budgetquest.app.data.db.UserEntity).\n"
                  + " Expected:\n" + _infoUserEntity + "\n"
                  + " Found:\n" + _existingUserEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsCategoryEntity = new HashMap<String, TableInfo.Column>(5);
        _columnsCategoryEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryEntity.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryEntity.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryEntity.put("emoji", new TableInfo.Column("emoji", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryEntity.put("colorHex", new TableInfo.Column("colorHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategoryEntity = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCategoryEntity.add(new TableInfo.ForeignKey("UserEntity", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCategoryEntity = new HashSet<TableInfo.Index>(1);
        _indicesCategoryEntity.add(new TableInfo.Index("index_CategoryEntity_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoCategoryEntity = new TableInfo("CategoryEntity", _columnsCategoryEntity, _foreignKeysCategoryEntity, _indicesCategoryEntity);
        final TableInfo _existingCategoryEntity = TableInfo.read(db, "CategoryEntity");
        if (!_infoCategoryEntity.equals(_existingCategoryEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "CategoryEntity(com.budgetquest.app.data.db.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategoryEntity + "\n"
                  + " Found:\n" + _existingCategoryEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsExpenseEntity = new HashMap<String, TableInfo.Column>(10);
        _columnsExpenseEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("amountZar", new TableInfo.Column("amountZar", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("startTime", new TableInfo.Column("startTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("endTime", new TableInfo.Column("endTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("receiptPhotoPath", new TableInfo.Column("receiptPhotoPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenseEntity.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExpenseEntity = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysExpenseEntity.add(new TableInfo.ForeignKey("UserEntity", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        _foreignKeysExpenseEntity.add(new TableInfo.ForeignKey("CategoryEntity", "CASCADE", "NO ACTION", Arrays.asList("categoryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExpenseEntity = new HashSet<TableInfo.Index>(3);
        _indicesExpenseEntity.add(new TableInfo.Index("index_ExpenseEntity_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        _indicesExpenseEntity.add(new TableInfo.Index("index_ExpenseEntity_categoryId", false, Arrays.asList("categoryId"), Arrays.asList("ASC")));
        _indicesExpenseEntity.add(new TableInfo.Index("index_ExpenseEntity_date", false, Arrays.asList("date"), Arrays.asList("ASC")));
        final TableInfo _infoExpenseEntity = new TableInfo("ExpenseEntity", _columnsExpenseEntity, _foreignKeysExpenseEntity, _indicesExpenseEntity);
        final TableInfo _existingExpenseEntity = TableInfo.read(db, "ExpenseEntity");
        if (!_infoExpenseEntity.equals(_existingExpenseEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "ExpenseEntity(com.budgetquest.app.data.db.ExpenseEntity).\n"
                  + " Expected:\n" + _infoExpenseEntity + "\n"
                  + " Found:\n" + _existingExpenseEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgetGoalEntity = new HashMap<String, TableInfo.Column>(5);
        _columnsBudgetGoalEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoalEntity.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoalEntity.put("monthlyMin", new TableInfo.Column("monthlyMin", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoalEntity.put("monthlyMax", new TableInfo.Column("monthlyMax", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoalEntity.put("month", new TableInfo.Column("month", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgetGoalEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBudgetGoalEntity = new HashSet<TableInfo.Index>(1);
        _indicesBudgetGoalEntity.add(new TableInfo.Index("index_BudgetGoalEntity_userId_month", true, Arrays.asList("userId", "month"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoBudgetGoalEntity = new TableInfo("BudgetGoalEntity", _columnsBudgetGoalEntity, _foreignKeysBudgetGoalEntity, _indicesBudgetGoalEntity);
        final TableInfo _existingBudgetGoalEntity = TableInfo.read(db, "BudgetGoalEntity");
        if (!_infoBudgetGoalEntity.equals(_existingBudgetGoalEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "BudgetGoalEntity(com.budgetquest.app.data.db.BudgetGoalEntity).\n"
                  + " Expected:\n" + _infoBudgetGoalEntity + "\n"
                  + " Found:\n" + _existingBudgetGoalEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsCategoryLimitEntity = new HashMap<String, TableInfo.Column>(5);
        _columnsCategoryLimitEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryLimitEntity.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryLimitEntity.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryLimitEntity.put("limitAmount", new TableInfo.Column("limitAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryLimitEntity.put("month", new TableInfo.Column("month", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategoryLimitEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategoryLimitEntity = new HashSet<TableInfo.Index>(1);
        _indicesCategoryLimitEntity.add(new TableInfo.Index("index_CategoryLimitEntity_userId_categoryId_month", true, Arrays.asList("userId", "categoryId", "month"), Arrays.asList("ASC", "ASC", "ASC")));
        final TableInfo _infoCategoryLimitEntity = new TableInfo("CategoryLimitEntity", _columnsCategoryLimitEntity, _foreignKeysCategoryLimitEntity, _indicesCategoryLimitEntity);
        final TableInfo _existingCategoryLimitEntity = TableInfo.read(db, "CategoryLimitEntity");
        if (!_infoCategoryLimitEntity.equals(_existingCategoryLimitEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "CategoryLimitEntity(com.budgetquest.app.data.db.CategoryLimitEntity).\n"
                  + " Expected:\n" + _infoCategoryLimitEntity + "\n"
                  + " Found:\n" + _existingCategoryLimitEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsBadgeEntity = new HashMap<String, TableInfo.Column>(4);
        _columnsBadgeEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBadgeEntity.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBadgeEntity.put("badgeKey", new TableInfo.Column("badgeKey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBadgeEntity.put("earnedAt", new TableInfo.Column("earnedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBadgeEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBadgeEntity = new HashSet<TableInfo.Index>(1);
        _indicesBadgeEntity.add(new TableInfo.Index("index_BadgeEntity_userId_badgeKey", true, Arrays.asList("userId", "badgeKey"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoBadgeEntity = new TableInfo("BadgeEntity", _columnsBadgeEntity, _foreignKeysBadgeEntity, _indicesBadgeEntity);
        final TableInfo _existingBadgeEntity = TableInfo.read(db, "BadgeEntity");
        if (!_infoBadgeEntity.equals(_existingBadgeEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "BadgeEntity(com.budgetquest.app.data.db.BadgeEntity).\n"
                  + " Expected:\n" + _infoBadgeEntity + "\n"
                  + " Found:\n" + _existingBadgeEntity);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1e380938aeed52016be0ae540cef26bb", "dde37d468e6d1420c6a339453be7cba8");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "UserEntity","CategoryEntity","ExpenseEntity","BudgetGoalEntity","CategoryLimitEntity","BadgeEntity");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `UserEntity`");
      _db.execSQL("DELETE FROM `CategoryEntity`");
      _db.execSQL("DELETE FROM `ExpenseEntity`");
      _db.execSQL("DELETE FROM `BudgetGoalEntity`");
      _db.execSQL("DELETE FROM `CategoryLimitEntity`");
      _db.execSQL("DELETE FROM `BadgeEntity`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExpenseDao.class, ExpenseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetGoalDao.class, BudgetGoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryLimitDao.class, CategoryLimitDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BadgeDao.class, BadgeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public ExpenseDao expenseDao() {
    if (_expenseDao != null) {
      return _expenseDao;
    } else {
      synchronized(this) {
        if(_expenseDao == null) {
          _expenseDao = new ExpenseDao_Impl(this);
        }
        return _expenseDao;
      }
    }
  }

  @Override
  public BudgetGoalDao budgetGoalDao() {
    if (_budgetGoalDao != null) {
      return _budgetGoalDao;
    } else {
      synchronized(this) {
        if(_budgetGoalDao == null) {
          _budgetGoalDao = new BudgetGoalDao_Impl(this);
        }
        return _budgetGoalDao;
      }
    }
  }

  @Override
  public CategoryLimitDao categoryLimitDao() {
    if (_categoryLimitDao != null) {
      return _categoryLimitDao;
    } else {
      synchronized(this) {
        if(_categoryLimitDao == null) {
          _categoryLimitDao = new CategoryLimitDao_Impl(this);
        }
        return _categoryLimitDao;
      }
    }
  }

  @Override
  public BadgeDao badgeDao() {
    if (_badgeDao != null) {
      return _badgeDao;
    } else {
      synchronized(this) {
        if(_badgeDao == null) {
          _badgeDao = new BadgeDao_Impl(this);
        }
        return _badgeDao;
      }
    }
  }
}
