package isel.leic.daw.dawProject.model.inputModel

data class ChecklistItemDTO(
        var checklistItemName: String = "",
        var description: String = "",
        var state: String = "",
        var checklistItemId: Int = -1
)