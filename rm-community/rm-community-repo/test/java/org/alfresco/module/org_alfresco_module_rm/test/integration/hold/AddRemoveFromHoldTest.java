package org.alfresco.module.org_alfresco_module_rm.test.integration.hold;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.module.org_alfresco_module_rm.test.util.BaseRMTestCase;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.webscripts.GUID;

/**
 * Hold service integration test.
 *
 * @author Roy Wetherall
 * @since 2.2
 */
public class AddRemoveFromHoldTest extends BaseRMTestCase
{
    private static final int RECORD_COUNT = 10;
    
    public void testAddRecordToHold()
    { 
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {
            private NodeRef hold;
            private NodeRef recordCategory;
            private NodeRef recordFolder;
            private NodeRef record;
            
            public void given()
            {   
                // create a hold
                hold = holdService.createHold(filePlan, GUID.generate(), GUID.generate(), GUID.generate());
                
                // create a record folder that contains records  
                recordCategory = filePlanService.createRecordCategory(filePlan, GUID.generate());
                recordFolder = recordFolderService.createRecordFolder(recordCategory, GUID.generate());
                record = recordService.createRecordFromContent(recordFolder, GUID.generate(), ContentModel.TYPE_CONTENT, null, null);
                
                // assert current states
                assertFalse(freezeService.isFrozen(recordFolder));
                assertFalse(freezeService.isFrozen(record));
                assertFalse(freezeService.hasFrozenChildren(recordFolder));
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(0, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }
            
            public void when() throws Exception
            {      
                // add the record to hold
                holdService.addToHold(hold, record);
            }
            
            public void then()
            {
                // record is held    
                assertTrue(freezeService.isFrozen(record));
                
                // record folder has frozen children
                assertFalse(freezeService.isFrozen(recordFolder));
                assertTrue(freezeService.hasFrozenChildren(recordFolder));
                
                // record folder is not held
                assertFalse(holdService.getHeld(hold).contains(recordFolder));
                assertFalse(holdService.heldBy(recordFolder, true).contains(hold));
                
                // hold contains record
                assertTrue(holdService.getHeld(hold).contains(record));
                assertTrue(holdService.heldBy(record, true).contains(hold));
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(1, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }            
        });
        
    }
    
    public void testAddRecordsToHold()
    { 
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {   
            private NodeRef hold;
            private NodeRef recordCategory;
            private NodeRef recordFolder;
            private List<NodeRef> records = new ArrayList<NodeRef>(RECORD_COUNT);
            
            public void given()
            {   
                // create a hold
                hold = holdService.createHold(filePlan, GUID.generate(), GUID.generate(), GUID.generate());
                
                // create a record folder that contains records  
                recordCategory = filePlanService.createRecordCategory(filePlan, GUID.generate());
                recordFolder = recordFolderService.createRecordFolder(recordCategory, GUID.generate());
                for (int i = 0; i < RECORD_COUNT; i++)
                {
                    records.add(recordService.createRecordFromContent(recordFolder, GUID.generate(), ContentModel.TYPE_CONTENT, null, null));
                }
                
                // assert current states
                assertFalse(freezeService.isFrozen(recordFolder));
                assertFalse(freezeService.hasFrozenChildren(recordFolder));
                for (NodeRef record : records)
                {
                    assertFalse(freezeService.isFrozen(record));
                }
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(0, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }
            
            public void when() throws Exception
            {      
                // add the record to hold
                holdService.addToHold(hold, records);
            }
            
            public void then()
            {
                // record is held    
                for (NodeRef record : records)
                {                
                    assertTrue(freezeService.isFrozen(record));
                }
                
                // record folder has frozen children
                assertFalse(freezeService.isFrozen(recordFolder));
                assertTrue(freezeService.hasFrozenChildren(recordFolder));
                
                // record folder is not held
                assertFalse(holdService.getHeld(hold).contains(recordFolder));
                assertFalse(holdService.heldBy(recordFolder, true).contains(hold));
                
                for (NodeRef record : records)
                {                                    
                    // hold contains record
                    assertTrue(holdService.getHeld(hold).contains(record));
                    assertTrue(holdService.heldBy(record, true).contains(hold));
                }
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(RECORD_COUNT, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }            
        });        
    }
    
    public void testAddRecordFolderToHold()
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {   
            private NodeRef hold;
            private NodeRef recordCategory;
            private NodeRef recordFolder;
            private List<NodeRef> records = new ArrayList<NodeRef>(RECORD_COUNT);
            
            public void given()
            {   
                // create a hold
                hold = holdService.createHold(filePlan, GUID.generate(), GUID.generate(), GUID.generate());
                
                // create a record folder that contains records  
                recordCategory = filePlanService.createRecordCategory(filePlan, GUID.generate());
                recordFolder = recordFolderService.createRecordFolder(recordCategory, GUID.generate());
                for (int i = 0; i < RECORD_COUNT; i++)
                {
                    records.add(recordService.createRecordFromContent(recordFolder, GUID.generate(), ContentModel.TYPE_CONTENT, null, null));
                }
                
                // assert current states
                assertFalse(freezeService.isFrozen(recordFolder));
                assertFalse(freezeService.hasFrozenChildren(recordFolder));
                for (NodeRef record : records)
                {
                    assertFalse(freezeService.isFrozen(record));
                }
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(0, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }
            
            public void when() throws Exception
            {                      
                // add the record to hold
                holdService.addToHold(hold, recordFolder);
            }
            
            public void then()
            {
                for (NodeRef record : records)
                {
                    // record is held    
                    assertTrue(freezeService.isFrozen(record));
                    assertFalse(holdService.getHeld(hold).contains(record));
                    assertTrue(holdService.heldBy(record, true).contains(hold));
                }
                
                // record folder has frozen children
                assertTrue(freezeService.isFrozen(recordFolder));
                assertTrue(freezeService.hasFrozenChildren(recordFolder));
                
                // hold contains record folder
                assertTrue(holdService.getHeld(hold).contains(recordFolder));
                assertTrue(holdService.heldBy(recordFolder, true).contains(hold));
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(RECORD_COUNT, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }            
        });
        
    }
    
    public void testRemoveRecordsFromHold()
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {   
            private NodeRef hold;
            private NodeRef recordCategory;
            private NodeRef recordFolder;
            private List<NodeRef> records = new ArrayList<NodeRef>(RECORD_COUNT);
            
            public void given()
            {   
                // create a hold
                hold = holdService.createHold(filePlan, GUID.generate(), GUID.generate(), GUID.generate());
                
                // create a record folder that contains records  
                recordCategory = filePlanService.createRecordCategory(filePlan, GUID.generate());
                recordFolder = recordFolderService.createRecordFolder(recordCategory, GUID.generate());
                for (int i = 0; i < RECORD_COUNT; i++)
                {
                    records.add(recordService.createRecordFromContent(recordFolder, GUID.generate(), ContentModel.TYPE_CONTENT, null, null));
                }
                
                // add records to hold
                holdService.addToHold(hold, records);
            }
            
            public void when() throws Exception
            {      
                // remove *some* of the records
                holdService.removeFromHold(hold, records.subList(0, 5));
            }
            
            public void then()
            {
                // check record state (no longer held)   
                for (NodeRef record : records.subList(0, 5))
                {                
                    assertFalse(freezeService.isFrozen(record));
                    assertFalse(holdService.getHeld(hold).contains(record));
                    assertFalse(holdService.heldBy(record, true).contains(hold));
                }
                
                // check record state (still held)   
                for (NodeRef record : records.subList(5, 10))
                {                
                    assertTrue(freezeService.isFrozen(record));
                    assertTrue(holdService.getHeld(hold).contains(record));
                    assertTrue(holdService.heldBy(record, true).contains(hold));
                }
                
                // record folder has frozen children
                assertFalse(freezeService.isFrozen(recordFolder));
                assertTrue(freezeService.hasFrozenChildren(recordFolder));
                
                // record folder is not held
                assertFalse(holdService.getHeld(hold).contains(recordFolder));
                assertFalse(holdService.heldBy(recordFolder, true).contains(hold));
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(5, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }            
        });         
    }
    
    public void testRemoveAllRecordsFromHold()
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {   
            private NodeRef hold;
            private NodeRef recordCategory;
            private NodeRef recordFolder;
            private List<NodeRef> records = new ArrayList<NodeRef>(RECORD_COUNT);
            
            public void given()
            {   
                // create a hold
                hold = holdService.createHold(filePlan, GUID.generate(), GUID.generate(), GUID.generate());
                
                // create a record folder that contains records  
                recordCategory = filePlanService.createRecordCategory(filePlan, GUID.generate());
                recordFolder = recordFolderService.createRecordFolder(recordCategory, GUID.generate());
                for (int i = 0; i < RECORD_COUNT; i++)
                {
                    records.add(recordService.createRecordFromContent(recordFolder, GUID.generate(), ContentModel.TYPE_CONTENT, null, null));
                }
                
                // add records to hold
                holdService.addToHold(hold, records);
            }
            
            public void when() throws Exception
            {      
                // remove all of the records
                holdService.removeFromHold(hold, records);
            }
            
            public void then()
            {
                // check record state (no longer held)   
                for (NodeRef record : records)
                {                
                    assertFalse(freezeService.isFrozen(record));
                    assertFalse(holdService.getHeld(hold).contains(record));
                    assertFalse(holdService.heldBy(record, true).contains(hold));
                }
                
                // record folder has frozen children
                assertFalse(freezeService.isFrozen(recordFolder));
                assertFalse(freezeService.hasFrozenChildren(recordFolder));
                
                // record folder is not held
                assertFalse(holdService.getHeld(hold).contains(recordFolder));
                assertFalse(holdService.heldBy(recordFolder, true).contains(hold));
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(0, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }            
        });         
    }
    
