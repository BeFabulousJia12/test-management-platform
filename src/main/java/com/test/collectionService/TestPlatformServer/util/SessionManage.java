package com.test.collectionService.TestPlatformServer.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author You Jia
 * @Date 8/11/2018 3:44 PM
 */
public class SessionManage {
    private static ConcurrentHashMap<String,String> sessionMap = new ConcurrentHashMap<>();
    public static void addSession2Map(String sessionId, String user){
        sessionMap.put(sessionId,user);
    }
    public static void removeSession2Map(String sessionId){sessionMap.remove(sessionId);}
    public static String checkValidSession(String sessionId){
        return sessionMap.get(sessionId);
    }
    public static boolean checkUserHasLogin(String username){
        boolean valid = false;
        for(Map.Entry entry: sessionMap.entrySet()){
            if(username.equals(entry.getValue())){
                valid = true;
                break;
            }
        }
        return valid;
    }
}
