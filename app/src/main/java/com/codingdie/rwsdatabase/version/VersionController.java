package com.codingdie.rwsdatabase.version;

import android.os.Handler;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.log.LogUtil;
import com.codingdie.rwsdatabase.version.exception.VersionException;
import com.codingdie.rwsdatabase.version.imp.UpgradeDatabaseListener;
import com.codingdie.rwsdatabase.version.imp.VersionControllerImp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xupen on 2016/8/25.
 */
public class VersionController implements VersionControllerImp {
    @Override
    public void createOrUpgradeDatabase(int versionFinal, Class versionManagerClass, WritableConnection db,final UpgradeDatabaseListener upgradeDatabaseListener,Handler mainHandler) {
        try {
            db.beginTransaction();
            Object versionManager=versionManagerClass.newInstance();
            int versionBegin=db.getVersion();
            LogUtil.log("versionBegin:"+versionBegin);

            if(versionBegin==0){
                versionBegin = getMaxVersionForMethodToCreateDatabase(versionManagerClass);

                LogUtil.log("maxVersionForMethod:"+versionBegin);
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
                LogUtil.log("versionNow:"+i);

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
        } catch (VersionException e){
             throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new VersionException(VersionException.FAILED_CREATE_OR_UPGRADE_DATABASE);
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
            throw  new VersionException(String.format(VersionException.NO_UPGRADE_METHOD,versionBegin,versionEnd));
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
            throw  new VersionException(String.format(VersionException.NO_CREATE_METHOD,version));
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
