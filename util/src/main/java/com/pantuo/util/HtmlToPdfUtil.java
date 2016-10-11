package com.pantuo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * 将html转换为pdf
 * @author yfx
 * 2016年5月18日 下午5:52:13
 */
public class HtmlToPdfUtil {
	
	public static void buildPdf(List<String> contexts,String destFile) throws DocumentException, IOException{
		byte[] result=buildPdf(contexts);
		FileUtils.writeByteArrayToFile(new File(destFile), result);
	}
	
	/**
	 * 生成多页pdf
	 * @param contexts
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static byte[] buildPdf(List<String> contexts) throws DocumentException, IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream(1024);
		Document document = new Document();
	    PdfCopy copy = new PdfCopy(document, baos);
	    document.open();
	    PdfReader reader;
	    for (String ctx : contexts) {
	        reader = new PdfReader(buildPdf(ctx));
	        copy.addDocument(reader);
	        reader.close();
	    }
	    document.close();
	    byte[] result=baos.toByteArray();
		baos.flush();
		baos.close();
		return result;
	}
	
	/**
	 * 生成单页pdf
	 * @param ctx
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static byte[] buildPdf(String ctx) throws DocumentException, IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream(1024);
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
	    writer.setInitialLeading(12);//文字间距
	    document.open();
	    HtmlToPdfUtil.MyFontsProvider fontProvider = new HtmlToPdfUtil.MyFontsProvider();
        fontProvider.addFontSubstitute("lowagie", "garamond");
        fontProvider.setUseUnicode(true);
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,new HtmlPipeline(htmlContext, new PdfWriterPipeline(document,writer)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);
        ByteArrayInputStream bais=new ByteArrayInputStream(ctx.getBytes());
    	p.parse(new InputStreamReader(bais));
    	p.flush();

        document.close();
		byte[] result=baos.toByteArray();
		baos.flush();
		baos.close();
		return result;
	}
    
    public static class MyFontsProvider extends XMLWorkerFontProvider{
        public MyFontsProvider(){
            super(null,null);
        }
        @Override
        public Font getFont(final String fontname, String encoding, float size, final int style) {
            String fntname = fontname;
            if(fntname==null){
                fntname="宋体";
            }
            return super.getFont(fntname, encoding, size, style);
        }
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
    	String DEST = "./test2.pdf";
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        ArrayList<String> str=new ArrayList<String>();
        str.add("<div>中文hello</div><br><img style='width:500px;' src='http://b.hiphotos.baidu.com/album/s%3D1600%3Bq%3D90/sign=4f04be8ab8014a90853e42bb99470263/b8389b504fc2d562d426d1d5e61190ef76c66cdf.jpg?v=tbs'>");
        str.add("<div>中文hello111</div><br>");
        HtmlToPdfUtil.buildPdf(str, DEST);
    }
}