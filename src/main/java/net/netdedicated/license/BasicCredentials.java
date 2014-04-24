package net.netdedicated.license;


import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: artemz
 * Date: 3/24/14
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicCredentials {
	String username, password;

	public BasicCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	String toBase64() throws IOException {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encodeBuffer((username + ":" + password).getBytes()).trim();
	}

}
