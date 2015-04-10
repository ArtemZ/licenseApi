package net.netdedicated.license;

import net.netdedicated.beans.ErrorBean;
import net.netdedicated.domain.License;

import java.beans.XMLDecoder;
import java.io.*;
import java.net.*;
import java.util.Properties;

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
    private Boolean debug = false;
    public LicenseApi(BasicCredentials credentials, String licenseServerUrl, Boolean debug){
        this.credentials = credentials;
		this.licenseServerUrl = licenseServerUrl;
        this.debug = debug;
    }
	public LicenseApi(BasicCredentials credentials, String licenseServerUrl) {
		this.credentials = credentials;
		this.licenseServerUrl = licenseServerUrl;
	}
	public License create(String type, String ip) throws IOException, LicenseApiException {
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

        License createdLicense = null;
        XMLDecoder decoder;
        if (debug){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null){
                baos.write(line.getBytes());
            }
            writer.close();
            reader.close();
            System.out.println("Actual data: '" + baos.toString() + "' ");
            decoder = new XMLDecoder(new ByteArrayInputStream(baos.toByteArray()));
        } else {
            decoder = new XMLDecoder(urlConnection.getInputStream());
        }
        Object receivedObject = decoder.readObject();
        if (receivedObject instanceof License){
            createdLicense = (License) receivedObject;
        } else if (receivedObject instanceof ErrorBean){
            ErrorBean errorBean = (ErrorBean) receivedObject;
            throw new LicenseApiException("API Error: " + errorBean.getErrorMessage());
        } else {
            throw new LicenseApiException("API Error: received object has unknown class " + receivedObject.getClass());
        }

		return createdLicense;
	}
	public void delete(String ip) throws IOException, LicenseApiException{
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
	public void update(String oldIp, String newIp, String newType) throws IOException, LicenseApiException{
		delete(oldIp);
		create(newType, newIp);
	}
	public License getLicenseByIp(String ip) throws IOException, LicenseApiException{
		URL url = new URL(licenseServerUrl + "/license/apilicense?ipaddr=" + URLEncoder.encode(ip, "UTF-8") );
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(false);
		urlConnection.setRequestProperty("Authorization", "Basic " + credentials.toBase64());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null){
            baos.write(line.getBytes());
        }
        reader.close();

        if (debug){

            System.out.println("Actual data: '" + baos.toString() + "' ");
        }
        if (baos.toString().contains("ERROR=")){
            /*Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(baos.toByteArray()));*/

            throw new LicenseApiException("Unable to get license from the server, server returns error: " + baos.toString());
        } else {
            XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(baos.toByteArray()));
            License createdLicense = (License) decoder.readObject();
            return createdLicense;
        }

	}
}
