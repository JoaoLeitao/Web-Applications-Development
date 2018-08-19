package isel.leic.daw.dawProject.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.*
import isel.leic.daw.dawProject.model.entity.Checklist
import isel.leic.daw.dawProject.model.entity.ChecklistItem
import isel.leic.daw.dawProject.model.inputModel.ChecklistDTO
import isel.leic.daw.dawProject.model.inputModel.ChecklistItemDTO
import isel.leic.daw.dawProject.model.outputModel.checklist.collection.CollectionChecklistItemSirenHTO
import isel.leic.daw.dawProject.model.outputModel.checklist.collection.CollectionChecklistSirenHTO
import isel.leic.daw.dawProject.model.outputModel.checklist.single.ChecklistItemSirenHTO
import isel.leic.daw.dawProject.model.outputModel.checklist.single.ChecklistSirenHTO
import isel.leic.daw.dawProject.model.outputModel.template.single.TemplateSirenHTO
import isel.leic.daw.dawProject.service.ChecklistItemService
import isel.leic.daw.dawProject.service.ChecklistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.ServletRequest

@RestController
@Api(description = "Operations about checklists")
@RequestMapping(value = ["/checklists"], produces = [(Siren4J.JSON_MEDIATYPE)])
class ChecklistRestController{

    @Autowired
    lateinit var checklistService : ChecklistService

    @Autowired
    lateinit var checklistItemService : ChecklistItemService

