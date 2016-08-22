package com.pantuo.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
public class Word2Html {
	 public static void main(String argv[]) {  
		 List<Map<String, Object>> list=Lists.newArrayList();
		 List<Double> list2=Lists.newArrayList();
		 list2.add(34.5);
		 list2.add(64.5);
		 list2.add(38.5);
		 list2.add(94.54);
		 Map<String, Object> map=Maps.newLinkedHashMap();
		 Map<String, Object> map2=Maps.newLinkedHashMap();
		 map.put("name", "小李");
		 map2.put("name", "小章");
		 map.put("money", list2);
		 map2.put("money", list2);
		 list.add(map);
		 list.add(map2);
		 List<String> list3=Lists.newLinkedList();
		 list3.add("201601");
		 list3.add("201602");
		 System.out.println(JsonTools.getJsonFromObject(list3));
		 System.out.println(JsonTools.getJsonFromObject(list));
//	        try {  
//	            convert2Html("D://1.doc","D://1.html");  
//	        } catch (Exception e) {  
//	            e.printStackTrace();  
//	        }  
	    }  
	  
	    public static void writeFile(String content, String path) {  
	        FileOutputStream fos = null;  
	        BufferedWriter bw = null;  
	        try {  
	            File file = new File(path);  
	            fos = new FileOutputStream(file);  
	            bw = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));  
	            bw.write(content);  
	        } catch (FileNotFoundException fnfe) {  
	            fnfe.printStackTrace();  
	        } catch (IOException ioe) {  
	            ioe.printStackTrace();  
	        } finally {  
	            try {  
	                if (bw != null)  
	                    bw.close();  
	                if (fos != null)  
	                    fos.close();  
	            } catch (IOException ie) {  
	            }  
	        }  
	    }  
	  
	    public static void convert2Html(String fileName, String outPutFile)  
	            throws TransformerException, IOException,  
	            ParserConfigurationException {  
	        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));  
	        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(  
	                DocumentBuilderFactory.newInstance().newDocumentBuilder()  
	                        .newDocument());  
	         wordToHtmlConverter.setPicturesManager( new PicturesManager()  
	         {  
	             public String savePicture( byte[] content,  
	                     PictureType pictureType, String suggestedName,  
	                     float widthInches, float heightInches )  
	             {  
	                 return "test/"+suggestedName;  
	             }  
	         } );  
	        wordToHtmlConverter.processDocument(wordDocument);  
	        //save pictures  
	        List pics=wordDocument.getPicturesTable().getAllPictures();  
	        Document htmlDocument = wordToHtmlConverter.getDocument();  
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        DOMSource domSource = new DOMSource(htmlDocument);  
	        StreamResult streamResult = new StreamResult(out);  
	  
	        TransformerFactory tf = TransformerFactory.newInstance();  
	        Transformer serializer = tf.newTransformer();  
	        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");  
	        serializer.setOutputProperty(OutputKeys.INDENT, "yes");  
	        serializer.setOutputProperty(OutputKeys.METHOD, "html");  
	        serializer.transform(domSource, streamResult);  
	        out.close();  
	        writeFile(new String(out.toByteArray()), outPutFile);  
	    }  
}
