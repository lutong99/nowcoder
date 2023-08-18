package org.example.nowcoder.service;

import java.util.Date;

public interface DataService {

    Long statisticUV(Date from, Date end);

    void recordIP(String ip);

    Long statisticDAU(Date from, Date end);

    void recordDAU(Integer userId);

}
