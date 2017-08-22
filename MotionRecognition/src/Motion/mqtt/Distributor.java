package Motion.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 
 * @author HwaSung Seo
 */

/**
 *  This class is for mqtt publisher.
 * It will be publish each value from sensor you connect this sever.
*/
public class Distributor {
	/**
	 *  url, this broker is made by mosquitto
	 */
	private String url = "tcp://106.253.56.122:1883";
	/**
	 *  mqttId, it will be used when mqtt client is generated.
	 */
	private String clientId;
	/**
	 *  subscribe topic string, it will be used when mqtt client is generated.
	 */
	private String request;
	/**
	 *  publish topic string, it will be used when mqtt client is generated.
	 */
	private String response;
	
	/**
	 *  mqttId, it will be used when mqtt client is generated.
	 */
	private String sensor;
	
	private int qos = 1;
	
	/**
	 *  MQTT client for publishing and subscribing 
	 */
	private MqttClient mqttClient;
	
	/**
	 *  MqttCallback is for handling after connection. <br/>
	 *  
	 */
	private MqttCallback callback = new MqttCallback(){
		/**
		*  
		*/
		@Override
		public void deliveryComplete(IMqttDeliveryToken imdt) {
			
		}
		/**
		*  
		*/
		@Override
		public void messageArrived(String string, MqttMessage mm) throws Exception {
			publish(sensor, mm.toString());			
		}
		/**
		*  
		*/
		@Override
		public void connectionLost(Throwable thrwbl) {
			try {
				close();
			} catch (MqttException ex) {
				ex.printStackTrace();
			}
		}
		
	};
	/**
	 * Constructor by parameter. It will be used for client for MQTT.
	 * @param clientId It is for mqtt id and topic
	 * @throws MqttException
	 */
	public Distributor(String clientId) throws MqttException {
		
		this.clientId =  MqttClient.generateClientId();
		
		mqttClient = new MqttClient(url, clientId);
		
		mqttClient.setCallback(callback);
		
		mqttClient.connect();
	}
	
	/**
	 *  
	 * @throws MqttException
	 */
	public void close() throws MqttException{
		if(mqttClient !=null){
			mqttClient.disconnect();
			mqttClient.close();
			mqttClient = null;
		}
	}
	/**
	 *  
	 * @throws MqttException
	 */
	public void subscribe(String sensor) throws MqttException{
		this.sensor = sensor;
		this.request = "/"+clientId+"/"+sensor+"/request";
		mqttClient.subscribe(request);
	}
	/**
	 *  
	 * @throws MqttException
	 */
	public void publish(String sensor, String json) throws MqttException{
		this.sensor = sensor;
		this.response ="/"+clientId+"/"+ sensor+"/response";
		MqttMessage mqttMessage = new MqttMessage(json.getBytes());
		mqttMessage.setQos(qos);
		mqttClient.publish(response, mqttMessage);
	}
}
