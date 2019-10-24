package com.videojj.server;

import com.videojj.vjtransaction.annotation.VjTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;


    @VjTransactional(isEnd = true)
    @Transactional
    public void test() {
        demoDao.insert("server2");
    }
}
