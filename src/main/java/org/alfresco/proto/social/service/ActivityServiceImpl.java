package org.alfresco.proto.social.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.alfresco.proto.social.api.Activity;
import org.alfresco.proto.social.api.ActivityService;
import org.alfresco.proto.social.api.Person;
import org.alfresco.proto.social.dao.BoundActivityDAO;
import org.alfresco.proto.social.dao.model.ActivityData;
import org.alfresco.proto.social.dao.model.IdentifierData;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;


public class ActivityServiceImpl implements ActivityService
{
    public static String ACTIVITY_POST_CHANNEL = "ActivityPost";
    
    private Channel channel;

    private BoundActivityDAO activityDAO;


    public ActivityServiceImpl(BoundActivityDAO activityDAO) throws IOException
    {
        this.activityDAO = activityDAO;
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.channel.queueDeclare(ACTIVITY_POST_CHANNEL, true, false, false, null);
    }
    
    
    @Override
    public Activity newActivity(Person fromPerson)
    {
        ActivityData activity = activityDAO.newActivityData();
        activity.setOriginatorPersonId(fromPerson.getId());
        activity.setPersonId(fromPerson.getId());
        return activity;
    }

    @Override
    public Activity postActivity(Activity activity)
    {
        ActivityData activityData = (ActivityData)activity;
        activityData.setPostedTime(new Date());
        String json = activityDAO.toJSON(activityData);
        
        try
        {
            channel.basicPublish( "", ACTIVITY_POST_CHANNEL, MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes());
        }
        catch (IOException e)
        {
            // TODO 
            e.printStackTrace();
        }
        
        return activityData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Activity> getActivities(Person forPerson)
    {
        return (List)activityDAO.findActivities((IdentifierData)forPerson.getId());
    }
    
}
