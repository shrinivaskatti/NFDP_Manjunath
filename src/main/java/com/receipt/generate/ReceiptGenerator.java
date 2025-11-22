package com.receipt.generate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.receipt.bean.ReceiptData;


public class ReceiptGenerator {

	
	public static final Properties props = new Properties();
	public static final char[] PASSWORD = "password".toCharArray();
	public static String RECEIPT_NUMBER ="1";
	public static String strHeader ="Name,PAN,Mobile,Email,VolunteerEmail,2020 Trxn Date,2020,Reciept";
	public static float bodyTextSize = 14;
	public static String masterFileLocation ="C:/Users/shkatti/pvnDocs/2021-22/DonorMasterList_2021_1.csv";
//	public static String dest = "/home/ubuntu/pvn";
//	public  static final String logoImage = "/home/ubuntu/tomcat8/webapps/nfdp/Nilaya1.jpg"; 
//	public static final String receiptStore = "/home/ubuntu/pvn/NilayaFoundation_FY_2023.csv";
// 	public static final String donorStore = "/home/ubuntu/pvn/NilayaFoundation_DonorList.csv";
	
	
	public static final String receiptStore = "D:/pvnDocs/pvnDocs/NilayaFoundation_FY_2025_temp.csv";
	public static final String donorStore = "D:/pvnDocs/pvnDocs/NilayaFoundation_DonorList.csv";
	public static String dest = "D:/pvnDocs/pvnDocs/pvn_2025_temp/";
	public  static final String logoImage = "D:/pvn/N_POCW/src/main/webapp/Nilaya1.jpg";
	public static  FileInputStream fis;
	public static final String properties = "D:/PVN_Data/config.properties";
	
	public static void loadProperties() throws Exception {
		 fis = new FileInputStream(properties);
		props.load(fis);
	}
	
	public static String getProperty(String propertykey) throws Exception {
		if(props.isEmpty()) {
			loadProperties();
		}
		return props.getProperty(propertykey);
	}
	public static void setProperty(String key, String value)throws Exception {
		if(props.isEmpty()) {
			loadProperties();
		}
		props.setProperty(key, value);
		try (FileOutputStream fos = new FileOutputStream(properties)) {
	            props.store(fos, "Updated "+key);
	            System.out.println("Property updated.");
	        } catch (IOException e) {
	            throw e;
	        }
	}
	
	public static void main(String[] args) {
		
		 ReceiptGenerator.beginReceiptGeneration(args[0]);
	}
	
