package org.alfresco.proto.social.job;

import java.io.IOException;
import java.util.List;

import org.alfresco.proto.social.dao.BoundActivityDAO;
import org.alfresco.proto.social.dao.RelationshipDAO;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.alfresco.proto.social.dao.model.BoundActivityData;
import org.alfresco.proto.social.dao.model.IdentifierData;
import org.alfresco.proto.social.dao.model.RelationshipData;
import org.alfresco.proto.social.service.ActivityServiceImpl;
import org.alfresco.proto.util.counter.Counter;
import org.alfresco.proto.util.counter.Counters;

import com.google.code.morphia.mapping.MappingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ActivityPostJob
{
    private RelationshipDAO relationshipDAO;
    private BoundActivityDAO activityDAO;
    private Counter consume = Counters.instance.createThreadCounter("ConsumeActivity");
    private Counter produce = Counters.instance.createThreadCounter("ProduceBoundActivity");


    public ActivityPostJob(RelationshipDAO relationshipDAO, BoundActivityDAO activityDAO) throws IOException
    {
        this.relationshipDAO = relationshipDAO;
        this.activityDAO = activityDAO;
    }

    public void consumePosts() throws Exception
    {
        consume.start();
        produce.start();
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(ActivityServiceImpl.ACTIVITY_POST_CHANNEL, true, false, false, null);
        channel.basicQos(1);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(ActivityServiceImpl.ACTIVITY_POST_CHANNEL, false, consumer);

        try
        {
            while (true)
            {
                QueueingConsumer.Delivery delivery;
                try
                {
                    delivery = consumer.nextDelivery();
                }
                catch(InterruptedException e)
                {
                    return;
                }
                try
                {
                    String message = new String(delivery.getBody());
                    ActivityData activityData = activityDAO.toActivityData(message);
                    postActivities(activityData);
                }
                catch(IllegalArgumentException e)
                {
                    System.out.println("Failed to consume activity: " + e.getMessage());
                }
                catch(MappingException e)
                {
                    System.out.println("Failed to consume activity: " + e.getMessage());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        }
        finally
        {
            consume.stop();
            produce.stop();
            channel.close();
            connection.close();
        }
    }
    
    private void postActivities(ActivityData activity)
    {
        consume.incrementCount();
        
        ActivityData activityData = (ActivityData)activity;
        
        // for each follower, post bound activity
        List<RelationshipData> relationships = relationshipDAO.findFollowers((IdentifierData)activityData.getPersonId());
        for (RelationshipData relationship : relationships)
        {
            // TODO: full copy from activity data
            BoundActivityData boundActivityData = activityDAO.newBoundActivityData();
            boundActivityData.setOriginatorPersonId(activityData.getOriginatorPersonId());
            boundActivityData.setPersonId(activityData.getPersonId());
            boundActivityData.setPostedTime(activityData.getPostedTime());
            boundActivityData.setBody(activityData.getBody());
            boundActivityData.setForPersonId(relationship.getFollower().getId());
            activityDAO.save(boundActivityData);
            produce.incrementCount();
        }
    }
}
