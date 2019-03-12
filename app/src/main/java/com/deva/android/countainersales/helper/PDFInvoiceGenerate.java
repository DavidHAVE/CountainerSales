package com.deva.android.countainersales.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.deva.android.countainersales.object.ProductModel;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by David on 14/10/2017.
 */

public class PDFInvoiceGenerate {

    public static String name, phoneNumber, address;
    public static String orderNumber, itemOrder, itemQuantity, itemPrice = "";
    public static String isDesign, designQuantity, designPrice, totalPrice = "";
    public static long payment30, payment40 = 0;
    private static String currentDate = "";

    public static String itemOrder2, itemQuantity2, itemPrice2 = "";

    //we will add some products to arrayListRProductModel to show in the PDF document
    private static ArrayList<ProductModel> arrayListRProductModel = new ArrayList<ProductModel>();
    //creating a PdfWriter variable. PdfWriter class is available at com.itextpdf.text.pdf.PdfWriter
    private PdfWriter pdfWriter;

    /**
     * iText allows to add metadata to the PDF which can be viewed in your Adobe Reader. If you right click
     * on the file and to to properties then you can see all these information.
     *
     * @param document
     */
    private static void addMetaData(Document document) {
        document.addTitle("Order Invoice");
        document.addSubject("none");
        document.addKeywords("Countainer, Design");
        document.addAuthor("PT. Global Perkasa Mekanindo");
//        document.addCreator(StaticValue.USER_MODEL.getName());
    }

    /**
     * In this method title(s) of the document is added.
     *
     * @param document
     * @throws DocumentException
     */
    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();
        Paragraph paragraph1 = new Paragraph();
        Paragraph paragraph2 = new Paragraph();

        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
        Paragraph childParagraph = new Paragraph("PT. Global Perkasa Mekanindo", StaticValue.FONT_TITLE); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.add(childParagraph);

        Paragraph childParagraph2 = new Paragraph("DATE: "+currentDate+"\n" + "INVOICE: "+orderNumber+"\n" + "FOR: Payment"); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph2.setAlignment(Element.ALIGN_RIGHT);
        paragraph.add(childParagraph2);

        Paragraph childParagraph3 = new Paragraph("Jl. Candi Raya, Purwoyoso, Ngaliyan\n" +
                "Semarang, Jawa Tengah, Indonesia\n" +
                "Phone 0811-2599-889"); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph3.setAlignment(Element.ALIGN_LEFT);
        paragraph.add(childParagraph3);

        addEmptyLine(childParagraph3, 2);

        Paragraph childParagraph4 = new Paragraph("Bill To:\n" + "Name: "+name+"\n" + "Company Name:\n" + "Street Address: "
                +address+"\n"+ "City, ST ZIP Code:\n" + "Phone: "+phoneNumber+"\n"); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph4.setAlignment(Element.ALIGN_LEFT);
        paragraph.add(childParagraph4);

//        childParagraph = new Paragraph("Product List", StaticValue.FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
//        childParagraph.setAlignment(Element.ALIGN_CENTER);
//        paragraph.add(childParagraph);
//
//        childParagraph = new Paragraph("Report generated on: 17.12.2015" , StaticValue.FONT_SUBTITLE);
//        childParagraph.setAlignment(Element.ALIGN_CENTER);
//        paragraph.add(childParagraph);

        addEmptyLine(paragraph, 2);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        //End of adding several titles

    }

    /**
     * In this method the main contents of the documents are added
     *
     * @param document
     * @throws DocumentException
     */

    private static void addContent(Document document) throws DocumentException {

        Paragraph reportBody = new Paragraph();
        reportBody.setFont(StaticValue.FONT_BODY); //public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);

        // Creating a table
        createTable(reportBody);

        // now add all this to the document
        document.add(reportBody);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 2);
        document.add(paragraph);


        Font regular = new Font(Font.FontFamily.HELVETICA, 12);
        Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