	public static void beginReceiptGeneration(String rootDir)
	{
		try
		{
			loadProperties();
			File donorOutputFile = new File(getProperty("com.receipt.destination")+"donorOutput.csv");
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(donorOutputFile));
			bWriter.write(getProperty("com.receipt.DonorOutputHeader")+"\n");
			List<HashMap> donorDataList = readMasterFile();
			System.out.println(donorDataList);
			for (int i = 0; i < donorDataList.size(); i++) {
				Map donorMap = donorDataList.get(i);
				String name=(String)donorMap.get("name");
				String mobileNumber = (String)donorMap.get("mobilenumber");
				String emailAddress =(String)donorMap.get("emailaddress");
				String pan =(String)donorMap.get("pan");
				String strDonationAmount = (String)donorMap.get("amount");
				String strTransactionDate =(String)donorMap.get("transactionDate");
				String scheme ="Scholorship";
				String volunteeremail = (String)donorMap.get("volunteeremail");
				System.out.println();
				String fileName = generatePdf(name, mobileNumber, emailAddress, pan, strDonationAmount, strTransactionDate,scheme);
				
				String strOutputLine = name+","+pan+","+mobileNumber+","+emailAddress+","+volunteeremail+","+strTransactionDate+
						","+strDonationAmount+","+fileName;
				bWriter.write(strOutputLine+"\n");	
			}
			bWriter.close();
						
			System.out.println("Doc Created");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static List<HashMap> readMasterFile() {
		ArrayList donorDataList = new ArrayList<Map>();
		try {
			
			Map donorData;
			String line ="";
			BufferedReader br = new BufferedReader(new FileReader(masterFileLocation));
			br.readLine();
			while ((line = br.readLine()) != null)   //returns a Boolean value  
			{  
				System.out.println(line);
				String[]data =  line.split(",");
				System.out.println("DataLength=> "+data.length);
				donorData = new HashMap<String, String>();
				
				if(data.length == 8) {
					donorData.put("name", data[0]);
					donorData.put("pan", data[1]);
					donorData.put("mobilenumber", data[2]);
					donorData.put("emailaddress", data[3]);
					donorData.put("transactionDate", data[7]);
					donorData.put("amount", data[6]);
					donorData.put("volunteeremail", data[5]);
					donorDataList.add(donorData);
				}else {
					System.out.println("Donor "+data[0]+" is skipped due to insufficient data");
					return null;
				}
				
				
			}
					
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		 
		
		return donorDataList;
	}
	
	public static String generatePdf(String name, String mobileNumber, String emailAddress, String pan,
			String strDonationAmount, String dateReceived, String scheme) throws Exception  {
		System.out.println("Generating Receipt - Started");
		String strFileName=name+"_"+mobileNumber+".pdf";
		strFileName = strFileName.replaceAll("\\s", "");
		String finalPath = null;
		try {
			finalPath = getProperty("com.receipt.destination")+strFileName;
			File destFile = new File(finalPath);
//		if(destFile.exists()) {
//			strFileName = name+"_"+RECEIPT_NUMBER+".pdf";
//		}
			PdfWriter writer = new PdfWriter(getProperty("com.receipt.destination")+strFileName);
			PdfDocument pdfDoc = new PdfDocument(writer);
			pdfDoc.addNewPage(); 
			Document document = new Document(pdfDoc); 
			
			configureHeader(document);

			setReceiptDetails(document);

			//addToPersonDetails(document, name, mobileNumber, emailAddress, pan);

			addReceiptBody(document, strDonationAmount, scheme, name, mobileNumber, dateReceived);


			addWatermark(pdfDoc, document);
			
			addLogo(document);
			
			PdfPage page = pdfDoc.getPage(1);
			Rectangle cropBox = page.getCropBox();
			Rectangle mediaBox = page.getMediaBox();
			
			page.setCropBox(new Rectangle(0, 350, 600, 500));

			PdfCanvas  canvas = new PdfCanvas(pdfDoc, 1);
			canvas.rectangle(10, 10, 400, 600);
			canvas.saveState();
			
			document.close();
			System.out.println("Generating Receipt - Finished");
		} catch (Exception e) {
		
			e.printStackTrace();
			throw e;
		}
		
		return finalPath;
	}

	private static void addLogo(Document document) {
		
		try {
			      
			ImageData data = ImageDataFactory.create(getProperty("com.receipt.logoImage"));              

			// Creating an Image object        
			Image image = new Image(data);   
			image.scale(0.03f, 0.03f);
			image.setFixedPosition(60, PageSize.A4.getHeight() - image.getImageScaledHeight() - 30);
			
			// Adding image to the document       
			document.add(image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}              
		
	}

	private static void addWatermark(PdfDocument pdfDoc, Document document) throws Exception {

		PdfCanvas over = new PdfCanvas(pdfDoc.getPage(1));
		Paragraph paragraph = new Paragraph("NILAYA FOUNDATION")
				.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
				.setFontSize(50);
		PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.1f);
		over.saveState();
		over.setExtGState(gs1);
		      
		ImageData data = ImageDataFactory.create(getProperty("com.receipt.logoImage"));              
		Image image = new Image(data);   
		image.scale(0.13f, 0.13f);
		image.setFixedPosition((PageSize.A4.getWidth() - image.getImageScaledWidth())/2, (PageSize.A4.getHeight() - 30)/2 );
		document.add(image);
		over.restoreState();
	}
	
	private static void addReceiptBody(Document document, String strDonationAmount, String scheme, String name, String mobileNumber, String receivedDate) throws Exception {

		StringBuilder sbContent = new StringBuilder();
		//sbContent.append("Received with thanks from Shri/Smt <<name>>, <<mobile>> the sum of rupees <<amount>> <<receivedDate>>");
		//sbContent.append(" towards Special Scholorship.");
		sbContent.append("Received with thanks from Shri/Smt ");
		String contentString1 = "Received with thanks from Shri/Smt ";
		String contentString2 = " <<mobile>> the sum of rupees <<amount>> <<receivedDate>> towards Special Scholorship.";
		
		
		Text nameText = new Text(name);
		nameText.setFontSize(bodyTextSize);
		nameText.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD));
		

		if(mobileNumber.isEmpty()) { 
			contentString2 = contentString2.replace("<<mobile>>",mobileNumber); 
		}else { 
			contentString2 = contentString2.replace("<<mobile>>", "Mobile : "+mobileNumber);
		}

		if(receivedDate.isEmpty()) { 
			contentString2 =	contentString2.replace("<<receivedDate>>", receivedDate);
		}else {
			contentString2 = contentString2.replace("<<receivedDate>>","on "+receivedDate);
		}
		contentString2 = contentString2.replace("<<amount>>", strDonationAmount);


		Text contentText1 = new Text(contentString1);
		contentText1.setFontSize(bodyTextSize);
		contentText1.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));

		Text contentText2 = new Text(contentString2);
		contentText2.setFontSize(bodyTextSize);
		contentText2.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
		
		Paragraph contentParagraph = new Paragraph();
	//	contentParagraph.add(new Tab());
		contentParagraph.add(contentText1);
		contentParagraph.add(nameText);
		contentParagraph.add(contentText2);
		contentParagraph.setTextAlignment(TextAlignment.JUSTIFIED);

		//PdfFont f1 =  PdfFontFactory.createRegisteredFont(StandardFonts.SYMBOL , PdfEncodings.UNICODE_BIG);		 
		 //PdfFont f1=  PdfFontFactory.getFont("resources/fonts/PlayfairDisplay-Regular.ttf", PdfFontFactory.IDENTITY_H, BaseFont.EMBEDDED, 12);
		//PdfFont font = PdfFontFactory.createFont("D:/temp/arial.ttf");
		//String strRupee = font.decode(new PdfString("\u10B9", PdfEncodings.UTF8));
		/*
		 * String imageFile = "D:/temp/indian_rupee_sign.jpg"; ImageData data =
		 * ImageDataFactory.create(imageFile); Image img = new Image(data);
		 * img.setWidth(15); img.setHeight(15);
		 */
		String strAmount = "  Amount Rs :"+strDonationAmount;
		Text amountText = new Text(strAmount);
		amountText.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
		
		Paragraph amountParagraph = new Paragraph(amountText);
		amountParagraph.setBorder(new SolidBorder(1));
		amountParagraph.setTextAlignment(TextAlignment.LEFT);
		amountParagraph.setVerticalAlignment(VerticalAlignment.MIDDLE);
		amountParagraph.setPaddingLeft(5);
		amountParagraph.setWidth(120);
		amountParagraph.setHeight(40);
		amountParagraph.setFontSize(12);
		
		String thanksString = "Thank you so much for your generous contribution and sustained support to Nilaya Foundation cause.";
		Text thanksText = new Text(thanksString);
		thanksText.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
		thanksText.setFontSize(11);
		Paragraph thanksParagraph = new Paragraph();
		thanksParagraph.setTextAlignment(TextAlignment.CENTER);
		thanksParagraph.add(thanksText);
		
		String trustPan ="Pan of Trust : AAAAP6998H \n";
		Text panText = new Text(trustPan);


		String exemption = "Exemption U/S 80G of I.T. Act 1961 vide 118/629/CIT-HBL/2008-09 ";
		Text exemptionText = new Text(exemption);

		Paragraph exemParagraph = new Paragraph();
		exemParagraph.setFontSize(9);
		exemParagraph.setTextAlignment(TextAlignment.CENTER);

		exemParagraph.add(exemptionText);
		exemParagraph.add(panText);

		String regards ="Regards,\nNilaya Foundation.";
		Text regardsText = new Text(regards);

		String declaration ="This is computer generated receipt do not require signature.";

		document.add(new Paragraph());
		document.add(new Paragraph());
		document.add(contentParagraph);
		document.add(new Paragraph());
		document.add(amountParagraph);
		document.add(new Paragraph());
		document.add(thanksParagraph);
		document.add(new Paragraph());
		document.add(exemParagraph);
		//document.add(new Paragraph(regardsText).setFontSize(12));
		document.add(new Paragraph(declaration).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
	}

	private static void addToPersonDetails(Document document, String name, String mobileNumber, String emailAddress, String strPAN) throws Exception {

		Text personName = new Text(name+"\n");
		personName.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));

		String mobileNoLabel= "Mobile : "+mobileNumber+"\n";
		Text mobileNumberText = new Text(mobileNoLabel);

		String emailLabel = "E-Mail : "+emailAddress+"\n";
		Text emailText = new Text(emailLabel);

		String panLabel = "PAN : "+strPAN+"\n";
		Text panText = new Text(panLabel);

		Paragraph donorDetails = new Paragraph();
		donorDetails.setTextAlignment(TextAlignment.LEFT);

		donorDetails.add(personName);
		donorDetails.add(panText);
		donorDetails.add(mobileNumberText);
		donorDetails.add(emailText);

		document.add(donorDetails);

	}

	/*
	 * public static void sign(String src, String dest, Certificate[] chain,
	 * PrivateKey pk, String digestAlgorithm, String provider,
	 * PdfSigner.CryptoStandard signatureType, String reason, String location)
	 * throws GeneralSecurityException, IOException { PdfReader reader = new
	 * PdfReader(src); PdfSigner signer = new PdfSigner(reader, new
	 * FileOutputStream(dest), new StampingProperties());
	 * 
	 * // Create the signature appearance Rectangle rect = new Rectangle(36, 648,
	 * 200, 100); PdfSignatureAppearance appearance =
	 * signer.getSignatureAppearance(); appearance .setReason(reason)
	 * .setLocation(location)
	 * 
	 * // Specify if the appearance before field is signed will be used // as a
	 * background for the signed field. The "false" value is the default value.
	 * .setReuseAppearance(false) .setPageRect(rect) .setPageNumber(1);
	 * signer.setFieldName("sig");
	 * 
	 * IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm,
	 * provider); IExternalDigest digest = new BouncyCastleDigest();
	 * 
	 * // Sign the document using the detached mode, CMS or CAdES equivalent.
	 * signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
	 * }
	 */
	private static void setReceiptDetails(Document document) throws Exception {
		Text receiptHeader = new Text("RECEIPT \n");
		receiptHeader.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
		receiptHeader.setFontSize(18);
		receiptHeader.setFontColor(ColorConstants.BLUE);

		Paragraph receiptParagraph = new Paragraph();
		receiptParagraph.add(receiptHeader);
		receiptParagraph.setTextAlignment(TextAlignment.CENTER);

		String strReceiptNumber = getReceiptNumber();
		Text receiptNumber = new Text(strReceiptNumber);
		receiptNumber.setTextAlignment(TextAlignment.LEFT);
		Paragraph receiptNumberParagraph = new Paragraph();

		receiptNumberParagraph.setTextAlignment(TextAlignment.LEFT);

		String date = getCurrentDate();
		Text dateText = new Text(date);
		dateText.setTextAlignment(TextAlignment.RIGHT);
		Paragraph dateTextParagraph = new Paragraph();

		//dateTextParagraph.setTextAlignment(TextAlignment.RIGHT);

		receiptNumberParagraph.add(receiptNumber);
		receiptNumberParagraph.add(new Tab());

		receiptNumberParagraph.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
		receiptNumberParagraph.add(dateText);

		Text toText = new Text("To,");
		toText.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
		Paragraph toParagraph = new Paragraph(toText);

		document.add(receiptParagraph);
		document.add(receiptNumberParagraph);
		//document.add(toParagraph);
	}

	private static String getCurrentDate() {

		String returnString="Date: ";
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();

		try {

			returnString=returnString+formatter.format(today);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnString;
	}

	/**          01234567
	 * Format  - NFYYYYMMNNN.
	 * @return Returns Receipt Number
	 * @throws Exception
	 */
	private static String getReceiptNumber() throws Exception {
		
		String finalReceiptNumber = "";
		Calendar cal =  Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.format("%02d",(cal.get(Calendar.MONTH)+1));
		File file = new File(getProperty("com.receipt.receiptStore"));
		
		if(file.exists()) {
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			String sLine = bReader.readLine();
			String lastLine = "";
			while(sLine != null) {
				
				lastLine = sLine ;
						sLine= bReader.readLine();
			}
			String[] entrySplit = lastLine.split(",");
			String prevReceiptNumber = entrySplit[0];
			String sequenceNumber = prevReceiptNumber.substring(8);
			int seqInt = Integer.parseInt(sequenceNumber);
			seqInt++;
			finalReceiptNumber="NF"+year+month+String.format("%04d", seqInt);;
		}else {
			finalReceiptNumber="NF"+year+month+"0001";
		}
		System.out.println("Receipt Number Generated :"+finalReceiptNumber);
		
		RECEIPT_NUMBER = finalReceiptNumber;
		setProperty("com.receipt.lastreceiptnumber", RECEIPT_NUMBER);
		return "Receipt No :"+finalReceiptNumber;
		
	}

	private static void configureHeader(Document document) throws IOException {

		Paragraph header = new Paragraph();
		header.setTextAlignment(TextAlignment.CENTER);
		header.setBorderBottom(new SolidBorder(2)); 

		Text headerText = new Text("NILAYA FOUNDATION\n");
		headerText.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
		headerText.setFontSize(30);
		//headerText.setFontColor(ColorConstants.BLUE);


		//header.setBackgroundColor(ColorConstants.ORANGE);


		String p = "(Wing of \"Prahlad Vidyarthi Welfare Association\")\n";

		Text description = new Text(p);
		description.setFontSize(10);
		description.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));

		String address =  "Registered under KSR Act 1960, Registration No."
				+ "376/2005 No. 1077, Shri Ram, 4th Cross, 9th Main, Srinivas Nagar, BSK "
				+ "1st Stage, Bengaluru - 560 050.\n Mobile : 94480 64123, 99805 41495 "
				+ "Web: www.nilayafoundation.org Email: nilaya.foundation@gmail.com";
		Text addressText = new Text(address);
		addressText.setFontSize(10);
		addressText.setTextAlignment(TextAlignment.JUSTIFIED);


		header.add(headerText);
		header.add(description);
		header.add(addressText);


		document.add(header);
		//document.add(descParagraph);
	}

	
	public static boolean storeIntoRecord(String name, String mobileNumber, String emailAddress, String pan,
			String strDonationAmount, String dateReceived, String scheme, String volunteeremail, String volunteername, String newDonor) {
		//Name,PAN,Mobile,Email,VolunteerEmail,2020 Trxn Date,2020,Reciept
		
		try {
			
			if("newDonor".equalsIgnoreCase(newDonor)) {
				addToDonorDatabase(name, mobileNumber, emailAddress, pan);
			}
			boolean isNewFile = false;
			File file = new File(getProperty("com.receipt.receiptStore"));
			
			if(!file.exists()) {
				isNewFile = true;
			}
			file.setWritable(true);
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true));
			
			if(isNewFile) {
				bWriter.write(getProperty("com.receipt.receiptStoreHeader")+"\n");
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(RECEIPT_NUMBER);
			sb.append(",");
			sb.append(name);
			sb.append(",");
			sb.append(pan);
			sb.append(",");
			sb.append(mobileNumber);
			sb.append(",");
			sb.append(emailAddress);
			sb.append(",");
			sb.append(strDonationAmount);
			sb.append(",");
			sb.append(dateReceived);
			sb.append(",");
			sb.append(volunteeremail+"\n");
			bWriter.write(sb.toString());
			bWriter.close();
			file.setReadOnly();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addToDonorDatabase(String name, String mobileNumber, String emailAddress, String pan) {
	
		try {
			File file = new File(getProperty("com.receipt.donorStore"));
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true));
			
			StringBuffer sb = new StringBuffer();
			sb.append(name);
			sb.append(",");
			sb.append(mobileNumber);
			sb.append(",");
			sb.append(emailAddress);
			sb.append(",");
			sb.append(pan);
			sb.append(",");
			sb.append(" ");
			
			bWriter.write(sb.toString()+"\n");
			bWriter.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public List getDonorList (String donorpan) {

		List<ReceiptData> returnList = new ArrayList<ReceiptData>();
		ReceiptData data ;
		try {
			File file = new File(getProperty("com.receipt.donorStore"));
			
			if(file.exists()) {
				BufferedReader bReader = new BufferedReader(new FileReader(file));
				String sLine = bReader.readLine();
				while ((sLine = bReader.readLine())!= null) {

					data = getDonorDataObject(sLine);

					if(data.getDonorPAN().equalsIgnoreCase(donorpan)) {
						returnList.add(data);
						System.out.println("2"+data.getDonorPAN());
						break;
					}
				
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnList;
	}
	
	public List getDonorListWithName (String donorname) {

		List<ReceiptData> returnList = new ArrayList<ReceiptData>();
		ReceiptData data ;
		System.out.println(donorname);
		StringBuffer sb = new StringBuffer();
		
		try {
			File file = new File(getProperty("com.receipt.donorStore"));
			
			if(file.exists()) {
				BufferedReader bReader = new BufferedReader(new FileReader(file));
				String sLine = bReader.readLine();
				while ((sLine = bReader.readLine())!= null) {

					data = getDonorDataObject(sLine);
					
					if(data != null && data.getDonorName().toUpperCase().startsWith(donorname.toUpperCase())) {
						
						returnList.add(data);
						
					}
				
				}

			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnList;
	}
	
	public List getReceiptList () {
		
		List<ReceiptData> returnList = new ArrayList<ReceiptData>();
		ReceiptData data ;
		try {
			File file = new File(getProperty("com.receipt.receiptStore"));
			
			if(file.exists()) {
				BufferedReader bReader = new BufferedReader(new FileReader(file));
				String sLine = bReader.readLine();
				while ((sLine = bReader.readLine())!= null) {

					data = getReceiptDataObject(sLine);
					returnList.add(data);
					
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnList;
	}

	/**
	 *     0		1  2    3      4     5                6            7
	 * Receipt No,Name,PAN,Mobile,Email,Donation Amount,Donation Date,VolunteerEmail
	 * 
	 * @param sLine
	 * @return
	 */
	public ReceiptData getReceiptDataObject(String sLine) {
		
		ReceiptData  data ;
		String[] dataSplit = sLine.split(",");
		
		data = new ReceiptData();
		data.setReceiptNumber(dataSplit[0]);
		data.setDonorName(dataSplit[1]);
		data.setDonorPAN(dataSplit[2]);
		data.setDonorMobileNumber(dataSplit[3]);
		data.setDonorEmail(dataSplit[4]);
		data.setDonationAmount(Long.parseLong(dataSplit[5]));
		data.setDonationDate(dataSplit[6]);
		
		if(dataSplit.length == 8) {
			data.setVolunteerEmail(dataSplit[7]);	
		}
		
		
		return data;
	}


	/**
	 *     0		1           2    3      4     
	 *    Name,MobileNumber,EmailId,PAN,Address
	 * 
	 * @param sLine
	 * @return
	 */
	public ReceiptData getDonorDataObject(String sLine) {
		
		ReceiptData  data = null ;
		String[] dataSplit = sLine.split(",");
		
		
		System.out.println(dataSplit.length+"--"+dataSplit[0]);
		if(dataSplit.length >= 4) {
			data = new ReceiptData();
			data.setDonorName(dataSplit[0]);
			data.setDonorMobileNumber(dataSplit[1]);
			data.setDonorEmail(dataSplit[2]);
			data.setDonorPAN(dataSplit[3]);
		
		}
		
		
		return data;
	}

	
	
	
}