    /**
     * Get checklist with id
     * @param id id of the checklist
     * @return checklist
     */
    @ApiOperation(value = "View a checklist", response = Checklist::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the checklist"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to view this checklist"),
            ApiResponse(code = 403, message = "Accessing the checklist you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The checklist you were trying to reach is not found")
    )
    @GetMapping(name = "getChecklistById", value = ["/{checklistId}"])
    fun getChecklistById(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable checklistId : Int,
            user: ServletRequest) : ResponseEntity<Entity> {
        val checklist : Checklist = checklistService.getUserChecklist(checklistId, user)
        val siren = ChecklistSirenHTO(
                checklistName = checklist.checklistName,
                completionDate = checklist.completionDate.toString(),
                checklistId = checklist.checklistId,
                templateUsed = checklist.template?.templateId,
                template = if(checklist.template!=null) TemplateSirenHTO() else null,
                author = checklist.account!!.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Get all checklists
     * @return all checklists
     */
    @ApiOperation(value = "View all checklists", response = List::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the checklists"),
            ApiResponse(code = 401, message = "You are not authorized to view this checklists"),
            ApiResponse(code = 403, message = "Accessing the checklists you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The checklists you were trying to reach are not found")
    )
    @GetMapping(name = "getAllChecklists")
    fun getAllChecklists(user: ServletRequest) : ResponseEntity<Entity> {
        val list: List<Checklist> = checklistService.getUserChecklists(user)
        val siren = CollectionChecklistSirenHTO(
                list.map {
                    ChecklistSirenHTO(
                            checklistName = it.checklistName,
                            completionDate = it.completionDate.toString(),
                            checklistId = it.checklistId,
                            templateUsed = it.template?.templateId,
                            template = if(it.template!=null) TemplateSirenHTO() else null,
                            author = it.account!!.sub
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Create new checklist
     * @param checklist inputModel via request body, has information to create a checklist
     */
    @ApiOperation(value = "Create a checklist")
    @ApiResponses(
            ApiResponse(code = 201, message = "Successfully created the checklist"),
            ApiResponse(code = 401, message = "You are not authorized to create this checklist"),
            ApiResponse(code = 403, message = "Creating the checklist you were trying to reach is forbidden")
    )
    @PostMapping(name = "postSingleChecklist")
    fun addChecklist(
            @ApiParam(value = "Checklist to be created", required = true)
            @RequestBody input: ChecklistDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        val checklist: Checklist = checklistService.save(input, user)
        val siren = ChecklistSirenHTO(
                checklistName = checklist.checklistName,
                completionDate = checklist.completionDate.toString(),
                checklistId = checklist.checklistId,
                templateUsed = checklist.template?.templateId,
                template = if(checklist.template!=null) TemplateSirenHTO() else null,
                author = checklist.account!!.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Delete a checklist by id
     * @param id id of the checklist
     */
    @ApiOperation(value = "Delete a checklist")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully deleted the checklist"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to delete this checklist"),
            ApiResponse(code = 403, message = "Deleting the checklist you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The checklist you were trying to reach is not found")
    )
    @DeleteMapping(name = "deleteChecklistById", value = ["/{id}"])
    fun removeChecklistById(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable id : Int,
            user: ServletRequest) {
        checklistService.deleteById(id, user)
    }

    /**
     * Update a checklist
     * @param id id of the checklist
     * @param checklist inputModel via request body, has information to update a checklist
     */
    @ApiOperation(value = "Update a checklist")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully updated the checklist"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to update this checklist"),
            ApiResponse(code = 403, message = "Updating the checklist you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The checklist you were trying to reach is not found")
    )
    @PutMapping(name = "updateChecklist", value = ["/{checklistId}"])
    fun updateChecklist(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable checklistId : Int,
            @ApiParam(value = "Checklist to be updated", required = true)
            @RequestBody input : ChecklistDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        var checklist: Checklist = checklistService.getUserChecklist(checklistId, user)

        input.checklistId = checklist.checklistId
        if(input.checklistName.isBlank())
            input.checklistName = checklist.checklistName
        if(checklist.template != null)
            input.templateId = checklist.template!!.templateId
        input.completionDate = input.completionDate ?: checklist.completionDate
        input.checklistItems = checklist.checklistItems

        checklist = checklistService.save(input, user)
        val siren = ChecklistSirenHTO(
                checklistName = checklist.checklistName,
                checklistId = checklist.checklistId,
                author = checklist.account!!.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          ITEMS                                                       //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get checklist item with id
     * @param checklistItemId id of the checklist item
     * @return item from a checklist
     */
    @ApiOperation(value = "View a checklist item", response = ChecklistItem::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to view this item"),
            ApiResponse(code = 403, message = "Accessing the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The item you were trying to reach is not found")
    )
    @GetMapping(name = "getChecklistItemById", value = ["/{checklistId}/checklistItems/{checklistItemId}"])
    fun getChecklistItemById(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable checklistId: Int,
            @ApiParam(value = "Identifier for a checklist item", required = true)
            @PathVariable checklistItemId : Int,
            user: ServletRequest) : ResponseEntity<Entity> {

        val checklist: Checklist = checklistService.getUserChecklist(checklistId, user)
        val checklistItem: ChecklistItem = checklistItemService.getUserChecklistItem(checklist, checklistItemId)
        val siren = ChecklistItemSirenHTO(
                checklistItemName = checklistItem.checklistItemName,
                state = checklistItem.state,
                description = checklistItem.description,
                checklistItemId = checklistItemId,
                checklistId = checklistId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Get all checklist items
     * @return all items from a checklist
     */
    @ApiOperation(value = "View all checklist items", response = List::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the items"),
            ApiResponse(code = 401, message = "You are not authorized to view this items"),
            ApiResponse(code = 403, message = "Accessing the items you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The items you were trying to reach are not found")
    )
    @GetMapping(name = "getAllChecklistItems", value = ["/{checklistId}/checklistItems"])
    fun getAllChecklistItems(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable checklistId: Int,
            user: ServletRequest) : ResponseEntity<Entity> {
        val checklist: Checklist = checklistService.getUserChecklist(checklistId, user)

        val siren = CollectionChecklistItemSirenHTO(
                checklist.checklistItems!!.map {
                    ChecklistItemSirenHTO(
                            checklistItemName = it.checklistItemName,
                            state = it.state,
                            description = it.description,
                            checklistItemId = it.checklistItemId,
                            checklistId = checklistId
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Create new checklist item
     * @param checklistItem inputModel via request body, has information to create a checklist item
     */
    @ApiOperation(value = "Add a item in a checklist")
    @ApiResponses(
            ApiResponse(code = 201, message = "Successfully created the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to create this item"),
            ApiResponse(code = 403, message = "Creating the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The checklist you were trying to insert the item is not found")
    )
    @PostMapping(name = "postSingleChecklistItem", value = ["/{checklistId}/checklistItems"])
    fun addChecklistItem(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable checklistId : Int,
            @ApiParam(value = "Item to be added to a checklist", required = true)
            @RequestBody input : ChecklistItemDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        val c: Checklist = checklistService.getUserChecklist(checklistId, user)
        val checklistItem: ChecklistItem = checklistItemService.save(c, input)
        val siren = ChecklistItemSirenHTO(
                checklistItemName = checklistItem.checklistItemName,
                state = checklistItem.state,
                description = checklistItem.description,
                checklistItemId = checklistItem.checklistItemId,
                checklistId = checklistId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Delete a checklist item by id
     * @param checklistItemId id of the checklist items
     */
    @ApiOperation(value = "Delete a item from a checklist")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully deleted the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to delete this item"),
            ApiResponse(code = 403, message = "Deleting the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The item you were trying to reach is not found")
    )
    @DeleteMapping(name = "deleteChecklistItemById", value = ["/{checklistId}/checklistItems/{checklistItemId}"])
    fun removeChecklistItemById(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable checklistId : Int,
            @ApiParam(value = "Identifier for a checklist item", required = true)
            @PathVariable checklistItemId : Int,
            user: ServletRequest) {
        val c: Checklist = checklistService.getUserChecklist(checklistId, user)
        checklistItemService.deleteById(c.checklistId)
    }

    /**
     * Update checklist item
     * @param checklistId id of the checklist
     * @param checklistItemId id of the checklist item
     * @param checklistItem checklist item inputModel via request body, has information to update checklist item
     */
    @ApiOperation(value = "Update item from a checklist")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully updated the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to update this item"),
            ApiResponse(code = 403, message = "Updating the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The checklist you were trying to insert the item is not found")
    )
    @PutMapping(name = "updateChecklistItem", value = ["/{checklistId}/checklistItems/{checklistItemId}"])
    fun updateChecklistItem(
            @ApiParam(value = "Identifier for a checklist", required = true)
            @PathVariable checklistId : Int,
            @ApiParam(value = "Identifier for a checklist item", required = true)
            @PathVariable checklistItemId : Int,
            @ApiParam(value = "Item to be updated for a checklist", required = true)
            @RequestBody input : ChecklistItemDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        val checklist: Checklist = checklistService.getUserChecklist(checklistId, user)
        var checklistItem: ChecklistItem = checklistItemService.getUserChecklistItem(checklist, checklistItemId)

        input.checklistItemId = checklistItem.checklistItemId
        if(input.checklistItemName.isBlank())
            input.checklistItemName = checklistItem.checklistItemName
        if(input.description.isBlank())
            input.description = checklistItem.description
        if(input.state.isBlank())
            input.state = checklistItem.state

        checklistItem = checklistItemService.save(checklist, input)
        val siren = ChecklistItemSirenHTO(
                checklistItemName = checklistItem.checklistItemName,
                description = checklistItem.description,
                checklistItemId = checklistItem.checklistItemId,
                checklistId = checklistId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

}