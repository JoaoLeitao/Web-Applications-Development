package isel.leic.daw.dawProject.model.outputModel.template.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl.*
import isel.leic.daw.dawProject.model.outputModel.account.AccountSirenHTO
import isel.leic.daw.dawProject.model.outputModel.checklist.single.ChecklistSirenHTO
import isel.leic.daw.dawProject.model.outputModel.checklist.collection.CollectionChecklistSirenHTO
import isel.leic.daw.dawProject.model.outputModel.template.collection.CollectionTemplateItemSirenHTO

@Siren4JEntity(
        name = "template",
        suppressClassProperty = true,
        uri = "/templates/{templateId}",
        links = [
            Siren4JLink(rel = ["next"], href = "/templates/{templateId}/next"),
            Siren4JLink(rel = ["prev"], href = "/templates/{templateId}/prev")
        ],
        actions = [
            Siren4JAction(
                name = "update-Template", method = Method.PUT,
                href = "/templates/{templateId}",
                type = "application/json",
                fields = [
                    Siren4JActionField(name = "templateItemName", type = "text", required = true, maxLength = 128)
                ]
            ),
            Siren4JAction(
                    name = "delete-Template", method = Method.DELETE,
                    href = "/templates/{templateId}"
            )
        ]
)
class TemplateSirenHTO(
        var templateName: String = "",

        val author: String = "",

        @Siren4JSubEntity(rel = ["checklists"], uri = "/checklists", embeddedLink = true)
        var checklists: CollectionChecklistSirenHTO = CollectionChecklistSirenHTO(),

        @Siren4JSubEntity(rel = ["template-items"], uri = "/templates/{parent.templateId}/templateItems", embeddedLink = true)
        val templateItems: CollectionTemplateItemSirenHTO = CollectionTemplateItemSirenHTO(),

        @Siren4JSubEntity(rel = ["account"], uri = "/accounts/{parent.author}", embeddedLink = true)
        var account: AccountSirenHTO = AccountSirenHTO(),

        val templateId: Int = -1
) {
}