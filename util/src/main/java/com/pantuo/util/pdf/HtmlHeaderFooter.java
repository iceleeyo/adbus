package com.pantuo.util.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class HtmlHeaderFooter {
	public static final String DEST = "results/events/html_header_footer.pdf";

	public static final String EMPTY = "<table width=\"100%\" border=\"0\"><tr><td></td></tr></table>";
	public static final String HEADER = "<table width=\"100%\" border=\"0\"><tr><td>Header</td><td align=\"right\">Some 中文</td></tr></table>";
	public static final String FOOTER = "<table width=\"100%\" border=\"0\"><tr><td>Footer</td>  <td align=\"center\">  title</td>   <td align=\"right\">Some title</td></tr></table>";
	private final Phrase phrase;
	private final Font FooterFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	private final Integer Year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

	public HtmlHeaderFooter() {
		StringBuilder Builder = new StringBuilder("Corporacion Droling");
		FooterFont.setColor(BaseColor.BLUE);
		Builder.append(Year).append('.').append(" ").append("#Pagina: %d");
		phrase = new Phrase(getChunk()); //The Image is Loaded here with success.. :thumbup:        
		return;
	}

	private Chunk getChunk() {
		Chunk Chunk = null;
		try {
			final java.net.URL URL = HtmlHeaderFooter.class.getResource("c.jpg");
			Image Imagex = Image.getInstance(URL);
			Imagex.setAlignment(Image.MIDDLE);
			Imagex.setAlt("Corporacion Droling");
			Imagex.setBorder(4);
			Imagex.setBorderColor(BaseColor.BLACK);

			//  Imagex.scalePercent(100);
			Chunk = new Chunk(Imagex, 0, -35);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			return Chunk;
		}
	}

	public class HeaderFooter extends PdfPageEventHelper {
		protected ElementList header;
		protected ElementList footer;
		protected ElementList empty;

		public HeaderFooter() throws IOException {
			header = XMLWorkerHelper.parseToElementList(HEADER, null);
			footer = XMLWorkerHelper.parseToElementList(FOOTER, null);

			empty = XMLWorkerHelper.parseToElementList(EMPTY, null);
		}

		public void onEndPage(PdfWriter writer, Document document) {

			PdfContentByte cb = writer.getDirectContent();// 得到层  
			cb.saveState();
			// 开始  
			cb.beginText();
			BaseFont bf;
			try {
				bf = BaseFont.createFont("simsun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				// bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);   
				//bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
				cb.setFontAndSize(bf, 10);

				//-----------

				Rectangle rect = writer.getBoxSize("art");
				//[Header]new Phrase(Chunk) Chunk have the image.... Not working Nothing Show..
				// ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,phrase,rect.getRight(), rect.getTop(),0);

				// Header  
				float x = document.top(-30);// 位置  
				// 左  
				//   cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "H-Left", document.left(), x, 0);  
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,phrase,document.left(), x,0);

				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, phrase, document.left(), x + 30, 0);

				//				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "北京世巴传媒有限公司", (document.right() + document.left()) / 2 - 130, x, 0);
				// 中  
				//   cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "第" + writer.getPageNumber() + "页", (document.right() + document.left()) / 2, x, 0);  
				// 右    
				cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "合同编号：WBM（XS）-16-10-14-11", document.right() - 30, x, 0);

				//Footer runs Smootly..
				float y = document.bottom(-20);
				//[Header]new Phrase(Chunk) Chunk have the image.... Not working Nothing Show..
				//     ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,phrase,document.left(), y,0);

				// Footer  

				// 左  
				//   cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "F-Left", document.left(), y, 0);  
				// 中  
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "第" + writer.getPageNumber() + "页", (document.right() + document.left()) / 2, y, 0);
				// 右  
				//  cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "F-Right", document.right(), y, 0);  

				cb.endText();
				cb.restoreState();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void generalPdf(String destFile, String htmlPath) {
		File file = new File(destFile);
		file.getParentFile().mkdirs();
		try {
			new HtmlHeaderFooter().createPdf(destFile, htmlPath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new HtmlHeaderFooter().createPdf(DEST, "contract_templete.html");
	}

	public void createPdf(String filename, String htmlPath) throws IOException, DocumentException {
		// step 1
		Document document = new Document(PageSize.A4, 66, 66, 86, 72);

		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

		Rectangle rect = new Rectangle(36, 54, 20, 0);

		rect.setBorderColor(BaseColor.GRAY);
		writer.setBoxSize("art", rect);
		writer.setPageEvent(new HeaderFooter());
		// step 3
		document.open();

		BaseFont font = BaseFont.createFont("simsun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font bf = new Font(font, 12, Font.NORMAL);
		// step 4
		document.newPage();
		//			document.top(-20);
		//	document.setMargins(36, 36, 490, 72);
		//	document.add(new Paragraph("Hello你好埃里克圣诞节 World!"));
		//		String t = getActiviteTemplete();

		FileInputStream isr = new FileInputStream(htmlPath);
		//XMLWorkerHelper.getInstance().parseXHtml(writer, document,isr, Charset.forName("UTF-8"));
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, isr, null, new AsianFontProvider());
		//		for (Element e : XMLWorkerHelper.parseToElementList(t, null)) {
		//			//document.add(e);
		//
		//			//   document.add(new Paragraph(getActiviteTemplete(),bf));
		//		}
		//document.newPage();
		//	document.add(new Paragraph("Hello World!"));
		// step 5
		document.close();
	}
}