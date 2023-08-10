package org.example.nowcoder.service;

import org.example.nowcoder.mapper.AlphaDao;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Service
//@Scope("singleton")
public class AlphaService {

    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("AlphaService Constructor");
    }

    @PostConstruct
    public void init() {
        System.out.println("init method PostConstructor");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("pre destroy");
    }

//    @Autowired
    public void setAlphaDao(AlphaDao alphaDao) {
        this.alphaDao = alphaDao;
    }

    public String find() {
        return alphaDao.select();
    }


}
