package tech.com.commoncore.avdb;


import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.FileNotFoundException;

import tech.com.commoncore.utils.FileUtils;
import tech.com.commoncore.utils.ToastUtil;

import static tech.com.commoncore.avdb.AVDbManager.*;

public class AVDbImpl implements AVDb {

    @Override
    public void addResume(String name, String sex, int age, String address, String number, String email,
                          String jobStart, String intention, String jobAddress, String salary, String jobFlag, SaveCallback callback) {
        AVObject avObject = new AVObject(TABLE_RESUME);
        avObject.put(RESUME_USER, AVUser.getCurrentUser().getObjectId());
        avObject.put(RESUME_NAME, name);
        avObject.put(RESUME_SEX, sex);
        avObject.put(RESUME_AGE, age);
        avObject.put(RESUME_ADDRESS, address);
        avObject.put(RESUME_NUMBER, number);
        avObject.put(RESUME_E_MAIL, email);
        avObject.put(RESUME_J_START, jobStart);
        avObject.put(RESUME_INTENTION, intention);
        avObject.put(RESUME_J_ADDRESS, jobAddress);
        avObject.put(RESUME_SALARY, salary);
        avObject.put(RESUME_J_FLAG, jobFlag);
        avObject.saveInBackground(callback);
    }

    @Override
    public void upDateResume(String resumeId, String name, String sex, int age, String address, String number,
                             String email, String jobStart, String intention, String jobAddress, String salary, String jobFlag, SaveCallback callback) {
        AVObject avObject = AVObject.createWithoutData(TABLE_RESUME, resumeId);
        avObject.put(RESUME_NAME, name);
        avObject.put(RESUME_SEX, sex);
        avObject.put(RESUME_AGE, age);
        avObject.put(RESUME_ADDRESS, address);
        avObject.put(RESUME_NUMBER, number);
        avObject.put(RESUME_E_MAIL, email);
        avObject.put(RESUME_J_START, jobStart);
        avObject.put(RESUME_INTENTION, intention);
        avObject.put(RESUME_J_ADDRESS, jobAddress);
        avObject.put(RESUME_SALARY, salary);
        avObject.put(RESUME_J_FLAG, jobFlag);
        avObject.saveInBackground(callback);
    }

    @Override
    public void requestResume(FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(RESUME_NAME);
        query.findInBackground(findCallback);
    }

    @Override
    public void requestResume(String userId, FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(RESUME_NAME);
        query.whereEqualTo(RESUME_USER, AVUser.getCurrentUser().getObjectId());
        query.findInBackground(findCallback);
    }

    @Override
    public void addPrat(String title, String content, long startTime, long endTime, String address, int people, SaveCallback callback) {
        AVObject avObject = new AVObject(TABLE_PARTY);
        avObject.put(PARTY_USER, AVUser.getCurrentUser().getObjectId());
        avObject.put(PARTY_TITLE, title);
        avObject.put(PARTY_CONTENT, content);
        avObject.put(PARTY_START_TIME, startTime);
        avObject.put(PARTY_END_TIME, endTime);
        avObject.put(PARTY_ADDRESS, address);
        avObject.put(PARTY_PEOPLE, people);
        avObject.put(PARTY_STATUS, STATUS_TYPE_UNSTART);
        avObject.saveInBackground(callback);
    }

    @Override
    public void updatePrat(String pratId, String title, String content, long startTime, long endTime, String address, int people, SaveCallback callback) {
        AVObject avObject = AVObject.createWithoutData(TABLE_PARTY, pratId);
        avObject.put(PARTY_TITLE, title);
        avObject.put(PARTY_CONTENT, content);
        avObject.put(PARTY_START_TIME, startTime);
        avObject.put(PARTY_END_TIME, endTime);
        avObject.put(PARTY_ADDRESS, address);
        avObject.put(PARTY_PEOPLE, people);
        avObject.put(PARTY_STATUS, STATUS_TYPE_UNSTART);
        avObject.saveInBackground(callback);
    }

    @Override
    public void requestPrat(FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(TABLE_PARTY);
        query.findInBackground(findCallback);
    }

    @Override
    public void requestPrat(String userId, FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(TABLE_PARTY);
        query.whereEqualTo(PARTY_USER, userId);
        query.findInBackground(findCallback);
    }

    @Override
    public void addPlan(String type, String title, String content, long sTime, long eTime, String status, SaveCallback callback) {
        AVObject avObject = new AVObject(TABLE_PLAN);
        avObject.put(PLAN_USER, AVUser.getCurrentUser().getObjectId());
        avObject.put(PLAN_TYPE, type);
        avObject.put(PLAN_TITLE, title);
        avObject.put(PLAN_CONTENT, content);
        avObject.put(PLAN_START_TIME, sTime);
        avObject.put(PLAN_END_TIME, eTime);
        avObject.put(PLAN_STATUS, status);
        avObject.saveInBackground(callback);
    }

    @Override
    public void updatePlan(String planId, String type, String title, String content, long sTime, long eTime, String status, SaveCallback callback) {
        AVObject avObject = AVObject.createWithoutData(TABLE_PLAN, planId);
        avObject.put(PLAN_TYPE, type);
        avObject.put(PLAN_TITLE, title);
        avObject.put(PLAN_CONTENT, content);
        avObject.put(PLAN_START_TIME, sTime);
        avObject.put(PLAN_END_TIME, eTime);
        avObject.put(PLAN_STATUS, status);
        avObject.saveInBackground(callback);
    }

    @Override
    public void requestPlan(FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(TABLE_PLAN);
        query.findInBackground(findCallback);
    }

    @Override
    public void requestPlan(String userId, FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(TABLE_PLAN);
        query.whereEqualTo(PLAN_USER, AVUser.getCurrentUser().getObjectId());
        query.findInBackground(findCallback);
    }

    @Override
    public AVFile getAVFileByPath(String path) {
        try {
            String fileName = FileUtils.splitFileName(path);
            final AVFile avFile = AVFile.withAbsoluteLocalPath(fileName, path);
            return avFile;
        } catch (FileNotFoundException e) {
            ToastUtil.show("文件未知");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addFile(String file, int grade, String fileName, String fileType, String content, SaveCallback callback) {
        AVObject object = new AVObject(TABLE_MEDIA);
        object.put(MEDIA_USER, AVUser.getCurrentUser().getObjectId());
        object.put(MEDIA_FILE, file);
        object.put(MEDIA_GRADE, file);
        object.put(MEDIA_FILE_NAME, file);
        object.put(MEDIA_FILE_TYPE, file);
        object.put(MEDIA_CONTENT, file);
        object.put(MEDIA_LIKE_COUNT, file);
        object.put(MEDIA_COMMENT_COUNT, file);
        object.put(MEDIA_IS_DELETED, file);
        object.saveInBackground(callback);
    }

    @Override
    public void requestFile(FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(TABLE_MEDIA);
        query.findInBackground(findCallback);
    }

    @Override
    public void requestFile(String userId, FindCallback<AVObject> findCallback) {
        AVQuery<AVObject> query = new AVQuery<>(TABLE_MEDIA);
        query.whereEqualTo(MEDIA_USER, AVUser.getCurrentUser().getObjectId());
        query.findInBackground(findCallback);
    }

}
