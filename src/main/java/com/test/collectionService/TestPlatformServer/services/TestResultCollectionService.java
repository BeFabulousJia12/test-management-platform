package com.test.collectionService.TestPlatformServer.services;


import com.test.collectionService.TestPlatformServer.mapper.TestCaseManagementMapper;
import com.test.collectionService.TestPlatformServer.model.*;
import com.test.collectionService.TestPlatformServer.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

/**
 * @Author You Jia
 * @Date 8/2/2018 4:05 PM
 */
@Service
public class TestResultCollectionService {
    @Autowired
    TestCaseManagementMapper testCaseManagementMapper;
    private static String admin = "admin";
    //processing test cases
    public void insertTestCase(List<TestCaseInfo> testCaseInfoList){
        //delete the same test cases in order to keep the newest test cases
        testCaseManagementMapper.deleteTestCaseByTestNameByUser(testCaseInfoList.get(0).getTestName(),testCaseInfoList.get(0).getUser());
        testCaseManagementMapper.insertTestCaseByBatch(testCaseInfoList);
    }
    //processing test results
    public void insertTestResults(List<TestResults> testResultsList){
        testCaseManagementMapper.insertTestResultsByBatch(testResultsList);
    }
    //processing test result summary
    public void insertTestResultSummary(TestResultSummary testResultSummary){
        testCaseManagementMapper.insertTestResultSummary(testResultSummary);
    }
    //get latest test result summary list
    public List<TestResultSummary> getLatestTestResultSummaryListByUser(String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getLatestTestResultSummary();
        }
        return testCaseManagementMapper.getLatestTestResultSummaryByUser(user);
    }
   //get latest test cases
    public List<TestCaseInfo> getLatestTestCaseInfo(String testName,String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getAllTestCaseInfo(testName);
        }
        return testCaseManagementMapper.getAllTestCaseInfoByUser(testName,user);
    }
    //get Test Results according to testName and timestamp
    public List<TestResults> getHistoryTestResults(String testName, Timestamp timestamp,String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getTestResults(testName,timestamp);
        }
        return  testCaseManagementMapper.getTestResultsByUser(testName,timestamp,user);
    }
    //get Failed Test Results according to testName and timestamp
    public List<TestResults> getHistoryFailedTestResults(String testName, Timestamp timestamp,String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getFailedTestResults(testName,timestamp);
        }
        return  testCaseManagementMapper.getFailedTestResultsByUser(testName,timestamp,user);
    }
    //get Failed Test Results according to testName and timestamp
    public TestResultSummary getHistoryFailedTestSummaryByUser(String testName, Timestamp timestamp,String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getHistoryLatestTestResultSummary(testName,timestamp);
        }
        return  testCaseManagementMapper.getHistoryFailedTestSummaryByUser(testName,timestamp,user);
    }
    //get latest Test Summary according to testName
    public TestResultSummary getLatestTestResultSummaryByTestname(String testName,String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getLatestTestResultSummaryByTestname(testName);
        }
        return  testCaseManagementMapper.getLatestTestResultSummaryByTestnameByUser(testName,user);
    }
    //get history Test Summary List according to testName
    public List<TestResultSummary> getHistoryTestResultSummaryListByTestnameByUser(String testName,Timestamp from, Timestamp to, int limit, String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getHistoryTestResultSummaryListByTestname(testName,from,to,limit);
        }
        return  testCaseManagementMapper.getHistoryTestResultSummaryListByTestnameByUser(testName,from,to,limit,user);
    }

    //get latest Failed Test Results according to testName
    public List<TestResults> getLatestFailedTestResultsByTestname(String testName,String user){
        TestResultSummary testResultSummary = testCaseManagementMapper.getLatestTestResultSummaryByTestname(testName);
        if(user.equals(admin)){
            return testCaseManagementMapper.getFailedTestResults(testName,testResultSummary.getTimestamp());
        }
        return  testCaseManagementMapper.getFailedTestResultsByUser(testName,testResultSummary.getTimestamp(),user);
    }

    //get distinct test names
    public List<TestName> getTestNameList(String user){
        if(user.equals(admin)){
            return testCaseManagementMapper.getTestnameList();
        }
        return  testCaseManagementMapper.getTestnameListByUser(user);
    }

    //check if account name and password is correct
    public boolean checkAccountInfo(String userName, String password){
        return testCaseManagementMapper.getAccountInfo(userName,password)==0?false:true;
    }
    //check if account name existed when new user register an account
    public boolean checkIfAccountExisted(String userName){
        return testCaseManagementMapper.getAccountNameWhenRegisterNewAccount(userName)==0?false:true;
    }
    //create new account
    public void registerNewAccountInfo(AccountInfo accountInfo){
        testCaseManagementMapper.insertAccountInfo(accountInfo);
    }
    //check if client from 3rd party has the correct username and password
    public boolean isValidPassword(AccountInfo ac) {
        //Read account info from database
        if (checkAccountInfo(ac.getUsername(),ac.getPassword()))   {
            long currentTime = new Date().getTime();
            if(abs(currentTime - ac.timestamp.getTime()) <= 7200000){
                return true;
            }

        }
        return false;
    }

}
