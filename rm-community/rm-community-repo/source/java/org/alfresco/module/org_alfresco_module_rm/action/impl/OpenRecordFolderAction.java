package org.alfresco.module.org_alfresco_module_rm.action.impl;

import org.alfresco.model.ContentModel;
import org.alfresco.module.org_alfresco_module_rm.action.RMActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.surf.util.I18NUtil;

/**
 * Action to re-open the records folder
 *
 * @author Roy Wetherall
 */
public class OpenRecordFolderAction extends RMActionExecuterAbstractBase
{
    /** Logger */
    private static Log logger = LogFactory.getLog(OpenRecordFolderAction.class);

    /** I18N */
    private static final String MSG_NO_OPEN_RECORD_FOLDER = "rm.action.no-open-record-folder";

    /** Parameter names */
    public static final String PARAM_OPEN_PARENT = "openParent";

    /**
     * @see org.alfresco.repo.action.executer.ActionExecuterAbstractBase#executeImpl(org.alfresco.service.cmr.action.Action,
     *      org.alfresco.service.cmr.repository.NodeRef)
     */
    @Override
    protected void executeImpl(Action action, NodeRef actionedUponNodeRef)
    {
        if (getNodeService().exists(actionedUponNodeRef) &&
            !getFreezeService().isFrozen(actionedUponNodeRef) &&
            !(getDictionaryService().isSubClass(getNodeService().getType(actionedUponNodeRef), ContentModel.TYPE_CONTENT) && !getRecordService().isFiled(actionedUponNodeRef)))
        {
            // TODO move re-open logic into a service method
            // TODO check that the user in question has the correct permission to re-open a records folder

            if (getRecordService().isRecord(actionedUponNodeRef))
            {
                ChildAssociationRef assocRef = getNodeService().getPrimaryParent(actionedUponNodeRef);
                if (assocRef != null)
                {
                    actionedUponNodeRef = assocRef.getParentRef();
                }
            }

            if (getRecordFolderService().isRecordFolder(actionedUponNodeRef))
            {
                Boolean isClosed = (Boolean) getNodeService().getProperty(actionedUponNodeRef, PROP_IS_CLOSED);
                if (Boolean.TRUE.equals(isClosed))
                {
                    getNodeService().setProperty(actionedUponNodeRef, PROP_IS_CLOSED, false);
                }
            }
            else
            {
                if (logger.isWarnEnabled())
                {
                    logger.warn(I18NUtil.getMessage(MSG_NO_OPEN_RECORD_FOLDER, actionedUponNodeRef.toString()));
                }
            }
        }
    }
}
