package net.netdedicated.license;

import net.netdedicated.domain.License;

import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: artemz
 * Date: 3/24/14
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseApi {
	private BasicCredentials credentials;
	private String licenseServerUrl;

	public LicenseApi(BasicCredentials credentials, String licenseServerUrl) {
		this.credentials = credentials;
		this.licenseServerUrl = licenseServerUrl;
	}
	public License create(String type, String ip) throws IOException {
		License license = new License();
		license.setType(type);
		license.setIp(InetAddress.getByName(ip).getAddress());
		URL url = new URL(licenseServerUrl + "/license/apicreate");
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Authorization", "Basic " + credentials.toBase64());

		String data = "license=" + URLEncoder.encode(license.toXML(), "UTF-8");

		OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
		writer.write(data);
		writer.flush();


		/*BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		StringBuffer licenseBuffer = new StringBuffer();
		while ((line = reader.readLine()) != null){
			licenseBuffer.append(line);
		}
		writer.close();
		reader.close();*/
		XMLDecoder decoder = new XMLDecoder(urlConnection.getInputStream());
		License createdLicense = (License) decoder.readObject();
		return createdLicense;
	}
	public void delete(String ip) throws IOException{
		License licenseToDelete = getLicenseByIp(ip);
		URL url = new URL(licenseServerUrl + "/license/apidelete");
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Authorization", "Basic " + credentials.toBase64());

		String data = "license.id=" + URLEncoder.encode(licenseToDelete.getId().toString(), "UTF-8");

		OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
		writer.write(data);
		writer.flush();


		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		StringBuffer licenseBuffer = new StringBuffer();
		while ((line = reader.readLine()) != null){
			licenseBuffer.append(line);
		}
		writer.close();
		reader.close();

	}
	public void update(String oldIp, String newIp, String newType) throws IOException{
		delete(oldIp);
		create(newType, newIp);
	}
	public License getLicenseByIp(String ip) throws IOException{
		URL url = new URL(licenseServerUrl + "/license/apilicense?ipaddr=" + URLEncoder.encode(ip, "UTF-8") );
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(false);
		urlConnection.setRequestProperty("Authorization", "Basic " + credentials.toBase64());

		/*OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
		writer.write(data);
		writer.flush();*/


		/*BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		StringBuffer licenseBuffer = new StringBuffer();
		while ((line = reader.readLine()) != null){
			licenseBuffer.append(line);
		}
*//*		System.out.println("output: '" + licenseBuffer.toString() + "'");*//*
		reader.close();*/
		XMLDecoder decoder = new XMLDecoder(urlConnection.getInputStream());
		License createdLicense = (License) decoder.readObject();
		return createdLicense;
	}
}
