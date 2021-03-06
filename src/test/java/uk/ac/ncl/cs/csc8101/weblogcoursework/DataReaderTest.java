/*
Copyright 2014 Red Hat, Inc. and/or its affiliates.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package uk.ac.ncl.cs.csc8101.weblogcoursework;


import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.GZIPInputStream;


/**
 * Unit test for parsing of compressed web server log file
 * 
 * @author Jonathan Halliday (jonathan.halliday@redhat.com)
 * @since 2014-01
 */
public class DataReaderTest {

	private final static File dataDir = new File(
			"/home/ubuntu/data/cassandra-test-dataset/CSC8101-logfile.gz");
	// 1,352,794,346 lines, 13050324662bytes (13G),
	// md5sum=b7089321366fe6f8131196b81d060c5d
	// first line: 34600 [30/Apr/1998:21:30:17 +0000]
	// "GET /images/hm_bg.jpg HTTP/1.0" 200 24736
	// last line: 515626 [26/Jul/1998:21:59:55 +0000]
	// "GET /english/images/team_hm_header.gif HTTP/1.1" 200 763
	// private final File logFile = new File(dataDir, "all-clean.gz");


	private final static DateFormat dateFormat = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss z]",Locale.ENGLISH);

	public static void readDataFile() throws IOException, ParseException {

		final FileInputStream fileInputStream = new FileInputStream(dataDir);
		final GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
		final InputStreamReader inputStreamReader = new InputStreamReader(
				gzipInputStream);
		final BufferedReader bufferedReader = new BufferedReader(
				inputStreamReader);
		
		String line=null;
    	long num=0;
        while( (line=bufferedReader.readLine())!=null )
        {

        	if(num%1000000==0){
        		System.out.println(num);
        	}
        	
        	final String[] tokens = line.split(" ");
    		String dateString = tokens[1] + " " + tokens[2];
    		//System.out.println(dateString);
    		Date date = dateFormat.parse(dateString);	
    		
    		long millis = date.getTime();
    		DataControl.addsession(tokens[0], millis, tokens[4]);
    		num++;
            
        }
        DataControl.endsession();
        fileInputStream.close();
        inputStreamReader.close();
		bufferedReader.close();
		// assertEquals(893971817000L, millis); // 30/Apr/1998:21:30:17 +0000

	}
	
	public static void main(String arg[]) throws IOException, ParseException{
		WriterSessionData.create();
		readDataFile();
	}
}
