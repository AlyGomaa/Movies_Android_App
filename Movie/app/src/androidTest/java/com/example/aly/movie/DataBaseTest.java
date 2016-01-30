package com.example.aly.movie;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;

/**
 * Created by aly on 06/01/2016.
 */
public class DataBaseTest {

    public static Test suit(){
        return new TestSuiteBuilder(DataBaseTest.class).includeAllPackagesUnderHere().build();
    }

    public DataBaseTest(){

        super();
    }
}
