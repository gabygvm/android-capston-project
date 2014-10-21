/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.symptom.client;

public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
