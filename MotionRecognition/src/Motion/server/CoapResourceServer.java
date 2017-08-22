package Motion.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import org.eclipse.californium.core.CaliforniumLogger;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.scandium.ScandiumLogger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author CheolMin Kim
 */
public class CoapResourceServer {

	private static final Logger logger = LoggerFactory.getLogger(CoapResourceServer.class);
	/**
	 * CoapServer
	 */
	public static CoapServer coapServer;

	//static block(californium의 자체 로그 출력 금지
	static {
		CaliforniumLogger.initialize();
		CaliforniumLogger.setLevel(Level.OFF);
		ScandiumLogger.initialize();
		ScandiumLogger.setLevel(Level.OFF);
	}
/**
 * registers the sensor resources with CoapServer.
 * @throws Exception 
 */
	public CoapResourceServer() throws Exception {
		coapServer = new CoapServer();
		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			if (!addr.isLinkLocalAddress()) {
				coapServer.addEndpoint(new CoapEndpoint(new InetSocketAddress(addr, CoAP.DEFAULT_COAP_PORT)));
			}
		}

		coapServer.add(new GyroscopeResource());
		coapServer.add(new UltrasonicResource());
		coapServer.add(new IRResource());
		coapServer.add(new ButtonResource());
		coapServer.add(new MQTTResource());
	}

	/**
	 * starts CoAP Server.
	 */
	public static void start() {
		coapServer.start();
	}

	/**
	 * stops CoAP Server.
	 * 
	 */
	public static void stop() throws MqttException {
		coapServer.stop();
		coapServer.destroy();
		MQTTResource.mqtt.close();
	}

}
