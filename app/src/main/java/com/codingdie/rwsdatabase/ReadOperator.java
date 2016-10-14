package com.codingdie.rwsdatabase;

import com.codingdie.rwsdatabase.connection.ReadableConnection;

/**
 * Created by xupen on 2016/10/14.
 */
public interface ReadOperator<T> {
    public T exec(ReadableConnection readableConnection);
}
