package org.alfresco.repo.rule.ruletrigger;

import java.util.Set;

import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.transaction.TransactionalResourceHelper;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Prevent multiple triggering of outbound rules when moving records.
 *
 * @author Roy Wetherall
 */
public class ExtendedBeforeDeleteChildAssociationRuleTrigger
                extends RuleTriggerAbstractBase
                implements NodeServicePolicies.BeforeDeleteChildAssociationPolicy
{
    /**
     * The logger
     */
    private static Log logger = LogFactory.getLog(BeforeDeleteChildAssociationRuleTrigger.class);

    private static final String POLICY = "beforeDeleteChildAssociation";

    private boolean isClassBehaviour = false;

    public void setIsClassBehaviour(boolean isClassBehaviour)
    {
        this.isClassBehaviour = isClassBehaviour;
    }

    /**
     * @see org.alfresco.repo.rule.ruletrigger.RuleTrigger#registerRuleTrigger()
     */
    public void registerRuleTrigger()
    {
        if (isClassBehaviour)
        {
            this.policyComponent.bindClassBehaviour(
                    QName.createQName(NamespaceService.ALFRESCO_URI, POLICY),
                    this,
                    new JavaBehaviour(this, POLICY, NotificationFrequency.FIRST_EVENT));
        }
        else
        {
            this.policyComponent.bindAssociationBehaviour(
                    QName.createQName(NamespaceService.ALFRESCO_URI, POLICY),
                    this,
                    new JavaBehaviour(this, POLICY, NotificationFrequency.FIRST_EVENT));
        }
    }

    public void beforeDeleteChildAssociation(ChildAssociationRef childAssocRef)
    {
        // Break out early if rules are not enabled
        if (!areRulesEnabled())
        {
            return;
        }

        NodeRef childNodeRef = childAssocRef.getChildRef();

        // Avoid renamed nodes
        Set<NodeRef> renamedNodeRefSet = TransactionalResourceHelper.getSet(RULE_TRIGGER_RENAMED_NODES);
        if (renamedNodeRefSet.contains(childNodeRef))
        {
            return;
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("Single child assoc trigger (policy = " + POLICY + ") fired for parent node " + childAssocRef.getParentRef() + " and child node " + childAssocRef.getChildRef());
        }

        triggerRules(childAssocRef.getParentRef(), childNodeRef);
    }

}
