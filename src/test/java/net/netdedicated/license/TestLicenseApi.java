package net.netdedicated.license;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: artemz
 * Date: 3/26/14
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestLicenseApi {
	LicenseApi api = new LicenseApi(new BasicCredentials("admin", "admin"), "http://license.multicabinet.com", true);
/*	@Test
	public void testCreate(){
		try {
			api.create("Test", "127.0.0.5");
		} catch (Exception e){
			fail(e.getMessage());
		}
	}
	@Test
	public void testGetLicenseByIp(){
		try {
			api.getLicenseByIp("127.0.0.5");
		} catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}*/
	@Test
	public void testDelete(){
		try{
			api.delete("192.168.1.11");
		}catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}
}
