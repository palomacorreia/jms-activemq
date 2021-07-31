package br.com.caelum.jms;

import javax.jms.*;
import javax.naming.InitialContext;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

import java.util.Properties;
import java.util.Scanner;

public class TesteProdutorTopico {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		/*Configuração alternativa ao jndi.properties*/
		Properties properties = new Properties();
		properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");

		properties.setProperty("java.naming.provider.url", "tcp://192.168.0.94:61616");
		properties.setProperty("queue.financeiro", "fila.financeiro");

		Connection connection = factory.createConnection(); 
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination fila = (Destination) context.lookup("financeiro");

		MessageProducer producer = session.createProducer(fila);

		for(int i = 0; i < 1000; i ++) {
			Pedido pedido = new PedidoFactory().geraPedidoComValores();
			System.out.println(pedido.getCodigo());
			Message message = session.createObjectMessage(pedido);
			producer.send(message);
		}
/*		Interface QueueBrowser-https://docs.oracle.com/javaee/7/api/javax/jms/QueueBrowser.html
		Destination fila2 = (Destination) context.lookup("financeiro");
		QueueBrowser browser = session.createBrowser((Queue) fila2);*/

		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}
}
