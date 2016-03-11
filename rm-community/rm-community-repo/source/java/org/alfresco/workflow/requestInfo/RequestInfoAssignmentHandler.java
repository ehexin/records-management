package org.alfresco.workflow.requestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.workflow.activiti.ActivitiScriptNode;
import org.alfresco.repo.workflow.activiti.ActivitiScriptNodeList;
import org.alfresco.util.ParameterCheck;
import org.springframework.extensions.surf.util.I18NUtil;

/**
 * An assignment handler for the request info workflow.
 * An RM manager/admin can select one or more user(s) and/or
 * one or more group(s) when starting the request info workflow.
 * This assignment handler assigns for everyone a task (it is a pooled task).
 *
 * @author Tuna Aksoy
 * @since 2.1
 */
public class RequestInfoAssignmentHandler implements TaskListener
{
    private static final long serialVersionUID = -3179929030094957978L;

    /**
     * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
     */
    @Override
    public void notify(DelegateTask delegateTask)
    {
        ParameterCheck.mandatory("delegateTask", delegateTask);

        // Set the workflow description for the task
        delegateTask.setVariable("bpm_workflowDescription", getWorkflowDescription(RequestInfoUtils.getRecordName(delegateTask)));

        // Get the list of user(s) and/or group(s)
        ActivitiScriptNodeList usersAndGroups = (ActivitiScriptNodeList) delegateTask.getVariable("rmwf_mixedAssignees");

        // Check if it was possible to extract the user(s) and/or group(s)
        if (usersAndGroups == null)
        {
            throw new AlfrescoRuntimeException("It was not possible to extract the user(s) and/or group(s)!!!");
        }

        // Define lists for candidate user(s)/group(s)
        List<String> candidateUsers = new ArrayList<String>();
        List<String> candidateGroups = new ArrayList<String>();

        // Iterate through the list add user(s)/group(s) to the lists
        for (ActivitiScriptNode activitiScriptNode : usersAndGroups)
        {
            // Get the node type
            String type = activitiScriptNode.getType();
            // Get the properties
            Map<String, Object> properties = activitiScriptNode.getProperties();

            // Check if it is a user or a group
            if (type.equalsIgnoreCase(ContentModel.TYPE_PERSON.toString()))
            {
                // Add the user
                candidateUsers.add((String) properties.get(ContentModel.PROP_USERNAME.toString()));
            }
            else if (type.equalsIgnoreCase(ContentModel.TYPE_AUTHORITY_CONTAINER.toString()))
            {
                // Add the group
                candidateGroups.add((String) properties.get(ContentModel.PROP_AUTHORITY_NAME.toString()));
            }
            else
            {
                throw new AlfrescoRuntimeException("The type '" + type + "' is neither a user nor a group!!!");
            }
        }

        // Check if there is at least one user or one group
        if (candidateUsers.size() == 0 && candidateGroups.size() == 0)
        {
            throw new AlfrescoRuntimeException("Neither a user nor a group was found!!!");
        }

        // Add the user(s) to the task
        if (candidateUsers.size() > 0)
        {
            delegateTask.addCandidateUsers(candidateUsers);
        }

        // Add the group(s) to the task
        if (candidateGroups.size() > 0)
        {
            delegateTask.addCandidateGroups(candidateGroups);
        }
    }

    /**
     * Helper method for building the workflow description
     *
     * @param recordName The name of the record
     * @return Returns the workflow description
     */
    private String getWorkflowDescription(String recordName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(I18NUtil.getMessage("activitiReviewPooled.workflow.info.requested"));
        sb.append(" '");
        sb.append(recordName);
        sb.append("'");
        return  sb.toString();
    }

}
