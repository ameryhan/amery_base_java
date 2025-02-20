package amery.activeMQ;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component

public class LogQueueListener implements MessageListener {

    public static Logger logger = Logger.getLogger(LogQueueListener.class);


    //@Autowired
    //private ILoggingService loggingService;


    @Override
    public void onMessage(final Message message) {

        if (message instanceof ObjectMessage) {

            try {

                final LoggingEventWrapper loggingEventWrapper = (LoggingEventWrapper) ((ObjectMessage) message).getObject();

                // loggingService.saveLog(loggingEventWrapper);

            } catch (final JMSException e) {

                logger.error(e.getMessage(), e);

            } catch (Exception e) {

                logger.error(e.getMessage(), e);

            }

        }

    }

}
