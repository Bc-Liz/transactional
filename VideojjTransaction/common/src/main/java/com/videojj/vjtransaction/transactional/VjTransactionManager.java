package com.videojj.vjtransaction.transactional;

import com.alibaba.fastjson.JSONObject;
import com.videojj.vjtransaction.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author videopls
 */
@Component
public class VjTransactionManager {


    private static NettyClient nettyClient;

    private static ThreadLocal<VjTransaction> current = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<>();

    @Autowired
    public void setNettyClient(NettyClient nettyClient) {
        VjTransactionManager.nettyClient = nettyClient;
    }

    public static Map<String, VjTransaction> VJ_TRANSACION_MAP = new HashMap<>();

    public static String createLbTransactionGroup() {
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", groupId);
        jsonObject.put("command", "create");
        nettyClient.send(jsonObject);
        System.out.println("创建事务组");

        currentGroupId.set(groupId);
        return groupId;
    }

    public static VjTransaction createLbTransaction(String groupId) {
        String transactionId = UUID.randomUUID().toString();
        VjTransaction vjTransaction = new VjTransaction(groupId, transactionId);
        VJ_TRANSACION_MAP.put(groupId, vjTransaction);
        current.set(vjTransaction);
        addTransactionCount();

        System.out.println("创建事务");

        return vjTransaction;
    }

    public static VjTransaction addLbTransaction(VjTransaction vjTransaction, Boolean isEnd, TransactionType transactionType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", vjTransaction.getGroupId());
        jsonObject.put("transactionId", vjTransaction.getTransactionId());
        jsonObject.put("transactionType", transactionType);
        jsonObject.put("command", "add");
        jsonObject.put("isEnd", isEnd);
        jsonObject.put("transactionCount", VjTransactionManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("添加事务");
        return vjTransaction;
    }

    public static VjTransaction getLbTransaction(String groupId) {
        return VJ_TRANSACION_MAP.get(groupId);
    }

    public static VjTransaction getCurrent() {
        return current.get();
    }
    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionCount.get();
    }

    public static void setTransactionCount(int i) {
        transactionCount.set(i);
    }

    public static Integer addTransactionCount() {
        int i = (transactionCount.get() == null ? 0 : transactionCount.get()) + 1;
        transactionCount.set(i);
        return i;
    }
}
