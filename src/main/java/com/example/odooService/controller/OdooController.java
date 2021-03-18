package com.example.odooService.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

import com.example.odooService.utility.RespListingRecord;
import com.example.odooService.utility.ResponseCalling;
import com.example.odooService.utility.ResponseCount;
import com.example.odooService.utility.ResponseList;
import com.example.odooService.utility.ResponseLogin;
import com.example.odooService.utility.RespDataBase;
import com.example.odooService.utility.RespCreateRecord;

@RestController
@Controller
@RequestMapping(value = "/odoo")
public class OdooController {

	@Autowired
	private ResponseLogin responselogin;

	@Autowired
	private ResponseCount responsecount;

	@Autowired
	private RespDataBase responsedb;

	@Autowired
	private ResponseList responselist;

	@Autowired
	private ResponseCalling responsecalling;

	@Autowired
	private RespListingRecord resplistingrecord;

	@Autowired
	private RespCreateRecord rescreaterecord;

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

	// final Map<String, Object> map = null;

	// Servicio de autenticación.

	@RequestMapping(value = "/login", method = RequestMethod.GET)
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

	@RequestMapping(value = "/dataBase", method = RequestMethod.GET)
	public RespDataBase dataBase() throws XmlRpcException, MalformedURLException {

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

	@RequestMapping(value = "/calling", method = RequestMethod.GET)
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseList list() throws MalformedURLException, XmlRpcException {

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

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public ResponseCount count() throws XmlRpcException, MalformedURLException {

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

			responsecount.setStatus("OK");
			responsecount.setCantidadRegistros(contar);
			return responsecount;

		} catch (XmlRpcException ex) {

			responsecount.setStatus("NOT-OK: " + ex);
			return responsecount;

		}

		catch (Exception e) {

			responsecount.setStatus("NOT-OK: " + e);
			return responsecount;
		}

	}

	// Servicio que hace la búsqueda de campos en específico de los registros que
	// existen la base de datos. Limitando la búsqueda a solo doce(12) registros.

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseList searchRead() throws XmlRpcException, MalformedURLException {

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
							
							//Arrays.asList(Arrays.asList(Arrays.asList("is_company", "=", true))),
							Arrays.asList(Arrays.asList(Arrays.asList("is_company", "!=", true))),							
							new HashMap<String, Object>() {
								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								{
									put("fields", Arrays.asList("name", "country_id", "comment", "company_type"));
									put("limit", 12);
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

	@RequestMapping(value = "/listingRecordFields", method = RequestMethod.GET)
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
	
	//Servicio para crear un registro. (Devuelve su identificador de Base de Datos).

	@RequestMapping(value = "/createRecord", method = RequestMethod.POST)
	public RespCreateRecord createRecord() throws XmlRpcException, MalformedURLException {

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

			final Integer id = (Integer) models.execute("execute_kw", Arrays.asList(dataBase, usserId, password,
					"res.partner", "create", Arrays.asList(new HashMap<String, Object>() {
						/**
						* 
						*/
						private static final long serialVersionUID = 1L;

						{
							put("name", "New Partner2");
						}
					})));

			rescreaterecord.setStatus("OK");
			rescreaterecord.setIdRegistro(id);

			return rescreaterecord;

		} catch (XmlRpcException ex) {

			responselist.setStatus("NOT-OK: " + ex);
			return rescreaterecord;

		}

		catch (Exception e) {

			responselist.setStatus("NOT-OK: " + e);
			return rescreaterecord;

		}
	}
}