    public void testRemoveRecordFolderFromHold()
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {   
            private NodeRef hold;
            private NodeRef recordCategory;
            private NodeRef recordFolder;
            private List<NodeRef> records = new ArrayList<NodeRef>(RECORD_COUNT);
            
            public void given()
            {   
                // create a hold
                hold = holdService.createHold(filePlan, GUID.generate(), GUID.generate(), GUID.generate());
                
                // create a record folder that contains records  
                recordCategory = filePlanService.createRecordCategory(filePlan, GUID.generate());
                recordFolder = recordFolderService.createRecordFolder(recordCategory, GUID.generate());
                for (int i = 0; i < RECORD_COUNT; i++)
                {
                    records.add(recordService.createRecordFromContent(recordFolder, GUID.generate(), ContentModel.TYPE_CONTENT, null, null));
                }
                
                // add record folder to hold
                holdService.addToHold(hold, recordFolder);
            }
            
            public void when() throws Exception
            {      
                // remove record folder from hold
                holdService.removeFromHold(hold, recordFolder);
            }
            
            public void then()
            {
                // check record states   
                for (NodeRef record : records)
                {                
                    assertFalse(freezeService.isFrozen(record));
                    assertFalse(holdService.getHeld(hold).contains(record));
                    assertFalse(holdService.heldBy(record, true).contains(hold));
                }
                
                // record folder has frozen children
                assertFalse(freezeService.isFrozen(recordFolder));
                assertFalse(freezeService.hasFrozenChildren(recordFolder));
                
                // record folder is not held
                assertFalse(holdService.getHeld(hold).contains(recordFolder));
                assertFalse(holdService.heldBy(recordFolder, true).contains(hold));
                
                // additional check for child held caching
                assertTrue(nodeService.hasAspect(recordFolder, ASPECT_HELD_CHILDREN));
                assertEquals(0, nodeService.getProperty(recordFolder, PROP_HELD_CHILDREN_COUNT));
            }            
        });         
    }
}
