//package com.deva.android.countainersales.helper;
//
//import android.content.Context;
//import android.os.Environment;
//
//
//import com.deva.android.countainersales.object.ProductModel;
//import com.itextpdf.text.BadElementException;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.ColumnText;
//import com.itextpdf.text.pdf.GrayColor;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfPageEventHelper;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//
//import static android.R.attr.x;
//
//public class ProductListAllPDF {
//    //creating a PdfWriter variable. PdfWriter class is available at com.itextpdf.text.pdf.PdfWriter
//    private PdfWriter pdfWriter;
//
//    //we will add some products to arrayListRProductModel to show in the PDF document
//    private static ArrayList<ProductModel> arrayListRProductModel = new ArrayList<ProductModel>();
//
//    public boolean createPDF(Context context, String reportName) {
//
//        try {
//            //creating a directory in SD card
//            File mydir = new File(Environment.getExternalStorageDirectory()
//                    + StaticValue.PATH_PRODUCT_REPORT); //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
//            if (!mydir.exists()) {
//                mydir.mkdirs();
//            }
//
//            //getting the full path of the PDF report name
//            String mPath = Environment.getExternalStorageDirectory().toString()
//                    + StaticValue.PATH_PRODUCT_REPORT //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
//                    + reportName+".pdf"; //reportName could be any name
//
//            //constructing the PDF file
//            File pdfFile = new File(mPath);
//
//            //Creating a Document with size A4. Document class is available at  com.itextpdf.text.Document
//            Document document = new Document(PageSize.A4);
//
//            //assigning a PdfWriter instance to pdfWriter
//            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
//
//            //PageFooter is an inner class of this class which is responsible to create Header and Footer
//            PageHeaderFooter event = new PageHeaderFooter();
//            pdfWriter.setPageEvent(event);
//
//            //Before writing anything to a document it should be opened first
//            document.open();
//
//            //Adding meta-data to the document
////            addMetaData(document);
////            //Adding Title(s) of the document
////            addTitlePage(document);
////            //Adding main contents of the document
////
////            addContent(document);
////            addContent2(document);
////            float[] columnWidths = {183, 31, 88, 49, 35, 25, 35, 35, 35, 32, 32, 33, 35, 60, 46, 26 };
////            PdfPTable table = new PdfPTable(columnWidths);
////            table.setTotalWidth(770F);
////            table.setLockedWidth(true);
////            buildNestedTables(table);
////            document.add(new Paragraph("Add table straight to another table"));
////            document.add(table);
////            table = new PdfPTable(columnWidths);
////            table.setTotalWidth(770F);
////            table.setLockedWidth(true);
////            buildNestedTables2(table);
////            document.add(new Paragraph("Add table to the cell constructor"));
////            document.add(table);
////            table = new PdfPTable(columnWidths);
////            table.setTotalWidth(770F);
////            table.setLockedWidth(true);
////            buildNestedTables3(table);
////            document.add(new Paragraph("Add table as an element to a cell"));
////            document.add(table);
//
////            Paragraph paragraph = new Paragraph();
////            PdfPCell cell = null;
////            // Main table
////            PdfPTable mainTable = new PdfPTable(2);
////            mainTable.setWidthPercentage(100.0f);
////            // First table
////            PdfPCell firstTableCell = new PdfPCell();
////            firstTableCell.setBorder(PdfPCell.NO_BORDER);
////            PdfPTable firstTable = new PdfPTable(2);
////            firstTable.setWidthPercentage(50.0f);
////            cell = new PdfPCell(new Phrase("T1R1C1"));
////            firstTable.addCell(cell);
////            cell = new PdfPCell(new Phrase("T1R1C2"));
////            firstTable.addCell(cell);
////            cell = new PdfPCell(new Phrase("T1R2C1"));
////            firstTable.addCell(cell);
////            cell = new PdfPCell(new Phrase("T1R2C2"));
////            firstTable.addCell(cell);
////            firstTableCell.addElement(firstTable);
////            mainTable.addCell(firstTableCell);
////            // Second table
////            PdfPCell secondTableCell = new PdfPCell();
////            secondTableCell.setBorder(PdfPCell.NO_BORDER);
////            PdfPTable secondTable = new PdfPTable(2);
////            secondTable.setWidthPercentage(50.0f);
////            cell = new PdfPCell(new Phrase("T2R1C1"));
////            secondTable.addCell(cell);
////            cell = new PdfPCell(new Phrase("T2R1C2"));
////            secondTable.addCell(cell);
////            cell = new PdfPCell(new Phrase("T2R2C1"));
////            secondTable.addCell(cell);
////            cell = new PdfPCell(new Phrase("T2R2C2"));
////            secondTable.addCell(cell);
////            secondTableCell.addElement(secondTable);
////            mainTable.addCell(secondTableCell);
////            paragraph.add(mainTable);
////            document.add(paragraph);
//
//            float[] columnWidths = {183, 31, 88, 49, 35, 25, 35, 35, 35, 32, 32, 33, 35, 60, 46, 26 };
//            PdfPTable table = new PdfPTable(columnWidths);
//            table.setTotalWidth(770F);
//            table.setLockedWidth(true);
//            buildNestedTables(table);
//            document.add(new Paragraph("Add table straight to another table"));
//            document.add(table);
//            table = new PdfPTable(columnWidths);
//            table.setTotalWidth(770F);
//            table.setLockedWidth(true);
//            buildNestedTables2(table);
//            document.add(new Paragraph("Add table to the cell constructor"));
//            document.add(table);
//            table = new PdfPTable(columnWidths);
//            table.setTotalWidth(770F);
//            table.setLockedWidth(true);
//            buildNestedTables3(table);
//            document.add(new Paragraph("Add table as an element to a cell"));
//            document.add(table);
//
//
//            //Closing the document
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return false;
//        }
//        return true;
//    }
//
//    private void buildNestedTables(PdfPTable outerTable) {
//        PdfPTable innerTable1 = new PdfPTable(1);
//        PdfPTable innerTable2 = new PdfPTable(2);
//        PdfPCell cell;
//        innerTable1.addCell("Cell 1");
//        innerTable1.addCell("Cell 2");
//        outerTable.addCell(innerTable1);
//        innerTable2.addCell("Cell 3");
//        innerTable2.addCell("Cell 4");
//        outerTable.addCell(innerTable2);
//        cell = new PdfPCell();
//        cell.setColspan(14);
//        outerTable.addCell(cell);
//    }
//
//    private void buildNestedTables2(PdfPTable outerTable) {
//        PdfPTable innerTable1 = new PdfPTable(1);
//        innerTable1.setWidthPercentage(100);
//        PdfPTable innerTable2 = new PdfPTable(2);
//        innerTable2.setWidthPercentage(100);
//        PdfPCell cell;
//        innerTable1.addCell("Cell 1");
//        innerTable1.addCell("Cell 2");
//        cell = new PdfPCell(innerTable1);
//        outerTable.addCell(cell);
//        innerTable2.addCell("Cell 3");
//        innerTable2.addCell("Cell 4");
//        cell = new PdfPCell(innerTable2);
//        outerTable.addCell(cell);
//        cell = new PdfPCell();
//        cell.setColspan(14);
//        outerTable.addCell(cell);
//    }
//
//    private void buildNestedTables3(PdfPTable outerTable) {
//        PdfPTable innerTable1 = new PdfPTable(1);
//        innerTable1.setWidthPercentage(100);
//        PdfPTable innerTable2 = new PdfPTable(2);
//        innerTable2.setWidthPercentage(100);
//        PdfPCell cell;
//        innerTable1.addCell("Cell 1");
//        innerTable1.addCell("Cell 2");
//        cell = new PdfPCell();
//        cell.addElement(innerTable1);
//        outerTable.addCell(cell);
//        innerTable2.addCell("Cell 3");
//        innerTable2.addCell("Cell 4");
//        cell = new PdfPCell();
//        cell.addElement(innerTable2);
//        outerTable.addCell(cell);
//        cell = new PdfPCell();
//        cell.setColspan(14);
//        outerTable.addCell(cell);
//    }
//
//    /**
//     *  iText allows to add metadata to the PDF which can be viewed in your Adobe Reader. If you right click
//     *  on the file and to to properties then you can see all these information.
//     * @param document
//     */
//    private static void addMetaData(Document document) {
//        document.addTitle("All Product Names");
//        document.addSubject("none");
//        document.addKeywords("Java, PDF, iText");
//        document.addAuthor("SIAS ERP");
//        document.addCreator("Creator");
//    }
//
//    /**
//     * In this method title(s) of the document is added.
//     * @param document
//     * @throws DocumentException
//     */
//    private static void addTitlePage(Document document)
//            throws DocumentException {
//        Paragraph paragraph = new Paragraph();
//
//        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
//        Paragraph childParagraph = new Paragraph("PLUS Electronics Pvt. Ltd.", StaticValue.FONT_TITLE); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
//        childParagraph.setAlignment(Element.ALIGN_CENTER);
//        paragraph.add(childParagraph);
//
//        childParagraph = new Paragraph("Product List", StaticValue.FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
//        childParagraph.setAlignment(Element.ALIGN_CENTER);
//        paragraph.add(childParagraph);
//
//        childParagraph = new Paragraph("Report generated on: 17.12.2015" , StaticValue.FONT_SUBTITLE);
//        childParagraph.setAlignment(Element.ALIGN_CENTER);
//        paragraph.add(childParagraph);
//
//        addEmptyLine(paragraph, 2);
//        paragraph.setAlignment(Element.ALIGN_CENTER);
//        document.add(paragraph);
//        //End of adding several titles
//
//    }
//
//    /**
//     * In this method the main contents of the documents are added
//     * @param document
//     * @throws DocumentException
//     */
//
//    private static void addContent(Document document) throws DocumentException {
//
//        Paragraph reportBody = new Paragraph();
//        reportBody.setFont(StaticValue.FONT_BODY); //public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
//
//        // Creating a table
//        createTable1(reportBody);
//
////        createTable1(reportBody);
////        createTable2();
////        createTable3();
//
//        // now add all this to the document
//        document.add(reportBody);
//
//    }
//
//    private static void addContent2(Document document) throws DocumentException {
//
//        Paragraph reportBody2 = new Paragraph();
//        reportBody2.setFont(StaticValue.FONT_BODY); //public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
//
//        // Creating a table
//
//        createTable(reportBody2);
////        createTable1(reportBody);
////        createTable2();
////        createTable3();
//
//        // now add all this to the document
//        document.add(reportBody2);
//
//    }
//
//    /**
//     * Creates a table; widths are set with setWidths().
//     * @return a PdfPTable
//     * @throws DocumentException
//     */
//
//    private static void createTable1(Paragraph reportBody) throws BadElementException {
////        PdfPTable table = new PdfPTable(3);
//////        table.setWidthPercentage(288 / 5.23f);
//////        table.setWidths(new int[]{2, 1, 1});
////        PdfPCell cell;
////        cell = new PdfPCell(new Phrase("Table 1"));
////        cell.setColspan(3);
////        table.addCell(cell);
////        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
////        cell.setRowspan(2);
////        table.addCell(cell);
////        table.addCell("row 1; cell 1");
////        table.addCell("row 1; cell 2");
////        table.addCell("row 2; cell 1");
////        table.addCell("row 2; cell 2");
//
//        PdfPTable table3 = new PdfPTable(3);
//        table3.setSpacingAfter(10);
//        // the cell object
//        PdfPCell cell3;
//        // we add a cell with colspan 3
//        cell3 = new PdfPCell(new Phrase("Cell with colspan 3"));
//        cell3.setColspan(3);
//        table3.addCell(cell3);
//        // now we add a cell with rowspan 2
//        cell3 = new PdfPCell(new Phrase("Cell with rowspan 2"));
//        cell3.setRowspan(2);
//        table3.addCell(cell3);
//        // we add the four remaining cells with addCell()
//        table3.addCell("row 1; cell 1");
//        table3.addCell("row 1; cell 2");
//        table3.addCell("row 2; cell 1");
//        table3.addCell("row 2; cell 2");
////        return table;
//        reportBody.add(table3);
//    }
//
//
//    /**
//     * Creates a table; widths are set with setWidths().
//     * @return a PdfPTable
//     * @throws DocumentException
//     */
//    public static PdfPTable createTable2() throws DocumentException {
//        PdfPTable table = new PdfPTable(3);
////        table.setTotalWidth(288);
////        table.setLockedWidth(true);
////        table.setWidths(new float[]{2, 1, 1});
//        PdfPCell cell;
//        cell = new PdfPCell(new Phrase("Table 2"));
//        cell.setColspan(3);
//        table.addCell(cell);
//        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
//        cell.setRowspan(2);
//        table.addCell(cell);
//        table.addCell("row 1; cell 1");
//        table.addCell("row 1; cell 2");
//        table.addCell("row 2; cell 1");
//        table.addCell("row 2; cell 2");
//        return table;
//    }
//
//    /**
//     * Creates a table; widths are set in the constructor.
//     * @return a PdfPTable
//     * @throws DocumentException
//     */
//    public static PdfPTable createTable3() throws DocumentException {
//        PdfPTable table = new PdfPTable(new float[]{ 2, 1, 1 });
//        table.setWidthPercentage(55.067f);
//        PdfPCell cell;
//        cell = new PdfPCell(new Phrase("Table 3"));
//        cell.setColspan(3);
//        table.addCell(cell);
//        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
//        cell.setRowspan(2);
//        table.addCell(cell);
//        table.addCell("row 1; cell 1");
//        table.addCell("row 1; cell 2");
//        table.addCell("row 2; cell 1");
//        table.addCell("row 2; cell 2");
//        return table;
//    }
//
//    /**
//     * Creates a table; widths are set with special setWidthPercentage() method.
//     * @return a PdfPTable
//     * @throws DocumentException
//     */
//    public static PdfPTable createTable4() throws DocumentException {
//        PdfPTable table = new PdfPTable(3);
//        Rectangle rect = new Rectangle(523, 770);
//        table.setWidthPercentage(new float[]{ 144, 72, 72 }, rect);
//        PdfPCell cell;
//        cell = new PdfPCell(new Phrase("Table 4"));
//        cell.setColspan(3);
//        table.addCell(cell);
//        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
//        cell.setRowspan(2);
//        table.addCell(cell);
//        table.addCell("row 1; cell 1");
//        table.addCell("row 1; cell 2");
//        table.addCell("row 2; cell 1");
//        table.addCell("row 2; cell 2");
//        return table;
//    }
//
//    /**
//     * Creates a table; widths are set with setTotalWidth().
//     * @return a PdfPTable
//     * @throws DocumentException
//     */
//    public static PdfPTable createTable5() throws DocumentException {
//        PdfPTable table = new PdfPTable(3);
//        table.setTotalWidth(new float[]{ 144, 72, 72 });
//        table.setLockedWidth(true);
//        PdfPCell cell;
//        cell = new PdfPCell(new Phrase("Table 5"));
//        cell.setColspan(3);
//        table.addCell(cell);
//        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
//        cell.setRowspan(2);
//        table.addCell(cell);
//        table.addCell("row 1; cell 1");
//        table.addCell("row 1; cell 2");
//        table.addCell("row 2; cell 1");
//        table.addCell("row 2; cell 2");
//        return table;
//    }
//
//    /**
//     * This method is responsible to add table using iText
//     * @param reportBody2
//     * @throws BadElementException
//     */
//    private static void createTable(Paragraph reportBody2)
//            throws BadElementException {
//
////        float[] columnWidths2 = {5,5,5,2};
////        PdfPTable table2 = new PdfPTable(columnWidths2);
////        table2.setWidthPercentage(100); //set table with 100% (full page)
////        table2.getDefaultCell().setUseAscender(true);
//
//        // a table with three columns
//        PdfPTable table3 = new PdfPTable(3);
//
//        // the cell object
//        PdfPCell cell3;
//        // we add a cell with colspan 3
//        cell3 = new PdfPCell(new Phrase("Cell with colspan 3"));
//        cell3.setColspan(3);
//        table3.addCell(cell3);
//        // now we add a cell with rowspan 2
//        cell3 = new PdfPCell(new Phrase("Cell with rowspan 2"));
//        cell3.setRowspan(2);
//        table3.addCell(cell3);
//        // we add the four remaining cells with addCell()
//        table3.addCell("row 1; cell 1");
//        table3.addCell("row 1; cell 2");
//        table3.addCell("row 2; cell 1");
//        table3.addCell("row 2; cell 2");
//
////        PdfPTable table4 = new PdfPTable(3);
////        table4.setWidthPercentage(288 / 5.23f);
////        try {
////            table4.setWidths(new int[]{2, 1, 1});
////        }catch (DocumentException de){
////        }
////        PdfPCell cell4;
////        cell4 = new PdfPCell(new Phrase("Table 1"));
////        cell4.setColspan(3);
////        table4.addCell(cell4);
////        cell4 = new PdfPCell(new Phrase("Cell with rowspan 2"));
////        cell4.setRowspan(2);
////        table4.addCell(cell4);
////        table4.addCell("row 1; cell 1");
////        table4.addCell("row 1; cell 2");
////        table4.addCell("row 2; cell 1");
////        table4.addCell("row 2; cell 2");
//
//
//
////        float[] columnWidths = {5,5,5,2}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
////        PdfPTable table = new PdfPTable(columnWidths);
////
////        table.setWidthPercentage(100); //set table with 100% (full page)
////        table.getDefaultCell().setUseAscender(true);
////
////
////        //Adding table headers
////        PdfPCell cell = new PdfPCell(new Phrase("Countainer", //Table Header
////                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
////        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
////        cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
////        cell.setFixedHeight(30); //cell height
////        table.addCell(cell);
////
////        cell = new PdfPCell(new Phrase("Amount",
////                StaticValue.FONT_TABLE_HEADER));
////        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
////        cell.setBackgroundColor(new GrayColor(0.75f));
////        cell.setFixedHeight(30);
////        table.addCell(cell);
////
////        cell = new PdfPCell(new Phrase("Amount per Countainer",
////                StaticValue.FONT_TABLE_HEADER));
////        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
////        cell.setBackgroundColor(new GrayColor(0.75f));
////        cell.setFixedHeight(30);
////        table.addCell(cell);
////
////        cell = new PdfPCell(new Phrase("Total Amount",
////                StaticValue.FONT_TABLE_HEADER));
////        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
////        cell.setBackgroundColor(new GrayColor(0.75f));
////        cell.setFixedHeight(30);
////        table.addCell(cell);
////
////        //End of adding table headers
////
////        //This method will generate some static data for the table
////        generateTableData();
////
////        //Adding data into table
////        for (int i = 0; i < arrayListRProductModel.size(); i++) { //
////            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getName()));
////            cell.setFixedHeight(28);
////            table.addCell(cell);
////
////            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getBrandName()));
////            cell.setFixedHeight(28);
////            table.addCell(cell);
////
////            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getCategoryName()));
////            cell.setFixedHeight(28);
////            table.addCell(cell);
////
////            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getUnitName()));
////            cell.setFixedHeight(28);
////            table.addCell(cell);
////        }
//
//        reportBody2.add(table3);
//
//    }
//
//    /**
//     * This method is used to add empty lines in the document
//     * @param paragraph
//     * @param number
//     */
//    private static void addEmptyLine(Paragraph paragraph, int number) {
//        for (int i = 0; i < number; i++) {
//            paragraph.add(new Paragraph(" "));
//        }
//    }
//
//    /**
//     * This is an inner class which is used to create header and footer
//     * @author XYZ
//     *
//     */
//
//    class PageHeaderFooter extends PdfPageEventHelper {
//        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
//
//        public void onEndPage(PdfWriter writer, Document document) {
//
//            /**
//             * PdfContentByte is an object containing the user positioned text and graphic contents
//             * of a page. It knows how to apply the proper font encoding.
//             */
//            PdfContentByte cb = writer.getDirectContent();
//
//            /**
//             * In iText a Phrase is a series of Chunks.
//             * A chunk is the smallest significant part of text that can be added to a document.
//             *  Most elements can be divided in one or more Chunks. A chunk is a String with a certain Font
//             */
//            Phrase footer_poweredBy = new Phrase("Powered By SIAS ERP", StaticValue.FONT_HEADER_FOOTER); //public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
//            Phrase footer_pageNumber = new Phrase("Page " + document.getPageNumber(), StaticValue.FONT_HEADER_FOOTER);
//
//            // Header
//            // ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header,
//            // (document.getPageSize().getWidth()-10),
//            // document.top() + 10, 0);
//
//            // footer: show page number in the bottom right corner of each age
//            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
//                    footer_pageNumber,
//                    (document.getPageSize().getWidth() - 10),
//                    document.bottom() - 10, 0);
////			// footer: show page number in the bottom right corner of each age
//            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
//                    footer_poweredBy, (document.right() - document.left()) / 2
//                            + document.leftMargin(), document.bottom() - 10, 0);
//        }
//    }
//
//    /**
//     * Generate static data for table
//     */
//
//    private static void generateTableData(){
//        ProductModel productModel = new ProductModel("Samsung Galaxy Note", "Piece", "Samsung", "Smartphone");
//        arrayListRProductModel.add(productModel);
//
//        productModel = new ProductModel("HTC One", "Piece", "HTC", "Smartphone");
//        arrayListRProductModel.add(productModel);
//
//        productModel = new ProductModel("LG Mini", "Piece", "LG", "Smartphone");
//        arrayListRProductModel.add(productModel);
//
//        productModel = new ProductModel("iPhone 6", "Piece", "Apple", "Smartphone");
//        arrayListRProductModel.add(productModel);
//    }
//}
