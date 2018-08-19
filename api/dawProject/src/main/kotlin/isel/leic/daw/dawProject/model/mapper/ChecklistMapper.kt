package isel.leic.daw.dawProject.model.mapper

import isel.leic.daw.dawProject.model.inputModel.ChecklistDTO
import isel.leic.daw.dawProject.model.entity.Checklist

class ChecklistMapper {
    companion object Mapper {
        fun map(c: Checklist) =
                ChecklistDTO(c.checklistName, c.completionDate)

        fun mapAll(cs : List<Checklist>) =
                cs.map { c -> map(c) }
    }
}