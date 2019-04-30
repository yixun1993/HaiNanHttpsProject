package com.example.hainanhttpsproject.Dao;

import Impl.DbMangerImpl;
import JsonBean.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class DbManger implements DbMangerImpl {
    private SQLiteDatabase db;
    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoReadMaster;
    private DaoMaster mDaoWriteMaster;
    private DaoSession mDaoRSession;
    private DaoSession mDaoWSession;
    private WeakReference<Context> mContext;
    private volatile static DbManger mSingleton = null;
    public static DbManger getInstance() {
        if (mSingleton == null) {
            synchronized (DbManger.class) {
                if (mSingleton == null) {
                    mSingleton = new DbManger();
                }
            }
        }
        return mSingleton;

    }

    private SQLiteDatabase getReadableDatabase(){
        return mHelper.getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase(){
        return mHelper.getWritableDatabase();
    }



    @Override
    public void InitDb(Context context) {
        mContext = new WeakReference<Context>(context);
        mHelper = new DaoMaster.DevOpenHelper(mContext.get(),"hnsmz-db",null);
        mDaoReadMaster = new DaoMaster(getReadableDatabase());
        mDaoWriteMaster = new DaoMaster(getWritableDatabase());
        mDaoRSession = mDaoReadMaster.newSession();
        mDaoWSession = mDaoWriteMaster.newSession();
    }

    @Override
    public void addPerson(GetAddPerson addPerson) {//新增
        mDaoWSession.getGetAddPersonDao().insert(addPerson);
    }

    @Override
    public void deletePerson(GetAddPerson addPerson) {//删除
        mDaoWSession.getGetAddPersonDao().delete(addPerson);
    }

    @Override
    public GetAddPerson queryPersonByID(String id) {
        return mDaoRSession.getGetAddPersonDao().queryBuilder().where(GetAddPersonDao.Properties.User_id.eq(id)).unique();
    }

    @Override
    public void cleanAllPerson() {//清空
        mDaoWSession.getGetAddPersonDao().deleteAll();
    }

    @Override
    public void cleanGetPseronAll() {
       mDaoRSession.getGetAddPersonDao().deleteAll();
    }

    @Override
    public void cleanGetPersonListBeanAll() {
        mDaoRSession.getGetAddPersonDao().deleteAll();
    }
    @Override
    public List<GetAddPerson> findPeson() {
        return  mDaoWSession.getGetAddPersonDao().loadAll();
    }

    @Override
    public void addUploadLogsBo(Uploadlogs uploadlogs) {
       mDaoRSession.getUploadlogsDao().insert(uploadlogs);
    }

    @Override
    public void deleteUploadData() {
        mDaoRSession.getUploadlogsDao().deleteAll();
    }

    @Override
    public List<Uploadlogs> queryUploadData() {
        return mDaoRSession.getUploadlogsDao().loadAll();
    }

    @Override
    public void addErrorPserson(ErrorPerson errorPerson) {
        mDaoRSession.getErrorPersonDao().insert(errorPerson);
    }

//    @Override
//    public ErrorPerson queryByUserId(String id) {
//        return mDaoRSession.getErrorPersonDao().queryBuilder().where(ErrorPersonDao.Properties.User_id.eq(id)).unique();
//    }
}
