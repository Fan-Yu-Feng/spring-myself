package org.example.core;

import org.example.application.MyApplicationContext;



public interface ApplicationContextAware {
    void setApplicationContext(MyApplicationContext myApplicationContext);
}
