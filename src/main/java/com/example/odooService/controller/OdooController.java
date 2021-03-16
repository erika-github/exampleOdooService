package com.example.odooService.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.example.odooService.utility.Response_list;
import com.example.odooService.utility.Response_db;
import com.example.odooService.utility.ResponseInt;
import com.example.odooService.utility.ResponseLogin;
import com.example.odooService.utility.ResponseCalling;
import com.example.odooService.utility.RespListingRecord;

@RestController
@Controller
@RequestMapping(value = "/odoo")
public class OdooController {

	@Autowired
	private ResponseLogin responselogin;

	@Autowired
	private ResponseInt responseint;

	@Autowired
	private Response_db responsedb;

	@Autowired
	private Response_list responselist;

	@Autowired
	private ResponseCalling responsecalling;

	@Autowired
	private RespListingRecord resplistingrecord;

	@Value("${login.OdooController.conect}")
	private String login;

	@Value("${passw.OdooController.conect}")
	private String password;

	@Value("${db.OdooController.connect}")
	private String dataBase;

	@Value("${puerto.OdooController.connect}")
	private int port;

	@Value("${host.OdooController.connect}")
	private String host;

	@Value("${uid.OdooController}")
	private int usserId;

	final Map<String, Object> map = null;

	// Servicio de autenticación.

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseLogin Connect() throws MalformedURLException, XmlRpcException {
		XmlRpcClient xmlrpcLogin = new XmlRpcClient();

		XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
		xmlrpcConfigLogin.setEnabledForExtensions(true);
		xmlrpcConfigLogin.setServerURL(new URL("http", host, port, "/xmlrpc/2/common"));
		xmlrpcLogin.setConfig(xmlrpcConfigLogin);

		try {
			// Connect
			Object[] params = new Object[] { dataBase, login, password };
			Object uid = xmlrpcLogin.execute("login", params);

			responselogin.setStatus("OK");
			responselogin.setUid((int) uid);

			return responselogin;

		} catch (XmlRpcException ex) {

			responselogin.setStatus("NOT-OK: " + ex);
			return responselogin;

		} catch (Exception e) {

			responselogin.setStatus("NOT-OK: " + e);
			return responselogin;
		}

	}

	// Servicio de solicitud de una base de datos de prueba a Odoo.

	@RequestMapping(value = "/dataBase", method = RequestMethod.POST)
	public Response_db dataBase() throws XmlRpcException, MalformedURLException {

		final XmlRpcClient client = new XmlRpcClient();

		try {

			final XmlRpcClientConfigImpl start_config = new XmlRpcClientConfigImpl();
			start_config.setServerURL(new URL("https://demo.odoo.com/start"));
			@SuppressWarnings("unchecked")
			final Map<String, String> info = (Map<String, String>) client.execute(start_config, "start",
					Collections.<String>emptyList());

			final String url = info.get("host"), db = info.get("database"), username = info.get("user"),
					password = info.get("password");

			responsedb.setStatus("OK");
			responsedb.setUrl(url);
			responsedb.setDb(db);
			responsedb.setUsername(username);
			responsedb.setPassword(password);

			return responsedb;

		} catch (XmlRpcException ex) {

			responsedb.setStatus("NOT-OK: " + ex);
			return responsedb;

		} catch (Exception e) {

			responsedb.setStatus("NOT-OK: " + e);
			return responsedb;

		}

	}

	@RequestMapping(value = "/calling", method = RequestMethod.POST)
	public ResponseCalling callinMethods() throws MalformedURLException, XmlRpcException {

		try {
			final XmlRpcClient models = new XmlRpcClient() {
				{
					setConfig(new XmlRpcClientConfigImpl() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						{

							setServerURL(new URL("http", host, port, "/xmlrpc/2/object"));
						}
					});
				}
			};

			Connect();

			Boolean calling = (Boolean) models.execute("execute_kw", Arrays.asList(dataBase, usserId, password,
					"res.partner", "check_access_rights", Arrays.asList("read"), new HashMap<String, Object>() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						{
							put("raise_exception", false);
						}
					}));

			responsecalling.setStatus("OK");
			responsecalling.setCallingMethods(calling);

