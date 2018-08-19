package isel.leic.daw.dawProject.model.inputModel

import isel.leic.daw.dawProject.model.entity.ChecklistItem
import java.time.LocalDate

data class ChecklistDTO(
        var checklistName: String = "",
        var completionDate: LocalDate? = null,
        var checklistItems: MutableSet<ChecklistItem>? = null,
        var templateId: Int = -1,
        var checklistId: Int = -1
)