//        Paragraph p = new Paragraph("NAME: ", bold);
//        p.add(new Chunk(CC_CUST_NAME, regular));

        Paragraph paragraph2 = new Paragraph("Make all checks payable to Your Company Name\n" +
                "If you have any questions concerning this invoice, contact Name, Phone Number, E-mail");
        document.add(paragraph2);
//        document.add(new Paragraph("Make all checks payable to Your Company Name\n" +
//                "If you have any questions concerning this invoice, contact Name, Phone Number, E-mail"));

        Paragraph p = new Paragraph();
        addEmptyLine(p, 1);
        document.add(p);

        Paragraph paragraph3 = new Paragraph("THANK YOU FOR YOUR BUSINESS!", bold);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph3);
    }

    public static PdfPTable createFirstTable() {
        // a table with three columns
        PdfPTable table = new PdfPTable(3);
        // the cell object
        PdfPCell cell;
        // we add a cell with colspan 3
        cell = new PdfPCell(new Phrase("Cell with colspan 3"));
        cell.setColspan(3);
        table.addCell(cell);
        // now we add a cell with rowspan 2
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        // we add the four remaining cells with addCell()
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }

    /**
     * This method is responsible to add table using iText
     *
     * @param reportBody
     * @throws BadElementException
     */
    private static void createTable(Paragraph reportBody)
            throws BadElementException {

        float[] columnWidths = {3, 2, 1}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100); //set table with 100% (full page)
        table.getDefaultCell().setUseAscender(true);


        //Adding table headers
        PdfPCell cell = new PdfPCell(new Phrase("Product Name", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quantity",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);


        generateTableData();
        for (int i = 0; i < arrayListRProductModel.size(); i++) { //
            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getName()));
            cell.setFixedHeight(40);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getUnitName()));
            cell.setFixedHeight(40);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getBrandName()));
            cell.setFixedHeight(40);
            table.addCell(cell);
        }

//        cell = new PdfPCell(new Phrase("Total"));
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setBackgroundColor(new GrayColor(0.75f));
//        cell.setFixedHeight(30);
//        cell.setColspan(1);
//        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("IDR "+totalPrice));
//        cell.setRowspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("30% Payment"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("IDR "+payment30));
//        cell.setRowspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("40% Payment"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("IDR "+payment40));
//        cell.setRowspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("30% Payment"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("IDR "+payment30));
//        cell.setRowspan(1);
        table.addCell(cell);


//        Paragraph paragraph1 = new Paragraph();
////        Paragraph paragraph2 = new Paragraph();
//
//        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
//        Paragraph childParagraph = new Paragraph("PT. Global Perkasa Menanindo", StaticValue.FONT_TITLE); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
//        childParagraph.setAlignment(Element.ALIGN_LEFT);
//        paragraph1.add(childParagraph);
//
//        Paragraph childParagraph2 = new Paragraph("DATE: October 7, 2017\n" + "INVOICE # 100\n" + "FOR:"); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
//        childParagraph2.setAlignment(Element.ALIGN_LEFT);
//        paragraph1.add(childParagraph2);


        // we add the four remaining cells with addCell()
//        table.addCell("row 1; cell 1");
//        table.addCell("row 1; cell 2");
//        table.addCell("row 2; cell 1");
//        table.addCell("row 2; cell 2");

        //End of adding table headers

        //This method will generate some static data for the table
//        generateTableData();

        //Adding data into table
