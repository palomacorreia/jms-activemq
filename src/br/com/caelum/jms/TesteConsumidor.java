package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.*;
import javax.naming.InitialContext;

public class TesteConsumidor {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = factory.createConnection(); 
		connection.start();
		//Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//seremos o responsável pela confirmação
		  //Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		// mudando de false para true e usando SESSION_TRANSACTED
		 Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

		Destination fila = (Destination) context.lookup("financeiro");
		MessageConsumer consumer = session.createConsumer(fila );
		
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(((TextMessage) message).getText());
			       // message.acknowledge(); // fazendo programaticamente
			        session.commit(); 

				} catch (JMSException e) {
					e.printStackTrace();
				}

			}
		});

		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}
}
