package com.leige.ZookeeperTest.util;

import org.apache.curator.utils.ZKPaths;

public class MoreZKPaths {

    private MoreZKPaths() {
    }

    public static String makeAbsolutePath(String relativePath) {
        return ZKPaths.makePath(ZKPaths.PATH_SEPARATOR, relativePath);
    }

}
