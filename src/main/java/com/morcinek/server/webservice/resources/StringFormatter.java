package com.morcinek.server.webservice.resources;

import com.google.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 2/10/13
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class StringFormatter {

    public String getCapitalize(String string){
        return string.toUpperCase();
//        return StringUtils.capitalize(string);
    }
}
