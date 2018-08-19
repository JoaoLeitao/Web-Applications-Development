package isel.leic.daw.dawProject.controller

import com.google.code.siren4j.Siren4J
import isel.leic.daw.dawProject.model.entity.TemplateItem
import isel.leic.daw.dawProject.model.inputModel.TemplateDTO
import isel.leic.daw.dawProject.model.inputModel.TemplateItemDTO
import isel.leic.daw.dawProject.model.outputModel.template.single.TemplateItemSirenHTO
import isel.leic.daw.dawProject.service.TemplateItemService
import isel.leic.daw.dawProject.service.TemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.*
import isel.leic.daw.dawProject.model.entity.Template
import isel.leic.daw.dawProject.model.outputModel.template.collection.CollectionTemplateItemSirenHTO
import isel.leic.daw.dawProject.model.outputModel.template.collection.CollectionTemplateSirenHTO
import isel.leic.daw.dawProject.model.outputModel.template.single.TemplateSirenHTO
import org.springframework.http.ResponseEntity
import javax.servlet.ServletRequest

@RestController
@Api(description = "Operations about templates")
@RequestMapping(value = ["/templates"], produces = [(Siren4J.JSON_MEDIATYPE)])
class TemplateRestController{

    @Autowired
    lateinit var templateService : TemplateService

    @Autowired
    lateinit var templateItemService : TemplateItemService

