package isel.leic.daw.dawProject.model.mapper

import isel.leic.daw.dawProject.model.inputModel.TemplateItemDTO
import isel.leic.daw.dawProject.model.entity.TemplateItem

class TemplateItemMapper {
    companion object Mapper {
        fun map(ti: TemplateItem) =
                TemplateItemDTO(ti.templateItemName)

        fun mapAll(tis : List<TemplateItem>) =
                tis.map { ti -> map(ti) }
    }
}