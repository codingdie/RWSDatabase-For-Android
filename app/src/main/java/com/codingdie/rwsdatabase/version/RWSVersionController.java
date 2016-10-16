package com.codingdie.rwsdatabase.version;

import android.os.Handler;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.log.RWSLogUtil;
import com.codingdie.rwsdatabase.exception.RWSVersionException;
import com.codingdie.rwsdatabase.version.imp.UpgradeDatabaseListener;
import com.codingdie.rwsdatabase.version.imp.RWSVersionControllerInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xupeng on 2016/8/25.
 */
public class RWSVersionController implements RWSVersionControllerInterface {
    @Override
    public void createOrUpgradeDatabase(int versionFinal, Class versionManagerClass, WritableConnection db,final UpgradeDatabaseListener upgradeDatabaseListener,Handler mainHandler) {
        try {
            db.beginTransaction();
            Object versionManager=versionManagerClass.newInstance();
            int versionBegin=db.getVersion();
            RWSLogUtil.log("versionBegin:"+versionBegin);

            if(versionBegin==0){
                versionBegin = getMaxVersionForMethodToCreateDatabase(versionManagerClass);

                RWSLogUtil.log("maxVersionForMethod:"+versionBegin);
                createDatabase(db,versionManager, versionBegin);
            }
            if(versionFinal>versionBegin){
                if(upgradeDatabaseListener!=null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            upgradeDatabaseListener.beginUpgrade();
                            upgradeDatabaseListener.progress(0.0);
                        }
                    });
                }
            }
            for (int i=versionBegin;i<versionFinal;i++){
                RWSLogUtil.log("versionNow:"+i+"  versionFinal:"+versionFinal);

                upgradeDatabase(db,versionManager,i,i+1);
                final   double progress = ((i + 1 - versionBegin) * 1.0) / (versionFinal - versionBegin);

                if(upgradeDatabaseListener!=null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            upgradeDatabaseListener.progress(progress);
                        }
                    });
                }
            }

            db.setTransactionSuccessful();
            db.setVersion(versionFinal);
            if(versionFinal>versionBegin){
                if(upgradeDatabaseListener!=null) {

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            upgradeDatabaseListener.endUpgrade();
                        }
                    });
                }
            }
        } catch (RWSVersionException e){
             throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new RWSVersionException(RWSVersionException.FAILED_CREATE_OR_UPGRADE_DATABASE);
        }finally {
            db.endTransaction();
        }
    }

    private void upgradeDatabase(WritableConnection db, Object versionManager, int versionBegin, int versionEnd) throws InvocationTargetException, IllegalAccessException {
        try {
            String methodName=String.format("version%dToVersion%d",versionBegin,versionEnd);
            Method method= versionManager.getClass().getMethod(methodName,WritableConnection.class);
            method.invoke(versionManager,db);
        } catch (NoSuchMethodException e) {
            throw  new RWSVersionException(String.format(RWSVersionException.NO_UPGRADE_METHOD,versionBegin,versionEnd));
        }
    }

    private void createDatabase(WritableConnection db, Object versionManager, int version) throws InvocationTargetException, IllegalAccessException {
        try {
            if(version==1){
                String methodName=String.format("createDatabase");
                Method method= versionManager.getClass().getMethod(methodName,WritableConnection.class);
                method.invoke(versionManager,db);
            }else if(version>1){
                String methodName=String.format("createDatabaseFromVersion%d",version);
                Method method= versionManager.getClass().getMethod(methodName,WritableConnection.class);
                method.invoke(versionManager,db);
            }
        } catch (NoSuchMethodException e) {
            throw  new RWSVersionException(String.format(RWSVersionException.NO_CREATE_METHOD,version));
        }
    }

    private int getMaxVersionForMethodToCreateDatabase(Class versionManagerClass) {
        try {
            int maxVersion=1;
            Method[] allMethods=versionManagerClass.getMethods();
            for (int i=0;i<allMethods.length;i++){
                Method method=allMethods[i];
                if (method.getName().contains("createDatabaseFromVersion")){
                    int version=Integer.valueOf(method.getName().replace("createDatabaseFromVersion","")).intValue();
                    if(version>maxVersion){
                        maxVersion=version;
                    }
                }
            }
            return maxVersion;
        } catch (Exception e) {
            return 1;
        }
    }
}
