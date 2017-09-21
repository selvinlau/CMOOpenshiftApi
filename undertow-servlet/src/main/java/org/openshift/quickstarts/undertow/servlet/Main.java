package org.openshift.quickstarts.undertow.servlet;
import static spark.Spark.*;

import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.sql2o.Sql2o;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.Subscription.Keys;
import nl.martijndwars.webpush.Utils;

public class Main {

    public static void addSecurityProvider() {
        Security.addProvider(new BouncyCastleProvider());
    }
	public static void main(String[] args) {
		addSecurityProvider();
		Sql2o sql2o = new Sql2o("jdbc:mysql://155.69.160.33/cmo", "root", "root");
		Model.setSql2o(sql2o);

		

		get("/student", (req, res) -> {
			res.status(200);
			res.type("application/json");
			return dataToJson(Student.getAllStudent());
		});

		// Creation via string param
		post("/student/:f_name/:l_name", (req, res) -> {
			Student student = new Student();
			student.setF_name(req.params(":f_name"));
			student.setL_name(req.params(":l_name"));
			int createdID = Student.createStudent(student);

			return createdID;
		});

		// Creation via Json
		post("/student/create", (req, res) -> {
			ObjectMapper mapper = new ObjectMapper();
			Student student = mapper.readValue(req.body(),Student.class);
			int createdID = Student.createStudent(student);
			return createdID;
		});
		
		post("/notify", (req, res) -> {
			
			//unique for each user
			//****************************************************************
			String endpoint = "https://fcm.googleapis.com/fcm/send/d0S3ldUTpcI:APA91bF6diZqdo7TZgTj9nPlyQ9d_eJEluWdInYjdaIpZVgue-MMP1jl5avAp0oOcUE7P1zNOf-MBdGXIBInlK_HuHpiclzgNnQfan6RRod1Lep1k9230nOUE2La_jfxFBON6wnq-jyE";
			String userPublicKey = "BP1hb0dx4indRXw0ryvGowaF8KmJgINDc-FJA6Y5NTmKDxOod1o8Fw0ZYz1gJSeaaZelZTF2E94tvwHH2KBT0j0=";
			String userAuth = "Mc62T1IaSSqUg4zSydApGQ==";
			//****************************************************************
			
			
			//Unique for server
			//****************************************************************
			String serverPublic = "BMhjsB6I3RkrDJj4MPCgAkK9jbseglZevsFr9SvVYSy8VFOZj6goW9bpSoRUjp3rv0BRAuYkLzFsbtUbFvkpUBk";
			String serverPrivate = "RgI3e4KktwkcfXBgT5agm8ccW_4S3PsIFWPslJvth7s";
			//****************************************************************
			
			try {
				// Construct notification
				Notification notification;
				notification = new Notification(endpoint, userPublicKey, userAuth, getPayload());
				
				PushService pushService = new PushService();
		        pushService.setSubject("mailto:admin@martijndwars.nl");
		        pushService.setPublicKey(Utils.loadPublicKey(serverPublic));
		        pushService.setPrivateKey(Utils.loadPrivateKey(serverPrivate));
		        pushService.send(notification);
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		});
		

	}

	
	
	public static String dataToJson(Object data) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			StringWriter sw = new StringWriter();
			mapper.writeValue(sw, data);
			return sw.toString();
		} catch (IOException e) {
			throw new RuntimeException("IOException from a StringWriter?");
		}
	}

	private static byte[] getPayload() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("title", "Hello");
		jsonObject.addProperty("message", "World");

		return jsonObject.toString().getBytes();
	}



}
