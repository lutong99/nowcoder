package org.example.nowcoder.dao.dao.impl;

import org.example.nowcoder.dao.AlphaDao;
import org.springframework.stereotype.Repository;

@Repository
public class AlphaDaoImpl implements AlphaDao {

    @Override
    public String select() {
        return "Select Success";
    }
}
