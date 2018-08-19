package isel.leic.daw.dawProject.model.outputModel.checklist.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl.*

@Siren4JEntity(
        name = "ChecklistItem",
        suppressClassProperty = true,
        uri = "/checklists/{checklistId}/checklistItems/{checklistItemId}",
        links = [
            Siren4JLink(rel = ["next"], href = "/checklists/{checklistId}/checklistItems/{checklistItemId}/next"),
            Siren4JLink(rel = ["prev"], href = "/checklists/{checklistId}/checklistItems/{checklistItemId}/prev")
        ],
        actions = [
            Siren4JAction(
                name = "add-ChecklistItem", method = Method.PUT,
                href = "/checklists/{checklistId}/checklistItems/{checklistItemId}",
                type = "application/json",
                fields = [
                    Siren4JActionField(name = "checklistItemName", type = "text", required = true, maxLength = 128),
                    Siren4JActionField(name = "state", type = "text", required = true, maxLength = 128),
                    Siren4JActionField(name = "description", type = "text", required = false, maxLength = 128)
                ]
        ),
            Siren4JAction(
                    name = "delete-ChecklistItem", method = Method.DELETE,
                    href = "/checklists/{checklistId}/checklistItems/{checklistItemId}",
                    type = ""
            )
        ]
)
class ChecklistItemSirenHTO(
        val checklistItemName: String = "",

        val state: String = "",

        val description: String = "",

        @Siren4JSubEntity(rel = ["checklist"], uri = "/checklists/{parent.checklistId}", embeddedLink = true)
        var checklist: ChecklistSirenHTO = ChecklistSirenHTO(),

        val checklistItemId: Int = -1,

        val checklistId: Int = -1
) {
}