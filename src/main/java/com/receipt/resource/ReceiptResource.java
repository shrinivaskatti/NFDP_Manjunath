package com.receipt.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.receipt.bean.ReceiptData;
import com.receipt.emailutility.SendUtility;
import com.receipt.generate.ReceiptGenerator;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("receipt")
public class ReceiptResource {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 * @throws URISyntaxException 
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/setDetails")
	public Response getIt(@Context HttpServletRequest httpServletRequest, ReceiptData receiptData) throws URISyntaxException {

		System.out.println(receiptData);
		System.out.println(httpServletRequest.getRequestURL());

		String name = receiptData.getDonorName().trim();
		String mobileNumber = receiptData.getDonorMobileNumber().trim();
		String pan = receiptData.getDonorPAN().trim();
		String amount = receiptData.getDonationAmount().toString().trim();
		String transactiondate = receiptData.getDonationDate().toString().trim();
		String email = receiptData.getDonorEmail().trim();
		String volunteername = receiptData.getVolunteerName().trim();
		String volunteeremail = receiptData.getVolunteerEmail().trim();
		String newDonor = receiptData.getNewDonor();
		String scheme ="Scholorship";
		try {
			InputStream thankyouEmailResource = httpServletRequest.getServletContext().getResourceAsStream("/WEB-INF/thankYouEmail.txt");
			String generatePdf = ReceiptGenerator.generatePdf(name, mobileNumber, email, pan, amount, transactiondate, scheme );
			if(generatePdf!= null && !generatePdf.isEmpty()) {


				System.out.println("Pushing Donor Details into stoage - Started");
				ReceiptGenerator.storeIntoRecord(name, mobileNumber, email, pan, amount, transactiondate, scheme,volunteeremail,volunteername,newDonor);
				System.out.println("Pushing Donor Details into stoage - Finished");

				System.out.println("Emailing Reciept to Donor - Started");
				//SendUtility.send(name,email,volunteeremail,generatePdf, thankyouEmailResource);
				System.out.println("Emailing Reciept to Donor - Finished");
			}

		} catch (Exception e) {			
			e.printStackTrace();

			return Response.serverError().entity("Error."+e.getLocalizedMessage()).build();
		}
		return Response.ok().entity("Email Sent Successfully.").build();
	}


	@GET
	@Path("/getData")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ReceiptData> getData() {


		ReceiptGenerator  receiptGenerator = new ReceiptGenerator();
		List<ReceiptData> donorList = receiptGenerator.getReceiptList();


		Collections.reverse(donorList);
		return donorList;
	}

	@GET
	@Path("/getData/{donorpan}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ReceiptData> getData(@PathParam("donorpan") String donorpan) {


		ReceiptGenerator  receiptGenerator = new ReceiptGenerator();
		List<ReceiptData> donorList = receiptGenerator.getDonorList(donorpan);


		Collections.reverse(donorList);
		return donorList;
	}
	
	@GET
	@Path("/getData/name")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ReceiptData> getDataWithName(@QueryParam("term") String donorname) {


		ReceiptGenerator  receiptGenerator = new ReceiptGenerator();
		List<ReceiptData> donorList = receiptGenerator.getDonorListWithName(donorname);
		
		return donorList;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/setDetailsjson")
	public Response setDetailsBeans(ReceiptData receiptData) {

		System.out.println("data : "+receiptData);
		return Response.ok().entity(receiptData.getDonationAmount()).build();
	}
	
	
}
