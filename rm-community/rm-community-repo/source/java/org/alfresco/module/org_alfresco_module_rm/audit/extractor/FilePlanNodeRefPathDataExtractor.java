package org.alfresco.module.org_alfresco_module_rm.audit.extractor;

import java.io.Serializable;
import java.util.List;

import org.alfresco.module.org_alfresco_module_rm.fileplan.FilePlanService;
import org.alfresco.module.org_alfresco_module_rm.model.RecordsManagementModel;
import org.alfresco.repo.audit.extractor.AbstractDataExtractor;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.rule.RuleService;

/**
 * An extractor that extracts the NodeRef path from the RM root down to
 * - and including - the node itself.  This will only extract data if the
 * node is a {@link RecordsManagementModel#ASPECT_FILE_PLAN_COMPONENT fileplan component}.
 *
 * @see FilePlanService#getNodeRefPath(NodeRef)
 *
 * @author Derek Hulley
 * @since 1.0
 */
public final class FilePlanNodeRefPathDataExtractor extends AbstractDataExtractor
{
    private NodeService nodeService;
    private FilePlanService filePlanService;
    private RuleService ruleService;

    /**
     * Used to check that the node in the context is a fileplan component
     */
    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }

    /**
     * @param filePlanService   file plan service
     */
    public void setFilePlanService(FilePlanService filePlanService)
    {
		this.filePlanService = filePlanService;
	}

    /**
     * @param ruleService the ruleService to set
     */
    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
    }

    /**
     * @return              Returns <tt>true</tt> if the data is a NodeRef and it represents
     *                      a fileplan component
     */
    public boolean isSupported(Serializable data)
    {
        if (!(data instanceof NodeRef))
        {
            return false;
        }
        return nodeService.hasAspect((NodeRef)data, RecordsManagementModel.ASPECT_FILE_PLAN_COMPONENT);
    }

    public Serializable extractData(Serializable value)
    {
        Serializable extractedData = null;

        ruleService.disableRules();
        try
        {
            NodeRef nodeRef = (NodeRef) value;

            // Get path from the RM root
            List<NodeRef> nodeRefPath = filePlanService.getNodeRefPath(nodeRef);

            // Done
            extractedData = (Serializable) nodeRefPath;
        }
        finally
        {
            ruleService.enableRules();
        }

        return extractedData;
    }
}
