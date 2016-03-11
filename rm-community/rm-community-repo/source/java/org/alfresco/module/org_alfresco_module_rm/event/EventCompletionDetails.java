package org.alfresco.module.org_alfresco_module_rm.event;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

/**
 * Event completion details
 * 
 * @author Roy Wetherall
 */
public class EventCompletionDetails
{
    /** node reference */
    private NodeRef nodeRef;
    
    /** event name */
    private String eventName;
    private String eventLabel;
    private boolean eventExecutionAutomatic;
    private boolean eventComplete;
    private Date eventCompletedAt;
    private String eventCompletedBy;


    /**
     * Constructor
     * 
     * @param nodeRef                   node reference
     * @param eventName                 event name
     * @param eventLabel                event label
     * @param eventExecutionAutomatic   indicates whether the event is executed automatically or not
     * @param eventComplete             indicates whether the event is complete or not
     * @param eventCompletedAt          when the event was completed
     * @param eventCompletedBy          who completed the event
     */
    public EventCompletionDetails(  NodeRef nodeRef,
                                    String eventName,
                                    String eventLabel,
                                    boolean eventExecutionAutomatic, 
                                    boolean eventComplete,
                                    Date eventCompletedAt, 
                                    String eventCompletedBy)
    {
        this.nodeRef = nodeRef;
        this.eventName = eventName;
        this.eventLabel = eventLabel;
        this.eventExecutionAutomatic = eventExecutionAutomatic;
        this.eventComplete = eventComplete;
        this.eventCompletedAt = eventCompletedAt;
        this.eventCompletedBy = eventCompletedBy;
    } 
    
    /**
     * @return the node reference
     */
    public NodeRef getNodeRef()
    {
        return nodeRef;
    }
    
    /**
     * @return the eventName
     */
    public String getEventName()
    {
        return eventName;
    }
    
    /**
     * @param eventName the eventName to set
     */
    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }
    
    /**
     * @return The display label of the event
     */
    public String getEventLabel()
    {
        return this.eventLabel;
    }
    
    /**
     * @return the eventExecutionAutomatic
     */
    public boolean isEventExecutionAutomatic()
    {
        return eventExecutionAutomatic;
    }
    
    /**
     * @param eventExecutionAutomatic the eventExecutionAutomatic to set
     */
    public void setEventExecutionAutomatic(boolean eventExecutionAutomatic)
    {
        this.eventExecutionAutomatic = eventExecutionAutomatic;
    }
    
    /**
     * @return the eventComplete
     */
    public boolean isEventComplete()
    {
        return eventComplete;
    }
    
    /**
     * @param eventComplete the eventComplete to set
     */
    public void setEventComplete(boolean eventComplete)
    {
        this.eventComplete = eventComplete;
    }
    
    /**
     * @return the eventCompletedAt
     */
    public Date getEventCompletedAt()
    {
        return eventCompletedAt;
    }
    
    /**
     * @param eventCompletedAt the eventCompletedAt to set
     */
    public void setEventCompletedAt(Date eventCompletedAt)
    {
        this.eventCompletedAt = eventCompletedAt;
    }
    
    /**
     * @return the eventCompletedBy
     */
    public String getEventCompletedBy()
    {
        return eventCompletedBy;
    }
    
    /**
     * @param eventCompletedBy the eventCompletedBy to set
     */
    public void setEventCompletedBy(String eventCompletedBy)
    {
        this.eventCompletedBy = eventCompletedBy;
    }  
}
