package com.test.collectionService.TestPlatformServer;


import com.alibaba.fastjson.JSONObject;
import com.test.collectionService.TestPlatformServer.model.*;
import com.test.collectionService.TestPlatformServer.services.TestResultCollectionService;
import com.test.collectionService.TestPlatformServer.util.SessionManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;


/**
 * @Author You Jia
 * @Date 8/9/2018 2:20 PM
 */
@Slf4j
@RestController
public class FEController {
    @Autowired
    TestResultCollectionService testResultCollectionService;

    @PostMapping("/api/registration")
    @ResponseBody
    public Result createAccount(@Valid @RequestBody AccountInfo accountInfo){
        log.debug("Register new account...");
        JSONObject response = new JSONObject();
        response.put("username",accountInfo.getUsername());
        if(!testResultCollectionService.checkIfAccountExisted(accountInfo.getUsername())){
            //If username is not created, create this new account
            testResultCollectionService.registerNewAccountInfo(accountInfo);
            return new Result("0","create account success",response);
        }
        return new Result(ExceptionEnum.ACCOUNT_DUPLICATE,response);

    }

    @PostMapping("/api/formlogin")
    @ResponseBody
    public Result logonHomepage(@Valid @RequestBody AccountInfo accountInfo, HttpSession session, HttpServletRequest request,HttpServletResponse response){
        log.debug(accountInfo.getUsername() + " ==> Login the TestPlatform...");
        JSONObject resp = new JSONObject();
        resp.put("username",accountInfo.getUsername());
        if(testResultCollectionService.checkAccountInfo(accountInfo.getUsername(),accountInfo.getPassword())){
            log.debug("session id: " + session.getId());
            SessionManage.addSession2Map(session.getId(),accountInfo.getUsername());
            log.debug("add session id and username into sessionMap");
            Cookie cookie = new Cookie("JSESSIONID",session.getId());
            cookie.setMaxAge(10800);
            cookie.setPath("/");
            //session.setMaxInactiveInterval(60);
            //cookie.setPath(request.getContextPath());
            response.addCookie(cookie);
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            return new Result("0","success","logon successfully.");

        }
        return new Result(ExceptionEnum.LOGIN_ERROR,resp);

    }
    @PostMapping("/api/formlogout")
    @ResponseBody
    public Result logoutHomepage(@RequestBody JSONObject userName, HttpSession session) {
        log.debug(userName.get("username") + " ==> LogOut the TestPlatform...");
            String user = SessionManage.checkValidSession(session.getId());
            if (user != null) {
                //remove session from hashmap
                log.debug("remove session id and username from sessionMap");
                session.invalidate();
                //SessionManage.removeSession2Map(session.getId());

                return new Result("0", "success", "logout successfully.");
            }
            return new Result(ExceptionEnum.SESSION_INVALID, user);
    }
    @GetMapping("/api/functionalLatestTestSummaryList")
    @ResponseBody
    public Object getFunctionTestSummaryList(HttpSession session){
        log.debug("Get Function Test Summary List, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        if(user!=null){
            List<TestResultSummary> testResultSummary = testResultCollectionService.getLatestTestResultSummaryListByUser(user);
            if(testResultSummary.isEmpty()){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResultSummary;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);
    }

    @GetMapping("/api/functionalLatestTestSummary")
    @ResponseBody
    public Object getlatestFunctionTestSummary(@RequestParam("testName") String testName,HttpSession session){
        log.debug("Get Latest Function Test Summary, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        if(user!=null){
            TestResultSummary testResultSummary = testResultCollectionService.getLatestTestResultSummaryByTestname(testName,user);
            if(testResultSummary==null){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResultSummary;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);
    }
    @GetMapping("/api/functionalHistoryTestSummaryList")
    @ResponseBody
    public Object getHistoryFunctionTestSummaryList(@RequestParam("testName") String testName,@RequestParam("from") String startTime,@RequestParam("to") String endTime, @RequestParam("limit") int limit,HttpSession session){
        log.debug("Get History Function Test Summary List, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        Timestamp from = new Timestamp(Instant.parse(startTime).toEpochMilli());
        Timestamp to = new Timestamp(Instant.parse(endTime).toEpochMilli());
        if(user!=null){
            List<TestResultSummary> testResultSummary = testResultCollectionService.getHistoryTestResultSummaryListByTestnameByUser(testName,from,to,limit,user);
            if(testResultSummary==null){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResultSummary;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);
    }

    @GetMapping("/api/functionalHistoryTestSummary")
    @ResponseBody
    public Object getHistoryFunctionTestSummary(@RequestParam("testName") String testName,@RequestParam("timestamp") String timestamp,HttpSession session){
        log.debug("Get History Function Test Summary, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        Timestamp timestamp1 = new Timestamp(Instant.parse(timestamp).toEpochMilli());
        if(user!=null){
            TestResultSummary testResultSummary = testResultCollectionService.getHistoryFailedTestSummaryByUser(testName,timestamp1,user);
            if(testResultSummary==null){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResultSummary;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);
    }

    @GetMapping("/api/functionalLatestFailedTestResults")
    @ResponseBody
    public Object getlatestFailedTestResults(@RequestParam("testName") String testName,HttpSession session){
        log.debug("Get Latest Function Failed Test Results, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        if(user!=null){
            List<TestResults> testResults = testResultCollectionService.getLatestFailedTestResultsByTestname(testName,user);
            if(testResults.isEmpty()){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResults;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);

    }

    @GetMapping("/api/functionalTestNameList")
    @ResponseBody
    public Object getTestNameList(HttpSession session){
        log.debug("Get testName List, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        if(user!=null){
            List<TestName> testResults = testResultCollectionService.getTestNameList(user);
            if(testResults.isEmpty()){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResults;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);

    }


    @GetMapping("/api/functionalHistoryFailedTestResults")
    @ResponseBody
    public Object getHistoryFailedTestResults(@RequestParam("testName") String testName, @RequestParam("timestamp") String timestamp, HttpSession session){
        log.debug("Get Function Failed Test Results, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        Timestamp timestamp1 = new Timestamp(Instant.parse(timestamp).toEpochMilli());
        if(user!=null){
            List<TestResults> testResults = testResultCollectionService.getHistoryFailedTestResults(testName,timestamp1,user);
            if(testResults.isEmpty()){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResults;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);

    }

    @GetMapping("/api/testCaseList")
    @ResponseBody
    public Object getFunctionTestCase(@RequestParam("testName") String testName,HttpSession session){
        log.debug("Get Test Case List, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        if(user!=null){
            List<TestCaseInfo> testCaseInfo = testResultCollectionService.getLatestTestCaseInfo(testName,user);
            if(testCaseInfo.isEmpty()){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testCaseInfo;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);
    }

    @GetMapping("/api/loginstatus")
    @ResponseBody
    public Object getloginstatus(HttpSession session){
        log.debug("Get login status, "  + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        if(user!=null){
            return new Result("0","success",user);
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);
    }

    @GetMapping("/api/functionalHistoryTestResults")
    @ResponseBody
    public Object getFunctionHistoryTestResults(@RequestParam("testName") String testName, @RequestParam("timestamp") String timestamp, HttpSession session){
        log.debug("Get Function Test Results, " + "Session id is: " + session.getId());
        String user = SessionManage.checkValidSession(session.getId());
        Timestamp timestamp1 = new Timestamp(Instant.parse(timestamp).toEpochMilli());
        if(user!=null){
            List<TestResults> testResults = testResultCollectionService.getHistoryTestResults(testName,timestamp1,user);
            if(testResults.isEmpty()){
                return new Result(ExceptionEnum.DATA_NOT_FOUND,user);
            }
            return testResults;
        }
        return new Result(ExceptionEnum.SESSION_INVALID,user);

    }

}