			return responsecalling;

		} catch (XmlRpcException ex) {

			responsecalling.setStatus("NOT-OK: " + ex);
			responsecalling.setCallingMethods(false);
			return responsecalling;

		} catch (Exception e) {

			responsecalling.setStatus("NOT-OK:" + e);
			responsecalling.setCallingMethods(false);
			return responsecalling;
		}
	}

	// Servicio que devuelve los identificadores de la base de datos de todos los
	// registros que coinciden con el filtro.

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Response_list list() throws MalformedURLException, XmlRpcException {

		try {
			final XmlRpcClient models = new XmlRpcClient() {
				{
					setConfig(new XmlRpcClientConfigImpl() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						{

							setServerURL(new URL("http", host, port, "/xmlrpc/2/object"));
						}
					});
				}
			};

			List<Object> listado = Arrays.asList(
					(Object[]) models.execute("execute_kw", Arrays.asList(dataBase, usserId, password, "res.partner",
							"search", Arrays.asList(Arrays.asList(Arrays.asList("is_company", "=", true))))));

			responselist.setStatus("OK");
			responselist.setListado(listado);
			return responselist;

		} catch (XmlRpcException ex) {

			responselist.setStatus("NOT-OK: " + ex);
			return responselist;

		}

		catch (Exception e) {

			responselist.setStatus("NOT-OK: " + e);
			return responselist;

		}

	}

	// Servicio para recuperar solo el número de registros que coinciden con la
	// consulta.

	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public ResponseInt count() throws XmlRpcException, MalformedURLException {

		try {

			final XmlRpcClient models = new XmlRpcClient() {
				{
					setConfig(new XmlRpcClientConfigImpl() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						{

							setServerURL(new URL("http", host, port, "/xmlrpc/2/object"));
						}
					});
				}
			};

			Integer contar = (Integer) models.execute("execute_kw",
					Arrays.asList(dataBase, usserId, password, "res.partner", "search_count",
							Arrays.asList(Arrays.asList(Arrays.asList("is_company", "=", true)))));

			responseint.setStatus("OK");
			responseint.setCantidadRegistros(contar);
			return responseint;

		} catch (XmlRpcException ex) {

			responseint.setStatus("NOT-OK: " + ex);
			return responseint;

		}

		catch (Exception e) {

			responseint.setStatus("NOT-OK: " + e);
			return responseint;
		}

	}

	// Servicio que hace la búsqueda de campos en específico de los registros que
	// existen la base de datos. Limitando la búsqueda a solo cinco(5) registros.

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public Response_list searchRead() throws XmlRpcException, MalformedURLException {

		try {
			final XmlRpcClient models = new XmlRpcClient() {
				{
					setConfig(new XmlRpcClientConfigImpl() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						{

							setServerURL(new URL("http", host, port, "/xmlrpc/2/object"));
						}
					});
				}
			};

			List<Object> asList2 = Arrays.asList((Object[]) models.execute("execute_kw",
					Arrays.asList(dataBase, usserId, password, "res.partner", "search_read",
							Arrays.asList(Arrays.asList(Arrays.asList("is_company", "=", true))),
							new HashMap<String, Object>() {
								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								{
									put("fields", Arrays.asList("name", "country_id", "comment"));
									put("limit", 5);
								}
							})));

			responselist.setStatus("OK");
			responselist.setListado(asList2);
			return responselist;

		} catch (XmlRpcException ex) {

			responselist.setStatus("NOT-OK: " + ex);
			return responselist;

		}

		catch (Exception e) {

			responselist.setStatus("NOT-OK: " + e);
			return responselist;

		}

	}

	// Servicio que se utiliza para inspeccionar los campos de un modelo y comprobar
	// cuáles parecen ser de interés.

	@RequestMapping(value = "/listingRecordFields", method = RequestMethod.POST)
	public RespListingRecord listingRecordFields() throws XmlRpcException, MalformedURLException {

		try {
			final XmlRpcClient models = new XmlRpcClient() {
				{
					setConfig(new XmlRpcClientConfigImpl() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						{

							setServerURL(new URL("http", host, port, "/xmlrpc/2/object"));
						}
					});
				}
			};

			@SuppressWarnings("unchecked")
			final Map<String, Object> map = (Map<String, Object>) models.execute("execute_kw",
					Arrays.asList(dataBase, usserId, password, "res.partner", "fields_get",
							Collections.<String>emptyList(), new HashMap<String, Object>() {

								/**
									 * 
									 */
								private static final long serialVersionUID = 1L;

								{
									put("attributes", Arrays.asList("string", "help", "type"));
								}
							}));

			resplistingrecord.setStatus("OK");

			resplistingrecord.setListingRecords(map);

			return resplistingrecord;

		} catch (XmlRpcException ex) {

			resplistingrecord.setStatus("NOT-OK: " + ex);
			return resplistingrecord;
		}

		catch (Exception e) {

			resplistingrecord.setStatus("NOT-OK: " + e);
			return resplistingrecord;

		}

	}
}
