package com.kingt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Properties;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * ��Office�ĵ�ת��ΪPDF�ĵ�
 * 
 * @author Kingt.W
 * 
 */
public class Office2PDF {
	
	public static void main(String[] args){
		office2PDF("G:\\234.docx" , "G:\\1234.pdf");
	}

	/**
	 * �������������url.properties�ľ���·��
	 */
	private static final String RUL_PATH = Thread.currentThread()
			.getContextClassLoader().getResource("").getPath()
			.replace("%20", " ")
			+ "url.properties";

	/**
	 * ��Office�ĵ�ת��ΪPDF. ���иú�����Ҫ�õ�OpenOffice, OpenOffice���ص�ַΪ
	 * http://www.openoffice.org/
	 * 
	 * <pre>
	 * ����ʾ��:
	 * String sourcePath = "F:\\office\\source.doc";
	 * String destFile = "F:\\pdf\\dest.pdf";
	 * Converter.office2PDF(sourcePath, destFile);
	 * </pre>
	 * 
	 * @param sourceFile
	 *            Դ�ļ�, ����·��. ������Office2003-2007ȫ����ʽ���ĵ�, Office2010��û����. ����.doc,
	 *            .docx, .xls, .xlsx, .ppt, .pptx��. ʾ��: F:\\office\\source.doc
	 * @param destFile
	 *            Ŀ���ļ�. ����·��. ʾ��: F:\\pdf\\dest.pdf
	 * @return �����ɹ�������ʾ��Ϣ. ������� -1, ��ʾ�Ҳ���Դ�ļ�, ��url.properties���ô���; ������� 0,
	 *         ���ʾ�����ɹ�; ����1, ���ʾת��ʧ��
	 */
	public static boolean office2PDF(String sourceFile, String destFile) {
		try {
			File inputFile = new File(sourceFile);
			if (!inputFile.exists()) {
				return false;// �Ҳ���Դ�ļ�, �򷵻�-1
			}

			// ���Ŀ��·��������, ���½���·��
			File outputFile = new File(destFile);
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}
			
			if (outputFile.exists()) 
				return true;

			/*
			 * ��url.properties�ļ��ж�ȡOpenOffice�İ�װ��Ŀ¼, OpenOffice_HOME��Ӧ�ļ�ֵ.
			 * �ҵ�OpenOffice�ǰ�װ��D:\Program Files\OpenOffice.org 3�����, �����ҵ�
			 * OpenOffice���ǰ�װ�����Ŀ¼���棬��Ҫ�޸�url.properties�ļ��е� OpenOffice_HOME�ļ�ֵ.
			 * ������Ҫע����ǣ�Ҫ��"\\"����"\",��"\:"����":" . ���������鷳,
			 * ����ֱ�Ӹ�OpenOffice_HOME������ֵΪ�Լ�OpenOffice�İ�װĿ¼
			 */
			Properties prop = new Properties();
			FileInputStream fis = null;
			fis = new FileInputStream(RUL_PATH);// �����ļ�������
			prop.load(fis);// �������ļ���װ�ص�Properties������
			fis.close();// �ر���

			String OpenOffice_HOME = prop.getProperty("OpenOffice_HOME");
//			String OpenOffice_HOME = 
//					"E:\\Program Files\\OpenOffice.org 3" +
//					"E:\\Program Files\\OpenOfficePortable\\App\\openoffice";
			
			
			if (OpenOffice_HOME == null)
				return false;
			// ������ļ��ж�ȡ��URL��ַ���һ���ַ����� '\'�������'\'
			if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {
				OpenOffice_HOME += "\\";
			}
			// ����OpenOffice�ķ���
			String command = OpenOffice_HOME
					+ "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8300;urp;\" -nofirststartwizard";
			Process pro = Runtime.getRuntime().exec(command);
			// connect to an OpenOffice.org instance running on port 8100
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(
					"127.0.0.1", 8300);
			connection.connect();

			// convert
			DocumentConverter converter = new OpenOfficeDocumentConverter(
					connection);
			converter.convert(inputFile, outputFile);

			// close the connection
			connection.disconnect();
			// �ر�OpenOffice����Ľ���
			pro.destroy();

			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
}
