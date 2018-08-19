package isel.leic.daw.dawProject.model.outputModel.checklist.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl.*
import isel.leic.daw.dawProject.model.outputModel.template.single.TemplateSirenHTO
import isel.leic.daw.dawProject.model.outputModel.account.AccountSirenHTO
import isel.leic.daw.dawProject.model.outputModel.checklist.collection.CollectionChecklistItemSirenHTO

import java.time.LocalDate

@Siren4JInclude(Siren4JInclude.Include.NON_EMPTY)
@Siren4JEntity(
        name = "checklist",
        suppressClassProperty = true,
        uri = "/checklists/{checklistId}",
        links = [
            Siren4JLink(rel = ["next"], href = "/checklists/{checklistId}/next"),
            Siren4JLink(rel = ["prev"], href = "/checklists/{checklistId}/prev")
        ],
        actions = [
            Siren4JAction(
                name = "update-checklist", method = Method.PUT,
                href = "/checklists/{checklistId}",
                type = "application/json",
                fields = [
                    Siren4JActionField(name = "checklistName", type = "text", required = true),
                    Siren4JActionField(name = "completionDate", type = "text", required = false, maxLength = 128)
                ]
            ),
            Siren4JAction(
                    name = "delete-checklist", method = Method.DELETE,
                    href = "/checklists/{checklistId}"
            )
        ]
)
class ChecklistSirenHTO(
        val checklistName: String = "",

        val completionDate: String? = null,

        val author: String = "",

        var templateUsed: Int? = null,

        @Siren4JSubEntity(rel = ["template"], uri = "/templates/{parent.templateUsed}", embeddedLink = true)
        var template: TemplateSirenHTO? = null,

        @Siren4JSubEntity(rel = ["checklist-items"], uri = "/checklists/{parent.checklistId}/checklistItems", embeddedLink = true)
        val checklistItems: CollectionChecklistItemSirenHTO = CollectionChecklistItemSirenHTO(),

        @Siren4JSubEntity(rel = ["account"], uri = "/accounts/{parent.author}", embeddedLink = true)
        var account: AccountSirenHTO = AccountSirenHTO(),

        val checklistId: Int = -1
)