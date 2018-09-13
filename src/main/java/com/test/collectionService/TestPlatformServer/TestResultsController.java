package com.test.collectionService.TestPlatformServer;

import com.alibaba.fastjson.JSON;
import com.test.collectionService.TestPlatformServer.model.*;
import com.test.collectionService.TestPlatformServer.security.DESDecrypt;
import com.test.collectionService.TestPlatformServer.security.JwtUtil;
import com.test.collectionService.TestPlatformServer.services.TestResultCollectionService;

import com.test.collectionService.TestPlatformServer.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.test.collectionService.TestPlatformServer.util.SessionManage.checkUserHasLogin;

/**
 * @Author You Jia
 * @Date 8/2/2018 4:28 PM
 */
@RestController
@Slf4j
public class TestResultsController {
    @Autowired
    TestResultCollectionService testResultCollectionService;

    @PostMapping("/db/TestCase")
    //@PreAuthorize("hasAuthority('admin')")
    public Result testCasesProcessing(@RequestBody List<TestCaseInfo> testCaseInfo) throws EncodeException, IOException {
        testResultCollectionService.insertTestCase(testCaseInfo);
        log.debug("Test Case Debug!");
        log.info("Insert test cases successfully...");
//        if(WebSocketServer.getOnlineCount() > 0){
//            log.debug("WebSocket Push TestCaseInfo Message to FE.");
//            WebSocketServer.sendInfo(JSON.toJSONString(testCaseInfo),testCaseInfo.get(0).getUser());
//        }
        return new Result("0","success","test cases have inserted into test case table.");
    }
    @PostMapping("/login")
    public Object login(@RequestBody String accountString) throws Exception {

            String decode = new DESDecrypt().decode("9ba45bfd50061212328ec03adfef1b6e75","utf-8",accountString);
            AccountInfo account = JSON.parseObject(decode,AccountInfo.class);
            log.debug("access login to get jwt");
            if(testResultCollectionService.isValidPassword(account)) {
                String jwt = JwtUtil.generateToken(account.getUsername());
                return new HashMap<String,String>(){{
                    put("token", jwt);
                }};
            }else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
    }

    @PostMapping("/db/TestResultSummary")
    public Result testResultSummaryProcessing(@RequestBody TestResultSummary testResultSummary) throws EncodeException, IOException {
        testResultCollectionService.insertTestResultSummary(testResultSummary);
        log.debug("Insert test result summary successfully...");
        if(WebSocketServer.getOnlineCount() > 0){
            log.debug("WebSocket Push TestResultSummary Message to FE.");
            WebSocketServer.sendInfo(JSON.toJSONString(testResultSummary),testResultSummary.getUser());
        }
        return new Result("0","success","test cases have inserted into test result summary table.");
    }
    @PostMapping("/db/TestResults")
    public Result testResultsProcessing(@RequestBody List<TestResults> testResultsList) throws EncodeException, IOException {
        testResultCollectionService.insertTestResults(testResultsList);
        log.debug("Insert test results successfully...");
//        if(WebSocketServer.getOnlineCount() > 0){
//            log.debug("WebSocket Push TestResultsList Message to FE.");
//            WebSocketServer.sendInfo(JSON.toJSONString(testResultsList),testResultsList.get(0).getUser());
//        }
        return new Result("0","success","test cases have inserted into test results table.");
    }

}
