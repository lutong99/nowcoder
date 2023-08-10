package org.example.nowcoder.mapper.impl;

import org.example.nowcoder.mapper.AlphaDao;
import org.springframework.stereotype.Repository;

//@Repository
public class AlphaDaoImpl implements AlphaDao {

    @Override
    public String select() {
        return "Select Success";
    }
}
