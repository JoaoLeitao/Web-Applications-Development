package isel.leic.daw.dawProject.model.mapper

import isel.leic.daw.dawProject.model.inputModel.ChecklistItemDTO
import isel.leic.daw.dawProject.model.entity.ChecklistItem

class ChecklistItemMapper {
    companion object Mapper {
        fun map(ci: ChecklistItem) =
                ChecklistItemDTO(ci.checklistItemName)

        fun mapAll(cis : List<ChecklistItem>) =
                cis.map { ci -> map(ci) }
    }
}