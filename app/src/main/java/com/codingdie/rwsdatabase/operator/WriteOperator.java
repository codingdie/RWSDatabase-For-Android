package com.codingdie.rwsdatabase.operator;

import com.codingdie.rwsdatabase.connection.WritableConnection;

/**
 * Created by xupen on 2016/10/14.
 */
public interface WriteOperator {
    public void exec(WritableConnection writableConnection);
}
