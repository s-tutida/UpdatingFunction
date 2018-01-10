package Tsuchida;

import java.io.InputStream;
import java.util.Properties;
import java.io.FileInputStream;

public class Configuration {
	
	private String file_path = new String();//configurationファイルのパス
	
	public Configuration(String file_path) {
		this.file_path = file_path;
	}
	
	/**
	 * properitiesファイルのkeyを指定して,valueを読み込む
	 * @param key
	 * @return　value
	 */
	public String getValue(String key) {
		
        Properties properties = new Properties();

        try {
        	
            InputStream inputStream = new FileInputStream(file_path);
            properties.load(inputStream);
            inputStream.close();

            return properties.getProperty(key);//値を返す

        } catch (Exception ex) {
        
        		System.out.println(ex.getMessage());
        }
        
        return null;
		
	}

}
