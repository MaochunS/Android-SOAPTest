package com.maochun.android_soaptest;

import android.util.Log;
import android.util.Pair;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private SoapRequestHandler mSoapHdr;

    @Before
    public void setUp(){
        mSoapHdr = new SoapRequestHandler();
    }

    @Test
    public void soapReqTest() {

        String host = "www.webxml.com.cn";
        String soapAction = "http://WebXml.com.cn/getSupportCity";
        String param = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                "<soap:Body><getSupportCity xmlns=\"http://WebXml.com.cn/\"><byProvinceName>广东</byProvinceName></getSupportCity>" +
                "</soap:Body></soap:Envelope>";

        Pair<Boolean, String> reqRet = mSoapHdr.processPostRequest("http://www.webxml.com.cn/WebServices/WeatherWebService.asmx", host, soapAction, param);

        System.out.println(reqRet.second);


    }
}