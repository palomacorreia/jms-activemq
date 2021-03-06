package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.*;
import javax.naming.InitialContext;

public class TesteConsumidorTopicoComercial {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
    	System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","java.lang,br.com.caelum.modelo");
        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.setClientID("estoque");
        
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination topico = (Destination) context.lookup("comercial");
        MessageConsumer consumer = session.createDurableSubscriber((Topic) topico, "assinatura");

        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {

                TextMessage textMessage = (TextMessage)message;

                try {
                    System.out.println(textMessage.getText());
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
