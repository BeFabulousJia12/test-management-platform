package com.test.collectionService.TestPlatformServer.mapper;


import com.test.collectionService.TestPlatformServer.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author You Jia
 * @Date 8/1/2018 1:16 PM
 */
@Mapper
public interface TestCaseManagementMapper {
    //Get test cases by user
    List<TestCaseInfo> getAllTestCaseInfoByUser(@Param("testName")String testName, @Param("user")String user);
    //Get all test cases
    List<TestCaseInfo> getAllTestCaseInfo(@Param("testName")String testName);
    //Insert test cases one by one
    void insertTestCase(TestCaseInfo testCaseInfo);
    //Insert test cases by batch
    void insertTestCaseByBatch(List<TestCaseInfo> testCaseInfoList);
    //Delete test cases by test name
    void deleteTestCaseByTestNameByUser(@Param("name")String name,@Param("user")String user);
    //Insert into test_results
    void insertTestResultsByBatch(List<TestResults> testResultsList);
    //Insert into test_result_summary
    void insertTestResultSummary(TestResultSummary testResultSummary);
    //Query the latest test result

    //Query test results according to different testName and timestamp and user
    List<TestResults> getTestResultsByUser(@Param("testName")String testName, @Param("timestamp")Timestamp timestamp, @Param("user")String user);
    //Query test results according to different testName and timestamp
    List<TestResults> getTestResults(@Param("testName")String testName, @Param("timestamp")Timestamp timestamp);

    //Query failed test results according to different testName and timestamp and user
    List<TestResults> getFailedTestResultsByUser(@Param("testName")String testName, @Param("timestamp")Timestamp timestamp, @Param("user")String user);
    //Query failed test results according to different testName and timestamp
    List<TestResults> getFailedTestResults(@Param("testName")String testName, @Param("timestamp")Timestamp timestamp);

    //Query test result summary for different test_name and user
    TestResultSummary getHistoryFailedTestSummaryByUser(@Param("testName")String testName, @Param("timestamp")Timestamp timestamp,@Param("user")String user);
    //Query latest test result summaries for admin
    TestResultSummary getHistoryLatestTestResultSummary(@Param("testName")String testName, @Param("timestamp")Timestamp timestamp);

    //Query latest test result summaries list for different test_name and user
    List<TestResultSummary> getLatestTestResultSummaryByUser(@Param("user")String user);
    //Query latest test result summaries for different test_name
    List<TestResultSummary> getLatestTestResultSummary();

    //Query latest test result summaries list for different test_name and user
    TestResultSummary getLatestTestResultSummaryByTestnameByUser(@Param("testName")String testName,@Param("user")String user);
    //Query latest test result summaries for different test_name
    TestResultSummary getLatestTestResultSummaryByTestname(@Param("testName")String testName);

    //Query history test result summaries list for different test_name and user
    List<TestResultSummary> getHistoryTestResultSummaryListByTestnameByUser(@Param("testName")String testName,@Param("from")Timestamp from,@Param("to")Timestamp to,@Param("limit")int limit,@Param("user")String user);

    //Query history test result summaries for admin
    List<TestResultSummary> getHistoryTestResultSummaryListByTestname(@Param("testName")String testName,@Param("from")Timestamp from,@Param("to")Timestamp to,@Param("limit")int limit);

    //Query distinct test names from test summery
    List<TestName> getTestnameListByUser(@Param("user")String user);

    //Query distinct test names from test summery for admin
    List<TestName> getTestnameList();

    //Query account info according to user and user password
    int getAccountInfo(@Param("username")String username, @Param("password")String password);

    //Query account name for register service
    int getAccountNameWhenRegisterNewAccount(@Param("username")String username);

    //insert account info when user registered.
    void insertAccountInfo(AccountInfo accountInfo);
}
