package com.pantuo.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
 
 
public class HtmlHeaderFooter {
    public static final String DEST = "results/events/html_header_footer.pdf";
    public static final String HEADER = "<table width=\"100%\" border=\"0\"><tr><td>Header</td><td align=\"right\">Some title</td></tr></table>";
    public static final String FOOTER = "<table width=\"100%\" border=\"0\"><tr><td>Footer</td>  <td align=\"center\">  title</td>   <td align=\"right\">Some title</td></tr></table>";
 
    public class HeaderFooter extends PdfPageEventHelper {
        protected ElementList header;
        protected ElementList footer;
        public HeaderFooter() throws IOException {
            header = XMLWorkerHelper.parseToElementList(HEADER, null);
            footer = XMLWorkerHelper.parseToElementList(FOOTER, null);
        }
        public void onEndPage(PdfWriter writer, Document document) {  
        	  
            PdfContentByte cb = writer.getDirectContent();// 得到层  
            cb.saveState();  
            // 开始  
            cb.beginText();  
            BaseFont bf;
			try {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
				 cb.setFontAndSize(bf, 10);  
		            // Header  
		            float x = document.top(-20);// 位置  
		            // 左  
		            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "H-Left", document.left(), x, 0);  
		            // 中  
		            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "第" + writer.getPageNumber() + "页", (document.right() + document.left()) / 2, x, 0);  
		            // 右    
		            cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "H-Right", document.right(), x, 0);  
		            // Footer  
		            float y = document.bottom(-20);  
		            // 左  
		            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "F-Left", document.left(), y, 0);  
		            // 中  
		            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "第" + writer.getPageNumber() + "页", (document.right() + document.left()) / 2, y, 0);  
		            // 右  
		            cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "F-Right", document.right(), y, 0);  
		            cb.endText();  
		            cb.restoreState();  
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
           
      
        } 
        /*
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                ColumnText ct = new ColumnText(writer.getDirectContent());
                ct.setSimpleColumn(new Rectangle(36, 832, 559, 810));
                for (Element e : header) {
                    ct.addElement(e);
                }
                ct.go();
                ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
                for (Element e : footer) {
                    ct.addElement(e);
                }
                ct.go();
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }*/
    }
 
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HtmlHeaderFooter().createPdf(DEST);
    }
 
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4, 36, 36, 36, 72);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setPageEvent(new HeaderFooter());
        // step 3
        document.open();
        // step 4
        for (int i = 0; i < 50; i++)
            document.add(new Paragraph("Hello World!"));
        document.newPage();
        document.add(new Paragraph("Hello World!"));
        document.newPage();
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
    }
}