//        for (int i = 0; i < arrayListRProductModel.size(); i++) { //
//            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getName()));
//            cell.setFixedHeight(28);
//            table.addCell(cell);
//
//            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getBrandName()));
//            cell.setFixedHeight(28);
//            table.addCell(cell);
//
//            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getCategoryName()));
//            cell.setFixedHeight(28);
//            table.addCell(cell);
//
//            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getUnitName()));
//            cell.setFixedHeight(28);
//            table.addCell(cell);
//        }

        reportBody.add(table);

    }

    /**
     * This method is used to add empty lines in the document
     *
     * @param paragraph
     * @param number
     */
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * Generate static data for table
     */

    private static void generateTableData() {
        ProductModel productModel = null;
        productModel = new ProductModel(itemOrder2, itemQuantity2, itemPrice2);
        arrayListRProductModel.add(productModel);

//        productModel = new ProductModel("Countainer 40 Feet", "1", "Rp 3.000.000");
//        arrayListRProductModel.add(productModel);
//
//        productModel = new ProductModel("Design", "3", "Rp 750.0000");
//        arrayListRProductModel.add(productModel);

//        productModel = new ProductModel("LG Mini", "Piece", "LG", "Smartphone");
//        arrayListRProductModel.add(productModel);
//
//        productModel = new ProductModel("iPhone 6", "Piece", "Apple", "Smartphone");
//        arrayListRProductModel.add(productModel);
    }

    public boolean createPDF(Context context, String reportName, String name, String phoneNumber, String address, String orderNumber,
                             String itemOrder, String itemQuantity, String itemPrice, String isDesign, String designQuantity,
                             String designPrice, String totalPrice) {

        arrayListRProductModel.clear();

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderNumber = orderNumber;
        this.itemOrder = itemOrder;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.isDesign = isDesign;
        this.designQuantity = designQuantity;
        this.designPrice = designPrice;
        this.totalPrice = totalPrice;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(date);

//        this.itemOrder = itemOrder +"\n"+isDesign;

        itemOrder2 = this.itemOrder + "\n" + isDesign;
        itemQuantity2 = this.itemQuantity + "\n" + designQuantity;
        itemPrice2 = this.itemPrice + "\n" + designPrice;;

        Log.e("PDF", itemOrder2+", "+itemQuantity2+", "+itemPrice2);

        payment30 = Long.parseLong(totalPrice) * 30/100;
        payment40 = Long.parseLong(totalPrice) * 40/100;

        try {
            //creating a directory in SD card
            File mydir = new File(Environment.getExternalStorageDirectory()
                    + StaticValue.PATH_ORDER_INVOICE); //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            //getting the full path of the PDF report name
            String mPath = Environment.getExternalStorageDirectory().toString()
                    + StaticValue.PATH_ORDER_INVOICE //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
                    + orderNumber + ".pdf"; //reportName could be any name

            //constructing the PDF file
            File pdfFile = new File(mPath);

            //Creating a Document with size A4. Document class is available at  com.itextpdf.text.Document
            Document document = new Document(PageSize.A4);

            //assigning a PdfWriter instance to pdfWriter
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            //PageFooter is an inner class of this class which is responsible to create Header and Footer
            PageHeaderFooter event = new PageHeaderFooter();
            pdfWriter.setPageEvent(event);

            //Before writing anything to a document it should be opened first
            document.open();

            //Adding meta-data to the document
            addMetaData(document);
            //Adding Title(s) of the document
            addTitlePage(document);
            //Adding main contents of the document
            addContent(document);
            //Closing the document
            document.close();
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    /**
     * This is an inner class which is used to create header and footer
     *
     * @author XYZ
     */

    class PageHeaderFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);

        public void onEndPage(PdfWriter writer, Document document) {

            /**
             * PdfContentByte is an object containing the user positioned text and graphic contents
             * of a page. It knows how to apply the proper font encoding.
             */
            PdfContentByte cb = writer.getDirectContent();

            /**
             * In iText a Phrase is a series of Chunks.
             * A chunk is the smallest significant part of text that can be added to a document.
             *  Most elements can be divided in one or more Chunks. A chunk is a String with a certain Font
             */
            Phrase footer_poweredBy = new Phrase("PT. Global Perkasa Mekanindo", StaticValue.FONT_HEADER_FOOTER); //public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
            Phrase footer_pageNumber = new Phrase("Page " + document.getPageNumber(), StaticValue.FONT_HEADER_FOOTER);

            // Header
            // ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header,
            // (document.getPageSize().getWidth()-10),
            // document.top() + 10, 0);

            // footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footer_pageNumber,
                    (document.getPageSize().getWidth() - 10),
                    document.bottom() - 10, 0);
//			// footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer_poweredBy, (document.right() - document.left()) / 2
                            + document.leftMargin(), document.bottom() - 10, 0);
        }
    }
}
