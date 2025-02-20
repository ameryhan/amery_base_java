package amery.activeMQ;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public abstract class JMSQueueAppender extends AppenderSkeleton implements Appender {


    private static Logger logger = Logger.getLogger("JMSQueueAppender");


    private String brokerUri;

    private String queueName;


    //@Override
    public void close() {


    }


    //@Override
    public boolean requiresLayout() {

        return false;

    }


    //@Override
    protected synchronized void append(LoggingEvent event) {


        try {


            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(

                    this.brokerUri);


            // Create a Connection

            javax.jms.Connection connection = connectionFactory.createConnection();

            connection.start();//np


            // Create a Session

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);


            // Create the destination (Topic or Queue)

            Destination destination = session.createQueue(this.queueName);


            // Create a MessageProducer from the Session to the Topic or Queue

            MessageProducer producer = session.createProducer(destination);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


            ObjectMessage message = session.createObjectMessage(new LoggingEventWrapper(event));


            // Tell the producer to send the message

            producer.send(message);


            // Clean up

            session.close();

            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    public void setBrokerUri(String brokerUri) {

        this.brokerUri = brokerUri;

    }


    public String getBrokerUri() {

        return brokerUri;

    }


    public void setQueueName(String queueName) {

        this.queueName = queueName;

    }


    public String getQueueName() {

        return queueName;

    }

}