    /**
     * Get template with id
     * @param id id of the template
     * @return template with id passed as parameter
     */
    @ApiOperation(value = "View a template", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the template"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to view this template"),
            ApiResponse(code = 403, message = "Accessing the template you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The template you were trying to reach is not found")
    )
    @GetMapping(name = "getTemplateById", value = ["/{templateId}"])
    fun getTemplate(
            @ApiParam(value = "Identifier for a template", required = true)
            @PathVariable templateId: Int,
            user: ServletRequest) : ResponseEntity<Entity> {
        val template: Template = templateService.getUserTemplate(templateId, user)
        val siren = TemplateSirenHTO(
                templateName = template.templateName,
                templateId = templateId,
                author = template.account!!.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Get all templates
     * @return all templates
     */
    @ApiOperation(value = "View all the templates", response = List::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the templates"),
            ApiResponse(code = 401, message = "You are not authorized to view this templates")
    )
    @GetMapping(name = "getAllTemplates")
    fun getAllTemplates(user: ServletRequest) : ResponseEntity<Entity> {
        val list: List<Template> = templateService.getUserTemplates(user)
        val siren = CollectionTemplateSirenHTO(
                list.map {
                    TemplateSirenHTO(
                            templateName = it.templateName,
                            templateId = it.templateId,
                            author = it.account!!.sub
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))

    }

    /**
     * Create new template
     * @param template inputModel via request body, has information to create a template
     */
    @ApiOperation(value = "Create a new account")
    @ApiResponses(
            ApiResponse(code = 201, message = "Successfully created the template")
    )
    @PostMapping(name = "postSingleTemplate")
    fun addTemplate(
            @ApiParam(value = "Template to be created", required = true)
            @RequestBody input: TemplateDTO,
            user: ServletRequest) : ResponseEntity<Entity> {

        val template: Template = templateService.save(input, user)
        val siren = TemplateSirenHTO(
                templateName = template.templateName,
                templateId = template.templateId,
                author = template.account!!.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Delete a template by id
     * @param id id of the template
     */
    @ApiOperation(value = "Delete an account")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully deleted the template"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to delete this template"),
            ApiResponse(code = 403, message = "Deleting the template you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The template you were trying to reach is not found")
    )
    @DeleteMapping(name = "deleteTemplateById", value = ["/{id}"])
    fun removeTemplateById(
            @ApiParam(value = "Identifier for a template", required = true)
            @PathVariable id : Int,
            user: ServletRequest) {
        templateService.deleteById(id, user)
    }

    /**
     * Update a template
     * @param id id of the template
     * @param template inputModel via request body, has information to update a template
     */
    @ApiOperation(value = "Update a template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully updated the template"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to update this template"),
            ApiResponse(code = 403, message = "Updating the template you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The template you were trying to reach is not found")
    )
    @PutMapping(name = "updateTemplate", value = ["/{templateId}"])
    fun updateTemplate(
            @ApiParam(value = "Identifier for a template", required = true)
            @PathVariable templateId : Int,
            @ApiParam(value = "Template to be updated", required = true)
            @RequestBody input : TemplateDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        var template: Template = templateService.getUserTemplate(templateId, user)

        input.templateId = template.templateId

        if(input.templateName.isBlank())
            input.templateName = template.templateName
        input.templateItems = template.templateItems
        input.checklists = template.checklists

        template = templateService.save(input, user)
        val siren = TemplateSirenHTO(
                templateName = template.templateName,
                templateId = template.templateId,
                author = template.account!!.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          ITEMS                                                       //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get template item with id
     * @param templateId id of the template
     * @param templateItemId id of the template item
     * @return template item siren representation
     */
    @ApiOperation(value = "View a template item", response = ResponseEntity::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to view this item"),
            ApiResponse(code = 403, message = "Accessing the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The item you were trying to reach is not found")
    )
    @GetMapping(name = "getTemplateItemById", value = ["/{templateId}/templateItems/{templateItemId}"])
    fun getTemplateItem(
            @ApiParam(value = "Identifier for a template", required = true)
            @PathVariable templateId : Int,
            @ApiParam(value = "Identifier for a template item", required = true)
            @PathVariable templateItemId : Int,
            user: ServletRequest) : ResponseEntity<Entity> {

        val template: Template = templateService.getUserTemplate(templateId, user)
        val templateItem: TemplateItem = templateItemService.getUserTemplateItem(template, templateItemId)
        val siren = TemplateItemSirenHTO(
                templateItemName = templateItem.templateItemName,
                description = templateItem.description,
                templateItemId = templateItemId,
                templateId = templateId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Get all template items
     * @param templateId id of the template
     * @return list of template items
     */
    @ApiOperation(value = "View all template items", response = List::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the items"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to view this items"),
            ApiResponse(code = 403, message = "Accessing the items you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The items you were trying to reach are not found")
    )
    @GetMapping(name = "getAllTemplateItems", value = ["/{templateId}/templateItems"])
    fun getAllTemplateItems(
            @ApiParam(value = "Identifier for a template item", required = true)
            @PathVariable templateId : Int,
            user: ServletRequest) : ResponseEntity<Entity> {

        val template: Template = templateService.getUserTemplate(templateId, user)

        val siren = CollectionTemplateItemSirenHTO(
                template.templateItems!!.map {
                    TemplateItemSirenHTO(
                            templateItemName = it.templateItemName,
                            description = it.description,
                            templateItemId = it.templateItemId,
                            templateId = templateId
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Create new template item
     * @param templateId id of the template
     * @param templateItem inputModel via request body, has information to create a template item
     */
    @ApiOperation(value = "Add an item to a template")
    @ApiResponses(
            ApiResponse(code = 201, message = "Successfully created the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to create this item"),
            ApiResponse(code = 403, message = "Creating the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The template you were trying to insert the item is not found")
    )
    @PostMapping(name = "postSingleTemplateItem", value = ["/{templateId}/templateItems"])
    fun addTemplateItem(
            @ApiParam(value = "Identifier for a template item", required = true)
            @PathVariable templateId : Int,
            @ApiParam(value = "Item to be added to a template", required = true)
            @RequestBody input : TemplateItemDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        val t: Template = templateService.getUserTemplate(templateId, user)
        val templateItem: TemplateItem = templateItemService.save(t, input)
        val siren = TemplateItemSirenHTO(
                templateItemName = templateItem.templateItemName,
                description = templateItem.description,
                templateItemId = templateItem.templateItemId,
                templateId = templateId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Delete a template item by id
     * @param templateId id of the template
     * @param templateItemId id of the template
     */
    @ApiOperation(value = "Delete a item from a template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully deleted the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to delete this item"),
            ApiResponse(code = 403, message = "Deleting the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The item you were trying to reach is not found")
    )
    @DeleteMapping(name = "deleteTemplateItemById", value = ["/{templateId}/templateItems/{templateItemId}"])
    fun removeTemplateItemById(
            @ApiParam(value = "Identifier for a template", required = true)
            @PathVariable templateId: Int,
            @ApiParam(value = "Identifier for a template item", required = true)
            @PathVariable templateItemId: Int,
            user: ServletRequest) {
        val t: Template = templateService.getUserTemplate(templateId, user)
        templateItemService.deleteById(t.templateId)
    }

    /**
     * Update template item
     * @param templateId id of the template
     * @param templateItemId id of the template item
     * @param templateItem item from a template
     */
    @ApiOperation(value = "Update item from a template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully updated the item"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to update this item"),
            ApiResponse(code = 403, message = "Updating the item you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The template you were trying to insert the item is not found")
    )
    @PutMapping(name = "updateTemplateItem", value = ["/{templateId}/templateItems/{templateItemId}"])
    fun updateTemplateItem(
            @ApiParam(value = "Identifier for a template", required = true)
            @PathVariable templateId: Int,
            @ApiParam(value = "Identifier for a template item", required = true)
            @PathVariable templateItemId : Int,
            @ApiParam(value = "Item to be updated to a template", required = true)
            @RequestBody input : TemplateItemDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        val template: Template = templateService.getUserTemplate(templateId, user)
        var templateItem: TemplateItem = templateItemService.getUserTemplateItem(template, templateItemId)

        input.templateItemId = templateItem.templateItemId
        if(input.templateItemName.isBlank())
            input.templateItemName = templateItem.templateItemName
        if(input.description.isBlank())
            input.description = templateItem.description

        input.templateItemId = templateItem.templateItemId
        templateItem = templateItemService.save(template, input)
        val siren = TemplateItemSirenHTO(
                templateItemName = templateItem.templateItemName,
                description = templateItem.description,
                templateItemId = templateItem.templateItemId,
                templateId = templateId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

}