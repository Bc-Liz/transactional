package com.videojj.server;

import com.videojj.vjtransaction.annotation.VjTransactional;
import com.videojj.vjtransaction.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    @VjTransactional(isStart = true)
    @Transactional
    public void test() {
        demoDao.insert("server1");
        HttpClient.get("http://localhost:8082/server2/test");
        int i = 100/0;
    }
}
