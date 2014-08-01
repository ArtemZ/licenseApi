package net.netdedicated.license;

/**
 * Created with IntelliJ IDEA.
 * User: artemz
 * Date: 8/1/14
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseApiException extends Exception {
    public LicenseApiException(){
        super();
    }
    public LicenseApiException(String msg){
        super(msg);
    }
}
