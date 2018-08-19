package isel.leic.daw.dawProject.model.outputModel.template.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl.*

@Siren4JEntity(
        name = "templateItem",
        suppressClassProperty = true,
        uri = "/templates/{templateId}/templateItems/{templateItemId}",
        links = [
            Siren4JLink(rel = ["next"], href = "/templates/{templateId}/templateItems/{templateItemId}/next"),
            Siren4JLink(rel = ["prev"], href = "/templates/{templateId}/templateItems/{templateItemId}/prev")
        ],
        actions = [
            Siren4JAction(
                name = "update-TemplateItem", method = Method.PUT,
                href = "/templates/{templateId}/templateItems/{templateItemId}",
                type = "application/json",
                fields = [
                    Siren4JActionField(name = "templateItemName", type = "text", required = true, maxLength = 128),
                    Siren4JActionField(name = "description", type = "text", required = false, maxLength = 128)
                ]
            ),
            Siren4JAction(
                    name = "delete-TemplateItem", method = Method.DELETE,
                    href = "/templates/{templateId}/templateItems/{templateItemId}"
            )
        ]
)
class TemplateItemSirenHTO(
        val templateItemName: String = "",

        val description: String = "",

        @Siren4JSubEntity(rel = ["template"], uri = "/templates/{parent.templateId}", embeddedLink = true)
        var template: TemplateSirenHTO = TemplateSirenHTO(),

        val templateItemId: Int = -1,

        val templateId: Int = -1
)