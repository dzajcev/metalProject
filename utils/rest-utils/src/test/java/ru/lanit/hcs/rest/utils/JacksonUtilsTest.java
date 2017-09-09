package ru.lanit.hcs.rest.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by akurilyonok on 6/14/2014.
 */

public class JacksonUtilsTest {
    private static final String cookieValue="{\"username\":\"uospeclast_name uospecfirst_name uospecmiddleName\",\"userId\":\"4cffa751-9a56-4460-b5c9-7db3c6385096\",\"orgOid\":\"1b424148-ea6f-421b-8657-470c6d98b468\",\"loginMethod\":\"PWD\",\"organizationGuid\":\"40f43a4a-deb4-41ab-b552-04bb8843bc80\",\"oktmoGuids\":[\"5349c064-c9b5-480b-b412-6a764240cbfd\"],\"userAuthorities\":[{\"organizationType\":\"B\",\"role\":\"ADDITIONAL_ADMIN\",\"privilege\":\"BASIC\"}]}\"";


    @Test
    public void testAsList(){
        List<String> oktmoGuids = JacksonUtils.asList(cookieValue, "oktmoGuids");
        Assert.assertNotNull(oktmoGuids);
        Assert.assertEquals(oktmoGuids.size(), 1);
        Assert.assertEquals(oktmoGuids.get(0),"5349c064-c9b5-480b-b412-6a764240cbfd");

    }
}
