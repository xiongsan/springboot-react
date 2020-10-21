package com.fable.demo;

import org.junit.Test;

import java.net.URLDecoder;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class FileTest {

	@Test
    public void test() throws Exception{
        String deurl = URLDecoder.decode("%7B%22serviceBean%22%3A%22digitalFlowController%22%2C%22serviceName%22%3A%22digitalFlowController%22%2C%22methodName%22%3A%22saveOrUpdateWo%22%2C%22para%22%3A%5B%7B%22woId%22%3A%22${wo_id}%22%2C%22woOperAttr%22%3A%7B%22HZRS_REMARK%22%3A%221%22%2C%22SYMBOL_YEAR%22%3A%221%22%2C%22SYMBOL_NUMBER%22%3A%221%22%2C%22AUTO_INDEX_SUM%22%3A%221%22%2C%22AUTO_INDEX_PASS%22%3A%221%22%2C%22AUTO_INDEX_EXCEPTION%22%3A%221%22%2C%22ARTIFICIAL_INDEX_SUM%22%3A%221%22%2C%22ARTIFICIAL_INDEX_PASS%22%3A%22-%22%2C%22ARTIFICIAL_INDEX_EXCEPTION%22%3A%22-%22%2C%22AUDIT_RESULT%22%3A%22Y%22%7D%7D%5D%7D","UTF-8");
        System.out.println(deurl);
        }


}
